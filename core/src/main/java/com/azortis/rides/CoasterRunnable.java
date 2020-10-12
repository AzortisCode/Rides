/*
 * Create custom rides for your Minecraft server.
 *     Copyright (C) 2020  Azortis
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

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
    private HashMap<Long, Float> yawPath;
    private HashMap<Long, Float> pitchPath;
    private long maxTicks;
    private long tick;

    public CoasterRunnable(Rides plugin, ArmorStand stand, HashMap<Long, Vector> path, HashMap<Long, Float> yawPath, HashMap<Long, Float> pitchPath , long maxTicks){
        this.plugin = plugin;
        this.stand = stand;
        this.path = path;
        this.yawPath = yawPath;
        this.pitchPath = pitchPath;
        this.maxTicks = maxTicks;
        this.tick = 0;
    }

    @Override
    public void run() {
        /*if(!plugin.getNativeAPI().getCustomArmorStand(stand).hasGravity()){
            plugin.getNativeAPI().getCustomArmorStand(stand).setGravity(true);
        }*/
        if(tick <= maxTicks){
            stand.setVelocity(path.get(tick));
            stand.setRotation(yawPath.get(tick) + stand.getLocation().getYaw(), pitchPath.get(tick) + stand.getLocation().getPitch());
        }
        Location loc = stand.getLocation();
        Bukkit.broadcastMessage(ChatColor.RED + "" + path.get(tick).length());
        Bukkit.broadcastMessage(ChatColor.GREEN + " tick: " + tick + " out of: " + maxTicks + ", x=" + loc.getX() + ", y=" + loc.getY() + ", z=" + loc.getZ());
        tick++;
    }

}
