package com.mineswine.sg.handler;

import com.mineswine.sg.SurvivalGames;
import com.mineswine.sg.game.map.MapVoter;
import com.mineswine.sg.game.util.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by Ethan on 11/11/2016.
 */
public class VoteHandler implements Runnable {

    private String name;
    private MapVoter voter;

    private List<ItemStack> mapItems;

    public VoteHandler(String name, MapVoter voter) {
        this.name = name;
        this.voter = voter;

        mapItems = new ArrayList<ItemStack>();
        refresh();
    }

    public String getName() {
        return name;
    }

    public MapVoter getVoter() {
        return voter;
    }

    public List<ItemStack> getMapItems() {
        return mapItems;
    }

    public void refresh() {
        mapItems.clear();
        for(String key : voter.getValues().keySet()) {
            mapItems.add(new ItemBuilder(Material.PAPER)
                    .setName(ChatColor.WHITE + "" + ChatColor.BOLD + "VOTE: " + ChatColor.RESET + ChatColor.YELLOW + key)
                    .setLore(Collections.singletonList("This map has " + ChatColor.LIGHT_PURPLE + voter.getValues().get(key) + ChatColor.RESET + " votes"))
                    .setAmount(1)
                    .build());
        }
    }

    public void updateItems(Collection<? extends Player> players) {
        for(Player player : players) {
            if (voter.hasVoted(player.getUniqueId())) break;
            for (ItemStack item : getMapItems()) player.getInventory().setItem(getMapItems().indexOf(item) + 1, item);
        }
    }

    public void end(Collection<? extends Player> players) {
        for(Player player : players)
            for (ItemStack item : getMapItems()) player.getInventory().setItem(getMapItems().indexOf(item), new ItemStack(Material.AIR, 1));
    }

    public void run() {
        if(!SurvivalGames.getInstance().enabled()) return;
        if(!SurvivalGames.getInstance().getState().getName().equalsIgnoreCase("waiting")) return;
        for(Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(ChatColor.DARK_GRAY + "--------------------------------------------");
            for(String team : voter.getValues().keySet()) {
                String format = SurvivalGames.getInstance().getPrefix() + team + " has " + ChatColor.LIGHT_PURPLE + voter.getValues().get(team) + ChatColor.YELLOW + " votes";
                player.sendMessage(format);
            }
            player.sendMessage(ChatColor.DARK_GRAY + "--------------------------------------------");
        }

        refresh();
        updateItems(Bukkit.getOnlinePlayers());
    }
}
