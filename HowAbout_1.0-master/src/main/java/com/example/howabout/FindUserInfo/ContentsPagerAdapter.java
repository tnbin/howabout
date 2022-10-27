package com.example.howabout.FindUserInfo;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class ContentsPagerAdapter extends FragmentStateAdapter {
    List<Map<String, Object>> mapList;

    public ContentsPagerAdapter(@NonNull FragmentActivity fragmentActivity,List<Map<String,Object>>mapList) {
        super(fragmentActivity);
        this.mapList = mapList;
    }


    @NonNull
    @NotNull
    @Override
    public Fragment createFragment(int position) {
        return (Fragment) mapList.get(position).get("fragment");
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {
        return mapList.size();
    }
    public String getTitle(int position){

        String title = (String) mapList.get(position).get("fragmentTitle");

        if(title == null) title = "No Title";

        return title;
    }
}
