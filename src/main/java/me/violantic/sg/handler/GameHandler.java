package me.violantic.sg.handler;

import me.violantic.sg.SurvivalGames;
import org.bukkit.Bukkit;
import org.bukkit.Sound;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by Ethan on 11/3/2016.
 */
public class GameHandler implements Runnable {

    private int second = 120;

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
        if(players() >= SurvivalGames.getInstance().minimumPlayers()) {
            second--;
            playTick();
            handleXP();
        }


    }

    public void playTick() {
        Bukkit.getOnlinePlayers().forEach((p -> p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1)));
    }

    public void handleXP() {
        AtomicReference<Integer> second = new AtomicReference<>();
        Bukkit.getOnlinePlayers().forEach((p -> p.setLevel(second.get())));
    }
}
