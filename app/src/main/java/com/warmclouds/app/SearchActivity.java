package com.warmclouds.app;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.warmclouds.app.adapters.NurseryAdapter;
import com.warmclouds.app.models.Nursery;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private RecyclerView nurseriesRecyclerView;
    private EditText searchEditText;
    private Spinner priceSpinner, locationSpinner, ratingSpinner;
    private ProgressBar progressBar;
    private TextView resultsCountTextView;
    
    private NurseryAdapter adapter;
    private List<Nursery> allNurseries;
    private List<Nursery> filteredNurseries;
    
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        db = FirebaseFirestore.getInstance();
        allNurseries = new ArrayList<>();
        filteredNurseries = new ArrayList<>();

        // Initialize views
        nurseriesRecyclerView = findViewById(R.id.nurseriesRecyclerView);
        searchEditText = findViewById(R.id.searchEditText);
        priceSpinner = findViewById(R.id.priceSpinner);
        locationSpinner = findViewById(R.id.locationSpinner);
        ratingSpinner = findViewById(R.id.ratingSpinner);
        progressBar = findViewById(R.id.progressBar);
        resultsCountTextView = findViewById(R.id.resultsCountTextView);

        // Setup RecyclerView
        nurseriesRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new NurseryAdapter(filteredNurseries, new NurseryAdapter.OnNurseryClickListener() {
            @Override
            public void onNurseryClick(Nursery nursery) {
                Intent intent = new Intent(SearchActivity.this, NurseryDetailsActivity.class);
                intent.putExtra("nursery_id", nursery.getId());
                startActivity(intent);
            }
        });
        nurseriesRecyclerView.setAdapter(adapter);

        // Setup search
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterNurseries();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Load nurseries from Firebase
        loadNurseries();
    }

    private void loadNurseries() {
        progressBar.setVisibility(View.VISIBLE);
        
        // Load all nurseries from Firestore
        // Note: Firestore uses "active" field, not "isActive"
        db.collection("nurseries")
                .get()
                .addOnCompleteListener(task -> {
                    progressBar.setVisibility(View.GONE);
                    
                    if (task.isSuccessful()) {
                        allNurseries.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Nursery nursery = document.toObject(Nursery.class);
                            if (nursery != null) {
                                nursery.setId(document.getId());
                                
                                // Check if nursery is active (handle both "active" and "isActive" fields)
                                Boolean active = document.getBoolean("active");
                                Boolean isActive = document.getBoolean("isActive");
                                boolean isNurseryActive = (active != null && active) || (isActive != null && isActive);
                                
                                // Only add active nurseries
                                if (isNurseryActive) {
                                    allNurseries.add(nursery);
                                }
                            }
                        }
                        filterNurseries();
                        
                        if (allNurseries.isEmpty()) {
                            Toast.makeText(this, "No nurseries available at the moment", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Exception exception = task.getException();
                        String errorMessage = "Error loading nurseries";
                        if (exception != null) {
                            errorMessage += ": " + exception.getMessage();
                        }
                        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void filterNurseries() {
        filteredNurseries.clear();
        
        String searchQuery = searchEditText.getText().toString().toLowerCase().trim();
        String priceFilter = priceSpinner.getSelectedItem() != null ? 
                priceSpinner.getSelectedItem().toString() : "";
        String locationFilter = locationSpinner.getSelectedItem() != null ? 
                locationSpinner.getSelectedItem().toString() : "";
        String ratingFilter = ratingSpinner.getSelectedItem() != null ? 
                ratingSpinner.getSelectedItem().toString() : "";
        
        for (Nursery nursery : allNurseries) {
            boolean matches = true;
            
            // Search filter
            if (!searchQuery.isEmpty()) {
                if (!nursery.getName().toLowerCase().contains(searchQuery) &&
                    !nursery.getLocation().toLowerCase().contains(searchQuery)) {
                    matches = false;
                }
            }
            
            // Location filter
            if (!locationFilter.isEmpty() && !locationFilter.equals("All")) {
                if (!nursery.getLocation().equals(locationFilter)) {
                    matches = false;
                }
            }
            
            // Rating filter
            if (!ratingFilter.isEmpty() && !ratingFilter.equals("All")) {
                double minRating = 0;
                if (ratingFilter.contains("4")) {
                    minRating = 4.0;
                } else if (ratingFilter.contains("3")) {
                    minRating = 3.0;
                }
                if (nursery.getRating() < minRating) {
                    matches = false;
                }
            }
            
            if (matches) {
                filteredNurseries.add(nursery);
            }
        }
        
        updateResultsCount();
    }
    
    private void updateResultsCount() {
        int count = filteredNurseries.size();
        String text = count == 1 ? "Found 1 nursery" : "Found " + count + " nurseries";
        if (resultsCountTextView != null) {
            resultsCountTextView.setText(text);
        }
        adapter.notifyDataSetChanged();
    }
}
