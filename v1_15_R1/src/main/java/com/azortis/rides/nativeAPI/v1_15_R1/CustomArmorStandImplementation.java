package com.azortis.rides.nativeAPI.v1_15_R1;

import com.azortis.rides.nativeAPI.CustomArmorStand;
import net.minecraft.server.v1_15_R1.*;

public class CustomArmorStandImplementation extends EntityArmorStand implements CustomArmorStand {

    public CustomArmorStandImplementation(World world, double d0, double d1, double d2) {
        super(world, d0 + .5, d1, d2 + .5);
        this.noclip = true;
        this.setMarker(false);
        this.setNoGravity(false);
    }

    @Override
    protected float f(float f, float f1) {
        if(!isNoGravity()){
            return super.f(f,f1);
        } else {
            move(EnumMoveType.SELF, getMot()); //Used to give the entity some velocity while gravity is turned off.
        }
        return 0.0F;
    }

    @Override
    public void setGravity(boolean gravity) {
        this.setNoGravity(!gravity);
    }

    @Override
    public boolean hasGravity() {
        return !this.isNoGravity();
    }
}
