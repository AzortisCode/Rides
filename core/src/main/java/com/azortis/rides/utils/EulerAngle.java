package com.azortis.rides.utils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EulerAngle {

    private final float x; // Pitch
    private final float y; // Yaw
    private final float z; // Roll

    public EulerAngle(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

}
