package me.violantic.sg.handler;

import me.violantic.sg.SurvivalGames;
import me.violantic.sg.game.util.LocationUtil;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Ethan on 11/23/2016.
 */
public class WaitingGlassAnimationHandler implements Runnable {

    private World world;
    private List<Location> initiated;
    private Map<Location, Integer> animation;
    private List<Location> garbage;
    private boolean progress = true;

    public WaitingGlassAnimationHandler(World world, List<Location> initiated) {
        this.world = world;
        this.initiated = initiated;
        this.animation = new ConcurrentHashMap<Location, Integer>();
        this.garbage = new ArrayList<Location>();
    }

    public void run() {
        if (!progress) return;

        if (SurvivalGames.getInstance().getState().getName().equalsIgnoreCase("progress") && SurvivalGames.getInstance().second <= 585) {
            for (Location location : garbage) {
                world.getBlockAt(location).setType(Material.AIR);
            }
            progress = false;
        }

        for (Location location : initiated) {
            if (animation.get(location) >= 10) {
                animation.put(location, 0);
            }

            animation.put(location, animation.get(location) + 1);
            if (!animation.containsKey(location)) {
                for (Location r : LocationUtil.getCircle(location, 2, 16)) {
                    Block b = world.getBlockAt(r);
                    for (int i = 0; i < 10; i++) {
                        if (b.getType() == Material.AIR) {
                            Location garbage = r.add(0, i, 0);
                            world.getBlockAt(r.add(garbage)).setType(Material.GLASS);
                            world.getBlockAt(r.add(garbage)).setData(DyeColor.PURPLE.getWoolData());
                            this.garbage.add(garbage);
                            this.animation.put(location, 0);
                        }
                    }
                }
            }

            Location height = location.add(0, animation.get(location), 0);
            for (Location r : LocationUtil.getCircle(height, 2, 16)) {
                Block b = world.getBlockAt(r);
                if (b.getType() == Material.AIR) {
                    world.getBlockAt(r).setType(Material.GLASS);
                    world.getBlockAt(r).setData(DyeColor.YELLOW.getWoolData());
                }
            }
        }

    }
}
