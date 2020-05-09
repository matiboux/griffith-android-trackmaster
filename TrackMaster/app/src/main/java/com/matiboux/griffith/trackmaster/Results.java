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

import java.io.File;
import java.util.Objects;

public class Results extends AppCompatActivity {

    private SpeedGraphView speedGraphView;
    private TextView txvFilename, txvResults;
    private File file;
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
        file = new File(getExternalFilesDir(null), Constants.DIRNAME + Objects.requireNonNull(gpxFilename));
        gpxFile = new GPXFile(file);
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
                        .setTitle("Delete " + txvFilename.getText() + "?")
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