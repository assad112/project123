package com.warmclouds.app;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.warmclouds.app.models.Nursery;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddEditNurseryActivity extends AppCompatActivity {

    private EditText nameEditText, descriptionEditText, locationEditText, addressEditText;
    private EditText phoneEditText, emailEditText, instagramEditText;
    private EditText registrationFeeEditText, monthlyFeeEditText;
    private EditText ageGroupsEditText, facilitiesEditText, featuresEditText;
    private Button saveButton;
    private ProgressBar progressBar;

    private String nurseryId;
    private FirebaseFirestore db;
    private boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_nursery);

        db = FirebaseFirestore.getInstance();
        nurseryId = getIntent().getStringExtra("nursery_id");
        isEditMode = nurseryId != null;

        initializeViews();
        
        if (isEditMode) {
            loadNurseryData();
        }
    }

    private void initializeViews() {
        nameEditText = findViewById(R.id.nameEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        locationEditText = findViewById(R.id.locationEditText);
        addressEditText = findViewById(R.id.addressEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        emailEditText = findViewById(R.id.emailEditText);
        instagramEditText = findViewById(R.id.instagramEditText);
        registrationFeeEditText = findViewById(R.id.registrationFeeEditText);
        monthlyFeeEditText = findViewById(R.id.monthlyFeeEditText);
        ageGroupsEditText = findViewById(R.id.ageGroupsEditText);
        facilitiesEditText = findViewById(R.id.facilitiesEditText);
        featuresEditText = findViewById(R.id.featuresEditText);
        saveButton = findViewById(R.id.saveButton);
        progressBar = findViewById(R.id.progressBar);

        // Update title and button text
        TextView titleTextView = findViewById(R.id.titleTextView);
        TextView subtitleTextView = findViewById(R.id.subtitleTextView);
        
        if (isEditMode) {
            if (titleTextView != null) {
                titleTextView.setText("Edit Nursery");
            }
            if (subtitleTextView != null) {
                subtitleTextView.setText("Update the following information");
            }
            saveButton.setText("Update");
        } else {
            if (titleTextView != null) {
                titleTextView.setText("Add New Nursery");
            }
            if (subtitleTextView != null) {
                subtitleTextView.setText("Fill in the following information to add a new nursery");
            }
            saveButton.setText("Save");
        }
        
        saveButton.setOnClickListener(v -> saveNursery());
    }

    private void loadNurseryData() {
        progressBar.setVisibility(View.VISIBLE);

        db.collection("nurseries").document(nurseryId).get()
                .addOnCompleteListener(new OnCompleteListener<com.google.firebase.firestore.DocumentSnapshot>() {
                    @Override
                    public void onComplete(Task<com.google.firebase.firestore.DocumentSnapshot> task) {
                        progressBar.setVisibility(View.GONE);

                        if (task.isSuccessful() && task.getResult() != null) {
                            Nursery nursery = task.getResult().toObject(Nursery.class);
                            if (nursery != null) {
                                populateFields(nursery);
                            }
                        } else {
                            Toast.makeText(AddEditNurseryActivity.this, "Error loading data: " + 
                                    (task.getException() != null ? task.getException().getMessage() : "Unknown error"), 
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void populateFields(Nursery nursery) {
        nameEditText.setText(nursery.getName());
        descriptionEditText.setText(nursery.getDescription());
        locationEditText.setText(nursery.getLocation());
        addressEditText.setText(nursery.getAddress());
        phoneEditText.setText(nursery.getPhone());
        emailEditText.setText(nursery.getEmail());
        instagramEditText.setText(nursery.getInstagram());
        registrationFeeEditText.setText(String.valueOf(nursery.getRegistrationFee()));
        monthlyFeeEditText.setText(String.valueOf(nursery.getMonthlyFee()));
        
        if (nursery.getAgeGroups() != null && !nursery.getAgeGroups().isEmpty()) {
            ageGroupsEditText.setText(String.join(", ", nursery.getAgeGroups()));
        }
        if (nursery.getFacilities() != null && !nursery.getFacilities().isEmpty()) {
            facilitiesEditText.setText(String.join(", ", nursery.getFacilities()));
        }
        if (nursery.getFeatures() != null && !nursery.getFeatures().isEmpty()) {
            featuresEditText.setText(String.join(", ", nursery.getFeatures()));
        }
    }

    private void saveNursery() {
        String name = nameEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();
        String location = locationEditText.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(description) || TextUtils.isEmpty(location)) {
            Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        saveButton.setEnabled(false);

        Nursery nursery = new Nursery();
        if (isEditMode) {
            nursery.setId(nurseryId);
        }
        nursery.setName(name);
        nursery.setDescription(description);
        nursery.setLocation(location);
        nursery.setAddress(addressEditText.getText().toString().trim());
        nursery.setPhone(phoneEditText.getText().toString().trim());
        nursery.setEmail(emailEditText.getText().toString().trim());
        nursery.setInstagram(instagramEditText.getText().toString().trim());
        
        try {
            nursery.setRegistrationFee(Double.parseDouble(registrationFeeEditText.getText().toString()));
            nursery.setMonthlyFee(Double.parseDouble(monthlyFeeEditText.getText().toString()));
        } catch (NumberFormatException e) {
            nursery.setRegistrationFee(0);
            nursery.setMonthlyFee(0);
        }

        // Parse lists
        String ageGroupsText = ageGroupsEditText.getText().toString().trim();
        if (!ageGroupsText.isEmpty()) {
            nursery.setAgeGroups(Arrays.asList(ageGroupsText.split(",")));
        } else {
            nursery.setAgeGroups(new ArrayList<>());
        }

        String facilitiesText = facilitiesEditText.getText().toString().trim();
        if (!facilitiesText.isEmpty()) {
            nursery.setFacilities(Arrays.asList(facilitiesText.split(",")));
        } else {
            nursery.setFacilities(new ArrayList<>());
        }

        String featuresText = featuresEditText.getText().toString().trim();
        if (!featuresText.isEmpty()) {
            nursery.setFeatures(Arrays.asList(featuresText.split(",")));
        } else {
            nursery.setFeatures(new ArrayList<>());
        }

        nursery.setActive(true);
        nursery.setRating(0);
        nursery.setReviewCount(0);

        if (isEditMode) {
            db.collection("nurseries").document(nurseryId).set(nursery)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(Task<Void> task) {
                            progressBar.setVisibility(View.GONE);
                            saveButton.setEnabled(true);
                            
                            if (task.isSuccessful()) {
                                Toast.makeText(AddEditNurseryActivity.this, "Nursery updated successfully", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(AddEditNurseryActivity.this, "Error updating: " + 
                                        (task.getException() != null ? task.getException().getMessage() : "Unknown error"), 
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            db.collection("nurseries").add(nursery)
                    .addOnCompleteListener(new OnCompleteListener<com.google.firebase.firestore.DocumentReference>() {
                        @Override
                        public void onComplete(Task<com.google.firebase.firestore.DocumentReference> task) {
                            progressBar.setVisibility(View.GONE);
                            saveButton.setEnabled(true);
                            
                            if (task.isSuccessful()) {
                                Toast.makeText(AddEditNurseryActivity.this, "Nursery added successfully", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(AddEditNurseryActivity.this, "Error adding: " + 
                                        (task.getException() != null ? task.getException().getMessage() : "Unknown error"), 
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}
