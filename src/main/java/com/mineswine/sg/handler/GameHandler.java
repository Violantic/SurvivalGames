package com.mineswine.sg.handler;

import com.mineswine.sg.SurvivalGames;
import com.mineswine.sg.game.GameState;
import com.mineswine.sg.game.event.GameEndEvent;
import com.mineswine.sg.game.event.GameStartEvent;
import com.mineswine.sg.game.lang.Messages;
import com.mineswine.sg.game.util.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

/**
 * Created by Ethan on 11/3/2016.
 */
public class GameHandler implements Runnable {

    private int second;
    private boolean lobby = true;
    private boolean voteStarted = false;

    public GameHandler() {
        second = 60;
    }

    public int players() {
        return Bukkit.getOnlinePlayers().size();
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    /**
     * Second will increment every 20 ticks (If healthy server state, every 1 second).
     * TODO - Instead of booleans for stages, make use of already existing game state's (I'm an idiot)
     */
    public void run() {
        if (!SurvivalGames.getInstance().enabled()) return;

        // Waits for the
        if (lobby) {
            handleXP();
        }

        if (players() >= SurvivalGames.getInstance().minimumPlayers()) {

            /**
             * Waiting stage.
             * @info this stage is the lobby portion of the minigame,
             * players cannot PVP, PVE, or alter any blocks. Players
             * also will vote for the map that they would like to
             * play on in the waiting stage.
             */
            if (SurvivalGames.getInstance().getState().getName().equalsIgnoreCase("waiting")) {
                if (second == 15) {
                    Bukkit.broadcastMessage(Messages.LINE);
                    Bukkit.broadcastMessage("");
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        ChatUtil.sendCenteredMessage(player, SurvivalGames.getInstance().getPrefix());
                        ChatUtil.sendCenteredMessage(player, Messages.EN_MAP_VOTE_SUCCESS.replace("{map}", SurvivalGames.getInstance().getGameMapVoter().getWinner()));
                    }
                    Bukkit.broadcastMessage("");
                    Bukkit.broadcastMessage(Messages.LINE);
                } else if (second == 0) {
                    GameState progress = new GameState("started");
                    progress.setCanMove(true);
                    progress.setCanPVP(false);
                    progress.setCanOpen(false);
                    SurvivalGames.getInstance().setState(progress);
                } else if (second == 10) {
                    Bukkit.broadcastMessage(Messages.LINE);
                    Bukkit.broadcastMessage("");
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        ChatUtil.sendCenteredMessage(player, SurvivalGames.getInstance().getPrefix());
                        ChatUtil.sendCenteredMessage(player, ChatColor.YELLOW + "Loading map...");
                    }
                    Bukkit.broadcastMessage("");
                    playTick();

                    // Start loading new crates so players don't get antsy. //
                    SurvivalGames.getInstance().initiateGameMap();
                    for(Player player : Bukkit.getOnlinePlayers()) {
                        ChatUtil.sendCenteredMessage(player, ChatColor.YELLOW + "Game starting in " + second + " seconds");
                    }
                    Bukkit.broadcastMessage("");
                    Bukkit.broadcastMessage(Messages.LINE);
                } else if (second <= 5) {
                    Bukkit.broadcastMessage(Messages.LINE);
                    Bukkit.broadcastMessage("");
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        ChatUtil.sendCenteredMessage(player, SurvivalGames.getInstance().getPrefix());
                        ChatUtil.sendCenteredMessage(player, ChatColor.YELLOW + "Game starting in " + second + " seconds");
                    }
                    Bukkit.broadcastMessage("");
                    Bukkit.broadcastMessage(Messages.LINE);
                    playTick();
                }
            }

