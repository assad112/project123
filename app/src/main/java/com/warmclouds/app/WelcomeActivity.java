package com.warmclouds.app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.warmclouds.app.adapters.EducationalSlideAdapter;
import com.warmclouds.app.models.EducationalSlide;

import java.util.ArrayList;
import java.util.List;

public class WelcomeActivity extends AppCompatActivity {

    private ImageView logoImageView;
    private TextView appNameTextView;
    private TextView welcomeTextView;
    private Button getStartedButton;
    private ViewPager2 educationalViewPager;
    private View indicator1, indicator2, indicator3;
    private List<EducationalSlide> slides;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        // Initialize views
        logoImageView = findViewById(R.id.logoImageView);
        appNameTextView = findViewById(R.id.appNameTextView);
        welcomeTextView = findViewById(R.id.welcomeTextView);
        getStartedButton = findViewById(R.id.getStartedButton);
        educationalViewPager = findViewById(R.id.educationalViewPager);
        indicator1 = findViewById(R.id.indicator1);
        indicator2 = findViewById(R.id.indicator2);
        indicator3 = findViewById(R.id.indicator3);

        // Set welcome text
        appNameTextView.setText(R.string.welcome_title);
        welcomeTextView.setText(R.string.welcome_subtitle);
        getStartedButton.setText(R.string.get_started);

        // Setup educational slides
        setupEducationalSlides();

        // Navigate to LoginActivity when button is clicked
        getStartedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void setupEducationalSlides() {
        // Create educational slides
        slides = new ArrayList<>();
        slides.add(new EducationalSlide(
                R.drawable.ic_search,
                "Find the Best Nursery",
                "Search and compare nurseries to find the perfect one for your child"
        ));
        slides.add(new EducationalSlide(
                R.drawable.ic_nursery,
                "View Details & Reviews",
                "See detailed information, photos, and reviews from other parents"
        ));
        slides.add(new EducationalSlide(
                R.drawable.ic_booking,
                "Book & Pay Easily",
                "Reserve your child's spot and pay securely online"
        ));

        // Setup adapter
        EducationalSlideAdapter adapter = new EducationalSlideAdapter(slides);
        educationalViewPager.setAdapter(adapter);

        // Setup page change callback for indicators
        educationalViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                updateIndicators(position);
            }
        });

        // Auto-scroll slides
        startAutoScroll();
    }

    private void updateIndicators(int position) {
        int activeColor = getResources().getColor(R.color.primary_blue);
        int inactiveColor = getResources().getColor(R.color.gray_medium);

        indicator1.setBackgroundTintList(android.content.res.ColorStateList.valueOf(
                position == 0 ? activeColor : inactiveColor
        ));
        indicator2.setBackgroundTintList(android.content.res.ColorStateList.valueOf(
                position == 1 ? activeColor : inactiveColor
        ));
        indicator3.setBackgroundTintList(android.content.res.ColorStateList.valueOf(
                position == 2 ? activeColor : inactiveColor
        ));
    }

    private void startAutoScroll() {
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            int currentPage = 0;

            @Override
            public void run() {
                if (currentPage == slides.size()) {
                    currentPage = 0;
                }
                educationalViewPager.setCurrentItem(currentPage++, true);
                handler.postDelayed(this, 3000); // Auto-scroll every 3 seconds
            }
        };
        handler.postDelayed(runnable, 3000);
    }
}


