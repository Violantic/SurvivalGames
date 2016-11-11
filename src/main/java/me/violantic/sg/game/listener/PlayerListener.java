package me.violantic.sg.game.listener;

import me.violantic.sg.SurvivalGames;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;

/**
 * Created by Ethan on 11/3/2016.
 */
public class PlayerListener {

    private SurvivalGames instance;

    public PlayerListener(SurvivalGames instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        event.setDeathMessage(null);
        event.getEntity().getWorld().strikeLightningEffect(event.getEntity().getLocation());
        try {
            instance.getVerifiedPlayers().remove(event.getEntity().getUniqueId());
        } catch (Exception e) {
            System.out.println(event.getEntity().getName() + " was never a verified player in SurvivalGames!");
        }
    }

    @EventHandler
    public void onLogin(PlayerLoginEvent event) {
        if(instance.getState().getName().equalsIgnoreCase("lobby")) {
            event.getPlayer().teleport(instance.getLobby());
            event.getPlayer().sendMessage(instance.GAME_LOBBY_JOIN_SUCCESS());
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
                event.setCancelled(true);
            }
        } else if (event.getEntity() instanceof Player && event.getDamager() != null) {
            if(instance.getState().isCanPVE()) {
                event.setCancelled(true);
            }
        } else if (event.getDamager() instanceof Player) {
            if(instance.getState().isCanPVE()) {
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
        if(!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
        if(!event.getClickedBlock().getType().equals(Material.CHEST) || !event.getClickedBlock().getType().equals(Material.ENDER_CHEST)) return;

        if(!instance.getState().isCanOpen()) {
            event.setCancelled(true);
        }
    }
}
