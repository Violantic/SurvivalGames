package me.violantic.sg.game.listener;

import me.violantic.sg.SurvivalGames;
import me.violantic.sg.game.event.GameEndEvent;
import me.violantic.sg.game.util.LocationUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by Ethan on 11/3/2016.
 */
public class PlayerListener implements Listener {

    private SurvivalGames instance;

    public PlayerListener(SurvivalGames instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        if(!instance.getVerifiedPlayers().contains(event.getEntity().getUniqueId())) return;

        if(event.getEntity().getKiller() != null) {
            Player player = event.getEntity().getKiller();
            instance.getMysql().setStat(player.getUniqueId().toString(), "kills", 1);
            instance.getMysql().setStat(player.getUniqueId().toString(), "points", 2);
            event.getEntity().sendMessage(instance.getPrefix() + ChatColor.GREEN + "[Stats] +1 Kills");
            event.getEntity().sendMessage(instance.getPrefix() + ChatColor.GREEN + "[Stats] +2 Rating");
            event.getEntity().playSound(event.getEntity().getLocation(), Sound.BLOCK_NOTE_PLING, 1, 1);
        }

        instance.getMysql().setStat(event.getEntity().getUniqueId().toString(), "deaths", 1);
        event.getEntity().sendMessage(instance.getPrefix() + ChatColor.RED + "[Stats] +1 Deaths");
        event.getEntity().playSound(event.getEntity().getLocation(), Sound.BLOCK_NOTE_BASEDRUM, 1, 1);

        event.setDeathMessage(null);
        event.getEntity().getWorld().strikeLightningEffect(event.getEntity().getLocation());
        Bukkit.broadcastMessage(instance.getPrefix() + event.getEntity().getName() + "[Stats] is no longer alive");
        try {
            instance.getVerifiedPlayers().remove(event.getEntity().getUniqueId());

            if(instance.getVerifiedPlayers().size() == 1) {
                Bukkit.getServer().getPluginManager().callEvent(new GameEndEvent(instance, Bukkit.getPlayer(instance.getWinner()).getName()));
                return;
            }

            Bukkit.broadcastMessage(instance.getPrefix() + "Players left: (" + instance.getVerifiedPlayers().size() + "/" + instance.maximumPlayers() + ")");
        } catch (Exception e) {
            System.out.println(event.getEntity().getName() + " was never a verified player in SurvivalGames!");
        }
    }

    @EventHandler
    public void onLogin(final PlayerLoginEvent event) {
        if(instance.getState().getName().equalsIgnoreCase("waiting")) {
            event.getPlayer().sendMessage(instance.GAME_LOBBY_JOIN_SUCCESS());
            if(!instance.getVerifiedPlayers().contains(event.getPlayer().getUniqueId())) {
                instance.getVerifiedPlayers().add(event.getPlayer().getUniqueId());
                new BukkitRunnable() {
                    public void run() {
                        event.getPlayer().getInventory().clear();
                        event.getPlayer().teleport(LocationUtil.getLocation(event.getPlayer().getWorld().getName(), instance.getConfig().getString("center")));
                    }
                }.runTaskLater(instance, 20l);
            }
        } else {
            event.getPlayer().kickPlayer(instance.ERROR_GAME_IN_PROGRESS());
        }
    }

    @EventHandler
    public void onLogout(PlayerQuitEvent event) {
        if(instance.getVerifiedPlayers().contains(event.getPlayer().getUniqueId())) {
            instance.getVerifiedPlayers().remove(event.getPlayer().getUniqueId());
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        event.setCancelled(!instance.getState().isCanMove());
    }

    @EventHandler
    public void onPVP(EntityDamageByEntityEvent event) {
        if(event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            if(instance.getState().isCanPVP()) {
                event.setCancelled(false);
            } else {
                event.setCancelled(true);
            }
        } else if (event.getEntity() instanceof Player && event.getDamager() != null) {
            if(instance.getState().isCanPVE()) {
                event.setCancelled(false);
            } else {
                event.setCancelled(true);
            }
        } else if (event.getDamager() instanceof Player) {
            if(instance.getState().isCanPVE()) {
                event.setCancelled(false);
            } else {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        if(!instance.getState().isCanBreak()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        if(!instance.getState().isCanPlace()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onSpeak(AsyncPlayerChatEvent event) {
        if(!instance.getState().isCanTalk()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onOpen(PlayerInteractEvent event) {
        // Opening loot //
        if(event.getAction() == Action.RIGHT_CLICK_BLOCK && (event.getClickedBlock().getType() == Material.SPONGE || event.getClickedBlock().getType() == Material.ENDER_CHEST)) {
            if (instance.getCrateGenerator().tier1.contains(event.getClickedBlock())) {
                instance.getCrateGenerator().tier1.remove(event.getClickedBlock());
                instance.getMysql().setStat(event.getPlayer().getUniqueId().toString(), "chests_opened", 1);
                event.getPlayer().sendMessage(instance.getPrefix() + ChatColor.GREEN + "[Stats] +1 Chests Opened");
            } else if (instance.getCrateGenerator().tier2.contains(event.getClickedBlock())) {
                instance.getCrateGenerator().tier2.remove(event.getClickedBlock());
                instance.getMysql().setStat(event.getPlayer().getUniqueId().toString(), "chests_opened", 1);
                event.getPlayer().sendMessage(instance.getPrefix() + ChatColor.GREEN + "[Stats] +1 Chests Opened");
            }
        }

        // Voting //
        if (event.getPlayer().getItemInHand().getType() == Material.PAPER) {
            if(!instance.getState().getName().equalsIgnoreCase("waiting")) return;

            ItemMeta meta = event.getItem().getItemMeta();
            String namePlus = ChatColor.stripColor(meta.getDisplayName());
            String name = namePlus.replace("VOTE: ", "");
            instance.getGameMapVoter().addVote(event.getPlayer().getUniqueId(), name);

            event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.BLOCK_NOTE_PLING, 1, 1);
            event.getPlayer().sendMessage(instance.getPrefix() + "You have voted for: " + ChatColor.YELLOW + name);
        }
        if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK) || !event.getAction().equals(Action.RIGHT_CLICK_AIR))
            return;

        if (!instance.getState().isCanOpen()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        event.setCancelled((instance.getState().getName().equalsIgnoreCase("waiting")));
    }
}
