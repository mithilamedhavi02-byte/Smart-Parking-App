package com.example.sp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.regex.Pattern;

public class DriverRegister extends AppCompatActivity {

    // Declare UI components
    private EditText editTextFullName;
    private EditText editTextNIC;
    private EditText editTextPhone;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextConfirmPassword;

    private Button buttonRegister;
    private TextView textView10; // Login link

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_register); // Make sure layout file name matches

        initializeViews();
        setupTextChangeListeners();
        setupButtonClickListeners();
    }

    private void initializeViews() {
        // Initialize EditTexts
        editTextFullName = findViewById(R.id.editTextFullName);
        editTextNIC = findViewById(R.id.editTextNIC);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);

        // Initialize Buttons and Links
        buttonRegister = findViewById(R.id.buttonRegister);
        textView10 = findViewById(R.id.textView10);
    }

    private void setupTextChangeListeners() {
        // Real-time validation listeners
        editTextFullName.addTextChangedListener(new TextWatcher() {
            @Override public void afterTextChanged(Editable s) { validateFullName(); }
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        editTextNIC.addTextChangedListener(new TextWatcher() {
            @Override public void afterTextChanged(Editable s) { validateNIC(); }
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        editTextPhone.addTextChangedListener(new TextWatcher() {
            @Override public void afterTextChanged(Editable s) { validatePhone(); }
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        editTextEmail.addTextChangedListener(new TextWatcher() {
            @Override public void afterTextChanged(Editable s) { validateEmail(); }
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        editTextPassword.addTextChangedListener(new TextWatcher() {
            @Override public void afterTextChanged(Editable s) {
                validatePassword();
                validateConfirmPassword();
            }
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        editTextConfirmPassword.addTextChangedListener(new TextWatcher() {
            @Override public void afterTextChanged(Editable s) { validateConfirmPassword(); }
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });
    }

    private void setupButtonClickListeners() {
        // Register Button Click Listener
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateAllFields()) {
                    registerUser();
                } else {
                    Toast.makeText(DriverRegister.this,
                            "Please fix all errors before registering",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Login Link Click Listener
        textView10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Login Activity
                navigateToLogin();
            }
        });
    }

    // Validation Functions
    private boolean validateFullName() {
        String name = editTextFullName.getText().toString().trim();
        if (name.length() < 3) {
            showError(editTextFullName, "Name must be at least 3 characters");
            return false;
        } else {
            clearError(editTextFullName);
            return true;
        }
    }

    private boolean validateNIC() {
        String nic = editTextNIC.getText().toString().trim();
        Pattern nicPattern = Pattern.compile("^([0-9]{9}[xXvV]|[0-9]{12})$");
        if (!nicPattern.matcher(nic).matches()) {
            showError(editTextNIC, "Please enter a valid NIC number");
            return false;
        } else {
            clearError(editTextNIC);
            return true;
        }
    }

    private boolean validatePhone() {
        String phone = editTextPhone.getText().toString().trim();
        Pattern phonePattern = Pattern.compile("^(\\+94|0)(7[0-9])([0-9]{7})$");
        if (!phonePattern.matcher(phone).matches()) {
            showError(editTextPhone, "Please enter a valid Sri Lankan phone number");
            return false;
        } else {
            clearError(editTextPhone);
            return true;
        }
    }

    private boolean validateEmail() {
        String email = editTextEmail.getText().toString().trim();
        Pattern emailPattern = Pattern.compile(
                "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$"
        );
        if (!emailPattern.matcher(email).matches()) {
            showError(editTextEmail, "Please enter a valid email address");
            return false;
        } else {
            clearError(editTextEmail);
            return true;
        }
    }

    private boolean validatePassword() {
        String password = editTextPassword.getText().toString();
        if (password.length() < 8) {
            showError(editTextPassword, "Password must be at least 8 characters");
            return false;
        } else {
            clearError(editTextPassword);
            return true;
        }
    }

    private boolean validateConfirmPassword() {
        String password = editTextPassword.getText().toString();
        String confirmPassword = editTextConfirmPassword.getText().toString();
        if (!password.equals(confirmPassword)) {
            showError(editTextConfirmPassword, "Passwords do not match");
            return false;
        } else {
            clearError(editTextConfirmPassword);
            return true;
        }
    }

    private boolean validateAllFields() {
        return validateFullName() && validateNIC() && validatePhone() &&
                validateEmail() && validatePassword() && validateConfirmPassword();
    }

    private void registerUser() {
        // Get all values
        String fullName = editTextFullName.getText().toString().trim();
        String nic = editTextNIC.getText().toString().trim();
        String phone = editTextPhone.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString();

        // Save user data (you can use SharedPreferences, Database, or API)
        saveUserData(fullName, nic, phone, email, password);

        // Show success message
        Toast.makeText(this,
                "Registration successful! Welcome " + fullName,
                Toast.LENGTH_SHORT).show();

        // Navigate to Login page
        navigateToLogin();
    }

    private void saveUserData(String fullName, String nic, String phone,
                              String email, String password) {
        // Example using SharedPreferences
        // Note: In production, never store passwords in plain text!
        // Use encryption or better yet, use a backend API

        /*
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("fullName", fullName);
        editor.putString("nic", nic);
        editor.putString("phone", phone);
        editor.putString("email", email);
        editor.putString("password", password);
        editor.putBoolean("isRegistered", true);
        editor.apply();
        */

        // For now, just show what would be saved
        System.out.println("User Data to Save:");
        System.out.println("Full Name: " + fullName);
        System.out.println("NIC: " + nic);
        System.out.println("Phone: " + phone);
        System.out.println("Email: " + email);
        System.out.println("Password: " + password);
    }

    private void navigateToLogin() {
        // Navigate to Login Activity
        // If you have a LoginActivity, uncomment this:
        /*
        Intent intent = new Intent(RegisterUser.this, LoginActivity.class);
        startActivity(intent);
        finish(); // Optional: close current activity
        */

        // For demo, just show a toast
        Toast.makeText(this,
                "Navigate to Login Page",
                Toast.LENGTH_SHORT).show();
    }

    // Helper methods for showing/hiding errors
    private void showError(EditText editText, String errorMessage) {
        editText.setError(errorMessage);
    }

    private void clearError(EditText editText) {
        editText.setError(null);
    }

    // Optional: Clear form after registration
    private void clearForm() {
        editTextFullName.setText("");
        editTextNIC.setText("");
        editTextPhone.setText("");
        editTextEmail.setText("");
        editTextPassword.setText("");
        editTextConfirmPassword.setText("");

        // Clear all errors
        clearError(editTextFullName);
        clearError(editTextNIC);
        clearError(editTextPhone);
        clearError(editTextEmail);
        clearError(editTextPassword);
        clearError(editTextConfirmPassword);
    }
}