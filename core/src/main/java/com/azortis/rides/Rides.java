package com.azortis.rides;

import com.azortis.rides.listeners.PlayerInteractListener;
import com.azortis.rides.nativeAPI.NativeAPI;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Rides extends JavaPlugin {

    private MinecraftVersion minecraftVersion;
    private NativeAPI nativeAPI;

    @Override
    public void onEnable(){
        fetchNativeAPI();
        new PlayerInteractListener(this);
    }

    private void fetchNativeAPI(){
        try {
            this.minecraftVersion = MinecraftVersion.valueOf(Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3]);
            this.nativeAPI = (NativeAPI) Class.forName("com.azortis.rides.nativeAPI." + minecraftVersion.getVersionString() + ".NativeImplementation").getConstructor().newInstance();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public MinecraftVersion getMinecraftVersion(){
        return minecraftVersion;
    }

    public NativeAPI getNativeAPI(){
        return nativeAPI;
    }

}
