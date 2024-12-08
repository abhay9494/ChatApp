package com.example.easychat.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easychat.ChatActivity;
import com.example.easychat.R;
import com.example.easychat.model.ChatroomModel;
import com.example.easychat.model.UserModel;
import com.example.easychat.utils.AndroidUtil;
import com.example.easychat.utils.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class RecentChatRecyclerAdapter extends FirestoreRecyclerAdapter<ChatroomModel, RecentChatRecyclerAdapter.ChatroomModelViewHolder> {

    Context context;

    public RecentChatRecyclerAdapter(@NonNull FirestoreRecyclerOptions<ChatroomModel> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ChatroomModelViewHolder holder, int position, @NonNull ChatroomModel model) {
        FirebaseUtil.getOtherUserFromChatroom(model.getUserIds())
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String lastMessageSenderId = model.getLastMessageSenderId();
                        String currentUserId = FirebaseUtil.currentUserId();
                        boolean lastMessageSentByMe = lastMessageSenderId != null && lastMessageSenderId.equals(currentUserId);

                        UserModel otherUserModel = task.getResult().toObject(UserModel.class);

                        if (otherUserModel != null) {
                            FirebaseUtil.getOtherProfilePicStorageRef(otherUserModel.getUserId()).getDownloadUrl()
                                    .addOnCompleteListener(t -> {
                                        if (t.isSuccessful()) {
                                            Uri uri = t.getResult();
                                            if (uri != null) {
                                                AndroidUtil.setProfilePic(context, uri, holder.profilePic);
                                            } else {
                                                holder.profilePic.setImageResource(R.drawable.person_icon);
                                            }
                                        } else {
                                            // Set a default profile picture
                                            holder.profilePic.setImageResource(R.drawable.person_icon);
                                        }
                                    }).addOnFailureListener(e -> {
                                        // Handle the failure case and set a default image
                                        holder.profilePic.setImageResource(R.drawable.person_icon);
                                    });

                            holder.usernameText.setText(otherUserModel.getUsername());
                            String lastMessage = model.getLastMessage();
                            if ("image".equals(model.getLastMessageType())) {
                                lastMessage = "Image";
                            } else if ("video".equals(model.getLastMessageType())) {
                                lastMessage = "Video";
                            }
                            holder.lastMessageText.setText(lastMessageSentByMe ? "You: " + lastMessage : lastMessage);
                            holder.lastMessageTime.setText(FirebaseUtil.timestampToString(model.getLastMessageTimestamp()));

                            holder.itemView.setOnClickListener(v -> {
                                // Navigate to chat activity
                                Intent intent = new Intent(context, ChatActivity.class);
                                AndroidUtil.passUserModelAsIntent(intent, otherUserModel);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                            });
                        }
                    }
                });
    }

    @NonNull
    @Override
    public ChatroomModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recent_chat_recycler_row, parent, false);
        return new ChatroomModelViewHolder(view);
    }

    class ChatroomModelViewHolder extends RecyclerView.ViewHolder {
        TextView usernameText;
        TextView lastMessageText;
        TextView lastMessageTime;
        ImageView profilePic;

        public ChatroomModelViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameText = itemView.findViewById(R.id.user_name_text);
            lastMessageText = itemView.findViewById(R.id.last_message_text);
            lastMessageTime = itemView.findViewById(R.id.last_message_time_text);
            profilePic = itemView.findViewById(R.id.profile_pic_image_view);
        }
    }
}
