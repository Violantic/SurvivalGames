package me.violantic.sg.game.util;

import me.violantic.sg.SurvivalGames;
import me.violantic.sg.game.ChestContent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ethan on 11/12/2016.
 */
public class CrateGenerator {

    public List<Location> tier1;
    public List<Location> tier2;

    public CrateGenerator() {
        tier1 = new ArrayList<>();
        tier2 = new ArrayList<>();
    }

    public void start() {
        new BukkitRunnable() {
            public void run() {
                search(SurvivalGames.getInstance().getMap().getName());
            }
        }.runTaskTimer(SurvivalGames.getInstance(), 0l, 20*10);
    }

    public void search(String map) {
        World w = Bukkit.getWorld(map);
        for (Chunk c : w.getLoadedChunks()) {
            int cx = c.getX() << 4;
            int cz = c.getZ() << 4;
            for (int x = cx; x < cx + 16; x++) {
                for (int z = cz; z < cz + 16; z++) {
                    for (int y = 0; y < 128; y++) {
                        if (w.getBlockAt(x, y, z).getType() == Material.SPONGE) {
                            tier1.add(new Location(Bukkit.getWorld(map), x, y, z));
                            Block block = w.getBlockAt(x, y, z);
                            block.setType(Material.CHEST);

                            Chest chest = (Chest) block.getState();
                            List<ChestContent> shtuff = LootUtil.getRandomContents(ChestContent.Tier.I);
                            for(ChestContent content : shtuff) {
                                chest.getBlockInventory().addItem(content.getItem());
                            }
                        } else if (w.getBlockAt(x, y, z).getType() == Material.ENDER_CHEST) {
                            tier2.add(new Location(Bukkit.getWorld(map), x, y, z));
                            Block block = w.getBlockAt(x, y, z);
                            block.setType(Material.CHEST);

                            Chest chest = (Chest) block.getState();
                            List<ChestContent> shtuff = LootUtil.getRandomContents(ChestContent.Tier.II);
                            for(ChestContent content : shtuff) {
                                chest.getBlockInventory().addItem(content.getItem());
                            }
                        }
                    }
                }
            }
        }
    }

}
