package me.violantic.sg.game.util;

import me.violantic.sg.SurvivalGames;

import java.util.logging.Level;

/**
 * Created by Ethan on 11/27/2016.
 */
public class LagUtil implements Runnable {

    public static int TICK_COUNT = 0;
    public static long[] TICKS = new long[600];
    public static long LAST_TICK = 0L;

    public LagUtil() {
    }

    public double getTPS() {
        return getTPS(100);
    }

    public double getTPS(int ticks) {
        if (TICK_COUNT < ticks) {
            return 20.0D;
        }
        int target = (TICK_COUNT - 1 - ticks) % TICKS.length;
        long elapsed = System.currentTimeMillis() - TICKS[target];

        return ticks / (elapsed / 1000.0D);
    }

    public long getElapsed(int tickID) {
        long time = TICKS[(tickID % TICKS.length)];
        return System.currentTimeMillis() - time;
    }

    public void run() {
        TICKS[(TICK_COUNT % TICKS.length)] = System.currentTimeMillis();
        TICK_COUNT += 1;

        if(getTPS() <= 18.5 && SurvivalGames.getInstance().getCrateGenerator().isEnabled()) {
            // Stop crate generation - it lags the server. //
            SurvivalGames.getInstance().getLogger().log(Level.WARNING, "TPS is below 18.5, disabling crate generation.");
            SurvivalGames.getInstance().getCrateGenerator().disable();
        } else if(getTPS() > 18.5 && (!SurvivalGames.getInstance().getCrateGenerator().isEnabled())) {
            // Restart crate generation - the server can handle it. //
            SurvivalGames.getInstance().getLogger().log(Level.INFO, "TPS is now above 18.5, enabling crate generation.");
            SurvivalGames.getInstance().getCrateGenerator().enable();
        }
    }

}
