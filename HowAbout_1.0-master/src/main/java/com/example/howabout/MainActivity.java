package com.example.howabout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.example.howabout.function.HowAboutThere;


public class MainActivity extends AppCompatActivity {

    //Button
    Button btn_login;
    ImageButton btn_mypage;
    ImageButton btn_mycource1;
    ImageButton img_main1;
    ImageButton img_main2;
    //login
    SharedPreferences sharedPreferences;
    TextView helloId;
    ViewFlipper viewFlipper;
    HowAboutThere FUNC = new HowAboutThere();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        FUNC.sideBar(MainActivity.this);

        img_main1 = findViewById(R.id.img_main1);
        img_main1.setClipToOutline(true);

        img_main2 = findViewById(R.id.img_main2);
        img_main2.setClipToOutline(true);

        viewFlipper = findViewById(R.id.viewFlipper);
        Animation showIn = AnimationUtils.loadAnimation(getBaseContext(), android.R.anim.slide_in_left);
        viewFlipper.setInAnimation(showIn);
        viewFlipper.setOutAnimation(getBaseContext(), android.R.anim.slide_out_right);
        viewFlipper.setFlipInterval(2000);
        viewFlipper.startFlipping();

        sharedPreferences = getSharedPreferences("USER", Activity.MODE_PRIVATE);
        //로그인시도 후 저장된 값
        String u_nick = sharedPreferences.getString("u_nick", null);
        String token = sharedPreferences.getString("token", null);
        Boolean autock = sharedPreferences.getBoolean("auto", false);

        //로그인버튼
        btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentl = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intentl);
            }
        });
        //마이페이지
        btn_mypage = findViewById(R.id.btn_mypage);
        btn_mypage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentmp = new Intent(MainActivity.this, MyPageActivity.class);
                startActivity(intentmp);
            }
        });
        //내코스
        btn_mycource1 = findViewById(R.id.btn_mycource1);
        btn_mycource1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentmc = new Intent(MainActivity.this, MyCourseActivity.class);
                startActivity(intentmc);
            }
        });

        if (u_nick == null) {
            Log.i("subin", "null 값이 오나요? ");
        } else {
            btn_login.setVisibility(View.INVISIBLE);
            btn_mycource1.setVisibility(View.VISIBLE);
            btn_mypage.setVisibility(View.VISIBLE);
            Log.i("subin", "user 정보가 오나요?: " + u_nick);
        }
////////////////////
        //intent button
        Button btn_popular = findViewById(R.id.btn_popular);
        Button btn_find = findViewById(R.id.btn_find);

        btn_popular.setClipToOutline(true);
        btn_popular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentpo = new Intent(MainActivity.this, PopularActivity.class);
                startActivity(intentpo);
            }
        });
        btn_find.setClipToOutline(true);
        btn_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentf = new Intent(MainActivity.this, FindActivity.class);
                startActivity(intentf);
            }
        });

        img_main1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "안녕", Toast.LENGTH_SHORT).show();
            }
        });

        img_main2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "하세요", Toast.LENGTH_SHORT).show();
            }
        });
    }
    /////////////////////

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("subin", "onDestroy");
        sharedPreferences = getSharedPreferences("UserInfo", MODE_PRIVATE);
        Boolean autock = sharedPreferences.getBoolean("auto_ck", false);
        if (autock == false) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            String pp1 = sharedPreferences.getAll().toString();
            Log.i("subin", "onDestroy: " + pp1);
            editor.putString("u_id", null);
            editor.putString("u_nick", null);
            editor.putBoolean("auto_ck", false);
            editor.commit();
            editor.apply();
            btn_login.setVisibility(View.VISIBLE);
            btn_mycource1.setVisibility(View.INVISIBLE);
            btn_mypage.setVisibility(View.INVISIBLE);

        }
    }


}