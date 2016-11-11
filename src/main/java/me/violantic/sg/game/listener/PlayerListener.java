package me.violantic.sg.game.listener;

import me.violantic.sg.SurvivalGames;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerLoginEvent;

/**
 * Created by Ethan on 11/3/2016.
 */
public class PlayerListener {

    private SurvivalGames instance;

    public PlayerListener(SurvivalGames instance) {
        this.instance = instance;
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
}
