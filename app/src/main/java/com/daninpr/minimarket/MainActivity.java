package com.daninpr.minimarket;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG =MainActivity.class.getSimpleName() ;

    SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void tambahClick(View view) {
        Log.d(LOG_TAG, "Tambah Minimarket");
        Intent intent = new Intent(this, Minimarket_MainActivity.class);
        startActivity(intent);
    }

    public void minimarketClick(View view) {
        Log.d(LOG_TAG, "Minimarket");
        Intent intent = new Intent(this, MinimarketList.class);
        startActivity(intent);
    }

    public void userClick(View view) {
        Log.d(LOG_TAG, "User");
        Intent intent = new Intent(this, User_Main_Activity.class);
        startActivity(intent);
    }

    public void logoutMinimarket(View view) {
        Log.d(LOG_TAG, "Logout");

        sharedPrefManager = new SharedPrefManager(this);

        sharedPrefManager.saveSPBoolean(SharedPrefManager.SP_SUDAH_LOGIN, false);
        startActivity(new Intent(MainActivity.this, Login_Main_Activity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
        finish();
    }
}
