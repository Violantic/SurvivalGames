package me.violantic.sg.game.command.custom;

import me.violantic.sg.SurvivalGames;
import me.violantic.sg.game.command.SGCommand;
import me.violantic.sg.game.lang.Messages;
import org.bukkit.command.CommandSender;

/**
 * Created by Ethan on 12/22/2016.
 */
public class ForceStartCommand implements SGCommand {

    private SurvivalGames instance;

    public ForceStartCommand(SurvivalGames instance) {
        this.instance = instance;
    }

    public SurvivalGames getInstance() {
        return instance;
    }

    @Override
    public String getName() {
        return "forcestart";
    }

    @Override
    public String getNode() {
        return "sg.admin";
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (instance.getState().getName().equalsIgnoreCase("progress")) {
            if (instance.getHandler().getSecond() < 585) {
                commandSender.sendMessage(instance.getPrefix() + Messages.EN_GAME_NOT_STARTED);
            } else {
                instance.getHandler().setSecond(590);
                commandSender.sendMessage(instance.getPrefix() + Messages.EN_FORCE_STARTING);
            }
        } else if (instance.getState().getName().equalsIgnoreCase("waiting")) {
            commandSender.sendMessage(instance.getPrefix() + Messages.EN_FORCE_MAP);
            instance.getHandler().setSecond(16);
        } else if (instance.getState().getName().equalsIgnoreCase("started")) {
            commandSender.sendMessage(instance.getPrefix() + Messages.EN_GAME_STARTED);
        }
    }
}
