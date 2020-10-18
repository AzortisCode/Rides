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

package com.azortis.rides;

import com.azortis.rides.nativeAPI.NMSBridge;
import com.azortis.rides.settings.SettingsManager;
import com.azortis.rides.tracked.TrackManager;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;
import java.util.List;

@Getter
public final class Rides extends JavaPlugin {

    private NMSBridge nativeAPI;
    private Metrics metrics;

    private SettingsManager settingsManager;
    private TrackManager trackManager;

    @Override
    public void onEnable(){
        this.getLogger().info("Fetching server version...");
        try{
            String name = this.getServer().getClass().getPackage().getName();
            String version = (name.substring(name.lastIndexOf('.') + 1)).replace("v", "").toUpperCase();
            List<String> supportedVersions = Collections.singletonList("1_16_R2");
            if(!supportedVersions.contains(version)){
                this.getLogger().severe("Version: v" + version + " is not supported! Shutting down plugin...");
                Bukkit.getPluginManager().disablePlugin(this);
                return;
            }
            this.getLogger().info("Found version: v" + version + " loading nativeAPI...");
            this.nativeAPI = (NMSBridge) Class.forName("com.azortis.rides.nativeAPI.v" + version + ".NMSImplementation").newInstance();
        } catch (Exception e) {
            this.getLogger().severe("Something went wrong couldn't load native implementation! Shutting down plugin...");
            e.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        this.metrics = new Metrics(this, 9116);
        this.settingsManager = new SettingsManager(this);
        this.trackManager = new TrackManager(this);
    }



}
