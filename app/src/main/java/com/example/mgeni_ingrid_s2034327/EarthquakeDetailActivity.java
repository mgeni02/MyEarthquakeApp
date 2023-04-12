package com.example.mgeni_ingrid_s2034327;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class EarthquakeDetailActivity extends AppCompatActivity {

    private TextView titleTextView;
    private TextView descriptionTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earthquake_detail);

        // Enable the back button in the action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Get the earthquake data passed from MainActivity
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String description = intent.getStringExtra("description");

        // Initialize the views
        titleTextView = findViewById(R.id.title);
        descriptionTextView = findViewById(R.id.description);

        // Set the properties of the earthquake object to the TextViews
        titleTextView.setText(title);
        descriptionTextView.setText(description);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle back button clicks here
        if (item.getItemId() == android.R.id.home) {
            finish(); // Close the current activity and return to the previous one
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
