package com.example.howabout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.howabout.API.KakaoAPIClient;
import com.example.howabout.API.RetrofitClient;
import com.example.howabout.Search.CategoryResult;
import com.example.howabout.Search.Document;
import com.example.howabout.Search.SearchAdapter;
import com.example.howabout.functions.HowAboutThere;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import net.daum.mf.map.api.CalloutBalloonAdapter;
import net.daum.mf.map.api.CameraUpdateFactory;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapPointBounds;
import net.daum.mf.map.api.MapPolyline;
import net.daum.mf.map.api.MapView;

import org.json.simple.JSONObject;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FindActivity extends AppCompatActivity implements MapView.CurrentLocationEventListener, MapView.MapViewEventListener, MapView.POIItemEventListener {

    SharedPreferences sharedPreferences;

    HowAboutThere FUNC = new HowAboutThere();

    Map<String, Double> first_marker_location; //첫번째 마커 좌표 저장
    Map<String, Document> saveCourse_data = new HashMap<>(); //마커 클릭 시, 마커에 저장된 document 저장 {"rest", "cafe"}
    Map<String, String> getLocationInfo_data; //가게 정보 보기 클릭 시, 서버에 보낼 데이터 저장 {place_name, place_url, place_id}
    ArrayList<Object> result_list; //코스 저장 시, 서버로 보낼 데이터 {u_id, rest document, cafe document}
    Map<String, String> saveMyCourse_data = new HashMap<>();
    JSONObject location; //서버로 보낼 location 정보 저장 (x, y, radius)
    Document rest, cafe;

    //Search keyword
    RecyclerView rl_search; //recycler view
    SearchAdapter searchAdapter; //recycler view에 적용할 adapter
    EditText ed_search; //검색어 입력받는 Edit Text

    //map, marker
    private MapView mapView;
    MapPOIItem marker; //기본 마커. (위치 검색, 현재위치)
    MapPOIItem custom_marker; //리스트로 받아온 식당, 카페 마커.
    MapPolyline polyline; //직선 연결
    Switch aSwitch; //내코스 저장 스위치

    //FloatingActionButton, 애니메이션
    private Animation fab_open, fab_close;
    private Boolean isFabOpen = false;
    private FloatingActionButton fab, fab_currentLoca, fab2;

    //현재위치 권한설정
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    String[] REQUIRED_PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION};

    //트래킹모드
    private boolean isTrackingMode = false;

    //반경
    int radius = 300;

    //코스 찍기 관련 code
    static boolean CODE_1st = false;
    static boolean CODE_2nd = false;
    static boolean CODE_3rd = false;
    static String CODE_flag = "3.0";

    //CalloutBalloonAdapter 인터페이스 구현
    class CustomCalloutBalloonAdapter implements CalloutBalloonAdapter {
        private final View mCalloutBalloon;

        public CustomCalloutBalloonAdapter() {
            mCalloutBalloon = getLayoutInflater().inflate(R.layout.find_dialog_callout_balloon, null);
        }

        @Override
        public View getCalloutBalloon(MapPOIItem mapPOIItem) { //마커 클릭 시 표시할 뷰(말풍선)
            //말풍선에 장소 이름 출력
            ((TextView) mCalloutBalloon.findViewById(R.id.tv_balloon_placename)).setText(mapPOIItem.getItemName());
            return mCalloutBalloon;
        }

        @Override
        public View getPressedCalloutBalloon(MapPOIItem mapPOIItem) { //말풍선 클릭 시 표시할 뷰
            return null;
        }
    } //CustomCalloutBalloonAdapter{}...


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find);

        FUNC.sideBar(FindActivity.this);

        //위치검색.
        rl_search = (RecyclerView) findViewById(R.id.rl_search);
        rl_search.addItemDecoration(new DividerItemDecoration(getApplicationContext(), 0));
        ed_search = findViewById(R.id.ed_search);
        ed_search.addTextChangedListener(textWatcher);

        //FloatingActionButton. 왼쪽 아래 수납 버튼. 선언, 이벤트 처리.
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab_currentLoca = (FloatingActionButton) findViewById(R.id.fab1);
        fab2 = (FloatingActionButton) findViewById(R.id.fab2);

        fab.setOnClickListener(click_fab);
        fab_currentLoca.setOnClickListener(click_fab);
        fab2.setOnClickListener(click_fab);

        //mapview
        mapView = findViewById(R.id.map_view);
        mapView.setCurrentLocationEventListener(this);
        mapView.setMapViewEventListener(this);
        mapView.setPOIItemEventListener(this);
        mapView.fitMapViewAreaToShowAllPOIItems();
        mapView.setCalloutBalloonAdapter(new CustomCalloutBalloonAdapter());

        //save mycourse
        aSwitch = (Switch) findViewById(R.id.find_switch);
        aSwitch.setOnTouchListener(click_switch);
