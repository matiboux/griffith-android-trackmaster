package com.matiboux.griffith.trackmaster;

public class GPXEntry {
    public final double latitude;
    public final double longitude;
    public final double altitude;
    public final long time;

    public GPXEntry(double latitude, double longitude, double altitude, long time) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
        this.time = time;
    }
}
