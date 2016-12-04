package me.violantic.sg.game.util;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.event.world.ChunkLoadEvent;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Ethan on 12/3/2016.
 */
public class LocationGenerator {

    private World world;
    private Map<String, Location> locationValues;

    public LocationGenerator(World world) {
        this.world = world;
        this.locationValues = new ConcurrentHashMap<>();
    }

    public World getWorld() {
        return world;
    }

    public Map<String, Location> getLocationValues() {
        return locationValues;
    }

    public void invokeWorldListener(ChunkLoadEvent event) {
        for(BlockState e : event.getChunk().getTileEntities()) {
            if(e.getType().equals(Material.SIGN)) {
                Sign s = (Sign) e.getBlock();
                if(!s.getLine(0).startsWith("{sg:")) return;

                locationValues.put(s.getLine(0), e.getLocation());
                System.out.println("[SG] " + s.getLine(0) + " has been registered!");
            }
        }
    }

}
