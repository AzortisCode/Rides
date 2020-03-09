package com.azortis.rides;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class CoasterRunnable extends BukkitRunnable {

    private Rides plugin;
    private ArmorStand stand;
    private Vector direction;
    private long maxTicks;
    private int tick;

    public CoasterRunnable(Rides plugin, ArmorStand stand, Vector direction, long maxTicks){
        this.plugin = plugin;
        this.stand = stand;
        this.direction = direction;
        this.maxTicks = maxTicks;
        this.tick = 0;
    }

    @Override
    public void run() {
        if(!plugin.getNativeAPI().getCustomArmorStand(stand).hasGravity()){
            plugin.getNativeAPI().getCustomArmorStand(stand).setGravity(true);
        }
        if(tick <= maxTicks){
            stand.setVelocity(direction);
        }
        Location loc = stand.getLocation();
        Bukkit.broadcastMessage(ChatColor.RED + "" + direction.length());
        Bukkit.broadcastMessage(ChatColor.GREEN + " tick: " + tick + " out of: " + maxTicks + ", x=" + loc.getX() + ", y=" + loc.getY() + ", z=" + loc.getZ());
        tick++;
    }

}
