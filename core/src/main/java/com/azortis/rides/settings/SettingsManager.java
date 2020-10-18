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

package com.azortis.rides.settings;

import com.azortis.rides.Rides;
import com.azortis.rides.settings.adapter.LocationAdapter;
import com.azortis.rides.tracked.Cart;
import com.azortis.rides.tracked.TrackedRide;
import com.azortis.rides.tracked.Train;
import com.azortis.rides.tracked.path.PathMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import org.bukkit.Location;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("all")
@Getter
public class SettingsManager {

    private final Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().enableComplexMapKeySerialization().
            registerTypeAdapter(Location.class, new LocationAdapter()).create();

    private final File trackedDataFolder;
    private final File pathsDataFolder;
    private final File trainsDataFolder;
    private final File cartsDataFolder;


    public SettingsManager(Rides plugin){
        if(!plugin.getDataFolder().exists())plugin.getDataFolder().mkdir();
        trackedDataFolder = new File(plugin.getDataFolder() + "tracked");
        if(!trackedDataFolder.exists())trackedDataFolder.mkdirs();
        pathsDataFolder = new File(plugin.getDataFolder() + "tracked/paths");
        if(!pathsDataFolder.exists())pathsDataFolder.mkdirs();
        trainsDataFolder = new File(plugin.getDataFolder() + "tracked/trains");
        if(!trainsDataFolder.exists())trainsDataFolder.mkdirs();
        cartsDataFolder = new File(plugin.getDataFolder() + "tracked/carts");
        if(!cartsDataFolder.exists())cartsDataFolder.mkdirs();
    }

    public Map<String, TrackedRide> getTrackedRides(){
        Map<String, TrackedRide> trackedRides = new HashMap<>();
        try{
            for (File trackedRideFile : pathsDataFolder.listFiles()){
                TrackedRide trackedRide = gson.fromJson(new FileReader(trackedRideFile), TrackedRide.class);
                trackedRides.put(trackedRideFile.getName().split(".")[0], trackedRide);
            }
        }catch (FileNotFoundException ex){
            ex.printStackTrace();
        }
        return trackedRides;
    }

    public Map<String, PathMap> getPaths() {
        Map<String, PathMap> paths = new HashMap<>();
        try{
            for (File pathFile : pathsDataFolder.listFiles()){
                PathMap pathMap = gson.fromJson(new FileReader(pathFile), PathMap.class);
                paths.put(pathFile.getName().split(".")[0], pathMap);
            }
        }catch (FileNotFoundException ex){
            ex.printStackTrace();
        }
        return paths;
    }

    public Map<String, Train> getTrains(){
        Map<String, Train> trains = new HashMap<>();
        try{
            for (File trainFile : pathsDataFolder.listFiles()){
                Train train = gson.fromJson(new FileReader(trainFile), Train.class);
                trains.put(trainFile.getName().split(".")[0], train);
            }
        }catch (FileNotFoundException ex){
            ex.printStackTrace();
        }
        return trains;
    }

    public Map<String, Cart> getCarts(){
        Map<String, Cart> carts = new HashMap<>();
        try{
            for (File cartFile : pathsDataFolder.listFiles()){
                Cart cart = gson.fromJson(new FileReader(cartFile), Cart.class);
                carts.put(cartFile.getName().split(".")[0], cart);
            }
        }catch (FileNotFoundException ex){
            ex.printStackTrace();
        }
        return carts;
    }

    public void savePath(String name, PathMap pathMap){
        try{
            final String json = gson.toJson(pathMap);
            File pathFile = new File(pathsDataFolder, name + ".json");
            Files.write(pathFile.toPath(), json.getBytes(), StandardOpenOption.WRITE, StandardOpenOption.CREATE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveTrain(String name, Train train){
        try{
            final String json = gson.toJson(train);
            File pathFile = new File(trainsDataFolder, name + ".json");
            Files.write(pathFile.toPath(), json.getBytes(), StandardOpenOption.WRITE, StandardOpenOption.CREATE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
