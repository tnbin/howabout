package com.example.howabout;

import static net.daum.mf.map.api.MapPoint.mapPointWithGeoCoord;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.howabout.API.RetrofitClient;
import com.example.howabout.functions.HowAboutThere;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapPolyline;
import net.daum.mf.map.api.MapView;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CourseInfoActivity extends AppCompatActivity {

    HowAboutThere FUNC = new HowAboutThere();
    SharedPreferences sharedPreferences;
    TextView storeInfo_tv_time;
    CompoundButton compoundButton;
    ScaleAnimation scaleAnimation;
    Map<String, String> savePopularCourse_data = new HashMap<>();

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_info);

        FUNC.sideBar(CourseInfoActivity.this);

        //heart animation
        scaleAnimation = new ScaleAnimation(0.7f, 1.0f, 0.7f, 1.0f, Animation.RELATIVE_TO_SELF, 0.7f, Animation.RELATIVE_TO_SELF, 0.7f);
        BounceInterpolator bounceInterpolator = new BounceInterpolator();
        scaleAnimation.setInterpolator(bounceInterpolator);
        scaleAnimation.setDuration(500);
        compoundButton = findViewById(R.id.btn_favorite);

        //Mapview 생성
        MapView mapView = new MapView(this);
        //mapview 가 들어갈 layout
        LinearLayout mapViewContainer = findViewById(R.id.map_mycourse);
        //layout에 mapview 추가
        mapViewContainer.addView(mapView);

        //popular에서 보낸 intent 받기
        Intent couseintent = getIntent();
//        String heart=couseintent.getStringExtra("heart");
//        if (heart.equals("mycourse")){
//            compoundButton.setVisibility(View.INVISIBLE);
//        }
        String jsonObject = couseintent.getStringExtra("storeInfo");
        JSONParser parser = new JSONParser();
        JSONObject aa = null;
        try {
            aa = (JSONObject) parser.parse(jsonObject);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //gg..
        String r_lat = aa.get("r_lat").toString();
        String r_lon = aa.get("r_lon").toString();
        String c_lat = aa.get("c_lat").toString();
        String c_lon = aa.get("c_lon").toString();
        String c_name = aa.get("c_name").toString();
        String r_name = aa.get("r_name").toString();
        String c_image_url = "http:" + aa.get("c_image_url").toString();
        String r_image_url = "http:" + aa.get("r_image_url").toString();
        String c_cat = aa.get("c_cat").toString();
        String r_cat = aa.get("r_cat").toString();
        String r_address = aa.get("r_do").toString() + " " + aa.get("r_si").toString() + " " + aa.get("r_gu").toString() + " " + aa.get("r_dong").toString();
        String c_address = aa.get("c_do").toString() + " " + aa.get("c_si").toString() + " " + aa.get("c_gu").toString() + " " + aa.get("c_dong").toString();
        String c_phone = aa.get("c_phone").toString();
        String r_phone = aa.get("r_phone").toString();
        String r_url = aa.get("r_url").toString();
        String c_url = aa.get("c_url").toString();
        String r_id = aa.get("r_id").toString();
        String c_id = aa.get("c_id").toString();

        savePopularCourse_data.put("r_id", r_id);
        savePopularCourse_data.put("c_id", c_id);

        Log.i("subin", "음식점 경도 위도 정보 값:" + r_lon + "," + r_lat);
        Log.i("subin", "카페 경도 위도 정보 값:" + c_lon + "," + c_lat);
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
                Information(r_name, r_cat, r_image_url, r_address, r_phone, r_url, r_lat, r_lon, c_lat, c_lon);
                return false;
            }
        });
        place2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Information(c_name, c_cat, c_image_url, c_address, c_phone, c_url, c_lat, c_lon, r_lat, r_lon);
                return false;
            }
        });

        //favorit button event
        compoundButton.setOnTouchListener(touch_favoritBtn);
        compoundButton.startAnimation(scaleAnimation);

    }

    public void Information(String name, String cat, String image_url, String address, String phone, String url, String first_lat, String first_lon, String second_lat, String second_lon) {
        Dialog storeInfo_dialog = new Dialog(CourseInfoActivity.this);
        storeInfo_dialog.setContentView(R.layout.store_info);
        //배경 투명
        storeInfo_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ImageView storeInfo_img = storeInfo_dialog.findViewById(R.id.storeInfo_img);
        TextView storeInfo_tv_placeName = storeInfo_dialog.findViewById(R.id.storeInfo_tv_placeName);
        TextView storeInfo_tv_cat = storeInfo_dialog.findViewById(R.id.storeInfo_tv_cat);
        TextView storeInfo_tv_address = storeInfo_dialog.findViewById(R.id.storeInfo_tv_address);
        TextView storeInfo_tv_phone = storeInfo_dialog.findViewById(R.id.storeInfo_tv_phone);
        TextView storeInfo_tv_url = storeInfo_dialog.findViewById(R.id.storeInfo_tv_url);
        Button storeInfo_btn_phone = storeInfo_dialog.findViewById(R.id.storeInfo_btn_phone);
        Button storeInfo_btn_road = storeInfo_dialog.findViewById(R.id.storeInfo_btn_road);

        Glide.with(CourseInfoActivity.this).load(image_url).placeholder(R.drawable.error_img1).override(Target.SIZE_ORIGINAL).apply(new RequestOptions().transforms(new CenterCrop(),
                new RoundedCorners(25))).into(storeInfo_img);
        storeInfo_tv_placeName.setText(name);
        storeInfo_tv_placeName.setTextIsSelectable(true);
        storeInfo_tv_cat.setText(cat);
        storeInfo_tv_cat.setTextIsSelectable(true);
        storeInfo_tv_address.setText("  " + address);
        storeInfo_tv_address.setSelected(true);
        storeInfo_tv_address.setTextIsSelectable(true);
        storeInfo_tv_phone.setText("  " + phone);
        storeInfo_tv_url.setText("  " + url);
        storeInfo_tv_url.setTextIsSelectable(true);
        storeInfo_tv_url.setSelected(true);

        storeInfo_btn_road.setVisibility(View.VISIBLE);

        //전화 다이얼
        storeInfo_btn_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent telintent = new Intent(Intent.ACTION_DIAL);
                telintent.setData(Uri.parse("tel:" + phone));
                startActivity(telintent);
            }
        });
        //길찾기 (카카오)
        storeInfo_btn_road.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String road = "kakaomap://route?sp=" + first_lat + "," + first_lon + "&ep=" + second_lat + "," + second_lon + "&by=FOOT";
