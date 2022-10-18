package com.example.howabout.popular;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.howabout.API.RetrofitClient;
import com.example.howabout.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BottomSheet_region extends BottomSheetDialogFragment {
    Context context;
    ListView lv_si;
    public BottomSheet_region(Context context) {
        this.context = context;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.bottonsheet_region, container, false);

        Call<ArrayList<String>> getdo = RetrofitClient.getApiService().getDo();
        getdo.enqueue(new Callback<ArrayList<String>>() {
            @Override
            public void onResponse(Call<ArrayList<String>> call, Response<ArrayList<String>> response) {
                ArrayList<String> al_do=new ArrayList<String>();
                for (int i=0; i<response.body().size();i++){
                    al_do.add(response.body().get(i));
                }
                ArrayAdapter<String>aa;
                aa=new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1,al_do);
                ListView lv_do=view.findViewById(R.id.region_do);
                lv_do.setAdapter(aa);

                lv_si=view.findViewById(R.id.region_si);

                Log.i("subin",response.body()+"do sever 성공");
                lv_do.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        String item=aa.getItem(i);
                        Log.i("subin","do: "+item);

                        Call<ArrayList<String>> getsi=RetrofitClient.getApiService().getSi(item);
                        Log.i("subin","si"+ getsi.toString());
                        getsi.enqueue(new Callback<ArrayList<String>>() {
                            @Override
                            public void onResponse(Call<ArrayList<String>> call, Response<ArrayList<String>> response) {
                                Log.i("subin","si sever 성공 : "+response.body());
                                ArrayList<String> al_si=new ArrayList<String>();
                                for (int s=0;s<response.body().size();s++){
                                    al_si.add(response.body().get(s));
                                }
                                Log.i("subin","si arraylist"+al_si);

                                ArrayAdapter<String> bb;
                                bb=new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1,al_si);
                                lv_si.setAdapter(bb);
                                lv_si.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                        String item=aa.getItem(i);
                                        Log.i("subin","si: "+item);
                                    }
                                });
                            }

                            @Override
                            public void onFailure(Call<ArrayList<String>> call, Throwable t) {
                                Log.i("subin","si sever 연결 실패"+t.getMessage());
                            }
                        });
                    }
                });
            }

            @Override
            public void onFailure(Call<ArrayList<String>> call, Throwable t) {
                Log.i("subin","연결실패"+t.getMessage());
            }
        });

        return view;
    }
}
