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

import com.azortis.rides.Rides;
import com.azortis.rides.nativeAPI.RidesStand;
import com.azortis.rides.tracked.Cart;
import com.azortis.rides.tracked.Seat;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

@Getter
public class CartEditor {

    private final Rides plugin;
    private final Cart cart;
    private final Player editor;
    private final Location location;
    private final CartVisualizer visualizer;
    private final BukkitTask visualizerTask;
    private final CartListener cartListener;

    @Setter
    private int selectedSeat = 0;

    public CartEditor(Rides plugin, Cart cart, Player editor, Location location){
        this.plugin = plugin;
        this.cart = cart;
        this.editor = editor;
        this.location = location;

        // Initialize all Armor Stands, and then Visualizer task.
        RidesStand mainStand = plugin.getNativeAPI().spawnRidesStand(location);
        cart.setMainStand(mainStand);
        cart.applyModel();

        for (Seat seat : cart.getSeats()){
            RidesStand seatStand = plugin.getNativeAPI().spawnRidesStand(location);
            seat.setStand(seatStand);
        }

        this.visualizer = new CartVisualizer(cart, location);
        this.visualizerTask = Bukkit.getScheduler().runTaskTimer(plugin, this.visualizer, 1L, 0L);
        this.cartListener = new CartListener(this, editor);
        Bukkit.getPluginManager().registerEvents(this.cartListener, plugin);
    }

    public Seat createSeat(){
        Seat seat = new Seat(0, 0, 0,0);
        cart.getSeats().add(seat);
        return seat;
    }

    public Cart save(){
        return null;
    }

}
