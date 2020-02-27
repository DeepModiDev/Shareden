package com.deepmodi.shareden;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.deepmodi.shareden.common.Common;
import com.deepmodi.shareden.model.ChatRequest;
import com.deepmodi.shareden.model.UserRegisterClass;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import io.paperdb.Paper;
import okhttp3.Request;

public class ShowUserFullDetails extends AppCompatActivity {

    private Button btn_send_follow_request;
    private UserRegisterClass registerUser;
    private String usrProfileImage;
    private String usrProfileLevel;
    private String usrProfileName;
    private String userPhoneNumber;
    private String usrBio;
    private String usrNoBooksDonated;
    private ChatRequest request;
    private RoundedImageView roundedImageView;
    FirebaseDatabase databaseRequest;
    DatabaseReference referenceRequest;

    TextView no_book_donated,display_user_name,display_user_occupation,display_user_phone,display_user_bio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_user_full_details);

        Intent intent = getIntent();

        databaseRequest = FirebaseDatabase.getInstance();
        referenceRequest = databaseRequest.getReference("UserRequests");

        usrProfileImage = intent.getStringExtra("userImage");
        usrProfileName = intent.getStringExtra("userName");
        usrProfileLevel = intent.getStringExtra("userLevel");
        userPhoneNumber = intent.getStringExtra("userNumber");


        btn_send_follow_request = findViewById(R.id.btn_send_follow_request);
        no_book_donated = findViewById(R.id.no_book_donated);
        display_user_name = findViewById(R.id.display_user_name);
        display_user_occupation = findViewById(R.id.display_user_occupation);
        display_user_phone = findViewById(R.id.display_user_phone);
        display_user_bio = findViewById(R.id.display_user_bio);
        roundedImageView = findViewById(R.id.profile_img);
        UserDataLoad();

        display_user_name.setText(usrProfileName);
        display_user_occupation.setText(usrProfileLevel);
        display_user_phone.setText(userPhoneNumber);

        Picasso.get().load(usrProfileImage).error(R.drawable.usr_img).into(roundedImageView);
        btn_send_follow_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //referenceUserRequest.child(model.getUserPhoneNumber()).child("userFollowStatus").setValue("false");
                request = new ChatRequest(
                        Paper.book().read(Common.USER_FINAL_NUMBER).toString(),
                        registerUser.getUserName(),
                        registerUser.getUserLevel(),
                        registerUser.getUserImg(),
                        userPhoneNumber,
                        usrProfileName,
                        usrProfileLevel,
                        usrProfileImage,
                        "false");
                btn_send_follow_request.setText("Requested");
                //FirebaseDatabase.getInstance().getReference("UserPost").child(model.getPostId()).child("userFollowStatus").setValue("false");
                referenceRequest.child(Paper.book().read(Common.USER_FINAL_NUMBER).toString()).child("ToRequests").child(userPhoneNumber).setValue(request);
                referenceRequest.child(userPhoneNumber).child("MyRequests").child(Paper.book().read(Common.USER_FINAL_NUMBER).toString()).setValue(request);
            }
        });
    }

    private void UserDataLoad() {
        FirebaseDatabase.getInstance().getReference("Users").child(Paper.book().read(Common.USER_FINAL_NUMBER).toString()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                registerUser = dataSnapshot.getValue(UserRegisterClass.class);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        DatabaseReference.goOffline();
    }
}
