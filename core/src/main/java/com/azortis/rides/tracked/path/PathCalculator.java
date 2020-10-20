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

    private final PathPoint originPoint;
    private final PathIterator pathIterator;
    private final Train train;

    private final Map<Integer, Map<Long, Vector>> directionPathMap = new HashMap<>();
    private final Map<Integer, Map<Long, EulerAngle>> rotationsPathMap = new HashMap<>();

    private volatile boolean finished = false;
    private volatile BakedPath result;

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

        while (pathIterator.hasNext() || pathIterator.isLast()){
            Vector direction = nextPoint.toVector().subtract(originPoint.toVector());
            double distance = direction.length();
            double speed = originPoint.getSpeed();
            double maxTicks = distance / speed;
            long roundedMaxTicks = Math.round(maxTicks);

            calculateCart(train.getMainCart(), originPoint, nextPoint, speed, currentMaxTicks, roundedMaxTicks);

            for (Cart cart : train.getCarts()){
                double calculatedDistance = 0;
                int index = pathIterator.getCurrentIndex() - 1;
                PathPoint cartOriginPoint = cartOriginPoints.get(cart);
                PathPoint cartNextPoint = null;
                PathPoint point2 = nextPoint;
                while (calculatedDistance < cart.getCartOffset()){
                    PathPoint point = pathIterator.getPoint(index);
                    if(point.toVector().distance(point2.toVector()) >= (cart.getCartOffset() - calculatedDistance)){
                        Vector cartDirection = point2.toVector().subtract(point.toVector());
                        double remainingDistance = cart.getCartOffset() - calculatedDistance;
                        double distanceToNextPoint = cartDirection.length() - remainingDistance;
                        cartDirection = cartDirection.normalize().multiply(distanceToNextPoint);

                        Vector nextCartPosition = point.toVector().add(cartDirection);
                        double distancePercentage = ((cartDirection.length() - distanceToNextPoint) / cartDirection.length());

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
                double cartDistance = cartNextPoint.toVector().distance(cartOriginPoint.toVector());
                calculateCart(cart, cartOriginPoint, cartNextPoint, cartDistance, currentMaxTicks, roundedMaxTicks);
                cartOriginPoints.remove(cart);
                cartOriginPoints.put(cart, cartNextPoint);
            }
            currentMaxTicks += roundedMaxTicks;
            if(pathIterator.hasNext()){
                originPoint = nextPoint;
                nextPoint = pathIterator.next();
            }
        }
        this.result = new BakedPath(directionPathMap, rotationsPathMap, currentMaxTicks);
        finished = true;
    }

    private void calculateCart(Cart cart, PathPoint originPoint, PathPoint nextPoint, double distance, long currentMaxTicks, long maxTicks){
        double originYaw = originPoint.getYaw();
        double originPitch = originPoint.getPitch();
        double originRoll = originPoint.getRoll();

        double targetYaw = nextPoint.getYaw();
        double targetPitch = nextPoint.getPitch();
        double targetRoll = nextPoint.getRoll();

        double yawIncrements = getRotationIncrements(originYaw, targetYaw, maxTicks);
        double pitchIncrements = getRotationIncrements(originPitch, targetPitch, maxTicks);
        double rollIncrements = getRotationIncrements(originRoll, targetRoll, maxTicks);

        Vector cartForce = nextPoint.toVector().subtract(originPoint.toVector()).normalize().multiply((distance / maxTicks));
        Map<Seat, Vector> currentSeatOrigins = new HashMap<>();
        for (Seat seat : cart.getSeats()){
            currentSeatOrigins.put(seat, getSeatPosition(originPoint.toVector(), seat, originYaw, originPitch));
        }
        for (int i = 1; i <= maxTicks; i++) {
            directionPathMap.get(cart.getStandId()).put((currentMaxTicks + i), cartForce);

            double yaw = fixRotation((originYaw + yawIncrements * i));
            double pitch = fixRotation((originPitch + pitchIncrements * i));
            double roll = fixRotation((originRoll + rollIncrements * i));

            EulerAngle modelRotation = new EulerAngle(Math.toRadians(yaw), Math.toRadians(pitch), Math.toRadians(roll));
            rotationsPathMap.get(cart.getStandId()).put((currentMaxTicks + i), modelRotation);

            Vector cartPosition = originPoint.toVector().add(cartForce.multiply(i));
            for (Seat seat : cart.getSeats()){
                Vector nextSeatPosition = getSeatPosition(cartPosition, seat, yaw, pitch);
                Vector seatForce = nextSeatPosition.subtract(currentSeatOrigins.get(seat));
                directionPathMap.get(seat.getStandId()).put((currentMaxTicks + i), seatForce);
                currentSeatOrigins.remove(seat);
                currentSeatOrigins.put(seat, nextSeatPosition);
            }

        }

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

    public BakedPath getResult(){
        return result;
    }

}
