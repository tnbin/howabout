package com.example.howabout;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.howabout.API.RetrofitClient;
import com.example.howabout.DTO.LoginDTO;
import com.example.howabout.functions.HowAboutThere;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    Map<String, String> login_data; //로그인 데이터

    EditText et_id, et_pw; //input id, pw
    Button btn_login, btn_regist; //button login, regist
    CheckBox check_autoLogin; //checkbox auto login
    TextView tv_findIDPW; //text find id, pw

    SharedPreferences sharedPreferences; //로그인한 사용자 정보 저장
    Intent intent;

    HowAboutThere FUNC = new HowAboutThere();

    boolean CODE_check_autoLogin = false; //자동 로그인 여부 체크 코드

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        //자동로그인 여부 체크
        check_autoLogin = (CheckBox) findViewById(R.id.login_check_autologin);
        check_autoLogin.setOnCheckedChangeListener(check_autologin);

        btn_login = (Button) findViewById(R.id.login_btn_login); //로그인 버튼 클릭
        btn_login.setOnClickListener(click_loginBtn);

        tv_findIDPW = (TextView) findViewById(R.id.login_findIDPW); //아이디 찾기, 비밀번호 재설정 버튼 클릭
        tv_findIDPW.setOnClickListener(click_findIDPW);

        btn_regist = (Button) findViewById(R.id.login_btn_registin);
        btn_regist.setOnClickListener(click_regist);
    }

    //regist button click event
    View.OnClickListener click_regist = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            FUNC.activity_intent(LoginActivity.this, RegistActivity.class);
            Toast.makeText(LoginActivity.this, "회원가입으로 넘어갑니다. 😊", Toast.LENGTH_SHORT).show();
        }
    };

    //auto login check
    CompoundButton.OnCheckedChangeListener check_autologin = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            CODE_check_autoLogin = b;
            if (b) {
                Log.e("leehj", "자동로그인 체크");
            } else {
                Log.e("leehj", "자동로그인 체크 해제");
            }
        }
    };

    //login button click event
    View.OnClickListener click_loginBtn = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            sharedPreferences = getSharedPreferences("USER", Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            make_requestData(); //서버 요청 데이터
            Log.e("leehj", "LOGIN MAP DATA u_id : " + login_data.get("u_id")); //@@
            Log.e("leehj", "LOGIN MAP DATA u_pw : " + login_data.get("u_pw")); //@@

            Call<LoginDTO> login = RetrofitClient.getApiService().signIn(login_data);
            login.enqueue(new Callback<LoginDTO>() {
                @Override
                public void onResponse(Call<LoginDTO> call, Response<LoginDTO> response) {
                    if (response.isSuccessful()) {
                        LoginDTO loginDTO = response.body();
                        assert loginDTO != null;
                        if (loginDTO.getSuccess() == 1) { //로그인이 성공한 경우
                            //공유 프레퍼런스에 정보 저장
                            Log.e("leehj", "RESPONSE LOGIN DTO : " + loginDTO.toString()); //@@
                            FUNC.save_login_Data(editor, loginDTO, CODE_check_autoLogin); //데이터 공유 프리퍼런스에 저장
                            Log.e("leehj", "공유 프레퍼런스 저장 성공: " + sharedPreferences.getString("token", null)); //@@

//                            Toast.makeText(LoginActivity.this, "로그인 성공!", Toast.LENGTH_SHORT).show();

                            finish(); //나중에 처리 Main에서 Login으로 넘어오는 거니까
                            FUNC.activity_intent(LoginActivity.this, MainActivity.class); //intent 처리
                        } else { //로그인이 실패한 경우
                            Toast.makeText(LoginActivity.this, loginDTO.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<LoginDTO> call, Throwable t) {
                    Log.e("leehj", "LOGIN ERROR : " + t);
                }
            });
        }
    };

    //로그인 데이터 저장 함수
    public Map make_requestData(){
        //사용자가 입력한 id, pw 가져오기
        et_id = (EditText) findViewById(R.id.login_et_id);
        String str_id = et_id.getText().toString();

        et_pw = (EditText) findViewById(R.id.login_et_pw);
        String str_pw = et_pw.getText().toString();
        Log.i("leehj", str_id + " " + str_pw);

        //사용자가 입력한 데이터를 로그인 데이터에 담기
        login_data = new HashMap<>();
        login_data.put("u_id", str_id);
        login_data.put("u_pw", str_pw);

        return login_data;
    }

    //아이디 찾기, 비밀번호 재설정
    View.OnClickListener click_findIDPW = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            FUNC.activity_intent(LoginActivity.this, FindUserInfoActivity.class);
        }
    };
}