package com.example.howabout.API;

import com.example.howabout.Search.CategoryResult;
import com.example.howabout.Search.Document;
import com.example.howabout.Vo.IdVo;
import com.example.howabout.Vo.NickNameVo;
import com.example.howabout.Vo.UserVo;
import com.example.howabout.Vo.signInVo;


import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RetrofitAPI {

    @POST("/login/signUp/idCheck")
    Call<Integer> idcheck(@Body IdVo idcheck);

    @POST("/login/signUp/nickCheck")
    Call<Integer> nickcheck(@Body NickNameVo nickNameVo);

    @POST("/login/signUp")
    Call<Integer> all(@Body UserVo userVo);

    @POST("/login/signIn")
    Call<signInVo> login(@Body UserVo userVo);

    //findCourse................................................................
    @POST("/findCourse/location")
    Call<Integer> restcource(@Body ArrayList<JSONObject> arrayList);

    @POST("/findCourse/rest") //식당 리스트
    Call<ArrayList<CategoryResult>> rest(@Body ArrayList<JSONObject> arrayList);

    @POST("/findCourse/cafe") //카페 리스트
    Call<ArrayList<CategoryResult>> cafe(@Body ArrayList<JSONObject> arrayList);

    @POST("/findCourse/saveCourse") //코스 저장
    Call<Map> saveCourse(@Body ArrayList<Object> arrayList);

   @POST("/findCourse/courseDibs")//내 코스 저장
   Call<Integer> saveMyCourse(@Body Map saveMyCourse_data);

    @POST("/findCourse/getLocationInfo") //가게 정보
    Call<ArrayList<ArrayList<String>>> getLocationInfo(Map place_info);
}
