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

import com.azortis.rides.tracked.TrackedRide;
import com.azortis.rides.tracked.Train;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;

public class PathCalculator implements Runnable{

    // Read only
    private final PathPoint originPoint;
    private final PathIterator pathIterator;
    private final Train train;

    private final Map<Integer, Map<Long, Vector>> directionPathMap = new HashMap<>();
    private final Map<Integer, Map<Long, EulerAngle>> rotationsPathMap = new HashMap<>();

    private PathPoint currentOriginPoint;

    private volatile boolean finished = false;
    private volatile PathCache result;

    public PathCalculator(TrackedRide trackedRide){
        this.pathIterator = trackedRide.getPathMap().getIterator();
        this.originPoint = pathIterator.next();
        this.train = trackedRide.getTrain();
        this.currentOriginPoint = originPoint;
    }

    @Override
    public void run() {
        while(pathIterator.hasNext()){
            PathPoint nextPoint = pathIterator.next();
            Vector direction = nextPoint.toVector().subtract(currentOriginPoint.toVector());
            
        }
        finished = true;
    }

    public boolean isFinished() {
        return finished;
    }

    public PathCache getResult(){
        return null;
    }

}
