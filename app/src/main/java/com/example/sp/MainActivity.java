package com.example.sp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private VideoView videoBackground;
    private Button startButton;
    private TextView appTitle, welcomeMessage, tagline, bottomNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize all views
        initializeViews();

        // Setup video background
        setupVideoBackground();

        // Setup button click listener
        setupButtonClickListener();

        // Start sequential animations
        startSequentialAnimations();
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
            // Try to load video from raw folder
            Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.mm);
            videoBackground.setVideoURI(uri);
            videoBackground.start();

            // Set video properties
            videoBackground.setOnPreparedListener(mp -> {
                mp.setLooping(true);
                mp.setVolume(0f, 0f); // Mute audio
                mp.setVideoScalingMode(android.media.MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
            });

        } catch (Exception e) {
            e.printStackTrace();
            // Fallback to black background if video fails
            videoBackground.setBackgroundColor(getResources().getColor(android.R.color.black));
        }
    }

    private void setupButtonClickListener() {
        startButton.setOnClickListener(v -> {
            // Button click animation
            Animation shrinkAnim = AnimationUtils.loadAnimation(MainActivity.this, R.anim.button_shrink);
            startButton.startAnimation(shrinkAnim);

            // Navigate to next activity after animation
            new Handler().postDelayed(() -> {
                Intent intent = new Intent(MainActivity.this, Welcome.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish(); // Optional: close current activity
            }, 200);
        });
    }

    private void startSequentialAnimations() {
        // Create handler for delayed animations
        Handler handler = new Handler();

        // Animation 1: App Title (500ms delay)
        handler.postDelayed(() -> {
            Animation titleAnim = AnimationUtils.loadAnimation(this, R.anim.typewriter);
            appTitle.startAnimation(titleAnim);
            appTitle.setVisibility(View.VISIBLE);
        }, 500);

        // Animation 2: Welcome Message (1200ms delay)
        handler.postDelayed(() -> {
            Animation welcomeAnim = AnimationUtils.loadAnimation(this, R.anim.fade_in);
            welcomeMessage.startAnimation(welcomeAnim);
            welcomeMessage.setVisibility(View.VISIBLE);
        }, 1200);

        // Animation 3: Tagline (1800ms delay)
        handler.postDelayed(() -> {
            Animation taglineAnim = AnimationUtils.loadAnimation(this, R.anim.slide_up);
            tagline.startAnimation(taglineAnim);
            tagline.setVisibility(View.VISIBLE);
        }, 1800);

        // Animation 4: Bottom Note (2400ms delay)
        handler.postDelayed(() -> {
            Animation noteAnim = AnimationUtils.loadAnimation(this, R.anim.fade_in);
            bottomNote.startAnimation(noteAnim);
            bottomNote.setVisibility(View.VISIBLE);
        }, 2400);

        // Animation 5: Start Button (3000ms delay) - with bounce effect
        handler.postDelayed(() -> {
            Animation buttonAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);
            startButton.startAnimation(buttonAnim);
            startButton.setVisibility(View.VISIBLE);
        }, 3000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Restart video when returning to activity
        if (videoBackground != null) {
            videoBackground.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Pause video to save resources
        if (videoBackground != null && videoBackground.isPlaying()) {
            videoBackground.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Clean up resources
        if (videoBackground != null) {
            videoBackground.stopPlayback();
        }
    }
}