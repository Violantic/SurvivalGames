package com.mineswine.sg.handler;

import com.mineswine.sg.SurvivalGames;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

/**
 * Created by Ethan on 11/11/2016.
 */
public class ScoreboardHandler implements Runnable {

    private String name;
    private Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();

    private String title = ChatColor.YELLOW + "" + ChatColor.BOLD + "Mine" + ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "Swine";
    final Objective objective = scoreboard.registerNewObjective("GameSB", "dummy");
    final org.bukkit.scoreboard.Team team = scoreboard.registerNewTeam("team1");
    final org.bukkit.scoreboard.Team team2 = scoreboard.registerNewTeam("team2");
    final org.bukkit.scoreboard.Team team3 = scoreboard.registerNewTeam("team3");
    final org.bukkit.scoreboard.Team team4 = scoreboard.registerNewTeam("team4");
    final org.bukkit.scoreboard.Team team5 = scoreboard.registerNewTeam("team5");
    final org.bukkit.scoreboard.Team team6 = scoreboard.registerNewTeam("team6");
    final org.bukkit.scoreboard.Team team7 = scoreboard.registerNewTeam("team7");
    final org.bukkit.scoreboard.Team team8 = scoreboard.registerNewTeam("team8");
    final org.bukkit.scoreboard.Team team9 = scoreboard.registerNewTeam("team9");
    final org.bukkit.scoreboard.Team team10 = scoreboard.registerNewTeam("team10");
    final org.bukkit.scoreboard.Team team11 = scoreboard.registerNewTeam("team11");


    public ScoreboardHandler(String name) {
        this.name = name;

        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        team.addPlayer(Bukkit.getOfflinePlayer(ChatColor.AQUA.toString()));
        team2.addPlayer(Bukkit.getOfflinePlayer(ChatColor.STRIKETHROUGH.toString()));
        team3.addPlayer(Bukkit.getOfflinePlayer(ChatColor.BLACK.toString()));
        team4.addPlayer(Bukkit.getOfflinePlayer(ChatColor.BLUE.toString()));
        team5.addPlayer(Bukkit.getOfflinePlayer(ChatColor.DARK_AQUA.toString()));
        team6.addPlayer(Bukkit.getOfflinePlayer(ChatColor.DARK_PURPLE.toString()));
        team7.addPlayer(Bukkit.getOfflinePlayer(ChatColor.DARK_GREEN.toString()));
        team8.addPlayer(Bukkit.getOfflinePlayer(ChatColor.DARK_BLUE.toString()));
        team9.addPlayer(Bukkit.getOfflinePlayer(ChatColor.DARK_GRAY.toString()));
        team10.addPlayer(Bukkit.getOfflinePlayer(ChatColor.DARK_RED.toString()));
        team11.addPlayer(Bukkit.getOfflinePlayer(ChatColor.WHITE.toString()));
        objective.getScore(ChatColor.AQUA.toString()).setScore(11);
        objective.getScore(ChatColor.STRIKETHROUGH.toString()).setScore(10);
        objective.getScore(ChatColor.BLACK.toString()).setScore(9);
        objective.getScore(ChatColor.BLUE.toString()).setScore(8);
        objective.getScore(ChatColor.DARK_AQUA.toString()).setScore(7);
        objective.getScore(ChatColor.DARK_PURPLE.toString()).setScore(6);
        objective.getScore(ChatColor.DARK_GREEN.toString()).setScore(5);
        objective.getScore(ChatColor.DARK_BLUE.toString()).setScore(4);
        objective.getScore(ChatColor.DARK_GRAY.toString()).setScore(3);
        objective.getScore(ChatColor.DARK_RED.toString()).setScore(2);
        objective.getScore(ChatColor.WHITE.toString()).setScore(1);

        objective.setDisplayName(SurvivalGames.getInstance().getPrefix().replace("| ", ""));
        team.setPrefix("");
        team2.setPrefix(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "Survivors");
        team3.setPrefix(ChatColor.GRAY + "" + SurvivalGames.getInstance().getVerifiedPlayers().size());
        team4.setPrefix("");
        team5.setPrefix(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "State");
        team6.setPrefix(ChatColor.GRAY + (SurvivalGames.getInstance().getState().getName()));
        team7.setPrefix("");
        team11.setPrefix(ChatColor.YELLOW + "MINESWINE.COM");
    }

    public Scoreboard getScoreboard() {
        return scoreboard;
    }

    public void run() {
        if(SurvivalGames.getInstance().getState().getName().equalsIgnoreCase("waiting")) return;

        team3.setPrefix(ChatColor.GRAY + "" + SurvivalGames.getInstance().getVerifiedPlayers().size());
        team6.setPrefix(ChatColor.GRAY + SurvivalGames.getInstance().getState().getName());

        int numberOfMinutes,numberOfSeconds;
        numberOfMinutes = ((SurvivalGames.getInstance().getHandler().getSecond() % 86400 ) % 3600 ) / 60;
        numberOfSeconds = ((SurvivalGames.getInstance().getHandler().getSecond() % 86400 ) % 3600 ) % 60;
        team8.setPrefix(ChatColor.LIGHT_PURPLE + "Death Match In");
        if(SurvivalGames.getInstance().getState().getName().equalsIgnoreCase("progress")) {
            if(SurvivalGames.getInstance().second > 600) {
                team8.setPrefix(ChatColor.LIGHT_PURPLE + "Releasing In");
            } else {
                team8.setPrefix(ChatColor.LIGHT_PURPLE + "Death Match In");
            }
        } else if(SurvivalGames.getInstance().getState().getName().equalsIgnoreCase("deathmatch")) {
            team8.setPrefix(ChatColor.YELLOW + "" + ChatColor.BOLD + "Ends In");
        }

        String seconds = ((numberOfSeconds < 10) ? ("0"+numberOfSeconds) : numberOfSeconds + "");
        String minutes = ((numberOfMinutes < 10) ? ("0"+numberOfMinutes) + ":" : numberOfMinutes + ":") + seconds;
        team9.setPrefix(ChatColor.GRAY + minutes);

        for(Player player : Bukkit.getOnlinePlayers()) {
            player.setScoreboard(getScoreboard());
        }
    }
}
