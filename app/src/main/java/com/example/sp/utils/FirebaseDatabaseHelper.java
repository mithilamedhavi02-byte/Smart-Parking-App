package com.example.sp.utils;

import android.util.Log;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DataSnapshot;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.HashMap;
import java.util.Map;

public class FirebaseDatabaseHelper {

    private DatabaseReference databaseReference;
    private static final String TAG = "FirebaseDBHelper";

    public FirebaseDatabaseHelper() {
        try {
            // Initialize Firebase Database
            FirebaseDatabase database = FirebaseDatabase.getInstance();

            // Get reference to "users" node
            databaseReference = database.getReference("users");

            Log.d(TAG, "Firebase Database initialized successfully");

        } catch (Exception e) {
            Log.e(TAG, "Error initializing Firebase: " + e.getMessage());
        }
    }

    // Save user to Firebase using Map
    public void saveUserMap(String fullName, String nic, String phone,
                            String email, String password, String userType,
                            DatabaseCallback callback) {
        try {
            String userId = databaseReference.push().getKey();
            if (userId == null) {
                callback.onError("Failed to generate user ID");
                return;
            }

            // Create user data as Map
            Map<String, Object> userData = new HashMap<>();
            userData.put("userId", userId);
            userData.put("fullName", fullName);
            userData.put("nic", nic);
            userData.put("phone", phone);
            userData.put("email", email);
            userData.put("password", password);
            userData.put("userType", userType);
            userData.put("timestamp", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                    Locale.getDefault()).format(new Date()));

            // Save to Firebase
            databaseReference.child(userId).setValue(userData)
                    .addOnSuccessListener(aVoid -> {
                        Log.d(TAG, "User data saved: " + userId);
                        callback.onSuccess(userId);
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "Failed to save: " + e.getMessage());
                        callback.onError(e.getMessage());
                    });

        } catch (Exception e) {
            Log.e(TAG, "Error in saveUserMap: " + e.getMessage());
            callback.onError(e.getMessage());
        }
    }

    // Check if email already exists
    public void checkEmailExists(String email, EmailCheckCallback callback) {
        databaseReference.orderByChild("email").equalTo(email)
                .addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        boolean exists = dataSnapshot.exists();
                        Log.d(TAG, "Email check: " + email + " exists: " + exists);
                        callback.onEmailChecked(exists);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e(TAG, "Error checking email: " + databaseError.getMessage());
                        callback.onEmailChecked(false);
                    }
                });
    }

    // Check if NIC already exists
    public void checkNicExists(String nic, NicCheckCallback callback) {
        databaseReference.orderByChild("nic").equalTo(nic)
                .addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        boolean exists = dataSnapshot.exists();
                        Log.d(TAG, "NIC check: " + nic + " exists: " + exists);
                        callback.onNicChecked(exists);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e(TAG, "Error checking NIC: " + databaseError.getMessage());
                        callback.onNicChecked(false);
                    }
                });
    }

    // Interface for callbacks
    public interface DatabaseCallback {
        void onSuccess(String userId);
        void onError(String errorMessage);
    }

    public interface EmailCheckCallback {
        void onEmailChecked(boolean exists);
    }

    public interface NicCheckCallback {
        void onNicChecked(boolean exists);
    }
}