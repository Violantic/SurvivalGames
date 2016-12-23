package com.mineswine.sg.game.event;

import com.mineswine.sg.SurvivalGames;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;

/**
 * Created by Ethan on 12/4/2016.
 */
public class WorldListener implements Listener {

    private SurvivalGames instance;

    public WorldListener(SurvivalGames instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event) {
        if(!instance.isLocationGenerationInvoked()) return;
        instance.getLocationGenerator().invokeWorldListener(event);
    }

}
