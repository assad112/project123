package com.warmclouds.app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.warmclouds.app.R;

import java.util.List;

public class FeaturesAdapter extends RecyclerView.Adapter<FeaturesAdapter.FeatureViewHolder> {

    private List<String> features;

    public FeaturesAdapter(List<String> features) {
        this.features = features;
    }

    @NonNull
    @Override
    public FeatureViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_feature, parent, false);
        return new FeatureViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeatureViewHolder holder, int position) {
        holder.textView.setText("• " + features.get(position));
    }

    @Override
    public int getItemCount() {
        return features != null ? features.size() : 0;
    }

    static class FeatureViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public FeatureViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.featureTextView);
        }
    }
}


