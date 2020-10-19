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

package com.azortis.rides.tracked.path;

import com.azortis.rides.tracked.Seat;
import com.azortis.rides.tracked.TrackedRide;
import com.azortis.rides.tracked.Train;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;

public class PathCalculator implements Runnable{

    // Read only
    private final PathPoint originPoint;
    private final PathIterator pathIterator;
    private final Train train;

    private final Map<Integer, Map<Long, Vector>> directionPathMap = new HashMap<>();
    private final Map<Integer, Map<Long, EulerAngle>> rotationsPathMap = new HashMap<>();

    private volatile boolean finished = false;
    private volatile PathCache result;

    public PathCalculator(TrackedRide trackedRide){
        this.pathIterator = trackedRide.getPathMap().getIterator();
        this.originPoint = pathIterator.next();
        this.train = trackedRide.getTrain();
    }

    @Override
    public void run() {
        // Initialising StandIds to be referenced in PathCache
        directionPathMap.put(1, new HashMap<>());
        rotationsPathMap.put(1, new HashMap<>());
        train.getMainCart().getSeats().forEach(seat -> directionPathMap.put(seat.getStandId(), new HashMap<>()));
        train.getCarts().forEach(cart -> {
            directionPathMap.put(cart.getStandId(), new HashMap<>());
            rotationsPathMap.put(cart.getStandId(), new HashMap<>());
            cart.getSeats().forEach(seat -> directionPathMap.put(seat.getStandId(), new HashMap<>()));
        });

        PathPoint originPoint = this.originPoint;
        PathPoint nextPoint = pathIterator.next();
        double currentSpeed;
        long currentMaxTick = 0;

        while (pathIterator.hasNext()){
            // Main Cart
            Vector direction = nextPoint.toVector().subtract(originPoint.toVector());
            double distance = direction.length();
            currentSpeed = originPoint.getSpeed();
            double maxTicks = distance / currentSpeed;
            long roundedMaxTicks = Math.round(maxTicks);

            double originYaw = originPoint.getYaw();
            double originPitch = originPoint.getPitch();
            double originRoll = originPoint.getRoll();

            double targetYaw = nextPoint.getYaw();
            double targetPitch = nextPoint.getPitch();
            double targetRoll = nextPoint.getRoll();

            double yawIncrements = getRotationIncrements(originYaw, targetYaw, roundedMaxTicks);
            double pitchIncrements = getRotationIncrements(originPitch, targetPitch, roundedMaxTicks);
            double rollIncrements = getRotationIncrements(originRoll, targetRoll, roundedMaxTicks);

            Vector mainCartForce = direction.normalize().multiply((distance / roundedMaxTicks));
            Map<Seat, Vector> currentSeatOrigins = new HashMap<>();
            for (Seat seat : train.getMainCart().getSeats()){
                currentSeatOrigins.put(seat, getSeatPosition(originPoint.toVector(), seat, originYaw, originPitch));
            }
            for (int i = 1; i <= roundedMaxTicks; i++) {
                directionPathMap.get(1).put((currentMaxTick + i), mainCartForce);

                double yaw = fixRotation((originYaw + yawIncrements * i));
                double pitch = fixRotation((originPitch + pitchIncrements * i));
                double roll = fixRotation((originRoll + rollIncrements * i));

                EulerAngle modelRotation = new EulerAngle(Math.toRadians(yaw), Math.toRadians(pitch), Math.toRadians(roll));
                rotationsPathMap.get(1).put((currentMaxTick + i), modelRotation);

                Vector cartPosition = originPoint.toVector().add(mainCartForce.multiply(i));
                for (Seat seat : train.getMainCart().getSeats()){
                    Vector nextSeatPosition = getSeatPosition(cartPosition, seat, yaw, pitch);
                    Vector seatForce = nextSeatPosition.subtract(currentSeatOrigins.get(seat));
                    directionPathMap.get(seat.getStandId()).put((currentMaxTick + i), seatForce);
                    currentSeatOrigins.remove(seat);
                    currentSeatOrigins.put(seat, nextSeatPosition);
                }

            }

            currentMaxTick += roundedMaxTicks;
            originPoint = nextPoint;
            nextPoint = pathIterator.next();
        }

        finished = true;
    }

    private double getRotationIncrements(double originRotation, double targetRotation, long roundedMaxTicks){
        double rotationIncrements = 0;
        double rotationSubtract = Math.abs(originRotation - targetRotation);
        if(targetRotation < originRotation) targetRotation+=360;
        double rotationAdd = targetRotation - originRotation;
        if(rotationSubtract < rotationAdd){
            rotationIncrements = (-rotationSubtract) / roundedMaxTicks;
        }else if(rotationSubtract > rotationAdd){
            rotationIncrements = rotationAdd / roundedMaxTicks;
        }
        return rotationIncrements;
    }

    private Vector getSeatPosition(Vector cartPosition, Seat seat, double yaw, double pitch){
        double seatYaw = fixRotation((yaw + seat.getYawOffset()));
        double seatPitch = fixRotation((pitch + seat.getPitchOffset()));

        double dX = -(Math.sin(seatYaw) * seat.getYawDistance());
        double dY = Math.cos(seatPitch) * seat.getPitchDistance();
        double dZ = Math.cos(seatYaw) * seat.getYawDistance();

        return cartPosition.add(new Vector(dX, dY, dZ));
    }

    private double fixRotation(double rotation){
        if(rotation > 360)return rotation-360;
        if(rotation < 0)return rotation+360;
        return rotation;
    }

    public boolean isFinished() {
        return finished;
    }

    public PathCache getResult(){
        return null;
    }

}
