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

import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import java.io.Serializable;
import java.util.Map;

public class PathCache implements Serializable {
    private static final long serialVersionUID = 3191402257770448636L;

    private final Map<Integer, DirectionPath> directionPathMap;
    private final Map<Integer, RotationPath> rotationPathMap;

    public PathCache(Map<Integer, DirectionPath> directionPathMap, Map<Integer, RotationPath> rotationPathMap) {
        this.directionPathMap = directionPathMap;
        this.rotationPathMap = rotationPathMap;
    }

    public Map<Integer, DirectionPath> getDirectionPathMap() {
        return directionPathMap;
    }

    public Map<Integer, RotationPath> getRotationPathMap() {
        return rotationPathMap;
    }

    public static class DirectionPath {
        private final Map<Long, Vector> vectorMap;

        public DirectionPath(Map<Long, Vector> vectorMap) {
            this.vectorMap = vectorMap;
        }

        public Map<Long, Vector> getVectorMap() {
            return vectorMap;
        }

    }

    public static class RotationPath {
        private final Map<Long, EulerAngle> eulerAngleMap;

        public RotationPath(Map<Long, EulerAngle> eulerAngleMap) {
            this.eulerAngleMap = eulerAngleMap;
        }

        public Map<Long, EulerAngle> getEulerAngleMap() {
            return eulerAngleMap;
        }
    }

}
