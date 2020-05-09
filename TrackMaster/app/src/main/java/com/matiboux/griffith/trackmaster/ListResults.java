package com.matiboux.griffith.trackmaster;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

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
                startActivity(intent);
            }
        });

        // Load data
        List<String> arrayFiles = GPXFile.getFileList(new File(getExternalFilesDir(null), Constants.DIRNAME));
        ListResultsAdapter adapterResults = new ListResultsAdapter(this, arrayFiles);
        listViewResults.setAdapter(adapterResults);
    }
}