package me.violantic.sg.game.listener;

import me.violantic.sg.SurvivalGames;
import me.violantic.sg.game.event.GameEndEvent;
import me.violantic.sg.game.event.GameStartEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.UUID;

/**
 * Created by Ethan on 11/10/2016.
 */
public class GameListener implements Listener {

    private SurvivalGames instance;

    public GameListener(SurvivalGames instance) {
        this.instance = instance;
    }

    public SurvivalGames getInstance() {
        return instance;
    }

    @EventHandler
    public void onStart(GameStartEvent event) {
        for(UUID uuid : SurvivalGames.getInstance().getVerifiedPlayers()) {
            Player player = Bukkit.getPlayer(uuid);
            int index = SurvivalGames.getInstance().getVerifiedPlayers().indexOf(uuid);
            player.teleport(SurvivalGames.getInstance().getStartingLocations().get(index));
        }
    }

    @EventHandler
    public void onEnd(GameEndEvent event) {
        for(Player player : Bukkit.getOnlinePlayers()) {
            //player.teleport(instance.getLobby());
            if(event.getWinner().equalsIgnoreCase(player.getName())) {
                instance.getMysql().setStat(player.getUniqueId().toString(), "points", 25);
                instance.getMysql().setStat(player.getUniqueId().toString(), "games", 1);
                player.sendMessage(instance.getPrefix() + ChatColor.GREEN + "+25 Rating!");
                player.sendMessage(instance.getPrefix() + ChatColor.LIGHT_PURPLE + "Good game!");
            } else {
                instance.getMysql().setStat(player.getUniqueId().toString(), "points", 5);
                instance.getMysql().setStat(player.getUniqueId().toString(), "games", 1);
                player.sendMessage(instance.getPrefix() + ChatColor.LIGHT_PURPLE + "Good game!");
            }
        }

        instance.setEnable(false);
    }
}
