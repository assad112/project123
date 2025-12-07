package com.warmclouds.app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.textview.MaterialTextView;
import com.warmclouds.app.R;
import com.warmclouds.app.models.Booking;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {

    private List<Booking> bookings;
    private OnBookingActionListener listener;

    public interface OnBookingActionListener {
        void onConfirmClick(Booking booking);
        void onRejectClick(Booking booking);
    }

    public BookingAdapter(List<Booking> bookings, OnBookingActionListener listener) {
        this.bookings = bookings;
        this.listener = listener;
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_booking, parent, false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        holder.bind(bookings.get(position));
    }

    @Override
    public int getItemCount() {
        return bookings.size();
    }

    class BookingViewHolder extends RecyclerView.ViewHolder {
        private MaterialTextView bookingCodeTextView;
        private Chip statusChip;
        private MaterialTextView nurseryNameTextView;
        private MaterialTextView childNameTextView;
        private MaterialTextView childAgeTextView;
        private MaterialTextView parentNameTextView;
        private MaterialTextView parentPhoneTextView;
        private MaterialTextView feeTextView;
        private MaterialButton confirmButton;
        private MaterialButton rejectButton;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            bookingCodeTextView = itemView.findViewById(R.id.bookingCodeTextView);
            statusChip = itemView.findViewById(R.id.statusChip);
            nurseryNameTextView = itemView.findViewById(R.id.nurseryNameTextView);
            childNameTextView = itemView.findViewById(R.id.childNameTextView);
            childAgeTextView = itemView.findViewById(R.id.childAgeTextView);
            parentNameTextView = itemView.findViewById(R.id.parentNameTextView);
            parentPhoneTextView = itemView.findViewById(R.id.parentPhoneTextView);
            feeTextView = itemView.findViewById(R.id.feeTextView);
            confirmButton = itemView.findViewById(R.id.confirmButton);
            rejectButton = itemView.findViewById(R.id.rejectButton);
        }

        public void bind(Booking booking) {
            // Booking Code
            if (booking.getBookingCode() != null && !booking.getBookingCode().isEmpty()) {
                bookingCodeTextView.setText(booking.getBookingCode());
            } else {
                bookingCodeTextView.setText("N/A");
            }

            // Status
            String status = booking.getStatus();
            if (status == null) {
                status = "pending";
            }

            switch (status) {
                case "pending":
                    statusChip.setText("Pending");
                    statusChip.setChipBackgroundColorResource(R.color.warning);
                    statusChip.setChipStrokeColorResource(R.color.warning);
                    confirmButton.setVisibility(View.VISIBLE);
                    rejectButton.setVisibility(View.VISIBLE);
                    break;
                case "confirmed":
                    statusChip.setText("Confirmed");
                    statusChip.setChipBackgroundColorResource(R.color.success_light);
                    statusChip.setChipStrokeColorResource(R.color.success);
                    confirmButton.setVisibility(View.GONE);
                    rejectButton.setVisibility(View.GONE);
                    break;
                case "cancelled":
                    statusChip.setText("Cancelled");
                    statusChip.setChipBackgroundColorResource(R.color.error_light);
                    statusChip.setChipStrokeColorResource(R.color.error);
                    confirmButton.setVisibility(View.GONE);
                    rejectButton.setVisibility(View.GONE);
                    break;
                default:
                    statusChip.setText("Pending");
                    statusChip.setChipBackgroundColorResource(R.color.warning);
                    statusChip.setChipStrokeColorResource(R.color.warning);
                    break;
            }

            // Nursery Name
            nurseryNameTextView.setText(booking.getNurseryName() != null ? 
                    booking.getNurseryName() : "Not specified");

            // Child Info
            childNameTextView.setText(booking.getChildName() != null ? 
                    booking.getChildName() : "Not specified");
            childAgeTextView.setText(booking.getChildAge() != null ? 
                    booking.getChildAge() + " years" : "Not specified");

            // Parent Info
            parentNameTextView.setText(booking.getParentName() != null ? 
                    booking.getParentName() : "Not specified");
            parentPhoneTextView.setText(booking.getParentPhone() != null ? 
                    booking.getParentPhone() : "");

            // Fee
            feeTextView.setText(String.format(Locale.getDefault(), 
                    "Registration Fee: $%.0f", booking.getRegistrationFee()));

            // Buttons
            confirmButton.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onConfirmClick(booking);
                }
            });

            rejectButton.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onRejectClick(booking);
                }
            });
        }
    }
}


