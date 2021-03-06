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

import java.util.Iterator;
import java.util.Map;

public class PathIterator implements Iterator<PathPoint> {

    private final Map<Integer, PathPoint> pathPoints;
    private final int size;
    private int currentIndex = -1;

    public PathIterator(Map<Integer, PathPoint> pathPoints){
        this.pathPoints = pathPoints;
        this.size = pathPoints.size() - 1;
    }

    @Override
    public boolean hasNext() {
        return (currentIndex+1) <= size;
    }

    @Override
    public PathPoint next() {
        currentIndex++;
        return pathPoints.get(currentIndex);
    }

    public boolean isLast(){
        return currentIndex == size;
    }

    public PathPoint getPoint(int index){
        return pathPoints.get(index);
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public int getSize() {
        return size;
    }

    public void reset(){
        currentIndex = 0;
    }

}
