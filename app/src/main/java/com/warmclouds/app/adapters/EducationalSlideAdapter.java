package com.warmclouds.app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.warmclouds.app.R;
import com.warmclouds.app.models.EducationalSlide;

import java.util.List;

public class EducationalSlideAdapter extends RecyclerView.Adapter<EducationalSlideAdapter.SlideViewHolder> {

    private List<EducationalSlide> slides;

    public EducationalSlideAdapter(List<EducationalSlide> slides) {
        this.slides = slides;
    }

    @NonNull
    @Override
    public SlideViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_educational_slide, parent, false);
        return new SlideViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SlideViewHolder holder, int position) {
        EducationalSlide slide = slides.get(position);
        holder.bind(slide);
    }

    @Override
    public int getItemCount() {
        return slides.size();
    }

    static class SlideViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView titleTextView;
        private TextView descriptionTextView;

        public SlideViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.slideImageView);
            titleTextView = itemView.findViewById(R.id.slideTitleTextView);
            descriptionTextView = itemView.findViewById(R.id.slideDescriptionTextView);
        }

        public void bind(EducationalSlide slide) {
            imageView.setImageResource(slide.getImageResource());
            titleTextView.setText(slide.getTitle());
            descriptionTextView.setText(slide.getDescription());
        }
    }
}
