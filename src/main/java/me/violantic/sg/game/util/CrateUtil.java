package me.violantic.sg.game.util;

import me.violantic.sg.SurvivalGames;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Ethan on 11/12/2016.
 */
public class CrateUtil {

    public static List<Location> tier1 = new ArrayList<Location>();
    public static List<Location> tier2 = new ArrayList<Location>();

    public static void start() {
        new BukkitRunnable() {
            public void run() {
                search(SurvivalGames.getInstance().getMap().getName());
            }
        }.runTaskTimer(SurvivalGames.getInstance(), 0l, 20*10);
    }

    // TODO - make configurable crate item loadouts you noob
    public static void search(String map) {
        int amount = 0;
        ArrayList<Location> stones = new ArrayList<Location>();

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
                            int i = ThreadLocalRandom.current().nextInt(100) + 1;

                            if(i > 0 && i <= 3) {
                                chest.getBlockInventory().addItem(new ItemBuilder(Material.DIAMOND_CHESTPLATE).build());
                            }
                            else if(i > 3 && i <= 40) {
                                chest.getBlockInventory().addItem(new ItemBuilder(Material.STONE_SWORD).build());
                                chest.getBlockInventory().addItem(new ItemBuilder(Material.COOKED_BEEF).setAmount(3).build());
                            }
                            else if(i > 40 && i <= 85) {
                                chest.getBlockInventory().addItem(new ItemBuilder(Material.CARROT).setAmount(3).build());
                                chest.getBlockInventory().addItem(new ItemBuilder(Material.LEATHER_HELMET).build());
                                chest.getBlockInventory().addItem(new ItemBuilder(Material.STICK).setAmount(2).build());
                                chest.getBlockInventory().addItem(new ItemBuilder(Material.LEATHER_LEGGINGS).build());
                                chest.getBlockInventory().addItem(new ItemBuilder(Material.STRING).setAmount(1).build());
                            } else if(i > 85 && i <= 92) {
                                chest.getBlockInventory().addItem(new ItemBuilder(Material.IRON_SWORD).build());
                                chest.getBlockInventory().addItem(new ItemBuilder(Material.GOLD_CHESTPLATE).build());
                                chest.getBlockInventory().addItem(new ItemBuilder(Material.FLINT_AND_STEEL).build());
                            } else if(i > 92 && i <= 94) {
                                chest.getBlockInventory().addItem(new ItemBuilder(Material.DIAMOND_SWORD).build());
                                chest.getBlockInventory().addItem(new ItemBuilder(Material.IRON_CHESTPLATE).build());
                                chest.getBlockInventory().addItem(new ItemBuilder(Material.GOLD_BOOTS).build());
                                chest.getBlockInventory().addItem(new ItemBuilder(Material.GOLDEN_APPLE).build());
                            } else if(i > 94 && i <= 98) {
                                chest.getBlockInventory().addItem(new ItemBuilder(Material.BOW).build());
                                chest.getBlockInventory().addItem(new ItemBuilder(Material.ARROW).setAmount(24).build());
                            } else if(i > 98 && i <= 100) {
                                chest.getBlockInventory().addItem(new ItemBuilder(Material.GOLD_SWORD).addEnchant(Enchantment.DAMAGE_ALL, 2).build());
                                chest.getBlockInventory().addItem(new ItemBuilder(Material.CHAINMAIL_CHESTPLATE).build());
                                chest.getBlockInventory().addItem(new ItemBuilder(Material.CHAINMAIL_LEGGINGS).build());
                            }
                            amount++;
                        } else if (w.getBlockAt(x, y, z).getType() == Material.ENDER_CHEST) {
                            tier2.add(new Location(Bukkit.getWorld(map), x, y, z));
                            Block block = w.getBlockAt(x, y, z);
                            block.setType(Material.GOLD_BLOCK);

                            // TODO - give tier 2 better items
                        }
                    }
                }
            }
        }
    }

}
