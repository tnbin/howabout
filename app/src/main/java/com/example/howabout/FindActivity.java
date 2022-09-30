package com.example.howabout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.howabout.Retrofit.RetrofitClient;

import net.daum.android.map.MapViewTouchEventListener;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FindActivity extends AppCompatActivity implements MapView.CurrentLocationEventListener {

    DrawerLayout drawerLayout;
    View drawerView;
    double mCurrentLat;
    double mCurrentLng;
    private MapView mapView;

    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    String[] REQUIRED_PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION};
    private boolean isTrackingMode = false;
    int radius = 300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find);

        drawerLayout = findViewById(R.id.drawer_layout);
        drawerView = findViewById(R.id.drawer);

        final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

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
                Intent intenth = new Intent(FindActivity.this, MainActivity.class);
                startActivity(intenth);
            }
        });
        Button btn_courcebar = findViewById(R.id.btn_courcebar);
        btn_courcebar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawers();
            }
        });

        Button btn_mypagebar = findViewById(R.id.btn_mypagebar);
        btn_mypagebar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawers();
                Intent intentmp = new Intent(FindActivity.this, MyPageActivity.class);
                startActivity(intentmp);
            }
        });
        Button btn_mycourcebar = findViewById(R.id.btn_mycourcebar);
        btn_mycourcebar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawers();
                Intent intentmc = new Intent(FindActivity.this, MyCourseActivity.class);
                startActivity(intentmc);
            }
        });

        mapView = findViewById(R.id.map_view);
        mapView.setCurrentLocationEventListener(this);

        ImageButton mylocation = findViewById(R.id.mylocation);
        mylocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000);
                            //중심을 현재위치로 가져오는 trackingmode
                            if (!checkLocationServicesStatus()) {
                                showDialogForLocationServiceSetting();
                            } else {
                                checkRunTimePermission();
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        isTrackingMode = false;
                    }
                };
                thread.start();
            }
        });

        SeekBar seekBar = findViewById(R.id.seekbar);
        seekBar.setMax(1200);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                if (progress % 100 == 0) {

                } else {
                    seekBar.setProgress((progress / 300) * 300);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                radius = seekBar.getProgress();
                Toast.makeText(FindActivity.this, "반경: " + seekBar.getProgress() + "m 기준입니다.", Toast.LENGTH_SHORT).show();
            }
        });


        Button btn11 = findViewById(R.id.button11);
        btn11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MapPoint mp = mapView.getMapCenterPoint();
                MapPoint.GeoCoordinate gc = mp.getMapPointGeoCoord();

                double gCurrentLat = gc.latitude;
                double gCurrentLog = gc.longitude;

                Log.i("subin", "경도: " + gCurrentLat + "위도: " + gCurrentLog);
            }
        });
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

    public void Marker(String MakerName, double startX, double startY) {
        MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(startY, startX);
        mapView.setMapCenterPoint(mapPoint, true);
        //true면 앱 실행 시 애니메이션 효과가 나오고 false면 애니메이션이 나오지않음.
        MapPOIItem marker = new MapPOIItem();
        marker.setItemName(MakerName); // 마커 클릭 시 컨테이너에 담길 내용
        marker.setMapPoint(mapPoint);
        // 기본으로 제공하는 BluePin 마커 모양.
        marker.setMarkerType(MapPOIItem.MarkerType.RedPin);
        // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
        marker.setSelectedMarkerType(MapPOIItem.MarkerType.BluePin);
        mapView.addPOIItem(marker);
    }

    public void MapMarker(String MakerName, String detail, double startX, double startY) {
        MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(startY, startX);
        mapView.setMapCenterPoint(mapPoint, true);
        //true면 앱 실행 시 애니메이션 효과가 나오고 false면 애니메이션이 나오지않음.
        MapPOIItem marker = new MapPOIItem();
        marker.setItemName(MakerName + "(" + detail + ")"); // 마커 클릭 시 컨테이너에 담길 내용
        marker.setMapPoint(mapPoint);
        // 기본으로 제공하는 BluePin 마커 모양.
        marker.setMarkerType(MapPOIItem.MarkerType.RedPin);
        // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
        marker.setSelectedMarkerType(MapPOIItem.MarkerType.BluePin);
        mapView.addPOIItem(marker);
    }

    //현재위치 업데이트
    @Override
    public void onCurrentLocationUpdate(MapView mapView, MapPoint mapPoint, float v) {
        MapPoint.GeoCoordinate mapPointGeo = mapPoint.getMapPointGeoCoord();
        Log.i("subin", String.format("MapView onCurrentLocationUpdate (%f,%f) accuracy (%f)", mapPointGeo.latitude, mapPointGeo.longitude, v));
        mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithHeading);
        MapPoint currentMapPoint = MapPoint.mapPointWithGeoCoord(mapPointGeo.latitude, mapPointGeo.longitude);
        Log.i("subin", "" + currentMapPoint);
        mapView.setMapCenterPoint(currentMapPoint, true);
        mCurrentLat = mapPointGeo.latitude;
        mCurrentLng = mapPointGeo.longitude;

        Log.i("subin", "Current: " + radius);
        mapView.setCurrentLocationRadius(radius);
        mapView.setCurrentLocationRadiusStrokeColor(Color.argb(128, 255, 87, 87));
        mapView.setCurrentLocationRadiusFillColor(Color.argb(0, 0, 0, 0));

        if (!isTrackingMode) {
            mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOff);
        }
        JSONObject location = new JSONObject();
        try {
            location.put("lat", mCurrentLat);
            location.put("lng", mCurrentLng);
            location.put("radius", radius);
            Log.i("subin", "locationput: " + radius);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ArrayList<JSONObject> arrayList = new ArrayList<>();
        arrayList.add(location);
        Call<Integer> restcource = RetrofitClient.getApiService().restcource(arrayList);
        Log.i("subin", "" + arrayList);
        restcource.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                Integer test = response.body();
                Log.i("subin", "연결성공 :" + test);
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {

                Log.i("subin", "연결실패: " + t.getMessage());
            }
        });
    }

    @Override
    public void onCurrentLocationDeviceHeadingUpdate(MapView mapView, float v) {

    }

    @Override
    public void onCurrentLocationUpdateFailed(MapView mapView) {

    }

    @Override
    public void onCurrentLocationUpdateCancelled(MapView mapView) {

    }

    void checkRunTimePermission() {

        //런타임 퍼미션 처리
        // 1. 위치 퍼미션을 가지고 있는지 체크합니다.
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(FindActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION);


        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED) {

            // 2. 이미 퍼미션을 가지고 있다면
            // ( 안드로이드 6.0 이하 버전은 런타임 퍼미션이 필요없기 때문에 이미 허용된 걸로 인식합니다.)
            // 3.  위치 값을 가져올 수 있음
            mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);

        } else {  //2. 퍼미션 요청을 허용한 적이 없다면 퍼미션 요청이 필요합니다. 2가지 경우(3-1, 4-1)가 있습니다.

            // 3-1. 사용자가 퍼미션 거부를 한 적이 있는 경우에는
            if (ActivityCompat.shouldShowRequestPermissionRationale(FindActivity.this, REQUIRED_PERMISSIONS[0])) {

                // 3-2. 요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유를 설명해줄 필요가 있습니다.
                Toast.makeText(FindActivity.this, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_LONG).show();
                // 3-3. 사용자게에 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(FindActivity.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);

            } else {
                // 4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(FindActivity.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            }
        }
    }

    //여기부터는 GPS 활성화를 위한 메소드들
    //사용자가 gps기능을 활성화할지 여부를 묻는 팝업창을 생성
    private void showDialogForLocationServiceSetting() {

        AlertDialog.Builder builder = new AlertDialog.Builder(FindActivity.this);
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"
                + "위치 설정을 수정하실래요?");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent
                        = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }

    //현재 gps를 제공받을 수 있는 환경인지 체크합니다
    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }
}