package com.example.sp;

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

import androidx.appcompat.app.AppCompatActivity;

import com.example.sp.utils.FirebaseDatabaseHelper;

import java.util.regex.Pattern;

public class AdminRegister extends AppCompatActivity {

    // Declare UI components
    private EditText fullNameInput;
    private EditText nicInput;
    private EditText phoneInput;
    private EditText emailInput;
    private EditText passwordInput;
    private EditText confirmPasswordInput;

    private TextView fullNameError;
    private TextView nicError;
    private TextView phoneError;
    private TextView emailError;
    private TextView passwordError;
    private TextView confirmPasswordError;

    private Button registerButton;
    private TextView loginLink;
    private ProgressBar progressBar;

    // Firebase helper
    private FirebaseDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Fixed: use R.layout, not R.activity
        setContentView(R.layout.activity_admin_register);

        // Initialize Firebase helper
        dbHelper = new FirebaseDatabaseHelper();

        System.out.println("AdminRegister Activity Started");
        System.out.println("Firebase initialized for Admin Register");

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
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }
    }

    private void setupTextChangeListeners() {
        // Full Name validation
        fullNameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                validateFullName();
            }
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        // NIC validation
        nicInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                validateNIC();
            }
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        // Phone validation
        phoneInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                validatePhone();
            }
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        // Email validation
        emailInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                validateEmail();
                String email = s.toString().trim();
                if (!email.isEmpty() && validateEmailFormat(email)) {
                    checkEmailExists(email);
                }
            }
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        // Password validation
        passwordInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                validatePassword();
                validateConfirmPassword();
            }
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        // Confirm Password validation
        confirmPasswordInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                validateConfirmPassword();
            }
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });
    }

    private void setupButtonClickListeners() {
        registerButton.setOnClickListener(v -> {
            if (validateAllFields()) {
                registerAdminToFirebase();
            } else {
                Toast.makeText(AdminRegister.this, "Please fix all errors", Toast.LENGTH_SHORT).show();
            }
        });

        loginLink.setOnClickListener(v ->
                Toast.makeText(AdminRegister.this, "Navigate to Login", Toast.LENGTH_SHORT).show());
    }

    // Validation functions
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

    private boolean validateEmailFormat(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
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

    private boolean validateAllFields() {
        boolean isFullNameValid = validateFullName();
        boolean isNICValid = validateNIC();
        boolean isPhoneValid = validatePhone();
        boolean isEmailValid = validateEmail();
        boolean isPasswordValid = validatePassword();
        boolean isConfirmPasswordValid = validateConfirmPassword();

        if (fullNameInput.getText().toString().trim().isEmpty()) {
            fullNameInput.setError("Full name is required");
            return false;
        }
        if (nicInput.getText().toString().trim().isEmpty()) {
            nicInput.setError("NIC is required");
            return false;
        }
        if (phoneInput.getText().toString().trim().isEmpty()) {
            phoneInput.setError("Phone is required");
            return false;
        }
        if (emailInput.getText().toString().trim().isEmpty()) {
            emailInput.setError("Email is required");
            return false;
        }
        if (passwordInput.getText().toString().isEmpty()) {
            passwordInput.setError("Password is required");
            return false;
        }

        return isFullNameValid && isNICValid && isPhoneValid &&
                isEmailValid && isPasswordValid && isConfirmPasswordValid;
    }

    private void registerAdminToFirebase() {
        String fullName = fullNameInput.getText().toString().trim();
        String nic = nicInput.getText().toString().trim();
        String phone = phoneInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString();

        showProgress(true);
        registerButton.setEnabled(false);

        System.out.println("Saving Admin to Firebase:");
        System.out.println("Full Name: " + fullName);
        System.out.println("Email: " + email);
        System.out.println("Phone: " + phone);

        dbHelper.saveUserMap(fullName, nic, phone, email, password, "admin",
                new FirebaseDatabaseHelper.DatabaseCallback() {
                    @Override
                    public void onSuccess(String userId) {
                        runOnUiThread(() -> {
                            showProgress(false);
                            registerButton.setEnabled(true);

                            Toast.makeText(AdminRegister.this,
                                    "Admin Registration Successful!\nAdmin ID: " + userId,
                                    Toast.LENGTH_LONG).show();

                            System.out.println("Admin saved successfully! User ID: " + userId);

                            clearForm();
                        });
                    }

                    @Override
                    public void onError(String errorMessage) {
                        runOnUiThread(() -> {
                            showProgress(false);
                            registerButton.setEnabled(true);
                            Toast.makeText(AdminRegister.this,
                                    "Registration Failed: " + errorMessage,
                                    Toast.LENGTH_LONG).show();
                            System.err.println("Firebase Error: " + errorMessage);
                        });
                    }
                });
    }

    private void showProgress(boolean show) {
        if (progressBar != null) {
            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    private void clearForm() {
        fullNameInput.setText("");
        nicInput.setText("");
        phoneInput.setText("");
        emailInput.setText("");
        passwordInput.setText("");
        confirmPasswordInput.setText("");

        fullNameError.setVisibility(View.GONE);
        nicError.setVisibility(View.GONE);
        phoneError.setVisibility(View.GONE);
        emailError.setVisibility(View.GONE);
        passwordError.setVisibility(View.GONE);
        confirmPasswordError.setVisibility(View.GONE);

        fullNameInput.setError(null);
        nicInput.setError(null);
        phoneInput.setError(null);
        emailInput.setError(null);
        passwordInput.setError(null);
        confirmPasswordInput.setError(null);
    }
}
