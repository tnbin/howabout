package com.example.howabout.API;

import com.example.howabout.Vo.IdVo;
import com.example.howabout.Vo.NickNameVo;
import com.example.howabout.Vo.UserVo;
import com.example.howabout.Vo.signInVo;


import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

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

    @POST("/findCourse/location")
    Call<Integer> restcource(@Body ArrayList<JSONObject> arrayList);

    @POST("/findCourse/rest")
    Call<ArrayList<JSONObject>> rest(@Body ArrayList<JSONObject> arrayList);

    @POST("/findCourse/cafe")
    Call<ArrayList<JSONObject>> cafe(@Body ArrayList<JSONObject> arrayList);

    @POST("/myCourse/saveCourse")
    Call<Integer> saveCourse(@Body ArrayList<JSONObject> arrayList);

    //마이페이지 내정보 가지고 오기
    @POST("/myPage/myInfo/getMyData")
    Call<UserVo> myDataUp(@Body ArrayList<JSONObject> ttl);

    //회원탈퇴
    @POST("/myPage/myInfo/withdrawal")
    Call<Integer> withdrawal(@Body ArrayList<JSONObject> aal);

    //회원정보 수정
    @POST("/myPage/myInfo/updateInfo")
    Call<UserVo> updateUser(@Body UserVo user);

    //비밀번호 확인
    @POST("/myPage/myInfo/CheckPW")
    Call<Integer> checkPW(@Body List jsonList);

}
