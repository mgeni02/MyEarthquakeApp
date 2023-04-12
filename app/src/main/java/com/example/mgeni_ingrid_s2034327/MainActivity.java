package com.example.mgeni_ingrid_s2034327;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Button parseButton;
    private Button searchButton;
    private Button clearButton;
    private EditText locationFilterEditText;
    private EditText dateFilterEditText;
    private List<Earthquake> earthquakes;
    private List<Earthquake> filteredEarthquakes;
    private EarthquakeAdapter adapter;

    private Handler filterHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        parseButton = findViewById(R.id.parse_button);
        parseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        earthquakes = EarthquakeXmlParser.parse("http://quakes.bgs.ac.uk/feeds/WorldSeismology.xml");
                        filteredEarthquakes = new ArrayList<>(earthquakes);
                        Message message = Message.obtain();
                        message.obj = filteredEarthquakes;
                        handler.sendMessage(message);
                    }
                }).start();
                parseButton.setVisibility(View.GONE);
                locationFilterEditText.setVisibility(View.VISIBLE);
                dateFilterEditText.setVisibility(View.VISIBLE);
                searchButton.setVisibility(View.VISIBLE);
            }
        });

        locationFilterEditText = findViewById(R.id.location_filter_edittext);
        dateFilterEditText = findViewById(R.id.date_filter_edittext);

        searchButton = findViewById(R.id.search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterEarthquakes();
            }
        });

        clearButton = findViewById(R.id.clear_button);
        clearButton.setVisibility(View.GONE);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearSearchQuery();
                clearButton.setVisibility(View.GONE);
                searchButton.setVisibility(View.VISIBLE);
            }
        });


    }


    private void filterEarthquakes() {
        String locationFilter = locationFilterEditText.getText().toString().trim();
        String dateFilterInput = dateFilterEditText.getText().toString().trim();

        // Convert user input date to the yyyy-MM-dd format
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        SimpleDateFormat outputDateFormat = new SimpleDateFormat("EEE, dd MMM yyyy", Locale.US);
        String dateFilter = null;
        try {
            Date date = inputDateFormat.parse(dateFilterInput);
            dateFilter = outputDateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        filteredEarthquakes.clear();
        if ((locationFilter == null || locationFilter.isEmpty()) && (dateFilter == null || dateFilter.isEmpty())) {
            // No filters applied, show all earthquakes
            filteredEarthquakes.addAll(earthquakes);
        } else if (dateFilter == null || dateFilter.isEmpty()) {
            // Only date filter applied
            Toast.makeText(this, "Please enter a date to search with location", Toast.LENGTH_SHORT).show();
            //filteredEarthquakes.addAll(earthquakes);
        } else if (locationFilter == null || locationFilter.isEmpty()) {
            // Only location filter applied
            for (Earthquake earthquake : earthquakes) {
                if (earthquake.getDate().contains(dateFilter)) {
                    filteredEarthquakes.add(earthquake);
                }
            }
        } else {
            // Both filters applied
            for (Earthquake earthquake : earthquakes) {
                if (earthquake.getDescription().contains(locationFilter) &&
                        earthquake.getDate().contains(dateFilter)) {
                    filteredEarthquakes.add(earthquake);
                }
            }
        }

        if (filteredEarthquakes.size() > 0) {
            clearButton.setVisibility(View.VISIBLE);
        } else {
            clearButton.setVisibility(View.GONE);
        }

        // Update the adapter's filteredEarthquakes list with the new filtered results
        adapter.filteredEarthquakes.clear();
        adapter.filteredEarthquakes.addAll(filteredEarthquakes);

        // Notify the adapter that the data set has changed
        adapter.notifyDataSetChanged();
    }



    private void clearSearchQuery() {
        locationFilterEditText.setText("");
        dateFilterEditText.setText("");
        adapter.resetData();
    }




    private Runnable filterRunnable = new Runnable() {
        @Override
        public void run() {
            adapter.notifyDataSetChanged();
        }
    };

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            filteredEarthquakes = (List<Earthquake>) message.obj;
            adapter = new EarthquakeAdapter(filteredEarthquakes, new EarthquakeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(Earthquake earthquake) {
                    Intent intent = new Intent(MainActivity.this, EarthquakeDetailActivity.class);
                    intent.putExtra("title", earthquake.getTitle());
                    intent.putExtra("description", earthquake.getDescription());
                    startActivity(intent);
                }
            });
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            recyclerView.setVisibility(View.VISIBLE);
            return true;
        }
    });
}











