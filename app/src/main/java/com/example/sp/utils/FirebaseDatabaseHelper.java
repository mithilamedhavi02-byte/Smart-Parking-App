package com.example.sp.utils;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FirebaseDatabaseHelper {

    private final DatabaseReference dbRef;

    public FirebaseDatabaseHelper() {
        dbRef = FirebaseDatabase.getInstance().getReference("users"); // "users" node එක
    }

    // Interface for login callback
    public interface LoginCallback {
        void onSuccess();
        void onFailure(String message);
    }

    public void checkAdminLogin(String nic, String password, LoginCallback callback) {
        dbRef.orderByChild("nic").equalTo(nic)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                                String userPassword = userSnapshot.child("password").getValue(String.class);
                                String role = userSnapshot.child("role").getValue(String.class);
                                if (userPassword != null && userPassword.equals(password) && role.equals("admin")) {
                                    callback.onSuccess();
                                    return;
                                }
                            }
                            callback.onFailure("Incorrect password or not an admin");
                        } else {
                            callback.onFailure("NIC not registered");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        callback.onFailure("Firebase Error: " + error.getMessage());
                    }
                });
    }

    // 🔥 NEW: Driver Login Method
    public void checkDriverLogin(String email, String password, LoginCallback callback) {
        dbRef.orderByChild("email").equalTo(email)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                                String userPassword = userSnapshot.child("password").getValue(String.class);
                                String role = userSnapshot.child("role").getValue(String.class);
                                if (userPassword != null && userPassword.equals(password) && role.equals("driver")) {
                                    callback.onSuccess();
                                    return;
                                }
                            }
                            callback.onFailure("Incorrect password or not a driver");
                        } else {
                            callback.onFailure("Email not registered");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        callback.onFailure("Firebase Error: " + error.getMessage());
                    }
                });
    }

    // Save user/admin/driver data
    public interface DatabaseCallback {
        void onSuccess(String userId);
        void onError(String errorMessage);
    }

    public void saveUserMap(String fullName, String nic, String phone, String email, String password, String role, DatabaseCallback callback) {
        String userId = dbRef.push().getKey();
        if (userId == null) {
            callback.onError("Failed to generate User ID");
            return;
        }

        dbRef.child(userId).child("fullName").setValue(fullName);
        dbRef.child(userId).child("nic").setValue(nic);
        dbRef.child(userId).child("phone").setValue(phone);
        dbRef.child(userId).child("email").setValue(email);
        dbRef.child(userId).child("password").setValue(password);
        dbRef.child(userId).child("role").setValue(role);

        callback.onSuccess(userId);
    }

    public void checkEmailExists(String email, EmailCheckCallback callback) {
        dbRef.orderByChild("email").equalTo(email)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        callback.onResult(snapshot.exists());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        callback.onResult(false);
                    }
                });
    }

    public interface EmailCheckCallback {
        void onResult(boolean exists);
    }
}