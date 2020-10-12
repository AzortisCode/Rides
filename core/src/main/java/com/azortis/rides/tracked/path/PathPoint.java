package com.azortis.rides.tracked.path;

import com.azortis.rides.tracked.TrackedRide;
import lombok.Getter;
import org.bukkit.util.Vector;

import java.io.Serializable;

@Getter
public class PathPoint implements Serializable {
    private static final long serialVersionUID = -551856799294811757L;

    /**
     * The XYZ-axis offset from {@link TrackedRide} origin {@link org.bukkit.Location}.
     */
    private final double x,y,z;

    /**
     * The speed in blocks per tick from current path point, to the next.
     */
    private final double speed;

    /**
     * The yaw, pitch & roll of the model of the cart.
     */
    private final float yaw,pitch,roll;

    public PathPoint(double x, double y, double z, double speed, float yaw, float pitch, float roll){
        this.x = x;
        this.y = y;
        this.z = z;
        this.speed = speed;
        this.yaw = yaw;
        this.pitch = pitch;
        this.roll = roll;
    }

    public PathPoint setLocation(double x, double y, double z){
        return new PathPoint(x, y, z, speed, yaw, pitch, roll);
    }

    public PathPoint setSpeed(double speed){
        return new PathPoint(x, y, z, speed, yaw, pitch, roll);
    }

    public PathPoint setDirection(float yaw, float pitch, float roll){
        return new PathPoint(x, y, z, speed, yaw, pitch, roll);
    }

    public Vector toVector(){
        return new Vector(x,y,z);
    }

    

}