//                Log.i("subin", "길찾기 카카오 주소: " + road);
                Intent roadintent = new Intent(Intent.ACTION_VIEW);
                roadintent.setData(Uri.parse(road));
                roadintent.addCategory(Intent.CATEGORY_BROWSABLE);

                List<ResolveInfo> list = getPackageManager().queryIntentActivities(roadintent, PackageManager.MATCH_DEFAULT_ONLY);
                if (list == null || list.isEmpty()) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=net.daum.android.map")));
                } else {
                    startActivity(roadintent);
                }
            }
        });
        //창닫기 버튼 클릭 이벤트
        ImageButton cancel = (ImageButton) storeInfo_dialog.findViewById(R.id.storeInfo_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                storeInfo_dialog.dismiss();
            }
        });
        //리뷰 서버연결
        Map getLocationInfo_data = new HashMap();
        getLocationInfo_data.put("place_url", url);

        Call<Map<String, String>> getLocationInfo = RetrofitClient.getApiService().getLocationInfo(getLocationInfo_data);
        getLocationInfo.enqueue(new Callback<Map<String, String>>() {
            @Override
            public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {

                Map<String, String> result = response.body();
//                Log.i("subin", "/////////////////" + result.get("storeTime"));
                String storeTime = result.get("storeTime");
                storeInfo_tv_time = storeInfo_dialog.findViewById(R.id.storeInfo_tv_time);
                storeInfo_tv_time.setText("  " + storeTime);
                storeInfo_tv_time.setTextIsSelectable(true);
//                Log.i("subin", storeTime);
                storeInfo_tv_time.setSelected(true);
                String review1 = result.get("review_1");
//                Log.i("subin", "review: " + review1);
                TextView storeInfo_tv_reivew1 = storeInfo_dialog.findViewById(R.id.storeInfo_tv_reivew1);
                storeInfo_tv_reivew1.setText(review1);
                String review2 = result.get("review_2");
                TextView storeInfo_tv_reivew2 = storeInfo_dialog.findViewById(R.id.storeInfo_tv_reivew2);
                storeInfo_tv_reivew2.setText(review2);
                String review3 = result.get("review_3");
                TextView storeInfo_tv_reivew3 = storeInfo_dialog.findViewById(R.id.storeInfo_tv_reivew3);
                storeInfo_tv_reivew3.setText(review3);
            }

            @Override
            public void onFailure(Call<Map<String, String>> call, Throwable t) {

                Log.i("subin", "서버 연결 실패 : " + t.getMessage());
            }
        });
        storeInfo_dialog.show();
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

    //내 코스 저장하기 버튼 클릭 이벤트
    View.OnTouchListener touch_favoritBtn = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            sharedPreferences = getSharedPreferences("USER", Activity.MODE_PRIVATE);
            String token = sharedPreferences.getString("token", null);
            if (token != null) {
                Log.i("leehj", "token: " + token);
                compoundButton.setOnCheckedChangeListener(check_favoritBtn);
                return false;
            } else {
                Toast.makeText(CourseInfoActivity.this, "로그인 후 이용 가능한 서비스입니다.", Toast.LENGTH_SHORT).show();
                return true;
            }
        }
    };

    CompoundButton.OnCheckedChangeListener check_favoritBtn = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            sharedPreferences = getSharedPreferences("USER", Activity.MODE_PRIVATE);
            String token = sharedPreferences.getString("token", null);
            String request_token = "Bearer " + token;

            if (b) {
                Log.e("leehj", "스위치 on 서버에 내코스 저장 해요!!");
//                saveMyCourse_data.put("u_id", "leehj");
                Call<Integer> save_myCourse = RetrofitClient.getApiService().courseDibs(savePopularCourse_data, request_token);
                save_myCourse.enqueue(new Callback<Integer>() {
                    @Override
                    public void onResponse(Call<Integer> call, Response<Integer> response) {
                        Log.i("leehj", "성공 시 return value는 1: " + response.body());
                        Toast.makeText(CourseInfoActivity.this, "내 코스에 저장됐습니다.", Toast.LENGTH_SHORT).show();
//                            compoundButton.setClickable(false);
                    }

                    @Override
                    public void onFailure(Call<Integer> call, Throwable t) {

                    }
                });
            } else {
                Log.e("leehj", "스위치 off!!! 서버에 내코스 삭제해요");
                //내 코스 삭제
                Call<Integer> save_myCourse = RetrofitClient.getApiService().courseDibs(savePopularCourse_data, request_token);
                save_myCourse.enqueue(new Callback<Integer>() {
                    @Override
                    public void onResponse(Call<Integer> call, Response<Integer> response) {
                        Log.i("leehj", "성공 시 return value는 1: " + response.body());
                        Toast.makeText(CourseInfoActivity.this, "내 코스에서 삭제 되었습니다.", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<Integer> call, Throwable t) {

                    }
                });
            }
        }
    };
}