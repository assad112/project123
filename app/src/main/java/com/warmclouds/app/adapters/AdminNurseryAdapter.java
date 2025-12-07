package com.warmclouds.app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.textview.MaterialTextView;
import com.warmclouds.app.R;
import com.warmclouds.app.models.Nursery;
import com.warmclouds.app.utils.ImageUtils;

import java.util.List;

public class AdminNurseryAdapter extends RecyclerView.Adapter<AdminNurseryAdapter.NurseryViewHolder> {

    private List<Nursery> nurseries;
    private OnNurseryActionListener listener;

    public interface OnNurseryActionListener {
        void onEditClick(Nursery nursery);
        void onDeleteClick(Nursery nursery);
    }

    public AdminNurseryAdapter(List<Nursery> nurseries, OnNurseryActionListener listener) {
        this.nurseries = nurseries;
        this.listener = listener;
    }

    @NonNull
    @Override
    public NurseryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_admin_nursery, parent, false);
        return new NurseryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NurseryViewHolder holder, int position) {
        holder.bind(nurseries.get(position));
    }

    @Override
    public int getItemCount() {
        return nurseries.size();
    }

    class NurseryViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private MaterialTextView nameTextView;
        private MaterialTextView locationTextView;
        private Chip statusChip;
        private MaterialButton editButton;
        private MaterialButton deleteButton;

        public NurseryViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.nurseryImageView);
            nameTextView = itemView.findViewById(R.id.nurseryNameTextView);
            locationTextView = itemView.findViewById(R.id.nurseryLocationTextView);
            statusChip = itemView.findViewById(R.id.statusChip);
            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }

        public void bind(Nursery nursery) {
            nameTextView.setText(nursery.getName());
            locationTextView.setText(nursery.getLocation());
            
            // Update status chip
            if (nursery.isActive()) {
                statusChip.setText("Active");
                statusChip.setChipBackgroundColorResource(R.color.success_light);
                statusChip.setChipStrokeColorResource(R.color.success);
            } else {
                statusChip.setText("Inactive");
                statusChip.setChipBackgroundColorResource(R.color.error_light);
                statusChip.setChipStrokeColorResource(R.color.error);
            }

            // Load image using ImageUtils
            if (nursery.getImages() != null && !nursery.getImages().isEmpty()) {
                ImageUtils.loadRoundedImage(
                        itemView.getContext(),
                        nursery.getImages().get(0),
                        imageView,
                        12 // corner radius in pixels
                );
            } else {
                imageView.setImageResource(R.drawable.ic_nursery_empty);
            }

            editButton.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onEditClick(nursery);
                }
            });

            deleteButton.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDeleteClick(nursery);
                }
            });
        }
    }
}