//        aSwitch.setOnCheckedChangeListener(check_switch);
    } //...onCreate()

    //위치검색 텍스트 입력 이벤트 처리
    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            rl_search.setVisibility(View.VISIBLE);
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String keyword = charSequence.toString();
            if (ed_search.getText().toString().length() > 0) {
                mapView.removeAllPolylines();
                mapView.removeAllPOIItems();
                aSwitch.setChecked(false);
                searchKeyword(keyword);
            }
//            else {
//                searchAdapter.clear();
//                searchAdapter.notifyDataSetChanged();
//            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    }; //...textWatcher

    //키워드 검색 함수 ================================================================================
    public void searchKeyword(String keyword) {

        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE); //soft keyboard handling manager
//        String API_KEY = "KakaoAK f33950708cffc6664e99ac21489fd117"; //kakao app key
        String API_KEY = "KakaoAK 66be8808cdde00fd86beab9d744bdffc"; //kakao app key
        Log.e("searchKeyword", "검색 키워드는? : " + keyword);

        Call<CategoryResult> search = KakaoAPIClient.getApiService().getSearchKeword(API_KEY, keyword); //kakap rest api 받아오기
        search.enqueue(new Callback<CategoryResult>() {
            @Override
            public void onResponse(Call<CategoryResult> call, Response<CategoryResult> response) {
//                Log.e("searchKeyword", "post 성공!! 결과값: "+response.body().getDocuments().toString());

                List<Document> result = response.body().getDocuments();
                if (result.size() != 0) { //api 결과가 0이 아니면 adapter에 연결
                    searchAdapter = new SearchAdapter(result);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(FindActivity.this, LinearLayoutManager.VERTICAL, false); //레이아웃매니저 생성
                    rl_search.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));//아래구분선
                    rl_search.setLayoutManager(layoutManager);
                    rl_search.setAdapter(searchAdapter);

                    searchAdapter.setOnItemClickListener(new SearchAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClicked(int position, View view) {
                            rl_search.setVisibility(View.GONE);
                            aSwitch.setVisibility(View.GONE);
                            inputMethodManager.hideSoftInputFromWindow(ed_search.getWindowToken(), 0); //hide keyboard
                            Document document = result.get(position);
                            MapMarker(document.getPlaceName(), Double.parseDouble(document.getX()), Double.parseDouble(document.getY()));
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<CategoryResult> call, Throwable t) {

            }
        });
    } //...searchKeyword()

    //왼쪽 아래에 있는 수납 버튼 이벤트 처리
    View.OnClickListener click_fab = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();
            switch (id) {
                case R.id.fab:
                    anim();
//                    Toast.makeText(FindActivity.this, "Floating Action Button", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.fab1:
                    anim();
                    currentLocation();
                    Toast.makeText(FindActivity.this, "현재위치를 받아오고 있습니다. 잠시만 기다려주세요 😅", Toast.LENGTH_LONG).show();
                    isTrackingMode = false;
                    break;
                case R.id.fab2: //찜하기 기능 구현
                    anim();
                    Toast.makeText(FindActivity.this, "Button2", Toast.LENGTH_SHORT).show();
                    isFabOpen = true;
                    anim();
                    break;
            }
        }
    };

    //햔재 위치 받기
    public void currentLocation() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!checkLocationServicesStatus()) {
                    showDialogForLocationServiceSetting();
                } else {
                    checkRunTimePermission();
                }
            }
        }, 1000);
    }

    //floatingactionbutton animation
    public void anim() {
        if (isFabOpen) {
            fab_currentLoca.startAnimation(fab_close);
            fab2.startAnimation(fab_close);
            fab_currentLoca.setClickable(false);
            fab2.setClickable(false);
            isFabOpen = false;
        } else {
            fab_currentLoca.startAnimation(fab_open);
            fab2.startAnimation(fab_open);
            fab_currentLoca.setClickable(true);
            fab2.setClickable(true);
            isFabOpen = true;
        }
    }

    //선택된 위치 마커찍기
    public void draw_marker(String MarkerName, double x, double y, int image) {
        Log.i("leehj", "marker name, x, y : " + MarkerName + ", " + x + ", " + y);

        MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(y, x);
        mapView.setMapCenterPoint(mapPoint, true);
        marker = new MapPOIItem();
        marker.setItemName(MarkerName);
        marker.setMapPoint(mapPoint);
        marker.setTag(80);
        marker.setMarkerType(MapPOIItem.MarkerType.CustomImage);
        // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
        marker.setCustomImageResourceId(image); // 마커 이미지.
        marker.setCustomImageAutoscale(false); // hdpi, xhdpi 등 안드로이드 플랫폼의 스케일을 사용할 경우 지도 라이브러리의 스케일 기능을 꺼줌.
        marker.setCustomImageAnchor(0.5f, 1.0f); // 마커 이미지중 기준이 되는 위치(앵커포인트) 지정 - 마커 이미지 좌측 상단 기준 x(0.0f ~ 1.0f), y(0.0f ~ 1.0f) 값.
        marker.setShowCalloutBalloonOnTouch(true);
        mapView.addPOIItem(marker);
    }

    //basic marker (현재위치, 검색위치 )===============================================================
    public void MapMarker(String MakerName, double x, double y) {
        CODE_1st = true;
        CODE_2nd = false;

        MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(y, x);
        mapView.setMapCenterPoint(mapPoint, true);
        //true면 앱 실행 시 애니메이션 효과가 나오고 false면 애니메이션이 나오지않음.

        marker = new MapPOIItem();
        marker.setItemName(MakerName); // 마커 클릭 시 컨테이너에 담길 내용
        marker.setMapPoint(mapPoint);
        marker.setTag(60);
        // 기본으로 제공하는 BluePin 마커 모양.
        marker.setMarkerType(MapPOIItem.MarkerType.CustomImage);
        marker.setCustomImageResourceId(R.drawable.location_red);
        marker.setCustomImageAutoscale(false); // hdpi, xhdpi 등 안드로이드 플랫폼의 스케일을 사용할 경우 지도 라이브러리의 스케일 기능을 꺼줌.
//        custom_marker.setCustomImageAnchor(0.5f, 1.0f); // 마커 이미지중 기준이 되는 위치(앵커포인트) 지정 - 마커 이미지 좌측 상단 기준 x(0.0f ~ 1.0f), y(0.0f ~ 1.0f) 값.

        marker.setSelectedMarkerType(MapPOIItem.MarkerType.CustomImage);
        marker.setCustomSelectedImageResourceId(R.drawable.location_blue);
        //마커 드래그 가능하게 설정
        marker.setDraggable(true);
        mapView.addPOIItem(marker);
    }

    //custom marker ================================================================================
    public void customMarker(Document document, int image) {
        String place_name = document.getPlaceName();
        double x = Double.parseDouble(document.getX());
        double y = Double.parseDouble(document.getY());

        MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(y, x);
        mapView.setMapCenterPoint(mapPoint, true);
        //true면 앱 실행 시 애니메이션 효과가 나오고 false면 애니메이션이 나오지않음.
        custom_marker = new MapPOIItem();
        custom_marker.setItemName(place_name); // 마커 클릭 시 컨테이너에 담길 내용
        custom_marker.setUserObject(document);
        custom_marker.setMapPoint(mapPoint);
        // 기본으로 제공하는 BluePin 마커 모양.
        custom_marker.setMarkerType(MapPOIItem.MarkerType.CustomImage);
        // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
        custom_marker.setCustomImageResourceId(image); // 마커 이미지.
        custom_marker.setCustomImageAutoscale(false); // hdpi, xhdpi 등 안드로이드 플랫폼의 스케일을 사용할 경우 지도 라이브러리의 스케일 기능을 꺼줌.
        custom_marker.setCustomImageAnchor(0.5f, 1.0f); // 마커 이미지중 기준이 되는 위치(앵커포인트) 지정 - 마커 이미지 좌측 상단 기준 x(0.0f ~ 1.0f), y(0.0f ~ 1.0f) 값.
        //마커 드래그 가능하게 설정
        custom_marker.setDraggable(true);
        mapView.addPOIItem(custom_marker);
    } //..customMarker

    //현재위치 업데이트 ===============================================================================
    @Override
    public void onCurrentLocationUpdate(MapView mapView, MapPoint mapPoint, float v) {
        MapPoint.GeoCoordinate mapPointGeo = mapPoint.getMapPointGeoCoord();

        double mCurrentLat = mapPointGeo.latitude;
        double mCurrentLng = mapPointGeo.longitude;
        //트래킹 모드
        mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithHeading);

        MapPoint currentMapPoint = MapPoint.mapPointWithGeoCoord(mapPointGeo.latitude, mapPointGeo.longitude);

        mapView.setMapCenterPoint(currentMapPoint, true);
        mapView.setCurrentLocationRadiusStrokeColor(Color.argb(128, 255, 87, 87));
        mapView.setCurrentLocationRadiusFillColor(Color.argb(0, 0, 0, 0));

        mapView.removeAllPOIItems();
        mapView.removeAllPolylines();
        aSwitch.setVisibility(View.GONE);
        MapMarker("현재위치", mCurrentLng, mCurrentLat);

        if (!isTrackingMode) {
            mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOff);
        }
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

    //런타임 위치 퍼미션 처리, 위치 접금 권한
    void checkRunTimePermission() {
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(FindActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION); //위치 퍼미션 확인

        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED) { //이미 위치 퍼미션을 가지고 있다면
            mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading); //현재 위치값 가져오기
        } else {  // 사용자가 퍼미션 요청을 허용한 적이 없는 경우
            if (ActivityCompat.shouldShowRequestPermissionRationale(FindActivity.this, REQUIRED_PERMISSIONS[0])) { //사용자가 퍼미션 거부 이력이 있는 경우
                Toast.makeText(FindActivity.this, "이 기능을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_LONG).show(); //요청 진행 전 필요 이유 설명
                ActivityCompat.requestPermissions(FindActivity.this, REQUIRED_PERMISSIONS, //위치 퍼미션 요청
                        PERMISSIONS_REQUEST_CODE);
            } else { //사용자가 퍼미션 거부 이력이 없는 경우
                ActivityCompat.requestPermissions(FindActivity.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE); //위치 퍼미션 요청
            }
        }
    }

    //GPS 활성화를 위한 메소드 시작+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    //사용자가 gps기능을 활성화할지 여부를 묻는 팝업창을 생성
    private void showDialogForLocationServiceSetting() {
        AlertDialog.Builder builder = new AlertDialog.Builder(FindActivity.this);
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"
                + "위치 설정을 수정하시겠습니까?");
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
    } //GPS 활성화를 위한 메소드 종료+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    @Override
    public void onMapViewInitialized(MapView mapView) {
        MapPoint mp = mapView.getMapCenterPoint();
        MapPoint.GeoCoordinate gc = mp.getMapPointGeoCoord();

        double gCurrentLat = gc.latitude;
        double gCurrentLog = gc.longitude;
    }

    @Override
    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) {
    }

    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int i) {
    }

    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {
    }

    @Override
    public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {
    }

    @Override
    public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {
    }

    @Override
    public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {
    }

    @Override
    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {
    }

    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {
        MapPoint mp = mapView.getMapCenterPoint();
        MapPoint.GeoCoordinate gc = mp.getMapPointGeoCoord();

        double gCurrentLat = gc.latitude;
        double gCurrentLog = gc.longitude;

        Log.i("leehj", "지도 드래그 끝날 시 경도: " + gCurrentLat + "위도: " + gCurrentLog + "반경" + radius);
    }

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {
    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {
    }

    //말풍선 터치 이벤트 처리 ==========================================================================
    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {
        String place_name = mapPOIItem.getItemName();
        double lat = mapPOIItem.getMapPoint().getMapPointGeoCoord().latitude; //위도, y
        double lon = mapPOIItem.getMapPoint().getMapPointGeoCoord().longitude; //경도, x
        Toast.makeText(this, "장소: " + mapPOIItem.getItemName(), Toast.LENGTH_SHORT).show();

        if (CODE_1st) { //처음 찍히는 MapMarker balloon touched
            Dialog dialog = new Dialog(FindActivity.this);
            dialog.setContentView(R.layout.find_dialog_first_balloon_click);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            first_marker_location = new HashMap();
            first_marker_location.put("x", lon);
            first_marker_location.put("y", lat);

            //음식점 선택
            TextView rest = (TextView) dialog.findViewById(R.id.dialog_click_rest);
            rest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.cancel();
                    mapView.removeAllPOIItems();
                    draw_marker("선택한 위치", first_marker_location.get("x"), first_marker_location.get("y"), R.drawable.location_blue);
                    SearchRestaurant(String.valueOf(lon), String.valueOf(lat), String.valueOf(radius));
                }
            });

            //카페 선택
            TextView cafe = (TextView) dialog.findViewById(R.id.dialog_click_cafe);
            cafe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.cancel();
                    mapView.removeAllPOIItems();
                    draw_marker("선택한 위치", first_marker_location.get("x"), first_marker_location.get("y"), R.drawable.location_blue);
                    SearchCafe(String.valueOf(lon), String.valueOf(lat), String.valueOf(radius));
                }
            });
            dialog.show();
        } else { //NOT 처음 찍히는 MapMarker balloon touched
            Dialog dialog_2st = new Dialog(FindActivity.this);
            dialog_2st.setContentView(R.layout.find_dialog_balloon_click);
            dialog_2st.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            ImageButton select = (ImageButton) dialog_2st.findViewById(R.id.balloon_click_btn_check);
            select.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog_2st.dismiss();
                    if (CODE_2nd && !CODE_3rd) { //식당을 먼저 선택한 경우
                        Document rest_document = (Document) mapPOIItem.getUserObject(); //클릭 마커 Dcument Object 가져오기
                        saveCourse_data.put("rest", rest_document); // 식당 정보 Map에 저장
                        saveMyCourse_data.put("r_id", rest_document.getId());
                        mapView.removeAllPOIItems(); //맵 마커 초기화
                        draw_marker("선택한 위치", first_marker_location.get("x"), first_marker_location.get("y"), R.drawable.location_blue); //처음 선택한 위치 마커 찍기
                        draw_marker(rest_document.getPlaceName(), Double.parseDouble(rest_document.getX()), Double.parseDouble(rest_document.getY()), R.drawable.location_rest_blue); //먼저 선택한 식당 마커 찍기
                        SearchCafe(String.valueOf(lon), String.valueOf(lat), String.valueOf(radius)); //식당 주변 카페 리스트 마커 표시
                        Log.e("leehj", "식당 이름 : " + place_name + ", 식당 좌표: " + lon + ", " + lat);

                    } else if (!CODE_2nd && CODE_3rd) { //카페를 먼저 선택한 경우
                        Document cafe_document = (Document) mapPOIItem.getUserObject(); //클릭 마커 Document Object 가져오기
                        saveCourse_data.put("cafe", cafe_document); //카페 정보 Map에 저장
                        saveMyCourse_data.put("c_id", cafe_document.getId());
                        mapView.removeAllPOIItems(); //맵 마커 초기화
                        draw_marker("선택한 위치", first_marker_location.get("x"), first_marker_location.get("y"), R.drawable.location_blue); //처음 선택한 위치 마커 찍기
                        draw_marker(cafe_document.getPlaceName(), Double.parseDouble(cafe_document.getX()), Double.parseDouble(cafe_document.getY()), R.drawable.location_cafe_blue); //먼저 선택한 카페 마커 찍기
                        SearchRestaurant(String.valueOf(lon), String.valueOf(lat), String.valueOf(radius)); //카페 주변 식당 리스트 마커 표시
                        Log.e("leehj", "카페 이름 : " + place_name + ", 카페 좌표: " + lon + ", " + lat);

                    } else if (CODE_2nd && CODE_3rd) { //카페, 식당 모두 선택
                        //CODE 초기화
                        CODE_1st = false;
                        CODE_2nd = false;
                        CODE_3rd = false;

                        mapView.removeAllPOIItems();
                        polyline = new MapPolyline();
                        polyline.setTag(1000);
//                        polyline.setLineColor();

                        if (saveCourse_data.containsKey("rest")) { //식당 좌표가 있는 경우. 식당을 먼저 선택한 경우
                            Document cafe_document = (Document) mapPOIItem.getUserObject();
                            saveCourse_data.put("cafe", cafe_document);
                            saveMyCourse_data.put("c_id", cafe_document.getId());
                            polyline.addPoint(MapPoint.mapPointWithGeoCoord(first_marker_location.get("y"), first_marker_location.get("x")));
                            polyline.addPoint(MapPoint.mapPointWithGeoCoord(Double.parseDouble(saveCourse_data.get("rest").getY()), Double.parseDouble(saveCourse_data.get("rest").getX())));
                            polyline.addPoint(MapPoint.mapPointWithGeoCoord(Double.parseDouble(saveCourse_data.get("cafe").getY()), Double.parseDouble(saveCourse_data.get("cafe").getX())));
                        } else { //식당 좌표가 있는 경우. 카페를 먼저 선택한 경우
                            Document rest_document = (Document) mapPOIItem.getUserObject();
                            saveCourse_data.put("rest", rest_document);
                            saveMyCourse_data.put("r_id", rest_document.getId());
                            polyline.addPoint(MapPoint.mapPointWithGeoCoord(first_marker_location.get("y"), first_marker_location.get("x")));
                            polyline.addPoint(MapPoint.mapPointWithGeoCoord(Double.parseDouble(saveCourse_data.get("cafe").getY()), Double.parseDouble(saveCourse_data.get("cafe").getX())));
                            polyline.addPoint(MapPoint.mapPointWithGeoCoord(Double.parseDouble(saveCourse_data.get("rest").getY()), Double.parseDouble(saveCourse_data.get("rest").getX())));
                        }

                        //위치, 식당, 카페 마커 찍기
                        Document rest_marker = (Document) saveCourse_data.get("rest");
                        Document cafe_marker = (Document) saveCourse_data.get("cafe");

                        draw_marker("선택한 위치", first_marker_location.get("x"), first_marker_location.get("y"), R.drawable.location_blue);
                        draw_marker(rest_marker.getPlaceName(), Double.parseDouble(rest_marker.getX()), Double.parseDouble(rest_marker.getY()), R.drawable.location_rest_blue);
                        draw_marker(cafe_marker.getPlaceName(), Double.parseDouble(cafe_marker.getX()), Double.parseDouble(cafe_marker.getY()), R.drawable.location_cafe_blue);

                        //맵에 연결한 직선 표시
                        mapView.addPolyline(polyline);
//                        aSwitch.setChecked(false);
//                        aSwitch.setClickable(true);
                        aSwitch.setVisibility(View.VISIBLE);

                        //직선 연결 옵션
                        MapPointBounds mapPointBounds = new MapPointBounds(polyline.getMapPoints());
                        int padding = 400; // px
//                        mapView.fitMapViewAreaToShowAllPolylines();
                        mapView.moveCamera(CameraUpdateFactory.newMapPointBounds(mapPointBounds, padding));

                        sharedPreferences = getSharedPreferences("USER", Activity.MODE_PRIVATE);
                        String token = sharedPreferences.getString("token", null);
//                        String request_token = "Bearer "+token;
                        Log.i("leehj", "token: " + token);

                        if (token != null) { //로그인을 한 경우
                            Log.i("leehj", "로그인을 한 사용자 입니다. 코스 데이터를 서버에 저장합니다.");
                            //서버로 코스 데이터 전송. 1. u_id list에 담기 2. rest api로 데이터 받아서 저장 *****
                            result_list = new ArrayList<>(2);

                            rest = saveCourse_data.get("rest"); //데이터 정상적으로 가는지 확인할 것
                            cafe = saveCourse_data.get("cafe");

                            result_list.add(0, rest);
                            result_list.add(1, cafe);

                            Log.e("leehj", "result list rest index 1: " + result_list.get(0).toString()); //@@@
                            Log.e("leehj", "result list cafe index 2: " + result_list.get(1).toString()); //@@@
                            //**************************************************************************

//                            new Handler().postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
                            //서버로 데이터 요청 -- r_id, c_id
//                                sharedPreferences = getSharedPreferences("USER", Activity.MODE_PRIVATE);
//                                String token = "Bearer "+sharedPreferences.getString("token", null);
//                                Log.i("leehj", "token: " + token);
                            String request_token = "Bearer " + token;
                            Log.e("leehj", "\n코스 저장 데이터 요청하기 =============================");
                            Call<Map<String, String>> saveCourse = RetrofitClient.getApiService().saveCourse(result_list, request_token);
                            saveCourse.enqueue(new Callback<Map<String, String>>() {
                                @Override
                                public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                                    if (response.isSuccessful()) {
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                Log.e("leehj", "데이터 보내졌다구,,!");
                                                Log.i("leehj", "save course response: " + response.body());
                                                Log.i("leehj", "save course response - flag: " + response.body().get("flag"));
                                                Log.i("leehj", "save course - token: " + request_token);
                                                CODE_flag = response.body().get("flag");

                                                if (CODE_flag.equals("1")) {
                                                    aSwitch.setChecked(true);
                                                    Toast.makeText(FindActivity.this, "내 코스에 저장되어 있는 코스입니다 😗", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    aSwitch.setChecked(false);
                                                }
//                                                    if(response.body() != null) {
//                                                        Map<String, String> result = response.body();
//                                                        saveMyCourse_data.put("r_id", result.get("r_id")); //서버 응답값 오면 풀어주세요
//                                                        saveMyCourse_data.put("c_id", result.get("c_id"));
//                                                    }
                                            }
                                        }, 1000);
                                    }
                                }

                                @Override
                                public void onFailure(Call<Map<String, String>> call, Throwable t) {
                                }
                            });
