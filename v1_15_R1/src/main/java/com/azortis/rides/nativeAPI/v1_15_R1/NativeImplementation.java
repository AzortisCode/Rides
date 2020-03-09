package com.azortis.rides.nativeAPI.v1_15_R1;

import com.azortis.rides.nativeAPI.CustomArmorStand;
import com.azortis.rides.nativeAPI.NativeAPI;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.util.HashMap;

public class NativeImplementation implements NativeAPI {

    private HashMap<ArmorStand, CustomArmorStandImplementation> armorStandMap = new HashMap<>();

    @Override
    public ArmorStand spawnCustomArmorStand(World world, Location location) {
        CustomArmorStandImplementation casi = new CustomArmorStandImplementation(((CraftWorld)world).getHandle(),
                location.getBlockX(), location.getBlockY(), location.getBlockZ());
        ((CraftWorld)world).addEntity(casi, CreatureSpawnEvent.SpawnReason.CUSTOM);
        ArmorStand bukkitArmorStand = (ArmorStand) casi.getBukkitEntity();
        this.armorStandMap.put(bukkitArmorStand, casi);
        return bukkitArmorStand;
    }

    @Override
    public CustomArmorStand getCustomArmorStand(ArmorStand armorStand) {
        return armorStandMap.get(armorStand);
    }
}
