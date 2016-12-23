package com.mineswine.sg.game.loot;

import com.mineswine.sg.game.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Ethan on 11/23/2016.
 */
public enum ChestContent {

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
    BOW           (11, new ItemBuilder(Material.BOW).setAmount(1).build(), 45),
    BOW_OP        (12, new ItemBuilder(Material.BOW).addEnchant(Enchantment.ARROW_DAMAGE, 2).setAmount(1).build(), 5),
    CARROT        (13, new ItemBuilder(Material.CARROT).setAmount(3).build(), 80),
    CHEST_L       (14, new ItemBuilder(Material.LEATHER_CHESTPLATE).setAmount(1).build(), 40),
    CHEST_I       (15, new ItemBuilder(Material.IRON_CHESTPLATE).setAmount(1).build(), 30),
    CHEST_C       (16, new ItemBuilder(Material.CHAINMAIL_CHESTPLATE).setAmount(1).build(), 35),
    CHEST_G       (17, new ItemBuilder(Material.GOLD_CHESTPLATE).setAmount(1).build(), 20),
    CHEST_D       (18, new ItemBuilder(Material.DIAMOND_CHESTPLATE).setAmount(1).build(), 5),
    COOKIE        (19, new ItemBuilder(Material.COOKIE).setAmount(3).build(), 80),
    COMPASS       (20, new ItemBuilder(Material.COMPASS).setAmount(1).build(), 10),
    DIAMOND       (21, new ItemBuilder(Material.DIAMOND).setAmount(1).build(), 60),
    EXPERIENCE    (22, new ItemBuilder(Material.EXP_BOTTLE).setAmount(12).build(), 60),
    ENCHANT_TABLE (23, new ItemBuilder(Material.ENCHANTMENT_TABLE).setAmount(1).build(), 10),
    FEATHER       (24, new ItemBuilder(Material.FEATHER).setAmount(5).build(), 75),
    FLINT         (25, new ItemBuilder(Material.FLINT).setAmount(1).build(), 75),
    HELMET_L      (26, new ItemBuilder(Material.LEATHER_HELMET).setAmount(1).build(), 80),
    HELMET_C      (27, new ItemBuilder(Material.CHAINMAIL_HELMET).setAmount(1).build(), 60),
    HELMET_I      (28, new ItemBuilder(Material.IRON_HELMET).setAmount(1).build(), 50),
    HELMET_G      (29, new ItemBuilder(Material.GOLD_HELMET).setAmount(1).build(), 70),
    HELMET_D      (30, new ItemBuilder(Material.DIAMOND_HELMET).setAmount(1).build(), 5),
    LEGS_L        (31, new ItemBuilder(Material.LEATHER_LEGGINGS).setAmount(1).build(), 80),
    LEGS_C        (32, new ItemBuilder(Material.CHAINMAIL_LEGGINGS).setAmount(1).build(), 65),
    LEGS_G        (33, new ItemBuilder(Material.GOLD_LEGGINGS).setAmount(1).build(), 75),
    LEGS_D        (34, new ItemBuilder(Material.DIAMOND_LEGGINGS).setAmount(1).build(), 12),
    SWORD_S       (35, new ItemBuilder(Material.STONE_SWORD).setAmount(1).build(), 80),
    SWORD_I       (36, new ItemBuilder(Material.IRON_SWORD).setAmount(1).build(), 60),
    SWORD_G       (37, new ItemBuilder(Material.GOLD_SWORD).setAmount(1).build(), 45),
    SWORD_D       (38, new ItemBuilder(Material.DIAMOND_SWORD).setAmount(1).build(), 3);

    private int id;
    private ItemStack item;
    private double chance;

    ChestContent(int id, ItemStack item, double chance) {
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

    public static ChestContent get(int id) {
        for(ChestContent content : values()) {
            if(content.getId() == id) return content;
        }

        return null;
    }

    public enum Tier {
        I, II
    }
}
