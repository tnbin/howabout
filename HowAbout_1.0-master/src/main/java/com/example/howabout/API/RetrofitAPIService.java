package com.example.howabout.API;

import com.example.howabout.Search.CategoryResult;
import com.example.howabout.Vo.IdVo;
import com.example.howabout.Vo.NickNameVo;
import com.example.howabout.Vo.UserVo;
import com.example.howabout.Vo.signInVo;


import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface RetrofitAPIService {

    //아이디 중복 여부
    @POST("/login/signUp/idCheck")
    Call<Integer> idcheck(@Body IdVo idcheck);

    //닉네임 중복 여부
    @POST("/login/signUp/nickCheck")
    Call<Integer> nickcheck(@Body NickNameVo nickNameVo);

    //회원가입 성공 여부
    @POST("/login/signUp")
    Call<Integer> all(@Body UserVo userVo);

    //로그인 성공 여부
    @POST("/login/signIn")
    Call<signInVo> login(@Body UserVo userVo);


    //Find Course..............................................................
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

    //가게 리뷰 가져오기
    @POST("/findCourse/getLocationInfo") //가게 정보
    Call<Map<String, String>> getLocationInfo(@Body Map place_info);


    //MyPage................................................................
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


    //PopularCourse........................................................
    //DB에서 ‘도’ 를 반환
    @GET("/popularCourse/getDo")
    Call<ArrayList<String>> getDo();

    //DB에서 ‘도’ 기반 ‘시’ 를 반환
    @POST("/popularCourse/getSi")
    Call<ArrayList<String>> getSi(@Body String getsi);

    //인기코스 구하기
    @POST("/popularCourse/getCatCourse")
    Call<ArrayList<JSONObject>> popular(@Body ArrayList<JSONObject> po);



}
