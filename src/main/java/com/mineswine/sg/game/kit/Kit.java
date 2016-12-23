package com.mineswine.sg.game.kit;

import org.bukkit.inventory.ItemStack;

/**
 * Created by Ethan on 12/23/2016.
 */
public abstract class Kit {

    private String name;
    private String[] description;
    private ItemStack icon;

    public Kit(String name, String[] description, ItemStack icon) {
        this.name = name;
        this.description = description;
        this.icon = icon;
    }

}
