package com.example.letstalk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.example.letstalk.AppConstant.AppConstant;

public class LauncherActivity extends AppCompatActivity {

    private static final String TAG = "LauncherActivity";

    boolean login;

    private SharedPreferences mSharedPreferences;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        /*Intent intent=new Intent(this,HomeActivity.class);
        startActivity(intent);
        finish();*/
        mSharedPreferences =getSharedPreferences(AppConstant.PREFERENCE_FILE_NAME,MODE_PRIVATE);
        login = mSharedPreferences.getBoolean(AppConstant.isLogin, false);

        Log.d(TAG, "SharedPreferance "+mSharedPreferences.getString(AppConstant.LOGGED_IN_USER_CONTACT_NUMBER,null));

        if(!login)
        {
            intent = new Intent(this,GetStartedActivity.class);
            startActivity(intent);
            finish();
        }
        else
        {
            intent = new Intent(this,HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
