package com.mineswine.sg.game.command.custom;

import com.mineswine.sg.SurvivalGames;
import com.mineswine.sg.game.command.SGCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Ethan on 12/31/2016.
 */
public class HistoryCommand implements SGCommand {

    private SurvivalGames instance;

    public HistoryCommand(SurvivalGames instance) {
        this.instance = instance;
    }

    public SurvivalGames getInstance() {
        return instance;
    }

    @Override
    public String getName() {
        return "history";
    }

    @Override
    public String getNode() {
        return "sg.stats";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            return;
        }

        if(args.length > 1) {
            sender.sendMessage(ChatColor.RED + "Player history searching coming SoonTM");
            return;
        }

        Player player = (Player) sender;
        player.getInventory().addItem(instance.getHistory().getBook(player.getUniqueId()));
    }

}
