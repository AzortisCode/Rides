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

import org.bukkit.Location;

/**
 * Used for internal Path editing.
 */
public class PathEditor {

    private final Location pathOrigins;
    private final PathMap pathMap;

    private int currentIndex;
    private PathPoint currentPoint;


    /**
     * Constructor for creating new paths.
     *
     * @param pathOrigins The first {@link Location} of the path.
     */
    public PathEditor(Location pathOrigins){
        this.pathOrigins = pathOrigins;
        this.pathMap = new PathMap();

    }

    /**
     * Constructor for editing existing paths.
     *
     * @param pathOrigins The first {@link Location} of the path.
     * @param pathMap The existing {@link PathMap} to edit.
     */
    public PathEditor(Location pathOrigins, PathMap pathMap){
        this.pathOrigins = pathOrigins;
        this.pathMap = pathMap;
    }

    public PathEditor setX(double x){
        return this;
    }

    public PathEditor setY(double y){
        return this;
    }

    public PathEditor setZ(double z){
        return this;
    }

    public PathEditor setSpeed(double speed){
        return this;
    }

    public PathEditor setYaw(float yaw){
        return this;
    }

    public PathEditor setPitch(float pitch){
        return this;
    }

    public PathEditor setRoll(float roll){
        return this;
    }

}
