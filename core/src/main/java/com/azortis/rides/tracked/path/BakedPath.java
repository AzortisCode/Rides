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

import lombok.Getter;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Getter
public class BakedPath implements Serializable {
    private static final long serialVersionUID = -8871575497702255099L;

    private final Map<Integer, DirectionPath> directionPathMap = new HashMap<>();
    private final Map<Integer, RotationPath> rotationPathMap = new HashMap<>();
    private final long maxTicks;

    public BakedPath(Map<Integer, Map<Long, Vector>> directionPathMap, Map<Integer, Map<Long, EulerAngle>> rotationPathMap, long maxTicks) {
        directionPathMap.forEach((integer, vectorMap) -> this.directionPathMap.put(integer, new DirectionPath(vectorMap)));
        rotationPathMap.forEach((integer, longEulerAngleMap) -> this.rotationPathMap.put(integer, new RotationPath(longEulerAngleMap)));
        this.maxTicks = maxTicks;
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
