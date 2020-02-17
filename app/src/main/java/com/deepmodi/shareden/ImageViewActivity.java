package com.deepmodi.shareden;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.bogdwellers.pinchtozoom.ImageMatrixTouchHandler;
import com.squareup.picasso.Picasso;

public class ImageViewActivity extends AppCompatActivity {

    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);
        Toolbar toolbar = findViewById(R.id.toolbar_imageview);
        setSupportActionBar(toolbar);

        imageView = findViewById(R.id.placeholder_imageview);
        Intent intent = getIntent();
        try {
            if(intent.getBooleanExtra("enable",false))
            {
                Picasso.get().load(intent.getStringExtra("path")).error(R.mipmap.ic_launcher).into(imageView);
            }
            else
            {
                Picasso.get().load("file://"+intent.getStringExtra("path")).error(R.mipmap.ic_launcher).into(imageView);
            }
        }catch (Exception e)
        {

        }
        imageView.setOnTouchListener(new ImageMatrixTouchHandler(this));
       // Picasso.get().load(intent.getStringExtra("HomePath")).error(R.mipmap.ic_launcher).into(imageView);
    }
}
