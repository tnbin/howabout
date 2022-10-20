package com.example.howabout.popular;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.howabout.API.RetrofitClient;
import com.example.howabout.FindActivity;
import com.example.howabout.MainActivity;
import com.example.howabout.MyCourseActivity;
import com.example.howabout.R;

import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PopularActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    SharedPreferences preferences;
    View drawerView;
    ListView listView;
    PopularAdapter popularAdapter=new PopularAdapter();;
    ArrayList<Popular_item> popular_items=new ArrayList<Popular_item>();;
    List<String> urllist=new ArrayList<>();
    List<String> textlist=new ArrayList<>();
    Popular_item popular_item;
    //인기코스 리스트
    ArrayList<JSONObject>popularlist=new ArrayList<JSONObject>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popular);

        /////////////////////drawerlayout start
        drawerLayout = findViewById(R.id.drawer_layout);
        drawerView = findViewById(R.id.drawer);
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
                Intent intenth = new Intent(PopularActivity.this, MainActivity.class);
                startActivity(intenth);
            }
        });
        Button btn_courcebar = findViewById(R.id.btn_courcebar);
        btn_courcebar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawers();
                Intent intentc = new Intent(PopularActivity.this, FindActivity.class);
                startActivity(intentc);
            }
        });

        Button btn_mypagebar = findViewById(R.id.btn_mypagebar);
        btn_mypagebar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawers();
            }
        });
        Button btn_mycourcebar = findViewById(R.id.btn_mycourcebar);
        btn_mycourcebar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawers();
                Intent intentmc = new Intent(PopularActivity.this, MyCourseActivity.class);
                startActivity(intentmc);
            }
        });
        Button btn_gender = findViewById(R.id.btn_gender);
        final BottomSheet_gender bottomSheet_gender = new BottomSheet_gender(getApplicationContext());

        btn_gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheet_gender.show(getSupportFragmentManager(), bottomSheet_gender.getTag());

            }
        });
        Button btn_age=findViewById(R.id.btn_age);
        final BottomSheet_age bottomSheet_age=new BottomSheet_age(getApplicationContext());
        btn_age.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheet_age.show(getSupportFragmentManager(),bottomSheet_age.getTag());
            }
        });
        Button btn_region=findViewById(R.id.btn_region);
        final BottomSheet_region bottomSheet_region =new BottomSheet_region(getApplicationContext());
        btn_region.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheet_region.show(getSupportFragmentManager(),bottomSheet_region.getTag());
            }
        });
        /////////////////////drawerlayout finish
        //검색 버튼 눌렀을 때 
        Button btn_casearch=findViewById(R.id.btn_casearch);
        btn_casearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Bottomsheet에서 저장된 값 (성별,나이,도,시) 꺼내기
                preferences =getSharedPreferences("popular", MODE_PRIVATE);
                String gen=preferences.getString("p_gender","x");
                String age=preferences.getString("p_age","x");
                String region_do=preferences.getString("do","x");
                String region_si=preferences.getString("si","x");
                //검색할때마다 값을 불러오지 않게 값 초기화
                popularAdapter.clear();
                //서버 연결
                popularcourse(gen,age,region_do,region_si);
            }
        });
        popularAdapter=new PopularAdapter();
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
    public void popularcourse(String gender,String age,String location_do,String location_si){
        JSONObject popular=new JSONObject();
        try {
            popular.put("gender",gender);
            popular.put("age",age);
            popular.put("location_do",location_do);
            popular.put("location_si",location_si);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ArrayList<JSONObject> jsonObjectArrayList=new ArrayList<>();
        jsonObjectArrayList.add(popular);
        Call<ArrayList<JSONObject>> polcourse= RetrofitClient.getApiService().popular(jsonObjectArrayList);
        polcourse.enqueue(new Callback<ArrayList<JSONObject>>() {
            @Override
            public void onResponse(Call<ArrayList<JSONObject>> call, Response<ArrayList<JSONObject>> response) {
                //서버에서 받은 값 popularlist에 저장
                popularlist=response.body();
                //서버에서 받은 값 키값으로 불러오기 위해서 jsonobject 생성
                JSONObject jsonObject;
                try {
                    //불러온 값 크기에 맞추어 list에 저장 size
                    for (int i=0;i<popularlist.size();i++){
                        //jsonobject에 list 값 끝까지 저장
                        jsonObject=popularlist.get(i);
                        //보여줄 listview 불러오기
                        listView=findViewById(R.id.popular_list);
                        //arraylist url에 서버에서 받은 값 (카페 url) 저장
                        urllist.add(jsonObject.get("c_image_url").toString());
                        //arraylist text에 서버에서 받은 값 (카페 text) 저장
                        textlist.add(jsonObject.get("c_do").toString()+" "+jsonObject.get("c_si")+" 코스");
                        //객체 불러오기 (list custom)
                        popular_item=new Popular_item();
                        //객체에 맞게 list에 저장된값 저장
                        popular_item.setPlace(textlist.get(i));
                        popular_item.setImage(urllist.get(i));
                        //adapter에 값이 저장된 객체를 추가
                        popularAdapter.addItem(popular_item);
//                        Log.i("subin","imageurl: "+urllist);
//                        Log.i("subin","imageurl: "+textlist);
                        Log.i("subin",popular_item+"hhhhh"+i);
                        //listview와 adapter 연결
                        listView.setAdapter(popularAdapter);
                        //adapter 업데이트
                        popularAdapter.notifyDataSetChanged();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<ArrayList<JSONObject>> call, Throwable t) {
                Log.i("subin","popular sever 연결 실패"+t.getMessage());
            }
        });
    }
}