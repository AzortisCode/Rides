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

package com.azortis.rides.tracked.editor.cart;

import com.azortis.rides.tracked.Cart;
import com.azortis.rides.tracked.Seat;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CartVisualizer implements Runnable{

    private final Location location;
    private final Cart cart;

    private Seat selectedSeat;
    private boolean changed;

    private final Map<Seat, Vector> locationCache = new HashMap<>();

    CartVisualizer(Cart cart, Location location){
        this.location = location;
        this.cart = cart;
    }

    @Override
    public void run() {
        for (Seat seat : cart.getSeats()){
            if(!locationCache.containsKey(seat)){
                // No need to check if offsets > 2PI since Origin theta = 0.
                double yaw = Math.toRadians(seat.getYawOffset());
                double pitch = Math.toRadians(seat.getPitchOffset());
                double dx = -(Math.sin(yaw) * seat.getYawDistance());
                double dy = Math.cos(pitch) * seat.getPitchDistance();
                double dz = Math.cos(yaw) * seat.getYawDistance();
                Vector offsetVector = new Vector(dx, dy, dz);
                Location seatLocation = location.toVector().add(offsetVector).toLocation(Objects.requireNonNull(location.getWorld()));
                seat.getStand().getBukkitStand().teleport(seatLocation);
            }
        }
        if(changed){
            locationCache.remove(selectedSeat);
            setChanged(false);
        }
    }

    public void setSelectedSeat(Seat seat){
        if(selectedSeat != null){
            selectedSeat.getStand().getBukkitStand().setGlowing(false);
            selectedSeat.getStand().getBukkitStand().getPassengers().forEach(entity ->
                    selectedSeat.getStand().getBukkitStand().removePassenger(entity));
        }
        if(seat != null) {
            selectedSeat = seat;
            seat.getStand().getBukkitStand().setGlowing(true);
            Entity zombiePassenger = Objects.requireNonNull(location.getWorld()).spawnEntity(location, EntityType.ZOMBIE);
            seat.getStand().getBukkitStand().addPassenger(zombiePassenger);
        }
    }

    public void setChanged(boolean changed) {
        this.changed = changed;
    }
}
