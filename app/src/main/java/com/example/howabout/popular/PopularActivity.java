package com.example.howabout.popular;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.howabout.FindActivity;
import com.example.howabout.MainActivity;
import com.example.howabout.MyCourseActivity;
import com.example.howabout.R;

public class PopularActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    SharedPreferences preferences;
    View drawerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popular);

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

        Button btn_homebar = findViewById(R.id.btn_homebar);
        btn_homebar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawers();
                Intent intenth = new Intent(PopularActivity.this, MainActivity.class);
                startActivity(intenth);
            }
        });
        Button btn_courcebar = findViewById(R.id.btn_courcebar);
        btn_courcebar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawers();
                Intent intentc = new Intent(PopularActivity.this, FindActivity.class);
                startActivity(intentc);
            }
        });

        Button btn_mypagebar = findViewById(R.id.btn_mypagebar);
        btn_mypagebar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawers();
            }
        });
        Button btn_mycourcebar = findViewById(R.id.btn_mycourcebar);
        btn_mycourcebar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawers();
                Intent intentmc = new Intent(PopularActivity.this, MyCourseActivity.class);
                startActivity(intentmc);
            }
        });
        Button btn_gender = findViewById(R.id.btn_gender);
        final BottomSheet_gender bottomSheet_gender = new BottomSheet_gender(getApplicationContext());

        btn_gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheet_gender.show(getSupportFragmentManager(), bottomSheet_gender.getTag());

            }
        });
        Button btn_age=findViewById(R.id.btn_age);
//        Log.i("subin","///////////////"+preferences.getString("p_age","x"));
        final BottomSheet_age bottomSheet_age=new BottomSheet_age(getApplicationContext());
        btn_age.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheet_age.show(getSupportFragmentManager(),bottomSheet_age.getTag());
            }
        });
        Button btn_region=findViewById(R.id.btn_region);
        final BottomSheet_region bottomSheet_region =new BottomSheet_region(getApplicationContext());
        btn_region.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheet_region.show(getSupportFragmentManager(),bottomSheet_region.getTag());
            }
        });
        //검색
        Button btn_casearch=findViewById(R.id.btn_casearch);
        btn_casearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                preferences =getSharedPreferences("age", MODE_PRIVATE);
//                Log.i("subin",preferences.getString("p_age","x"));
                btn_age.setText(preferences.getString("p_age","나이"));
            }
        });
    }

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
