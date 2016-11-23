package me.violantic.sg.game.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.ArrayList;

/**
 * Created by Ethan on 11/3/2016.
 */
public class LocationUtil {

    public static Location getLocation(String name, String location){
        String[] loc = location.split(",");
        double x,y,z;
        x = Integer.parseInt(loc[0]);
        y = Integer.parseInt(loc[1]);
        z = Integer.parseInt(loc[2]);

        return new Location(Bukkit.getWorld(name), x, y, z);
    }

    public static ArrayList<Location> getCircle(Location center, double radius, int amount)
    {
        World world = center.getWorld();
        double increment = (2 * Math.PI) / amount;
        ArrayList<Location> locations = new ArrayList<Location>();
        for(int i = 0;i < amount; i++)
        {
            double angle = i * increment;
            double x = center.getX() + (radius * Math.cos(angle));
            double z = center.getZ() + (radius * Math.sin(angle));
            locations.add(new Location(world, x, center.getY(), z));
        }
        return locations;
    }
}
