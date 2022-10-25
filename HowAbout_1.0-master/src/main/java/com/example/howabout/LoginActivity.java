package com.example.howabout;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
    Boolean autologin_ck=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        EditText ed_loid = findViewById(R.id.ed_loid);
        EditText ed_lopw = findViewById(R.id.ed_lopw);

        Button btn_regist = findViewById(R.id.btn_registin);
        btn_regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegistActivity.class);
                startActivity(intent);
            }
        });
        //자동 로그인 설정
        CheckBox auto_login = findViewById(R.id.autologin);
        auto_login.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b == true) {
                    Toast.makeText(LoginActivity.this, "check", Toast.LENGTH_SHORT).show();
                    autologin_ck=true;
                } else {

                }
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
                        preferences = getSharedPreferences("UserInfo", MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        try {
                            if (job.getSuccess() == 1) {
                                Toast.makeText(LoginActivity.this, nick + "님 환영합니다", Toast.LENGTH_SHORT).show();
                                editor.putString("u_id", job.getUserVo().getU_id());
                                editor.putString("u_nick", job.getUserVo().getU_nick());
                                editor.putBoolean("auto_ck",autologin_ck);
                                editor.commit();

                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
//                                intent.putExtra("usernick", nick);
//                                setResult(RESULT_OK, intent);
                                finish();
                            } else {
                                editor.putString("u_id", null);
                                editor.putString("u_nick", null);
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
}