package com.warmclouds.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.warmclouds.app.adapters.BookingAdapter;
import com.warmclouds.app.models.Booking;
import com.warmclouds.app.models.User;

import java.util.ArrayList;
import java.util.List;

public class ParentDashboardActivity extends AppCompatActivity {

    private TextView welcomeTextView, userNameTextView;
    private MaterialButton searchNurseriesButton, myBookingsButton, logoutButton;
    private RecyclerView recentBookingsRecyclerView;
    private ProgressBar progressBar;
    private android.view.View emptyStateLayout;

    private BookingAdapter bookingAdapter;
    private ArrayList<Booking> recentBookings;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_dashboard);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        recentBookings = new ArrayList<>();

        initializeViews();
        loadUserData();
        loadRecentBookings();
    }

    private void initializeViews() {
        welcomeTextView = findViewById(R.id.welcomeTextView);
        userNameTextView = findViewById(R.id.userNameTextView);
        searchNurseriesButton = findViewById(R.id.searchNurseriesButton);
        myBookingsButton = findViewById(R.id.myBookingsButton);
        logoutButton = findViewById(R.id.logoutButton);
        recentBookingsRecyclerView = findViewById(R.id.recentBookingsRecyclerView);
        progressBar = findViewById(R.id.progressBar);
        emptyStateLayout = findViewById(R.id.emptyStateLayout);

        searchNurseriesButton.setOnClickListener(v -> {
            Intent intent = new Intent(ParentDashboardActivity.this, SearchActivity.class);
            startActivity(intent);
        });

        myBookingsButton.setOnClickListener(v -> {
            // Navigate to bookings list (can be same activity or different)
            loadRecentBookings();
        });

        logoutButton.setOnClickListener(v -> logout());

        // Setup RecyclerView
        recentBookingsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        bookingAdapter = new BookingAdapter(recentBookings, new BookingAdapter.OnBookingActionListener() {
            @Override
            public void onConfirmClick(Booking booking) {
                // Parents can't confirm bookings, this is for admin only
            }

            @Override
            public void onRejectClick(Booking booking) {
                // Parents can't reject bookings, this is for admin only
            }
        });
        recentBookingsRecyclerView.setAdapter(bookingAdapter);
    }

    private void loadUserData() {
        if (currentUser == null) {
            Toast.makeText(this, "Error: User not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        db.collection("users").document(currentUser.getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(Task<DocumentSnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                User user = document.toObject(User.class);
                                if (user != null && userNameTextView != null) {
                                    userNameTextView.setText(user.getName() != null ? user.getName() : "Parent");
                                }
                            } else {
                                if (userNameTextView != null) {
                                    userNameTextView.setText("Parent");
                                }
                            }
                        }
                    }
                });
    }

    private void loadRecentBookings() {
        if (currentUser == null) {
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        // Query bookings for current user
        // Note: Using orderBy with whereEqualTo requires a composite index in Firestore
        // For now, we'll fetch all user bookings and sort in memory
        db.collection("bookings")
                .whereEqualTo("userId", currentUser.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<com.google.firebase.firestore.QuerySnapshot>() {
                    @Override
                    public void onComplete(Task<com.google.firebase.firestore.QuerySnapshot> task) {
                        progressBar.setVisibility(View.GONE);

                        if (task.isSuccessful()) {
                            recentBookings.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Booking booking = document.toObject(Booking.class);
                                booking.setId(document.getId());
                                recentBookings.add(booking);
                            }
                            
                            // Sort by bookingDate descending (most recent first)
                            java.util.Collections.sort(recentBookings, (b1, b2) -> {
                                long date1 = b1.getBookingDate();
                                long date2 = b2.getBookingDate();
                                return Long.compare(date2, date1); // Descending order
                            });
                            
                            // Limit to 5 most recent
                            if (recentBookings.size() > 5) {
                                recentBookings = new ArrayList<>(recentBookings.subList(0, 5));
                            }
                            
                            bookingAdapter.notifyDataSetChanged();
                            
                            // Show/hide empty state
                            if (recentBookings.isEmpty()) {
                                emptyStateLayout.setVisibility(View.VISIBLE);
                                recentBookingsRecyclerView.setVisibility(View.GONE);
                            } else {
                                emptyStateLayout.setVisibility(View.GONE);
                                recentBookingsRecyclerView.setVisibility(View.VISIBLE);
                            }
                        } else {
                            Exception exception = task.getException();
                            String errorMessage = "Error loading bookings";
                            
                            if (exception != null) {
                                String exceptionMessage = exception.getMessage();
                                if (exceptionMessage != null && exceptionMessage.contains("FAILED_PRECONDITION")) {
                                    errorMessage = "Please reinstall the app or clear data";
                                } else {
                                    errorMessage += ": " + exceptionMessage;
                                }
                            }
                            
                            Toast.makeText(ParentDashboardActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                            
                            // Show empty state on error
                            emptyStateLayout.setVisibility(View.VISIBLE);
                            recentBookingsRecyclerView.setVisibility(View.GONE);
                        }
                    }
                });
    }

    private void logout() {
        mAuth.signOut();
        Intent intent = new Intent(ParentDashboardActivity.this, WelcomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadRecentBookings();
    }
}


