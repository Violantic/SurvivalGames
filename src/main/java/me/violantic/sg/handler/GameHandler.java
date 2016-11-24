package me.violantic.sg.handler;

import me.violantic.sg.SurvivalGames;
import me.violantic.sg.game.GameState;
import me.violantic.sg.game.event.GameEndEvent;
import me.violantic.sg.game.event.GameStartEvent;
import me.violantic.sg.game.util.ChatUtil;
import me.violantic.sg.game.util.VoteUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

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
        if(!SurvivalGames.getInstance().enabled()) return;

        // Waits for the
        if(lobby) {
            handleXP();
        }

        if(players() >= SurvivalGames.getInstance().minimumPlayers()) {
            if(second <= 0 && SurvivalGames.getInstance().getState().getName().equalsIgnoreCase("waiting")) {
                Bukkit.broadcastMessage(SurvivalGames.getInstance().getPrefix() + "Releasing in 15 second!");
                GameState progress = new GameState("started");
                progress.setCanMove(true);
                progress.setCanPVP(false);
                progress.setCanOpen(false);
                SurvivalGames.getInstance().setState(progress);
                lobby = false;
            } else if (second == 15 && SurvivalGames.getInstance().getState().getName().equalsIgnoreCase("waiting")) {
                SurvivalGames.getInstance().initiateGameMap();
                SurvivalGames.getInstance().setupLocations();
                Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "-----------------------------------------------------");
                for(Player player : Bukkit.getOnlinePlayers()) {
                    ChatUtil.sendCenteredMessage(player, SurvivalGames.getInstance().getPrefix());
                    ChatUtil.sendCenteredMessage(player, "The map voted was " + ChatColor.YELLOW + "" + ChatColor.BOLD + "" + SurvivalGames.getInstance().getGameMapVoter().getWinner());
                }
                Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "-----------------------------------------------------");
            } else if(second == 10 && SurvivalGames.getInstance().getState().getName().equalsIgnoreCase("waiting")) {
                Bukkit.broadcastMessage(SurvivalGames.getInstance().getPrefix() + "Game starting in " + second + " seconds");
                playTick();

                // Start loading new crates so players don't get antsy. //
                SurvivalGames.getInstance().getCrateGenerator().start();
            } else if(second <= 5 && SurvivalGames.getInstance().getState().getName().equalsIgnoreCase("waiting")){
                Bukkit.broadcastMessage(SurvivalGames.getInstance().getPrefix() + "Game starting in " + second + " seconds");
                playTick();
            }

            if(second <= 0 && SurvivalGames.getInstance().getState().getName().equalsIgnoreCase("started")) {
                GameState progress = new GameState("progress");
                progress.setCanMove(false);
                progress.setCanPVP(true);
                progress.setCanOpen(true);
                progress.setCanBreak(true);
                progress.setCanPlace(true);
                SurvivalGames.getInstance().setState(progress);
                second = 10*60;
                Bukkit.getServer().getPluginManager().callEvent(new GameStartEvent(SurvivalGames.getInstance()));
                return;
            }

            if(SurvivalGames.getInstance().getState().getName().equalsIgnoreCase("progress")) {
                SurvivalGames.getInstance().second = second;
                handleXP();
                if(SurvivalGames.getInstance().getVerifiedPlayers().size() == 5) {
                    Bukkit.getServer().getPluginManager().callEvent(new GameEndEvent(SurvivalGames.getInstance(), Bukkit.getPlayer(SurvivalGames.getInstance().getWinner()).getName()));
                    SurvivalGames.getInstance().setEnable(false);
                    return;
                } else if(second == 3*60) {
                    VoteUtil.startDMVote();
                } else if(second == 60) {
                    VoteUtil.endDMVote();
                } else if(second == 585) {
                    Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "-----------------------------------------------------");
                    for(Player player : Bukkit.getOnlinePlayers()) {
                        ChatUtil.sendCenteredMessage(player, SurvivalGames.getInstance().getPrefix());
                        ChatUtil.sendCenteredMessage(player, "You have been released!");
                        ChatUtil.sendCenteredMessage(player, "Try to survive for as long as you can");
                    }
                    Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "-----------------------------------------------------");

                    SurvivalGames.getInstance().getState().setCanMove(true);
                } else if(second > 586) {
                    playTick();
                } else if(second >= 3*60 && second <= 4*60) {
                    if(second % 15 == 0) {
                        //Bukkit.broadcastMessage(SurvivalGames.getInstance().getPrefix() + "Remember to vote for the death match arena with /dmvote <index>");
                    }
                }
            }

            if(second >= 0) {
                second--;
            }
            if(SurvivalGames.getInstance().getState().getName().equalsIgnoreCase("waiting")) {
                if(!voteStarted) {
                    voteStarted = true;
                }
                // TODO - Give all players papers to right click (vote for each map)
                playTick();
            }
        }


    }

    public void playTick() {
        Bukkit.getOnlinePlayers().forEach((new Consumer<Player>() {
            @Override
            public void accept(Player p) {
                p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
            }
        }));
    }

    public void handleXP() {
        final AtomicReference<Integer> second = new AtomicReference<Integer>();
        second.compareAndSet(second.get(), this.second);
        Bukkit.getOnlinePlayers().forEach((new Consumer<Player>() {
            @Override
            public void accept(Player p) {
                p.setLevel(second.get());
            }
        }));
    }
}
