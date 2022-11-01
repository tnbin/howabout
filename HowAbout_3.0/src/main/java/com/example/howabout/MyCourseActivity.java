package com.example.howabout;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.howabout.API.RetrofitClient;
import com.example.howabout.functions.HowAboutThere;
import com.example.howabout.popular.PopularAdapter;
import com.example.howabout.popular.Popular_item;

import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyCourseActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    Intent intent;
    ListView listView;
    PopularAdapter popularAdapter = new PopularAdapter();
    List<String> urllist;
    List<String> textlist;
    Popular_item popular_item;
    JSONObject jsonObject;
    ArrayList<JSONObject> mycourselist = new ArrayList<JSONObject>();
    HowAboutThere FUNC = new HowAboutThere();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mycourse);

        //sidebar
        FUNC.sideBar(MyCourseActivity.this);
        //token여부 확인하기
        sharedPreferences = getSharedPreferences("USER", Activity.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);
        String request_token = "Bearer " + token;
        //코스 목록 리스트 확인
        listView=findViewById(R.id.mycourse_list);
        //내 코스 서버연결
        urllist = new ArrayList<>();
        textlist = new ArrayList<>();
        popularAdapter.clear();
        popularAdapter.notifyDataSetChanged();
        mycourse(request_token);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                intent=new Intent(MyCourseActivity.this,CourseInfoActivity.class);
                JSONObject json= mycourselist.get(i);
                String string=json.toString();
                Log.i("subin",i+": listview click: "+string);
                intent.putExtra("storeInfo",string);
                startActivity(intent);
            }
        });
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        sharedPreferences = getSharedPreferences("USER", Activity.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);
        String request_token = "Bearer " + token;
        listView = findViewById(R.id.mycourse_list);
        //내 코스 서버연결
        urllist = new ArrayList<>();
        textlist = new ArrayList<>();
        popularAdapter.clear();
        popularAdapter.notifyDataSetChanged();
        mycourse(request_token);
    }
    public void mycourse(String token){
        Call<ArrayList<JSONObject>>mycourse= RetrofitClient.getApiService().myCourse(token);
        mycourse.enqueue(new Callback<ArrayList<JSONObject>>() {
            @Override
            public void onResponse(Call<ArrayList<JSONObject>> call, Response<ArrayList<JSONObject>> response) {
                mycourselist=response.body();
                Log.i("subin","내코스 값:"+mycourselist);
                //서버에서 받은 값 키값으로 불러오기 위해서 jsonobject 생성
                try {
                    if (mycourselist.isEmpty()) {
                        Toast.makeText(MyCourseActivity.this, "내 코스가 없습니다", Toast.LENGTH_SHORT).show();
                    } else {
                        //불러온 값 크기에 맞추어 list에 저장 size
                        for (int i = 0; i < mycourselist.size(); i++) {
                            //jsonobject에 list 값 끝까지 저장
                            jsonObject = mycourselist.get(i);
                            Log.i("subin","내코스 : "+jsonObject);
                            //보여줄 listview 불러오기
                            listView = findViewById(R.id.mycourse_list);
                            //arraylist url에 서버에서 받은 값 (카페 url) 저장
                            Log.i("subin","내코스 url: "+jsonObject.get("c_image_url"));
                            urllist.add("http:" + jsonObject.get("c_image_url").toString());
                            Log.i("subin","url: "+urllist);
                            //arraylist text에 서버에서 받은 값 (카페 text) 저장
                            textlist.add(jsonObject.get("c_do").toString() + " " + jsonObject.get("c_si") + " 코스");
                            Log.i("subin","url: "+textlist);
                            //객체 불러오기 (list custom)
                            popular_item = new Popular_item();
                            //객체에 맞게 list에 저장된값 저장
                            popular_item.setPlace(textlist.get(i));
                            popular_item.setImage(urllist.get(i));
                            //adapter에 값이 저장된 객체를 추가
                            popularAdapter.addItem(popular_item);
                            //listview와 adapter 연결
                            listView.setAdapter(popularAdapter);
                            //adapter 업데이트
                            popularAdapter.notifyDataSetChanged();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<ArrayList<JSONObject>> call, Throwable t) {
                Log.i("subin","내코스 서버연결 실패: "+t.getMessage());
            }
        });
    }
}