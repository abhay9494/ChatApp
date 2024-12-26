package com.example.chatapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chatapp.utils.AndroidUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import com.example.chatapp.utils.EmailTemplate;

public class LoginOtpActivity extends AppCompatActivity {

    private String email;
    private String generatedOtp;
    private long timeoutSeconds = 60L;

    private EditText otpInput;
    private Button nextBtn;
    private ProgressBar progressBar;
    private TextView resendOtpTextView;

    private Timer resendTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_otp);

        initializeUI();
        email = getIntent().getExtras().getString("email");
        generatedOtp = getIntent().getExtras().getString("otp");

        if (email == null || generatedOtp == null) {
            Log.e("LoginOtpActivity", "Email or OTP missing in intent.");
            redirectToLogin();
            return;
        }

        nextBtn.setOnClickListener(v -> {
            setInProgress(true);
            verifyOtp();
        });
        resendOtpTextView.setOnClickListener(v -> sendOtpToEmail(email));
        startResendTimer();
    }

    private void initializeUI() {
        otpInput = findViewById(R.id.login_otp);
        nextBtn = findViewById(R.id.login_next_btn);
        progressBar = findViewById(R.id.login_progress_bar);
        resendOtpTextView = findViewById(R.id.resend_otp_textview);
    }

    private void sendOtpToEmail(String email) {
        startResendTimer();
        setInProgress(true);

        generatedOtp = String.valueOf(100000 + new Random().nextInt(900000));

        String subject = "Your OTP Code - ChatApp";
        String body = EmailTemplate.createOtpEmail(generatedOtp);

        AndroidUtil.sendEmail(
                this,
                email,
                subject,
                body,
                "text/html"
        );

        setInProgress(false);
        Toast.makeText(this, "OTP sent to your email", Toast.LENGTH_SHORT).show();
    }

    private void verifyOtp() {
        String enteredOtp = otpInput.getText().toString().trim();

        if (enteredOtp.isEmpty() || enteredOtp.length() < 6) {
            AndroidUtil.showToast(this, "Please enter a valid OTP");
            setInProgress(false);
            return;
        }

        if (enteredOtp.equals(generatedOtp)) {
            authenticateUser();
        } else {
            AndroidUtil.showToast(this, "Invalid OTP. Please try again.");
            setInProgress(false);
        }
    }

    private void authenticateUser() {
        FirebaseAuth auth = FirebaseAuth.getInstance();

        // Example: Sign up the user if they don't exist
        auth.createUserWithEmailAndPassword(email, "someSecurePassword") // Create user if it doesn't exist
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // If sign up is successful, now try to sign in
                        signInUser(auth);
                    } else if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        // If user already exists, try to sign in
                        signInUser(auth);
                    } else {
                        Log.e("LoginOtpActivity", "Sign-up failed: " + task.getException().getMessage());
                        AndroidUtil.showToast(this, "Failed to authenticate user.");
                        setInProgress(false);
                    }
                });
    }

    private void signInUser(FirebaseAuth auth) {
        // Now, sign in the user with the correct password
        auth.signInWithEmailAndPassword(email, "someSecurePassword")
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        if (user != null) {
                            Log.d("LoginOtpActivity", "User authenticated: " + user.getUid());
                            navigateToNextActivity();
                        } else {
                            Log.e("LoginOtpActivity", "Firebase user is null.");
                            AndroidUtil.showToast(this, "Failed to authenticate user.");
                        }
                    } else {
                        Log.e("LoginOtpActivity", "Sign-in failed: " + task.getException().getMessage());
                        AndroidUtil.showToast(this, "Authentication failed. Please try again.");
                    }
                    setInProgress(false);
                });
    }


    private void navigateToNextActivity() {
        Intent intent = new Intent(LoginOtpActivity.this, LoginUsernameActivity.class);
        intent.putExtra("email", email);
        startActivity(intent);
        finish();
    }

    private void redirectToLogin() {
        Intent intent = new Intent(LoginOtpActivity.this, LoginEmailActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void startResendTimer() {
        timeoutSeconds = 60L; // Reset timeout
        resendOtpTextView.setEnabled(false);

        if (resendTimer != null) {
            resendTimer.cancel();
        }

        resendTimer = new Timer();
        resendTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> {
                    if (timeoutSeconds > 0) {
                        resendOtpTextView.setText("Resend OTP in " + timeoutSeconds + " seconds");
                        timeoutSeconds--;
                    } else {
                        resendTimer.cancel();
                        resendOtpTextView.setText("Resend OTP");
                        resendOtpTextView.setEnabled(true);
                    }
                });
            }
        }, 0, 1000);
    }

    private void setInProgress(boolean inProgress) {
        if (inProgress) {
            progressBar.setVisibility(View.VISIBLE);
            nextBtn.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            nextBtn.setVisibility(View.VISIBLE);
        }
    }
}
