package com.azortis.rides.tracked.path;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class PathMap implements Serializable {
    private static final long serialVersionUID = 9053765944289630945L;

    private final Map<Integer, PathPoint> pathPoints;

    public PathMap(){
        this.pathPoints = new HashMap<>();
    }

    public PathPoint getPoint(int index){
        return pathPoints.get(index);
    }

    public void addPoint(int index, PathPoint point){
        if(index <= pathPoints.size()){
            for (int i = pathPoints.size(); i >= index; i--){
                PathPoint point1 = getPoint(i);
                pathPoints.remove(i);
                pathPoints.put((i+1), point1);
            }
        }
        pathPoints.put(index, point);
    }

    public void removePoint(int index){
        pathPoints.remove(index);
        for (int i = (index+1); i<= pathPoints.size(); i++){
            PathPoint point = getPoint(i);
            pathPoints.remove(i);
            pathPoints.put((i-1), point);
        }
    }

    public void replacePoint(int index, PathPoint newPoint){
        pathPoints.remove(index);
        pathPoints.put(index, newPoint);
    }

    public int size(){
        return pathPoints.size();
    }

    public PathIterator getIterator(){
        Map<Integer, PathPoint> clone = new HashMap<>(pathPoints);
        return new PathIterator(clone);
    }

}
