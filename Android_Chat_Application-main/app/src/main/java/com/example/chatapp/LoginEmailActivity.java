package com.example.chatapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.chatapp.utils.EmailUtil;

public class LoginEmailActivity extends AppCompatActivity {

    EditText emailInput;
    Button sendOtpBtn;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_email);

        emailInput = findViewById(R.id.login_email);
        sendOtpBtn = findViewById(R.id.send_otp_btn);
        progressBar = findViewById(R.id.login_progress_bar);

        progressBar.setVisibility(View.GONE);

        sendOtpBtn.setOnClickListener(v -> {
            String email = emailInput.getText().toString();
            if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailInput.setError("Enter a valid email address");
                return;
            }

            setInProgress(true);
            EmailUtil.sendOtp(email, otp -> {
                setInProgress(false);
                Intent intent = new Intent(LoginEmailActivity.this, LoginOtpActivity.class);
                intent.putExtra("email", email);
                intent.putExtra("otp", otp);
                finish();
                startActivity(intent);
            }, error -> {
                setInProgress(false);
                emailInput.setError("Failed to send OTP: " + error.getMessage());
            });
        });
    }

    void setInProgress(boolean inProgress) {
        if (inProgress) {
            progressBar.setVisibility(View.VISIBLE);
            sendOtpBtn.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            sendOtpBtn.setVisibility(View.VISIBLE);
        }
    }
}
