package com.warmclouds.app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class WelcomeActivity extends AppCompatActivity {

    private ImageView logoImageView;
    private TextView appNameTextView;
    private TextView welcomeTextView;
    private Button getStartedButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        // Initialize views
        logoImageView = findViewById(R.id.logoImageView);
        appNameTextView = findViewById(R.id.appNameTextView);
        welcomeTextView = findViewById(R.id.welcomeTextView);
        getStartedButton = findViewById(R.id.getStartedButton);

        // Set welcome text
        appNameTextView.setText(R.string.welcome_title);
        welcomeTextView.setText(R.string.welcome_subtitle);
        getStartedButton.setText(R.string.get_started);

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
}


