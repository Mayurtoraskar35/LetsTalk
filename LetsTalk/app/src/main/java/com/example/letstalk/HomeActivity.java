package com.example.letstalk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.letstalk.Adapter.MyContactAdapter;
import com.example.letstalk.Adapter.MyTabPagerAdapter;
import com.example.letstalk.AppConstant.AppConstant;
import com.example.letstalk.Database.DatabaseHandler;
import com.example.letstalk.LTModel.User;
import com.example.letstalk.LTModel.UserContact;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.widget.Toolbar;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HomeActivity";

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    MyTabPagerAdapter myTabPagerAdapter;

    Toolbar myToolbar;

    private SharedPreferences mSharedPreferences;

    List<String> tabList;

    DatabaseHandler databaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        myToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(myToolbar);

        mTabLayout = findViewById(R.id.tab_layout);

        mViewPager = findViewById(R.id.view_pager);

        mTabLayout.setupWithViewPager(mViewPager);

        tabList = new ArrayList<>();

        tabList.add("Chat");
        tabList.add("Contact");

        myTabPagerAdapter =new MyTabPagerAdapter(getSupportFragmentManager(),tabList);

        mViewPager.setAdapter(myTabPagerAdapter);

    }
}
