package com.example.howabout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.example.howabout.API.RetrofitClient;
import com.example.howabout.DTO.UserDTO;
import com.example.howabout.functions.HowAboutThere;

import org.json.simple.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//main test activity
public class MainActivity extends AppCompatActivity {

    Button btn_login;
    TextView hello, btn_popular, btn_find, btn_mypage, btn_mycourse;
    ImageView hello_icon;
    ViewFlipper viewFlipper;

    SharedPreferences sharedPreferences;
    String token = null;
    HowAboutThere FUNC = new HowAboutThere(); //함수 모음 클래스
    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        FUNC.sideBar(MainActivity.this); //side bar

        sharedPreferences = getSharedPreferences("USER", Activity.MODE_PRIVATE);
        token = sharedPreferences.getString("token", null);
        Log.i("leehj", "main :: login token: "+token);
        String nickname = sharedPreferences.getString("u_nick", null);
        Log.i("leehj", "main :: login nickname: "+nickname);

        viewFlipper = findViewById(R.id.viewFlipper);
        Animation showIn = AnimationUtils.loadAnimation(getBaseContext(), android.R.anim.slide_in_left);
        viewFlipper.setInAnimation(showIn);
        viewFlipper.setOutAnimation(getBaseContext(), android.R.anim.slide_out_right);
        viewFlipper.setFlipInterval(2000);
        viewFlipper.startFlipping();

        btn_login = (Button) findViewById(R.id.main_btn_login);
        btn_mypage = (TextView) findViewById(R.id.main_btn_mypage);
        btn_mycourse = (TextView) findViewById(R.id.main_btn_mycourse);
        btn_popular = (TextView) findViewById(R.id.main_btn_popular);
        btn_find = (TextView) findViewById(R.id.main_btn_find);
        hello = (TextView) findViewById(R.id.main_hello);
        hello_icon = (ImageView) findViewById(R.id.main_hello_icon);

        if(token != null){ //로그인을 한 경우
            Toast.makeText(this, "로그인 되었습니다.", Toast.LENGTH_SHORT).show();
            btn_login.setVisibility(View.GONE);
            hello.setVisibility(View.VISIBLE);
            hello_icon.setVisibility(View.VISIBLE);
            hello.setText(nickname);
        }else{
            btn_login.setVisibility(View.VISIBLE);
        }

        btn_login.setOnClickListener(click);
        btn_mypage.setOnClickListener(click);
        btn_mycourse.setOnClickListener(click);
        btn_find.setOnClickListener(click);
        btn_popular.setOnClickListener(click);
    }

    View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int res = view.getId();
            switch (res){
                case R.id.main_btn_login:
                    FUNC.activity_intent(MainActivity.this, LoginActivity.class);break;
                case R.id.main_btn_mycourse:
                    if(token != null){
                        FUNC.activity_intent(MainActivity.this, MyCourseActivity.class);
                    }else{
                        Toast.makeText(MainActivity.this, "로그인 후 사용 가능한 기능입니다!", Toast.LENGTH_SHORT).show();
                    }break;
                case R.id.main_btn_mypage:
                    if(token != null){
                        FUNC.activity_intent(MainActivity.this, MyPageActivity.class);
                    }else{
                        Toast.makeText(MainActivity.this, "로그인 후 사용 가능한 기능입니다!", Toast.LENGTH_SHORT).show();
                    }break;
                case R.id.main_btn_popular:
                    FUNC.activity_intent(MainActivity.this, PopularActivity.class);break;
                case R.id.main_btn_find:
                    FUNC.activity_intent(MainActivity.this, FindActivity.class);break;
            }
        }
    };
}