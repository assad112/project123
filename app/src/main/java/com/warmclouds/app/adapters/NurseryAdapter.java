package com.warmclouds.app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.warmclouds.app.R;
import com.warmclouds.app.models.Nursery;
import com.warmclouds.app.utils.ImageUtils;

import java.util.List;

public class NurseryAdapter extends RecyclerView.Adapter<NurseryAdapter.NurseryViewHolder> {

    private List<Nursery> nurseries;
    private OnNurseryClickListener listener;

    public interface OnNurseryClickListener {
        void onNurseryClick(Nursery nursery);
    }

    public NurseryAdapter(List<Nursery> nurseries, OnNurseryClickListener listener) {
        this.nurseries = nurseries;
        this.listener = listener;
    }

    @NonNull
    @Override
    public NurseryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_nursery, parent, false);
        return new NurseryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NurseryViewHolder holder, int position) {
        Nursery nursery = nurseries.get(position);
        holder.bind(nursery);
    }

    @Override
    public int getItemCount() {
        return nurseries.size();
    }

    class NurseryViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView nameTextView;
        private TextView locationTextView;
        private TextView priceTextView;
        private RatingBar ratingBar;
        private TextView ratingTextView;

        public NurseryViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.nurseryImageView);
            nameTextView = itemView.findViewById(R.id.nurseryNameTextView);
            locationTextView = itemView.findViewById(R.id.nurseryLocationTextView);
            priceTextView = itemView.findViewById(R.id.nurseryPriceTextView);
            ratingBar = itemView.findViewById(R.id.nurseryRatingBar);
            ratingTextView = itemView.findViewById(R.id.nurseryRatingTextView);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onNurseryClick(nurseries.get(position));
                }
            });
        }

        public void bind(Nursery nursery) {
            nameTextView.setText(nursery.getName());
            locationTextView.setText(nursery.getLocation());
            priceTextView.setText(String.format("$%.0f", nursery.getRegistrationFee()));
            
            if (nursery.getRating() > 0) {
                ratingBar.setRating((float) nursery.getRating());
                ratingTextView.setText(String.format("%.1f", nursery.getRating()));
            } else {
                ratingBar.setRating(0);
                ratingTextView.setText("0.0");
            }

            // Load image using ImageUtils
            if (nursery.getImages() != null && !nursery.getImages().isEmpty()) {
                ImageUtils.loadRoundedImage(
                        itemView.getContext(),
                        nursery.getImages().get(0),
                        imageView,
                        16 // corner radius in pixels
                );
            } else {
                imageView.setImageResource(R.drawable.ic_nursery_empty);
            }
        }
    }
}


