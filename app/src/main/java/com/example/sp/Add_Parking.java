package com.example.sp;

import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Add_Parking extends AppCompatActivity {

    // Text fields
    private EditText editTextParkingName, editTextAddress;
    private AutoCompleteTextView autoCompleteDistrict;

    private EditText editTextCarCapacity, editTextBikeCapacity;
    private EditText editTextOpenTime, editTextCloseTime;
    private EditText editTextHourlyRate;

    // Switches
    private Switch switchCCTV, switchSecurity, switchBooked, switchEV;

    // Buttons
    private Button buttonSaveParking, buttonEditParking;

    // Images
    private ImageView imageEntrance, imageInterior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_parking);

        initializeViews();
        setupClickListeners();
    }

    private void initializeViews() {

        editTextParkingName = findViewById(R.id.editTextParkingName);
        editTextAddress = findViewById(R.id.editTextAddress);
        autoCompleteDistrict = findViewById(R.id.autoCompleteDistrict);

        editTextCarCapacity = findViewById(R.id.editTextCarCapacity);
        editTextBikeCapacity = findViewById(R.id.editTextBikeCapacity);

        editTextOpenTime = findViewById(R.id.editTextOpenTime);
        editTextCloseTime = findViewById(R.id.editTextCloseTime);

        editTextHourlyRate = findViewById(R.id.editTextHourlyRate);

        switchCCTV = findViewById(R.id.switchCCTV);
        switchSecurity = findViewById(R.id.switchSecurity);
        switchBooked = findViewById(R.id.switchBooked);
        switchEV = findViewById(R.id.switchEV);

        buttonSaveParking = findViewById(R.id.buttonSaveParking);
        buttonEditParking = findViewById(R.id.buttonEditParking);

        imageEntrance = findViewById(R.id.imageEntrance);
        imageInterior = findViewById(R.id.imageInterior);
    }

    private void setupClickListeners() {

        // SAVE BUTTON
        buttonSaveParking.setOnClickListener(v -> saveParking());

        // EDIT BUTTON
        buttonEditParking.setOnClickListener(v ->
                Toast.makeText(this, "Edit mode enabled", Toast.LENGTH_SHORT).show()
        );

        // IMAGE UPLOAD (Dummy)
        imageEntrance.setOnClickListener(v ->
                Toast.makeText(this, "Upload entrance photo", Toast.LENGTH_SHORT).show()
        );

        imageInterior.setOnClickListener(v ->
                Toast.makeText(this, "Upload interior photo", Toast.LENGTH_SHORT).show()
        );
    }

    private void saveParking() {

        String parkingName = editTextParkingName.getText().toString().trim();
        String address = editTextAddress.getText().toString().trim();
        String district = autoCompleteDistrict.getText().toString().trim();

        String carCapacity = editTextCarCapacity.getText().toString().trim();
        String bikeCapacity = editTextBikeCapacity.getText().toString().trim();

        String openTime = editTextOpenTime.getText().toString().trim();
        String closeTime = editTextCloseTime.getText().toString().trim();

        String hourlyRate = editTextHourlyRate.getText().toString().trim();

        boolean cctv = switchCCTV.isChecked();
        boolean security = switchSecurity.isChecked();
        boolean booked = switchBooked.isChecked();
        boolean ev = switchEV.isChecked();

        // BASIC VALIDATION
        if (parkingName.isEmpty() || address.isEmpty() || district.isEmpty()) {
            Toast.makeText(this,
                    "Please fill required fields",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        // 🔥 TEMP SUCCESS MESSAGE
        Toast.makeText(this,
                "Parking Saved Successfully ✅",
                Toast.LENGTH_LONG).show();

        // 🔜 Next Step:
        // Save to Firebase / Navigate to dashboard
    }
}
