package com.azortis.rides.utils;

public class TrigonometryUtils {

    public static double getX(float yaw, double radius){
        return Math.cos(Math.toRadians(yaw)) * radius;
    }

    public static double getZ(float yaw, double radius){
        return Math.sin(Math.toRadians(yaw)) * radius;
    }

    public static double getY(float pitch, double radius){
        return Math.sin(Math.toRadians(pitch)) * radius;
    }

    public static double getHorizontal(float pitch, double radius){
        return Math.cos(Math.toRadians(pitch)) * radius;
    }

}
