package com.azortis.rides;

import com.azortis.rides.nativeAPI.NMSBridge;
import com.azortis.rides.testing.PlayerInteractListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Rides extends JavaPlugin {

    private MinecraftVersion minecraftVersion;
    private NMSBridge nativeAPI;

    private RideManager rideManager;

    @Override
    public void onEnable(){
        fetchNativeAPI();
        new PlayerInteractListener(this);
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
