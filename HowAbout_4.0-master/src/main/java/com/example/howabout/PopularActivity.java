package com.example.howabout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.howabout.API.RetrofitClient;
import com.example.howabout.functions.HowAboutThere;
import com.example.howabout.popular.BottomSheet_age;
import com.example.howabout.popular.BottomSheet_gender;
import com.example.howabout.popular.BottomSheet_region;
import com.example.howabout.popular.PopularAdapter;
import com.example.howabout.popular.Popular_item;

import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PopularActivity extends AppCompatActivity {

    HowAboutThere FUNC = new HowAboutThere();

    SharedPreferences preferences;
    ListView listView;
    PopularAdapter popularAdapter = new PopularAdapter();
    List<String> urllist;
    List<String> textlist;
    Popular_item popular_item;
    //인기코스 리스트
    ArrayList<JSONObject> popularlist = new ArrayList<JSONObject>();
    JSONObject jsonObject;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popular);

        FUNC.sideBar(PopularActivity.this);

        //인기차트 들어갈때 oncreate시 전체 인기코스 불러오기
        //서버 연결
        urllist = new ArrayList<>();
        textlist = new ArrayList<>();

        popularAdapter.clear();
        popularAdapter.notifyDataSetChanged();
        popularcourse("전체", "전체", "전체", "전체");

        Button btn_gender = findViewById(R.id.btn_gender);
        final BottomSheet_gender bottomSheet_gender = new BottomSheet_gender(getApplicationContext());

        btn_gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheet_gender.show(getSupportFragmentManager(), bottomSheet_gender.getTag());

            }
        });
        Button btn_age = findViewById(R.id.btn_age);
        final BottomSheet_age bottomSheet_age = new BottomSheet_age(getApplicationContext());
        btn_age.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheet_age.show(getSupportFragmentManager(), bottomSheet_age.getTag());
            }
        });
        Button btn_region = findViewById(R.id.btn_region);
        final BottomSheet_region bottomSheet_region = new BottomSheet_region(getApplicationContext());
        btn_region.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheet_region.show(getSupportFragmentManager(), bottomSheet_region.getTag());
            }
        });
        /////////////////////drawerlayout finish
        //검색 버튼 눌렀을 때
        Button btn_casearch = findViewById(R.id.btn_casearch);
        btn_casearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Bottomsheet에서 저장된 값 (성별,나이,도,시) 꺼내기
                preferences = getSharedPreferences("popular", MODE_PRIVATE);
                String gen = preferences.getString("p_gender", "전체");
                String age = preferences.getString("p_age", "전체");
                String region_do = preferences.getString("do", "전체");
                String region_si = preferences.getString("si", "전체");
                Log.i("subin", gen + "," + age + "," + region_do + "," + region_si);
                //검색할때마다 값을 불러오지 않게 값 초기화
                urllist = new ArrayList<>();
                textlist = new ArrayList<>();
                popularAdapter.clear();
                popularAdapter.notifyDataSetChanged();
                //서버 연결
                popularcourse(gen, age, region_do, region_si);
            }
        });
        listView = findViewById(R.id.popular_list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(PopularActivity.this, CourseInfoActivity.class);
                JSONObject js = popularlist.get(i);
                Log.i("subin", js + "");
                String string = js.toString();
                intent.putExtra("storeInfo", string);

                startActivity(intent);

            }
        });
    }

    //앱의 activity을 벗어났을때 저장된 값을 삭제하기 어려워 preference에 저장시킴.
    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences preferences = getSharedPreferences("popular", MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putString("do", "전체");
        edit.putString("si", "전체");
        edit.putString("p_age", "전체");
        edit.putString("p_gender", "전체");
        edit.apply();
        edit.commit();

    }


    public void popularcourse(String gender, String age, String location_do, String location_si) {
        JSONObject popular = new JSONObject();
        try {
            popular.put("gender", gender);
            popular.put("age", age);
            popular.put("location_do", location_do);
            popular.put("location_si", location_si);
        } catch (Exception e) {
            e.printStackTrace();
        }
        SharedPreferences sharedPreferences = getSharedPreferences("USER", Activity.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);
        String request_token = "Bearer "+ token;
        String sand_token;
        if(token != null){
            sand_token = request_token;
        }else{
            sand_token = null;
        }

        ArrayList<JSONObject> jsonObjectArrayList = new ArrayList<>();
        jsonObjectArrayList.add(popular);
        Log.e("leehj", "popular :: sand_token: "+sand_token);
        Call<ArrayList<JSONObject>> polcourse = RetrofitClient.getApiService().getCatCourse(jsonObjectArrayList, sand_token);
        polcourse.enqueue(new Callback<ArrayList<JSONObject>>() {
            @Override
            public void onResponse(Call<ArrayList<JSONObject>> call, Response<ArrayList<JSONObject>> response) {
                //서버에서 받은 값 popularlist에 저장
                popularlist = response.body();
                Log.e("leehj", "popular :: getCatCourse response body: "+popularlist);
//                String str = popularlist.toString();
//                Log.i("leehj", "서버에서 준 값" + str);

                //서버에서 받은 값 키값으로 불러오기 위해서 jsonobject 생성
                try {
                    if (popularlist.size() == 0) {
                        Toast.makeText(PopularActivity.this, "인기 코스가 없습니다", Toast.LENGTH_SHORT).show();
                    } else {
                        //불러온 값 크기에 맞추어 list에 저장 size
                        for (int i = 0; i < popularlist.size(); i++) {
                            //jsonobject에 list 값 끝까지 저장
                            jsonObject = popularlist.get(i);
                            //보여줄 listview 불러오기
                            listView = findViewById(R.id.popular_list);
                            //arraylist url에 서버에서 받은 값 (카페 url) 저장
                            urllist.add("http:" + jsonObject.get("c_image_url").toString());
                            //arraylist text에 서버에서 받은 값 (카페 text) 저장
                            textlist.add(jsonObject.get("c_do").toString() + " " + jsonObject.get("c_si") + " 코스");
                            //객체 불러오기 (list custom)
                            popular_item = new Popular_item();
                            //객체에 맞게 list에 저장된값 저장
                            popular_item.setPlace(textlist.get(i));
                            popular_item.setImage(urllist.get(i));
                            //adapter에 값이 저장된 객체를 추가
                            popularAdapter.addItem(popular_item);
//                        Log.i("subin","imageurl: "+urllist);
//                        Log.i("subin","imageurl: "+textlist);
                            //listview와 adapter 연결
                            listView.setAdapter(popularAdapter);
                            //adapter 업데이트
                            popularAdapter.notifyDataSetChanged();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(PopularActivity.this,"인기 코스가 없습니다",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<JSONObject>> call, Throwable t) {
                Log.i("subin", "popular sever 연결 실패" + t.getMessage());
            }
        });
    }
}
