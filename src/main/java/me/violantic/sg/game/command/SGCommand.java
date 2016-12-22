package me.violantic.sg.game.command;

import org.bukkit.command.CommandSender;

/**
 * Created by Ethan on 12/21/2016.
 */
public interface SGCommand {

    String getName();

    String getNode();

    void execute(CommandSender sender, String[] args);

}
