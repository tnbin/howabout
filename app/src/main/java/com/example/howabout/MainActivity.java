package com.example.howabout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    View drawerView;
    Button btn_login;
    ImageButton btn_mypage;
    ImageButton btn_mycource1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        drawerLayout = findViewById(R.id.drawer_layout);
        drawerView = findViewById(R.id.drawer);

        ImageButton btn_open = findViewById(R.id.btn_open);
        btn_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(drawerView);
            }
        });

        drawerLayout.setDrawerListener(listener);
        drawerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                return true;
            }
        });

        Button btn_close = findViewById(R.id.btn_close);
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "메뉴오픈", Toast.LENGTH_SHORT).show();
                drawerLayout.closeDrawers();
            }
        });

        Button btn_hi = findViewById(R.id.btn_hello);
        btn_hi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawers();
                Toast.makeText(MainActivity.this, "Hi", Toast.LENGTH_SHORT).show();
            }
        });

        btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentl = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intentl);
//                startActivityForResult(intentl,1);
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
        btn_mycource1=findViewById(R.id.btn_mycource1);
        btn_mycource1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentmc=new Intent(MainActivity.this,MyCourseActivity.class);
                startActivity(intentmc);
            }
        });
        Button btn_popular=findViewById(R.id.btn_popular);
        btn_popular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentpo=new Intent(MainActivity.this,PopularActivity.class);
                startActivity(intentpo);
            }
        });
        Button btn_mycource2=findViewById(R.id.btn_mycource2);
        btn_mycource2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentmc=new Intent(MainActivity.this,MyCourseActivity.class);
                startActivity(intentmc);
            }
        });
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode==1){
//            if (resultCode==RESULT_OK){
//                Log.i("subin",data.getStringExtra("input"));
//                btn_login.setVisibility(View.INVISIBLE);
//                btn_mypage.setVisibility(View.VISIBLE);
//                btn_mycource1.setVisibility(View.VISIBLE);
//            }
//        }
//    }

    DrawerLayout.DrawerListener listener = new DrawerLayout.DrawerListener() {
        @Override
        public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

        }

        @Override
        public void onDrawerOpened(@NonNull View drawerView) {

        }

        @Override
        public void onDrawerClosed(@NonNull View drawerView) {

        }

        @Override
        public void onDrawerStateChanged(int newState) {

        }
    };
}