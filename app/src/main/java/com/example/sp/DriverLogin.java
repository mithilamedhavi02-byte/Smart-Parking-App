package com.example.sp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sp.utils.FirebaseDatabaseHelper;

public class DriverLogin extends AppCompatActivity {

    private EditText emailInput, passwordInput;
    private TextView emailError, passwordError;
    private Button loginButton;
    private TextView registerLink;

    private FirebaseDatabaseHelper dbHelper;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_login); // XML layout

        dbHelper = new FirebaseDatabaseHelper();
        sharedPreferences = getSharedPreferences("DriverPrefs", Context.MODE_PRIVATE);

        initializeViews();
        autoFillCredentials();

        // LOGIN button click
        loginButton.setOnClickListener(v -> loginDriver());

        // REGISTER link click
        registerLink.setOnClickListener(v -> {
            Intent intent = new Intent(DriverLogin.this, DriverRegister.class);
            startActivity(intent);
        });
    }

    private void initializeViews() {
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);

        emailError = findViewById(R.id.emailError);
        passwordError = findViewById(R.id.passwordError);

        loginButton = findViewById(R.id.loginButton);
        registerLink = findViewById(R.id.registerLink);
    }

    private void autoFillCredentials() {
        emailInput.setText(sharedPreferences.getString("Email", ""));
        passwordInput.setText(sharedPreferences.getString("Password", ""));
    }

    private void loginDriver() {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        boolean valid = true;

        if (TextUtils.isEmpty(email)) {
            emailError.setVisibility(View.VISIBLE);
            emailError.setText("Enter Email Address");
            valid = false;
        } else {
            emailError.setVisibility(View.GONE);
        }

        if (TextUtils.isEmpty(password)) {
            passwordError.setVisibility(View.VISIBLE);
            passwordError.setText("Enter Password");
            valid = false;
        } else {
            passwordError.setVisibility(View.GONE);
        }

        if (!valid) return;

        loginButton.setEnabled(false);

        // 🔥 FIREBASE DRIVER LOGIN - නිවැරදි method name එක
        dbHelper.checkDriverLogin(email, password, new FirebaseDatabaseHelper.LoginCallback() {
            @Override
            public void onSuccess() {
                runOnUiThread(() -> {
                    // Save login info
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("Email", email);
                    editor.putString("Password", password);
                    editor.apply();

                    Toast.makeText(DriverLogin.this,
                            "Login Successful",
                            Toast.LENGTH_SHORT).show();

                    // ✅ Navigate to Driver Main page
                    // ප්‍රශ්නය: ඔබගේ project එකේ DriverMainActivity නමින් activity එකක් නැත
                    // ඔබට අවශ්‍ය activity එකට change කරන්න
                    Intent intent = new Intent(DriverLogin.this, MainActivity.class); // හෝ අනෙක් activity එකක්
                    startActivity(intent);
                    finish();
                });
            }

            @Override
            public void onFailure(String message) {
                runOnUiThread(() -> {
                    Toast.makeText(DriverLogin.this,
                            "Login Failed: " + message,
                            Toast.LENGTH_SHORT).show();
                    loginButton.setEnabled(true);
                });
            }
        });
    }
}