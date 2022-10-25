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
import com.example.howabout.category_search.Document;

import org.w3c.dom.Text;

public class StoreInfoActivity extends AppCompatActivity {

    String url = null;
    String name = null;
    String address = null;
    String cat = null;
    String phone = null;

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
        String r_image_url = r_intent.getStringExtra("r_image_url");
        String r_name = r_intent.getStringExtra("r_name");
        String r_address = r_intent.getStringExtra("r_address");
        String r_cat = r_intent.getStringExtra("r_cat");
        String r_phone = r_intent.getStringExtra("r_phone");
        try {
            url = r_image_url;
            name = r_name;
            address = r_address;
            cat = r_cat;
            phone = r_phone;
        } catch (Exception e) {
            e.printStackTrace();
        }

//        Intent c_intent = getIntent();
//        String c_image_url = c_intent.getStringExtra("c_image_url");
//        String c_name = r_intent.getStringExtra("c_name");
//        Log.i("subin", "r_url 인기코스" + r_url);
//        Log.i("subin", "c_url 인기코스" + c_url);
//        url=c_url;
//        name=c_name;

//        ImageView storeInfo_img = findViewById(R.id.storeInfo_img);
//        TextView storeInfo_tv_placeName = findViewById(R.id.storeInfo_tv_placeName);
//        TextView storeInfo_tv_cat = findViewById(R.id.storeInfo_tv_cat);
//        TextView storeInfo_tv_address = findViewById(R.id.storeInfo_tv_address);
//        try {
//            if (url != null || name != null || cat != null) {
//                Log.i("subin", "url: " + url);
//                Glide.with(StoreInfoActivity.this).load(url).placeholder(R.drawable.error_img1).override(Target.SIZE_ORIGINAL).into(storeInfo_img);
//                storeInfo_tv_placeName.setText(name);
//                storeInfo_tv_cat.setText(cat);
//                storeInfo_tv_address.setText(address);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

}