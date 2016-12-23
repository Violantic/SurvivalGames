package com.mineswine.sg.game.map;

import com.mineswine.sg.SurvivalGames;
import org.apache.commons.io.FileUtils;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by Ethan on 11/3/2016.
 */
public class Map {

    private String name;
    private String[] creators;
    private List<Location> spawnLocations;
    private World world;

    public Map(String name, String[] creators, List<Location> spawnLocations) {
        this.name = name;
        this.creators = creators;
        this.spawnLocations = spawnLocations;
        try {
            // TODO - get map from location outside of server folder to save storage space
            FileUtils.copyDirectory(new File(name + "_template"), new File(name));
            this.world = SurvivalGames.getInstance().getServer().createWorld(new WorldCreator(name));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return name;
    }

    public String[] getCreators() {
        return creators;
    }

    public List<Location> getSpawnLocations() {
        return spawnLocations;
    }

    public World getWorld() {
        return world;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCreators(String[] creators) {
        this.creators = creators;
    }

    public void setSpawnLocations(List<Location> spawnLocations) {
        this.spawnLocations = spawnLocations;
    }

    public void setWorld(World world) {
        this.world = world;
    }
}

