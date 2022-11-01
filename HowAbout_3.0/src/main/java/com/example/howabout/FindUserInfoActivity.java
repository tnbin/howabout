package com.example.howabout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.example.howabout.Login.FindId;
import com.example.howabout.Login.FindPwCheck;
import com.example.howabout.Login.ResetPw;
import com.google.android.material.tabs.TabLayout;

public class FindUserInfoActivity extends AppCompatActivity {

    FindPwCheck findPwCheck;
    FindId findId;
    ResetPw resetPw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_user_info);

        findPwCheck = new FindPwCheck();
        findId = new FindId();
        resetPw = new ResetPw();

        getSupportFragmentManager().beginTransaction().replace(R.id.container, findId).commit();
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("아이디 찾기"));
        tabLayout.addTab(tabLayout.newTab().setText("비밀번호 재설정"));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();

                Fragment selected = null;
                if (position == 0) {
                    selected = findId;
                } else if (position == 1) {
                    selected = findPwCheck;
                } else if (position == 2) {
                    selected = resetPw;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.container, selected).commit();

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

}