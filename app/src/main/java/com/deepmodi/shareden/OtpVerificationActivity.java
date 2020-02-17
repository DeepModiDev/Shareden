package com.deepmodi.shareden;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.arch.core.executor.TaskExecutor;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.deepmodi.shareden.common.Common;
import com.deepmodi.shareden.model.UserRegister;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import io.paperdb.Paper;

public class OtpVerificationActivity extends AppCompatActivity {

    String USER_NAME,USER_NUMBER,USER_PASSWORD,USER_GENDER,USER_LEVEL;
    TextView textView_welcome;
    FirebaseDatabase database;
    DatabaseReference reference;
    EditText edit_1,edit_2,edit_3,edit_4,edit_5,edit_6;

    Button btn_final_register;
    private String VERIFICATION_ID;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);

        //firebase init
        Paper.init(this);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Users");

        textView_welcome = findViewById(R.id.welcome_text_view);

        Intent intent = getIntent();
        USER_NAME = intent.getStringExtra("userName");
        USER_NUMBER = intent.getStringExtra("userNumber");
        USER_PASSWORD = intent.getStringExtra("userPassword");
        USER_GENDER = intent.getStringExtra("userGender");
        USER_LEVEL = intent.getStringExtra("userLevel");

        textView_welcome.setText("Welcome, "+USER_NAME+"\nThis is the last step of the registration process.");

        edit_1 = findViewById(R.id.edit_1);
        edit_2 = findViewById(R.id.edit_2);
        edit_3 = findViewById(R.id.edit_3);
        edit_4 = findViewById(R.id.edit_4);
        edit_5 = findViewById(R.id.edit_5);
        edit_6 = findViewById(R.id.edit_6);

        btn_final_register = findViewById(R.id.btn_final_register);

        getVerificationCode(USER_NUMBER);

        btn_final_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code =
                        edit_1.getText().toString().trim() +
                        edit_2.getText().toString().trim() +
                        edit_3.getText().toString().trim() +
                        edit_4.getText().toString().trim() +
                        edit_5.getText().toString().trim() +
                        edit_6.getText().toString().trim();
                if (code.isEmpty() || code.length() < 6) {
                    edit_1.setError("Enter valid code");
                    edit_1.requestFocus();
                } else {
                    verifyVerificationCode(code);
                }
            }
        });
    }

    /**
     * THIS FUNCTION WILL BE EXECUTED FIRST.
     * @param userNumber
     */
    private void getVerificationCode(String userNumber)
    {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91"+userNumber,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallBacks //THIS LINE WILL BE CALL ANOTHER METHOD.
        );
    }

    /**
     * THIS METHOD WILL BE EXECUTED SECOND
     */
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            final String code = phoneAuthCredential.getSmsCode();

            if(code!=null)
            {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        edit_1.setText(String.valueOf(code.charAt(0)));
                    }
                },500);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        edit_2.setText(String.valueOf(code.charAt(1)));
                    }
                },600);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        edit_3.setText(String.valueOf(code.charAt(2)));
                    }
                },700);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        edit_4.setText(String.valueOf(code.charAt(3)));
                    }
                },800);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        edit_5.setText(String.valueOf(code.charAt(4)));
                    }
                },700);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        edit_6.setText(String.valueOf(code.charAt(5)));
                    }
                },800);

                verifyVerificationCode(code); //THIS LINE WILL CALL THIRD METHOD
            }
            else
            {
                edit_1.setText("");
                edit_2.setText("");
                edit_3.setText("");
                edit_4.setText("");
                edit_5.setText("");
                edit_6.setText("");
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            resendVerificationCode(USER_NUMBER);//THIS LINE WILL CALL RE SEND OTP METHOD AND ALL THE FOUR METHOD
                                                //EXECUTE AGAIN THIS IS A RECURSIVE PROCESS.
        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            VERIFICATION_ID = s;
            super.onCodeSent(s, forceResendingToken);
        }
    };


    private void resendVerificationCode(String number)
    {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91"+number,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallBacks
        );
    }

    /**
     * THIS METHOD WILL BE EXECUTE THIRD.
     * @param userCode
     */
    private void verifyVerificationCode(String userCode)
    {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(VERIFICATION_ID,userCode);
        try {
                signInWithphoneCredential(credential);//THIS LINE WILL CALL FOURTH METHOD.
        }catch(NullPointerException e)
        {
            Log.d("otpverification", Objects.requireNonNull(e.getMessage()));
        }
    }


    /**
     * THIS METHOD WILL BE EXECUTE FOURTH AND THIS IS THE FINAL METHOD
     * @param mCredential
     */
    private void signInWithphoneCredential(PhoneAuthCredential mCredential)
    {
        mAuth.signInWithCredential(mCredential)
                .addOnCompleteListener(OtpVerificationActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser firebase_user = mAuth.getCurrentUser();
                            if(firebase_user != null)
                            {
                                Paper.book().write(Common.USER_FINAL_NUMBER,USER_NUMBER);
                                Paper.book().write(Common.USER_FINAL_PASSWORD,USER_PASSWORD);
                                Paper.book().write(Common.USER_AUTH_ID,firebase_user.getUid());

                                UserRegister userRegister = new UserRegister(USER_NAME,USER_NUMBER,USER_PASSWORD,USER_GENDER,USER_LEVEL,firebase_user.getUid());
                                reference.child(USER_NUMBER).setValue(userRegister);

                                startActivity(new Intent(OtpVerificationActivity.this,HomeActivity.class));
                                finish();
                            }
                        } else {

                            //verification unsuccessful.. display an error message
                            String message = "Somthing is wrong, you entered wrong OTP";
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(OtpVerificationActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

    }
}
