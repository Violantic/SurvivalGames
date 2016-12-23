package com.mineswine.sg.game.util;

import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.List;

/**
 * Created by Ethan on 11/11/2016.
 */
public class ItemBuilder {

    private ItemStack item;
    private ItemMeta itemMeta;
    public ItemBuilder(Material mat){
        this.item = new ItemStack(mat);
        this.itemMeta = item.getItemMeta();
    }
    public ItemBuilder setAmount(int amount){
        item.setAmount(amount);
        return this;
    }
    public ItemBuilder setDurability(short durability){
        item.setDurability(durability);
        return this;
    }
    @SuppressWarnings("deprecation")
    public ItemBuilder setData(DyeColor color){
        item.getData().setData(color.getDyeData());
        return this;
    }
    public ItemBuilder addEnchant(Enchantment e, int level){
        itemMeta.addEnchant(e, level, true);
        return this;
    }
    public ItemBuilder removeEnchant(Enchantment e){
        itemMeta.removeEnchant(e);
        return this;
    }
    public ItemBuilder addItemFlags(ItemFlag[] list){
        itemMeta.addItemFlags(list);
        return this;
    }
    public ItemBuilder removeItemFlags(ItemFlag[] list){
        itemMeta.removeItemFlags(list);
        return this;
    }
    public ItemBuilder setName(String name){
        itemMeta.setDisplayName(name);
        return this;
    }
    public ItemBuilder setLore(List<String> list){
        itemMeta.setLore(list);
        return this;
    }
    public ItemBuilder setColor(Color color){
        LeatherArmorMeta meta = (LeatherArmorMeta) itemMeta;
        meta.setColor(color);
        return this;
    }
    public ItemBuilder setSkull(String name){
        SkullMeta meta = (SkullMeta) itemMeta;
        meta.setOwner(name);
        return this;
    }
    public ItemStack build(){
        item.setItemMeta(itemMeta);
        return item;
    }
    @Override
    public String toString(){
        return "Type=" + item.getType().name() + ", Amount=" + item.getAmount() + ", Durability=" + item.getDurability() + ", Enchantments=" + itemMeta.getEnchants() + ", ItemFlags=" + itemMeta.getItemFlags() + ", Name=" + itemMeta.getDisplayName() + ", Lore=" + itemMeta.getLore();
    }
    
}
