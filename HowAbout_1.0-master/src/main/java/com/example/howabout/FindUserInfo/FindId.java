package com.example.howabout.FindUserInfo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.howabout.API.RetrofitClient;
import com.example.howabout.R;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FindId extends Fragment {

    EditText ed_search_id_email;
    Button btn_search_id;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FindId() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static FindId newInstance(String param1, String param2) {
        FindId fragment = new FindId();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.findid, container, false);
        ed_search_id_email = view.findViewById(R.id.ed_search_id_email);

        btn_search_id = view.findViewById(R.id.btn_search_id);

        btn_search_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ed_email = ed_search_id_email.getText().toString();
                Log.i("subin", "입력한 값: " + ed_email);
                Search_id(ed_email);
                ed_search_id_email.setText("");
            }
        });

        return view;
    }

    public void Search_id(String email) {
        Map postemail = new HashMap();
        postemail.put("u_email", email);

        Call<Map<String, String>> FindId = RetrofitClient.getApiService().search_id(postemail);
        FindId.enqueue(new Callback<Map<String, String>>() {
            @Override
            public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                Map<String, String> result = response.body();
                Log.i("subin", "연결 성공" + result);
                String result_id = result.get("u_id");

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("아이디 찾기");
                if (result_id == null) {
                    builder.setMessage("이메일 주소와 일치되는 아이디가 없습니다.");
                } else {
                    builder.setMessage(result_id);
                }
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.create();
                builder.show();
            }

            @Override
            public void onFailure(Call<Map<String, String>> call, Throwable t) {
                Log.i("subin", "연결 실패: " + t.getMessage());
            }
        });
    }
}