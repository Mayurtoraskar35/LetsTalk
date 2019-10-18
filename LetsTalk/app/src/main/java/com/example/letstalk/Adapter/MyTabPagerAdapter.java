package com.example.letstalk.Adapter;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.letstalk.Fragment.ChatFragment;
import com.example.letstalk.Fragment.ContactFragment;

import java.util.List;

public class MyTabPagerAdapter extends FragmentStatePagerAdapter
{

    private static final String TAG = "MyTabPagerAdapter";

    List<String> mTabList;

    public MyTabPagerAdapter(@NonNull FragmentManager fm, List<String> tabList) {
        super(fm);
        mTabList = tabList;
        Log.d(TAG, "MyTabPagerAdapter: "+ mTabList);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if(position == 0)
        {
            Log.d(TAG, "getItem:  ChatFragmet");
            return new ChatFragment();
        }
        else
        {
            Log.d(TAG, "getItem:  ContactFragmet");
            return new ContactFragment();
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        Log.d(TAG, "getPageTitle: "+position);

        return mTabList.get(position);
    }

    @Override
    public int getCount() {
        return mTabList.size();
    }
}
