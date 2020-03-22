package com.azortis.rides.objects.builders;

import com.azortis.rides.objects.PathPoint;
import org.bukkit.Location;

public class PathPointBuilder {

    private TrackBuilder parent;
    private Location location;
    private int speed;
    private boolean saved = false;

    protected PathPointBuilder(TrackBuilder parent){
        this.parent = parent;
    }

    public TrackBuilder getParent() {
        return parent;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public boolean isSaved() {
        return saved;
    }

    public PathPoint build(){
        this.saved = true;
        return new PathPoint(location, speed);
    }
}
