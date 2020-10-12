/*package com.azortis.rides.testing;

import com.azortis.rides.old.objects.PathPoint;
import com.azortis.rides.utils.ConversionUtils;
import com.azortis.rides.utils.TrigonometryUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;

public class PathCalculator {

    // Vector paths
    private HashMap<Long, Vector> mainStandPath = new HashMap<>();
    private HashMap<Long, Vector> rightSeatPath = new HashMap<>();
    private HashMap<Long, Vector> leftSeatPath = new HashMap<>();

    // EulerAngle paths
    private HashMap<Long, EulerAngle> mainStandEulerPath = new HashMap<>();
    private HashMap<Long, EulerAngle> rightStandEulerPath = new HashMap<>();
    private HashMap<Long, EulerAngle> leftStandEulerPath = new HashMap<>();

    // Seat origin locations(Where to spawn them)
    private Location rightSeatOriginLocation;
    private Location leftSeatOriginLocation;

    // Other
    private long maxTicks;

    public PathCalculator(ArrayList<PathPoint> pathPoints){
        long currentTick = 0;
        for (int i = 0; i < (pathPoints.size() - 1); i++){
            PathPoint originPoint = pathPoints.get(i);
            PathPoint nextPoint = pathPoints.get(i + 1);
            Vector direction = nextPoint.getLocation().toVector().subtract(originPoint.getLocation().toVector()).normalize();

            double forwardSeatOffset = 1.0D;
            double sidewardsSeatOffset = 1.0D;

            // Seat location for originPoint
            Location originLocation = originPoint.getLocation();
            originLocation.setDirection(direction);
            float originYawTheta = ConversionUtils.toNormalYaw(originPoint.getLocation().getYaw());
            float originPitchTheta = ConversionUtils.toNormalPitch(originLocation.getPitch());
            Location originSeatCenterLocation = new Location(originLocation.getWorld(), originLocation.getX() +
                    TrigonometryUtils.getX(originYawTheta, TrigonometryUtils.getHorizontal(originPitchTheta, forwardSeatOffset)),
                    originLocation.getY() + TrigonometryUtils.getY(originPitchTheta, forwardSeatOffset), originLocation.getZ() +
                    TrigonometryUtils.getZ(originYawTheta, TrigonometryUtils.getHorizontal(originPitchTheta, forwardSeatOffset)));
            Location originRightSeatLocation = new Location(originLocation.getWorld(), originSeatCenterLocation.getX() +
                    TrigonometryUtils.getX(originYawTheta + 90, sidewardsSeatOffset),
                    originSeatCenterLocation.getY(), originSeatCenterLocation.getZ() + TrigonometryUtils.getZ(originYawTheta + 90, sidewardsSeatOffset));
            Location originLeftSeatLocation = new Location(originLocation.getWorld(), originSeatCenterLocation.getX() +
                    TrigonometryUtils.getX(originYawTheta - 90, sidewardsSeatOffset),
                    originSeatCenterLocation.getY(), originSeatCenterLocation.getZ() + TrigonometryUtils.getZ(originYawTheta - 90, sidewardsSeatOffset));


            Bukkit.broadcastMessage("OriginLocation: x=" + originLocation.getX() + ", y=" + originLocation.getY() + ", z=" + originLocation.getZ() + ". Theta: " + originYawTheta);
            Bukkit.broadcastMessage("OriginSeatCenterLocation: x=" + originSeatCenterLocation.getX() + ", y=" + originSeatCenterLocation.getY() + ", z=" + originSeatCenterLocation.getZ());
            Bukkit.broadcastMessage("OriginRightSeatLocation: x=" + originRightSeatLocation.getX() + ", y=" + originRightSeatLocation.getY() + ", z=" + originRightSeatLocation.getZ());
            Bukkit.broadcastMessage("OriginLeftSeatLocation: x=" + originLeftSeatLocation.getX() + ", y=" + originLeftSeatLocation.getY() + ", z=" + originLeftSeatLocation.getZ());


            // Seat location for nextPoint
            Vector nextDirection;
            if(pathPoints.size() >= i + 3){
                nextDirection = pathPoints.get(i + 2).getLocation().toVector().subtract(nextPoint.getLocation().toVector());
            } else {
                nextDirection = direction;
            }
            nextDirection.normalize();
            Location nextLocation = nextPoint.getLocation();
            nextLocation.setDirection(nextDirection);
            float nextYawTheta = ConversionUtils.toNormalYaw(nextLocation.getYaw());
            float nextPitchTheta = ConversionUtils.toNormalPitch(nextLocation.getPitch());
            Location nextSeatCenterLocation = new Location(nextLocation.getWorld(), nextLocation.getX() +
                    TrigonometryUtils.getX(nextYawTheta, TrigonometryUtils.getHorizontal(nextPitchTheta, forwardSeatOffset)),
                    nextLocation.getY() + TrigonometryUtils.getY(nextPitchTheta, forwardSeatOffset), nextLocation.getZ() +
                    TrigonometryUtils.getZ(nextYawTheta, TrigonometryUtils.getHorizontal(nextPitchTheta, forwardSeatOffset)));
            Location nextRightSeatLocation = new Location(nextLocation.getWorld(), nextSeatCenterLocation.getX() +
                    TrigonometryUtils.getX(nextYawTheta + 90, sidewardsSeatOffset),
                    nextSeatCenterLocation.getY(), nextSeatCenterLocation.getZ() + TrigonometryUtils.getZ(nextYawTheta + 90, sidewardsSeatOffset));
            Location nextLeftSeatLocation = new Location(nextLocation.getWorld(), nextSeatCenterLocation.getX() +
                    TrigonometryUtils.getX(nextYawTheta - 90, sidewardsSeatOffset),
                    nextSeatCenterLocation.getY(), nextSeatCenterLocation.getZ() + TrigonometryUtils.getZ(nextYawTheta - 90, sidewardsSeatOffset));

            if(i == 0){
                rightSeatOriginLocation = originRightSeatLocation;
                leftSeatOriginLocation = originLeftSeatLocation;
            }

            // Further vector calculations

            // MainStand
            double distance = originLocation.distance(nextLocation);
            double speed = originPoint.getSpeed();
            double maxTicks = distance / speed;
            long roundedMaxTicks = Math.round(maxTicks);
            direction.multiply((distance / roundedMaxTicks));

            // RightSeatStand
            Vector rightSeatDirection = nextRightSeatLocation.toVector().subtract(originRightSeatLocation.toVector()).normalize();
            double rightSeatDistance = originRightSeatLocation.distance(nextRightSeatLocation);
            rightSeatDirection.multiply((rightSeatDistance / roundedMaxTicks));

            // LeftSeatStand
            Vector leftSeatDirection = nextLeftSeatLocation.toVector().subtract(originLeftSeatLocation.toVector()).normalize();
            double leftSeatDistance = originLeftSeatLocation.distance(nextLeftSeatLocation);
            leftSeatDirection.multiply((leftSeatDistance / roundedMaxTicks));

            for (int i1 = 0; i1 <= roundedMaxTicks; i1++){
                if(i1 == 0 && i >= 1){
                    currentTick--;
                    mainStandPath.remove(currentTick);
                    rightSeatPath.remove(currentTick);
                    leftSeatPath.remove(currentTick);
                }
                // EulerAngles
                if(i1 == roundedMaxTicks){
                    EulerAngle eulerAngle = new EulerAngle(Math.toRadians(nextLocation.getPitch()), Math.toRadians(nextLocation.getYaw()), 0);
                    mainStandEulerPath.put(currentTick, eulerAngle);
                    rightStandEulerPath.put(currentTick, eulerAngle);
                    leftStandEulerPath.put(currentTick, eulerAngle);
                }
                mainStandPath.put(currentTick, direction);
                rightSeatPath.put(currentTick, rightSeatDirection);
                leftSeatPath.put(currentTick, leftSeatDirection);
                currentTick++;
            }
        }
        maxTicks = currentTick - 1;
    }

    public HashMap<Long, Vector> getMainStandPath() {
        return mainStandPath;
    }

    public HashMap<Long, Vector> getRightSeatPath() {
        return rightSeatPath;
    }

    public HashMap<Long, Vector> getLeftSeatPath() {
        return leftSeatPath;
    }

    public HashMap<Long, EulerAngle> getMainStandEulerPath() {
        return mainStandEulerPath;
    }

    public HashMap<Long, EulerAngle> getRightStandEulerPath() {
        return rightStandEulerPath;
    }

    public HashMap<Long, EulerAngle> getLeftStandEulerPath() {
        return leftStandEulerPath;
    }

    public Location getRightSeatOriginLocation() {
        return rightSeatOriginLocation;
    }

    public Location getLeftSeatOriginLocation() {
        return leftSeatOriginLocation;
    }

    public long getMaxTicks() {
        return maxTicks;
    }
}*/
