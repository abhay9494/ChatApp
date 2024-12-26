package com.example.chatapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;

import com.example.chatapp.model.UserModel;
import com.example.chatapp.utils.AndroidUtil;
import com.example.chatapp.utils.FirebaseUtil;

public class SplashActivity extends AppCompatActivity {
    private static final int SPLASH_DELAY = 2000; // Splash screen delay in milliseconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Handle cases where the app is opened via notification
        if (getIntent().getExtras() != null) {
            handleNotificationIntent();
        } else {
            // Handle standard splash screen behavior
            new Handler().postDelayed(() -> {
                if (FirebaseUtil.isLoggedIn()) {
                    // User is logged in, go to MainActivity
                    Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(mainIntent);
                } else {
                    // User is not logged in, go to LoginEmailActivity
                    Intent loginIntent = new Intent(SplashActivity.this, LoginEmailActivity.class);
                    startActivity(loginIntent);
                }
                finish(); // Close SplashActivity
            }, SPLASH_DELAY);
        }

        // Allow network operations on the main thread temporarily (shouldn't be used in production)
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    /**
     * Handles intent from a notification and redirects to the appropriate activity.
     */
    private void handleNotificationIntent() {
        String userId = getIntent().getExtras().getString("userId");
        if (userId != null) {
            FirebaseUtil.allUserCollectionReference().document(userId).get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && task.getResult() != null) {
                            UserModel model = task.getResult().toObject(UserModel.class);

                            // Start MainActivity without animation
                            Intent mainIntent = new Intent(this, MainActivity.class);
                            mainIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(mainIntent);

                            // Open ChatActivity with the user model passed
                            Intent chatIntent = new Intent(this, ChatActivity.class);
                            AndroidUtil.passUserModelAsIntent(chatIntent, model);
                            chatIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(chatIntent);
                        }
                        finish(); // Close SplashActivity
                    });
        } else {
            // If no userId is provided in the notification, fallback to the standard flow
            startActivity(new Intent(this, LoginEmailActivity.class));
            finish();
        }
    }
}
