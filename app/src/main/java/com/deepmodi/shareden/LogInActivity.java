package com.deepmodi.shareden;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.deepmodi.shareden.common.Common;
import com.deepmodi.shareden.model.UserRegisterClass;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import javax.security.auth.login.LoginException;

import io.paperdb.Paper;

public class LogInActivity extends AppCompatActivity {

    TextInputEditText edit_text_phonenumber,edit_text_password;
    AppCompatButton btn_sign_in,register_user;
    FirebaseDatabase database;
    DatabaseReference reference;
    TextView forgot_password;

    private static final int IS_ACTIVITY_PROGRESS_EXISTS = 999;
    private static final int IS_FORGOT_PASSWORD_IS_DONE = 121;
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
        forgot_password = findViewById(R.id.forgot_password);


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

        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                       Intent intent = new Intent(LogInActivity.this,ForgotPassword.class);
                       startActivity(intent);
            }
        });

        register_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogInActivity.this,SignUpActivity.class);
                startActivityForResult(intent,IS_ACTIVITY_PROGRESS_EXISTS);
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
        reference.child(phoneNumber).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserRegisterClass userRegister = dataSnapshot.getValue(UserRegisterClass.class);
                assert userRegister != null;
                try {
                    if(userRegister.getUserNumber().equals(phoneNumber) && userRegister.getUserPassword().equals(password))
                    {
                        Paper.book().write(Common.USER_AUTH_ID,userRegister.getUserId());
                        Paper.book().write(Common.USER_FINAL_NUMBER,userRegister.getUserNumber());
                        Intent intent = new Intent(LogInActivity.this,HomeActivity.class);
                        startActivityForResult(intent,IS_ACTIVITY_PROGRESS_EXISTS);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == IS_ACTIVITY_PROGRESS_EXISTS && resultCode == RESULT_OK)
        {
            LogInActivity.this.finish();
        }
    }
}
