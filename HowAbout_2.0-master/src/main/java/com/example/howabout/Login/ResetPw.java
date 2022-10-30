package com.example.howabout.Login;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.howabout.API.RetrofitClient;
import com.example.howabout.FindUserInfoActivity;
import com.example.howabout.LoginActivity;
import com.example.howabout.R;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ResetPw extends Fragment {

    EditText ed_reset_pw;
    EditText ed_reset_pwcheck;
    Button btn_reset_pw;
    TextView warning_repwcheck;
    TextView warning_repw;
    String inputRePwCk;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ResetPw() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static ResetPw newInstance(String param1, String param2) {
        ResetPw fragment = new ResetPw();
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
        View view = inflater.inflate(R.layout.resetpw, container, false);
        ed_reset_pw = view.findViewById(R.id.ed_reset_pw);
        ed_reset_pwcheck = view.findViewById(R.id.ed_reset_pwcheck);
        btn_reset_pw = view.findViewById(R.id.btn_reset_pw);
        warning_repwcheck = view.findViewById(R.id.warning_repwcheck);
        warning_repw = view.findViewById(R.id.warning_repw);

        ed_reset_pw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                final String UserPw = ed_reset_pw.getText().toString();
                try {
                    if (UserPw.equals("")) {
                        warning_repw.setText("8자 이상 30자 이하로 비밀번호를 입력해주세요");
                    } else {
                        warning_repw.setText("");
                        if (UserPw.length() < 8) {
                            warning_repw.setText("비밀번호는 8자 이상 입력해야 합니다");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    warning_repw.setText("비밀번호를 입력해주세요");
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        ed_reset_pwcheck.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                final String UserPw = ed_reset_pw.getText().toString();
                final String pwcheck = ed_reset_pwcheck.getText().toString();
                Log.e("subin", "비밀번호: " + UserPw);
                Log.e("subin", "비밀번호 확인: " + pwcheck);

                try {
                    if (UserPw.equals(pwcheck)) {
                        warning_repwcheck.setText("");
                        if (pwcheck.length() < 8) {
                            warning_repwcheck.setText("비밀번호를 8자 이상 적어주세요");
                        } else {
                            inputRePwCk = pwcheck;
                        }
                    } else {
                        warning_repwcheck.setText("비밀번호가 일치하지 않습니다");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    warning_repwcheck.setText("비밀번호를 확인할 수 없습니다");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        btn_reset_pw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String UserPw = ed_reset_pw.getText().toString();
                final String pwcheck = ed_reset_pwcheck.getText().toString();
                Log.e("subin", "비밀번호 재설정: " + inputRePwCk);
                if (inputRePwCk == null) {
                    Toast.makeText(getActivity(), "다시 비밀번호 설정해주세요", Toast.LENGTH_SHORT).show();
                } else {
                    if (UserPw.equals(pwcheck)){
                        setpasswordagain(inputRePwCk);
                    }
                }
            }
        });
        return view;
    }

    public void setpasswordagain(String pw) {
        Map setpw = new HashMap();
        String u_id = null;
        if (getArguments() != null) {
            u_id = getArguments().getString("u_id");
        }
        Log.e("subin", "" + u_id);
        setpw.put("u_id", u_id);
        setpw.put("u_pw", pw);
        Call<Integer> resetpw = RetrofitClient.getApiService().repw(setpw);
        resetpw.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                Log.e("subin", "연결성공: " + response.body());
                Integer sucess = response.body();
                if (sucess == null) {
                    Toast.makeText(getActivity(), "비밀번호 재설정이 실패했습니다 다시 시도해주세요", Toast.LENGTH_SHORT).show();
                } else if (sucess == 1) {
                    Toast.makeText(getActivity(), "비밀번호 재설정이 성공했습니다", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Log.i("subin", "연결실패: " + t.getMessage());
            }
        });
    }
}