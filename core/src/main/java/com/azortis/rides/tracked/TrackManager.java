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
import com.azortis.rides.tracked.editor.Editor;
import com.azortis.rides.tracked.editor.cart.CartEditor;
import com.azortis.rides.tracked.path.PathMap;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

@Getter
public class TrackManager {

    private final Rides plugin;
    private final Map<String, TrackedRide> trackedRides = new HashMap<>();
    private final Map<String, PathMap> pathMaps = new HashMap<>();
    private final Map<String, Train> trains = new HashMap<>();
    private final Map<String, Cart> carts = new HashMap<>();

    private final Map<Integer, Editor<?>> editSessions = new HashMap<>();
    private int editSessionIds = 1;

    public TrackManager(Rides plugin){
        this.plugin = plugin;
        trackedRides.putAll(plugin.getSettingsManager().getTrackedRides());
        pathMaps.putAll(plugin.getSettingsManager().getPaths());
        trains.putAll(plugin.getSettingsManager().getTrains());
        carts.putAll(plugin.getSettingsManager().getCarts());

        for (TrackedRide trackedRide : trackedRides.values()){
            if(trackedRide.isLoadOnStart()){
                trackedRide.setLoaded(true);
                PathMap pathMap = pathMaps.get(trackedRide.getPathFile());
                trackedRide.setPathMap(pathMap);
                Train train = trains.get(trackedRide.getTrainFile()).createCopy();
                trackedRide.setTrain(train);

                Cart mainCart = carts.get(train.getMainCartFile()).createCopy();
                train.setMainCart(mainCart);
                mainCart.setStandId(1);
                mainCart.setCartId(0);
                mainCart.setCartOffset(0);

                int assignedStandIds = 2;
                for (Seat seat : mainCart.getSeats()){
                    seat.setStandId(assignedStandIds);
                    assignedStandIds++;
                }
                for (int i = 1; i <= train.getCartFiles().size(); i++) {
                    Cart cart = carts.get(train.getCartFiles().get(i)).createCopy();
                    cart.setStandId(assignedStandIds);
                    cart.setCartId(i);
                    cart.setCartOffset(train.getCartOffsets().get(i));
                    assignedStandIds++;
                    for (Seat seat : cart.getSeats()){
                        seat.setStandId(assignedStandIds);
                        assignedStandIds++;
                    }
                }

                if(trackedRide.isSpawnOnStart()){

                }

            }else{
                trackedRide.setLoaded(false);
            }
        }

    }

    public boolean edit(Player editor, String objectName, Class<?> objectType){
        if(objectType == PathMap.class){

        }else if(objectType == Train.class){

        } else if(objectType == Cart.class){
            if(carts.containsKey(objectName)) {
                Cart cart = carts.get(objectName);
                Location location = editor.getLocation();
                editSessions.put(editSessionIds, new CartEditor(plugin, cart.createCopy(), editor, objectName, editSessionIds, location));
                editSessionIds++;
            }
        }
        return false;
    }

    public void saveEdits(int sessionId){
        if(editSessions.containsKey(sessionId)){
            Editor<?> editor = editSessions.get(sessionId);
            if(editor.getType() == Cart.class){
                Cart cart = (Cart) editor.getResult();

            }
        }
    }

}
