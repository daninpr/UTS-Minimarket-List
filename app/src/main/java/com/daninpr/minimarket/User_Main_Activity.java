package com.daninpr.minimarket;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class User_Main_Activity extends AppCompatActivity {
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);

        recyclerView = findViewById(R.id.recycler_view);
        ArrayList<User> users = new ArrayList<>();
        users.add(new User("admin", "admin123"));
        users.add(new User("danin", "danin123"));
        users.add(new User("puri", "puri123"));

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(User_Main_Activity.this);

        UserAdapter userAdapter = new UserAdapter(users, this);

        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(userAdapter);
    }
}
