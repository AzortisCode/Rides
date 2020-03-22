package com.azortis.rides.objects.builders;

import com.azortis.rides.objects.Cart;
import com.azortis.rides.objects.Train;
import com.azortis.rides.objects.TrainMode;

import java.util.ArrayList;

public class RideBuilder {

    private String name;

    // Track
    private TrackBuilder trackBuilder;
    private PathPointBuilder currentPathPointBuilder; // A convenient place to store the current pathPoint that is being made/edited.

    // Train & carts
    private TrainMode trainMode;
    private ArrayList<Train> trains = new ArrayList<>(); // If trainMode == TrainMode.SINGLE_TRAIN || TrainMode.MULTIPLE_TRAINS
    private ArrayList<Cart> carts = new ArrayList<>(); // If trainMode == TrainMode.SINGLE_CART || TrainMode.MULTIPLE_CARTS

    public RideBuilder(String name){
        this.name = name;
        this.trackBuilder = new TrackBuilder(this);
        this.currentPathPointBuilder = new PathPointBuilder(trackBuilder); // Origin point.
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public TrackBuilder getTrackBuilder(){
        return trackBuilder;
    }

    public PathPointBuilder getCurrentPathPointBuilder() {
        return currentPathPointBuilder;
    }

    public PathPointBuilder createPathPointBuilder(){
        if(currentPathPointBuilder.isSaved()) {
            this.currentPathPointBuilder = new PathPointBuilder(trackBuilder);
            return currentPathPointBuilder;
        }
        return null;
    }

    private void setTrainMode(TrainMode trainMode){
        this.trainMode = trainMode;
    }

    public TrainMode getTrainMode(){
        return trainMode;
    }



}
