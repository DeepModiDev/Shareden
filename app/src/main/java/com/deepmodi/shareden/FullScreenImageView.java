package com.deepmodi.shareden;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bogdwellers.pinchtozoom.ImageMatrixTouchHandler;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class FullScreenImageView extends AppCompatActivity {

    ImageView imageView;
    ProgressBar progressBar;
    private static final String TAG = "FullScreenImageView";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image_view);
        imageView = findViewById(R.id.fullScreen_imageView);
        progressBar = findViewById(R.id.progressBar_fullscreen_imageview);
        Intent intent = getIntent();
        try {
            Picasso.get().load(intent.getStringExtra("fullScreenImageUrl")).into(imageView, new Callback() {
                @Override
                public void onSuccess() {
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onError(Exception e) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(FullScreenImageView.this, "Unable to load image.", Toast.LENGTH_LONG).show();
                }
            });
        }catch (Exception e)
        {
            Log.e(TAG,e.getMessage());
        }
        try {
            imageView.setOnTouchListener(new ImageMatrixTouchHandler(this));
        }catch (Exception e)
        {
             Log.e(TAG,e.getMessage());
        }

    }
}
