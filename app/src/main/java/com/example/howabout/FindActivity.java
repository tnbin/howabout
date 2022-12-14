package com.example.howabout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.howabout.API.KakaoAPIClient;
import com.example.howabout.API.KakaoAPIService;
import com.example.howabout.API.RetrofitClient;
import com.example.howabout.category_search.BusProvider;
import com.example.howabout.category_search.CategoryResult;
import com.example.howabout.category_search.MyAdatpter;
import com.example.howabout.category_search.Document;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FindActivity extends AppCompatActivity implements MapView.CurrentLocationEventListener, MapView.MapViewEventListener, MapView.POIItemEventListener, View.OnClickListener {

    //drawerlayout
    DrawerLayout drawerLayout;
    View drawerView;
    //??????,?????? ??????
    String SearchName;
    double mCurrentLat;
    double mCurrentLng;
    double mSearchLng;
    double mSearchLat;
    String search;
    //??????
    ViewGroup map;
    MapView mapView;
    RecyclerView rl_search;
    //???????????????
    private Animation fab_open, fab_close;
    private Boolean isFabOpen = false;
    private FloatingActionButton fab, fab1, fab2;
    //???????????? ????????????
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    String[] REQUIRED_PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION};
    //???????????????
    private boolean isTrackingMode = false;
    //??????
    int radius = 300;
    //??????
    EditText ed_search;
    MyAdatpter myAdatpter;
    ArrayList<Document> documentArrayList = new ArrayList<>();
    Bus bus = BusProvider.getInstance();
    //??????
    MapPOIItem searchMarker = new MapPOIItem();
    //    SharedPreferences preferences;
    //??????,?????????
    ArrayList<JSONObject> cafeList = new ArrayList<JSONObject>();  //CE7 ??????
    ArrayList<JSONObject> restaurantList = new ArrayList<JSONObject>(); //FD6 ?????????
    //??? ???
    String API_KEY = "KakaoAK f33950708cffc6664e99ac21489fd117";

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find);

        drawerLayout = findViewById(R.id.drawer_layout);

        drawerView = findViewById(R.id.drawer);

        rl_search = findViewById(R.id.rl_search);
        ed_search = findViewById(R.id.ed_search);
        search = ed_search.getText().toString();

        bus.register(this); //????????? ??????
        //?????? ?????????
        myAdatpter = new MyAdatpter(documentArrayList, getApplicationContext(), ed_search, rl_search);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false); //????????????????????? ??????
        rl_search.addItemDecoration(new DividerItemDecoration(getApplicationContext(), 0));//???????????????
        rl_search.setLayoutManager(layoutManager);
        rl_search.setAdapter(myAdatpter);
        //????????? ??????
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        fab2 = (FloatingActionButton) findViewById(R.id.fab2);

        fab.setOnClickListener(this);
        fab1.setOnClickListener(this);
        fab2.setOnClickListener(this);

        //drawerLayout
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

        //mapview ??????
        mapView = new MapView(this);
        map = findViewById(R.id.map_view);
        map.addView(mapView);
        mapView.setCurrentLocationEventListener(this);
        mapView.setMapViewEventListener(this);
        mapView.setPOIItemEventListener(this);
