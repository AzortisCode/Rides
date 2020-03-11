package com.azortis.rides.listeners;

import com.azortis.rides.CoasterRunnable;
import com.azortis.rides.PathPoint;
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
    private ArrayList<PathPoint> pathPoints = new ArrayList<>();
    private double currentSpeed = 0.2;

    public PlayerInteractListener(Rides plugin){
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event){
        if(event.getMaterial() == Material.DIAMOND_AXE && event.getAction() == Action.RIGHT_CLICK_BLOCK){
            if(event.getClickedBlock() != null) {
                Location location = event.getClickedBlock().getLocation();
                location.setX(location.getX() + .5);
                location.setY(location.getY() + 1);
                location.setZ(location.getZ() + .5);
                pathPoints.add(new PathPoint(location, currentSpeed));
                event.getPlayer().sendMessage("Added pathPoint: x=" + location.getX() + ", y=" + location.getY() + ", z=" + location.getZ() + ", speed: " + currentSpeed);
            }
        }else if(event.getMaterial() == Material.STICK && event.getAction() == Action.RIGHT_CLICK_AIR){
            if(!pathPoints.isEmpty()) {
                pathPoints.clear();
                event.getPlayer().sendMessage(ChatColor.RED + "Cleared path!");
            } else{
                event.getPlayer().sendMessage(ChatColor.GREEN + "Path was already empty!");
            }
        } else if(event.getMaterial() == Material.GOLDEN_SHOVEL && event.getAction() == Action.RIGHT_CLICK_AIR){
            currentSpeed = currentSpeed - 0.1;
            event.getPlayer().sendMessage("Current speed decreased to: " + currentSpeed);
        } else if(event.getMaterial() == Material.DIAMOND_SHOVEL && event.getAction() == Action.RIGHT_CLICK_AIR){
            currentSpeed = currentSpeed + 0.1;
            event.getPlayer().sendMessage("Current speed increased to: " + currentSpeed);
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
            for (int i = 0; i < (pathPoints.size() - 1); i++){
                Location originLocation = pathPoints.get(i).getLocation();
                Location nextLocation = pathPoints.get(i + 1).getLocation();
                Vector direction = nextLocation.toVector().subtract(originLocation.toVector()).normalize();
                double distance = nextLocation.distance(originLocation);
                double speed = pathPoints.get(i).getSpeed();
                double maxTicks = distance / speed;
                long roundedMaxTicks = Math.round(maxTicks);
                direction.multiply((distance / roundedMaxTicks));
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
                Location endPoint = pathPoints.get(pathPoints.size() - 1).getLocation();
                Bukkit.broadcastMessage(ChatColor.LIGHT_PURPLE + "endPoint loc: x=" + endPoint.getX() + ", y=" + endPoint.getY() + ", z=" + endPoint.getZ());
            }, maxTicks + 1);
        }
    }

}
