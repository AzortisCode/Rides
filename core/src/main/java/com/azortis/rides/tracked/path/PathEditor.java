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

import org.bukkit.util.Vector;

import java.util.Map;

/**
 * Used for internal Path editing.
 */
public class PathEditor {

    private final Vector originsLocation;
    private final PathMap pathMap;

    private int currentIndex;
    private PathPoint currentPoint;
    private boolean editing;

    /**
     * Constructor for creating new paths.
     *
     * @param originsLocation The first {@link Vector} of the location of the path.
     */
    public PathEditor(Vector originsLocation){
        this.originsLocation = originsLocation;
        this.pathMap = new PathMap();
    }

    /**
     * Constructor for editing existing paths.
     *
     * @param originsLocation The first {@link Vector} of the location of the path.
     * @param pathMap The existing {@link PathMap} to edit.
     */
    public PathEditor(Vector originsLocation, PathMap pathMap){
        this.originsLocation = originsLocation;
        this.pathMap = pathMap;
    }

    /**
     * Add a point in the chain.
     *
     * @param pointLocation The vector of the {@link org.bukkit.Location} of the point.
     * @param appendCurrentIndex If it should append current index, instead of adding to the end of the chain.
     * @return PathEditor instance.
     */
    public PathEditor addPoint(Vector pointLocation, boolean appendCurrentIndex){
        if(editing){
            pathMap.replacePoint(currentIndex, currentPoint);
        }else {
            pathMap.addPoint(currentIndex, currentPoint);
        }
        if(appendCurrentIndex){
            currentIndex++;
        }else{
            currentIndex = pathMap.size() + 1;
        }
        pointLocation = pointLocation.subtract(originsLocation);
        currentPoint = new PathPoint(pointLocation.getX(), pointLocation.getY(), pointLocation.getZ(), 0, 0, 0 ,0);
        editing = false;
        return this;
    }

    /**
     * Remove the current {@link PathPoint} being edited.
     *
     * NOTE: The current index will stay the same unless this point is the last one in the chain,
     * then it will get the previous point.
     *
     * @return PathEditor instance.
     */
    public PathEditor removePoint(){
        if(currentIndex > pathMap.size()){
            currentIndex--;
        }else{
            pathMap.removePoint(currentIndex);
        }
        currentPoint = pathMap.getPoint(currentIndex);
        return this;
    }

    /**
     * Saves the current PathPoint, and selects another one to edit.
     *
     * @param index The index of the {@link PathPoint} to edit.
     * @return PathEditor instance.
     */
    public PathEditor editPoint(int index){
        if(editing){
            pathMap.replacePoint(currentIndex, currentPoint);
        }else {
            pathMap.addPoint(currentIndex, currentPoint);
        }
        currentIndex = index;
        currentPoint = pathMap.getPoint(index);
        editing = true;
        return this;
    }

    /**
     * Sets the x-axis offset from originsLocation.
     *
     * @param x The x-axis offset.
     * @return PathEditor instance.
     */
    public PathEditor setX(double x){
        double y = currentPoint.getY();
        double z = currentPoint.getZ();
        currentPoint = currentPoint.setLocation(x, y ,z);
        return this;
    }

    /**
     * Sets the y-axis offset from originsLocation.
     *
     * @param y The y-axis offset.
     * @return PathEditor instance.
     */
    public PathEditor setY(double y){
        double x = currentPoint.getX();
        double z = currentPoint.getZ();
        currentPoint = currentPoint.setLocation(x, y ,z);
        return this;
    }

    /**
     * Sets the z-axis offset from originsLocation.
     *
     * @param z The z-axis offset.
     * @return PathEditor instance.
     */
    public PathEditor setZ(double z){
        double x = currentPoint.getX();
        double y = currentPoint.getY();
        currentPoint = currentPoint.setLocation(x, y ,z);
        return this;
    }

    /**
     * Sets the speed in blocks per tick to take if this point has been reached.
     *
     * @param speed The speed in blocks per tick.
     * @return PathEditor instance.
     */
    public PathEditor setSpeed(double speed){
        currentPoint = currentPoint.setSpeed(speed);
        return this;
    }

    /**
     * Sets the yaw rotation of the model.
     *
     * @param yaw The yaw rotation in degrees.
     * @return PathEditor instance.
     */
    public PathEditor setYaw(float yaw){
        float pitch = currentPoint.getPitch();
        float roll = currentPoint.getRoll();
        currentPoint = currentPoint.setDirection(yaw, pitch, roll);
        return this;
    }

    /**
     * Sets the pitch rotation of the model.
     *
     * @param pitch The pitch rotation in degrees.
     * @return PathEditor instance.
     */
    public PathEditor setPitch(float pitch){
        float yaw = currentPoint.getYaw();
        float roll = currentPoint.getRoll();
        currentPoint = currentPoint.setDirection(yaw, pitch, roll);
        return this;
    }

    /**
     * Sets the roll rotation of the model.
     *
     * @param roll The roll rotation in degrees.
     * @return PathEditor instance.
     */
    public PathEditor setRoll(float roll){
        float yaw = currentPoint.getYaw();
        float pitch = currentPoint.getPitch();
        currentPoint = currentPoint.setDirection(yaw, pitch, roll);
        return this;
    }

    /**
     * Get the current {@link PathPoint} being edited/added.
     *
     * @return The current point being edited/added.
     */
    public PathPoint getCurrentPoint() {
        return currentPoint;
    }

    /**
     * Get the index of the current point being edited/added.
     *
     * @return The index of the current point.
     */
    public int getCurrentIndex() {
        return currentIndex;
    }

    /**
     * Get the current {@link Vector} of the location where the offsets are being calculated from.
     *
     * @return The originPoint of the current editing session.
     */
    public Vector getOriginsLocation() {
        return originsLocation;
    }

    /**
     * Get all the current points in the map.
     *
     * NOTE: Will not include the current one being edited, or added.
     * And for the currentIndex might give another point completely.
     *
     * @return A map of all points and their corresponding indexes.
     */
    public Map<Integer, PathPoint> getPathPoints() {
        return pathMap.getPathPoints();
    }

    /**
     * Saves current PathPoint.
     *
     * @return The result PathMap.
     */
    public PathMap save(){
        if(editing){
            pathMap.replacePoint(currentIndex, currentPoint);
        }else {
            pathMap.addPoint(currentIndex, currentPoint);
        }
        return pathMap;
    }

}
