package com.warmclouds.app.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.warmclouds.app.R;
import com.warmclouds.app.adapters.BookingAdapter;
import com.warmclouds.app.models.Booking;

import java.util.ArrayList;
import java.util.List;

public class BookingsFragment extends Fragment {

    private RecyclerView bookingsRecyclerView;
    private ProgressBar progressBar;
    private ConstraintLayout emptyStateLayout;

    private BookingAdapter adapter;
    private List<Booking> bookings;
    private FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bookings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = FirebaseFirestore.getInstance();
        bookings = new ArrayList<>();

        initializeViews(view);
        setupRecyclerView();
        loadBookings();
    }

    private void initializeViews(View view) {
        bookingsRecyclerView = view.findViewById(R.id.bookingsRecyclerView);
        progressBar = view.findViewById(R.id.progressBar);
        emptyStateLayout = view.findViewById(R.id.emptyStateLayout);
    }

    private void setupRecyclerView() {
        bookingsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new BookingAdapter(bookings, new BookingAdapter.OnBookingActionListener() {
            @Override
            public void onConfirmClick(Booking booking) {
                confirmBooking(booking);
            }

            @Override
            public void onRejectClick(Booking booking) {
                rejectBooking(booking);
            }
        });
        bookingsRecyclerView.setAdapter(adapter);
    }

    private void loadBookings() {
        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
        }
        if (bookingsRecyclerView != null) {
            bookingsRecyclerView.setVisibility(View.GONE);
        }
        if (emptyStateLayout != null) {
            emptyStateLayout.setVisibility(View.GONE);
        }

        db.collection("bookings")
                .orderBy("bookingDate", com.google.firebase.firestore.Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<com.google.firebase.firestore.QuerySnapshot>() {
                    @Override
                    public void onComplete(Task<com.google.firebase.firestore.QuerySnapshot> task) {
                        if (progressBar != null) {
                            progressBar.setVisibility(View.GONE);
                        }

                        if (task.isSuccessful()) {
                            bookings.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Booking booking = document.toObject(Booking.class);
                                booking.setId(document.getId());
                                bookings.add(booking);
                            }
                            adapter.notifyDataSetChanged();

                            if (bookings.isEmpty()) {
                                showEmptyState();
                            } else {
                                showBookingsList();
                            }
                        } else {
                            Toast.makeText(getContext(), "Error loading bookings: " +
                                    (task.getException() != null ? task.getException().getMessage() : "Unknown error"),
                                    Toast.LENGTH_SHORT).show();
                            showEmptyState();
                        }
                    }
                });
    }

    private void showEmptyState() {
        if (bookingsRecyclerView != null) {
            bookingsRecyclerView.setVisibility(View.GONE);
        }
        if (emptyStateLayout != null) {
            emptyStateLayout.setVisibility(View.VISIBLE);
        }
    }

    private void showBookingsList() {
        if (bookingsRecyclerView != null) {
            bookingsRecyclerView.setVisibility(View.VISIBLE);
        }
        if (emptyStateLayout != null) {
            emptyStateLayout.setVisibility(View.GONE);
        }
    }

    private void confirmBooking(Booking booking) {
        new AlertDialog.Builder(getContext())
                .setTitle("Confirm Booking")
                .setMessage("Are you sure you want to accept this booking?")
                .setPositiveButton("Accept", (dialog, which) -> {
                    performConfirm(booking);
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    dialog.dismiss();
                })
                .show();
    }

    private void rejectBooking(Booking booking) {
        new AlertDialog.Builder(getContext())
                .setTitle("Reject Booking")
                .setMessage("Are you sure you want to reject this booking?")
                .setPositiveButton("Reject", (dialog, which) -> {
                    performReject(booking);
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    dialog.dismiss();
                })
                .show();
    }

    private void performConfirm(Booking booking) {
        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
        }

        db.collection("bookings").document(booking.getId())
                .update("status", "confirmed")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(Task<Void> task) {
                        if (progressBar != null) {
                            progressBar.setVisibility(View.GONE);
                        }

                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "Booking accepted successfully", Toast.LENGTH_SHORT).show();
                            booking.setStatus("confirmed");
                            adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getContext(), "Error accepting booking: " +
                                    (task.getException() != null ? task.getException().getMessage() : "Unknown error"),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void performReject(Booking booking) {
        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
        }

        db.collection("bookings").document(booking.getId())
                .update("status", "cancelled")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(Task<Void> task) {
                        if (progressBar != null) {
                            progressBar.setVisibility(View.GONE);
                        }

                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "Booking rejected", Toast.LENGTH_SHORT).show();
                            booking.setStatus("cancelled");
                            adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getContext(), "Error rejecting booking: " +
                                    (task.getException() != null ? task.getException().getMessage() : "Unknown error"),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        loadBookings();
    }
}


