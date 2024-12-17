package com.example.chatapp.utils;

import android.content.Context;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.List;

public class FirebaseUtil {

    // Get current user ID
    public static String currentUserId() {
        return FirebaseAuth.getInstance().getUid();
    }

    // Check if user is logged in
    public static boolean isLoggedIn() {
        return currentUserId() != null;
    }

    // Reference to current user's details in Firestore
    public static DocumentReference currentUserDetails() {
        return FirebaseFirestore.getInstance().collection("users").document(currentUserId());
    }

    public static DocumentReference getUserDetails(String userId) {
        return FirebaseFirestore.getInstance().collection("users").document(userId);
    }

    // Reference to all users collection
    public static CollectionReference allUserCollectionReference() {
        return FirebaseFirestore.getInstance().collection("users");
    }

    // Reference to a specific chatroom
    public static DocumentReference getChatroomReference(String chatroomId) {
        return FirebaseFirestore.getInstance().collection("chatrooms").document(chatroomId);
    }

    // Reference to messages within a chatroom
    public static CollectionReference getChatroomMessageReference(String chatroomId) {
        return getChatroomReference(chatroomId).collection("chats");
    }

    // Generate chatroom ID
    public static String getChatroomId(String userId1, String userId2) {
        if (userId1.hashCode() < userId2.hashCode()) {
            return userId1 + "_" + userId2;
        } else {
            return userId2 + "_" + userId1;
        }
    }

    // Reference to all chatrooms collection
    public static CollectionReference allChatroomCollectionReference() {
        return FirebaseFirestore.getInstance().collection("chatrooms");
    }

    // Get the other user's reference from a chatroom
    public static DocumentReference getOtherUserFromChatroom(List<String> userIds) {
        if (userIds.get(0).equals(FirebaseUtil.currentUserId())) {
            return allUserCollectionReference().document(userIds.get(1));
        } else {
            return allUserCollectionReference().document(userIds.get(0));
        }
    }

    // Convert Timestamp to String (HH:MM format)
    public static String timestampToString(Timestamp timestamp) {
        return new SimpleDateFormat("HH:mm").format(timestamp.toDate());
    }

    // Logout user
    public static void logout(Context context) {
        FirebaseAuth.getInstance().signOut();
    }

    // Reference to current user's profile picture in Firebase Storage
    public static StorageReference getCurrentProfilePicStorageRef() {
        return FirebaseStorage.getInstance().getReference()
                .child("profile_pic")
                .child(currentUserId());
    }

    // Reference to another user's profile picture in Firebase Storage
//    public static StorageReference getOtherProfilePicStorageRef(String otherUserId) {
//        return FirebaseStorage.getInstance().getReference()
//                .child("profile_pic")
//                .child(otherUserId);
//    }

//    public static void getOtherProfilePicUrl(String userId, OnSuccessListener<String> listener) {
//        allUserCollectionReference()
//                .document(userId)
//                .get()
//                .addOnSuccessListener(documentSnapshot -> {
//                    if (documentSnapshot.exists()) {
//                        String profilePicUrl = documentSnapshot.getString("profilePicUrl");
//                        if (profilePicUrl != null) {
//                            listener.onSuccess(profilePicUrl);
//                        }
//                    }
//                });
//    }

//    public static void getOtherProfilePicUrlFromCloudinary(String userId, OnSuccessListener<String> onSuccess) {
//        allUserCollectionReference()
//                .document(userId)
//                .get()
//                .addOnSuccessListener(documentSnapshot -> {
//                    if (documentSnapshot.exists()) {
//                        String cloudinaryUrl = documentSnapshot.getString("cloudinaryProfilePicUrl");
//                        if (cloudinaryUrl != null && !cloudinaryUrl.isEmpty()) {
//                            onSuccess.onSuccess(cloudinaryUrl);
//                        }
//                    }
//                });
//    }


    public static void getOtherProfilePicUrl(String userId, OnSuccessListener<String> onSuccess) {
        allUserCollectionReference()
                .document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String profileImageUrl = documentSnapshot.getString("profileImage");
                        if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
                            onSuccess.onSuccess(profileImageUrl);
                        }
                    }
                });
    }





}
