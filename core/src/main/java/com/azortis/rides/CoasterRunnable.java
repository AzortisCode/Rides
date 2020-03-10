package com.azortis.rides;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashMap;

public class CoasterRunnable extends BukkitRunnable {

    private Rides plugin;
    private ArmorStand stand;
    private HashMap<Long, Vector> path;
    private long maxTicks;
    private long tick;

    public CoasterRunnable(Rides plugin, ArmorStand stand, HashMap<Long, Vector> path, long maxTicks){
        this.plugin = plugin;
        this.stand = stand;
        this.path = path;
        this.maxTicks = maxTicks;
        this.tick = 0;
    }

    @Override
    public void run() {
        if(!plugin.getNativeAPI().getCustomArmorStand(stand).hasGravity()){
            plugin.getNativeAPI().getCustomArmorStand(stand).setGravity(true);
        }
        if(tick <= maxTicks){
            stand.setVelocity(path.get(tick));
        }
        Location loc = stand.getLocation();
        Bukkit.broadcastMessage(ChatColor.RED + "" + path.get(tick).length());
        Bukkit.broadcastMessage(ChatColor.GREEN + " tick: " + tick + " out of: " + maxTicks + ", x=" + loc.getX() + ", y=" + loc.getY() + ", z=" + loc.getZ());
        tick++;
    }

}
