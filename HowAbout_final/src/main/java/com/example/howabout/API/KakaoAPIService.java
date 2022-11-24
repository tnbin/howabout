package com.example.howabout.API;

import com.example.howabout.Search.CategoryResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface KakaoAPIService {
    @GET("/v2/local/search/keyword.json") //keyword.json의 정보를 받아와요
    Call<CategoryResult> getSearchKeword(
            @Header("Authorization") String key,//카카오 api 인증키
            @Query("query") String query //검색을 원하는 질의어
    );
}
