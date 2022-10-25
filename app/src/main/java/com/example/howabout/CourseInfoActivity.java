package com.example.howabout;

import static net.daum.mf.map.api.MapPoint.mapPointWithGeoCoord;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapPolyline;
import net.daum.mf.map.api.MapView;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

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
        String c_url = aa.get("c_url").toString();
        String r_url = aa.get("r_url").toString();
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
                Intent intent=new Intent(CourseInfoActivity.this,StoreInfoActivity.class);
                intent.putExtra("r_url",r_url);
                intent.putExtra("r_name",r_name);
                startActivity(intent);
                return false;

            }
        });
        place2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Intent intent=new Intent(CourseInfoActivity.this,StoreInfoActivity.class);
                intent.putExtra("c_url",c_url);
                intent.putExtra("c_name",c_name);
                startActivity(intent);
                return false;
            }
        });
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