//                                }
//                            }, 1000 * 2); //2초 딜레이 준 후 실행
                        }
                    }
                }
            }); //...장소 선택 버튼 클릭 이벤트

            //가게 정보 보기 (음식점, 카페 모두 동일)
            TextView storeInfo = (TextView) dialog_2st.findViewById(R.id.balloon_click_placeInfo);
            storeInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Dialog loading_dialog = new Dialog(FindActivity.this);
                    loading_dialog.setContentView(R.layout.find_loading_dialog);
                    loading_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    loading_dialog.setCancelable(false);
                    loading_dialog.show();


//                    dialog_2st.cancel();
                    Document marker_document = (Document) mapPOIItem.getUserObject();
//                    Log.i("leehj", "가게 정보 보기 marker document: " + marker_document.toString()); //@@@

                    //서버 필요 데이터
                    getLocationInfo_data = new HashMap<>();
//                    getLocationInfo_data.put("place_name", marker_document.getPlaceName());
                    getLocationInfo_data.put("place_url", marker_document.getPlaceUrl());
//                    getLocationInfo_data.put("place_id", marker_document.getId());

                    Log.i("leehj", "가게 정보 보기 Map data"); //@@@
//                    Log.i("leehj", "가게 정보 보기 place_name: " + getLocationInfo_data.get("place_name")); //@@@
                    Log.i("leehj", "가게 정보 보기 place_url: " + getLocationInfo_data.get("place_url")); //@@@
