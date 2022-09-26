package com.example.howabout;

import com.example.howabout.Vo.IdVo;
import com.example.howabout.Vo.NickNameVo;
import com.example.howabout.Vo.UserVo;
import com.example.howabout.Vo.signInVo;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RetrofitAPI {

    @POST("/login/signUp/idCheck")
    Call<Integer> idcheck(@Body IdVo idcheck);

    @POST("/login/signUp/nickCheck")
    Call<Integer> nickcheck(@Body NickNameVo nickNameVo);

    @POST("/login/signUp")
    Call<Integer>all(@Body UserVo userVo);

    @POST("/login/signIn")
    Call<signInVo>login(@Body UserVo userVo);

}
