package com.example.howabout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.example.howabout.Fragment.FragMyAdapter;
import com.example.howabout.functions.HowAboutThere;

import me.relex.circleindicator.CircleIndicator3;


public class MainActivity extends AppCompatActivity {

    HowAboutThere FUNC = new HowAboutThere();

    //Button
    Button btn_login, btn_popular, btn_find;
    ImageButton btn_mypage, btn_mycourse;
    ImageButton img_main1, img_main2;
    TextView side_hello, main_hello;

    SharedPreferences sharedPreferences;

    ViewFlipper viewFlipper;

    //login
//    final static int REQUEST_CODE_START_INPUT = 1;
    //viewpager
//    private ViewPager2 mPager;
//    private FragmentStateAdapter pagerAdapter;
//    private int num_page = 4;
//    private CircleIndicator3 mIndicator;
//    TextView helloId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        FUNC.sideBar(MainActivity.this); //side bar

        sharedPreferences = getSharedPreferences("USER", Activity.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);
        Log.i("leehj", "main :: login token: "+token);
        String nickname = sharedPreferences.getString("u_nick", null);
        Log.i("leehj", "main :: login nickname: "+nickname);

        viewFlipper = findViewById(R.id.viewFlipper);
        Animation showIn = AnimationUtils.loadAnimation(getBaseContext(), android.R.anim.slide_in_left);
        viewFlipper.setInAnimation(showIn);
        viewFlipper.setOutAnimation(getBaseContext(), android.R.anim.slide_out_right);
        viewFlipper.setFlipInterval(2000);
        viewFlipper.startFlipping();

        btn_login = findViewById(R.id.main_btn_login);
        btn_mypage = findViewById(R.id.main_btn_mypage);
        btn_mycourse = findViewById(R.id.main_btn_mycourse);
        btn_popular = findViewById(R.id.main_btn_popular);
        btn_find = findViewById(R.id.main_btn_find);
        side_hello = findViewById(R.id.helloId);
        main_hello = findViewById(R.id.main_hello);

        if(token != null){ //로그인을 한 경우
            Toast.makeText(this, "로그인 되었습니다.", Toast.LENGTH_SHORT).show();
            btn_login.setVisibility(View.GONE);
            btn_mypage.setVisibility(View.VISIBLE);
            btn_mycourse.setVisibility(View.VISIBLE);

            main_hello.setText(nickname+"님 환영합니다 🙌");
        }else{
            btn_login.setVisibility(View.VISIBLE);
            btn_mypage.setVisibility(View.GONE);
            btn_mycourse.setVisibility(View.GONE);
            main_hello.setText("로그인을 해주세요! 🙏");
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
                    FUNC.activity_intent(MainActivity.this, MyCourseActivity.class);break;
                case R.id.main_btn_mypage:
                    FUNC.activity_intent(MainActivity.this, MyPageActivity.class);break;
                case R.id.main_btn_popular:
                    FUNC.activity_intent(MainActivity.this, PopularActivity.class);break;
                case R.id.main_btn_find:
                    FUNC.activity_intent(MainActivity.this, FindActivity.class);break;
            }
        }
    };
}