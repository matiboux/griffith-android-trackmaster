package com.matiboux.griffith.trackmaster;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

public class MyLocationListener implements LocationListener {

    // *** Static Methods

    private static Context context;
    private static LocationManager locationManager = null;
    private static Location lastKnownLocation = null;
    private static MyLocationListener locationListener = null;
    private static long startTimeMillis;

    public static void enableTracking(@NonNull Context context, @NonNull LocationManager locationManager) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions();
            return;
        }

        MyLocationListener.context = context;
        locationListener = new MyLocationListener();
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, Constants.DELAY_TRACKING, 0, locationListener);
        startTimeMillis = System.currentTimeMillis();
    }

    public static void disableTracking() {
        if (locationManager != null) locationManager.removeUpdates(locationListener);
        locationListener = null;
    }

    static void requestPermissions() {
        ActivityCompat.requestPermissions((Activity) context,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                Constants.REQUEST_GPS_PERMISSIONS);
    }

    public static boolean isTracking() {
        return locationListener != null;
    }

    public static Location getLastKnownLocation() {
        return lastKnownLocation;
    }

    public static long getRunningTimeMillis() {
        return System.currentTimeMillis() - startTimeMillis;
    }

    // *** LocationListener

    @Override
    public void onLocationChanged(Location location) {
        lastKnownLocation = location;
    }

    @Override
    public void onProviderEnabled(String provider) {
        if (provider.equals(LocationManager.GPS_PROVIDER)) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions();
                return;
            }

            // Save last known location if not null
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) lastKnownLocation = location;
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
        if (provider.equals(LocationManager.GPS_PROVIDER))
            Toast.makeText(context, "GPS unavailable", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }
}
