package com.daninpr.minimarket;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Login_Main_Activity extends AppCompatActivity {

    SharedPrefManager sharedPrefManager;
    public List<User> users;
    public EditText edt_username,edt_password;
    public Button btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_main);

        sharedPrefManager = new SharedPrefManager(this);

        if (sharedPrefManager.getSPSudahLogin()){
            startActivity(new Intent(Login_Main_Activity.this, MainActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
            finish();
        }

        addUser();

        edt_username = findViewById(R.id.username);
        edt_password = findViewById(R.id.password);
        btn_login = findViewById(R.id.login);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User tmpUser = null;
                for(User u:users   ){
                    if(u.getUsername().equals(edt_username.getText().toString()) && u.getPassword().equals(edt_password.getText().toString())){
                        tmpUser = u;
                    }
                }

                if(tmpUser != null){
                    SharedPreferences spUser = Login_Main_Activity.this.getSharedPreferences("UserLogin", Context.MODE_PRIVATE);
                    SharedPreferences.Editor edit = spUser.edit();

                    edit.putString("Login",tmpUser.getUsername());
                    edit.apply();

                    sharedPrefManager.saveSPBoolean(SharedPrefManager.SP_SUDAH_LOGIN, true);

                    Intent intent = new Intent(Login_Main_Activity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(Login_Main_Activity.this, "Username atau Password Salah", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void addUser() {
        users = new ArrayList<>();
        users.add(new User("admin", "admin123"));
        users.add(new User("danin", "danin123"));
        users.add(new User("puri", "puri123"));
    }
}
