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
import lombok.Getter;
import lombok.Setter;
import net.minecraft.server.v1_16_R2.EntityArmorStand;
import net.minecraft.server.v1_16_R2.EnumMoveType;
import net.minecraft.server.v1_16_R2.Vector3f;
import net.minecraft.server.v1_16_R2.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.util.EulerAngle;

@Getter
@Setter
public class NativeRidesStand extends EntityArmorStand implements RidesStand {

    private ArmorStand bukkitStand;
    private boolean moving;

    public NativeRidesStand(World world, double x, double y, double z) {
        super(world, x, y, z);
        this.noclip = true;
        this.setNoGravity(true);
        this.moving = false;
    }

    @Override
    public void setModelRotation(EulerAngle eulerAngle) {
        this.yaw = this.lastYaw = (float) Math.toDegrees(eulerAngle.getY());
        float pitch = (float) Math.toDegrees(eulerAngle.getX());
        float roll = (float) Math.toDegrees(eulerAngle.getZ());
        Vector3f nativeRotations = new Vector3f(pitch, 0 , roll);
        this.setHeadPose(nativeRotations);
    }

    @Override
    public EulerAngle getModelRotation() {
        Vector3f nativeRotations = this.headPose;
        double pitch = Math.toRadians(nativeRotations.getX());
        double yaw = Math.toRadians(nativeRotations.getY());
        double roll = Math.toRadians(nativeRotations.getZ());
        return new EulerAngle(pitch, yaw, roll);
    }

    // Used to give Velocity even while gravity is of.
    @Override
    protected float f(float f, float f1) {
        this.aB = lastYaw;
        this.aA = yaw;
        if(moving){
            move(EnumMoveType.SELF, getMot());
        }
        return 0.0f;
    }
}
