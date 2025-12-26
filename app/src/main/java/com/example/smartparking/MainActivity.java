package com.example.smartparking; // ඔබගේ package name එකට අනුව

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.VideoView;
import android.widget.Toast;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private VideoView videoBackground;
    private Button startButton;
    private TextView appTitle, welcomeMessage, tagline, bottomNote;

    // Video resource
    private static final int VIDEO_RESOURCE = R.raw.mm; // raw folder එකට video එකක් add කරන්න

    // Double back press exit
    private boolean doubleBackToExitPressedOnce = false;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        initializeViews();

        // Setup video background
        setupVideoBackground();

        // Setup click listeners
        setupClickListeners();

        // Setup animations
        setupAnimations();
    }

    private void initializeViews() {
        videoBackground = findViewById(R.id.videoBackground);
        startButton = findViewById(R.id.startButton);
        appTitle = findViewById(R.id.appTitle);
        welcomeMessage = findViewById(R.id.welcomeMessage);
        tagline = findViewById(R.id.tagline);
        bottomNote = findViewById(R.id.bottomNote);
    }

    private void setupVideoBackground() {
        try {
            // Set video URI
            String videoPath = "android.resource://" + getPackageName() + "/" + VIDEO_RESOURCE;
            Uri videoUri = Uri.parse(videoPath);
            videoBackground.setVideoURI(videoUri);

            // Set video properties
            videoBackground.setOnPreparedListener(mp -> {
                mp.setLooping(true);
                mp.setVolume(0, 0); // Mute audio

                // Calculate video scaling
                float videoRatio = mp.getVideoWidth() / (float) mp.getVideoHeight();
                float screenRatio = videoBackground.getWidth() / (float) videoBackground.getHeight();

                float scale = screenRatio / videoRatio;
                if (scale >= 1f) {
                    videoBackground.setScaleX(scale);
                } else {
                    videoBackground.setScaleY(1f / scale);
                }
            });

            // Start video playback
            videoBackground.start();

        } catch (Exception e) {
            // If video fails to load, use fallback
            Log.e(TAG, "Error setting up video background", e);
            videoBackground.setVisibility(View.GONE);
            Toast.makeText(this, "Using static background", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupClickListeners() {
        // Start Button Click Listener
        startButton.setOnClickListener(v -> {
            // Add button click animation
            startButton.animate()
                    .scaleX(0.95f)
                    .scaleY(0.95f)
                    .setDuration(100)
                    .withEndAction(() -> {
                        startButton.animate()
                                .scaleX(1f)
                                .scaleY(1f)
                                .setDuration(100)
                                .start();

                        // Navigate to next screen
                        navigateToNextScreen();
                    })
                    .start();
        });

        // Optional: App Title click for easter egg
        appTitle.setOnClickListener(new View.OnClickListener() {
            int clickCount = 0;
            long lastClickTime = 0;

            @Override
            public void onClick(View v) {
                long currentTime = System.currentTimeMillis();
                if (currentTime - lastClickTime < 500) {
                    clickCount++;
                } else {
                    clickCount = 1;
                }
                lastClickTime = currentTime;

                if (clickCount >= 5) {
                    Toast.makeText(MainActivity.this, "🎉 Welcome to Smart Parking Finder! 🚗", Toast.LENGTH_SHORT).show();
                    clickCount = 0;
                }
            }
        });
    }

    private void navigateToNextScreen() {
        // Navigate to Welcome screen
        Intent intent = new Intent(MainActivity.this, Welcome2.class); // Welcome class එකට change කරන්න
        startActivity(intent);

        // Add fade transition
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        // Optional delay before finishing
        new Handler().postDelayed(() -> {
            // If you want to finish this activity
            // finish();
        }, 300);
    }

    private void setupAnimations() {
        // Animate app title on start
        appTitle.setAlpha(0f);
        appTitle.animate()
                .alpha(1f)
                .setDuration(1500)
                .setStartDelay(500)
                .start();

        // Animate welcome message
        welcomeMessage.setAlpha(0f);
        welcomeMessage.setTranslationY(20f);
        welcomeMessage.animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(1000)
                .setStartDelay(800)
                .start();

        // Animate tagline
        tagline.setAlpha(0f);
        tagline.setTranslationY(20f);
        tagline.animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(1000)
                .setStartDelay(1000)
                .start();

        // Animate start button
        startButton.setAlpha(0f);
        startButton.setScaleX(0.8f);
        startButton.setScaleY(0.8f);
        startButton.animate()
                .alpha(1f)
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(800)
                .setStartDelay(1500)
                .start();

        // Animate bottom note
        bottomNote.setAlpha(0f);
        bottomNote.animate()
                .alpha(1f)
                .setDuration(1000)
                .setStartDelay(2000)
                .start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Resume video playback
        if (videoBackground != null) {
            videoBackground.start();
        }

        // Reset animations
        resetAnimations();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Pause video playback
        if (videoBackground != null && videoBackground.isPlaying()) {
            videoBackground.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Release video resources
        if (videoBackground != null) {
            videoBackground.stopPlayback();
        }
    }

    private void resetAnimations() {
        startButton.setScaleX(1f);
        startButton.setScaleY(1f);
    }

    // Handle back button press - updated method
    @Override
    public void onBackPressed() {
        // Use double back press to exit
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
    }
}