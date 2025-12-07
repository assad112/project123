package com.warmclouds.app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.button.MaterialButton;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.warmclouds.app.adapters.FeaturesAdapter;
import com.warmclouds.app.adapters.ImageSliderAdapter;
import com.warmclouds.app.adapters.ReviewAdapter;
import com.warmclouds.app.models.Nursery;
import com.warmclouds.app.models.Review;

import java.util.ArrayList;
import java.util.List;

public class NurseryDetailsActivity extends AppCompatActivity {

    private ViewPager2 imageViewPager;
    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private MaterialButton backButton, favoriteButton, shareButton;
    private TextView nameTextView, descriptionTextView, locationTextView;
    private TextView phoneTextView, emailTextView, instagramTextView;
    private TextView registrationFeeTextView, monthlyFeeTextView;
    private android.view.View instagramLayout, instagramDivider;
    private TextView ageGroupsTextView, facilitiesTextView;
    private RatingBar ratingBar;
    private TextView ratingTextView;
    private RecyclerView featuresRecyclerView, reviewsRecyclerView;
    private Button bookButton;
    private ProgressBar progressBar;

    private Nursery nursery;
    private FirebaseFirestore db;
    private String nurseryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nursery_details);

        db = FirebaseFirestore.getInstance();
        nurseryId = getIntent().getStringExtra("nursery_id");

        if (nurseryId == null) {
            Toast.makeText(this, "Error loading nursery data", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initializeViews();
        setupToolbar();
        loadNurseryDetails();
    }

    private void setupToolbar() {
        toolbar = findViewById(R.id.toolbar);
        collapsingToolbarLayout = findViewById(R.id.collapsingToolbarLayout);
        backButton = findViewById(R.id.backButton);
        favoriteButton = findViewById(R.id.favoriteButton);
        shareButton = findViewById(R.id.shareButton);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        backButton.setOnClickListener(v -> finish());

        favoriteButton.setOnClickListener(v -> {
            // TODO: Implement favorite functionality
            Toast.makeText(this, "Added to favorites", Toast.LENGTH_SHORT).show();
        });

        shareButton.setOnClickListener(v -> {
            // Share nursery details
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            String shareText = nursery != null ? 
                "Check out this nursery: " + nursery.getName() + "\n" + 
                (nursery.getLocation() != null ? "Location: " + nursery.getLocation() : "") :
                "Check out this nursery";
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
            startActivity(Intent.createChooser(shareIntent, "Share Nursery"));
        });
    }

    private void initializeViews() {
        imageViewPager = findViewById(R.id.imageViewPager);
        nameTextView = findViewById(R.id.nameTextView);
        descriptionTextView = findViewById(R.id.descriptionTextView);
        locationTextView = findViewById(R.id.locationTextView);
        phoneTextView = findViewById(R.id.phoneTextView);
        emailTextView = findViewById(R.id.emailTextView);
        instagramTextView = findViewById(R.id.instagramTextView);
        registrationFeeTextView = findViewById(R.id.registrationFeeTextView);
        monthlyFeeTextView = findViewById(R.id.monthlyFeeTextView);
        instagramLayout = findViewById(R.id.instagramLayout);
        instagramDivider = findViewById(R.id.instagramDivider);
        ageGroupsTextView = findViewById(R.id.ageGroupsTextView);
        facilitiesTextView = findViewById(R.id.facilitiesTextView);
        ratingBar = findViewById(R.id.ratingBar);
        ratingTextView = findViewById(R.id.ratingTextView);
        featuresRecyclerView = findViewById(R.id.featuresRecyclerView);
        reviewsRecyclerView = findViewById(R.id.reviewsRecyclerView);
        bookButton = findViewById(R.id.bookButton);
        progressBar = findViewById(R.id.progressBar);

        bookButton.setOnClickListener(v -> {
            Intent intent = new Intent(NurseryDetailsActivity.this, BookingActivity.class);
            intent.putExtra("nursery_id", nurseryId);
            intent.putExtra("nursery_name", nursery != null ? nursery.getName() : "");
            intent.putExtra("registration_fee", nursery != null ? nursery.getRegistrationFee() : 0);
            startActivity(intent);
        });

        // Setup features RecyclerView
        featuresRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        
        // Setup reviews RecyclerView
        reviewsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadNurseryDetails() {
        progressBar.setVisibility(View.VISIBLE);

        db.collection("nurseries").document(nurseryId).get()
                .addOnCompleteListener(task -> {
                    progressBar.setVisibility(View.GONE);

                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null && document.exists()) {
                            nursery = document.toObject(Nursery.class);
                            if (nursery != null) {
                                nursery.setId(document.getId());
                                displayNurseryDetails();
                                loadReviews();
                            }
                        } else {
                            Toast.makeText(this, "Nursery not found", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    } else {
                        Toast.makeText(this, "Error loading data: " + 
                                (task.getException() != null ? task.getException().getMessage() : "Unknown error"), 
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void displayNurseryDetails() {
        if (nursery == null) return;

        // Set collapsing toolbar title
        if (collapsingToolbarLayout != null) {
            collapsingToolbarLayout.setTitle(nursery.getName());
        }

        nameTextView.setText(nursery.getName());
        descriptionTextView.setText(nursery.getDescription());
        locationTextView.setText(nursery.getLocation());
        
        if (nursery.getPhone() != null && !nursery.getPhone().isEmpty()) {
            phoneTextView.setText(nursery.getPhone());
            phoneTextView.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + nursery.getPhone()));
                startActivity(intent);
            });
        } else {
            phoneTextView.setVisibility(View.GONE);
        }
        
        if (nursery.getEmail() != null && !nursery.getEmail().isEmpty()) {
            emailTextView.setText(nursery.getEmail());
            emailTextView.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:" + nursery.getEmail()));
                startActivity(intent);
            });
        } else {
            emailTextView.setVisibility(View.GONE);
        }
        
        if (nursery.getInstagram() != null && !nursery.getInstagram().isEmpty()) {
            instagramTextView.setText(nursery.getInstagram());
            instagramLayout.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://instagram.com/" + nursery.getInstagram()));
                startActivity(intent);
            });
            instagramLayout.setVisibility(View.VISIBLE);
            instagramDivider.setVisibility(View.VISIBLE);
        } else {
            instagramLayout.setVisibility(View.GONE);
            instagramDivider.setVisibility(View.GONE);
        }

        registrationFeeTextView.setText(String.format("$%.0f", nursery.getRegistrationFee()));
        monthlyFeeTextView.setText(String.format("$%.0f", nursery.getMonthlyFee()));

        if (nursery.getAgeGroups() != null && !nursery.getAgeGroups().isEmpty()) {
            ageGroupsTextView.setText(String.join(", ", nursery.getAgeGroups()));
        } else {
            ageGroupsTextView.setVisibility(View.GONE);
        }

        if (nursery.getFacilities() != null && !nursery.getFacilities().isEmpty()) {
            facilitiesTextView.setText(String.join(", ", nursery.getFacilities()));
        } else {
            facilitiesTextView.setVisibility(View.GONE);
        }

        if (nursery.getRating() > 0) {
            ratingBar.setRating((float) nursery.getRating());
            ratingTextView.setText(String.format("%.1f (%d reviews)", nursery.getRating(), nursery.getReviewCount()));
        } else {
            ratingBar.setRating(0);
            ratingTextView.setText("No ratings yet");
        }

        // Setup image slider
        if (nursery.getImages() != null && !nursery.getImages().isEmpty()) {
            ImageSliderAdapter adapter = new ImageSliderAdapter(nursery.getImages());
            imageViewPager.setAdapter(adapter);
        } else {
            imageViewPager.setVisibility(View.GONE);
        }

        // Setup features
        if (nursery.getFeatures() != null && !nursery.getFeatures().isEmpty()) {
            FeaturesAdapter adapter = new FeaturesAdapter(nursery.getFeatures());
            featuresRecyclerView.setAdapter(adapter);
        } else {
            featuresRecyclerView.setVisibility(View.GONE);
        }
    }

    private void loadReviews() {
        db.collection("reviews")
                .whereEqualTo("nurseryId", nurseryId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Review> reviews = new ArrayList<>();
                        for (DocumentSnapshot document : task.getResult()) {
                            Review review = document.toObject(Review.class);
                            if (review != null) {
                                review.setId(document.getId());
                                reviews.add(review);
                            }
                        }
                        if (reviews.isEmpty()) {
                            reviewsRecyclerView.setVisibility(View.GONE);
                        } else {
                            ReviewAdapter adapter = new ReviewAdapter(reviews);
                            reviewsRecyclerView.setAdapter(adapter);
                        }
                    }
                });
    }
}
