package me.violantic.sg.handler;

import me.violantic.sg.SurvivalGames;
import me.violantic.sg.game.GameState;
import me.violantic.sg.game.event.GameStartEvent;
import me.violantic.sg.game.util.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

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
            if (second == 0 && SurvivalGames.getInstance().getState().getName().equalsIgnoreCase("waiting")) {
                GameState progress = new GameState("started");
                progress.setCanMove(true);
                progress.setCanPVP(false);
                progress.setCanOpen(false);
                SurvivalGames.getInstance().setState(progress);
            } else if (second == 15 && SurvivalGames.getInstance().getState().getName().equalsIgnoreCase("waiting")) {
                Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "-----------------------------------------------------");
                Bukkit.broadcastMessage("");
                for (Player player : Bukkit.getOnlinePlayers()) {
                    ChatUtil.sendCenteredMessage(player, SurvivalGames.getInstance().getPrefix());
                    ChatUtil.sendCenteredMessage(player, "The map voted was " + ChatColor.YELLOW + "" + ChatColor.BOLD + "" + SurvivalGames.getInstance().getGameMapVoter().getWinner());
                }
                Bukkit.broadcastMessage("");
                Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "-----------------------------------------------------");
                SurvivalGames.getInstance().initiateGameMap();
            } else if (second == 10 && SurvivalGames.getInstance().getState().getName().equalsIgnoreCase("waiting")) {
                Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "-----------------------------------------------------");
                Bukkit.broadcastMessage("");
                for (Player player : Bukkit.getOnlinePlayers()) {
                    ChatUtil.sendCenteredMessage(player, SurvivalGames.getInstance().getPrefix());
                    ChatUtil.sendCenteredMessage(player, ChatColor.YELLOW + "Game starting in " + second + " seconds");
                }
                Bukkit.broadcastMessage("");
                Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "-----------------------------------------------------");
                SurvivalGames.getInstance().setupLocations();
                playTick();

                // Start loading new crates so players don't get antsy. //
                SurvivalGames.getInstance().getCrateGenerator().start(SurvivalGames.getInstance().getGameMap().getName());
            } else if (second <= 5 && SurvivalGames.getInstance().getState().getName().equalsIgnoreCase("waiting")) {
                Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "-----------------------------------------------------");
                Bukkit.broadcastMessage("");
                for (Player player : Bukkit.getOnlinePlayers()) {
                    ChatUtil.sendCenteredMessage(player, SurvivalGames.getInstance().getPrefix());
                    ChatUtil.sendCenteredMessage(player, ChatColor.YELLOW + "Game starting in " + second + " seconds");
                }
                Bukkit.broadcastMessage("");
                Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "-----------------------------------------------------");
                playTick();
            } else if (second <= 0 && SurvivalGames.getInstance().getState().getName().equalsIgnoreCase("started")) {
                GameState progress = new GameState("progress");
                progress.setCanMove(false);
                progress.setCanPVP(true);
                progress.setCanOpen(true);
                progress.setCanBreak(true);
                progress.setCanPlace(true);
                SurvivalGames.getInstance().setState(progress);
                second = 10 * 60;
                Bukkit.getServer().getPluginManager().callEvent(new GameStartEvent(SurvivalGames.getInstance()));
                return;
            } else if (SurvivalGames.getInstance().getState().getName().equalsIgnoreCase("progress")) {
                SurvivalGames.getInstance().second = second;
                handleXP();
                if (SurvivalGames.getInstance().getVerifiedPlayers().size() == 5) {
                    Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "-----------------------------------------------------");
                    Bukkit.broadcastMessage("");
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        ChatUtil.sendCenteredMessage(player, SurvivalGames.getInstance().getPrefix());
                        Bukkit.broadcastMessage("");
                        ChatUtil.sendCenteredMessage(player, ChatColor.YELLOW + "The Death Match has started early?");
                        ChatUtil.sendCenteredMessage(player, ChatColor.DARK_RED + "" + ChatColor.BOLD + "FIGHT TO THE DEATH!!!");
                        Bukkit.broadcastMessage("");
                    }
                    Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "-----------------------------------------------------");
                    return;
                } else if (second == 180) {
                    Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "-----------------------------------------------------");
                    Bukkit.broadcastMessage("");
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        ChatUtil.sendCenteredMessage(player, SurvivalGames.getInstance().getPrefix());
                        Bukkit.broadcastMessage("");
                        ChatUtil.sendCenteredMessage(player, ChatColor.YELLOW + "The death match is in");
                        ChatUtil.sendCenteredMessage(player, ChatColor.LIGHT_PURPLE + "3:00 " + ChatColor.YELLOW + "Minutes!");
                        Bukkit.broadcastMessage("");
                    }
                    Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "-----------------------------------------------------");
                } else if (second == 60) {
                    Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "-----------------------------------------------------");
                    Bukkit.broadcastMessage("");
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        ChatUtil.sendCenteredMessage(player, SurvivalGames.getInstance().getPrefix());
                        Bukkit.broadcastMessage("");
                        ChatUtil.sendCenteredMessage(player, ChatColor.YELLOW + "The death match is in");
                        ChatUtil.sendCenteredMessage(player, ChatColor.LIGHT_PURPLE + "1:00 " + ChatColor.YELLOW + "Minute!");
                        Bukkit.broadcastMessage("");
                    }
                    Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "-----------------------------------------------------");
                } else if (second == 600) {
                    Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "-----------------------------------------------------");
                    Bukkit.broadcastMessage("");
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        ChatUtil.sendCenteredMessage(player, SurvivalGames.getInstance().getPrefix());
                        ChatUtil.sendCenteredMessage(player, ChatColor.YELLOW + "You have been released!");
                        ChatUtil.sendCenteredMessage(player, ChatColor.YELLOW + "Try to survive for as long as you can");
                    }
                    Bukkit.broadcastMessage("");
                    Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "-----------------------------------------------------");

                    SurvivalGames.getInstance().getState().setCanMove(true);
                } else if (second >= 600) {
                    playTick();
                } else if (second == 0) {
                    startDeathMatch();
                    Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "-----------------------------------------------------");
                    Bukkit.broadcastMessage("");
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        ChatUtil.sendCenteredMessage(player, SurvivalGames.getInstance().getPrefix());
                        ChatUtil.sendCenteredMessage(player, ChatColor.YELLOW + "The death match has started...");
                        ChatUtil.sendCenteredMessage(player, ChatColor.DARK_RED + "" + ChatColor.BOLD + "FIGHT TO THE DEATH!!!");
                    }
                    Bukkit.broadcastMessage("");
                    Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "-----------------------------------------------------");
                }
            } else if(SurvivalGames.getInstance().getState().getName().equalsIgnoreCase("deathmatch")) {
                if(second == 130) {
                    playTick();
                    Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "-----------------------------------------------------");
                    Bukkit.broadcastMessage("");
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        ChatUtil.sendCenteredMessage(player, SurvivalGames.getInstance().getPrefix());
                        ChatUtil.sendCenteredMessage(player, ChatColor.YELLOW + "Releasing in");
                        ChatUtil.sendCenteredMessage(player, ChatColor.LIGHT_PURPLE + "0:10 " + ChatColor.YELLOW + "Seconds!");
                    }
                    Bukkit.broadcastMessage("");
                    Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "-----------------------------------------------------");
                } else if(second <= 125 && second >= 120) {
                    playTick();
                    Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "-----------------------------------------------------");
                    Bukkit.broadcastMessage("");
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        ChatUtil.sendCenteredMessage(player, SurvivalGames.getInstance().getPrefix());
                        ChatUtil.sendCenteredMessage(player, ChatColor.YELLOW + "Releasing in");
                        ChatUtil.sendCenteredMessage(player, ChatColor.LIGHT_PURPLE + "0:0" + second + " " + ChatColor.YELLOW + "Seconds!");
                    }
                    Bukkit.broadcastMessage("");
                    Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "-----------------------------------------------------");
                } else if(second == 120) {
                    Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "-----------------------------------------------------");
                    Bukkit.broadcastMessage("");
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        ChatUtil.sendCenteredMessage(player, SurvivalGames.getInstance().getPrefix());
                        ChatUtil.sendCenteredMessage(player, ChatColor.YELLOW + "The death match ends in");
                        ChatUtil.sendCenteredMessage(player, ChatColor.LIGHT_PURPLE + "2:00 " + ChatColor.YELLOW + "Minute!");
                    }
                    Bukkit.broadcastMessage("");
                    Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "-----------------------------------------------------");
                } else if(second == 60) {
                    Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "-----------------------------------------------------");
                    Bukkit.broadcastMessage("");
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        ChatUtil.sendCenteredMessage(player, SurvivalGames.getInstance().getPrefix());
                        ChatUtil.sendCenteredMessage(player, ChatColor.YELLOW + "The death match ends in");
                        ChatUtil.sendCenteredMessage(player, ChatColor.LIGHT_PURPLE + "1:00 " + ChatColor.YELLOW + "Minute!");
                    }
                    Bukkit.broadcastMessage("");
                    Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "-----------------------------------------------------");
                } else if(second == 30) {
                    Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "-----------------------------------------------------");
                    Bukkit.broadcastMessage("");
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        ChatUtil.sendCenteredMessage(player, SurvivalGames.getInstance().getPrefix());
                        ChatUtil.sendCenteredMessage(player, ChatColor.YELLOW + "The death match ends in");
                        ChatUtil.sendCenteredMessage(player, ChatColor.LIGHT_PURPLE + "0:30 " + ChatColor.YELLOW + "Seconds!");
                    }
                    Bukkit.broadcastMessage("");
                    Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "-----------------------------------------------------");
                } else if(second == 20) {
                    Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "-----------------------------------------------------");
                    Bukkit.broadcastMessage("");
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        ChatUtil.sendCenteredMessage(player, SurvivalGames.getInstance().getPrefix());
                        ChatUtil.sendCenteredMessage(player, ChatColor.YELLOW + "The death match ends in");
                        ChatUtil.sendCenteredMessage(player, ChatColor.LIGHT_PURPLE + "0:20 " + ChatColor.YELLOW + "Seconds!");
                        ChatUtil.sendCenteredMessage(player, ChatColor.GREEN + "" + ChatColor.BOLD + "Disease is spreading...");

                        player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 20, 1));
                    }
                    Bukkit.broadcastMessage("");
                    Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "-----------------------------------------------------");
                } else if(second <= 10) {
                    Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "-----------------------------------------------------");
                    Bukkit.broadcastMessage("");
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        ChatUtil.sendCenteredMessage(player, SurvivalGames.getInstance().getPrefix());
                        ChatUtil.sendCenteredMessage(player, ChatColor.YELLOW + "The death match ends in");
                        ChatUtil.sendCenteredMessage(player, ChatColor.LIGHT_PURPLE + "0:20 " + ChatColor.YELLOW + "Seconds!");
                        ChatUtil.sendCenteredMessage(player, ChatColor.GREEN + "" + ChatColor.BOLD + "Disease is spreading...");

                        player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 20, 1));
                    }
                    Bukkit.broadcastMessage("");
                    Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "-----------------------------------------------------");
                }
            }

            if (second >= 0) {
                second--;
            } else if (SurvivalGames.getInstance().getState().getName().equalsIgnoreCase("waiting")) {
                if (!voteStarted) {
                    voteStarted = true;
                }
                // TODO - Give all players papers to right click (vote for each map)
            }
        }


    }

    public void startDeathMatch() {
        GameState dm = new GameState("deathmatch");
        dm.setCanMove(false);
        dm.setCanPVP(false);
        dm.setCanPlace(false);
        dm.setCanBreak(false);

        second = 135;
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
                p.setLevel(second.get());
            }
        }));
    }
}