//        preferences = getSharedPreferences("URL", MODE_PRIVATE);
        //???????????? ???????????? ??????
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000);
                            //????????? ??????????????? ???????????? trackingmode
                            //???????????? ????????? ?????????
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
        //SeekBar ??????
        SeekBar seekBar = findViewById(R.id.seekbar);
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
                Toast.makeText(FindActivity.this, "??????: " + seekBar.getProgress() + "m ???????????????.", Toast.LENGTH_SHORT).show();
            }
        });
        //editText ????????? ?????????
        ed_search.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                rl_search.setVisibility(View.VISIBLE);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                search = ed_search.getText().toString();
                if (charSequence.length() >= 1) {
                    documentArrayList.clear();
                    myAdatpter.clear();
                    myAdatpter.notifyDataSetChanged();
                    rl_search.setVisibility(View.VISIBLE);
                    Log.i("subin", search);
                    searchKeyword(search);

                } else {
                    if (charSequence.length() <= 0) {
                        rl_search.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        ImageButton btn_search = findViewById(R.id.btn_search);
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    //?????? ????????? ????????? ??????
    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.fab:
                anim();
                Toast.makeText(this, "1.?????? ?????? 2. ???????????? ?????? ", Toast.LENGTH_SHORT).show();
                break;
            case R.id.fab1:
                anim();
                Toast.makeText(this, "Button1", Toast.LENGTH_SHORT).show();
                break;
            case R.id.fab2:
                anim();
                Toast.makeText(this, "Button2", Toast.LENGTH_SHORT).show();
                isFabOpen = true;
                anim();
                break;
        }
    }

    //?????? ???????????????
    public void anim() {

        if (isFabOpen) {
            fab1.startAnimation(fab_close);
            fab2.startAnimation(fab_close);
            fab1.setClickable(false);
            fab2.setClickable(false);
            isFabOpen = false;
        } else {
            fab1.startAnimation(fab_open);
            fab2.startAnimation(fab_open);
            fab1.setClickable(true);
            fab2.setClickable(true);
            isFabOpen = true;
        }
    }

    //drawerlayout
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

    //?????? ??????
    public void MapMarker(String MakerName, double startX, double startY) {
        MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(startY, startX);
        mapView.setMapCenterPoint(mapPoint, true);
        //true??? ??? ?????? ??? ??????????????? ????????? ????????? false??? ?????????????????? ???????????????.
        MapPOIItem marker = new MapPOIItem();
        marker.setItemName(MakerName); // ?????? ?????? ??? ??????????????? ?????? ??????
        marker.setMapPoint(mapPoint);
        // ???????????? ???????????? BluePin ?????? ??????.
        marker.setMarkerType(MapPOIItem.MarkerType.RedPin);
        // ????????? ???????????????, ???????????? ???????????? RedPin ?????? ??????.
        marker.setSelectedMarkerType(MapPOIItem.MarkerType.YellowPin);
        mapView.addPOIItem(marker);
        //?????? ????????? ???????????? ??????
        marker.setDraggable(true);
        mapView.addPOIItem(marker);
    }

    //????????? ??????
    public void CustomMarker(String markername, double x, double y, int image) {
        MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(y, x);
        mapView.setMapCenterPoint(mapPoint, true);
        //true??? ??? ?????? ??? ??????????????? ????????? ????????? false??? ?????????????????? ???????????????.
        MapPOIItem marker = new MapPOIItem();
        marker.setItemName(markername); // ?????? ?????? ??? ??????????????? ?????? ??????
        marker.setMapPoint(mapPoint);
        // ???????????? ???????????? BluePin ?????? ??????.
        marker.setMarkerType(MapPOIItem.MarkerType.CustomImage);
        // ????????? ???????????????, ???????????? ???????????? RedPin ?????? ??????.
        marker.setCustomImageResourceId(image); // ?????? ?????????.
        marker.setCustomImageAutoscale(true); // hdpi, xhdpi ??? ??????????????? ???????????? ???????????? ????????? ?????? ?????? ?????????????????? ????????? ????????? ??????.
        marker.setCustomImageAnchor(0.5f, 1.0f); // ?????? ???????????? ????????? ?????? ??????(???????????????) ?????? - ?????? ????????? ?????? ?????? ?????? x(0.0f ~ 1.0f), y(0.0f ~ 1.0f) ???.
        mapView.addPOIItem(marker);
        //?????? ????????? ???????????? ??????
        marker.setDraggable(true);
        mapView.addPOIItem(marker);
    }

    //???????????? ????????????
    @Override
    public void onCurrentLocationUpdate(MapView mapView, MapPoint mapPoint, float v) {
        MapPoint.GeoCoordinate mapPointGeo = mapPoint.getMapPointGeoCoord();

        mCurrentLat = mapPointGeo.latitude;
        mCurrentLng = mapPointGeo.longitude;
        //????????? ??????
        mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithHeading);

        MapPoint currentMapPoint = MapPoint.mapPointWithGeoCoord(mapPointGeo.latitude, mapPointGeo.longitude);

        mapView.setMapCenterPoint(currentMapPoint, true);
        //??????
        Log.i("subin", "Current: " + radius);
        mapView.setCurrentLocationRadius(radius);
        mapView.setCurrentLocationRadiusStrokeColor(Color.argb(128, 255, 87, 87));
        mapView.setCurrentLocationRadiusFillColor(Color.argb(0, 0, 0, 0));

        MapMarker("????????????", mCurrentLng, mCurrentLat);

        if (!isTrackingMode) {
            mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOff);
        }
        JSONObject location = new JSONObject();
        try {
            location.put("lat", mCurrentLat);
            location.put("lng", mCurrentLng);
            location.put("radius", radius);
        } catch (Exception e) {
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
                Log.i("subin", "?????? ?????? ???????????? :" + test);
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {

                Log.i("subin", "????????????: " + t.getMessage());
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

        //????????? ????????? ??????
        // 1. ?????? ???????????? ????????? ????????? ???????????????.
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(FindActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION);


        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED) {

            // 2. ?????? ???????????? ????????? ?????????
            // ( ??????????????? 6.0 ?????? ????????? ????????? ???????????? ???????????? ????????? ?????? ????????? ?????? ???????????????.)
            // 3.  ?????? ?????? ????????? ??? ??????
            //????????? ?????? TrackingModeOnWithoutHeading
            mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);

        } else {  //2. ????????? ????????? ????????? ?????? ????????? ????????? ????????? ???????????????. 2?????? ??????(3-1, 4-1)??? ????????????.

            // 3-1. ???????????? ????????? ????????? ??? ?????? ?????? ????????????
            if (ActivityCompat.shouldShowRequestPermissionRationale(FindActivity.this, REQUIRED_PERMISSIONS[0])) {

                // 3-2. ????????? ???????????? ?????? ?????????????????? ???????????? ????????? ????????? ???????????? ????????? ????????????.
                Toast.makeText(FindActivity.this, "??? ?????? ??????????????? ?????? ?????? ????????? ???????????????.", Toast.LENGTH_LONG).show();
                // 3-3. ??????????????? ????????? ????????? ?????????. ?????? ????????? onRequestPermissionResult?????? ???????????????.
                ActivityCompat.requestPermissions(FindActivity.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);

            } else {
                // 4-1. ???????????? ????????? ????????? ??? ?????? ?????? ???????????? ????????? ????????? ?????? ?????????.
                // ?????? ????????? onRequestPermissionResult?????? ???????????????.
                ActivityCompat.requestPermissions(FindActivity.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            }
        }
    }

    //??????????????? GPS ???????????? ?????? ????????????
    //???????????? gps????????? ??????????????? ????????? ?????? ???????????? ??????
    private void showDialogForLocationServiceSetting() {

        AlertDialog.Builder builder = new AlertDialog.Builder(FindActivity.this);
        builder.setTitle("?????? ????????? ????????????");
        builder.setMessage("?????? ???????????? ???????????? ?????? ???????????? ???????????????.\n"
                + "?????? ????????? ???????????????????");
        builder.setCancelable(true);
        builder.setPositiveButton("??????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent
                        = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("??????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }

    //?????? gps??? ???????????? ??? ?????? ???????????? ???????????????
    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    @Override
    public void onMapViewInitialized(MapView mapView) {
        MapPoint mp = mapView.getMapCenterPoint();
        MapPoint.GeoCoordinate gc = mp.getMapPointGeoCoord();

        double gCurrentLat = gc.latitude;
        double gCurrentLog = gc.longitude;

        Log.i("subin", "?????? ?????? ??? ??????: " + gCurrentLat + "??????: " + gCurrentLog);
    }

    @Override
    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) {
//        Log.i("subin","onMapViewCenterPointMoved");
    }

    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int i) {
//        Log.i("subin","onMapViewZoomLevelChanged");
    }

    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {
//        Log.i("subin","onMapViewSingleTapped");
        rl_search.setVisibility(View.GONE);
    }

    @Override
    public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {
//        Log.i("subin","onMapViewDoubleTapped");
    }

    @Override
    public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {
//        Log.i("subin","onMapViewLongPressed");
    }

    @Override
    public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {
//        Log.i("subin","onMapViewDragStarted");
    }

    @Override
    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {
//        Log.i("subin","onMapViewDragEnded");
    }

    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {
        MapPoint mp = mapView.getMapCenterPoint();
        MapPoint.GeoCoordinate gc = mp.getMapPointGeoCoord();

        double gCurrentLat = gc.latitude;
        double gCurrentLog = gc.longitude;

        Log.i("subin", "?????? ????????? ?????? ??? ??????: " + gCurrentLat + "??????: " + gCurrentLog + "??????" + radius);
    }

    @Subscribe //???????????? ????????? ????????? ????????????
    public void search(Document document) {
        //bus??? ????????? ??? document
        SearchName = document.getPlaceName();
        mCurrentLng = Double.parseDouble(document.getX());
        mCurrentLat = Double.parseDouble(document.getY());
        Toast.makeText(FindActivity.this, "????????????: " + SearchName + "x: " + mCurrentLng + "y:" + mCurrentLat, Toast.LENGTH_SHORT).show();
        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(mCurrentLat, mCurrentLng), true);
        mapView.removePOIItem(searchMarker);
        //maker ????????? ?????? ?????? ??????
        searchMarker.setItemName(SearchName);
        MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(mCurrentLat, mCurrentLng);
        searchMarker.setMapPoint(mapPoint);
        searchMarker.setMarkerType(MapPOIItem.MarkerType.RedPin); // ???????????? ???????????? BluePin ?????? ??????.
        searchMarker.setSelectedMarkerType(MapPOIItem.MarkerType.YellowPin); // ????????? ???????????????, ???????????? ???????????? RedPin ?????? ??????.
        //?????? ????????? ???????????? ??????
        searchMarker.setDraggable(true);
        mapView.addPOIItem(searchMarker);
    }

    //????????? ?????? ??????
    private void searchKeyword(String keyword) {
        String API_KEY = "KakaoAK f33950708cffc6664e99ac21489fd117";
        KakaoAPIService kakaoAPIService = KakaoAPIClient.getApiService();
        Call<CategoryResult> search = kakaoAPIService.getSearchKeword(API_KEY, keyword);
        Log.i("subin", "?????????: " + search);
        search.enqueue(new Callback<CategoryResult>() {
            @Override
            public void onResponse(Call<CategoryResult> call, Response<CategoryResult> response) {

                Log.i("subin", "????????????: " + response.body());
                if (response.isSuccessful()) {
                    assert response.body() != null;

                    for (Document document : response.body().getDocuments()) {
                        myAdatpter.addItem(document);
                        myAdatpter.setOnItemClickListener(new MyAdatpter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View v, int pos) {
                                search(document);
                            }
                        });
                    }
                    myAdatpter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<CategoryResult> call, Throwable t) {

                Log.i("subin", "l????????????: " + t.getMessage());
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        bus.unregister(this); //??????????????? ????????? ????????? ????????????
    }

    @Override
    public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {

    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {

    }

    //????????? ?????????
    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {
        String lat = String.valueOf(mapPOIItem.getMapPoint().getMapPointGeoCoord().latitude);
        String lng = String.valueOf(mapPOIItem.getMapPoint().getMapPointGeoCoord().longitude);
        Toast.makeText(this, "??????: " + mapPOIItem.getItemName(), Toast.LENGTH_SHORT).show();

        final CharSequence[] items = {"?????????", "??????", "?????? ??????"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("??????????????????");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                cafeList.clear();
                restaurantList.clear();
                //CharSequence[] items
                if (i == 0) {
                    //??? ?????? ????????? ????????????
                    mapView.removeAllPOIItems();
                    Log.i("subin", "??????:" + mapPOIItem.getItemName());
                    SearchRestaurant(lng, lat, String.valueOf(radius));
                } else if (i == 1) {
                    mapView.removeAllPOIItems();
                    //??? ?????? ?????? ????????????
                    Log.i("subin", "??????:" + mapPOIItem.getItemName());
                    SearchCafe(lng, lat, String.valueOf(radius));
                } else if (i == 2) {
//                    ?????? ?????? ???????????? ????????? ?????????
                    String API_KEY = "KakaoAK f33950708cffc6664e99ac21489fd117";
                    KakaoAPIService kakaoAPIService = KakaoAPIClient.getApiService();
                    Call<CategoryResult> call = kakaoAPIService.getSearchLocationDetail(API_KEY, mapPOIItem.getItemName(), lng, lat, 1);
                    call.enqueue(new Callback<CategoryResult>() {
                        @Override
                        public void onResponse(@NotNull Call<CategoryResult> call, @NotNull Response<CategoryResult> response) {
                            if (response.isSuccessful()) {
                                Intent intent = new Intent(FindActivity.this, StoreInfoActivity.class);
                                assert response.body() != null;
                                intent.putExtra("10", response.body().getDocuments().get(0));
                                startActivity(intent);
                            }
                        }
                        @Override
                        public void onFailure(Call<CategoryResult> call, Throwable t) {
                            Toast.makeText(getApplicationContext(), "??????????????? ?????? ??????????????? ????????????.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(FindActivity.this, StoreInfoActivity.class);
                            startActivity(intent);
                        }
                    });
                }
                dialogInterface.dismiss();
            }
        });
        builder.setPositiveButton("??????", null);
        builder.create().show();
    }

    @Override
    public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {
        MapPoint.GeoCoordinate mapPointGeo = mapPoint.getMapPointGeoCoord();
        SearchName = "???????????? ??????";
        mSearchLng = mapPointGeo.longitude;
        mSearchLat = mapPointGeo.latitude;
        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(mSearchLat, mSearchLng), true);
        MapMarker(SearchName, mSearchLng, mSearchLat);
    }

    //????????? ?????? ????????????
    public void SearchRestaurant(String x, String y, String radius) {
        JSONObject location = new JSONObject();
        try {
            location.put("x", x);
            location.put("y", y);
            location.put("radius", radius);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ArrayList<JSONObject> arrayList = new ArrayList<>();
        arrayList.add(location);
        Call<ArrayList<JSONObject>> rest = RetrofitClient.getApiService().rest(arrayList);
        rest.enqueue(new Callback<ArrayList<JSONObject>>() {
            @Override
            public void onResponse(Call<ArrayList<JSONObject>> call, Response<ArrayList<JSONObject>> response) {

                Log.i("subin","?????????????");
                restaurantList = response.body();

                String rest = restaurantList.get(0).toJSONString();
                Log.e("leehj", "restaurantList toString result: "+rest);

                JSONParser parser = new JSONParser();
                JSONObject jsonObj = null;

                try {
                    jsonObj = (JSONObject) parser.parse(rest);
                    JSONArray jsonArray = (JSONArray) jsonObj.get("documents");
                    JSONObject jsonObject;

                    for (int i = 0; i < jsonArray.size(); i++) {
                        jsonObject = (JSONObject) jsonArray.get(i);
                        Log.i("subin", "address_name " + i + " : " + jsonObject.get("address_name"));
                        Log.i("subin", "place_name " + i + " : " + jsonObject.get("place_name"));
                        Log.i("subin", "place_url " + i + " : " + jsonObject.get("place_url"));
                        Log.i("subin", "address_name " + i + " : " + jsonObject.get("address_name"));
                        Log.i("subin", "x " + i + " : " + jsonObject.get("x"));
                        Log.i("subin", "y " + i + " : " + jsonObject.get("y"));

                        CustomMarker(jsonObject.get("place_name").toString(), Double.parseDouble(jsonObject.get("x").toString()), Double.parseDouble(jsonObject.get("y").toString()), R.drawable.rest);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<JSONObject>> call, Throwable t) {
                Log.i("subin", "rest ???????????? : " + t.getMessage());
            }
        });
    }

    //?????? ?????? ????????????
    public void SearchCafe(String x, String y, String radius) {
        JSONObject location = new JSONObject();
        try {
            location.put("x", x);
            location.put("y", y);
            location.put("radius", radius);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ArrayList<JSONObject> arrayList = new ArrayList<>();
        arrayList.add(location);
        Call<ArrayList<JSONObject>> cafe = RetrofitClient.getApiService().cafe(arrayList);
        cafe.enqueue(new Callback<ArrayList<JSONObject>>() {
            @Override
            public void onResponse(Call<ArrayList<JSONObject>> call, Response<ArrayList<JSONObject>> response) {

                cafeList = response.body();

                String cafe = cafeList.get(0).toJSONString();

                JSONParser parser = new JSONParser();
                JSONObject jsonObj = null;

                try {
                    jsonObj = (JSONObject) parser.parse(cafe);
                    JSONArray jsonArray = (JSONArray) jsonObj.get("documents");
                    JSONObject jsonObject;

                    for (int i = 0; i < jsonArray.size(); i++) {
                        jsonObject = (JSONObject) jsonArray.get(i);
                        Log.i("subin", "address_name " + i + " : " + jsonObject.get("address_name"));
                        Log.i("subin", "place_name " + i + " : " + jsonObject.get("place_name"));
                        Log.i("subin", "place_url " + i + " : " + jsonObject.get("place_url"));
                        Log.i("subin", "address_name " + i + " : " + jsonObject.get("address_name"));
                        Log.i("subin", "x " + i + " : " + jsonObject.get("x"));
                        Log.i("subin", "y " + i + " : " + jsonObject.get("y"));

                        CustomMarker(jsonObject.get("place_name").toString(), Double.parseDouble(jsonObject.get("x").toString()), Double.parseDouble(jsonObject.get("y").toString()), R.drawable.cafe);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<JSONObject>> call, Throwable t) {
                Log.i("subin", "cafe ???????????? : " + t.getMessage());
            }
        });
    }
}