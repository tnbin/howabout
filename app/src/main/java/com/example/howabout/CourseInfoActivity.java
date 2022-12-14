package com.example.howabout;

import static net.daum.mf.map.api.MapPoint.mapPointWithGeoCoord;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.howabout.API.RetrofitClient;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapPolyline;
import net.daum.mf.map.api.MapView;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CourseInfoActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    View drawerView;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_info);

        drawerLayout = findViewById(R.id.drawer_layout);
        drawerView = findViewById(R.id.drawer);

        //Mapview 생성
        MapView mapView = new MapView(this);
        //mapview 가 들어갈 layout
        LinearLayout mapViewContainer = findViewById(R.id.map_mycourse);
        //layout에 mapview 추가
        mapViewContainer.addView(mapView);
/////////////////// drawerLayout
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
                Intent intenth = new Intent(CourseInfoActivity.this, MainActivity.class);
                startActivity(intenth);
            }
        });
        Button btn_courcebar = findViewById(R.id.btn_courcebar);
        btn_courcebar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawers();
                Intent intentc = new Intent(CourseInfoActivity.this, FindActivity.class);
                startActivity(intentc);
            }
        });

        Button btn_mypagebar = findViewById(R.id.btn_mypagebar);
        btn_mypagebar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawers();
                Intent intentmp = new Intent(CourseInfoActivity.this, MyPageActivity.class);
                startActivity(intentmp);
            }
        });
        Button btn_mycourcebar = findViewById(R.id.btn_mycourcebar);
        btn_mycourcebar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawers();
                Intent intentmc = new Intent(CourseInfoActivity.this, MyCourseActivity.class);
                startActivity(intentmc);
            }
        });
        ///////////////////drawlayout end
        //popular에서 보낸 intent 받기
        Intent couseintent = getIntent();
        String jsonObject = couseintent.getStringExtra("storeInfo");
        JSONParser parser = new JSONParser();
        JSONObject aa = null;
        try {
            aa = (JSONObject) parser.parse(jsonObject);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String r_lat = aa.get("r_lat").toString();
        String r_lon = aa.get("r_lon").toString();
        String c_lat = aa.get("c_lat").toString();
        String c_lon = aa.get("c_lon").toString();
        String c_name = aa.get("c_name").toString();
        String r_name = aa.get("r_name").toString();
        String c_image_url = aa.get("c_image_url").toString();
        String r_image_url = aa.get("r_image_url").toString();
        String c_cat = aa.get("c_cat").toString();
        String r_cat = aa.get("r_cat").toString();
        String r_address = aa.get("r_do").toString() + " " + aa.get("r_si").toString() + " " + aa.get("r_gu").toString() + " " + aa.get("r_dong").toString();
        String c_address = aa.get("c_do").toString() + " " + aa.get("c_si").toString() + " " + aa.get("c_gu").toString() + " " + aa.get("c_dong").toString();
        String c_phone=aa.get("c_phone").toString();
        String r_phone=aa.get("r_phone").toString();
        String r_url=aa.get("r_url").toString();
        String c_url=aa.get("c_url").toString();


        //marker 찍기
        MapMarker(mapView, r_name, r_lat, r_lon);
        MapMarker(mapView, c_name, c_lat, c_lon);
        //가게 끼리 선긋기
        Polyline(mapView, r_lat, r_lon, c_lat, c_lon);
        //restaurant
        TextView place1 = findViewById(R.id.place1);
        //cafe
        TextView place2 = findViewById(R.id.place2);
        place1.setText(r_name);
        place2.setText(c_name);
        place1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Information(r_name,r_cat,r_image_url,r_address,r_phone,r_url);
                return false;


            }
        });
        place2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Information(c_name,c_cat,c_image_url,c_address,c_phone,c_url);
                return false;
            }
        });
    }
    public void Information(String name,String cat,String image_url,String address,String phone,String url){

        Map getLocationInfo_data=new HashMap();
        getLocationInfo_data.put("place_url",url);

        Call<Map<String, String>> getLocationInfo = RetrofitClient.getApiService().getLocationInfo(getLocationInfo_data);
        getLocationInfo.enqueue(new Callback<Map<String, String>>() {
            @Override
            public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {

                Log.i("subin",response.body().toString());
            }

            @Override
            public void onFailure(Call<Map<String, String>> call, Throwable t) {

                Log.i("subin","서버 연결 실패 : "+t.getMessage());
            }
        });
        Dialog storeInfo_dialog=new Dialog(CourseInfoActivity.this);
        storeInfo_dialog.setContentView(R.layout.store_info);
        //배경 투명
        storeInfo_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ImageView storeInfo_img = storeInfo_dialog.findViewById(R.id.storeInfo_img);
        TextView storeInfo_tv_placeName = storeInfo_dialog.findViewById(R.id.storeInfo_tv_placeName);
        TextView storeInfo_tv_cat = storeInfo_dialog.findViewById(R.id.storeInfo_tv_cat);
        TextView storeInfo_tv_address = storeInfo_dialog.findViewById(R.id.storeInfo_tv_address);
        TextView storeInfo_tv_phone=storeInfo_dialog.findViewById(R.id.storeInfo_tv_phone);
        TextView storeInfo_tv_url=storeInfo_dialog.findViewById(R.id.storeInfo_tv_url);

        Glide.with(CourseInfoActivity.this).load(image_url).placeholder(R.drawable.error_img1).override(Target.SIZE_ORIGINAL).apply(new RequestOptions().transforms(new CenterCrop(),
                new RoundedCorners(25))).into(storeInfo_img);
        storeInfo_tv_placeName.setText(name);
        storeInfo_tv_cat.setText(cat);
        storeInfo_tv_address.setText("  "+address);
        storeInfo_tv_address.setSelected(true);
        storeInfo_tv_phone.setText("  "+phone);
        storeInfo_tv_url.setText("  "+url);
        storeInfo_tv_url.setSelected(true);

        //창닫기 버튼 클릭 이벤트
        ImageButton cancel = (ImageButton) storeInfo_dialog.findViewById(R.id.storeInfo_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                storeInfo_dialog.dismiss();
            }
        });

        storeInfo_dialog.show();

        //리뷰 서버연결

    }


    public void Polyline(MapView mapView, String r_lat, String r_lon, String c_lat, String c_lon) {
        MapPoint c_mapPoint = mapPointWithGeoCoord(Double.parseDouble(c_lat), Double.parseDouble(c_lon));
        MapPoint r_mapPoint = mapPointWithGeoCoord(Double.parseDouble(r_lat), Double.parseDouble(r_lon));
        MapPolyline mapPolyline = new MapPolyline();
        mapView.removeAllPolylines();
        mapPolyline.setTag(1000);
        mapPolyline.addPoint(r_mapPoint);
        mapPolyline.addPoint(c_mapPoint);
        mapView.addPolyline(mapPolyline);
        mapView.fitMapViewAreaToShowAllPolylines();
    }

    //일반 마커
    public void MapMarker(MapView mapView, String MakerName, String lat, String lon) {
        MapPoint mapPoint = mapPointWithGeoCoord(Double.parseDouble(lat), Double.parseDouble(lon));

        //true면 앱 실행 시 애니메이션 효과가 나오고 false면 애니메이션이 나오지않음.
        MapPOIItem marker = new MapPOIItem();
        marker.setItemName(MakerName); // 마커 클릭 시 컨테이너에 담길 내용
        marker.setMapPoint(mapPoint);
        // 기본으로 제공하는 BluePin 마커 모양.
        marker.setMarkerType(MapPOIItem.MarkerType.RedPin);
        // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
        marker.setSelectedMarkerType(MapPOIItem.MarkerType.YellowPin);
        mapView.addPOIItem(marker);
        mapView.fitMapViewAreaToShowAllPOIItems();
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