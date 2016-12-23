package com.mineswine.sg.game.loot;

import com.mineswine.sg.SurvivalGames;
import com.mineswine.sg.game.util.LootUtil;
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
        all = new ArrayList<Block>();
        this.instance = instance;

        start();
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

    public void start() {
        SurvivalGames.getInstance().setupLocations();
        all = blocksFromTwoPoints(instance.getCorner1(), instance.getCorner2());

        new BukkitRunnable() {
            int i = 0;

            public void run() {
                i++;
                if (i % 2 == 1) {
                    instance.getCorner1().getBlock().setType(Material.GOLD_BLOCK);
                    instance.getCorner2().getBlock().setType(Material.GOLD_BLOCK);
                } else {
                    instance.getCorner2().getBlock().setType(Material.DIAMOND_BLOCK);
                    instance.getCorner1().getBlock().setType(Material.DIAMOND_BLOCK);
                }
                search("");
            }
        }.runTaskTimer(SurvivalGames.getInstance(), 0l, 20 * 10l);
    }

    public void search(String map) {
        System.out.println("Testing crate gen: " + all.size() + " blocks found");
        for (Block block : all) {
            if (block.getType().equals(Material.SPONGE)) {
                int p = ThreadLocalRandom.current().nextInt(100) + 1;
                if (p <= 75.0) {
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
                } else {
                    block.setType(Material.AIR);
                }
            } else if (block.getType() == Material.ENDER_CHEST) {
                tier2.add(block);
                block.setType(Material.CHEST);

                Chest chest = (Chest) block.getState();
                List<ChestContent> shtuff = LootUtil.getRandomContents(ChestContent.Tier.II);
                for (ChestContent content : shtuff) {
                    chest.getBlockInventory().addItem(content.getItem());
                }
                tier2.add(block);
            }
        }
    }

    public static List<Block> blocksFromTwoPoints(Location loc1, Location loc2) {
        List<Block> blocks = new ArrayList<Block>();

        int topBlockX = (loc1.getBlockX() < loc2.getBlockX() ? loc2.getBlockX() : loc1.getBlockX());
        int bottomBlockX = (loc1.getBlockX() > loc2.getBlockX() ? loc2.getBlockX() : loc1.getBlockX());
        int topBlockY = (loc1.getBlockY() < loc2.getBlockY() ? loc2.getBlockY() : loc1.getBlockY());
        int bottomBlockY = (loc1.getBlockY() > loc2.getBlockY() ? loc2.getBlockY() : loc1.getBlockY());
        int topBlockZ = (loc1.getBlockZ() < loc2.getBlockZ() ? loc2.getBlockZ() : loc1.getBlockZ());
        int bottomBlockZ = (loc1.getBlockZ() > loc2.getBlockZ() ? loc2.getBlockZ() : loc1.getBlockZ());

        for (int x = bottomBlockX; x <= topBlockX; x++) {
            for (int z = bottomBlockZ; z <= topBlockZ; z++) {
                for (int y = bottomBlockY; y <= topBlockY; y++) {
                    Block block = loc1.getWorld().getBlockAt(x, y, z);
                    if (block.getType().equals(Material.SPONGE) || block.getType().equals(Material.ENDER_CHEST)) {
                        blocks.add(block);
                    }
                }
            }
        }

        return blocks;
    }

}
