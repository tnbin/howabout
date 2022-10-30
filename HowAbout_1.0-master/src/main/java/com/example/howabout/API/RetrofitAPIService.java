package com.example.howabout.API;

import com.example.howabout.Search.CategoryResult;
import com.example.howabout.Vo.LoginDTO;
import com.example.howabout.Vo.UserVo;
import com.example.howabout.Vo.UserDTO;
import com.example.howabout.function.User;


import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface RetrofitAPIService {

    @POST("/splash/test")
        //autoLogin. token 적용 필요
    Call<String> test(@Header("Authorization") String token);

    @POST("/splash/autoLogin")
        //autoLogin. token 적용 필요
    Call<Map<String, String>> autoLogin(@Header("Authorization") String token);

    //아이디 중복 여부
    @POST("/login/signUp/idCheck")
    Call<Integer> idcheck(@Body Map idck);

    //닉네임 중복 여부
    @POST("/login/signUp/nickCheck")
    Call<Integer> nickcheck(@Body Map nicknameck);

    //이메일 중복 여부
    @POST("/login/signUp/emailSendAuth")
    Call<Integer> emailSendCheck(@Body Map emailck);

    //이메일 인증번호 확인
    @POST("/login/signUp/emailAuthCheck")
    Call<Integer> emailAuthCheck(@Body Map emailAuthck);

    //이메일 인증번호
    @POST("/login/sendPwEmail")
    Call<Integer> emailSend(@Body Map email);

    //회원가입 성공 여부
    @POST("/login/signUp")
    Call<Integer> all(@Body UserDTO userDTO);

    //로그인 성공 여부 ***
    @POST("/login/signIn")
    Call<LoginDTO> login(@Body Map id_pw);

    //아이디 찾기
    @POST("/login/sendIdEmail")
    Call<Integer> search_id(@Body Map email);

    //비밀번호 재설정 회원확인
    @POST("/login/checkMyInfo")
    Call<Integer> usercheck(@Body Map repwcheck);

    //비밀번호 재설정
    @POST("/login/setNewPw")
    Call<Integer> repw(@Body Map postrepw);
    //Find Course..............................................................

    @POST("/findCourse/rest")
        //식당 리스트
    Call<ArrayList<CategoryResult>> rest(@Body ArrayList<JSONObject> arrayList);

    @POST("/findCourse/cafe")
        //카페 리스트
    Call<ArrayList<CategoryResult>> cafe(@Body ArrayList<JSONObject> arrayList);

    @POST("/myCourse/saveCourse")
        //코스 저장. token 적용 필요
    Call<Map> saveCourse(@Body ArrayList<Object> arrayList, @Header("Authorization") String token);

    @POST("/myCourse/courseDibs")
        //내 코스 저장. token 적용 필요
    Call<Integer> courseDibs(@Body Map saveMyCourse_data, @Header("Authorization") String token);

    @POST("/findCourse/getLocationInfo")
        //가게 정보
        //가게 정보
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
    Call<UserDTO> updateUser(@Body UserDTO user);

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

    @POST("/popularCourse/getCatCourse")
        //인기코스 구하기
    Call<ArrayList<JSONObject>> getCatCourse(@Body ArrayList<JSONObject> po);

    //mycourse
    @POST("/myCourse/myCourse")
    Call<ArrayList<JSONObject>> mycourse(@Body ArrayList<JSONObject> course);


}
