package com.azortis.rides;

import org.bukkit.Location;

public class PathPoint {

    private Location location;
    private double speed;

    public PathPoint(Location location, double speed){
        this.location = location;
        this.speed = speed;
    }

    public Location getLocation() {
        return location;
    }

    public double getSpeed() {
        return speed;
    }

}
