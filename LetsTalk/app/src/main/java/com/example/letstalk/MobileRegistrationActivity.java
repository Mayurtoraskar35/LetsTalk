package com.example.letstalk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.letstalk.LTModel.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MobileRegistrationActivity extends AppCompatActivity {

    @BindView(R.id.txtUserName) EditText txtName;

    @BindView(R.id.txtUserSurname) EditText txtSurname;

    @BindView(R.id.btnRegister) Button btnRegister;

    String number;

    private static final String TAG = "MobileRegistrationActiv";

    DatabaseReference databaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_registration);
        //txtName = findViewById(R.id.txtUserName);
        //txtSurname = findViewById(R.id.txtUserSurname);
        //number = getIntent().getStringExtra("phonenumber");
        number = getIntent().getStringExtra("phonenumber");
        databaseUser = FirebaseDatabase.getInstance().getReference("users");
        Log.d(TAG, "onCreate: "+databaseUser.toString());
        ButterKnife.bind(this);
    }

    public void addUser(){
        String name = txtName.getText().toString().trim();
        String surName = txtSurname.getText().toString().trim();
        if(!TextUtils.isEmpty(name) || !TextUtils.isEmpty(surName))
        {
            String numberText = String.valueOf(number);
            byte[] bytesEncoded = Base64.encode(numberText.getBytes(), Base64.DEFAULT);
            String base64Id = new String(bytesEncoded);
            String id = base64Id.replace("==", "").trim();
            User user = new User(id,name,surName,number);
            //String no = bytesEncoded.toString().trim();
            databaseUser.child(new String(bytesEncoded).trim()).setValue(user);
            Toast.makeText(this,"User added.",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this,HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }else{
            Toast.makeText(this,"You should enter name & surname.",Toast.LENGTH_LONG).show();
        }
    }

    @OnClick(R.id.btnRegister)
    public void Register() {
        try {
            addUser();
        }
        catch (Exception e){
            Log.d(TAG, "Register: "+e.toString());
        }
    }
}