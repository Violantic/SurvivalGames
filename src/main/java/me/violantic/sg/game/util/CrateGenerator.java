package me.violantic.sg.game.util;

import me.violantic.sg.SurvivalGames;
import me.violantic.sg.game.ChestContent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Ethan on 11/12/2016.
 */
public class CrateGenerator {

    public List<Block> tier1;
    public List<Block> tier2;
    public List<Block> all;
    private SurvivalGames instance;

    private boolean enabled = true;

    public CrateGenerator(SurvivalGames instance) {
        tier1 = new ArrayList<Block>();
        tier2 = new ArrayList<Block>();

        this.instance = instance;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void enable() {
        this.enabled = true;
    }

    public void disable() {
        this.enabled = false;
    }

    public void start(final String map) {
        // Only has to loop through map once. //
        all = getCrates(Bukkit.getWorld(map), new Location(Bukkit.getWorld(map), -1704, 84, 815), new Location(Bukkit.getWorld(map), -1671, 106, 847));

        new BukkitRunnable() {
            public void run() {
                search(map);
            }
        }.runTaskTimer(SurvivalGames.getInstance(), 0l, 20 * 10l);
    }

    public void search(String map) {
        if (!isEnabled()) return;

        for (Block block : all) {
            if (block.getType().equals(Material.SPONGE)) {
                int p = ThreadLocalRandom.current().nextInt(100) + 1;
                if (p <= 75.0) {

                    new BukkitRunnable() {
                        public void run() {
                            tier1.add(block);
                            block.setType(Material.CHEST);

                            Chest chest = (Chest) block.getState();
                            List<ChestContent> shtuff = LootUtil.getRandomContents(ChestContent.Tier.I);
                            for (
                                    ChestContent content
                                    : shtuff)

                            {
                                chest.getBlockInventory().addItem(content.getItem());
                            }

                            tier1.add(block);
                        }
                    }.runTaskAsynchronously(SurvivalGames.getInstance());
                } else {
                    block.setType(Material.AIR);
                }
            } else if (block.getType() == Material.ENDER_CHEST) {
                new BukkitRunnable() {
                    public void run() {
                        tier2.add(block);
                        block.setType(Material.CHEST);

                        Chest chest = (Chest) block.getState();
                        List<ChestContent> shtuff = LootUtil.getRandomContents(ChestContent.Tier.II);
                        for (ChestContent content : shtuff) {
                            chest.getBlockInventory().addItem(content.getItem());
                        }
                        tier2.add(block);
                    }
                }.runTaskAsynchronously(SurvivalGames.getInstance());
            }
        }
    }

    public List<Block> getCrates(World world, Location loc1, Location loc2) {
        List<Block> blocks = new ArrayList<Block>();

        for (double x = loc1.getX(); x <= loc2.getX(); x++) {
            for (double y = loc1.getY(); y <= loc2.getY(); y++) {
                for (double z = loc1.getZ(); z <= loc2.getZ(); z++) {
                    Location loc = new Location(world, x, y, z);
                    Block block = world.getBlockAt(loc);
                    if (block.getType() == Material.SPONGE || block.getType() == Material.ENDER_CHEST) {
                        blocks.add(block);
                    }
                }
            }
        }

        return blocks;
    }

}
