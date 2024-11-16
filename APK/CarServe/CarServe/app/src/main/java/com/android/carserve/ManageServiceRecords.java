package com.android.carserve;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.viewpager.widget.ViewPager;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;


public class ManageServiceRecords extends AppCompatActivity {


    ViewPager vp1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_service_records);
        vp1= findViewById(R.id.vp1);
        PageAdapter pageAdapter  = new PageAdapter(getSupportFragmentManager());
        vp1.setAdapter(pageAdapter);
        vp1.setCurrentItem(0, true);
    }

    public void addrecord(View view) {

        vp1.setCurrentItem(0, true);
    }


    public void viewrecords(View view) {
        vp1.setCurrentItem(1, true);

    }
    class PageAdapter extends FragmentPagerAdapter
    {

        public PageAdapter(FragmentManager fm)
        {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {

            if(position==0)
            {
                return new AddRecord();
            }
            else if(position==1)
            {
                return new ViewRecords();
            }
            else
            {
                return new AddRecord();
            }

        }

        @Override
        public int getCount() {
            return 3;
        }
    }

}