package com.azortis.rides.nativeAPI;

import org.bukkit.World;

public interface NMSBridge {

    RidesStand spawnRidesStand(World world, double x, double y, double z, float yaw, float pitch, float roll);

}
