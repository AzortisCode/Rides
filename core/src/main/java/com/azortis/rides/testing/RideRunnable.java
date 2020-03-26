package com.azortis.rides.testing;

import com.azortis.rides.Rides;
import org.bukkit.entity.ArmorStand;
import org.bukkit.scheduler.BukkitRunnable;

public class RideRunnable extends BukkitRunnable {

    private ArmorStand mainStand;
    private ArmorStand rightSeatStand;
    private ArmorStand leftSeatStand;
    private PathCalculator pathCalculator;
    private long maxTicks;
    private long tick;

    public RideRunnable(Rides plugin, ArmorStand mainStand, ArmorStand rightSeatStand, ArmorStand leftSeatStand, PathCalculator pathCalculator) {
        this.mainStand = mainStand;
        this.rightSeatStand = rightSeatStand;
        this.leftSeatStand = leftSeatStand;
        this.pathCalculator = pathCalculator;
        this.maxTicks = pathCalculator.getMaxTicks();
        this.tick = 0;

        plugin.getNativeAPI().getCustomArmorStand(mainStand).setGravity(true);
        plugin.getNativeAPI().getCustomArmorStand(rightSeatStand).setGravity(true);
        plugin.getNativeAPI().getCustomArmorStand(leftSeatStand).setGravity(true);
    }

    @Override
    public void run() {
        if(tick <= maxTicks){
            mainStand.setVelocity(pathCalculator.getMainStandPath().get(tick));
            rightSeatStand.setVelocity(pathCalculator.getRightSeatPath().get(tick));
            leftSeatStand.setVelocity(pathCalculator.getLeftSeatPath().get(tick));
        }
        if(pathCalculator.getMainStandEulerPath().containsKey(tick)){
            mainStand.setHeadPose(pathCalculator.getMainStandEulerPath().get(tick));
            rightSeatStand.setHeadPose(pathCalculator.getRightStandEulerPath().get(tick));
            leftSeatStand.setHeadPose(pathCalculator.getLeftStandEulerPath().get(tick));
        }
        tick++;
    }
}
