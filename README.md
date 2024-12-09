# Firebase Chat App  

Welcome to the Firebase Chat App repository! This is a real-time chat application for Android, enabling users to communicate securely and efficiently. The app leverages Firebase services for authentication, data storage, and notifications, ensuring a seamless user experience.  

## Key Features  

- **OTP-Based Login:** Secure user authentication using Firebase Authentication.  
- **Real-Time Communication:** Chat functionality powered by Firebase Realtime Database for instant updates.  
- **Push Notifications:** Firebase Cloud Messaging integration to notify users of new messages.  
- **User Search:** Search functionality to find and connect with other users.  
- **Profile Management:** View and edit user profiles.  

## File Overview  

### Activity Files  

- **`ChatActivity.java`**: Manages individual chat conversations.  
- **`LoginOtpActivity.java`**: Handles OTP-based user authentication.  
- **`LoginPhoneNumberActivity.java`**: Facilitates phone number login.  
- **`LoginUsernameActivity.java`**: Manages username-based login.  
- **`MainActivity.java`**: The entry point and navigation hub of the app.  
- **`SearchUserActivity.java`**: Enables searching for users to start chats.  
- **`SplashActivity.java`**: Displays the splash screen during app initialization.  

### Fragment Files  

- **`ChatFragment.java`**: Displays and manages chat UI and logic.  
- **`ProfileFragment.java`**: Manages user profile display and editing.  
- **`SearchUserFragment.java`**: Shows search results and options to start a chat.  

### Service File  

- **`FCMNotificationService.java`**: Handles push notifications using Firebase Cloud Messaging.  

## Development Highlights  

- **OTP Integration:** Used Firebase Authentication to enable secure OTP-based login.  
- **Database Management:** Firebase Realtime Database stores all user and chat data securely.  
- **Prompt Engineering:** Integrated ChatGPT prompts to enhance functionality and improve user interaction.  
- **Frontend Design:** Developed the UI using Android Studio and Java for smooth and intuitive navigation.  

## Getting Started  

To use this app:  

1. Clone or download this repository.  
2. Set up a Firebase project and add the `google-services.json` file to your app.  
3. Build and run the app on your Android device or emulator.  

## Notes  

- This app provides a foundational structure for a Firebase-based chat application. Feel free to extend and customize it to fit your needs.  
- Ensure that you implement robust security measures for user data and authentication.  

For more details, refer to:  
- [Firebase Documentation](https://firebase.google.com/docs)  
- [Android Documentation](https://developer.android.com/docs)  

Happy coding!
