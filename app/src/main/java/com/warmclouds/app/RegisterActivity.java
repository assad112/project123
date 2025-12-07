package com.warmclouds.app;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.warmclouds.app.models.User;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";

    private Spinner userTypeSpinner;
    private TextInputLayout nameInputLayout, emailInputLayout, phoneInputLayout, 
            passwordInputLayout, confirmPasswordInputLayout;
    private EditText nameEditText, emailEditText, phoneEditText, 
            passwordEditText, confirmPasswordEditText;
    private MaterialButton registerButton;
    private TextView loginLinkTextView;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private String selectedUserType = "parent"; // Default

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        initializeViews();
        setupSpinner();
        setupButtons();
    }

    private void initializeViews() {
        userTypeSpinner = findViewById(R.id.userTypeSpinner);
        nameInputLayout = findViewById(R.id.nameInputLayout);
        emailInputLayout = findViewById(R.id.emailInputLayout);
        phoneInputLayout = findViewById(R.id.phoneInputLayout);
        passwordInputLayout = findViewById(R.id.passwordInputLayout);
        confirmPasswordInputLayout = findViewById(R.id.confirmPasswordInputLayout);
        
        nameEditText = findViewById(R.id.nameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        
        registerButton = findViewById(R.id.registerButton);
        loginLinkTextView = findViewById(R.id.loginLinkTextView);
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

        // Set default selection to parent (index 0)
        userTypeSpinner.setSelection(0);

        // Handle selection
        userTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] userTypes = getResources().getStringArray(R.array.user_types);
                String selected = userTypes[position];
                
                if (selected.equals("Parent")) {
                    selectedUserType = "parent";
                    phoneInputLayout.setVisibility(View.VISIBLE);
                } else if (selected.equals("Admin")) {
                    selectedUserType = "admin";
                    phoneInputLayout.setVisibility(View.GONE);
                }
                
                Log.d(TAG, "Selected user type: " + selectedUserType);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedUserType = "parent";
            }
        });
    }

    private void setupButtons() {
        registerButton.setOnClickListener(v -> performRegister());
        loginLinkTextView.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void performRegister() {
        String name = nameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();

        // Validation
        if (TextUtils.isEmpty(name)) {
            nameInputLayout.setError("Please enter full name");
            return;
        } else {
            nameInputLayout.setError(null);
        }

        if (TextUtils.isEmpty(email)) {
            emailInputLayout.setError("Please enter email");
            return;
        } else if (!isValidEmail(email)) {
            emailInputLayout.setError("Invalid email address");
            return;
        } else {
            emailInputLayout.setError(null);
        }

        // Phone validation for parents
        final String phone;
        if ("parent".equals(selectedUserType)) {
            String phoneText = phoneEditText.getText().toString().trim();
            if (TextUtils.isEmpty(phoneText)) {
                phoneInputLayout.setError("Please enter phone number");
                return;
            } else {
                phoneInputLayout.setError(null);
            }
            phone = phoneText;
        } else {
            phone = "";
        }

        if (TextUtils.isEmpty(password)) {
            passwordInputLayout.setError("Please enter password");
            return;
        } else if (password.length() < 6) {
            passwordInputLayout.setError("Password must be at least 6 characters");
            return;
        } else {
            passwordInputLayout.setError(null);
        }

        if (TextUtils.isEmpty(confirmPassword)) {
            confirmPasswordInputLayout.setError("Please confirm password");
            return;
        } else if (!password.equals(confirmPassword)) {
            confirmPasswordInputLayout.setError("Passwords do not match");
            return;
        } else {
            confirmPasswordInputLayout.setError(null);
        }

        registerButton.setEnabled(false);
        registerButton.setText("Creating account...");

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        registerButton.setEnabled(true);
                        registerButton.setText("Create Account");

                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            if (firebaseUser != null) {
                                // Create user document in Firestore
                                User user = new User(firebaseUser.getUid(), email, name, selectedUserType);
                                user.setPhone(phone);
                                
                                Log.d(TAG, "Registering user with role: " + selectedUserType);

                                db.collection("users").document(firebaseUser.getUid())
                                        .set(user)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Log.d(TAG, "User saved with role: " + selectedUserType);
                                                    showSuccess("Account created successfully");
                                                    // Navigate to appropriate screen
                                                    navigateToAppropriateScreen(selectedUserType);
                                                } else {
                                                    showError("Account created but error saving data");
                                                }
                                            }
                                        });
                            }
                        } else {
                            String errorMessage = task.getException() != null ?
                                    task.getException().getMessage() : "Account creation failed";
                            showError("Account creation failed: " + errorMessage);
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

        if (role != null && "admin".equals(role.trim())) {
            Log.d(TAG, "Navigating to AdminActivity");
            intent = new Intent(RegisterActivity.this, AdminActivity.class);
        } else {
            Log.d(TAG, "Navigating to ParentDashboardActivity");
            intent = new Intent(RegisterActivity.this, ParentDashboardActivity.class);
        }
        startActivity(intent);
        finish();
    }
}

