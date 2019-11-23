package com.example.letstalk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.letstalk.AppConstant.AppConstant;
import com.example.letstalk.LTModel.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PhoneVerification extends AppCompatActivity {

    private static final String TAG = "PhoneVerification";

    private String verificationId;

    private FirebaseAuth mAuth;

    @BindView(R.id.txtenterotp) EditText txtOtp;

    private ProgressBar progressBar;

    DatabaseReference databaseUser;

    String phoneNumber;

    private SharedPreferences mSharedPreferences;

    Context mcontext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_verification);

        phoneNumber = getIntent().getStringExtra("phonenumber");

        progressBar = findViewById(R.id.progressbar);

        sentVerificationCode(phoneNumber);

        mAuth = FirebaseAuth.getInstance();

        //txtOtp = findViewById(R.id.txtenterotp);

        databaseUser = FirebaseDatabase.getInstance().getReference("users");

        ButterKnife.bind(this);

        mSharedPreferences = getSharedPreferences(AppConstant.PREFERENCE_FILE_NAME,MODE_PRIVATE);

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void signIn(View view){
        String code = txtOtp.getText().toString().trim();
        if(code.isEmpty() || code.length()<6){
            txtOtp.setError("Enter OTP...");
            txtOtp.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        verifyCode(code);

    }

    private void verifyCode(String code){
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId,code);
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    databaseUser.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            User user = null;
                                            Boolean numberIsAvailable = false;

                                            SharedPreferences.Editor editor = mSharedPreferences.edit();
                                            editor.putBoolean(AppConstant.isLogin,true);
                                            editor.putString(AppConstant.LOGGED_IN_USER_CONTACT_NUMBER,phoneNumber);
                                            editor.apply();

                                            for(DataSnapshot userSnapshot : dataSnapshot.getChildren())
                                            {
                                                user = userSnapshot.getValue(User.class);
                                                Log.d(TAG, "onDataChange: "+user);
                                                Log.d(TAG, "Usermobile: "+user.getUserMobile());
                                                if(user.getUserMobile().equals(phoneNumber)){
                                                    numberIsAvailable = true;
                                                }
                                            }
                                            if(numberIsAvailable.equals(true)){
                                                Log.d(TAG, "onDataChange: "+numberIsAvailable);
                                                Intent intent = new Intent(PhoneVerification.this,HomeActivity.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);

                                            }
                                            else
                                            {
                                                Intent intent = new Intent(PhoneVerification.this,MobileRegistrationActivity.class);
                                                intent.putExtra("phonenumber",phoneNumber);
                                                startActivity(intent);
                                            }
                                            Log.d(TAG, "onDataChange 123 : "+numberIsAvailable);
                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                        }
                                    });
                                }else{
                                    Toast.makeText(PhoneVerification.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                                }
                        }
                    });
    }

    private void sentVerificationCode(String number){

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallBack
        );

    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            verificationId = s;

        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

            String code = phoneAuthCredential.getSmsCode();

            if(code != null){
                progressBar.setVisibility(View.VISIBLE);
                verifyCode(code);
            }

        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(PhoneVerification.this,e.getMessage(),Toast.LENGTH_LONG).show();
        }
    };

}
