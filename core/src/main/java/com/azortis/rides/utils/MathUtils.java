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

package com.azortis.rides.utils;

import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

public class MathUtils {

    /*public static EulerAngle getDirection(Vector originPoint, Vector nextPoint){
        *//*/ Pitch - x
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
        float y = new Double(Math.toDegrees(Math.atan(yawZ/yawX))).floatValue();*


        return new EulerAngle(0, 0, 0);
    }*/

    public static EulerAngle getDirection(Vector originPoint, Vector nextPoint){
        double dX = nextPoint.getX() - originPoint.getX();
        double dY = nextPoint.getY() - originPoint.getY();
        double dZ = nextPoint.getZ() - originPoint.getZ();

        double pitch = Math.atan2(dZ, dY);
        double yaw = Math.atan2((-dX), dZ); // Invert X, to fix unit circle quadrants.
        return new EulerAngle(pitch, yaw, 0);
    }

}
