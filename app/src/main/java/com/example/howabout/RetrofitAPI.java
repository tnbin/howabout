package com.example.howabout;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RetrofitAPI {

    @GET("/user/all")
    Call<TestVO> test();

}
