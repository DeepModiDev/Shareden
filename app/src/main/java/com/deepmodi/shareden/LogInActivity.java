package com.deepmodi.shareden;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.deepmodi.shareden.common.Common;
import com.deepmodi.shareden.model.UserRegister;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import javax.security.auth.login.LoginException;

import io.paperdb.Paper;

public class LogInActivity extends AppCompatActivity {

    EditText edit_text_phonenumber,edit_text_password;
    Button btn_sign_in,register_user;
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.white));
        }

        //firebase init
        Paper.init(this);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Users");

        edit_text_phonenumber = findViewById(R.id.edit_text_phonenumber);
        edit_text_password = findViewById(R.id.edit_text_password);
        btn_sign_in = findViewById(R.id.btn_sign_in);
        register_user = findViewById(R.id.register_user);


        btn_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(edit_text_phonenumber.getText().toString().length() == 10 && !edit_text_phonenumber.getText().toString().isEmpty())
                {
                    if(!edit_text_password.getText().toString().isEmpty() && edit_text_password.getText().toString().length() >= 6)
                    {
                        checkUserExistance();
                    }else
                    {
                        edit_text_password.requestFocus();
                        edit_text_password.setError("Password length must be grater than or equals to 6");
                    }
                }
                else
                {
                    edit_text_phonenumber.requestFocus();
                    edit_text_phonenumber.setError("Number length must be 10");
                }

            }
        });

        register_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogInActivity.this,SignUpActivity.class);
                startActivity(intent);
                finish();

            }
        });
    }

    private void checkUserExistance()
    {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(edit_text_phonenumber.getText().toString()))
                {
                    userLoginProcess(edit_text_phonenumber.getText().toString(),edit_text_password.getText().toString());
                }
                else
                {
                    edit_text_phonenumber.requestFocus();
                    edit_text_phonenumber.setError("Your phone number is not registered.");
                    Toast.makeText(LogInActivity.this, "User(phone number) is not exist.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void userLoginProcess(final String phoneNumber, final String password)
    {
        reference.child(phoneNumber).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserRegister userRegister = dataSnapshot.getValue(UserRegister.class);
                assert userRegister != null;
                try {
                    if(userRegister.getUserNumber().equals(phoneNumber) && userRegister.getUserPassword().equals(password))
                    {
                        Paper.book().write(Common.USER_AUTH_ID,userRegister.getUserId());
                        Paper.book().write(Common.USER_FINAL_NUMBER,userRegister.getUserNumber());
                        startActivity(new Intent(LogInActivity.this,HomeActivity.class));
                        finish();
                    }
                    else
                    {
                        edit_text_password.requestFocus();
                        edit_text_password.setError("Password is wrong.");
                        Toast.makeText(LogInActivity.this, "Password is wrong.", Toast.LENGTH_SHORT).show();
                    }
                }catch(Exception e)
                {
                    Log.e("LoginException : ",e.getMessage());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
