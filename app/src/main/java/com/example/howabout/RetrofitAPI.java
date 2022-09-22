package com.example.howabout;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface RetrofitAPI {


    @POST("/login/signUp")
    Call<Integer>all(@Body UserVo userVo);

    @POST("/login/signUp/idcheck")
    Call<Integer> idcheck(@Body IdVo idcheck);

    @POST("/login/signUp/nickCheck")
    Call<Integer> nickcheck(@Body NickNameVo nickNameVo);
}
