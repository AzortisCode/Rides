package com.azortis.rides.objects.builders;


import com.azortis.rides.objects.PathPoint;

public class TrackBuilder {

    private RideBuilder parent;
    private PathPoint originPoint;

    protected TrackBuilder(RideBuilder parent){
        this.parent = parent;
    }

    public RideBuilder getParent() {
        return parent;
    }

    public void setOriginPoint(PathPoint originPoint) {
        this.originPoint = originPoint;
    }

    public PathPoint getOriginPoint() {
        return originPoint;
    }


}
