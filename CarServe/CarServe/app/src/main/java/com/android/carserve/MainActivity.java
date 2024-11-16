package com.android.carserve;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    final int MY_PERMISSIONS_REQUEST=110;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        askPermission();

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MYPREF", MODE_PRIVATE);
        if(pref.getString("username",null)!=null)
        {
            Shared.username=pref.getString("username",null);
            finish();
            startActivity(new Intent(getApplicationContext(), UserDashboard.class));
        }


        if(pref.getString("providerdminusername",null)!=null)
        {
            Shared.username=pref.getString("providerdminusername",null);
            finish();
            startActivity(new Intent(getApplicationContext(), ProviderDash.class));
        }

    }


    public void gotoproviderlogin(View view) {
        Intent in=new Intent(getApplicationContext(),ProviderLogin.class);
        startActivity(in);
    }

    public void gotouserlogin(View view) {
        Intent in=new Intent(getApplicationContext(),UserSignUp.class);
        startActivity(in);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void askPermission() {

        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED || checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED||checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSIONS_REQUEST);
            return;
        } else {
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                    askPermission();
                }
                return;
            }

            // other 'switch' lines to check for other
            // permissions this app might request
        }

    }

    public void gotousersignup(View view) {
        startActivity(new Intent(getApplicationContext(),UserSignupScreen.class));
    }
}