//                    Log.i("leehj", "가게 정보 보기 place_id: " + getLocationInfo_data.get("place_id")); //@@@

                    //서버에서 정보 받아오기
                    Call<Map<String, String>> getLocationInfo = RetrofitClient.getApiService().getLocationInfo(getLocationInfo_data);
                    getLocationInfo.enqueue(new Callback<Map<String, String>>() {
                        @Override
                        public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                            Map<String, String> result = response.body();
                            Log.e("leehj", "가게 정보 Map data: " + result.toString());

                            //store info dialog
                            Dialog storeInfo_dialog = new Dialog(FindActivity.this);
                            storeInfo_dialog.setContentView(R.layout.store_info);
                            storeInfo_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                            //data =================================================================
                            String placeName = marker_document.getPlaceName();
                            String address = marker_document.getAddressName();
                            String category = marker_document.getCategoryName();
                            String phone = marker_document.getPhone();
                            String storeUrl = marker_document.getPlaceUrl();

                            String url = "http:" + result.get("imgUrl"); //response data img Url 가져오기
                            Log.e("leehj", "img url : " + url);
                            String review1 = result.get("review_1");
                            String review2 = result.get("review_2");
                            String review3 = result.get("review_3");
                            String storeTime = result.get("storeTime");
                            String starpoint = result.get("startpoint");

                            //선언 & 세팅 ============================================================
//=================================================================================================================================================================================
                            Log.i("leehj", "marker document print : " + marker_document.toString());
                            Log.i("leehj", "response print: " + result.toString());

                            loading_dialog.dismiss();

                            ImageView img = (ImageView) storeInfo_dialog.findViewById(R.id.storeInfo_img);
//                            Glide.with(storeInfo_dialog.getContext()).load(url).into(img);
                            Glide.with(storeInfo_dialog.getContext()).load(url).placeholder(R.drawable.rabbit_and_bear).override(Target.SIZE_ORIGINAL).apply(new RequestOptions().transforms(new CenterCrop(),
                                    new RoundedCorners(25))).into(img);

                            TextView tv_place_name = (TextView) storeInfo_dialog.findViewById(R.id.storeInfo_tv_placeName);
                            TextView tv_category = (TextView) storeInfo_dialog.findViewById(R.id.storeInfo_tv_cat);
                            TextView tv_address = (TextView) storeInfo_dialog.findViewById(R.id.storeInfo_tv_address);
                            TextView tv_tel = (TextView) storeInfo_dialog.findViewById(R.id.storeInfo_tv_phone);
                            TextView tv_store_url = (TextView) storeInfo_dialog.findViewById(R.id.storeInfo_tv_url);
                            TextView tv_time = (TextView) storeInfo_dialog.findViewById(R.id.storeInfo_tv_time);
                            TextView tv_review1 = (TextView) storeInfo_dialog.findViewById(R.id.storeInfo_tv_reivew1);
                            TextView tv_review2 = (TextView) storeInfo_dialog.findViewById(R.id.storeInfo_tv_reivew2);
                            TextView tv_review3 = (TextView) storeInfo_dialog.findViewById(R.id.storeInfo_tv_reivew3);
                            Button storeInfo_btn_phone = storeInfo_dialog.findViewById(R.id.storeInfo_btn_phone);
                            Button storeInfo_btn_road = storeInfo_dialog.findViewById(R.id.storeInfo_btn_road);


                            tv_place_name.setText(placeName);
                            tv_category.setText(category);
                            tv_address.setText(" " + address);
                            tv_tel.setText(" " + phone);
                            tv_store_url.setText(" " + storeUrl);
                            tv_time.setText(" " + storeTime);
                            tv_review1.setText(review1);
                            tv_review2.setText(review2);
                            tv_review3.setText(review3);

                            //창닫기 버튼 클릭 이벤트 ==================================================
                            ImageButton cancel = (ImageButton) storeInfo_dialog.findViewById(R.id.storeInfo_cancel);
                            cancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    storeInfo_dialog.dismiss();
                                }
                            });

                            //전화 다이얼
                            storeInfo_btn_phone.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent telintent = new Intent(Intent.ACTION_DIAL);
                                    telintent.setData(Uri.parse("tel:" + phone));
                                    startActivity(telintent);
                                }
                            });

                            storeInfo_btn_road.setVisibility(View.GONE);
                            storeInfo_dialog.show();
                        }

                        @Override
                        public void onFailure(Call<Map<String, String>> call, Throwable t) {

                        }
                    });
                }
            });
            dialog_2st.show();
        } //...식당, 카페 마커 말풍선 클릭 이벤트 else{}

    } //...onCalloutBalloonOfPOIItemTouched()

    //==============================================================================================
    @Override
    public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {
        MapPoint.GeoCoordinate mapPointGeo = mapPoint.getMapPointGeoCoord();
        double mSearchLng = mapPointGeo.longitude;
        double mSearchLat = mapPointGeo.latitude;
        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(mSearchLat, mSearchLng), true);
        mapView.removeAllPOIItems();
        mapView.removeAllPolylines();
        aSwitch.setVisibility(View.GONE);
        MapMarker("드래그한 장소", mSearchLng, mSearchLat);
    }

    //식당 리스트 가져오기 =============================================================================
    public void SearchRestaurant(String x, String y, String radius) {
        CODE_1st = false;
        CODE_2nd = true;

        location = new JSONObject();
        try {
            location.put("x", x);
            location.put("y", y);
            location.put("radius", radius);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ArrayList<JSONObject> arrayList = new ArrayList<>();
        arrayList.add(location);
        Call<ArrayList<CategoryResult>> cafe = RetrofitClient.getApiService().rest(arrayList);
        cafe.enqueue(new Callback<ArrayList<CategoryResult>>() {
            @Override
            public void onResponse(Call<ArrayList<CategoryResult>> call, Response<ArrayList<CategoryResult>> response) {

                List<Document> documents = response.body().get(0).getDocuments();//document의 리스트

                if (documents.size() != 0) {
                    Log.e("leehj", "식당 리스트 첫번째 데이터 : " + documents.get(0).toString());
                    for (int i = 0; i < documents.size(); i++) {
                        customMarker(documents.get(i), R.drawable.location_rest);
                    }
                    mapView.setCurrentLocationRadius(Integer.parseInt(radius));
                    mapView.setCurrentLocationRadiusStrokeColor(Color.argb(128, 255, 87, 87));
                    mapView.setCurrentLocationRadiusFillColor(Color.argb(0, 0, 0, 0));
                } else {
                    Toast.makeText(FindActivity.this, "주변에 식당이 존재하지 않습니다. 다른 곳에서 찾아주세요 🥲", Toast.LENGTH_LONG).show();
                    CODE_1st = true;
                    CODE_2nd = false;
                    CODE_3rd = false;
                }
            }

            @Override
            public void onFailure(Call<ArrayList<CategoryResult>> call, Throwable t) {
                Log.i("leehj", "rest list data loading failed!! : " + t.getMessage());
            }
        });
    }

    //카페 리스트 가져오기 =============================================================================
    public void SearchCafe(String x, String y, String radius) {
        CODE_1st = false;
        CODE_3rd = true;

        location = new JSONObject();
        try {
            location.put("x", x);
            location.put("y", y);
            location.put("radius", radius);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ArrayList<JSONObject> arrayList = new ArrayList<>();
        arrayList.add(location);
        Call<ArrayList<CategoryResult>> cafe = RetrofitClient.getApiService().cafe(arrayList);
        cafe.enqueue(new Callback<ArrayList<CategoryResult>>() {
            @Override
            public void onResponse(Call<ArrayList<CategoryResult>> call, Response<ArrayList<CategoryResult>> response) {
                List<Document> documents = response.body().get(0).getDocuments();//document의 리스트

                if (documents.size() != 0) {
                    Log.e("leehj", "카페 리스트 첫번째 데이터 : " + documents.get(0).toString());
                    for (int i = 0; i < documents.size(); i++) {
                        customMarker(documents.get(i), R.drawable.location_cafe);
                    }
                    mapView.setCurrentLocationRadius(Integer.parseInt(radius));
                    mapView.setCurrentLocationRadiusStrokeColor(Color.argb(128, 255, 87, 87));
                    mapView.setCurrentLocationRadiusFillColor(Color.argb(0, 0, 0, 0));
                } else {
                    Toast.makeText(FindActivity.this, "주변에 카페가 존재하지 않습니다. 다른 곳에서 찾아주세요 🥲", Toast.LENGTH_LONG).show();
                    CODE_1st = true;
                    CODE_2nd = false;
                    CODE_3rd = false;
                }
            }

            @Override
            public void onFailure(Call<ArrayList<CategoryResult>> call, Throwable t) {
                Log.i("leehj", "cafe list data loading failed!! : " + t.getMessage());
            }
        });
    } //...SearchCafe()

    View.OnTouchListener click_switch = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            sharedPreferences = getSharedPreferences("USER", Activity.MODE_PRIVATE);
            String token = sharedPreferences.getString("token", null);
            if (token != null) {
                Log.i("leehj", "token: " + token);
                aSwitch.setOnCheckedChangeListener(check_switch);
                return false;
            } else {
                Toast.makeText(FindActivity.this, "로그인 후 이용 가능한 서비스입니다.", Toast.LENGTH_SHORT).show();
                return true;
            }
        }
    };


    //save myCourse event
    Switch.OnCheckedChangeListener check_switch = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            sharedPreferences = getSharedPreferences("USER", Activity.MODE_PRIVATE);
            String token = sharedPreferences.getString("token", null);
            String request_token = "Bearer " + token;