            /**
             * Started stage.
             * @info this stage is a transitional stage between the lobby and the progress.
             * It sets up the player handlers such as PVP, PVE, and sends them to the area.
             */
            else if (SurvivalGames.getInstance().getState().getName().equalsIgnoreCase("started")) {
                if (second <= 0) {
                    GameState progress = new GameState("progress");
                    progress.setCanMove(false);
                    progress.setCanPVP(true);
                    progress.setCanOpen(true);
                    progress.setCanBreak(true);
                    progress.setCanPlace(true);
                    SurvivalGames.getInstance().setState(progress);
                    second = 10 * 60 + 15;
                    Bukkit.getServer().getPluginManager().callEvent(new GameStartEvent(SurvivalGames.getInstance()));
                    return;
                }
            }

            /**
             * Progress stage.
             * @info this stage is the meat of the game, here is where the players actually
             * try to stay alive for as long as they can and kill other players during this
             * stage. If the stage has not expired, but the player count gets to 5 - the
             * death match automatically starts; likewise if the stage expires and there are
             * more than 5 players left in the game, it will start a death match anyways.
             */
            else if (SurvivalGames.getInstance().getState().getName().equalsIgnoreCase("progress")) {
                SurvivalGames.getInstance().second = second;
                handleXP();
                if (SurvivalGames.getInstance().getVerifiedPlayers().size() == 5) {
                    playTick();
                    Bukkit.broadcastMessage(Messages.LINE);
                    Bukkit.broadcastMessage("");
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        ChatUtil.sendCenteredMessage(player, SurvivalGames.getInstance().getPrefix());
                        ChatUtil.sendCenteredMessage(player, Messages.EN_DM_NATURAL);
                        Bukkit.broadcastMessage("");
                    }
                    Bukkit.broadcastMessage(Messages.LINE);
                    startDeathMatch();
                    return;
                } else if(SurvivalGames.getInstance().getVerifiedPlayers().size() == 0) {
                    SurvivalGames.getInstance().setEnable(false);
                    Bukkit.getPluginManager().callEvent(new GameEndEvent(SurvivalGames.getInstance(), "{none}"));
                } else if (second >= 601) {
                    playTick();
                } else if (second == 600) {
                    Bukkit.broadcastMessage(Messages.LINE);
                    Bukkit.broadcastMessage("");
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        ChatUtil.sendCenteredMessage(player, SurvivalGames.getInstance().getPrefix());
                        ChatUtil.sendCenteredMessage(player, ChatColor.YELLOW + "You have been released!");
                        ChatUtil.sendCenteredMessage(player, ChatColor.YELLOW + "Try to survive for as long as you can");
                    }
                    Bukkit.broadcastMessage("");
                    Bukkit.broadcastMessage(Messages.LINE);

