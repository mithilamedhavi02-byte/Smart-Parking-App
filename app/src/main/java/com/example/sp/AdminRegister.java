package com.example.sp;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_register);

        initializeViews();
        setupTextChangeListeners();
        setupButtonClickListeners();
    }

    private void initializeViews() {
        // Initialize EditTexts
        fullNameInput = findViewById(R.id.fullNameInput);
        nicInput = findViewById(R.id.nicInput);
        phoneInput = findViewById(R.id.phoneInput);
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        confirmPasswordInput = findViewById(R.id.confirmPasswordInput);

        // Initialize Error TextViews
        fullNameError = findViewById(R.id.fullNameError);
        nicError = findViewById(R.id.nicError);
        phoneError = findViewById(R.id.phoneError);
        emailError = findViewById(R.id.emailError);
        passwordError = findViewById(R.id.passwordError);
        confirmPasswordError = findViewById(R.id.confirmPasswordError);

        // Initialize Buttons and Links
        registerButton = findViewById(R.id.registerButton);
        loginLink = findViewById(R.id.loginLink);
    }

    private void setupTextChangeListeners() {
        // Full Name validation
        fullNameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                validateFullName();
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        // NIC validation
        nicInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                validateNIC();
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        // Phone validation
        phoneInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                validatePhone();
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        // Email validation
        emailInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                validateEmail();
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        // Password validation
        passwordInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                validatePassword();
                validateConfirmPassword();
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        // Confirm Password validation
        confirmPasswordInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                validateConfirmPassword();
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });
    }

    private void setupButtonClickListeners() {
        // Register Button Click Listener
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateAllFields()) {
                    registerUser();
                } else {
                    Toast.makeText(AdminRegister.this,
                            "Please fix all errors", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Login Link Click Listener
        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AdminRegister.this,
                        "Navigate to Login", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Validation Functions
    private boolean validateFullName() {
        String name = fullNameInput.getText().toString().trim();
        if (name.length() < 3) {
            fullNameError.setVisibility(View.VISIBLE);
            return false;
        } else {
            fullNameError.setVisibility(View.GONE);
            return true;
        }
    }

    private boolean validateNIC() {
        String nic = nicInput.getText().toString().trim();
        Pattern nicPattern = Pattern.compile("^([0-9]{9}[xXvV]|[0-9]{12})$");
        if (!nicPattern.matcher(nic).matches()) {
            nicError.setVisibility(View.VISIBLE);
            return false;
        } else {
            nicError.setVisibility(View.GONE);
            return true;
        }
    }

    private boolean validatePhone() {
        String phone = phoneInput.getText().toString().trim();
        Pattern phonePattern = Pattern.compile("^(\\+94|0)(7[0-9])([0-9]{7})$");
        if (!phonePattern.matcher(phone).matches()) {
            phoneError.setVisibility(View.VISIBLE);
            return false;
        } else {
            phoneError.setVisibility(View.GONE);
            return true;
        }
    }

    private boolean validateEmail() {
        String email = emailInput.getText().toString().trim();
        // Fix: Use android.util.Patterns instead of just Patterns
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailError.setVisibility(View.VISIBLE);
            return false;
        } else {
            emailError.setVisibility(View.GONE);
            return true;
        }
    }

    private boolean validatePassword() {
        String password = passwordInput.getText().toString();
        if (password.length() < 8) {
            passwordError.setVisibility(View.VISIBLE);
            return false;
        } else {
            passwordError.setVisibility(View.GONE);
            return true;
        }
    }

    private boolean validateConfirmPassword() {
        String password = passwordInput.getText().toString();
        String confirmPassword = confirmPasswordInput.getText().toString();
        if (!password.equals(confirmPassword) || confirmPassword.isEmpty()) {
            confirmPasswordError.setVisibility(View.VISIBLE);
            return false;
        } else {
            confirmPasswordError.setVisibility(View.GONE);
            return true;
        }
    }

    private boolean validateAllFields() {
        boolean isFullNameValid = validateFullName();
        boolean isNICValid = validateNIC();
        boolean isPhoneValid = validatePhone();
        boolean isEmailValid = validateEmail();
        boolean isPasswordValid = validatePassword();
        boolean isConfirmPasswordValid = validateConfirmPassword();

        return isFullNameValid && isNICValid && isPhoneValid &&
                isEmailValid && isPasswordValid && isConfirmPasswordValid;
    }

    private void registerUser() {
        String fullName = fullNameInput.getText().toString().trim();
        String nic = nicInput.getText().toString().trim();
        String phone = phoneInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString();

        Toast.makeText(this,
                "Registration successful for " + fullName,
                Toast.LENGTH_SHORT).show();

        clearForm();
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
    }
}