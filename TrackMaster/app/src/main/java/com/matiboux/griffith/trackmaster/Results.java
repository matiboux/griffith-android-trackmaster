package com.matiboux.griffith.trackmaster;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.File;
import java.util.Objects;

public class Results extends AppCompatActivity {

    private File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.results_title);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Layout components
        TextView txvFilename = findViewById(R.id.txv_filename);
        SpeedGraphView speedGraphView = findViewById(R.id.speed_graph_view);
        TextView txvEntries = findViewById(R.id.txv_entries);
        TextView txvTimer = findViewById(R.id.txv_timer);
        TextView txvDistance = findViewById(R.id.txv_distance);
        TextView txvOverallSpeed = findViewById(R.id.txv_ova_speed);
        TextView txvAverageAltitude = findViewById(R.id.txv_avg_altitude);
        TextView txvAverageSpeed = findViewById(R.id.txv_avg_speed);
        TextView txvMinAltitude = findViewById(R.id.txv_min_altitude);
        TextView txvMaxAltitude = findViewById(R.id.txv_max_altitude);

        // Load the GPX file
        Intent intent = getIntent();
        String gpxFilename = Objects.requireNonNull(intent.getExtras()).getString("gpxFilename");
        file = new File(getExternalFilesDir(null), Constants.DIRNAME + Objects.requireNonNull(gpxFilename));
        GPXData gpxData = new GPXFile(file).getData(); // Load GPX file data

        // Set the layout components values
        txvFilename.setText(gpxFilename);
        txvEntries.setText(String.valueOf(gpxData.getSize()));
        long minutes = (gpxData.getElapsedSeconds() / 60) % 60;
        long seconds = gpxData.getElapsedSeconds() % 60;
        txvTimer.setText(getString(R.string.timer, minutes, seconds));
        txvDistance.setText(String.valueOf(GPXData.roundDecimals(gpxData.getTotalMeters(), 2)));
        txvOverallSpeed.setText(String.valueOf(GPXData.roundDecimals(gpxData.getOverallSpeed() * 3.6, 2)));
        txvAverageSpeed.setText(String.valueOf(GPXData.roundDecimals(gpxData.getAverageSpeed() * 3.6, 2)));
        txvMinAltitude.setText(String.valueOf(GPXData.roundDecimals(gpxData.getMinAltitude(), 2)));
        txvMaxAltitude.setText(String.valueOf(GPXData.roundDecimals(gpxData.getMaxAltitude(), 2)));
        txvAverageAltitude.setText(String.valueOf(GPXData.roundDecimals(gpxData.getAverageAltitude(), 2)));
        speedGraphView.setData(gpxData.getSpeedsList());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_results, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks
        switch (item.getItemId()) {
            case android.R.id.home:
                // Up/Home button
                super.onBackPressed();
                return true;
            case R.id.action_delete:
                // Delete this results entry
                new AlertDialog.Builder(this)
                        .setTitle("Delete " + file.getName() + "?")
                        .setMessage(
                                "Do you really want to delete this entry?\n" +
                                        "This cannot be undone.")
                        .setIcon(android.R.drawable.ic_delete)
                        .setPositiveButton(android.R.string.yes,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        Toast toast = Toast.makeText(Results.this, null, Toast.LENGTH_SHORT);
                                        if (file.delete()) {
                                            toast.setText("Entry successfully deleted.");
                                            toast.show();
                                            setResult(RESULT_OK); // Force data reload
                                            finish(); // Quit activity
                                        } else {
                                            toast.setText("An error occurred.");
                                            toast.show();
                                        }
                                    }
                                })
                        .setNegativeButton(android.R.string.no, null).show();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}