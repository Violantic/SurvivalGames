package com.mineswine.sg.game.command.custom;

import com.mineswine.sg.SurvivalGames;
import com.mineswine.sg.game.command.SGCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Ethan on 12/21/2016.
 */
public class StatsCommand implements SGCommand {

    private SurvivalGames instance;

    public StatsCommand(SurvivalGames instance) {
        this.instance = instance;
    }

    public SurvivalGames getInstance() {
        return instance;
    }

    @Override
    public String getName() {
        return "stats";
    }

    @Override
    public String getNode() {
        return "sg.stats";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        System.out.println("STATS!");
        if (!(sender instanceof Player)) {
            return;
        }

        if(args.length > 1) {
            sender.sendMessage(ChatColor.RED + "Player stats searching coming SoonTM");
            return;
        }

        Player player = (Player) sender;
        instance.getMysql().sendStats(player);
    }
}
