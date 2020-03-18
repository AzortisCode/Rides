package com.azortis.rides.objects;

public class Ride {

    private int id;
    private String name;
    private Track track;
    private Train train;

    public Ride(int id, String name, Track track, Train train) {
        this.id = id;
        this.name = name;
        this.track = track;
        this.train = train;
    }

}
