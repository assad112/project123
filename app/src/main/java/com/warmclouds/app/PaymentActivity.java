package com.warmclouds.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

public class PaymentActivity extends AppCompatActivity {

    private TextView childNameTextView, ageGroupTextView, nurseryNameTextView;
    private TextView registrationFeeTextView, bookingCodeTextView;
    private Button payButton;
    private ProgressBar progressBar;

    private String bookingId, bookingCode, childName, ageGroup, nurseryName;
    private double registrationFee;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        db = FirebaseFirestore.getInstance();

        // Get data from intent
        bookingId = getIntent().getStringExtra("booking_id");
        bookingCode = getIntent().getStringExtra("booking_code");
        childName = getIntent().getStringExtra("child_name");
        ageGroup = getIntent().getStringExtra("age_group");
        nurseryName = getIntent().getStringExtra("nursery_name");
        registrationFee = getIntent().getDoubleExtra("registration_fee", 0);

        if (bookingId == null) {
            Toast.makeText(this, "Error in booking data", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initializeViews();
        displayBookingSummary();
    }

    private void initializeViews() {
        childNameTextView = findViewById(R.id.childNameTextView);
        ageGroupTextView = findViewById(R.id.ageGroupTextView);
        nurseryNameTextView = findViewById(R.id.nurseryNameTextView);
        registrationFeeTextView = findViewById(R.id.registrationFeeTextView);
        bookingCodeTextView = findViewById(R.id.bookingCodeTextView);
        payButton = findViewById(R.id.payButton);
        progressBar = findViewById(R.id.progressBar);

        payButton.setOnClickListener(v -> processPayment());
    }

    private void displayBookingSummary() {
        childNameTextView.setText(childName);
        ageGroupTextView.setText(ageGroup);
        nurseryNameTextView.setText(nurseryName);
        registrationFeeTextView.setText(String.format("$%.0f", registrationFee));
        if (bookingCode != null) {
            bookingCodeTextView.setText("Booking Code: " + bookingCode);
        }
    }

    private void processPayment() {
        progressBar.setVisibility(View.VISIBLE);
        payButton.setEnabled(false);

        // In a real app, you would integrate with a payment gateway here
        // For now, we'll simulate a successful payment
        // Update booking status to confirmed
        db.collection("bookings").document(bookingId)
                .update("status", "confirmed")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(Task<Void> task) {
                        progressBar.setVisibility(View.GONE);
                        payButton.setEnabled(true);

                        if (task.isSuccessful()) {
                            // Show success message
                            Toast.makeText(PaymentActivity.this, R.string.payment_success, Toast.LENGTH_LONG).show();
                            
                            // Send confirmation (in real app, send email/SMS)
                            // For now, just navigate back to search
                            Intent intent = new Intent(PaymentActivity.this, SearchActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(PaymentActivity.this, "Error confirming payment: " + 
                                    (task.getException() != null ? task.getException().getMessage() : "Unknown error"), 
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
