package com.mineswine.sg.game.util;

import com.mineswine.sg.SurvivalGames;
import org.bukkit.Bukkit;

/**
 * Created by Ethan on 11/10/2016.
 */
public class VoteUtil {

    public static void startVote() {
        SurvivalGames.getInstance().initiateVoteHandler();
    }

    public static void endVote() {
        SurvivalGames.getInstance().getVoteHandler().end(Bukkit.getOnlinePlayers());
    }

    public static void startDMVote() {
        // TODO - Initiate death match vote handler
    }

    public static void endDMVote() {
        // TODO - end death match vote handler
    }

}
