package com.example.myapplication;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Register extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        String role = getIntent().getStringExtra("SELECTED_ROLE");
        Toast.makeText(this, "Register as: " + role, Toast.LENGTH_SHORT).show();
    }
}