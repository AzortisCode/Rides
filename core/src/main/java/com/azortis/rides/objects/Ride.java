package com.azortis.rides.objects;

public class Ride {

    private String name;
    private Path path;
    private Train train;

    public Ride(String name, Path path, Train train) {
        this.name = name;
        this.path = path;
        this.train = train;
    }

}
