package com.example.sp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class Welcome extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome); // Assuming your XML file is named activity_welcome.xml

        initializeViews();
    }

    private void initializeViews() {
        // Initialize CardViews
        CardView cardAdmin = findViewById(R.id.cardAdmin);
        CardView cardDriver = findViewById(R.id.cardDriver);

        // Set click listeners for the cards
        cardAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle Admin card click
                Toast.makeText(Welcome.this, "Admin Selected", Toast.LENGTH_SHORT).show();

                // Navigate to Admin Activity
                Intent intent = new Intent(Welcome.this, AdminRegister.class); // Replace with your Admin activity class
                startActivity(intent);

                // Optional: Add animation
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        cardDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle Driver card click
                Toast.makeText(Welcome.this, "Driver Selected", Toast.LENGTH_SHORT).show();

                // Navigate to Driver Activity
                Intent intent = new Intent(Welcome.this, DriverRegister.class); // Replace with your Driver activity class
                startActivity(intent);

                // Optional: Add animation
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        // Optional: If you want to add visual feedback on card click
        cardAdmin.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case android.view.MotionEvent.ACTION_DOWN:
                    cardAdmin.setCardElevation(2f); // Lower elevation when pressed
                    break;
                case android.view.MotionEvent.ACTION_UP:
                case android.view.MotionEvent.ACTION_CANCEL:
                    cardAdmin.setCardElevation(6f); // Restore elevation
                    break;
            }
            return false;
        });

        cardDriver.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case android.view.MotionEvent.ACTION_DOWN:
                    cardDriver.setCardElevation(2f);
                    break;
                case android.view.MotionEvent.ACTION_UP:
                case android.view.MotionEvent.ACTION_CANCEL:
                    cardDriver.setCardElevation(6f);
                    break;
            }
            return false;
        });
    }

    // Optional: Handle back button press
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // You can add custom back button behavior here
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}