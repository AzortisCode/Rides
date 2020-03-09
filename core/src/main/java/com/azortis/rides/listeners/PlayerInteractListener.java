package com.azortis.rides.listeners;

import com.azortis.rides.CoasterRunnable;
import com.azortis.rides.Rides;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

public class PlayerInteractListener implements Listener {

    private Rides plugin;
    private Location startPoint;
    private Location endPoint;

    public PlayerInteractListener(Rides plugin){
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event){
        if(event.getMaterial() == Material.DIAMOND_AXE && event.getAction() == Action.RIGHT_CLICK_BLOCK){
            if(event.getClickedBlock() != null) {
                startPoint = event.getClickedBlock().getLocation();
                startPoint.setX(startPoint.getX() + .5);
                startPoint.setY(startPoint.getY() + 1);
                startPoint.setZ(startPoint.getZ() + .5);
                event.getPlayer().sendMessage("Set startPoint to: x=" + startPoint.getX() + ", y=" + startPoint.getY() + ", z=" + startPoint.getZ());
            }
        }else if(event.getMaterial() == Material.GOLDEN_AXE && event.getAction() == Action.RIGHT_CLICK_BLOCK){
            if(event.getClickedBlock() != null){
                endPoint = event.getClickedBlock().getLocation();
                endPoint.setX(endPoint.getX() + .5);
                endPoint.setY(endPoint.getY() + 1);
                endPoint.setZ(endPoint.getZ() + .5);
                event.getPlayer().sendMessage("Set endPoint to: x=" + endPoint.getX() + ", y=" + endPoint.getY() + ", z=" + endPoint.getZ());
            }
        }
    }

    @EventHandler
    public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent event){
        if(event.getRightClicked() instanceof ArmorStand){
            Location standLoc = event.getRightClicked().getLocation();
            Bukkit.broadcastMessage(ChatColor.RED + " x=" + standLoc.getX() + ", y=" + standLoc.getY() + ", z=" + standLoc.getZ());
            ArmorStand armorStand = plugin.getNativeAPI().spawnCustomArmorStand(event.getPlayer().getWorld(), standLoc);
            Location stand2Loc = armorStand.getLocation();
            Bukkit.broadcastMessage(ChatColor.LIGHT_PURPLE + " x=" + stand2Loc.getX() + ", y=" + stand2Loc.getY() + ", z=" + stand2Loc.getZ());
            event.getRightClicked().remove();
            event.getPlayer().sendMessage("Going to do vector math.");
            Vector direction = endPoint.toVector().subtract(startPoint.toVector()).normalize();
            double distance = endPoint.distance(startPoint);
            double speed = 0.2;
            long maxTicks = new Double(distance / speed).longValue();
            direction.multiply(speed);
            BukkitTask task = new CoasterRunnable(plugin, armorStand, direction, maxTicks).runTaskTimer(plugin, 0L, 1L);
            Bukkit.getScheduler().runTaskLater(plugin, ()->{
                task.cancel();
                Location eloc = armorStand.getLocation();
                armorStand.setVelocity(new Vector(0,0,0));
                plugin.getNativeAPI().getCustomArmorStand(armorStand).setGravity(false);
                Bukkit.broadcastMessage(ChatColor.RED + " x=" + eloc.getX() + ", y=" + eloc.getY() + ", z=" + eloc.getZ() + ", maxTicks: " + maxTicks + ", distance: " + distance);
                Bukkit.broadcastMessage(ChatColor.LIGHT_PURPLE + "endPoint loc: x=" + endPoint.getX() + ", y=" + endPoint.getY() + ", z=" + endPoint.getZ());
            }, maxTicks + 1);
        }
    }

}
