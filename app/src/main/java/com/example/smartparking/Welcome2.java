package com.example.smartparking; // ඔබගේ package name එකට අනුව

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout; // Add this import
import android.view.MotionEvent; // Add this import if using touch feedback

public class Welcome2 extends AppCompatActivity {

    // Views
    private ImageView bgImage;
    private View topGradient;
    private TextView title, subtitle;
    private CardView cardAdmin, cardDriver;
    private LinearLayout roleContainer; // Optional: if you want to use it later

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome2); // activity_welcome2.xml විය යුතුය

        // Initialize views
        initializeViews();

        // Setup click listeners
        setupClickListeners();

        // Optional: Setup animations
        setupAnimations();
    }

    private void initializeViews() {
        bgImage = findViewById(R.id.bgImage);
        topGradient = findViewById(R.id.topGradient);
        title = findViewById(R.id.title);
        subtitle = findViewById(R.id.subtitle);
        cardAdmin = findViewById(R.id.cardAdmin);
        cardDriver = findViewById(R.id.cardDriver);
        roleContainer = findViewById(R.id.roleContainer); // Optional
    }

    private void setupClickListeners() {
        // Admin Card Click
        cardAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add click animation
                cardAdmin.animate()
                        .scaleX(0.95f)
                        .scaleY(0.95f)
                        .setDuration(100)
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                cardAdmin.animate()
                                        .scaleX(1f)
                                        .scaleY(1f)
                                        .setDuration(100)
                                        .start();

                                // Navigate to Admin Login/Register
                                navigateToAdminScreen();
                            }
                        })
                        .start();
            }
        });

        // Driver Card Click
        cardDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add click animation
                cardDriver.animate()
                        .scaleX(0.95f)
                        .scaleY(0.95f)
                        .setDuration(100)
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                cardDriver.animate()
                                        .scaleX(1f)
                                        .scaleY(1f)
                                        .setDuration(100)
                                        .start();

                                // Navigate to Driver Login/Register
                                navigateToDriverScreen();
                            }
                        })
                        .start();
            }
        });

        // Optional: Background image click (for debugging)
        bgImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // You can add any debug functionality here
                Toast.makeText(Welcome2.this, "Welcome to Smart Parking", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void navigateToAdminScreen() {
        // Navigate to Admin Login or Register screen
        try {
            Intent intent = new Intent(Welcome2.this, AdminRegister.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            Toast.makeText(this, "Admin Portal Selected", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Admin screen not available yet", Toast.LENGTH_SHORT).show();
            // For testing, create a simple test activity
        }
    }

    private void navigateToDriverScreen() {
        // Navigate to Driver Login or Register screen
        try {
            Intent intent = new Intent(Welcome2.this, DriverRegister.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            Toast.makeText(this, "Driver Portal Selected", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Driver screen not available yet", Toast.LENGTH_SHORT).show();
            // For testing, create a simple test activity
        }
    }

    private void setupAnimations() {
        // Initial fade in animations
        title.setAlpha(0f);
        title.setTranslationY(-20f);
        title.animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(800)
                .setStartDelay(300)
                .start();

        subtitle.setAlpha(0f);
        subtitle.setTranslationY(-20f);
        subtitle.animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(800)
                .setStartDelay(500)
                .start();

        cardAdmin.setAlpha(0f);
        cardAdmin.setTranslationY(30f);
        cardAdmin.animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(800)
                .setStartDelay(700)
                .start();

        cardDriver.setAlpha(0f);
        cardDriver.setTranslationY(30f);
        cardDriver.animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(800)
                .setStartDelay(900)
                .start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reset animations when returning to this activity
        resetCardAnimations();
    }

    private void resetCardAnimations() {
        cardAdmin.setScaleX(1f);
        cardAdmin.setScaleY(1f);
        cardDriver.setScaleX(1f);
        cardDriver.setScaleY(1f);
    }

    @Override
    public void onBackPressed() {
        // Navigate back to MainActivity or exit
        Intent intent = new Intent(Welcome2.this, MainActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    // Optional: Add touch feedback for cards - COMMENTED OUT FOR NOW
    /*
    private void setupTouchFeedback() {
        cardAdmin.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        cardAdmin.setCardBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        // Use default color instead of R.color.card_admin_color
                        cardAdmin.setCardBackgroundColor(0x40FFFFFF); // #40FFFFFF
                        break;
                }
                return false;
            }
        });

        // Similar for cardDriver if needed
    }
    */
}