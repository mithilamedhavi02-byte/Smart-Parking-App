package com.example.sp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sp.utils.FirebaseDatabaseHelper;

public class AdminLogin extends AppCompatActivity {

    private EditText nicInput, passwordInput;
    private TextView nicError, passwordError, userLoginLink;
    private Button loginButton;

    private FirebaseDatabaseHelper dbHelper;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        dbHelper = new FirebaseDatabaseHelper();
        sharedPreferences = getSharedPreferences("AdminPrefs", Context.MODE_PRIVATE);

        initializeViews();
        autoFillCredentials();
        setupClickListeners();
    }

    private void initializeViews() {
        nicInput = findViewById(R.id.nicInput);
        passwordInput = findViewById(R.id.passwordInput);

        nicError = findViewById(R.id.nicError);
        passwordError = findViewById(R.id.passwordError);

        loginButton = findViewById(R.id.loginButton);
        userLoginLink = findViewById(R.id.userLoginLink);
    }

    private void autoFillCredentials() {
        String savedNIC = sharedPreferences.getString("NIC", "");
        String savedPassword = sharedPreferences.getString("Password", "");

        if (!TextUtils.isEmpty(savedNIC)) nicInput.setText(savedNIC);
        if (!TextUtils.isEmpty(savedPassword)) passwordInput.setText(savedPassword);
    }

    private void setupClickListeners() {
        loginButton.setOnClickListener(v -> loginAdmin());

        userLoginLink.setOnClickListener(v -> {
            // Example: Navigate to user login activity if needed
            Intent intent = new Intent(AdminLogin.this, Add_Parking.class);
            startActivity(intent);
        });
    }

    private void loginAdmin() {
        String nic = nicInput.getText().toString().trim();
        String password = passwordInput.getText().toString();

        boolean valid = true;

        if (TextUtils.isEmpty(nic)) {
            nicError.setVisibility(TextView.VISIBLE);
            nicInput.setError("Enter NIC");
            valid = false;
        } else {
            nicError.setVisibility(TextView.GONE);
            nicInput.setError(null);
        }

        if (TextUtils.isEmpty(password)) {
            passwordError.setVisibility(TextView.VISIBLE);
            passwordInput.setError("Enter Password");
            valid = false;
        } else {
            passwordError.setVisibility(TextView.GONE);
            passwordInput.setError(null);
        }

        if (!valid) return;

        loginButton.setEnabled(false);

        dbHelper.checkAdminLogin(nic, password, new FirebaseDatabaseHelper.LoginCallback() {
            @Override
            public void onSuccess() {
                runOnUiThread(() -> {
                    Toast.makeText(AdminLogin.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                    loginButton.setEnabled(true);

                    // Open AddParkingActivity after successful login
                    Intent intent = new Intent(AdminLogin.this, Add_Parking.class);
                    startActivity(intent);
                    finish();
                });
            }

            @Override
            public void onFailure(String message) {
                runOnUiThread(() -> {
                    Toast.makeText(AdminLogin.this, "Login Failed: " + message, Toast.LENGTH_SHORT).show();
                    loginButton.setEnabled(true);
                });
            }
        });
    }
}
