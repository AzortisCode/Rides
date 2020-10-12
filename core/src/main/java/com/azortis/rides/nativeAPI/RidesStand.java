package com.azortis.rides.nativeAPI;

import com.azortis.rides.utils.EulerAngle;
import org.bukkit.entity.ArmorStand;

public interface RidesStand {

    ArmorStand getBukkitStand();

    void setHeadRotation(EulerAngle eulerAngle);

}
