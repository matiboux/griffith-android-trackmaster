package com.matiboux.griffith.trackmaster;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.util.List;

public class ListResults extends AppCompatActivity {

    // Layout components
    private ListView listViewResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_results);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.results_title);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Layout components
        listViewResults = findViewById(R.id.lv_list_results);
        FloatingActionButton fab = findViewById(R.id.fab);

        // Events
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListResults.this, Tracking.class);
                startActivity(intent);
            }
        });
        listViewResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String filepath = (String) parent.getItemAtPosition(position);

                Intent intent = new Intent(ListResults.this, Results.class);
                intent.putExtra("gpxFilename", filepath);
                startActivityForResult(intent, Constants.REQUEST_DATA_RELOAD);
            }
        });

        // Load data
        reloadData();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == Constants.REQUEST_DATA_RELOAD)
            // Make sure the request was successful
            if (resultCode == RESULT_OK) reloadData();

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void reloadData() {
        List<String> arrayFiles = GPXFile.getFileList(new File(getExternalFilesDir(null), Constants.DIRNAME));
        ListResultsAdapter adapterResults = new ListResultsAdapter(this, arrayFiles);
        listViewResults.setAdapter(adapterResults);
    }
}