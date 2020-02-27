package com.deepmodi.shareden;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.deepmodi.shareden.common.Common;
import com.deepmodi.shareden.model.UserRegisterClass;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class EditUserActivity extends AppCompatActivity {

    Button btn;
    EditText edit_user_name;
    UserRegisterClass register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference red = database.getReference("Users");
        Paper.init(this);
        red.child(Paper.book().read(Common.USER_FINAL_NUMBER).toString()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                register = dataSnapshot.getValue(UserRegisterClass.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        edit_user_name = findViewById(R.id.edit_user_name_edit_profile);
        edit_user_name.setText(register.getUserName());

        btn = findViewById(R.id.btn_update_edit_profile);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register.setUserName(edit_user_name.getText().toString());
                red.child(Paper.book().read(Common.USER_FINAL_NUMBER).toString()).setValue(register);
            }
        });
    }
}
