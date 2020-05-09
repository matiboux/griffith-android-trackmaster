package com.matiboux.griffith.trackmaster;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Layout components
        listViewResults = findViewById(R.id.lv_list_results);
        FloatingActionButton fab = findViewById(R.id.fab);

        // Events
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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