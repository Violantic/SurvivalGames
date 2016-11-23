package me.violantic.sg.game;

import me.violantic.sg.game.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Ethan on 11/23/2016.
 */
public enum ChestContents {

    APPLE         (1, new ItemBuilder(Material.APPLE).setAmount(1).build(), 60),
    ARROW         (2, new ItemBuilder(Material.ARROW).setAmount(12).build(), 15),
    AXE_W         (3, new ItemBuilder(Material.WOOD_AXE).setAmount(1).build(), 90),
    AXE_S         (4, new ItemBuilder(Material.STONE_AXE).setAmount(1).build(), 70),
    AXE_I         (5, new ItemBuilder(Material.IRON_AXE).setAmount(1).build(), 60),
    AXE_G         (6, new ItemBuilder(Material.GOLD_AXE).setAmount(1).build(), 50),
    AXE_D         (7, new ItemBuilder(Material.DIAMOND_AXE).setAmount(1).build(), 35),
    BAKED_POTATO  (6, new ItemBuilder(Material.BAKED_POTATO).setAmount(3).build(), 45),
    BOOT_L        (7, new ItemBuilder(Material.LEATHER_BOOTS).setAmount(1).build(), 20),
    BOOT_C        (8, new ItemBuilder(Material.CHAINMAIL_BOOTS).setAmount(1).build(), 18),
    BOOT_G        (9, new ItemBuilder(Material.GOLD_BOOTS).setAmount(1).build(), 12),
    BOOT_D        (10, new ItemBuilder(Material.DIAMOND_BOOTS).setAmount(1).build(), 4),
    CARROT        (11, new ItemBuilder(Material.CARROT).setAmount(3).build(), 80),
    CHEST_L       (12, new ItemBuilder(Material.LEATHER_CHESTPLATE).setAmount(1).build(), 40),
    CHEST_I       (13, new ItemBuilder(Material.IRON_CHESTPLATE).setAmount(1).build(), 30),
    CHEST_C       (14, new ItemBuilder(Material.CHAINMAIL_CHESTPLATE).setAmount(1).build(), 35),
    CHEST_G       (15, new ItemBuilder(Material.GOLD_CHESTPLATE).setAmount(1).build(), 20),
    CHEST_D       (16, new ItemBuilder(Material.DIAMOND_CHESTPLATE).setAmount(1).build(), 5),
    COOKIE        (12, new ItemBuilder(Material.COOKIE).setAmount(3).build(), 80),
    COMPASS       (13, new ItemBuilder(Material.COMPASS).setAmount(1).build(), 10),
    DIAMOND       (14, new ItemBuilder(Material.DIAMOND).setAmount(1).build(), 60);

    private int id;
    private ItemStack item;
    private double chance;

    ChestContents(int id, ItemStack item, double chance) {
        this.id = id;
        this.item = item;
        this.chance = chance;
    }

    public int getId() {
        return id;
    }

    public ItemStack getItem() {
        return item;
    }

    public double getChance() {
        return chance;
    }
}
