package com.azortis.rides.tracked.path;

import java.util.Iterator;
import java.util.Map;

public class PathIterator implements Iterator<PathPoint> {

    private final Map<Integer, PathPoint> pathPoints;
    private final int size;
    private int currentIndex = 0;

    public PathIterator(Map<Integer, PathPoint> pathPoints){
        this.pathPoints = pathPoints;
        this.size = pathPoints.size();
    }

    @Override
    public boolean hasNext() {
        return (currentIndex +1) <= size;
    }

    @Override
    public PathPoint next() {
        currentIndex++;
        PathPoint pathPoint = pathPoints.get(currentIndex);
        pathPoints.remove(currentIndex);
        return pathPoint;
    }

}
