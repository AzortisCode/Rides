package com.azortis.rides.utils;

import com.azortis.rides.tracked.path.PathPoint;
import org.bukkit.util.Vector;

public class MathUtils {

    public static EulerAngle getDirection(PathPoint originPoint, PathPoint nextPoint){
        // Pitch - x
        double pitchZ = nextPoint.getZ() - originPoint.getZ();
        double pitchY = nextPoint.getY() - originPoint.getY();
        Vector pitchVector = new Vector(0, pitchY, pitchZ).normalize();
        pitchZ = pitchVector.getZ();
        pitchY = pitchVector.getY();
        float x = new Double(Math.toDegrees(Math.atan(pitchY/pitchZ))).floatValue();

        // Yaw - y
        double yawX = nextPoint.getX() - originPoint.getX();
        double yawZ = nextPoint.getZ() - originPoint.getZ();
        Vector yawVector = new Vector(yawX, 0 ,yawZ).normalize();
        yawX = yawVector.getX();
        yawZ = yawVector.getZ();
        float y = new Double(Math.toDegrees(Math.atan(yawZ/yawX))).floatValue();
        return new EulerAngle(x, y, 0);
    }

}
