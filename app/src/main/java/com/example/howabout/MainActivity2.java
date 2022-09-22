package com.example.howabout;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Call<TestVO> test=RetrofitClient.getApiService().test();
        test.enqueue(new Callback<TestVO>() {
            @Override
            public void onResponse(Call<TestVO> call, Response<TestVO> response) {
                Log.i("lee", response.body().toString());
            }

            @Override
            public void onFailure(Call<TestVO> call, Throwable t) {
                Log.i("lee", t.getMessage());
            }
        });
    }
}