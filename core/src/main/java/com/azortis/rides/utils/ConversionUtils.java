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

}
