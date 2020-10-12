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
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Rides extends JavaPlugin {

    private MinecraftVersion minecraftVersion;
    private NMSBridge nativeAPI;

    private RideManager rideManager;

    @Override
    public void onEnable(){
        fetchNativeAPI();

    }

    private void fetchNativeAPI(){
        try {
            this.minecraftVersion = MinecraftVersion.valueOf(Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3]);
            this.nativeAPI = (NMSBridge) Class.forName("com.azortis.rides.nativeAPI." + minecraftVersion.getVersionString() + ".NMSImplementation").getConstructor().newInstance();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public MinecraftVersion getMinecraftVersion(){
        return minecraftVersion;
    }

    public NMSBridge getNativeAPI(){
        return nativeAPI;
    }

}
