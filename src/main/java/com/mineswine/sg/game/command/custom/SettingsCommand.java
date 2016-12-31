package com.mineswine.sg.game.command.custom;

import com.mineswine.sg.SurvivalGames;
import com.mineswine.sg.game.command.SGCommand;
import com.mineswine.sg.game.lang.Messages;
import com.mineswine.sg.game.util.ChatUtil;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Ethan on 12/31/2016.
 */
public class SettingsCommand implements SGCommand {

    private SurvivalGames instance;

    public SettingsCommand(SurvivalGames instance) {
        this.instance = instance;
    }

    public SurvivalGames getInstance() {
        return instance;
    }

    @Override
    public String getName() {
        return "settings";
    }

    @Override
    public String getNode() {
        return "sg.settings";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            return;
        }

        Player player = (Player) sender;
        player.sendMessage(Messages.LINE);
        player.sendMessage("");
        ChatUtil.sendCenteredMessage(player, ChatColor.YELLOW + "Player settings coming " + ChatColor.LIGHT_PURPLE + "SoonTM" + ChatColor.YELLOW + "!");
        player.sendMessage("");
        player.sendMessage(Messages.LINE);
    }

}
