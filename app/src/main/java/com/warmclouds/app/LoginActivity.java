package com.warmclouds.app;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.warmclouds.app.models.User;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private Spinner userTypeSpinner;
    private TextInputLayout emailInputLayout, passwordInputLayout;
    private EditText emailEditText, passwordEditText;
    private MaterialButton loginButton;
    private TextView registerLinkTextView;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        initializeViews();
        setupSpinner();
        setupButtons();
    }

    private void initializeViews() {
        userTypeSpinner = findViewById(R.id.userTypeSpinner);
        emailInputLayout = findViewById(R.id.emailInputLayout);
        passwordInputLayout = findViewById(R.id.passwordInputLayout);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        registerLinkTextView = findViewById(R.id.registerLinkTextView);
    }

    private void setupSpinner() {
        // Create adapter with user types
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.user_types,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userTypeSpinner.setAdapter(adapter);
        userTypeSpinner.setSelection(0); // Default to parent
    }

    private void setupButtons() {
        loginButton.setOnClickListener(v -> performLogin());
        registerLinkTextView.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void performLogin() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            showError("Please enter email and password");
            return;
        }

        if (!isValidEmail(email)) {
            emailInputLayout.setError("Invalid email address");
            return;
        } else {
            emailInputLayout.setError(null);
        }

        loginButton.setEnabled(false);
        loginButton.setText("Logging in...");

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        loginButton.setEnabled(true);
                        loginButton.setText(R.string.login);

                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            if (firebaseUser != null) {
                                // Read role from Firestore (single source of truth)
                                checkUserRoleAndNavigate(firebaseUser.getUid());
                            }
                        } else {
                            String errorMessage = task.getException() != null ?
                                    task.getException().getMessage() : "Login failed";
                            showError("Login failed: " + errorMessage);
                        }
                    }
                });
    }


    private void checkUserRoleAndNavigate(String userId) {
        db.collection("users").document(userId).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document != null && document.exists()) {
                                User user = document.toObject(User.class);
                                if (user != null && user.getRole() != null) {
                                    // Use role from Firestore (single source of truth)
                                    String actualRole = user.getRole().trim();
                                    Log.d(TAG, "User role from Firestore: " + actualRole);
                                    showSuccess("Login successful");
                                    navigateToAppropriateScreen(actualRole);
                                } else {
                                    // User exists but role is null, default to parent
                                    Log.w(TAG, "User exists but role is null, defaulting to parent");
                                    String defaultRole = "parent";
                                    // Update user with default role
                                    if (user == null) {
                                        createUserDocument(userId, defaultRole);
                                    } else {
                                        user.setRole(defaultRole);
                                        db.collection("users").document(userId).set(user)
                                                .addOnCompleteListener(task1 -> {
                                                    showSuccess("Login successful");
                                                    navigateToAppropriateScreen(defaultRole);
                                                });
                                    }
                                }
                            } else {
                                // User document doesn't exist, create it with default role (parent)
                                Log.w(TAG, "User document doesn't exist, creating with default role: parent");
                                createUserDocument(userId, "parent");
                            }
                        } else {
                            // Error accessing Firestore
                            Exception exception = task.getException();
                            if (exception != null) {
                                Log.e(TAG, "Error getting user: " + exception.getMessage());
                            }
                            showError("Error reading user data");
                        }
                    }
                });
    }

    private void createUserDocument(String userId, String role) {
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser == null) {
            showError("Error: User not found");
            return;
        }

        String email = firebaseUser.getEmail();
        String name = email != null && email.contains("@") ? email.split("@")[0] : "User";
        
        User user = new User(userId, email, name, role);
        Log.d(TAG, "Creating user document with role: " + role);
        
        db.collection("users").document(userId).set(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User document created successfully with role: " + role);
                            showSuccess("Login successful");
                            navigateToAppropriateScreen(role);
                        } else {
                            Exception exception = task.getException();
                            Log.e(TAG, "Failed to create user document: " + 
                                    (exception != null ? exception.getMessage() : "Unknown error"));
                            showError("Failed to save user data: " + 
                                    (exception != null ? exception.getMessage() : "Unknown error"));
                            // Navigate anyway since authentication was successful
                            navigateToAppropriateScreen(role);
                        }
                    }
                });
    }

    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void showSuccess(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void navigateToAppropriateScreen(String role) {
        Intent intent;
        Log.d(TAG, "Navigating with role: " + role);
        
        // Always check role from Firestore, default to parent if role is null or unknown
        if (role != null && "admin".equals(role.trim())) {
            Log.d(TAG, "Navigating to AdminActivity");
            intent = new Intent(LoginActivity.this, AdminActivity.class);
        } else {
            // Default to parent dashboard for any other role or null
            Log.d(TAG, "Navigating to ParentDashboardActivity");
            intent = new Intent(LoginActivity.this, ParentDashboardActivity.class);
        }
        startActivity(intent);
        finish();
    }
}