                    SurvivalGames.getInstance().getState().setCanMove(true);
                } else if (second == 180) {
                    playTick();
                    Bukkit.broadcastMessage(Messages.LINE);
                    Bukkit.broadcastMessage("");
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        ChatUtil.sendCenteredMessage(player, SurvivalGames.getInstance().getPrefix());
                        ChatUtil.sendCenteredMessage(player, ChatColor.YELLOW + "The death match is in");
                        ChatUtil.sendCenteredMessage(player, ChatColor.LIGHT_PURPLE + "3:00 " + ChatColor.YELLOW + "Minutes!");
                        Bukkit.broadcastMessage("");
                    }
                    Bukkit.broadcastMessage(Messages.LINE);
                } else if (second == 60) {
                    playTick();
                    Bukkit.broadcastMessage(Messages.LINE);
                    Bukkit.broadcastMessage("");
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        ChatUtil.sendCenteredMessage(player, SurvivalGames.getInstance().getPrefix());
                        ChatUtil.sendCenteredMessage(player, ChatColor.YELLOW + "The death match is in");
                        ChatUtil.sendCenteredMessage(player, ChatColor.LIGHT_PURPLE + "1:00 " + ChatColor.YELLOW + "Minute!");
                        Bukkit.broadcastMessage("");
                    }
                    Bukkit.broadcastMessage(Messages.LINE);
                } else if (second == 0) {
                    playTick();
                    startDeathMatch();
                    Bukkit.broadcastMessage(Messages.LINE);
                    Bukkit.broadcastMessage("");
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        ChatUtil.sendCenteredMessage(player, SurvivalGames.getInstance().getPrefix());
                        ChatUtil.sendCenteredMessage(player, ChatColor.YELLOW + "The death match is commencing...");
                    }
                    Bukkit.broadcastMessage("");
                    Bukkit.broadcastMessage(Messages.LINE);
                }
            }

            /**
             * Death match stage.
             * @info this stage is the finale of the game. Here is where players are in-closed
             * and forced to fight to the death. It will last for approximately 2 minutes.
             * If there is not a winner by the last 20 seconds, poison will be applied to all
             * players to "motivate" them to eliminate all opponents.
             */
            else if (SurvivalGames.getInstance().getState().getName().equalsIgnoreCase("deathmatch")) {
                if (second == 130) {
                    playTick();
                    Bukkit.broadcastMessage(Messages.LINE);
                    Bukkit.broadcastMessage("");
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        ChatUtil.sendCenteredMessage(player, SurvivalGames.getInstance().getPrefix() + " ");
                        ChatUtil.sendCenteredMessage(player, ChatColor.YELLOW + "Releasing in");
                        ChatUtil.sendCenteredMessage(player, ChatColor.LIGHT_PURPLE + "0:10 " + ChatColor.YELLOW + "Seconds!");
                    }
                    Bukkit.broadcastMessage("");
                    Bukkit.broadcastMessage(Messages.LINE);
                } else if (second <= 125 && second > 120) {
                    playTick();
                    Bukkit.broadcastMessage(Messages.LINE);
                    Bukkit.broadcastMessage("");
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        ChatUtil.sendCenteredMessage(player, SurvivalGames.getInstance().getPrefix());
                        ChatUtil.sendCenteredMessage(player, ChatColor.YELLOW + "Releasing in");
                        ChatUtil.sendCenteredMessage(player, ChatColor.LIGHT_PURPLE + "0:0" + (second-120) + " " + ChatColor.YELLOW + "Seconds!");
                    }
                    Bukkit.broadcastMessage("");
                    Bukkit.broadcastMessage(Messages.LINE);
                } else if (second == 120) {
                    playTick();
                    Bukkit.broadcastMessage(Messages.LINE);
                    Bukkit.broadcastMessage("");
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        ChatUtil.sendCenteredMessage(player, SurvivalGames.getInstance().getPrefix());
                        ChatUtil.sendCenteredMessage(player, Messages.EN_DM_POST_RELEASE);
                    }
                    Bukkit.broadcastMessage("");
                    Bukkit.broadcastMessage(Messages.LINE);
                    SurvivalGames.getInstance().getState().setCanMove(true);
                    SurvivalGames.getInstance().getState().setCanPVP(true);
                    SurvivalGames.getInstance().getState().setCanOpen(true);
                    SurvivalGames.getInstance().getState().setCanBreak(true);
                    SurvivalGames.getInstance().getState().setCanPlace(true);
                } else if (second == 60) {
                    playTick();
                    Bukkit.broadcastMessage(Messages.LINE);
                    Bukkit.broadcastMessage("");
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        ChatUtil.sendCenteredMessage(player, SurvivalGames.getInstance().getPrefix());
                        ChatUtil.sendCenteredMessage(player, ChatColor.YELLOW + "The death match ends in");
                        ChatUtil.sendCenteredMessage(player, ChatColor.LIGHT_PURPLE + "1:00 " + ChatColor.YELLOW + "Minute!");
                    }
                    Bukkit.broadcastMessage("");
                    Bukkit.broadcastMessage(Messages.LINE);
                } else if (second == 30) {
                    playTick();
                    Bukkit.broadcastMessage(Messages.LINE);
                    Bukkit.broadcastMessage("");
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        ChatUtil.sendCenteredMessage(player, SurvivalGames.getInstance().getPrefix());
                        ChatUtil.sendCenteredMessage(player, ChatColor.YELLOW + "The death match ends in");
                        ChatUtil.sendCenteredMessage(player, ChatColor.LIGHT_PURPLE + "0:30 " + ChatColor.YELLOW + "Seconds!");
                    }
                    Bukkit.broadcastMessage("");
                    Bukkit.broadcastMessage(Messages.LINE);
                } else if (second == 20) {
                    playTick();
                    Bukkit.broadcastMessage(Messages.LINE);
                    Bukkit.broadcastMessage("");
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        ChatUtil.sendCenteredMessage(player, SurvivalGames.getInstance().getPrefix());
                        ChatUtil.sendCenteredMessage(player, ChatColor.YELLOW + "The death match ends in");
                        ChatUtil.sendCenteredMessage(player, ChatColor.LIGHT_PURPLE + "0:20 " + ChatColor.YELLOW + "Seconds!");
                        ChatUtil.sendCenteredMessage(player, Messages.EN_DM_DISEASE_START);

                        player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 20*20, 1));
                    }
                    Bukkit.broadcastMessage("");
                    Bukkit.broadcastMessage(Messages.LINE);
                } else if (second <= 10 && second >= 1) {
                    playTick();
                    Bukkit.broadcastMessage(Messages.LINE);
                    Bukkit.broadcastMessage("");
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        ChatUtil.sendCenteredMessage(player, SurvivalGames.getInstance().getPrefix());
                        ChatUtil.sendCenteredMessage(player, ChatColor.YELLOW + "The death match ends in");
                        ChatUtil.sendCenteredMessage(player, ChatColor.LIGHT_PURPLE + "0:" + ((second == 10) ? "10" : "0" + second) + ChatColor.YELLOW + " Seconds!");
                    }
                    Bukkit.broadcastMessage("");
                    Bukkit.broadcastMessage(Messages.LINE);
                } else if (second == 0) {
                    if(SurvivalGames.getInstance().getVerifiedPlayers().size() == 1) {
                        Bukkit.getServer().getPluginManager().callEvent(new GameEndEvent(SurvivalGames.getInstance(), Bukkit.getPlayer(SurvivalGames.getInstance().getWinner()).getName()));
                    } else if(SurvivalGames.getInstance().getVerifiedPlayers().size() > 1) {
                        Bukkit.getServer().getPluginManager().callEvent(new GameEndEvent(SurvivalGames.getInstance(), "{none}"));
                    } else {
                        Bukkit.getServer().getPluginManager().callEvent(new GameEndEvent(SurvivalGames.getInstance(), "{none}"));
                    }

                    SurvivalGames.getInstance().getState().setCanMove(true);
                }
            }

            if (second >= 0 && second != -1) {
                second--;
            }
        }

    }

    public void startDeathMatch() {
        GameState dm = new GameState("deathmatch");
        dm.setCanMove(false);
        dm.setCanPVP(false);
        dm.setCanPlace(false);
        dm.setCanBreak(false);
        dm.setCanOpen(false);

        SurvivalGames.getInstance().setState(dm);

        for (UUID uuid : SurvivalGames.getInstance().getVerifiedPlayers()) {
            Player player = Bukkit.getPlayer(uuid);
            int index = SurvivalGames.getInstance().getVerifiedPlayers().indexOf(uuid);
            player.teleport(SurvivalGames.getInstance().getStartingLocations().get(index));

            SurvivalGames.getInstance().getHistory().log(uuid, "Death match started");
        }

        // Lightning because why not. //


        second = 131;
    }

    public void playTick() {
        Bukkit.getOnlinePlayers().forEach((new Consumer<Player>() {
            public void accept(Player p) {
                p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
            }
        }));
    }

    public void handleXP() {
        final AtomicReference<Integer> second = new AtomicReference<Integer>();
        second.compareAndSet(second.get(), this.second);
        Bukkit.getOnlinePlayers().forEach((new Consumer<Player>() {
            public void accept(Player p) {
                if(SurvivalGames.getInstance().getState().getName().equalsIgnoreCase("progress") && second.get() > 600) {
                    p.setLevel((600 - SurvivalGames.getInstance().second));
                } else {
                    p.setHealth(20D);
                    p.setFoodLevel(20);
                    p.setLevel(second.get());
                }
            }
        }));
    }
}
