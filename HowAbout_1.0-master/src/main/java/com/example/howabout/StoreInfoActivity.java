package com.example.howabout;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;


public class StoreInfoActivity extends AppCompatActivity {

    String url = null;
    String name=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.store_info);

//        Intent intent = getIntent();
//        Document document = intent.getParcelableExtra("10");
//        String popular_url = document.getPlaceUrl();
//        url=popular_url;

//        //////////////
        Intent r_intent = getIntent();
        String r_url = r_intent.getStringExtra("r_url");
        String r_name = r_intent.getStringExtra("r_name");
        url=r_url;
        name=r_name;

//        Intent c_intent = getIntent();
//        String c_url = c_intent.getStringExtra("c_url");
//        String c_name = r_intent.getStringExtra("c_name");
//        Log.i("subin", "r_url 인기코스" + r_url);
//        Log.i("subin", "c_url 인기코스" + c_url);
//        url=c_url;
//        name=c_name;

//        ImageView storeInfo_img = findViewById(R.id.storeInfo_img);
//        TextView storeInfo_tv_placeName = findViewById(R.id.storeInfo_tv_placeName);
//        if (url != null||name!=null) {
//            Log.i("subin","url: "+url);
//            Glide.with(StoreInfoActivity.this).load(url).placeholder(R.drawable.error_img1).override(Target.SIZE_ORIGINAL).apply(new RequestOptions().transforms(new CenterCrop(),
//                    new RoundedCorners(25))).into(storeInfo_img);
//            storeInfo_tv_placeName.setText(name);
//        }



    }

}