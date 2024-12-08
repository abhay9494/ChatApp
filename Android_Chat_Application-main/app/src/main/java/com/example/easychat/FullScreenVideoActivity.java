package com.example.easychat;

import androidx.appcompat.app.AppCompatActivity;
import android.net.Uri;
import android.os.Bundle;
import android.widget.VideoView;

public class FullScreenVideoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_video);

        String videoUrl = getIntent().getStringExtra("videoUrl");
        VideoView videoView = findViewById(R.id.video_view);
        videoView.setVideoURI(Uri.parse(videoUrl));
        videoView.start();
    }
}
