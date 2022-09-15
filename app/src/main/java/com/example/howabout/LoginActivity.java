package com.example.howabout;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class LoginActivity extends AppCompatActivity {

    Button btn_login;
    ImageButton btn_mypage;
    ImageButton btn_mycource1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        Button btn_regist=findViewById(R.id.btn_regist);
        btn_regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this,RegistActivity.class);
                startActivity(intent);
            }
        });
        btn_login=findViewById(R.id.btn_login);
        btn_mypage=findViewById(R.id.btn_mypage);
        btn_mycource1=findViewById(R.id.btn_mycource1);

        Button btn_logindb=findViewById(R.id.btn_logindb);
        btn_logindb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                startActivity(intent);
//                intent.putExtra("input",1);
//                setResult(RESULT_OK,intent);
//                finish();

            }
        });


    }
}