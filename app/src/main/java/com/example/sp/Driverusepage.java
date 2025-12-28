package com.example.sp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class MainActivity extends AppCompatActivity {

    // UI Components
    private EditText searchEditText;
    private Button findParkingButton;
    private CardView historyCard, currentSessionCard, terminalChangeCard, parkingSpotCard;
    private TextView currentSessionTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();
        setupClickListeners();
        startSessionTimer();
    }

    private void initializeViews() {
        searchEditText = findViewById(R.id.searchEditText);
        findParkingButton = findViewById(R.id.findParkingButton);

        historyCard = findViewById(R.id.historyCard);
        currentSessionCard = findViewById(R.id.currentSessionCard);
        terminalChangeCard = findViewById(R.id.terminalChangeCard);
        parkingSpotCard = findViewById(R.id.parkingSpotCard);

        currentSessionTimer = findViewById(R.id.currentSessionTimer);
    }

    private void setupClickListeners() {
        // Find Parking Button
        findParkingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchQuery = searchEditText.getText().toString().trim();
                if (!searchQuery.isEmpty()) {
                    searchParking(searchQuery);
                } else {
                    showAllParkingSpots();
                }
            }
        });

        // History Card
        historyCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openParkingHistory();
            }
        });

        // Current Session Card
        currentSessionCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCurrentSession();
            }
        });

        // Terminal Change Card
        terminalChangeCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTerminalChange();
            }
        });

        // Parking Spot Card
        parkingSpotCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openParkingDetails("Central Plaza Garage", "123 Market St.", "0.2 mi");
            }
        });

        // Search functionality on enter
        searchEditText.setOnEditorActionListener((v, actionId, event) -> {
            String searchQuery = searchEditText.getText().toString().trim();
            if (!searchQuery.isEmpty()) {
                searchParking(searchQuery);
                return true;
            }
            return false;
        });
    }

    private void searchParking(String location) {
        Toast.makeText(this, "Searching for parking near: " + location,
                Toast.LENGTH_SHORT).show();

        // Here you would implement actual search logic
        // For now, just show a message
        Intent intent = new Intent(MainActivity.this, ParkingSearchResults.class);
        intent.putExtra("search_query", location);
        startActivity(intent);
    }

    private void showAllParkingSpots() {
        Toast.makeText(this, "Showing all available parking spots",
                Toast.LENGTH_SHORT).show();

        // Navigate to parking list activity
        Intent intent = new Intent(MainActivity.this, ParkingListActivity.class);
        startActivity(intent);
    }

    private void openParkingHistory() {
        Toast.makeText(this, "Opening Parking History", Toast.LENGTH_SHORT).show();

        // Navigate to history activity
        Intent intent = new Intent(MainActivity.this, ParkingHistoryActivity.class);
        startActivity(intent);
    }

    private void openCurrentSession() {
        Toast.makeText(this, "Current Session Details", Toast.LENGTH_SHORT).show();

        // Navigate to session details activity
        Intent intent = new Intent(MainActivity.this, CurrentSessionActivity.class);
        startActivity(intent);
    }

    private void openTerminalChange() {
        Toast.makeText(this, "Terminal/Payment Options", Toast.LENGTH_SHORT).show();

        // Navigate to payment/terminal activity
        Intent intent = new Intent(MainActivity.this, TerminalActivity.class);
        startActivity(intent);
    }

    private void openParkingDetails(String name, String address, String distance) {
        Toast.makeText(this, "Opening: " + name, Toast.LENGTH_SHORT).show();

        // Navigate to parking details activity
        Intent intent = new Intent(MainActivity.this, ParkingDetailsActivity.class);
        intent.putExtra("parking_name", name);
        intent.putExtra("parking_address", address);
        intent.putExtra("parking_distance", distance);
        startActivity(intent);
    }

    private void startSessionTimer() {
        // This is a simple timer simulation
        // In a real app, you would use a proper timer or get data from backend
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    int hours = 0;
                    int minutes = 0;
                    int seconds = 45;

                    while (true) {
                        Thread.sleep(1000); // Wait 1 second
                        seconds++;

                        if (seconds >= 60) {
                            minutes++;
                            seconds = 0;
                        }
                        if (minutes >= 60) {
                            hours++;
                            minutes = 0;
                        }

                        final String timeString = String.format("%02d:%02d:%02d",
                                hours, minutes, seconds);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                currentSessionTimer.setText(timeString);
                            }
                        });
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh data when returning to this activity
        refreshParkingData();
    }

    private void refreshParkingData() {
        // In a real app, this would fetch fresh data from API/database
        Toast.makeText(this, "Refreshing parking data...", Toast.LENGTH_SHORT).show();
    }

    // Optional: Add a refresh button functionality
    public void onRefreshClicked(View view) {
        refreshParkingData();
    }

    // Optional: Add user profile navigation
    public void onProfileClicked(View view) {
        Toast.makeText(this, "Opening User Profile", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(MainActivity.this, UserProfileActivity.class);
        startActivity(intent);
    }

    // Optional: Add settings navigation
    public void onSettingsClicked(View view) {
        Toast.makeText(this, "Opening Settings", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(intent);
    }
}