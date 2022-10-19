package com.example.howabout.popular;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.howabout.R;
import com.example.howabout.category_search.MyAdatpter;

import java.util.ArrayList;

public class PopularAdapter extends BaseAdapter {

    ArrayList<Popular_item> list=new ArrayList<Popular_item>();

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final Context context=viewGroup.getContext();
        if (view==null){
            LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view= inflater.inflate(R.layout.popular_item,viewGroup,false);
        }


        ImageView imageView=view.findViewById(R.id.popular_img);
        Glide.with(context).load(list.get(i).getImage()).apply(new RequestOptions().transforms(new CenterCrop(),
                new RoundedCorners(13))).into(imageView);
        TextView textView=view.findViewById(R.id.popular_text);
        textView.setText(list.get(i).getPlace());
        return view;
    }
//매개변수 공부하기
//    public void addItem(String ImageUrl,String text){
//        Popular_item popular_item=new Popular_item();
//        popular_item.setImage(ImageUrl);
//        popular_item.setPlace(text);
//
//        list.add(popular_item);
//    }
    public void addItem(Popular_item popular_item){
        list.add(popular_item);
    }
}
