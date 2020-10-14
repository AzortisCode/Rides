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

package com.azortis.rides.nativeAPI.v1_16_R2;

import com.azortis.rides.nativeAPI.NMSBridge;
import com.azortis.rides.nativeAPI.RidesStand;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_16_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R2.entity.CraftArmorStand;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class NMSImplementation  implements NMSBridge{

    @Override
    public RidesStand spawnRidesStand(World world, double x, double y, double z) {
        CraftWorld craftWorld = ((CraftWorld)world);
        NativeRidesStand nativeRidesStand = new NativeRidesStand(craftWorld.getHandle(), x, y, z);
        craftWorld.addEntity(nativeRidesStand, CreatureSpawnEvent.SpawnReason.CUSTOM);
        nativeRidesStand.setBukkitStand((ArmorStand) nativeRidesStand.getBukkitEntity());
        return nativeRidesStand;
    }

    @Override
    public RidesStand spawnRidesStand(Location location) {
        return spawnRidesStand(location.getWorld(), location.getX(), location.getY(), location.getZ());
    }

    @Override
    public boolean isRidesStand(ArmorStand armorStand) {
        return (((CraftArmorStand)armorStand).getHandle() instanceof NativeRidesStand);
    }

    @Override
    public void clearStands(World world) {
        CraftWorld craftWorld = (CraftWorld)world;
        for (Object entity : craftWorld.getHandle().entitiesById.values()){
            if(entity instanceof NativeRidesStand){
                ((NativeRidesStand) entity).getBukkitEntity().remove();
            }
        }
    }

}
