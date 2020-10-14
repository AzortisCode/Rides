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

public class ConversionUtils {

    /**
     * Takes a minecraft yaw where 0 or 360 represents a positive Z to
     * where it represents a positive X
     *
     * @param yaw The minecraft yaw.
     * @return A yaw rotation that follows the normal 360 circle.
     */
    public static float toNormalYaw(float yaw){
        yaw += 90;
        if(yaw > 360)yaw-=360;
        return yaw;
    }

    /**
     * Takes a normal yaw where 0 or 360 represent a positive X
     * to where it represents a positive Z
     *
     * @param yaw The 360 degree yaw.
     * @return A yaw rotation that follows minecraft conventions.
     */
    public static float toMinecraftYaw(float yaw){
        yaw -= 90;
        if(yaw < 0)yaw += 360;
        return yaw;
    }

    /**
     * Converts the -90 to 90 minecraft pitch and makes it a normal circle
     * so it can be used in trigonometry calculations;
     *
     * @param pitch The minecraft pitch;
     * @return A pitch rotation that can be used in trigonometry calculations.
     */
    public static float toNormalPitch(float pitch){
        if(pitch < 0){
            pitch = Math.abs(pitch);
        }else if(pitch > 0){
            pitch *= -1 + 360;
        }
        return pitch;
    }

    /**
     * Converts the 270 to 90 pitch to a minecraft 90 to -90 one.
     *
     * @param pitch The normal pitch rotation
     * @return A pitch rotation that follows minecraft conventions.
     */
    public static float toMinecraftPitch(float pitch){
        if(pitch > 0 && pitch <= 90){
            pitch *= -1;
        }else if(pitch >= 270){
            pitch = Math.abs(pitch - 360);
        }
        return pitch;
    }

    

}
