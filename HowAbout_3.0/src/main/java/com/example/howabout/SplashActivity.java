package com.example.howabout;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.example.howabout.API.RetrofitClient;
import com.example.howabout.functions.HowAboutThere;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {

    HowAboutThere FUNC = new HowAboutThere();
    Intent intent;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);


        sharedPreferences = getSharedPreferences("USER", Activity.MODE_PRIVATE);
        boolean CODE_autoLogin = sharedPreferences.getBoolean("auto", false);
        Log.e("leehj", CODE_autoLogin + "");
        if (CODE_autoLogin) { //자동로그인을 선택한 경우
            String token = sharedPreferences.getString("token", null);
            if (token != null) { //LOGIN 파일에 token 값이 있으면, 자동로그인 성공
                Log.i("leehj", "splash/ 자동로그인 선택 token이 " + token);
                reToken("Bearer " + token); //토큰 재발급
                splash(1);
            } else { //token이 null이면
                Log.i("leehj", "splash/ 자동로그인 선택 token이 null");
                splash(1);
            }
        } else { //자동 로그인을 선택하지 않을 경우
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.commit();
            Log.i("leehj", "splash/ 자동로그인 선택 안함");
            splash(1);
        }
    }

    //로딩 화면 처리
    private void splash(int sec) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FUNC.activity_intent(SplashActivity.this, MainActivity.class);
                finish();    //현재 액티비티 종료
            }
        }, 1000 * sec); // sec초 정도 딜레이를 준 후 시작
    }

    public void reToken(String token) {
        Call<Map<String, String>> login = RetrofitClient.getApiService().autoLogin(token);
        login.enqueue(new Callback<Map<String, String>>() {
            @Override
            public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                Map<String, String> result = response.body();
                if (result != null) {
                    String response_token = result.get("jwt");
                    Log.e("leehj", "재발급 토큰 : " + response_token);
                    sharedPreferences = getSharedPreferences("USER", Activity.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("token", response_token);
                    editor.commit();
                    Log.i("leehj", "공유 프레퍼런스 저장 토큰: " + sharedPreferences.getString("token", null)); //@@

                }
            }

            @Override
            public void onFailure(Call<Map<String, String>> call, Throwable t) {

            }
        });
    }
}