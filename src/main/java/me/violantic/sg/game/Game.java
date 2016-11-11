package me.violantic.sg.game;

import org.bukkit.Location;

/**
 * Created by Ethan on 11/3/2016.
 */
public interface Game {

    int minimumPlayers();

    int maximumPlayers();

    Map getMap();

    Location getLobby();

    GameState getState();

    Runnable getHandler();
}
