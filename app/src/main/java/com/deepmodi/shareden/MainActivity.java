package com.deepmodi.shareden;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.deepmodi.shareden.common.Common;
import com.deepmodi.shareden.model.UserRegisterClass;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference reference;
    UserRegisterClass register;
    TextView textView;
    Button btn_retry;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar_splash = findViewById(R.id.toolbar_splash);
        setSupportActionBar(toolbar_splash);

        imageView = findViewById(R.id.splash_logo);
        Animation animation = AnimationUtils.loadAnimation(this,R.anim.fade_in);
        imageView.setAnimation(animation);
        animation.start();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.white));
        }

        textView = findViewById(R.id.checkInternet_text);
        btn_retry = findViewById(R.id.btn_retry);
        Paper.init(this);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Users");
        if (Common.checkInternetConnenction(this)) {
            checkUserExistance();
        } else {
            textView.setVisibility(View.VISIBLE);
            btn_retry.setVisibility(View.VISIBLE);
        }

        btn_retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MainActivity.class));
                finish();
            }
        });
    }

    private void checkUserExistance() {
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    register = dataSnapshot.child(String.valueOf(Paper.book().read(Common.USER_FINAL_NUMBER))).getValue(UserRegisterClass.class);
                    assert register != null;
                    if (register.getUserId().equals(Paper.book().read(Common.USER_AUTH_ID))) {
                        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                        startActivityForResult(intent,110);
                    }
                } catch (Exception e) {
                    Intent intent = new Intent(MainActivity.this, LogInActivity.class);
                    startActivityForResult(intent,110);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 110 && resultCode == 110)
        {
            this.finish();
        }
    }
}
