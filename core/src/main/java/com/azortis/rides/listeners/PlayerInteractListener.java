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

import java.util.ArrayList;
import java.util.HashMap;

public class PlayerInteractListener implements Listener {

    private Rides plugin;
    private ArrayList<Location> pathPoints = new ArrayList<>();

    public PlayerInteractListener(Rides plugin){
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event){
        if(event.getMaterial() == Material.DIAMOND_AXE && event.getAction() == Action.RIGHT_CLICK_BLOCK){
            if(event.getClickedBlock() != null) {
                Location pathPoint = event.getClickedBlock().getLocation();
                pathPoint.setX(pathPoint.getX() + .5);
                pathPoint.setY(pathPoint.getY() + 1);
                pathPoint.setZ(pathPoint.getZ() + .5);
                pathPoints.add(pathPoint);
                event.getPlayer().sendMessage("Added pathPoint: x=" + pathPoint.getX() + ", y=" + pathPoint.getY() + ", z=" + pathPoint.getZ());
            }
        }else if(event.getMaterial() == Material.STICK && event.getAction() == Action.RIGHT_CLICK_AIR){
            if(!pathPoints.isEmpty()) {
                pathPoints.clear();
                event.getPlayer().sendMessage(ChatColor.RED + "Cleared path!");
            } else{
                event.getPlayer().sendMessage(ChatColor.GREEN + "Path was already empty!");
            }
        }
    }

    @EventHandler
    public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent event){
        if(event.getRightClicked() instanceof ArmorStand && pathPoints.size() > 1){
            Location standLoc = event.getRightClicked().getLocation();
            Bukkit.broadcastMessage(ChatColor.RED + " x=" + standLoc.getX() + ", y=" + standLoc.getY() + ", z=" + standLoc.getZ());
            ArmorStand armorStand = plugin.getNativeAPI().spawnCustomArmorStand(event.getPlayer().getWorld(), standLoc);
            Location stand2Loc = armorStand.getLocation();
            Bukkit.broadcastMessage(ChatColor.LIGHT_PURPLE + " x=" + stand2Loc.getX() + ", y=" + stand2Loc.getY() + ", z=" + stand2Loc.getZ());
            event.getRightClicked().remove();
            event.getPlayer().sendMessage("Going to do vector math.");
            HashMap<Long, Vector> vectorPath = new HashMap<>();
            long currentTick = 0;
            double speed = 0.2;
            for (int i = 0; i < (pathPoints.size() - 1); i++){
                Bukkit.broadcastMessage(String.valueOf(i));
                Location originPoint = pathPoints.get(i);
                Location nextPoint = pathPoints.get(i + 1);
                Vector direction = nextPoint.toVector().subtract(originPoint.toVector()).normalize();
                double distance = nextPoint.distance(originPoint);
                double maxTicks = distance / speed;
                long roundedMaxTicks = Math.round(maxTicks);
                direction.multiply((distance/ roundedMaxTicks));
                for (int i1 = 0; i1 <= roundedMaxTicks; i1++){
                    if(i1 == 0 && i >= 1){
                        currentTick--;
                        vectorPath.remove(currentTick);
                    }
                    vectorPath.put(currentTick, direction);
                    currentTick++;
                }
            }
            long maxTicks = currentTick - 1;
            BukkitTask task = new CoasterRunnable(plugin, armorStand, vectorPath, maxTicks).runTaskTimer(plugin, 0L, 1L);
            Bukkit.getScheduler().runTaskLater(plugin, ()->{
                task.cancel();
                Location entityLoc = armorStand.getLocation();
                armorStand.setVelocity(new Vector(0,0,0));
                plugin.getNativeAPI().getCustomArmorStand(armorStand).setGravity(false);
                Bukkit.broadcastMessage(ChatColor.RED + " x=" + entityLoc.getX() + ", y=" + entityLoc.getY() + ", z=" + entityLoc.getZ() + ", maxTicks: " + maxTicks);
                Location endPoint = pathPoints.get(pathPoints.size() - 1);
                Bukkit.broadcastMessage(ChatColor.LIGHT_PURPLE + "endPoint loc: x=" + endPoint.getX() + ", y=" + endPoint.getY() + ", z=" + endPoint.getZ());
            }, maxTicks + 1);
        }
    }

}
