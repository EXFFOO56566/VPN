package com.nairaland.snakevpn.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.android.material.tabs.TabLayout;
import com.nairaland.snakevpn.R;
import com.nairaland.snakevpn.adapter.ViewPagerAdapterFree;
import com.nairaland.snakevpn.adapter.ViewPagerAdapterPaid;

import smartdevelop.ir.eram.showcaseviewlib.GuideView;
import smartdevelop.ir.eram.showcaseviewlib.config.DismissType;
import smartdevelop.ir.eram.showcaseviewlib.config.Gravity;
import smartdevelop.ir.eram.showcaseviewlib.listener.GuideListener;

public class ServerActivity extends AppCompatActivity {

    SharedPreferences pref, pr;

    TabLayout tabLayout;
    ViewPager viewPager;

    private static final String APP_PREFS_NAME = "key";
    private static final String VALUEE = "value";

    RelativeLayout bottomsheet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);

        tabLayout = findViewById(R.id.tablayout);
        viewPager = findViewById(R.id.viewpager);
        bottomsheet = findViewById(R.id.bottom_sheet);

        pref = getSharedPreferences("key", MODE_PRIVATE);
        String pay = pref.getString("paid", "");

        if (pay.equals("yes")){

            getPaidTabs();

        }else {

            getFreeTabs();

        }

        tutorial();
    }

    private void getPaidTabs(){

        final ViewPagerAdapterPaid viewPagerAdapterPaid = new ViewPagerAdapterPaid(getSupportFragmentManager());

        new Handler().post(new Runnable() {
            @Override
            public void run() {

                viewPagerAdapterPaid.addFragment(ServerFreeFragment.getInstance(),"Free");
                viewPagerAdapterPaid.addFragment(ServerPaidFragment.getInstance(),"Premium");
                viewPager.setAdapter(viewPagerAdapterPaid);

                tabLayout.setupWithViewPager(viewPager);

            }
        });

    }

    private void getFreeTabs(){

        final ViewPagerAdapterFree viewPagerAdapterFree = new ViewPagerAdapterFree(getSupportFragmentManager());

        new Handler().post(new Runnable() {
            @Override
            public void run() {

                viewPagerAdapterFree.addFragment(ServerFreeFragment.getInstance(),"Free");
                viewPager.setAdapter(viewPagerAdapterFree);

                tabLayout.setupWithViewPager(viewPager);

            }
        });
    }

    private void tutorial(){
        pr = getSharedPreferences("key", MODE_PRIVATE); // Init preference
        String value = pref.getString("value", ""); // Get data from preferences with id "mark"

        if (value.equals("0")){
            showCase();
        }
    }

    private void showCase(){

        new GuideView.Builder(ServerActivity.this)
                .setTitle("Server Screen")
                .setContentText("Select your favorite server")
                .setGravity(Gravity.center) //optional
                .setDismissType(DismissType.anywhere) //optional - default DismissType.targetView
                .setTargetView(tabLayout)
                .setContentTextSize(12)//optional
                .setTitleTextSize(14)//optional
                .setGuideListener(new GuideListener() {
                    @Override
                    public void onDismiss(View view) {

                        SharedPreferences mPreference = getSharedPreferences(APP_PREFS_NAME, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = mPreference.edit();
                        editor.putString(VALUEE, "1");
                        editor.apply();

                        startActivity(new Intent(ServerActivity.this, MainActivity.class));

                    }
                })
                .build()
                .show();
    }
}