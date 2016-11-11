package me.violantic.sg.game.util;

import org.bukkit.Location;
import org.bukkit.World;

/**
 * Created by Ethan on 11/3/2016.
 */
public class LocationUtil {

    public static Location getLocation(World w, String location){
        String[] loc = location.split(",");
        double x,y,z;
        x = Integer.parseInt(loc[0]);
        y = Integer.parseInt(loc[1]);
        z = Integer.parseInt(loc[2]);

        return new Location(w, x, y, z);
    }
}
