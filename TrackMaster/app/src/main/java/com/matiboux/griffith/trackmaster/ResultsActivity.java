package com.matiboux.griffith.trackmaster;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class ResultsActivity extends AppCompatActivity {

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
        txvResults = findViewById(R.id.txv_results);

        // Load the GPX file
        Intent intent = getIntent();
        String gpxFileAbsPath = Objects.requireNonNull(intent.getExtras()).getString("gpxFileAbsPath");
        gpxFile = new GPXFile(new File(Objects.requireNonNull(gpxFileAbsPath)));
        List<GPXEntry> gpxEntries = gpxFile.getEntries();
        txvResults.append("Nb entries: " + String.valueOf(gpxEntries.size()));
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