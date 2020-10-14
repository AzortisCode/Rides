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

    private final Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().
            registerTypeAdapter(Location.class, new LocationAdapter()).create();



    private final File pathsDataFolder;
    private final Map<String, PathMap> pathsMap = new HashMap<>();

    private final File trainsDataFolder;
    private final Map<String, Train> trainsMap = new HashMap<>();


    public SettingsManager(Rides plugin){
        if(!plugin.getDataFolder().exists())plugin.getDataFolder().mkdir();

        // Data folders.
        pathsDataFolder = new File(plugin.getDataFolder() + "tracked/paths");
        if(!pathsDataFolder.exists())pathsDataFolder.mkdirs();
        trainsDataFolder = new File(plugin.getDataFolder() + "tracked/trains");
        if(!trainsDataFolder.exists())trainsDataFolder.mkdirs();

        try{
            for (File pathFile : pathsDataFolder.listFiles()){
                PathMap pathMap = gson.fromJson(new FileReader(pathFile), PathMap.class);
                pathsMap.put(pathFile.getName().split(".")[0], pathMap);
            }
            for (File trainFile : trainsDataFolder.listFiles()){
                Train train = gson.fromJson(new FileReader(trainFile), Train.class);
                trainsMap.put(trainFile.getName().split(".")[0], train);
            }
        }catch (FileNotFoundException ex){
            ex.printStackTrace();
        }
    }

    public void savePath(String fileName, PathMap pathMap){
        try{
            final String json = gson.toJson(pathMap);
            File pathFile = new File(pathsDataFolder, fileName + ".json");
            if(pathsMap.containsKey(fileName))pathFile.delete();
            Files.write(pathFile.toPath(), json.getBytes(), StandardOpenOption.WRITE, StandardOpenOption.CREATE);
            pathsMap.remove(fileName);
            pathsMap.put(fileName, pathMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveTrain(String fileName, Train train){
        try{
            final String json = gson.toJson(train);
            File pathFile = new File(trainsDataFolder, fileName + ".json");
            if(pathsMap.containsKey(fileName))pathFile.delete();
            Files.write(pathFile.toPath(), json.getBytes(), StandardOpenOption.WRITE, StandardOpenOption.CREATE);
            trainsMap.remove(fileName);
            trainsMap.put(fileName, train);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
