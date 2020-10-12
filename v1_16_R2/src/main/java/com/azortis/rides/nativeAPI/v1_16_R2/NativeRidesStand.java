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
