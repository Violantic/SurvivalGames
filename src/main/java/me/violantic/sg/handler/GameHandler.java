package me.violantic.sg.handler;

import me.violantic.sg.SurvivalGames;
import me.violantic.sg.game.GameState;
import me.violantic.sg.game.event.GameStartEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by Ethan on 11/3/2016.
 */
public class GameHandler implements Runnable {

    private int second = 120;
    private boolean lobby = true;
    private boolean started = false;
    private boolean inProgress = false;
    private boolean ready = false;

    public GameHandler() {
        second = 0;
    }

    public int players() {
        return Bukkit.getOnlinePlayers().size();
    }

    public int getSecond() {
        return second;
    }

    /**
     * Second will increment every 20 ticks (If healthy server state, every 1 second).
     */
    public void run() {
        // Waits for the
        if(lobby) {
            handleXP();
            return;
        }

        if(players() >= SurvivalGames.getInstance().minimumPlayers()) {
            if(second <= 0 && !inProgress) {
                GameState progress = new GameState("started");
                progress.setCanMove(false);
                progress.setCanPVP(false);
                progress.setCanOpen(false);
                SurvivalGames.getInstance().setState(progress);
                Bukkit.getServer().getPluginManager().callEvent(new GameStartEvent(SurvivalGames.getInstance()));
                inProgress = true;
                second = 15;
                return;
            }

            if(second <= 0 && inProgress && !ready) {
                GameState progress = new GameState("progress");
                progress.setCanMove(true);
                progress.setCanPVP(true);
                progress.setCanOpen(true);
                progress.setCanBreak(true);
                progress.setCanPlace(true);
                SurvivalGames.getInstance().setState(progress);
                ready = true;
                return;
            }

            if(!ready && started && inProgress && second <= 15 && second > 1) {
                Bukkit.broadcastMessage(SurvivalGames.getInstance().getPrefix() + "Releasing in " + ChatColor.YELLOW + "" + ChatColor.BOLD + "" + second);
            }

            second--;
            playTick();
            handleXP();

            lobby = false;
            if(!started) {
                started = true;
                if(second % 30 == 0) {
                    Bukkit.broadcastMessage(SurvivalGames.getInstance().getPrefix() + "The game will begin in " + ChatColor.YELLOW + "" + ChatColor.BOLD + (second / 60));
                    return;
                }
                Bukkit.broadcastMessage(SurvivalGames.getInstance().getPrefix() + "The game will begin in " + ChatColor.YELLOW + "" + ChatColor.BOLD + (second / 60));
            }
        }


    }

    public void playTick() {
        Bukkit.getOnlinePlayers().forEach((p -> p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1)));
    }

    public void handleXP() {
        AtomicReference<Integer> second = new AtomicReference<>();
        second.compareAndSet(second.get(), this.second);
        Bukkit.getOnlinePlayers().forEach((p -> p.setLevel(second.get())));
    }
}
