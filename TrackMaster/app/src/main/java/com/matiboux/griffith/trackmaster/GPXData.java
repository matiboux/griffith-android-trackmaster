package com.matiboux.griffith.trackmaster;

import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class GPXData {
    private final List<GPXEntry> gpxEntries;

    // Results from the processed entries
    private int size = 0;
    private long elapsedSeconds = 0;
    private double totalMeters = 0;
    private double overallSpeed = 0;
    private List<Pair<Long, Double>> speedsList = new ArrayList<>();
    private double averageSpeed = 0;
    private double minAltitude = 0;
    private double maxAltitude = 0;
    private double averageAltitude = 0;

    public GPXData(List<GPXEntry> gpxEntries) {
        this.gpxEntries = gpxEntries;
        processData();
    }

    private void processData() {
        size = gpxEntries.size(); // Size
        if (size <= 0) return;
        GPXEntry firstEntry = gpxEntries.get(0);
        GPXEntry lastEntry = gpxEntries.get(size - 1);

        // ** Elapsed Seconds
        elapsedSeconds = (lastEntry.time - firstEntry.time) / 1000;

        // ** Speed Part 1
        double sumSpeed = 0;

        // ** Altitude Part 1
        minAltitude = maxAltitude = firstEntry.altitude;
        double sumAltitude = firstEntry.altitude;

        // Iterate over entries (excluding the first entry)
        for (int i = 1; i < gpxEntries.size(); i++) {
            GPXEntry A = gpxEntries.get(i - 1);
            GPXEntry B = gpxEntries.get(i);

            // ** Distance
            double distance = computeDistance(A, B);
            totalMeters += distance;

            // ** Speed Part 2
            double speed = computeSpeed(A, B, distance);
            speedsList.add(new Pair<>(A.time / 2 + B.time / 2, speed));
            sumSpeed += speed;

            // ** Altitude Part 2
            if (minAltitude > B.altitude) minAltitude = B.altitude;
            if (maxAltitude < B.altitude) maxAltitude = B.altitude;
            sumAltitude += B.altitude;
        }

        // ** Speed Part 3
        overallSpeed = totalMeters / elapsedSeconds;
        averageSpeed = sumSpeed / (size - 1);

        // ** Altitude Part 3
        averageAltitude = sumAltitude / size;
    }

    public List<GPXEntry> getEntries() {
        return gpxEntries;
    }

    public int getSize() {
        return size;
    }

    public long getElapsedSeconds() {
        return elapsedSeconds;
    }

    public double getTotalMeters() {
        return totalMeters;
    }

    public double getOverallSpeed() {
        return overallSpeed;
    }

    public List<Pair<Long, Double>> getSpeedsList() {
        return speedsList;
    }

    public double getAverageSpeed() {
        return averageSpeed;
    }

    public double getMinAltitude() {
        return minAltitude;
    }

    public double getMaxAltitude() {
        return maxAltitude;
    }

    public double getAverageAltitude() {
        return averageAltitude;
    }

    // *** Static Methods for computing

    /**
     * @param A First GPXEntry point
     * @param B Second GPXEntry point
     * @return Distance between A and B in meters
     */
    public static double computeDistance(GPXEntry A, GPXEntry B) {
        int earthRadius = 6371000; // in meters
        double latA = A.latitude * Math.PI / 180;
        double latB = B.latitude * Math.PI / 180;
        double deltaLat = (B.latitude - A.latitude) * Math.PI / 180;
        double deltaLon = (B.longitude - A.longitude) * Math.PI / 180;
        double a = Math.pow(Math.sin(deltaLat / 2), 2)
                + Math.cos(latA) * Math.cos(latB) * Math.pow(Math.sin(deltaLon / 2), 2);
        return earthRadius * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    }

    /**
     * @param A First GPXEntry point
     * @param B Second GPXEntry point
     * @return Speed from A to B in meters per seconds
     */
    public static double computeSpeed(GPXEntry A, GPXEntry B, double distance) {
        return distance / (B.time - A.time) * 1000;
    }

    public static double roundDecimals(double value, int nbDecimals) {
        if (nbDecimals <= 0) return value;

        double factor = Math.pow(10, nbDecimals);
        return (double) Math.round(value * factor) / factor;
    }
}
