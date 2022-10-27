package com.example.howabout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.util.Log;

import com.example.howabout.FindUserInfo.BlankFragment01;
import com.example.howabout.FindUserInfo.BlankFragment02;
import com.example.howabout.FindUserInfo.ContentsPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FindUserInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_user_info);
        final ViewPager2 viewPager = findViewById(R.id.viewPager);

        List<Map<String, Object>> mapList = new ArrayList<>();

        Map<String, Object> stringObjectMap = setTabTitleAndFragment("아이디 찾기", new BlankFragment01());
        Map<String, Object> settingMap = setTabTitleAndFragment("비밀번호 재설정", new BlankFragment02());

        mapList.add(stringObjectMap);
        mapList.add(settingMap);

        final ContentsPagerAdapter contentsPagerAdapter = new ContentsPagerAdapter(this, mapList);
        viewPager.setAdapter(contentsPagerAdapter);
        TabLayout tabLayout = findViewById(R.id.tabLayout);

        new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(contentsPagerAdapter.getTitle(position));
                Log.i("subin","click: "+tab.getText());
            }
        }).attach();

    }

    Map<String, Object> setTabTitleAndFragment(String title, Fragment fragment){

        Map<String, Object> fragmentWithTitleMap = new HashMap<>();

        fragmentWithTitleMap.put("fragmentTitle", title);
        fragmentWithTitleMap.put("fragment", fragment);

        return fragmentWithTitleMap;
    }
}