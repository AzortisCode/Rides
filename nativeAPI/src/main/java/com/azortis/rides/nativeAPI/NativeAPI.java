package com.azortis.rides.nativeAPI;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;

public interface NativeAPI {

    ArmorStand spawnCustomArmorStand(World world, Location location);

    CustomArmorStand getCustomArmorStand(ArmorStand armorStand);

}
