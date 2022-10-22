package com.example.howabout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

public class CourseInfoActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    View drawerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_info);

        drawerLayout = findViewById(R.id.drawer_layout);
        drawerView = findViewById(R.id.drawer);

        MapView mapView = new MapView(this);

        LinearLayout mapViewContainer = findViewById(R.id.map_mycourse);
        mapViewContainer.addView(mapView);
///////////////////
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
        ///////////////////
        Intent couseintent = getIntent();
        String r_lat = couseintent.getStringExtra("r_lat");
        String r_lon = couseintent.getStringExtra("r_lon");
        String c_lat=couseintent.getStringExtra("c_lat");
        String c_lon=couseintent.getStringExtra("c_lon");
        MapMarker(mapView,"음식점",r_lat,r_lon);
        MapMarker(mapView,"카페",c_lat,c_lon);
//        LocationMove(mapView, r_lat, r_lon);
        Log.i("subin", "음식점 위도: " + r_lat);
        Log.i("subin", "음식점 경도: " + r_lon);
        Log.i("subin", "카페 위도: " + c_lat);
        Log.i("subin", "카페 경도: " + c_lon);

    }

    public void LocationMove(MapView mapView, String r_lat, String r_lon) {
        MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(Double.parseDouble(r_lat), Double.parseDouble(r_lon));
        mapView.setMapCenterPoint(mapPoint, true);
    }
    //일반 마커
    public void MapMarker(MapView mapView, String MakerName, String lat, String lon) {
        MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(Double.parseDouble(lat), Double.parseDouble(lon));
        mapView.setMapCenterPoint(mapPoint, true);
        //true면 앱 실행 시 애니메이션 효과가 나오고 false면 애니메이션이 나오지않음.
        MapPOIItem marker = new MapPOIItem();
        marker.setItemName(MakerName); // 마커 클릭 시 컨테이너에 담길 내용
        marker.setMapPoint(mapPoint);
        // 기본으로 제공하는 BluePin 마커 모양.
        marker.setMarkerType(MapPOIItem.MarkerType.RedPin);
        // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
        marker.setSelectedMarkerType(MapPOIItem.MarkerType.YellowPin);
        mapView.addPOIItem(marker);
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