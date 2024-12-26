package com.example.chatapp.utils;

import android.os.AsyncTask;

import java.util.Random;

public class EmailUtil {

    public static void sendOtp(String email, OnOtpSentListener listener, OnErrorListener errorListener) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                try {
                    // Generate OTP
                    String otp = String.format("%06d", new Random().nextInt(999999));

                    // Send email using SMTP
                    String subject = "Your OTP Code - ChatApp";
                    String body = EmailTemplate.createOtpEmail(otp);
//                    String body = "Your OTP is: " + otp;

                    SMTPUtil.sendEmail(email, subject, body, "text/html"); // SMTPUtil handles email sending
                    return otp;
                } catch (Exception e) {
                    errorListener.onError(e);
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String otp) {
                if (otp != null) {
                    listener.onOtpSent(otp);
                }
            }
        }.execute();
    }

    public interface OnOtpSentListener {
        void onOtpSent(String otp);
    }

    public interface OnErrorListener {
        void onError(Exception e);
    }
}
