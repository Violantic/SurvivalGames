package com.mineswine.sg.game;

import com.mineswine.sg.game.map.Map;
import org.bukkit.Location;

/**
 * Created by Ethan on 11/3/2016.
 */
public interface Game {

    int minimumPlayers();

    int maximumPlayers();

    String[] getDescriptions();

    Map getMap();

    Location getLobby();

    GameState getState();

    Runnable getHandler();
}
