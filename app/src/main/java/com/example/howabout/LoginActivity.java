package com.example.howabout;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

                LoginVo inputlogin=new LoginVo(loginid,loginpw);
                Call<List<UserVo>> login=RetrofitClient.getApiService().login(inputlogin);
                login.enqueue(new Callback<List<UserVo>>() {
                    @Override
                    public void onResponse(Call<List<UserVo>> call, Response<List<UserVo>> response) {
                        Log.i("subin","연결 성공");
                    }

                    @Override
                    public void onFailure(Call<List<UserVo>> call, Throwable t) {
                        Log.i("subin","연결 실패"+t.getMessage());
                    }
                });
//                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                startActivity(intent);
            }
        });
    }

}