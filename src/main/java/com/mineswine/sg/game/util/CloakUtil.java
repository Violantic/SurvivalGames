package com.mineswine.sg.game.util;

import com.mineswine.sg.game.cosmetic.Cosmetic;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

/**
 * Created by Ethan on 12/23/2016.
 */
public class CloakUtil {

    public static void displayCloak(Player player, Cosmetic cosmetic) {
        Location baseLoc = player.getLocation();
        baseLoc.setPitch(0);
        double angle = angleFunc(baseLoc.getDirection());
        double right = angle + (Math.PI / 2);
        right = right > Math.PI ? right - 2 * Math.PI : right;
        Vector vright = new Vector(Math.cos(right), 0, Math.sin(right));
        Location topLoc = baseLoc.add(0, 1.4D, 0).add(vright.clone().multiply(0.3)).add(baseLoc.getDirection().multiply(-0.2));
        int[][] design = getLegendaryWings();
        Integer t = 0;
        for (int j = 0; j < design.length; j++) {
            for (int i = 0; i < design[0].length; i++) {
                Location ploc = topLoc.clone().add(baseLoc.getDirection().multiply(-0.06 * t));
                ploc = ploc.subtract(vright.clone().multiply(0.125 * i));
                ploc = ploc.subtract(0, 0.18 * j, 0);
                int b = (design[j][(design[0].length - 1) - i]);
                if (b == 1) {
                    player.getWorld().spawnParticle(Particle.SMOKE_NORMAL, ploc, 1, 0D, 0D, 0D, 36D);
                } else if (b == 2) {
                    player.getWorld().spawnParticle(Particle.FLAME, ploc, 1, 0D, 0D, 0D, 36D);
                } else if (b == 3) {
                    //player.getWorld().spawnParticle(Particle.SUSPENDED_DEPTH, ploc, 1, 0D, 0D, 0D, 36D);
                }
            }
            t++;
        }
    }

    public static int[][] getLegendaryHead() {
        return new int[][]{
                {},
        };
    }

    public static int[][] getLegendaryCloak() {
        return new int[][]{
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 1, 2, 1, 1, 1, 2, 1, 0},
                {0, 1, 2, 2, 1, 2, 2, 1, 0},
                {0, 1, 2, 1, 2, 1, 2, 1, 0},
                {0, 1, 2, 1, 1, 1, 2, 1, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0}
        };
    }

    public static int[][] getLegendaryWings() {
        return new int[][]{
                /**
                 * Dimensions: V->(27x16)
                 */
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1},
                {1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1},
                {1, 3, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 3, 1},
                {1, 3, 3, 1, 1, 0, 0, 0, 1, 0, 0, 1, 1, 1, 1, 1, 0, 0, 1, 0, 0, 0, 1, 1, 3, 3, 1},
                {0, 1, 3, 3, 3, 1, 1, 1, 3, 1, 1, 2, 3, 3, 3, 2, 1, 1, 3, 1, 1, 1, 3, 3, 3, 1, 0},
                {0, 1, 3, 3, 3, 3, 3, 3, 3, 3, 3, 2, 2, 3, 2, 2, 3, 3, 3, 3, 3, 3, 3, 3, 3, 1, 0},
                {0, 0, 1, 3, 3, 3, 3, 3, 3, 3, 3, 2, 3, 2, 3, 2, 3, 3, 3, 3, 3, 3, 3, 3, 1, 0, 0},
                {0, 0, 1, 3, 1, 3, 3, 3, 3, 3, 3, 2, 3, 3, 3, 2, 3, 3, 3, 3, 3, 3, 1, 3, 1, 0, 0},
                {0, 0, 1, 3, 1, 3, 3, 1, 3, 1, 1, 1, 3, 3, 3, 1, 1, 1, 3, 1, 3, 3, 1, 3, 1, 0, 0},
                {0, 0, 0, 1, 0, 1, 3, 1, 3, 1, 0, 1, 1, 1, 1, 1, 0, 1, 3, 1, 3, 1, 0, 1, 0, 0, 0},
                {0, 0, 0, 1, 0, 1, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 1, 0, 1, 0, 0, 0},
                {0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0},
        };
    }

    public static double angleFunc(Vector v1) {
        Vector v1n = new Vector(v1.getX(), 0, v1.getZ()).normalize();
        double angle = 0;
        angle = Math.acos(Math.abs(v1n.getX()));
        if (v1n.getX() >= 0) {
            if (v1n.getZ() >= 0) {

            } else {
                angle = Math.PI * 2 - angle;
            }
        } else {
            if (v1n.getZ() >= 0) {
                angle = Math.PI - angle;
            } else {
                angle = Math.PI + angle;
            }
        }

        return angle;
    }

}
