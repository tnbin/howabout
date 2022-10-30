package com.example.howabout.FindUserInfo;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.howabout.API.RetrofitClient;
import com.example.howabout.FindUserInfoActivity;
import com.example.howabout.R;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FindPwCheck extends Fragment {

    FindUserInfoActivity activity;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    EditText ed_find_pw_id;
    EditText ed_find_pw_email;
    EditText ed_find_pw_email_ck;
    Button btn_email_ck;
    Button btn_find_pw_check;
    LinearLayout layout_find_emailconfirm;
    Button btn_emailconfirmck;

    public FindPwCheck() {
        // Required empty public constructor
    }
    @Override
    public void onAttach(Context context) {

        super.onAttach(context);
        activity=(FindUserInfoActivity) getActivity();
    }
    @Override
    public void onDetach() {

        super.onDetach();
        activity=null;
    }

    // TODO: Rename and change types and number of parameters
    public static FindPwCheck newInstance(String param1, String param2) {
        FindPwCheck fragment = new FindPwCheck();
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
        View view=inflater.inflate(R.layout.findpwcheck, container, false);
        //비밀번호 재설정 확인 버튼
        btn_find_pw_check=view.findViewById(R.id.btn_find_pw_check);
        //아이디 입력창
        ed_find_pw_id=view.findViewById(R.id.ed_find_pw_id);
        //이메일 주소
        ed_find_pw_email=view.findViewById(R.id.ed_find_pw_email);
        //이메일 인증 버튼
        btn_email_ck=view.findViewById(R.id.btn_email_ck);
        //이메일 인증번호 입력 layout
        layout_find_emailconfirm=view.findViewById(R.id.layout_find_emailconfirm);
        //이메일 인증번호 입력란
        ed_find_pw_email_ck=view.findViewById(R.id.ed_find_pw_email_ck);
        //이메일 인증번호 입력 확인 버튼
        btn_emailconfirmck=view.findViewById(R.id.btn_emailconfirmck);

        btn_email_ck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout_find_emailconfirm.setVisibility(View.VISIBLE);
                //서버로 이메일 입력값 보내서 이메일 인증하기
            }
        });
        btn_emailconfirmck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //서버로 이메일로 받은 번호 입력해서 보내기 (이메일주소 입력값,인증번호)
            }
        });
        btn_find_pw_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //아이디 값과 이메일 값 보내기
                //서버로 받은값 alert로 보여주기
                String ed_pw_id=ed_find_pw_id.getText().toString();
                String ed_pw_email=ed_find_pw_email.getText().toString();
                Checkidemail(ed_pw_id,ed_pw_email);
            }
        });
        return view;
    }
    public void Checkidemail(String id,String email){
        Map checkidemail = new HashMap();
        checkidemail.put("u_id",id);
        checkidemail.put("u_email", email);

        Call<Integer>CheckInfo= RetrofitClient.getApiService().usercheck(checkidemail);
        CheckInfo.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                Log.i("subin", "연결 성공" + response.body());
                Integer posiblereset=response.body();
                if (posiblereset==0){
                    //실패
                    Toast.makeText(getActivity(),"회원정보가 없습니다 다시 입력해주세요",Toast.LENGTH_SHORT).show();
                }else{

                    Bundle bundle=new Bundle();
                    bundle.putString("u_id",id);
                    FragmentTransaction transaction=getActivity().getSupportFragmentManager().beginTransaction();
                    ResetPw resetPw=new ResetPw();
                    resetPw.setArguments(bundle);
                    transaction.replace(R.id.container,resetPw);
                    transaction.commit();
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Log.i("subin", "연결 실패: " + t.getMessage());
            }
        });
    }
}