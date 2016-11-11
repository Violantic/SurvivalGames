package me.violantic.sg.game.util;

import net.minecraft.server.v1_9_R1.EnumParticle;
import net.minecraft.server.v1_9_R1.PacketPlayOutWorldParticles;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ethan on 11/6/2016.
 */
public class CloakUtil {

    private JavaPlugin plugin;
    private Map<String, Integer[][]> design = new HashMap<String, Integer[][]>();

    public CloakUtil(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void display(Player pl) {
        Location baseLoc = pl.getLocation();
        baseLoc.setPitch(0);
        double angle = angleFunc(baseLoc.getDirection());
        double right = angle + (Math.PI / 2);
        right = right > Math.PI ? right - 2 * Math.PI : right;
        Vector vright = new Vector(Math.cos(right), 0, Math.sin(right));
        Location topLoc = baseLoc.add(0, 1.4D, 0).add(vright.clone().multiply(0.3)).add(baseLoc.getDirection().multiply(-0.2));
        Integer t = 0;
        for (Integer j = 0; j < 6; j++) {
            for (Integer i = 0; i < 4; i++) {
                Location ploc = topLoc.clone().add(baseLoc.getDirection().multiply(-0.1 * t));
                ploc = ploc.subtract(vright.clone().multiply(0.2 * i));
                ploc = ploc.subtract(0, 0.25 * j, 0);
                if (!design.containsKey(pl.getName())) {
                    PacketPlayOutWorldParticles cape = new PacketPlayOutWorldParticles(EnumParticle.FLAME, false, (float) ploc.getX(), (float) ploc.getY(), (float) ploc.getZ(), 0, 0, 0, 0, 1, 0);
                    for (Player show : plugin.getServer().getOnlinePlayers()) {
                            ((CraftPlayer) show).getHandle().playerConnection.sendPacket(cape);
                    }
                }
            }
            t++;
        }
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

    public static Integer[][] presetCloakDesign(Integer i) {
        switch (i) {
            case 0:
                return new Integer[][]{
                        {0, 0, 0, 0},
                        {0, 0, 0, 0},
                        {0, 0, 0, 0},
                        {0, 0, 0, 0},
                        {0, 0, 0, 0},
                        {0, 0, 0, 0}};
            case 1:
                return new Integer[][]{
                        {3, 3, 3, 3},
                        {3, 3, 3, 3},
                        {3, 4, 4, 3},
                        {3, 4, 4, 3},
                        {3, 3, 3, 3},
                        {3, 3, 3, 3}};
            case 2:
                return new Integer[][]{
                        {3, 3, 3, 3},
                        {3, 2, 2, 3},
                        {3, 4, 1, 3},
                        {3, 1, 4, 3},
                        {3, 2, 2, 3},
                        {3, 3, 3, 3}};
            case 3:
                return new Integer[][]{
                        {3, 3, 3, 3},
                        {1, 2, 3, 4},
                        {4, 1, 2, 3},
                        {3, 4, 1, 2},
                        {2, 3, 4, 1},
                        {3, 3, 3, 3}};
            case 4:
                return new Integer[][]{
                        {8, 5, 5, 8},
                        {5, 3, 3, 5},
                        {5, 4, 4, 5},
                        {5, 4, 4, 5},
                        {5, 3, 3, 5},
                        {7, 5, 5, 7}};
            case 5:
                return new Integer[][]{
                        {3, 3, 3, 3},
                        {3, 5, 5, 3},
                        {3, 4, 4, 3},
                        {3, 4, 4, 3},
                        {3, 5, 5, 3},
                        {3, 3, 3, 3}};
            case 6:
                return new Integer[][]{
                        {3, 4, 4, 3},
                        {3, 3, 3, 3},
                        {5, 3, 3, 5},
                        {5, 3, 3, 5},
                        {3, 3, 3, 3},
                        {3, 4, 4, 3}};
        }
        return null;
    }

}
