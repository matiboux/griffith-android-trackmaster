package com.matiboux.griffith.trackmaster;

import java.util.List;

public class GPXData {
    private final List<GPXEntry> gpxEntries;

    // Results from the processed entries
    private int size = 0;
    private long elapsedSeconds = 0;
    private int totalMeters = 0;
    private double averageOverallSpeed = 0;
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
            int earthRadius = 6371000; // in meters
            double latA = A.latitude * Math.PI / 180;
            double latB = B.latitude * Math.PI / 180;
            double deltaLat = (B.latitude - A.latitude) * Math.PI / 180;
            double deltaLon = (B.longitude - A.longitude) * Math.PI / 180;
            double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2)
                    + Math.cos(latA) * Math.cos(latB) * Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);
            double distance = earthRadius * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
            totalMeters += distance;

            // ** Speed Part 2
            sumSpeed += distance / (B.time - A.time) * 1000;

            // ** Altitude Part 2
            if (minAltitude > B.altitude) minAltitude = B.altitude;
            if (maxAltitude < B.altitude) maxAltitude = B.altitude;
            sumAltitude += B.altitude;
        }

        // ** Speed Part 3
        averageOverallSpeed = (double) totalMeters / elapsedSeconds;
        averageSpeed = sumSpeed / (size - 1);

        // ** Altitude Part 3
        averageAltitude = sumAltitude / size;
    }

    public int getSize() {
        return size;
    }

    public long getElapsedSeconds() {
        return elapsedSeconds;
    }

    public int getTotalMeters() {
        return totalMeters;
    }

    public double getAverageOverallSpeed() {
        return averageOverallSpeed;
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
}
