package com.example.sp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sp.utils.FirebaseDatabaseHelper;

import java.util.regex.Pattern;

public class AdminRegister extends AppCompatActivity {

    private EditText fullNameInput, nicInput, phoneInput, emailInput, passwordInput, confirmPasswordInput;
    private TextView fullNameError, nicError, phoneError, emailError, passwordError, confirmPasswordError;
    private Button registerButton;
    private TextView loginLink;
    private ProgressBar progressBar;

    private FirebaseDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_register);

        dbHelper = new FirebaseDatabaseHelper();

        initializeViews();
        setupTextChangeListeners();
        setupButtonClickListeners();
    }

    private void initializeViews() {
        fullNameInput = findViewById(R.id.fullNameInput);
        nicInput = findViewById(R.id.nicInput);
        phoneInput = findViewById(R.id.phoneInput);
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        confirmPasswordInput = findViewById(R.id.confirmPasswordInput);

        fullNameError = findViewById(R.id.fullNameError);
        nicError = findViewById(R.id.nicError);
        phoneError = findViewById(R.id.phoneError);
        emailError = findViewById(R.id.emailError);
        passwordError = findViewById(R.id.passwordError);
        confirmPasswordError = findViewById(R.id.confirmPasswordError);

        registerButton = findViewById(R.id.registerButton);
        loginLink = findViewById(R.id.loginLink);
        progressBar = findViewById(R.id.progressBar);
        if (progressBar != null) progressBar.setVisibility(View.GONE);
    }

    private void setupTextChangeListeners() {
        fullNameInput.addTextChangedListener(createTextWatcher(this::validateFullName));
        nicInput.addTextChangedListener(createTextWatcher(this::validateNIC));
        phoneInput.addTextChangedListener(createTextWatcher(this::validatePhone));
        emailInput.addTextChangedListener(createTextWatcher(() -> {
            validateEmail();
            String email = emailInput.getText().toString().trim();
            if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                checkEmailExists(email);
            }
        }));
        passwordInput.addTextChangedListener(createTextWatcher(() -> {
            validatePassword();
            validateConfirmPassword();
        }));
        confirmPasswordInput.addTextChangedListener(createTextWatcher(this::validateConfirmPassword));
    }

    private TextWatcher createTextWatcher(Runnable afterTextChangedAction) {
        return new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                afterTextChangedAction.run();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        };
    }

    private void setupButtonClickListeners() {
        registerButton.setOnClickListener(v -> {
            Log.d("AdminRegister", "Register button clicked");
            Toast.makeText(AdminRegister.this, "Processing registration...", Toast.LENGTH_SHORT).show();

            if (validateAllFields()) {
                registerAdminToFirebase();
            } else {
                Toast.makeText(AdminRegister.this, "Please fix all errors", Toast.LENGTH_SHORT).show();
            }
        });

        loginLink.setOnClickListener(v -> {
            startActivity(new Intent(AdminRegister.this, AdminLogin.class));
        });
    }

    private boolean validateFullName() {
        String name = fullNameInput.getText().toString().trim();
        if (name.length() < 3) {
            fullNameError.setVisibility(View.VISIBLE);
            fullNameInput.setError("Name must be at least 3 characters");
            return false;
        } else {
            fullNameError.setVisibility(View.GONE);
            fullNameInput.setError(null);
            return true;
        }
    }

    private boolean validateNIC() {
        String nic = nicInput.getText().toString().trim();
        Pattern nicPattern = Pattern.compile("^([0-9]{9}[xXvV]|[0-9]{12})$");
        if (!nicPattern.matcher(nic).matches()) {
            nicError.setVisibility(View.VISIBLE);
            nicInput.setError("Invalid NIC format");
            return false;
        } else {
            nicError.setVisibility(View.GONE);
            nicInput.setError(null);
            return true;
        }
    }

    private boolean validatePhone() {
        String phone = phoneInput.getText().toString().trim();
        Pattern phonePattern = Pattern.compile("^(\\+94|0)(7[0-9])([0-9]{7})$");
        if (!phonePattern.matcher(phone).matches()) {
            phoneError.setVisibility(View.VISIBLE);
            phoneInput.setError("Invalid phone number");
            return false;
        } else {
            phoneError.setVisibility(View.GONE);
            phoneInput.setError(null);
            return true;
        }
    }

    private boolean validateEmail() {
        String email = emailInput.getText().toString().trim();
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailError.setVisibility(View.VISIBLE);
            emailInput.setError("Invalid email address");
            return false;
        } else {
            emailError.setVisibility(View.GONE);
            emailInput.setError(null);
            return true;
        }
    }

    private void checkEmailExists(String email) {
        dbHelper.checkEmailExists(email, exists -> runOnUiThread(() -> {
            if (exists) {
                emailInput.setError("Email already registered");
                emailError.setVisibility(View.VISIBLE);
            } else {
                emailInput.setError(null);
                emailError.setVisibility(View.GONE);
            }
        }));
    }

    private boolean validatePassword() {
        String password = passwordInput.getText().toString();
        if (password.length() < 8) {
            passwordError.setVisibility(View.VISIBLE);
            passwordInput.setError("Password must be at least 8 characters");
            return false;
        } else {
            passwordError.setVisibility(View.GONE);
            passwordInput.setError(null);
            return true;
        }
    }

    private boolean validateConfirmPassword() {
        String password = passwordInput.getText().toString();
        String confirmPassword = confirmPasswordInput.getText().toString();
        if (!password.equals(confirmPassword) || confirmPassword.isEmpty()) {
            confirmPasswordError.setVisibility(View.VISIBLE);
            confirmPasswordInput.setError("Passwords do not match");
            return false;
        } else {
            confirmPasswordError.setVisibility(View.GONE);
            confirmPasswordInput.setError(null);
            return true;
        }
    }

    private boolean validateAllFields() {
        boolean isValid = validateFullName() && validateNIC() && validatePhone() &&
                validateEmail() && validatePassword() && validateConfirmPassword();

        Log.d("AdminRegister", "All fields validation result: " + isValid);
        return isValid;
    }

    private void registerAdminToFirebase() {
        String fullName = fullNameInput.getText().toString().trim();
        String nic = nicInput.getText().toString().trim();
        String phone = phoneInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString();

        Log.d("AdminRegister", "Starting registration with: " + email);

        showProgress(true);
        registerButton.setEnabled(false);

        dbHelper.saveUserMap(fullName, nic, phone, email, password, "admin",
                new FirebaseDatabaseHelper.DatabaseCallback() {
                    @Override
                    public void onSuccess(String userId) {
                        runOnUiThread(() -> {
                            Log.d("AdminRegister", "Registration successful, user ID: " + userId);

                            showProgress(false);
                            registerButton.setEnabled(true);

                            // Save NIC & Password for auto-fill
                            SharedPreferences prefs = getSharedPreferences("AdminPrefs", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString("NIC", nic);
                            editor.putString("Password", password);
                            editor.apply();

                            Toast.makeText(AdminRegister.this,
                                    "Admin Registered! Redirecting to Login...",
                                    Toast.LENGTH_SHORT).show();

                            // Directly navigate to AdminLogin after successful registration
                            Intent intent = new Intent(AdminRegister.this, AdminLogin.class);
                            startActivity(intent);
                            finish();
                        });
                    }

                    @Override
                    public void onError(String errorMessage) {
                        runOnUiThread(() -> {
                            Log.e("AdminRegister", "Registration failed: " + errorMessage);

                            showProgress(false);
                            registerButton.setEnabled(true);
                            Toast.makeText(AdminRegister.this,
                                    "Registration Failed: " + errorMessage,
                                    Toast.LENGTH_LONG).show();
                        });
                    }
                });
    }

    private void showProgress(boolean show) {
        if (progressBar != null) {
            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }
}