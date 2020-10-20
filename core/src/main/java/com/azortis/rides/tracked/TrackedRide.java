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

import com.azortis.rides.tracked.path.BakedPath;
import com.azortis.rides.tracked.path.PathMap;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;

@Getter
@Setter
public class TrackedRide {

    private final String name;
    private boolean loadOnStart;
    private boolean spawnOnStart;
    private Location originLocation;
    private String trainFile;
    private String pathFile;

    // Runtime assigned values.
    private transient boolean loaded;
    private transient boolean running;
    private transient PathMap pathMap;
    private transient BakedPath bakedPath;
    private transient Train train;

    public TrackedRide(String name){
        this.name = name;
    }



}
