package com.matiboux.griffith.trackmaster;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.util.Objects;

public class Results extends AppCompatActivity {

    private SpeedGraphView speedGraphView;
    private TextView txvFilename, txvResults;
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
        txvFilename = findViewById(R.id.txv_filename);
        speedGraphView = findViewById(R.id.speed_graph_view);
        txvResults = findViewById(R.id.txv_results);

        // Load the GPX file
        Intent intent = getIntent();
        String gpxFilename = Objects.requireNonNull(intent.getExtras()).getString("gpxFilename");
        gpxFile = new GPXFile(new File(getExternalFilesDir(null),
                Constants.DIRNAME + Objects.requireNonNull(gpxFilename)));
        txvFilename.setText(gpxFilename);
        GPXData gpxData = gpxFile.getData();
        txvResults.append("\n- Nb entries: " + gpxData.getSize());
        int minutes = (int) (gpxData.getElapsedSeconds() / (1000 * 60)) % 60;
        int seconds = (int) (gpxData.getElapsedSeconds() / 1000) % 60;
        txvResults.append("\n- Elapsed time: " + minutes + " min " + seconds + " sec");
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