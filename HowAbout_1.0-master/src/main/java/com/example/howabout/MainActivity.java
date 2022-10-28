package com.example.howabout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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


public class MainActivity extends AppCompatActivity {

    //Button
    Button btn_login;
    ImageButton btn_mypage;
    ImageButton btn_mycource1;
    ImageButton img_main1;
    ImageButton img_main2;
    //login
    SharedPreferences preferences;
    TextView helloId;
    ViewFlipper viewFlipper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        img_main1 = findViewById(R.id.img_main1);
        img_main1.setClipToOutline(true);

        img_main2 = findViewById(R.id.img_main2);
        img_main2.setClipToOutline(true);

        Button logout = findViewById(R.id.logout);
        helloId = findViewById(R.id.helloId);

        viewFlipper = findViewById(R.id.viewFlipper);
        Animation showIn = AnimationUtils.loadAnimation(getBaseContext(), android.R.anim.slide_in_left);
        viewFlipper.setInAnimation(showIn);
        viewFlipper.setOutAnimation(getBaseContext(), android.R.anim.slide_out_right);
        viewFlipper.setFlipInterval(2000);
        viewFlipper.startFlipping();

        preferences = getSharedPreferences("UserInfo", MODE_PRIVATE);
        //로그인시도 후 저장된 값
        String u_nick = preferences.getString("u_nick", null);
        String u_id = preferences.getString("u_id", null);
        Boolean autock = preferences.getBoolean("auto_ck", false);

        btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentl = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intentl);
//                startActivityForResult(intentl, REQUEST_CODE_START_INPUT);
            }
        });
        btn_mypage = findViewById(R.id.btn_mypage);
        btn_mypage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentmp = new Intent(MainActivity.this, MyPageActivity.class);
                startActivity(intentmp);
            }
        });
        btn_mycource1 = findViewById(R.id.btn_mycource1);
        btn_mycource1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentmc = new Intent(MainActivity.this, MyCourseActivity.class);
                startActivity(intentmc);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("로그아웃");
                builder.setMessage("정말로 로그아웃 하시겠습니까?");
                builder.setPositiveButton("로그아웃", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(MainActivity.this, "logout", Toast.LENGTH_SHORT).show();
                        SharedPreferences.Editor editor = preferences.edit();
                        String pp1 = preferences.getAll().toString();
                        Log.i("subin", "onDestroy: " + pp1);
                        editor.putString("u_id", null);
                        editor.putString("u_nick", null);
                        editor.putBoolean("auto_ck", false);
                        editor.commit();
                        editor.apply();
                        btn_login.setVisibility(View.VISIBLE);
                        btn_mycource1.setVisibility(View.INVISIBLE);
                        btn_mypage.setVisibility(View.INVISIBLE);
                        Log.i("subin", "user정보 : " + preferences.getAll());
                        helloId = findViewById(R.id.helloId);
                        helloId.setText("저기어때에 오신걸 환영합니다.");

                    }
                });
                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
        if (u_nick == null || u_id == null) {
            Log.i("subin", "null 값이 오나요? ");
        } else {
            btn_login.setVisibility(View.INVISIBLE);
            btn_mycource1.setVisibility(View.VISIBLE);
            btn_mypage.setVisibility(View.VISIBLE);
            Log.i("subin", "user 정보가 오나요?: " + u_nick);
            helloId.setText(u_nick + "님, 환영합니다");
        }

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("subin", "onDestroy");
        preferences = getSharedPreferences("UserInfo", MODE_PRIVATE);
        Boolean autock = preferences.getBoolean("auto_ck", false);
        if (autock == false) {
            SharedPreferences.Editor editor = preferences.edit();
            String pp1 = preferences.getAll().toString();
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