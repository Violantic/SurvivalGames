package com.mineswine.sg.game.lang;

import org.bukkit.ChatColor;

/**
 * Created by Ethan on 12/21/2016.
 */
public class Messages {

    public final static String EN_CANNOT_JOIN_GAME = ChatColor.RED + "The game you tried to join is currently full!";

    public final static String EN_GAME_JOIN        = ChatColor.GREEN + "You have joined SG, right click a paper to vote for your map!";

    public final static String EN_GAME_STARTED     = ChatColor.RED + "The game has already started!";

    public final static String EN_GAME_NOT_STARTED = ChatColor.RED + "The game has not been started yet!";

    public final static String EN_FORCE_STARTING   = ChatColor.YELLOW + "Force starting the game!";

    public final static String EN_FORCE_MAP        = ChatColor.YELLOW + "Game has been force started, choosing the map winner...";

    public final static String EN_FORCE_ENDING     = ChatColor.YELLOW + "Force ending the game!";

    public final static String EN_PLAYER_KILL      = ChatColor.GREEN + "You killed {target}!";

    public final static String EN_PLAYER_KILLED    = ChatColor.RED + "You were killed by {player}!";

    public final static String EN_PLAYER_DIED      = ChatColor.RED + "You died from {reason}";

    public final static String EN_DEATH_BROADCAST  = ChatColor.YELLOW + "Player " + ChatColor.LIGHT_PURPLE + "{player} " + ChatColor.YELLOW + " has been eliminated!";

    public final static String EN_GAME_WIN         = ChatColor.GOLD + "You won the game!";

    public final static String EN_GAME_LOSS        = ChatColor.LIGHT_PURPLE + "Good game, better luck next time!";

    public final static String EN_NO_PERMS         = ChatColor.RED + "You do not have permission for that!";

    public final static String EN_RATING_CHANGE    = ChatColor.GREEN + "[STATS] +{rating}!";

    public final static String EN_TIER_I_OPEN      = ChatColor.GREEN + "[STATS] +1 Tier I Chest Opened";

    public final static String EN_TIER_II_OPEN     = ChatColor.GREEN + "[STATS] +1 Tier II Chest Opened";

    public final static String EN_MAP_VOTE         = ChatColor.YELLOW + "You have voted for " + ChatColor.LIGHT_PURPLE + "{map}" + ChatColor.YELLOW + "!";

    public final static String EN_PLAYERS_LEFT     = ChatColor.YELLOW + "There are currently " + ChatColor.LIGHT_PURPLE + "{remaining} " + ChatColor.YELLOW + "out of " + ChatColor.LIGHT_PURPLE + "{maximum} " + ChatColor.YELLOW + "survivors remaining!";

    public final static String EN_MAP_VOTE_SUCCESS = ChatColor.YELLOW + "The map voted was " + ChatColor.LIGHT_PURPLE + "{map}" + ChatColor.YELLOW + "!";

    public final static String EN_DM_NATURAL       = ChatColor.YELLOW + "The death match has come early? Huh...";

    public final static String EN_DM_POST_RELEASE  = ChatColor.DARK_RED + "" + ChatColor.BOLD + "FIGHT TO THE DEATH!";

    public final static String EN_DM_DISEASE_START = ChatColor.GREEN + "" + ChatColor.BOLD + "Disease is spreading...";

    public final static String EN_CHEST_REFILLING  = ChatColor.LIGHT_PURPLE + "{tierI} " + ChatColor.YELLOW + " Tier I chests have been filled, and " + ChatColor.LIGHT_PURPLE + "{tierII} " + ChatColor.YELLOW + "Tier II chests have been filled.";

    public final static String LINE = ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH + "-----------------------------------------------------";

    public final static String EN_ERROR            = ChatColor.RED + "There was an error in the code, go yell at Ethan @ https://forum.mineswine.com/members/violantic.4888/";

}
