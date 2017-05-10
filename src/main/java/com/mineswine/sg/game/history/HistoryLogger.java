package com.mineswine.sg.game.history;

import com.mineswine.sg.SurvivalGames;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Ethan on 12/27/2016.
 */
public class HistoryLogger {

    private Map<UUID, List<String>> log;

    public HistoryLogger() {
        log = new ConcurrentHashMap<>();
    }

    public Map<UUID, List<String>> getLog() {
        return log;
    }

    public void setLog(Map<UUID, List<String>> log) {
        this.log = log;
    }

    public void register(UUID player) {
        getLog().put(player, new ArrayList<>());
        log(player, "Your game history is logged here, enjoy!");
    }

    public void log(UUID player, String entry) {
        long current = System.currentTimeMillis();
        Date date = new Date(current);
        SimpleDateFormat f = new SimpleDateFormat("dd MMM yyyy HH:mm");
        String formatted = f.format(date);

        getLog().get(player).add(ChatColor.RED + "" + ChatColor.BOLD + "Borawski's SG" + ChatColor.RESET + "" + ChatColor.GRAY + entry + ChatColor.RED + " @ " + formatted);
    }

    public ItemStack getBook(UUID player) {
        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta bm = (BookMeta) book.getItemMeta();
        bm.setPages(getLog().get(player));
        bm.setAuthor(SurvivalGames.getInstance().getPrefix());
        bm.setDisplayName(ChatColor.YELLOW + "" + player.toString());
        book.setItemMeta(bm);

        return book;
    }
}
