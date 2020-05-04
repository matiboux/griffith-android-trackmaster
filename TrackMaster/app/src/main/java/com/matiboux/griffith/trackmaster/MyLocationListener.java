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

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MyLocationListener implements LocationListener {

    private Context context;

    private long startTimeMillis = -1;
    private long endTimeMillis = -1;

    private GPXFile gpxFile;
    private int savedEntries = 0;
    private LocationManager locationManager;

    public MyLocationListener(@NonNull Context context) {
        this.context = context;
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    public void enableTracking() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions();
            return;
        }

        startTimeMillis = System.currentTimeMillis(); // Save start timestamp
        endTimeMillis = -1; // Reset the end timestamp
        savedEntries = 0; // Reset the saved entries counter

        // Create the output file
        gpxFile = new GPXFile(new File(context.getExternalFilesDir(null), "GPStracks/" +
                new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
                        .format(new Date(System.currentTimeMillis())) + ".gpx"));
        gpxFile.createFile();

        // Initialize the location listener
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, Constants.DELAY_TRACKING, 0, this);
    }

    public void disableTracking() {
        if (locationManager != null) locationManager.removeUpdates(this);
        endTimeMillis = System.currentTimeMillis(); // Save end timestamp
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions((Activity) context,
                new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE},
                Constants.REQUEST_PERMISSIONS);
    }

    public boolean isTracking() {
        return startTimeMillis >= 0 && endTimeMillis < 0;
    }

    public long getRunningTimeMillis() {
        if (startTimeMillis < 0) return 0;
        if (endTimeMillis < 0) return System.currentTimeMillis() - startTimeMillis;
        return endTimeMillis - startTimeMillis;
    }

    public GPXFile getGpxFile() {
        return gpxFile;
    }

    public int getSavedEntries() {
        return savedEntries;
    }

    // *** LocationListener

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            gpxFile.addEntry(location);
            savedEntries++;
        }
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
            if (location != null) {
                gpxFile.addEntry(location);
                savedEntries++;
            }
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
