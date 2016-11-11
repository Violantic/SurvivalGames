package me.violantic.sg;

import me.violantic.sg.game.Game;
import me.violantic.sg.game.GameState;
import me.violantic.sg.game.Map;
import me.violantic.sg.game.util.LocationUtil;
import me.violantic.sg.handler.GameHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by Ethan on 11/3/2016.
 */
public class SurvivalGames extends JavaPlugin implements Game {

    private static SurvivalGames instance;

    private GameState state;
    private GameHandler handler;

    private Map lobby;

    @Override
    public void onEnable() {
        instance = this;

        lobby = initializeMap();

        setState(new GameState("waiting"));
        handler = new GameHandler();

        getServer().getScheduler().runTaskTimer(this, getHandler(), 0l, 20l);
    }

    @Override
    public void onDisable() {

    }

    public static SurvivalGames getInstance() {
        return instance;
    }

    public int minimumPlayers() {
        return 18;
    }

    public int maximumPlayers() {
        return 24;
    }

    public Map getMap() {
        return null;
    }

    public Location getLobby() {
        return LocationUtil.getLocation(lobby.getWorld(), getConfig().getString("lobby"));
    }

    public Map initializeMap() {
        return new Map("lobby", new String[]{"Mineswine Build Team"}, null);
    }

    public GameState getState() {
        return state;
    }

    public void setState(GameState state) {
        this.state = state;
    }

    public Runnable getHandler() {
        return null;
    }

    public String getPrefix() {
        return ChatColor.GREEN + "Survival" + ChatColor.DARK_GREEN + "Games " + ChatColor.GRAY ;
    }

    public String ERROR_GAME_IN_PROGRESS() {
        return getPrefix() + ChatColor.RED + "You could not join the current game, it is already in play!";
    }

    public String GAME_LOBBY_JOIN_SUCCESS() {
        return getPrefix() + "You have joined the lobby! Currently waiting for " + (minimumPlayers() - Bukkit.getOnlinePlayers().size()) + " players...";
    }

}