//            if(token != null){
//                Log.i("leehj", "token: " + token);

            if (b) { //switch on!!
                if (CODE_flag.equals("0")) {//저장하지 않은 코스이면
                    Log.e("leehj", "스위치 on 서버에 내코스 저장 해요!!");
//                saveMyCourse_data.put("u_id", "leehj");
                    Call<Integer> save_myCourse = RetrofitClient.getApiService().courseDibs(saveMyCourse_data, request_token);
                    save_myCourse.enqueue(new Callback<Integer>() {
                        @Override
                        public void onResponse(Call<Integer> call, Response<Integer> response) {
                            Log.i("leehj", "성공 시 return value는 1: " + response.body());
                            Toast.makeText(FindActivity.this, "내 코스에 저장됐습니다.", Toast.LENGTH_SHORT).show();
//                            compoundButton.setClickable(false);
                        }

                        @Override
                        public void onFailure(Call<Integer> call, Throwable t) {

                        }
                    });
                }
            } else { //switch off!!
                CODE_flag = "0";
                Log.e("leehj", "스위치 off!!! 서버에 내코스 삭제해요");
                //내 코스 삭제
                Call<Integer> save_myCourse = RetrofitClient.getApiService().courseDibs(saveMyCourse_data, request_token);
                save_myCourse.enqueue(new Callback<Integer>() {
                    @Override
                    public void onResponse(Call<Integer> call, Response<Integer> response) {
                        Log.i("leehj", "성공 시 return value는 1: " + response.body());
                        Toast.makeText(FindActivity.this, "내 코스에서 삭제 되었습니다.", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<Integer> call, Throwable t) {

                    }
                });
            }
        }
    };
}
