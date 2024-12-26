package com.example.chatapp.utils;

public class EmailTemplate {

    public static String createOtpEmail(String otp) {
        return
                "<html>" +
                        "<body style='font-family: Arial, sans-serif; text-align: center; padding: 40px; background-color: #f4f4f9; color: #333;'>" +
                        "<div style='border: 1px solid #ddd; border-radius: 15px; max-width: 450px; margin: auto; background-color: #ffffff; box-shadow: 0 4px 8px rgba(0,0,0,0.1); overflow: hidden;'>" +
                        "<div style='background-color: #F2F2F2; padding: 20px;'>" +
                        "<img src='https://res.cloudinary.com/chatapp-by-abhay/image/upload/v1734852323/launcher_icon-removebg-preview_r6vnfo.png' alt='App Logo' style='width: 70px; height: 70px; margin-bottom: 10px;' />" +
                        "<h1 style='font-size: 24px; color: #7b56e1; margin: 0;'>ChatApp</h1>" +
                        "</div>" +
                        "<div style='padding: 20px;'>" +
                        "<p style='font-size: 18px; color: #555; margin: 0 0 10px;'>Your One-Time Password (OTP) for login is:</p>" +
                        "<p style='font-size: 36px; font-weight: bold; color: #7b56e1; margin: 20px 0;'>"+ otp +"</p>" +
                        "<p style='font-size: 14px; color: #888;'>This OTP is valid for <strong>5 minutes</strong>. Please do not share it with anyone.</p>" +
                        "<hr style='border: none; height: 1px; background-color: #eee; margin: 20px 0;'/>" +
                        "<p style='font-size: 14px; color: #aaa;'>If you did not request this, please ignore this email.</p>" +
                        "</div>" +
                        "<div style='background-color: #f9f9fc; padding: 15px;'>" +
                        "<p style='font-size: 12px; color: #999;'>ChatApp © 2024. All Rights Reserved.</p>" +
                        "</div>" +
                        "</div>" +
                        "</body>" +
                        "</html>";

    }
}