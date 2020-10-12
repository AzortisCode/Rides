/*
package com.azortis.rides.testing;

import com.azortis.rides.Rides;
import com.azortis.rides.old.objects.PathPoint;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.ArrayList;

public class PlayerInteractListener implements Listener {

    private Rides plugin;
    private ArrayList<PathPoint> pathPoints = new ArrayList<>();
    private int currentSpeed = 2;

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
                pathPoints.add(new PathPoint(location, (currentSpeed / 10D)));
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
            currentSpeed = currentSpeed - 1;
            event.getPlayer().sendMessage("Current speed decreased to: " + (currentSpeed / 10D));
        } else if(event.getMaterial() == Material.DIAMOND_SHOVEL && event.getAction() == Action.RIGHT_CLICK_AIR){
            currentSpeed = currentSpeed + 1;
            event.getPlayer().sendMessage("Current speed increased to: " + (currentSpeed / 10D));
        }
    }

    @EventHandler
    public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent event){
        if(event.getRightClicked() instanceof ArmorStand && pathPoints.size() > 1){
            World world = event.getPlayer().getWorld();
            PathCalculator pathCalculator = new PathCalculator(pathPoints);
            ItemStack modelStack = new ItemStack(Material.DIAMOND_SWORD);
            ItemMeta modelMeta = modelStack.getItemMeta();
            modelMeta.setCustomModelData(21);
            modelStack.setItemMeta(modelMeta);
            ArmorStand mainStand = plugin.getNativeAPI().spawnCustomArmorStand(world, event.getRightClicked().getLocation());
            mainStand.getEquipment().setHelmet(modelStack);
            event.getRightClicked().remove();
            ArmorStand rightSeatStand = plugin.getNativeAPI().spawnCustomArmorStand(world, pathCalculator.getRightSeatOriginLocation());
            ArmorStand leftSeatStand = plugin.getNativeAPI().spawnCustomArmorStand(world, pathCalculator.getLeftSeatOriginLocation());
            BukkitTask task = new RideRunnable(plugin, mainStand, rightSeatStand, leftSeatStand, pathCalculator).runTaskTimer(plugin, 0L, 1L);
            Bukkit.getScheduler().runTaskLater(plugin, ()-> {
                task.cancel();
                mainStand.setVelocity(new Vector(0,0,0));
                rightSeatStand.setVelocity(new Vector(0,0,0));
                leftSeatStand.setVelocity(new Vector(0,0,0));
                plugin.getNativeAPI().getCustomArmorStand(mainStand).setGravity(false);
                plugin.getNativeAPI().getCustomArmorStand(rightSeatStand).setGravity(false);
                plugin.getNativeAPI().getCustomArmorStand(leftSeatStand).setGravity(false);
            }, pathCalculator.getMaxTicks() + 1);
        }
    }

}
*/
