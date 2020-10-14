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

package com.azortis.rides.tracked;

import com.azortis.rides.Rides;
import com.azortis.rides.tracked.path.PathMap;

import java.util.HashMap;
import java.util.Map;

public class TrackManager {

    private final Rides plugin;

    private final Map<String, PathMap> pathMaps = new HashMap<>();
    private final Map<String, Train> trainMap = new HashMap<>();

    public TrackManager(Rides plugin){
        this.plugin = plugin;
    }

}
