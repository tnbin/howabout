package com.example.howabout;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.howabout.API.RetrofitClient;
import com.example.howabout.Vo.LoginDTO;
import com.example.howabout.function.HowAboutThere;
import com.example.howabout.function.SaveData;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    Map<String, String> login_data; //로그인 데이터
    private SharedPreferences preferences;
    SharedPreferences sharedPreferences; //로그인한 사용자 정보 저장
    SaveData SAVEDATA = new SaveData();
    HowAboutThere FUNC = new HowAboutThere();

    EditText et_id;
    EditText et_pw;
    boolean CODE_check_autoLogin = false; //자동 로그인 여부 체크 코드

    @Override
    @SuppressLint("ClickableViewAccessibility")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        et_id = findViewById(R.id.ed_loid);
        et_pw = findViewById(R.id.ed_lopw);
        TextView search_idpw=findViewById(R.id.search_idpw);

        Button btn_regist = findViewById(R.id.btn_registin);
        Button btn_login = findViewById(R.id.btn_lologin);
        btn_login.setOnClickListener(click_loginBtn);
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
                CODE_check_autoLogin = b;
                if (b) {
                    Log.e("subin", "자동로그인 체크");
                } else {
                    Log.e("subin", "자동로그인 체크 해제");
                }
            }
        });
        search_idpw.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Intent intent=new Intent(LoginActivity.this,FindUserInfoActivity.class);
                startActivity(intent);
                return false;
            }
        });
    }
    View.OnClickListener click_loginBtn=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            sharedPreferences = getSharedPreferences("USER", Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            make_requestData(); //서버 요청 데이터
            Log.e("subin", "LOGIN MAP DATA u_id : " + login_data.get("u_id")); //@@
            Log.e("subin", "LOGIN MAP DATA u_pw : " + login_data.get("u_pw")); //@@

            Call<LoginDTO> login = RetrofitClient.getApiService().login(login_data);
            login.enqueue(new Callback<LoginDTO>() {
                @Override
                public void onResponse(Call<LoginDTO> call, Response<LoginDTO> response) {
                    if (response.isSuccessful()) {
                        LoginDTO loginDTO = response.body();
                        assert loginDTO != null;
                        if (loginDTO.getSuccess() == 1) { //로그인이 성공한 경우
                            //공유 프레퍼런스에 정보 저장
                            Log.e("subin", "RESPONSE LOGIN DTO : " + loginDTO.toString()); //@@
                            SAVEDATA.save_login_Data(editor, loginDTO, CODE_check_autoLogin); //데이터 공유 프리퍼런스에 저장
                            Log.e("subin", "공유 프레퍼런스 저장 성공: " + sharedPreferences.getString("token", null)); //@@

                            Toast.makeText(LoginActivity.this, "로그인 성공!", Toast.LENGTH_SHORT).show();

                            //finish(); //나중에 처리 Main에서 Login으로 넘어오는 거니까
                            FUNC.activity_intent(LoginActivity.this, MainActivity.class); //intent 처리
                        } else { //로그인이 실패한 경우
                            Toast.makeText(LoginActivity.this, loginDTO.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<LoginDTO> call, Throwable t) {
                    Log.e("subin", "LOGIN ERROR : " + t);
                }
            });
        }
    };
    //로그인 데이터 저장 함수
    public Map make_requestData(){
        //사용자가 입력한 id, pw 가져오기
        et_id = (EditText) findViewById(R.id.ed_loid);
        String str_id = et_id.getText().toString();

        et_pw = (EditText) findViewById(R.id.ed_lopw);
        String str_pw = et_pw.getText().toString();
        Log.i("leehj", str_id + " " + str_pw);

        //사용자가 입력한 데이터를 로그인 데이터에 담기
        login_data = new HashMap<>();
        login_data.put("u_id", str_id);
        login_data.put("u_pw", str_pw);

        return login_data;
    }
}