package com.example.easychat;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;

public class FullScreenImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image);

        String imageUrl = getIntent().getStringExtra("imageUrl");
        PhotoView photoView = findViewById(R.id.photo_view);
        Glide.with(this).load(imageUrl).into(photoView);
    }
}
