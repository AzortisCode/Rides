package com.azortis.rides.utils;

public class ConversionUtils {

    /**
     * Converts the minecraft yaw to a full 360 degree yaw.
     * So you can use it in trigonometry calculations.
     *
     * @param yaw The minecraft yaw.
     * @return A yaw rotation that follows the normal 360 circle.
     */
    public static float toNormalYaw(float yaw){
        if (yaw < 0){
            yaw += 360;
        }
        yaw += 90;
        if(yaw == 360)return 0;
        return yaw;
    }

    /**
     * Converts a full 360 degree yaw to a minecraft one.
     * This should be used after you calculated a yaw in 360 degrees.
     *
     * @param yaw The 360 degree yaw.
     * @return A yaw rotation that follows minecraft conventions.
     */
    public static float toMinecraftYaw(float yaw){
        yaw -= 90;
        if(yaw > 180){
            yaw -= 360;
        }
        return yaw;
    }

}
