package com.mineswine.sg.game.util;

import com.mineswine.sg.game.cosmetic.Cosmetic;
import org.bukkit.Effect;
import org.bukkit.Location;
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
        int[][] design = getLegendaryCloak();
        Integer t = 0;
        for (int j = 0; j < design.length; j++) {
            for (int i = 0; i < design[0].length; i++) {
                Location ploc = topLoc.clone().add(baseLoc.getDirection().multiply(-0.06 * t));
                ploc = ploc.subtract(vright.clone().multiply(0.125 * i));
                ploc = ploc.subtract(0, 0.18 * j, 0);
                int b = (design[j][(design[0].length - 1) - i]);
                int[] rgb = getRGB(b);
                int red = rgb[0];
                int green = rgb[1];
                int blue = rgb[2];
                player.getWorld().spigot().playEffect(ploc, Effect.COLOURED_DUST, 0, 0, red, green, blue, 1, 0, 16);
            }
            t++;
        }
    }

    public static void displayHead(Player player) {
        Location baseLoc = player.getLocation();
        baseLoc.setPitch(0);
        double angle = angleFunc(baseLoc.getDirection());
        double right = angle + (Math.PI / 2);
        right = right > Math.PI ? right - 2 * Math.PI : right;
        Vector vright = new Vector(Math.cos(right), 0, Math.sin(right));
        Location topLoc = baseLoc.add(0, 2.4D, 0).add(vright.clone().multiply(0.3));
        int[][] design = getLegendaryHead();
        Integer t = 0;
        for (int j = 0; j < design.length; j++) {
            for (int i = 0; i < design[0].length; i++) {
                Location ploc = topLoc.clone().add(baseLoc.getDirection().multiply(-0.06 * t));
                ploc = ploc.subtract(vright.clone().multiply(0.125 * i));
                ploc = ploc.subtract(0, 0.18 * j, 0);
                int b = (design[j][(design[0].length - 1) - i]);
                if(b == 0) break;
                int[] rgb = getRGB(b);
                int red = rgb[0];
                int green = rgb[1];
                int blue = rgb[2];
                player.getWorld().spigot().playEffect(ploc, Effect.COLOURED_DUST, 0, 0, red, green, blue, 1, 0, 16);
            }
            t++;
        }
    }

    /**
     *
     * RED     - 1
     * ORANGE  - 2
     * YELLOW  - 3
     * GREEN   - 4
     * BLUE    - 5
     * INDIGO  - 6
     * VIOLET  - 7
     * <p>
     * BLACK - 8
     * GRAY  - 9
     * WHITE - 10
     * @param id
     * @return
     */
    public static int[] getRGB(int id) {
        return
                (id == 1) ? new int[]{245, 90, 90} :
                        (id == 2) ? new int[]{218, 133, 84} :
                                (id == 3) ? new int[]{255, 235, 156} :
                                        (id == 4) ? new int[]{140, 255, 165} :
                                                (id == 5) ? new int[]{115, 225, 255} :
                                                        (id == 6) ? new int[]{75, 75, 215} :
                                                                (id == 7) ? new int[]{175, 64, 209} :
                                                                        (id == 8) ? new int[]{0, 0, 0} :
                                                                                (id == 9) ? new int[]{135, 135, 135} :
                                                                                        (id == 10) ? new int[]{255, 255, 255} :
                                                                                                new int[]{255, 255, 255};
    }

    public static int[][] getLegendaryHead() {
        return new int[][]{
                {0, 1, 0, 0, 0, 1, 0},
                {1, 0, 0, 7, 0, 0, 1},
                {1, 0, 0, 7, 0, 0, 1},
                {0, 1, 0, 0, 0, 1, 0},
                {0, 1, 0, 0, 0, 1, 0},
                {0, 0, 1, 0, 1, 0, 0}
        };
    }

    public static int[][] getLegendaryCloak() {
        return new int[][]{
                {7, 3, 7, 7, 7, 3, 7},
                {7, 3, 3, 7, 3, 3, 7},
                {7, 3, 7, 3, 7, 3, 7},
                {7, 3, 7, 7, 7, 3, 7},
                {7, 3, 7, 7, 7, 3, 7}
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
