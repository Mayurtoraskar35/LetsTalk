package com.example.letstalk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
/*
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;*/

public class GetStartedActivity extends AppCompatActivity {

    @BindView(R.id.btngetstarted) Button btnGetStarted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btngetstarted)
    public void getStarted(){
        Intent intent = new Intent(this,EnterContactNumberActivity.class);
        startActivity(intent);
    }
}