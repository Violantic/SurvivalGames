package com.mineswine.sg.game.command;

import com.mineswine.sg.SurvivalGames;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ethan on 12/21/2016.
 */
public class CommandManager {

    private List<SGCommand> commandCache;
    private SurvivalGames instance;

    public CommandManager(SurvivalGames instance){
        this.instance = instance;
        commandCache = new ArrayList<>();
    }

    public List<SGCommand> getCommandCache() {
        return commandCache;
    }

    public void setCommandCache(List<SGCommand> commandCache) {
        this.commandCache = commandCache;
    }

    public SurvivalGames getInstance() {
        return instance;
    }

    public void setInstance(SurvivalGames instance) {
        this.instance = instance;
    }

    public void register(SGCommand command) {
        getCommandCache().add(command);
    }

    public void unregister(SGCommand command) {
        try {
            getCommandCache().remove(command);
        } catch(Exception e) {
            System.out.println("[SG] command was never cached!");
        }
    }
}
