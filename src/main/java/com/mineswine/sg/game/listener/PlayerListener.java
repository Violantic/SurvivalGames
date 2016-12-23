package com.mineswine.sg.game.listener;

import com.mineswine.sg.SurvivalGames;
import com.mineswine.sg.game.event.GameEndEvent;
import com.mineswine.sg.game.lang.Messages;
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
            player.sendMessage(instance.getPrefix() + Messages.EN_PLAYER_KILL.replace("{target}", event.getEntity().getName()));
            player.sendMessage(instance.getPrefix() + Messages.EN_RATING_CHANGE.replace("{rating}", "2"));
            player.playSound(event.getEntity().getLocation(), Sound.BLOCK_NOTE_PLING, 1, 1);
        }

        instance.getMysql().setStat(event.getEntity().getUniqueId().toString(), "deaths", 1);
        event.getEntity().sendMessage(instance.getPrefix() + Messages.EN_PLAYER_DIED.replace("{reason}", event.getDeathMessage()));
        event.getEntity().playSound(event.getEntity().getLocation(), Sound.BLOCK_NOTE_BASEDRUM, 1, 1);

        event.setDeathMessage(null);
        event.getEntity().getWorld().strikeLightningEffect(event.getEntity().getLocation());
        Bukkit.broadcastMessage(instance.getPrefix() + Messages.EN_DEATH_BROADCAST.replace("{player}", event.getEntity().getName()));
        try {
            instance.getVerifiedPlayers().remove(event.getEntity().getUniqueId());

            if(instance.getVerifiedPlayers().size() == 1) {
                Bukkit.getServer().getPluginManager().callEvent(new GameEndEvent(instance, Bukkit.getPlayer(instance.getWinner()).getName()));
                return;
            } else if(instance.getVerifiedPlayers().size() == 0) {
                Bukkit.getServer().getPluginManager().callEvent(new GameEndEvent(instance, null));
            }

            Bukkit.broadcastMessage(instance.getPrefix() + Messages.EN_PLAYERS_LEFT.replace("{remaining}", SurvivalGames.getInstance().getVerifiedPlayers().size() + "").replace("{maximum}", SurvivalGames.getInstance().maximumPlayers() + ""));
        } catch (Exception e) {
            System.out.println(event.getEntity().getName() + " was never a verified player in SurvivalGames!");
        }
    }

    @EventHandler
    public void onLogin(final PlayerLoginEvent event) {
        if(instance.getState().getName().equalsIgnoreCase("waiting")) {
            event.getPlayer().sendMessage(instance.getPrefix() + Messages.EN_GAME_JOIN);
            if(!instance.getVerifiedPlayers().contains(event.getPlayer().getUniqueId())) {
                instance.getVerifiedPlayers().add(event.getPlayer().getUniqueId());
                new BukkitRunnable() {
                    public void run() {
                        event.getPlayer().getInventory().clear();
                        //event.getPlayer().teleport(LocationUtil.getLocation("world", instance.getConfig().getString("center")));
                    }
                }.runTaskLater(instance, 20l);
            }
        } else {
            event.getPlayer().kickPlayer(instance.getPrefix() + Messages.EN_GAME_STARTED);
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
        if(instance.getState().isCanMove()) return;
        event.setCancelled(((event.getFrom().getBlockX() != event.getTo().getBlockX()) || (event.getFrom().getBlockZ() != event.getTo().getBlockZ())));
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
                event.getPlayer().sendMessage(instance.getPrefix() + Messages.EN_TIER_I_OPEN);
            } else if (instance.getCrateGenerator().tier2.contains(event.getClickedBlock())) {
                instance.getCrateGenerator().tier2.remove(event.getClickedBlock());
                instance.getMysql().setStat(event.getPlayer().getUniqueId().toString(), "chests_opened", 1);
                event.getPlayer().sendMessage(instance.getPrefix() + Messages.EN_TIER_II_OPEN);
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
            event.getPlayer().sendMessage(instance.getPrefix() + Messages.EN_MAP_VOTE.replace("{map}", name));
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
