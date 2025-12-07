package com.warmclouds.app;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.warmclouds.app.models.Booking;

import java.util.UUID;

public class BookingActivity extends AppCompatActivity {

    private EditText childNameEditText, childAgeEditText;
    private EditText parentNameEditText, parentPhoneEditText, parentEmailEditText;
    private Spinner ageGroupSpinner;
    private Button confirmButton;
    private ProgressBar progressBar;

    private String nurseryId, nurseryName;
    private double registrationFee;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // Get data from intent
        nurseryId = getIntent().getStringExtra("nursery_id");
        nurseryName = getIntent().getStringExtra("nursery_name");
        registrationFee = getIntent().getDoubleExtra("registration_fee", 0);

        if (nurseryId == null) {
            Toast.makeText(this, "Error in nursery data", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        if (mAuth.getCurrentUser() == null) {
            Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initializeViews();
        setupAgeGroupSpinner();
    }

    private void initializeViews() {
        childNameEditText = findViewById(R.id.childNameEditText);
        childAgeEditText = findViewById(R.id.childAgeEditText);
        ageGroupSpinner = findViewById(R.id.ageGroupSpinner);
        parentNameEditText = findViewById(R.id.parentNameEditText);
        parentPhoneEditText = findViewById(R.id.parentPhoneEditText);
        parentEmailEditText = findViewById(R.id.parentEmailEditText);
        confirmButton = findViewById(R.id.confirmButton);
        progressBar = findViewById(R.id.progressBar);

        // Set current user email if available
        if (mAuth.getCurrentUser() != null) {
            parentEmailEditText.setText(mAuth.getCurrentUser().getEmail());
        }

        confirmButton.setOnClickListener(v -> createBooking());
    }

    private void setupAgeGroupSpinner() {
        // This should be loaded from nursery data, but for now using default
        String[] ageGroups = {"2-3 years", "3-4 years", "4-5 years", "5-6 years"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, 
                android.R.layout.simple_spinner_item, ageGroups);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ageGroupSpinner.setAdapter(adapter);
    }

    private void createBooking() {
        String childName = childNameEditText.getText().toString().trim();
        String childAge = childAgeEditText.getText().toString().trim();
        String ageGroup = ageGroupSpinner.getSelectedItem().toString();
        String parentName = parentNameEditText.getText().toString().trim();
        String parentPhone = parentPhoneEditText.getText().toString().trim();
        String parentEmail = parentEmailEditText.getText().toString().trim();

        if (TextUtils.isEmpty(childName) || TextUtils.isEmpty(childAge) ||
            TextUtils.isEmpty(parentName) || TextUtils.isEmpty(parentPhone) ||
            TextUtils.isEmpty(parentEmail)) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        confirmButton.setEnabled(false);

        Booking booking = new Booking(mAuth.getCurrentUser().getUid(), nurseryId, nurseryName);
        booking.setChildName(childName);
        booking.setChildAge(childAge);
        booking.setAgeGroup(ageGroup);
        booking.setParentName(parentName);
        booking.setParentPhone(parentPhone);
        booking.setParentEmail(parentEmail);
        booking.setRegistrationFee(registrationFee);
        booking.setBookingCode(UUID.randomUUID().toString().substring(0, 8).toUpperCase());

        db.collection("bookings").add(booking)
                .addOnCompleteListener(new OnCompleteListener<com.google.firebase.firestore.DocumentReference>() {
                    @Override
                    public void onComplete(Task<com.google.firebase.firestore.DocumentReference> task) {
                        progressBar.setVisibility(View.GONE);
                        confirmButton.setEnabled(true);

                        if (task.isSuccessful()) {
                            booking.setId(task.getResult().getId());
                            // Navigate to payment
                            Intent intent = new Intent(BookingActivity.this, PaymentActivity.class);
                            intent.putExtra("booking_id", booking.getId());
                            intent.putExtra("booking_code", booking.getBookingCode());
                            intent.putExtra("child_name", childName);
                            intent.putExtra("age_group", ageGroup);
                            intent.putExtra("nursery_name", nurseryName);
                            intent.putExtra("registration_fee", registrationFee);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(BookingActivity.this, "Error creating booking: " + 
                                    (task.getException() != null ? task.getException().getMessage() : "Unknown error"), 
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
