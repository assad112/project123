package com.warmclouds.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.warmclouds.app.adapters.AdminPagerAdapter;
import com.warmclouds.app.fragments.BookingsFragment;
import com.warmclouds.app.fragments.NurseriesFragment;
import com.warmclouds.app.utils.SampleDataGenerator;

import androidx.viewpager2.widget.ViewPager2;

public class AdminActivity extends AppCompatActivity {

    private MaterialButton logoutButton;
    private MaterialButton addSampleDataButton;
    private ProgressBar progressBar;
    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private AdminPagerAdapter pagerAdapter;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        mAuth = FirebaseAuth.getInstance();

        initializeViews();
        setupViewPager();
    }

    private void initializeViews() {
        logoutButton = findViewById(R.id.logoutButton);
        addSampleDataButton = findViewById(R.id.addSampleDataButton);
        progressBar = findViewById(R.id.progressBar);
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);

        logoutButton.setOnClickListener(v -> logout());
        addSampleDataButton.setOnClickListener(v -> showAddSampleDataDialog());
    }

    private void setupViewPager() {
        pagerAdapter = new AdminPagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Nurseries");
                    tab.setIcon(R.drawable.ic_nursery_empty);
                    break;
                case 1:
                    tab.setText("Bookings");
                    tab.setIcon(R.drawable.ic_booking);
                    break;
            }
        }).attach();
    }

    private void logout() {
        mAuth.signOut();
        Intent intent = new Intent(AdminActivity.this, WelcomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void showAddSampleDataDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Add Sample Nurseries")
                .setMessage("This will add 5 sample nurseries with images and complete data to Firebase. Continue?")
                .setPositiveButton("Add", (dialog, which) -> addSampleData())
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void addSampleData() {
        progressBar.setVisibility(View.VISIBLE);
        addSampleDataButton.setEnabled(false);

        SampleDataGenerator.addSampleNurseries(new SampleDataGenerator.OnDataAddedListener() {
            @Override
            public void onSuccess(int count) {
                progressBar.setVisibility(View.GONE);
                addSampleDataButton.setEnabled(true);
                
                new AlertDialog.Builder(AdminActivity.this)
                        .setTitle("Success!")
                        .setMessage(count + " nurseries added successfully! Go to Nurseries tab to see them.")
                        .setPositiveButton("OK", (dialog, which) -> {
                            // Refresh the nurseries list
                            if (pagerAdapter != null) {
                                viewPager.setAdapter(pagerAdapter);
                            }
                        })
                        .show();
            }

            @Override
            public void onError(String error) {
                progressBar.setVisibility(View.GONE);
                addSampleDataButton.setEnabled(true);
                
                new AlertDialog.Builder(AdminActivity.this)
                        .setTitle("Error")
                        .setMessage("Failed to add nurseries: " + error)
                        .setPositiveButton("OK", null)
                        .show();
            }

            @Override
            public void onProgress(int current, int total) {
                runOnUiThread(() -> 
                    Toast.makeText(AdminActivity.this, 
                        "Adding nurseries: " + current + "/" + total, 
                        Toast.LENGTH_SHORT).show()
                );
            }
        });
    }
}
