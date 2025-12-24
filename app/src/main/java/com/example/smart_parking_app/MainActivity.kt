package com.example.smart_parking_app

import android.net.Uri
import android.os.Bundle
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val videoView = findViewById<VideoView>(R.id.videoView)

        // Method 1: If you named it m.mp4
        // val uri = Uri.parse("android.resource://" + packageName + "/" + R.raw.m)

        // Method 2: Generate URI
        val videoResource = "android.resource://$packageName/${R.raw.M}"
        val uri = Uri.parse(videoResource)

        videoView.setVideoURI(uri)
        videoView.requestFocus()

        videoView.setOnPreparedListener { mediaPlayer ->
            mediaPlayer.isLooping = true // Loop the video
            mediaPlayer.setVolume(0f, 0f) // Mute audio
            videoView.start()

            // Optional: Adjust video scaling
            // mediaPlayer.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING)
        }

        // Handle errors
        videoView.setOnErrorListener { mp, what, extra ->
            // Log error or show fallback
            // You could show an image instead
            true
        }
    }
}