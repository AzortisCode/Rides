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

import com.azortis.rides.tracked.Cart;
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

        Vector originPointDirection = getDirection(originPoint.getDirection());
        Map<Cart, PathPoint> cartOriginPoints = new HashMap<>();
        for (Cart cart : train.getCarts()){
            Vector cartOrigin = originPointDirection.multiply(-cart.getCartOffset());
            cartOriginPoints.put(cart, originPoint.setLocation(cartOrigin.getX(), cartOrigin.getY(), cartOrigin.getZ()));
        }

        long currentMaxTicks = 0;

        while (pathIterator.hasNext()){
            Vector direction = nextPoint.toVector().subtract(originPoint.toVector());
            double distance = direction.length();
            double speed = originPoint.getSpeed();
            double maxTicks = distance / speed;
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
                directionPathMap.get(1).put((currentMaxTicks + i), mainCartForce);

                double yaw = fixRotation((originYaw + yawIncrements * i));
                double pitch = fixRotation((originPitch + pitchIncrements * i));
                double roll = fixRotation((originRoll + rollIncrements * i));

                EulerAngle modelRotation = new EulerAngle(Math.toRadians(yaw), Math.toRadians(pitch), Math.toRadians(roll));
                rotationsPathMap.get(1).put((currentMaxTicks + i), modelRotation);

                Vector cartPosition = originPoint.toVector().add(mainCartForce.multiply(i));
                for (Seat seat : train.getMainCart().getSeats()){
                    Vector nextSeatPosition = getSeatPosition(cartPosition, seat, yaw, pitch);
                    Vector seatForce = nextSeatPosition.subtract(currentSeatOrigins.get(seat));
                    directionPathMap.get(seat.getStandId()).put((currentMaxTicks + i), seatForce);
                    currentSeatOrigins.remove(seat);
                    currentSeatOrigins.put(seat, nextSeatPosition);
                }

            }

            for (Cart cart : train.getCarts()){
                double calculatedDistance = 0;
                int index = pathIterator.getCurrentIndex() - 1;
                PathPoint cartOriginPoint = cartOriginPoints.get(cart);
                PathPoint cartNextPoint = null;
                PathPoint point2 = nextPoint;
                while (calculatedDistance < cart.getCartOffset()){
                    PathPoint point = pathIterator.getPoint(index);
                    if(point.toVector().distance(point2.toVector()) >= (cart.getCartOffset() - calculatedDistance)){
                        double remainingDistance = cart.getCartOffset() - calculatedDistance;
                        Vector vector = point2.toVector().subtract(point.toVector());
                        double distanceFromPointToNextCartPoint = vector.length() - remainingDistance;
                        Vector vector1 = vector.normalize().multiply(distanceFromPointToNextCartPoint);

                        Vector nextCartPosition = point.toVector().add(vector1);
                        double distancePercentage = ((vector.length() - distanceFromPointToNextCartPoint) / vector.length());

                        double x = nextCartPosition.getX();
                        double y = nextCartPosition.getY();
                        double z = nextCartPosition.getZ();
                        double nextSpeed = pathIterator.getPoint(pathIterator.getCurrentIndex() + 1).getSpeed();
                        double yaw = getRotation(point.getYaw(), point2.getYaw(), distancePercentage);
                        double pitch = getRotation(point.getPitch(), point2.getPitch(), distancePercentage);
                        double roll = getRotation(point.getRoll(), point2.getRoll(), distancePercentage);
                        cartNextPoint = new PathPoint(x, y, z, nextSpeed, yaw, pitch, roll);
                        calculatedDistance = cart.getCartOffset(); // To stop the loop...
                    }else{
                        calculatedDistance += point.toVector().distance(nextPoint.toVector());
                        point2 = point;
                        index--;
                    }
                }
                assert cartNextPoint != null;
                Vector cartDirection = cartNextPoint.toVector().subtract(cartOriginPoint.toVector());
                double cartDistance = cartDirection.length();

                double cartOriginYaw = cartOriginPoint.getYaw();
                double cartOriginPitch = cartOriginPoint.getPitch();
                double cartOriginRoll = cartOriginPoint.getRoll();

                double cartTargetYaw = cartNextPoint.getYaw();
                double cartTargetPitch = cartNextPoint.getPitch();
                double cartTargetRoll = cartNextPoint.getRoll();

                double cartYawIncrements = getRotationIncrements(cartOriginYaw, cartTargetYaw, roundedMaxTicks);
                double cartPitchIncrements = getRotationIncrements(cartOriginPitch, cartTargetPitch, roundedMaxTicks);
                double cartRollIncrements = getRotationIncrements(cartOriginRoll, cartTargetRoll, roundedMaxTicks);

                Vector cartForce = cartDirection.normalize().multiply((cartDistance / roundedMaxTicks));
                Map<Seat, Vector> currentCartSeatOrigins = new HashMap<>();


                cartOriginPoints.remove(cart);
                cartOriginPoints.put(cart, cartNextPoint);
            }

            currentMaxTicks += roundedMaxTicks;
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

    private double getRotation(double originRotation, double targetRotation, double percentage){
        double rotation = 0;
        double rotationSubtract = Math.abs(originRotation - targetRotation);
        if(targetRotation < originRotation) targetRotation+=360;
        double rotationAdd = targetRotation - originRotation;
        if(rotationSubtract < rotationAdd){
            rotation = originRotation - (rotationSubtract * percentage);
        }else if(rotationSubtract > rotationAdd){
            rotation = originRotation + (rotationAdd * percentage);
        }
        return fixRotation(rotation);
    }

    private double fixRotation(double rotation){
        if(rotation > 360)return rotation-360;
        if(rotation < 0)return rotation+360;
        return rotation;
    }

    private Vector getSeatPosition(Vector cartPosition, Seat seat, double yaw, double pitch){
        double seatYaw = fixRotation((yaw + seat.getYawOffset()));
        double seatPitch = fixRotation((pitch + seat.getPitchOffset()));

        double dX = -(Math.sin(seatYaw) * seat.getYawDistance());
        double dY = Math.cos(seatPitch) * seat.getPitchDistance();
        double dZ = Math.cos(seatYaw) * seat.getYawDistance();

        return cartPosition.add(new Vector(dX, dY, dZ));
    }

    private Vector getDirection(EulerAngle direction){
        double yaw = direction.getY();
        double pitch = direction.getX();

        double dX = -Math.sin(yaw);
        double dY = Math.cos(pitch);
        double dZ = Math.cos(yaw);

        return new Vector(dX, dY, dZ);
    }

    public boolean isFinished() {
        return finished;
    }

    public PathCache getResult(){
        return null;
    }

}
