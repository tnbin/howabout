package com.example.howabout.API;

import com.example.howabout.category_search.CategoryResult;

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
    //장소이름으로 특정위치기준으로 검색
    @GET("v2/local/search/keyword.json")
    Call<CategoryResult> getSearchLocationDetail(
            @Header("Authorization") String token,
            @Query("query") String query,
            @Query("x") String x,
            @Query("y") String y,
            @Query("size") int size
    );

}
