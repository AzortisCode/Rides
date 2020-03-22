package com.azortis.rides.objects;

public class Ride {

    private String name;
    private Track track;
    private Train train;

    public Ride(String name, Track track, Train train) {
        this.name = name;
        this.track = track;
        this.train = train;
    }

}
