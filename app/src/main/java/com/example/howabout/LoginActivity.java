package com.example.howabout;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

        preferences=getSharedPreferences("UserInfo",MODE_PRIVATE);

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
                String loginid=ed_loid.getText().toString();
                String loginpw=ed_lopw.getText().toString();

                UserVo inputlogin=new UserVo();
                inputlogin.setU_id(loginid);
                inputlogin.setU_pw(loginpw);
                Call<signInVo> login=RetrofitClient.getApiService().login(inputlogin);
                login.enqueue(new Callback<signInVo>() {
                    @Override
                    public void onResponse(Call<signInVo> call, Response<signInVo> response) {
                        Log.i("subin","연결 성공");
                        String ress= String.valueOf(response.body());
                        signInVo job = response.body();
                        Log.i("subin", job.getUserVo().toString());
                        Log.i("subin",ress);
                        SharedPreferences.Editor editor=preferences.edit();
                        editor.putString("userinfo",job.getUserVo().toString());
//                        editor.putString("userid",ed_loid.getText().toString()); ress 값을 editor에 저장
//                        editor.putString("userpw",ed_lopw.getText().toString());

                        editor.commit();

                        getPreferences();
                    }

                    @Override
                    public void onFailure(Call<signInVo> call, Throwable t) {
                        Log.i("subin","연결 실패"+t.getMessage());
                    }
                });
//                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                startActivity(intent);
            }
        });
    }

    private void getPreferences() {
        Log.i("subin","USERINFO"+preferences.getString("userinfo",""));
//        tv_result.setText("USERID = " + preferences.getString("userid","")
//                + "\n USERPWD = " + preferences.getString("userpwd",""));
    }

}