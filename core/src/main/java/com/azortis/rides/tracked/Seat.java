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

import com.azortis.rides.nativeAPI.RidesStand;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Seat {

    // Serializable values for storing ride information offsets are in Radians.
    private double yawOffset;
    private double yawDistance;
    private double pitchOffset;
    private double pitchDistance;

    // Runtime assigned values
    private transient RidesStand stand;
    private transient int standId;
    private transient boolean locked;

    public Seat(float yawOffset, double yawDistance, float pitchOffset, double pitchDistance) {
        this.yawOffset = yawOffset;
        this.yawDistance = yawDistance;
        this.pitchOffset = pitchOffset;
        this.pitchDistance = pitchDistance;
    }

    public boolean hasPassenger(){
        return stand.getBukkitStand().getPassengers().size() > 0;
    }


}
