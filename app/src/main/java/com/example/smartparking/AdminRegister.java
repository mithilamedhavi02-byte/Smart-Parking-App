package com.example.smartparking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;
import java.util.regex.Pattern;
public class AdminRegister extends AppCompatActivity {


    // Input fields
    private EditText fullNameInput, nicInput, phoneInput, emailInput, passwordInput, confirmPasswordInput;

    // Error messages
    private TextView fullNameError, nicError, phoneError, emailError, passwordError, confirmPasswordError;

    // Card views
    private CardView fullNameCard, nicCard, phoneCard, emailCard, passwordCard, confirmPasswordCard;

    // Buttons and links
    private Button registerButton;
    private TextView loginLink, loginLinkText;

    // Progress bar
    private ProgressBar progressBar;

    // Firebase
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_register);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Initialize views
        initializeViews();

        // Setup text change listeners
        setupTextChangeListeners();

        // Setup click listeners
        setupClickListeners();

        // Check if user is already logged in
        checkCurrentUser();
    }

    private void initializeViews() {
        // Input fields
        fullNameInput = findViewById(R.id.fullNameInput);
        nicInput = findViewById(R.id.nicInput);
        phoneInput = findViewById(R.id.phoneInput);
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        confirmPasswordInput = findViewById(R.id.confirmPasswordInput);

        // Error messages
        fullNameError = findViewById(R.id.fullNameError);
        nicError = findViewById(R.id.nicError);
        phoneError = findViewById(R.id.phoneError);
        emailError = findViewById(R.id.emailError);
        passwordError = findViewById(R.id.passwordError);
        confirmPasswordError = findViewById(R.id.confirmPasswordError);

        // Card views
        fullNameCard = findViewById(R.id.fullNameCard);
        nicCard = findViewById(R.id.nicCard);
        phoneCard = findViewById(R.id.phoneCard);
        emailCard = findViewById(R.id.emailCard);
        passwordCard = findViewById(R.id.passwordCard);
        confirmPasswordCard = findViewById(R.id.confirmPasswordCard);

        // Buttons and links
        registerButton = findViewById(R.id.registerButton);
        loginLink = findViewById(R.id.loginLink);
        loginLinkText = findViewById(R.id.loginLinkText);

        // Progress bar (XML එකට ProgressBar එකක් add කරන්න අවශ්‍යනම්)


        // XML එකේ ProgressBar නැත්තම් එක create කරන්න
        if (progressBar == null) {
            progressBar = new ProgressBar(this);
        }
    }

    private void checkCurrentUser() {
        FirebaseAdmin currentUser = mAuth.getCurrentAdmin();
        if (currentUser != null) {
            Intent intent = new Intent(AdminRegister.this, AdminLogin.class);
            startActivity(intent);
            finish();
        }
    }

    private void setupTextChangeListeners() {
        // Full Name validation
        fullNameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateFullName(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // NIC validation
        nicInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateNIC(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Phone number validation
        phoneInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validatePhoneNumber(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Email validation
        emailInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateEmail(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Password validation
        passwordInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validatePassword(s.toString());
                validateConfirmPassword();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Confirm password validation
        confirmPasswordInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateConfirmPassword();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void setupClickListeners() {
        // Register button click
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();

                if (validateAllFields()) {
                    registerUserWithFirebase();
                } else {
                    Toast.makeText(AdminRegister.this, "Please fix all errors before registering", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Login link click
        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToLogin();
            }
        });

        loginLinkText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToLogin();
            }
        });
    }

    // Validation methods
    private boolean validateFullName(String fullName) {
        if (fullName.isEmpty()) {
            showError(fullNameError, "Full name is required");
            setCardError(fullNameCard, true);
            return false;
        } else if (fullName.trim().length() < 3) {
            showError(fullNameError, "Name must be at least 3 characters");
            setCardError(fullNameCard, true);
            return false;
        } else {
            hideError(fullNameError);
            setCardError(fullNameCard, false);
            return true;
        }
    }

    private boolean validateNIC(String nic) {
        String oldNicPattern = "^[0-9]{9}[Vv]$";
        String newNicPattern = "^[0-9]{12}$";

        if (nic.isEmpty()) {
            showError(nicError, "NIC is required");
            setCardError(nicCard, true);
            return false;
        } else if (!Pattern.matches(oldNicPattern, nic.trim()) && !Pattern.matches(newNicPattern, nic.trim())) {
            showError(nicError, "Please enter a valid NIC number");
            setCardError(nicCard, true);
            return false;
        } else {
            hideError(nicError);
            setCardError(nicCard, false);
            return true;
        }
    }

    private boolean validatePhoneNumber(String phone) {
        String phonePattern = "^(\\+94\\s?7\\d{1}\\s?\\d{3}\\s?\\d{4}|0\\d{9})$";

        if (phone.isEmpty()) {
            showError(phoneError, "Phone number is required");
            setCardError(phoneCard, true);
            return false;
        } else if (!Pattern.matches(phonePattern, phone.trim())) {
            showError(phoneError, "Please enter a valid Sri Lankan phone number");
            setCardError(phoneCard, true);
            return false;
        } else {
            hideError(phoneError);
            setCardError(phoneCard, false);
            return true;
        }
    }

    private boolean validateEmail(String email) {
        if (email.isEmpty()) {
            showError(emailError, "Email address is required");
            setCardError(emailCard, true);
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()) {
            showError(emailError, "Please enter a valid email address");
            setCardError(emailCard, true);
            return false;
        } else {
            hideError(emailError);
            setCardError(emailCard, false);
            return true;
        }
    }

    private boolean validatePassword(String password) {
        if (password.isEmpty()) {
            showError(passwordError, "Password is required");
            setCardError(passwordCard, true);
            return false;
        } else if (password.length() < 8) {
            showError(passwordError, "Password must be at least 8 characters");
            setCardError(passwordCard, true);
            return false;
        } else {
            hideError(passwordError);
            setCardError(passwordCard, false);
            return true;
        }
    }

    private boolean validateConfirmPassword() {
        String password = passwordInput.getText().toString();
        String confirmPassword = confirmPasswordInput.getText().toString();

        if (confirmPassword.isEmpty()) {
            showError(confirmPasswordError, "Please confirm your password");
            setCardError(confirmPasswordCard, true);
            return false;
        } else if (!password.equals(confirmPassword)) {
            showError(confirmPasswordError, "Passwords do not match");
            setCardError(confirmPasswordCard, true);
            return false;
        } else {
            hideError(confirmPasswordError);
            setCardError(confirmPasswordCard, false);
            return true;
        }
    }

    private boolean validateAllFields() {
        boolean allValid = true;

        allValid = validateFullName(fullNameInput.getText().toString()) && allValid;
        allValid = validateNIC(nicInput.getText().toString()) && allValid;
        allValid = validatePhoneNumber(phoneInput.getText().toString()) && allValid;
        allValid = validateEmail(emailInput.getText().toString()) && allValid;
        allValid = validatePassword(passwordInput.getText().toString()) && allValid;
        allValid = validateConfirmPassword() && allValid;

        return allValid;
    }

    // Helper methods
    private void showError(TextView errorView, String message) {
        errorView.setText(message);
        errorView.setVisibility(View.VISIBLE);
    }

    private void hideError(TextView errorView) {
        errorView.setVisibility(View.GONE);
    }

    private void setCardError(CardView cardView, boolean hasError) {
        if (hasError) {
            cardView.setCardBackgroundColor(0x40FF6B6B); // Light red
        } else {
            cardView.setCardBackgroundColor(0x80FFFFFF); // Original white
        }
    }

    // Firebase Registration Method
    private void registerUserWithFirebase() {
        // Get input values
        final String fullName = fullNameInput.getText().toString().trim();
        final String nic = nicInput.getText().toString().trim().toUpperCase();
        final String phone = phoneInput.getText().toString().trim();
        final String email = emailInput.getText().toString().trim();
        final String password = passwordInput.getText().toString();

        // Show progress bar
        showProgress(true);

        // Create user with Firebase Authentication
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Registration successful
                            FirebaseAdmin user = mAuth.getCurrentAdmin();

                            if (user != null) {
                                // Save additional user data to Realtime Database
                                saveUserToDatabase(user.getUid(), fullName, nic, phone, email);
                            }
                        } else {
                            // Registration failed
                            showProgress(false);
                            String errorMessage = "Registration failed";
                            if (task.getException() != null) {
                                errorMessage += ": " + task.getException().getMessage();
                            }
                            Toast.makeText(AdminRegister.this, errorMessage, Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void saveUserToDatabase(String userId, String fullName, String nic, String phone, String email) {
        // Create user object
        HashMap<String, Object> userData = new HashMap<>();
        userData.put("userId", userId);
        userData.put("fullName", fullName);
        userData.put("nic", nic);
        userData.put("phone", phone);
        userData.put("email", email);
        userData.put("userType", "user");
        userData.put("createdAt", System.currentTimeMillis());
        userData.put("isActive", true);

        // Save to Firebase Realtime Database
        mDatabase.child("users").child(userId).setValue(userData)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        showProgress(false);

                        if (task.isSuccessful()) {
                            // Successfully saved to database
                            Toast.makeText(RegisterAdmin.this,
                                    "Registration successful! Welcome " + fullName,
                                    Toast.LENGTH_LONG).show();

                            // Save to SharedPreferences
                            saveToSharedPreferences(userId, fullName, email, "user");

                            // Navigate to dashboard
                            Intent intent = new Intent(AdminRegister.this, AdminLogin.class);
                            startActivity(intent);
                            finish();

                        } else {
                            // Failed to save to database
                            Toast.makeText(AdminRegister.this,
                                    "Data save failed: " +
                                            (task.getException() != null ? task.getException().getMessage() : ""),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void saveToSharedPreferences(String userId, String fullName, String email, String userType) {
        android.content.SharedPreferences sharedPreferences =
                getSharedPreferences("UserPrefs", MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("ADMIN_ID", userId);
        editor.putString("FULL_NAME", fullName);
        editor.putString("EMAIL", email);
        editor.putString("USER_TYPE", userType);
        editor.putBoolean("IS_LOGGED_IN", true);

        editor.apply();
    }

    private void showProgress(boolean show) {
        if (show) {
            registerButton.setEnabled(false);
            registerButton.setText("Registering...");
            if (progressBar.getVisibility() != View.VISIBLE) {
                progressBar.setVisibility(View.VISIBLE);
            }
        } else {
            registerButton.setEnabled(true);
            registerButton.setText("Register");
            if (progressBar.getVisibility() != View.GONE) {
                progressBar.setVisibility(View.GONE);
            }
        }
    }

    private void hideKeyboard() {
        android.view.inputmethod.InputMethodManager imm =
                (android.view.inputmethod.InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        View view = getCurrentFocus();
        if (view != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void navigateToLogin() {
        try {
            Intent intent = new Intent(AdminRegister.this, AdminLogin.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        } catch (Exception e) {
            Toast.makeText(this, "Login screen not available", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}