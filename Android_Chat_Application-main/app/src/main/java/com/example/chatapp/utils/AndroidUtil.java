package com.example.chatapp.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.chatapp.R;
import com.example.chatapp.model.UserModel;
import com.google.firebase.firestore.auth.User;

import android.os.StrictMode;
import android.util.Log;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class AndroidUtil {

   public static  void showToast(Context context,String message){
       Toast.makeText(context,message,Toast.LENGTH_LONG).show();
    }

    public static void passUserModelAsIntent(Intent intent, UserModel model) {
        intent.putExtra("username", model.getUsername());
        intent.putExtra("email", model.getEmail());
        intent.putExtra("userId", model.getUserId());
        intent.putExtra("fcmToken", model.getFcmToken());
    }

    public static UserModel getUserModelFromIntent(Intent intent) {
        UserModel userModel = new UserModel();
        userModel.setUsername(intent.getStringExtra("username"));
        userModel.setEmail(intent.getStringExtra("email"));
        userModel.setUserId(intent.getStringExtra("userId"));
        userModel.setFcmToken(intent.getStringExtra("fcmToken"));
        return userModel;
    }

    public static void setProfilePic(Context context, Uri imageUri, ImageView imageView) {
        Glide.with(context)
                .load(imageUri)
                .placeholder(R.drawable.person_icon) // Default image if loading takes time
                .error(R.drawable.person_icon) // Default image on error
                .into(imageView);
    }



    public static void sendEmail(Context context, String to, String subject, String body, String contentType) {
        new Thread(() -> {
            try {
                SMTPUtil.sendEmail(to, subject, body, contentType); // Pass contentType to SMTPUtil
            } catch (MessagingException e) {
                showToast(context, "Failed to send email: " + e.getMessage());
            }
        }).start();
    }

    // Overloaded method for backward compatibility (defaults to plain text)
    public static void sendEmail(Context context, String to, String subject, String body) {
        sendEmail(context, to, subject, body, "text/plain"); // Defaults to plain text
    }


//    public static void showToast(Context context, String message) {
//        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
//    }

}
