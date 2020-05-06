package com.matiboux.griffith.trackmaster;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.util.Objects;

public class ResultsActivity extends AppCompatActivity {

    private SpeedGraphView speedGraphView;
    private TextView txvResults;
    private GPXFile gpxFile;

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
        speedGraphView = findViewById(R.id.speed_graph_view);
        txvResults = findViewById(R.id.txv_results);

        // Load the GPX file
        Intent intent = getIntent();
        String gpxFileAbsPath = Objects.requireNonNull(intent.getExtras()).getString("gpxFileAbsPath");
        gpxFile = new GPXFile(new File(Objects.requireNonNull(gpxFileAbsPath)));
        GPXData gpxData = gpxFile.getData();
        txvResults.append("\n- Nb entries: " + gpxData.getSize());
        txvResults.append("\n- Elapsed time: " + GPXData.roundDecimals(gpxData.getElapsedSeconds(), 2) + " sec");
        txvResults.append("\n- Total Distance: " + GPXData.roundDecimals(gpxData.getTotalMeters(), 2) + " m");
        txvResults.append("\n- Overall Speed: " + GPXData.roundDecimals(gpxData.getOverallSpeed(), 2) + " m/sec" +
                " (" + GPXData.roundDecimals(gpxData.getOverallSpeed() * 3.6, 2) + " km/h)");
        txvResults.append("\n- Average Speed: " + GPXData.roundDecimals(gpxData.getAverageSpeed(), 2) + " m/sec" +
                " (" + GPXData.roundDecimals(gpxData.getAverageSpeed() * 3.6, 2) + " km/h)");
        txvResults.append("\n- Min Altitude: " + GPXData.roundDecimals(gpxData.getMinAltitude(), 2) + " m");
        txvResults.append("\n- Max Altitude: " + GPXData.roundDecimals(gpxData.getMaxAltitude(), 2) + " m");
        txvResults.append("\n- Average Altitude: " + GPXData.roundDecimals(gpxData.getAverageAltitude(), 2) + " m");
        speedGraphView.setData(gpxData.getAverageSpeeds());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks
        switch (item.getItemId()) {
            case android.R.id.home:
                // Up/Home button
                super.onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}