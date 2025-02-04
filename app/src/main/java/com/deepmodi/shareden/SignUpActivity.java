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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.deepmodi.shareden.ViewModel.FirebaseCheckUserViewModel;
import com.deepmodi.shareden.model.CollegeUniqueId;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Collection;

public class SignUpActivity extends AppCompatActivity {

    RadioGroup radioGroup_gender,radioGroup_level;
    RadioButton appCompatRadioButton_gender,appCompatRadioButton_level;
    MaterialButton btn_register;
    AppCompatButton sign_in_user;
    TextInputLayout temp_textInput_others_users;
    TextInputEditText edit_signup_text_phonenumber,edit_signup_text_password,edit_signup_text_password_varify;
    TextInputEditText edit_signup_text_name,edit_text_signup_unique_id,edit_text_signup_others;
    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseCheckUserViewModel viewModel;
    private CollegeUniqueId collegeUniqueId;

    private static final int REQUEST_TO_CLOSE_ACTIVITY = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        loadUniqueId();

        //setStatus bar color
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.white));
        }

        radioGroup_gender = findViewById(R.id.radio_group);
        radioGroup_level = findViewById(R.id.radio_group_level);
        sign_in_user = findViewById(R.id.sign_in_user);
        btn_register = findViewById(R.id.btn_register);
        edit_signup_text_name = findViewById(R.id.edit_signup_text_name);
        edit_signup_text_phonenumber = findViewById(R.id.edit_text_signup_phonenumber);
        edit_signup_text_password = findViewById(R.id.edit_signup_text_password);
        edit_signup_text_password_varify = findViewById(R.id.edit_signup_text_password_varify);
        edit_text_signup_unique_id = findViewById(R.id.edit_text_signup_unique_id);
        edit_text_signup_others = findViewById(R.id.edit_text_signup_others);
        temp_textInput_others_users = findViewById(R.id.temp_textInput_others_users);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Users");

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("SignUpActivity",collegeUniqueId.getUniqueId());
                if (collegeUniqueId.getUniqueId().equals(edit_text_signup_unique_id.getText().toString())) {
                    if (!edit_signup_text_name.getText().toString().isEmpty()) {
                        if (edit_signup_text_phonenumber.getText().toString().length() == 10) {
                            if (edit_signup_text_password.getText().toString().length() >= 6) {
                                int radioLevel = radioGroup_level.getCheckedRadioButtonId();
                                appCompatRadioButton_level = findViewById(radioLevel);

                                int radioId = radioGroup_gender.getCheckedRadioButtonId();
                                appCompatRadioButton_gender = findViewById(radioId);

                                if (edit_signup_text_password.getText().toString().equals(edit_signup_text_password_varify.getText().toString())) {

                                    if(appCompatRadioButton_level.getText().equals("Others"))
                                    {
                                        temp_textInput_others_users.setVisibility(View.VISIBLE);
                                        if(!edit_text_signup_others.getText().toString().isEmpty()) {
                                            reference.addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    if (dataSnapshot.hasChild(edit_signup_text_phonenumber.getText().toString())) {
                                                        edit_signup_text_phonenumber.requestFocus();
                                                        edit_signup_text_phonenumber.setError("Phone number is already reqistered please login.");
                                                    } else {
                                                        registerProcess(
                                                                edit_signup_text_name.getText().toString(),
                                                                edit_signup_text_phonenumber.getText().toString(),
                                                                edit_signup_text_password.getText().toString(),
                                                                String.valueOf(appCompatRadioButton_gender.getText()),
                                                                edit_text_signup_others.getText().toString());
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            });
                                        }else{
                                            edit_text_signup_others.requestFocus();
                                            edit_text_signup_others.setError("Please enter required details.");
                                        }
                                    }
                                    else
                                    {
                                        reference.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.hasChild(edit_signup_text_phonenumber.getText().toString())) {
                                                    edit_signup_text_phonenumber.requestFocus();
                                                    edit_signup_text_phonenumber.setError("Phone number is already reqistered please login.");
                                                } else {
                                                    registerProcess(
                                                            edit_signup_text_name.getText().toString(),
                                                            edit_signup_text_phonenumber.getText().toString(),
                                                            edit_signup_text_password.getText().toString(),
                                                            String.valueOf(appCompatRadioButton_gender.getText()),
                                                            String.valueOf(appCompatRadioButton_level.getText()));
                                                }
                                            }
                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                                    }
                                } else {
                                    edit_signup_text_password_varify.requestFocus();
                                    edit_signup_text_password_varify.setError("Password doesn't match please enter again.");
                                }
                            } else {
                                edit_signup_text_password.requestFocus();
                                edit_signup_text_password.setError("Password length must be greater than or equals to 6");
                            }
                        } else {
                            edit_signup_text_phonenumber.requestFocus();
                            edit_signup_text_phonenumber.setError("Number length must be 10");
                        }
                    } else {
                        edit_signup_text_name.requestFocus();
                        edit_signup_text_name.setError("Please enter your name");
                    }
                }
                else {
                    edit_text_signup_unique_id.requestFocus();
                    edit_text_signup_unique_id.setError("You entered wrong id code.");
                    Toast.makeText(SignUpActivity.this, "You are not member of the college.\n or Maybe you entered wrong id code.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        sign_in_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this,LogInActivity.class));
                finish();
            }
        });
    }

    public void checkButton(View view) {
        int radioId = radioGroup_gender.getCheckedRadioButtonId();
        appCompatRadioButton_gender = view.findViewById(radioId);
        Toast.makeText(this, ""+appCompatRadioButton_gender.getText(), Toast.LENGTH_SHORT).show();
    }

    public void checkLevelButton(View v)
    {
        int radioLevel = radioGroup_level.getCheckedRadioButtonId();
        appCompatRadioButton_level = findViewById(radioLevel);
        Toast.makeText(this, ""+appCompatRadioButton_level.getText(), Toast.LENGTH_SHORT).show();
        if(appCompatRadioButton_level.getText().equals("Others"))
        {
            temp_textInput_others_users.setVisibility(View.VISIBLE);
        }
    }

    private void registerProcess(String userName,String userNumber,String userPassword,String userGender,String userLevel)
    {
        Intent intent = new Intent(SignUpActivity.this,OtpVerificationActivity.class);
        intent.putExtra("userName",userName);
        intent.putExtra("userNumber",userNumber);
        intent.putExtra("userPassword",userPassword);
        intent.putExtra("userGender",userGender);
        intent.putExtra("userLevel",userLevel);
        startActivityForResult(intent,REQUEST_TO_CLOSE_ACTIVITY);
    }

    private void loadUniqueId() {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference("CollegeId").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                collegeUniqueId = dataSnapshot.getValue(CollegeUniqueId.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onStop()
    {
        super.onStop();
        this.setResult(999);
    }


    @Override
    public void onDestroy()
    {
        super.onDestroy();
        this.setResult(999);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_TO_CLOSE_ACTIVITY && resultCode == RESULT_OK)
        {
            SignUpActivity.this.finish();
        }
    }
}