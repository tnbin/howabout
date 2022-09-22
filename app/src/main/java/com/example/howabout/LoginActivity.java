package com.example.howabout;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        EditText ed_loid = findViewById(R.id.ed_loid);
        EditText ed_lopw = findViewById(R.id.ed_lopw);
        String loginid=ed_loid.getText().toString();
        String loginpw=ed_lopw.getText().toString();


        Button btn_regist = findViewById(R.id.btn_registin);
        btn_regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegistActivity.class);
                startActivity(intent);
            }
        });

        Button btn_logindb = findViewById(R.id.btn_lologin);
        btn_logindb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);


            }
        });
    }

}