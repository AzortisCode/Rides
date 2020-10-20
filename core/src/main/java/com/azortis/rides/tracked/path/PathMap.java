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

package com.azortis.rides.tracked.path;

import java.util.HashMap;
import java.util.Map;

public class PathMap {

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

    public int getLastIndex(){
        return pathPoints.size() - 1; // Index starts at 0
    }

    public Map<Integer, PathPoint> getPathPoints() {
        return new HashMap<>(pathPoints);
    }

    public PathIterator getIterator(){
        Map<Integer, PathPoint> clone = new HashMap<>(pathPoints);
        return new PathIterator(clone);
    }

}
