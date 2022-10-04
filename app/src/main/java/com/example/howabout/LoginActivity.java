package com.example.howabout;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.howabout.API.RetrofitClient;
import com.example.howabout.Vo.UserVo;
import com.example.howabout.Vo.signInVo;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        EditText ed_loid = findViewById(R.id.ed_loid);
        EditText ed_lopw = findViewById(R.id.ed_lopw);

        preferences = getSharedPreferences("UserInfo", MODE_PRIVATE);

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
                String loginid = ed_loid.getText().toString();
                String loginpw = ed_lopw.getText().toString();

                UserVo inputlogin = new UserVo();
                inputlogin.setU_id(loginid);
                inputlogin.setU_pw(loginpw);
                Call<signInVo> login = RetrofitClient.getApiService().login(inputlogin);
                login.enqueue(new Callback<signInVo>() {
                    @Override
                    public void onResponse(Call<signInVo> call, Response<signInVo> response) {
                        Log.i("subin", "연결 성공");
                        signInVo job = response.body();
                        String msg = job.getMsg();
                        String nick = job.getUserVo().getU_nick();

                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("userinfo", job.getUserVo().toString());

                        editor.commit();

                        getPreferences();
                        try {
                            if (job.getSuccess() == 1) {
                                Toast.makeText(LoginActivity.this, nick + "님 환영합니다", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                setResult(RESULT_OK, intent);
                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this, "" + msg, Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(Call<signInVo> call, Throwable t) {
                        Log.i("subin", "연결 실패" + t.getMessage());
                    }
                });
            }
        });
    }

    private void getPreferences() {
        Log.i("subin", "USERINFO: " + preferences.getString("userinfo", ""));
    }

}