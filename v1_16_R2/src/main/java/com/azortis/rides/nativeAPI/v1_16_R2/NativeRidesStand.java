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

import com.azortis.rides.nativeAPI.RidesStand;
import com.azortis.rides.utils.EulerAngle;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.server.v1_16_R2.EntityArmorStand;
import net.minecraft.server.v1_16_R2.Vector3f;
import net.minecraft.server.v1_16_R2.World;
import org.bukkit.entity.ArmorStand;

public class NativeRidesStand extends EntityArmorStand implements RidesStand {

    @Getter
    @Setter
    private ArmorStand bukkitStand;

    public NativeRidesStand(World world, double x, double y, double z) {
        super(world, x, y, z);
        this.noclip = true;
        this.setMarker(false);
        this.setNoGravity(true);
    }

    @Override
    public void setHeadRotation(EulerAngle eulerAngle) {
        setHeadPose(toNativeRotations(eulerAngle));
    }

    private Vector3f toNativeRotations(EulerAngle eulerAngle){
        float x = eulerAngle.getX();
        if(x >= 0 && x <= 90){
            x = Math.abs(x-90);
        }else{
            x = Math.abs(x-360)+90;
        }
        float y = eulerAngle.getY();
        if(y >= 0 && y <= 90){
            y+=270;
        }else{
            y-=90;
        }
        float z = eulerAngle.getZ();
        if(z >= 0 && z <= 90){
            z+=270;
        }else{
            z-=90;
        }
        return new Vector3f(x,y,z);
    }

}
