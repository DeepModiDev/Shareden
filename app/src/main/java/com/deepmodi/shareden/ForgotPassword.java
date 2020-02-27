package com.deepmodi.shareden;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.deepmodi.shareden.Notifications.NotificationHelper;
import com.deepmodi.shareden.common.Common;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;
import java.util.logging.Logger;

import io.paperdb.Paper;

public class ForgotPassword extends AppCompatActivity {

    TextInputEditText edit_forgot_password_number;
    TextView textView8;
    MaterialButton btn_forgot_password_next,btn_forgot_password_done;
    NotificationHelper helper;
    TextInputLayout textInputLayout2;
    private DatabaseReference reference;
    private FirebaseDatabase firebaseDatabase;
    private int num;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        edit_forgot_password_number = findViewById(R.id.edit_forgot_password_number);
        textView8 = findViewById(R.id.textView8);
        btn_forgot_password_next = findViewById(R.id.btn_forgot_password_next);
        textInputLayout2 = findViewById(R.id.textInputLayout2);
        btn_forgot_password_done = findViewById(R.id.btn_forgot_password_done);


        //Paper init
        Paper.init(this);

        //init notification Helper
        helper = new NotificationHelper(this);

        //firebase init
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference("Users");

        btn_forgot_password_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                        if(!edit_forgot_password_number.getHint().equals("Enter OTP(One time Password)"))
                        {
                            Paper.book().write(Common.USER_FINAL_NUMBER,edit_forgot_password_number.getText().toString());
                            textView8.setText("Step 2 : \nCheck your notification.");
                            edit_forgot_password_number.setHint("Enter OTP(One time Password)");
                            textInputLayout2.setHint("Enter OTP(One time Password)");
                            edit_forgot_password_number.setText("");
                            btn_forgot_password_next.setText("Verify OTP");
                            generatePin(helper);
                            Toast.makeText(ForgotPassword.this, ""+Paper.book().read(Common.USER_FINAL_NUMBER), Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            checkCorrect(Integer.parseInt(edit_forgot_password_number.getText().toString()));
                        }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        btn_forgot_password_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ForgotPassword.this,LogInActivity.class));
                finish();
            }
        });
    }

    private int generatePin(NotificationHelper helper) throws Exception {
        textInputLayout2.setCounterMaxLength(6);
        Random generator = new Random();
        generator.setSeed(System.currentTimeMillis());
        num = generator.nextInt(99999) + 99999;
        if (num < 100000 || num > 999999) {
            num = generator.nextInt(99999) + 99999;
            if (num < 100000 || num > 999999) {
                throw new Exception("Unable to generate PIN at this time..");
            }
        }
        NotificationCompat.Builder nb = helper.getChannel2Builder("Your one time password(OTP).", String.valueOf(num));
        helper.getManager().notify(2,nb.build());
        return num;
    }

    private void checkCorrect(int n)
    {
        if(n == num)
        {
            textInputLayout2.setCounterMaxLength(20);
            textView8.setText("Step 3 : \nCreate new password");
            edit_forgot_password_number.setText("");
            edit_forgot_password_number.setHint("Enter your new password");
            textInputLayout2.setHint("Enter your new password");
            btn_forgot_password_next.setText("finish");
            FirebaseDatabase.getInstance().getReference("Users").child(Paper.book().read(Common.USER_FINAL_NUMBER).toString()).child("userPassword").setValue(edit_forgot_password_number.getText().toString());
            Toast.makeText(ForgotPassword.this, "Your password set Successfully.", Toast.LENGTH_SHORT).show();
        }
        else
        {
            edit_forgot_password_number.requestFocus();
            edit_forgot_password_number.setError("OTP is wrong please enter again.");
        }
    }

    private void setUserNewPassword(String newPassword)
    {
        ///reference.child(Paper.book().read(Common.USER_FINAL_NUMBER).toString()).child()
    }
}
