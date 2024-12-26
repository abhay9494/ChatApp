package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.chatapp.adapter.ChatRecyclerAdapter;
import com.example.chatapp.model.ChatMessageModel;
import com.example.chatapp.adapter.SearchUserRecyclerAdapter;
import com.example.chatapp.model.ChatroomModel;
import com.example.chatapp.model.UserModel;
import com.example.chatapp.utils.AndroidUtil;
import com.example.chatapp.utils.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.checkerframework.checker.units.qual.C;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Time;
import java.util.Arrays;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChatActivity extends AppCompatActivity {
    private static final int PICK_FILE_REQUEST = 1;

    private String senderId;
    private String receiverId;

    UserModel otherUser;
    String chatroomId;
    ChatroomModel chatroomModel;
    ChatRecyclerAdapter adapter;

    EditText messageInput;
    ImageButton sendMessageBtn;
    ImageButton backBtn;
    ImageButton attachFileBtn;
    TextView otherUsername;
    RecyclerView recyclerView;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        //get UserModel
        otherUser = AndroidUtil.getUserModelFromIntent(getIntent());
        chatroomId = FirebaseUtil.getChatroomId(FirebaseUtil.currentUserId(),otherUser.getUserId());

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        senderId = user.getUid();

        messageInput = findViewById(R.id.chat_message_input);
        sendMessageBtn = findViewById(R.id.message_send_btn);
        backBtn = findViewById(R.id.back_btn);
        attachFileBtn = findViewById(R.id.attach_file_btn);
        otherUsername = findViewById(R.id.other_username);
        recyclerView = findViewById(R.id.chat_recycler_view);
        imageView = findViewById(R.id.profile_pic_image_view);

        FirebaseUtil.getOtherProfilePicUrl(otherUser.getUserId(), profilePicUrl -> {
            if (profilePicUrl != null) {
                Glide.with(this)
                        .load(profilePicUrl)
                        .circleCrop() // Make the image circular
                        .placeholder(R.drawable.person_icon) // Optional placeholder image
                        .into(imageView);
            }
        });



        backBtn.setOnClickListener((v)->{
            onBackPressed();
        });
        otherUsername.setText(otherUser.getUsername());

        sendMessageBtn.setOnClickListener((v -> {
            String message = messageInput.getText().toString().trim();
            if(message.isEmpty())
                return;
            sendMessageToUser(message);
        }));

        attachFileBtn.setOnClickListener(v -> {
            openFilePicker();
        });

        getOrCreateChatroomModel();
        setupChatRecyclerView();
        determineSenderAndReceiver(chatroomId);
    }

    private void determineSenderAndReceiver(String chatroomId) {
        String currentUserId = FirebaseUtil.currentUserId();
        String[] userIds = chatroomId.split("_");
        if (userIds[0].equals(currentUserId)) {
            senderId = userIds[0];
            receiverId = userIds[1];
        } else {
            senderId = userIds[1];
            receiverId = userIds[0];
        }

//        Log.d("ChatActivity", "Sender ID: " + senderId);
//        Log.d("ChatActivity", "Receiver ID: " + receiverId);
    }

    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(Intent.createChooser(intent, "Select File"), PICK_FILE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FILE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri fileUri = data.getData();
            uploadFileToFirebase(fileUri);
        }
    }

    private void uploadFileToFirebase(Uri fileUri) {
        if (fileUri != null) {
            StorageReference storageRef = FirebaseStorage.getInstance().getReference();
            StorageReference fileRef = storageRef.child("Media/" + System.currentTimeMillis() + "_" + fileUri.getLastPathSegment());

            fileRef.putFile(fileUri)
                    .addOnSuccessListener(taskSnapshot -> fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        sendMessageToUser(uri.toString());
                    }))
                    .addOnFailureListener(e -> {
                        Toast.makeText(ChatActivity.this, "File upload failed", Toast.LENGTH_SHORT).show();
                    });
        }
    }

    void setupChatRecyclerView(){
        Query query = FirebaseUtil.getChatroomMessageReference(chatroomId)
                .orderBy("timestamp", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<ChatMessageModel> options = new FirestoreRecyclerOptions.Builder<ChatMessageModel>()
                .setQuery(query,ChatMessageModel.class).build();

        adapter = new ChatRecyclerAdapter(options,getApplicationContext());
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setReverseLayout(true);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        adapter.startListening();
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                recyclerView.smoothScrollToPosition(0);
            }
        });
    }

    void sendMessageToUser(String message){

        chatroomModel.setLastMessageTimestamp(Timestamp.now());
        chatroomModel.setLastMessageSenderId(FirebaseUtil.currentUserId());
        chatroomModel.setLastMessage(message);
        FirebaseUtil.getChatroomReference(chatroomId).set(chatroomModel);

        ChatMessageModel chatMessageModel = new ChatMessageModel(message,FirebaseUtil.currentUserId(),Timestamp.now());
        FirebaseUtil.getChatroomMessageReference(chatroomId).add(chatMessageModel)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if(task.isSuccessful()){
                            messageInput.setText("");
                            sendNotification(message);
                        }
                    }
                });
    }

    void getOrCreateChatroomModel(){
        FirebaseUtil.getChatroomReference(chatroomId).get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                chatroomModel = task.getResult().toObject(ChatroomModel.class);
                if(chatroomModel==null){
                    //first time chat
                    chatroomModel = new ChatroomModel(
                            chatroomId,
                            Arrays.asList(FirebaseUtil.currentUserId(),otherUser.getUserId()),
                            Timestamp.now(),
                            ""
                    );
                    FirebaseUtil.getChatroomReference(chatroomId).set(chatroomModel);
                }
            }
        });
    }

    public void sendNotification(String messageSMS) {
        String currentUserId = FirebaseUtil.currentUserId();
        String receiverId = getReceiverId(chatroomId, currentUserId);

        // Fetch receiver's details
        FirebaseUtil.getUserDetails(receiverId).get().addOnCompleteListener(receiverTask -> {
            if (receiverTask.isSuccessful()) {
                UserModel receiverUser = receiverTask.getResult().toObject(UserModel.class);
                String receiverFcmToken = receiverUser.getFcmToken();

                // Fetch current user's details
                FirebaseUtil.currentUserDetails().get().addOnCompleteListener(currentTask -> {
                    if (currentTask.isSuccessful()) {
                        UserModel currentUser = currentTask.getResult().toObject(UserModel.class);
                        String senderUsername = currentUser.getUsername();

                        Handler handler = new Handler();
                        handler.postDelayed(() -> {
                            SendNotification notificationsSender = new SendNotification(receiverFcmToken, senderUsername, messageSMS, ChatActivity.this);
                            notificationsSender.SendNotifications();
                            messageInput.setText("");
                        }, 300);
                    } else {
                        Toast.makeText(ChatActivity.this, "Failed to fetch sender's details", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(ChatActivity.this, "Failed to fetch receiver's details", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getReceiverId(String chatroomId, String currentUserId) {
        String[] userIds = chatroomId.split("_");
        if (userIds[0].equals(currentUserId)) {
            return userIds[1];
        } else {
            return userIds[0];
        }
    }
}