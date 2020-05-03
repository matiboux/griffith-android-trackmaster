package com.matiboux.griffith.trackmaster;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private MyLocationListener locationListener = null;

    private TextView txvTimer, txvStatus, txvCount;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Get the Location Manager
        locationListener = new MyLocationListener(this);

        // Layout components
        txvTimer = findViewById(R.id.txv_timer);
        txvStatus = findViewById(R.id.txv_status);
        txvCount = findViewById(R.id.txv_count);
        fab = findViewById(R.id.fab);

        // Events
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!locationListener.isTracking()) enableTracking(); // Enable tracking
                else disableTracking(); // Disable Tracking
            }
        });
    }

    private void enableTracking() {
        locationListener.enableTracking(); // Enable GPS Tracking
        enableClock(); // Enable Clock
        Toast.makeText(MainActivity.this, "Toggled on", Toast.LENGTH_LONG).show();

        // Update components
        txvStatus.setText(R.string.status_running);
        fab.setImageResource(R.drawable.ic_stop);
    }

    private void enableClock() {
        // Enable status clock
        final Handler clockHandler = new Handler();
        clockHandler.postDelayed(new Runnable() {
            public void run() {
                long runningTimeMillis = locationListener.getRunningTimeMillis();
                int minutes = (int) (runningTimeMillis / (1000 * 60)) % 60;
                int seconds = (int) (runningTimeMillis / 1000) % 60;
                txvTimer.setText(getString(R.string.timer, minutes, seconds));
                txvCount.setText(getString(R.string.entries_count, locationListener.getSavedEntries()));

                // Continue while it's tracking
                if (locationListener.isTracking())
                    clockHandler.postDelayed(this, 1000);
            }
        }, 1000);
    }

    private void disableTracking() {
        locationListener.disableTracking(); // Disable GPS Tracking
        Toast.makeText(MainActivity.this, "Toggled off", Toast.LENGTH_LONG).show();

        // Update components
        txvStatus.setText(R.string.status_paused);
        fab.setImageResource(R.drawable.ic_play);

        // Show results
        Intent intent = new Intent(this, ResultsActivity.class);
        intent.putExtra("gpxFileAbsPath", locationListener.getGpxFile().getAbsolutePath());
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case Constants.REQUEST_PERMISSIONS:
                if (grantResults.length > 0 // If request is cancelled, grantResults is empty
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    enableTracking(); // Granted: Enable Tracking
                else disableTracking(); // Disable Tracking (if not already)
                return;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}