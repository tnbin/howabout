package com.example.howabout.Login;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

    int emailck = 0;

    public FindPwCheck() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {

        super.onAttach(context);
        activity = (FindUserInfoActivity) getActivity();
    }

    @Override
    public void onDetach() {

        super.onDetach();
        activity = null;
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
        View view = inflater.inflate(R.layout.login_findpwcheck, container, false);
        //???????????? ????????? ?????? ??????
        btn_find_pw_check = view.findViewById(R.id.btn_find_pw_check);
        //????????? ?????????
        ed_find_pw_id = view.findViewById(R.id.ed_find_pw_id);
        //????????? ??????
        ed_find_pw_email = view.findViewById(R.id.ed_find_pw_email);
        //????????? ?????? ??????
        btn_email_ck = view.findViewById(R.id.btn_email_ck);
        //????????? ???????????? ?????? layout
        layout_find_emailconfirm = view.findViewById(R.id.layout_find_emailconfirm);
        //????????? ???????????? ?????????
        ed_find_pw_email_ck = view.findViewById(R.id.ed_find_pw_email_ck);
        //????????? ???????????? ?????? ?????? ??????
        btn_emailconfirmck = view.findViewById(R.id.btn_emailconfirmck);

        btn_email_ck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //????????? ????????? ????????? ????????? ????????? ????????????
                String ed_email = ed_find_pw_email.getText().toString();
                Map edemail = new HashMap();
                edemail.put("u_email", ed_email);
                Call<Integer> sendemailck = RetrofitClient.getApiService().emailSend(edemail);
                sendemailck.enqueue(new Callback<Integer>() {
                    @Override
                    public void onResponse(Call<Integer> call, Response<Integer> response) {
                        Integer sendemailck = response.body();
                        if (sendemailck == 0) {
                            //db??? ????????? ???????????? ??????
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setTitle("???????????? ?????????");
                            builder.setMessage("???????????? ?????? ?????????????????????");
                            builder.setPositiveButton("??????", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            });
                            builder.create();
                            builder.show();
                            Log.i("subin", "email ?????? ???????????? ??????");
                        } else if (sendemailck == 1) {
                            //?????? ??????
                            layout_find_emailconfirm.setVisibility(View.VISIBLE);
                            Log.i("subin", "email ?????? ?????????");
                            Toast.makeText(getActivity(), "???????????? ????????? ??????????????? ??????????????????", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Integer> call, Throwable t) {

                    }
                });
            }
        });
        btn_emailconfirmck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //????????? ???????????? ?????? ?????? ???????????? ????????? (??????????????? ?????????,????????????)
                String pw_ed_email_ck = ed_find_pw_email_ck.getText().toString();
                String pw_ed_email = ed_find_pw_email.getText().toString();
                try {
                    if (pw_ed_email_ck.isEmpty()) {
                        //???????????? ??????????????????
                    } else {
                        Map pw_emailck = new HashMap();
                        pw_emailck.put("u_email", pw_ed_email);
                        pw_emailck.put("auth", pw_ed_email_ck);
                        Call<Integer> sendemailnumck = RetrofitClient.getApiService().emailAuthCheck(pw_emailck);
                        sendemailnumck.enqueue(new Callback<Integer>() {
                            @Override
                            public void onResponse(Call<Integer> call, Response<Integer> response) {
                                Integer sendemailck = response.body();
                                if (sendemailck == 1) {
                                    //??????????????? ??????
                                    Log.i("subin", "email ?????? ??????");
                                    emailck = 1;
                                    Toast.makeText(getActivity(), "????????? ?????? ??????", Toast.LENGTH_SHORT).show();
                                } else if (sendemailck == 0) {
                                    //????????? ?????? ??????
                                    emailck = 0;
                                    Toast.makeText(getActivity(), "???????????? ????????? ??????????????? ?????? ??????????????????", Toast.LENGTH_SHORT).show();
                                    Log.i("subin", "email ?????? ??????");
                                }
                            }

                            @Override
                            public void onFailure(Call<Integer> call, Throwable t) {
                                Log.i("subin", "?????? ??????" + t.getMessage());
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        btn_find_pw_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //????????? ?????? ????????? ??? ?????????
                //????????? ????????? alert??? ????????????
                if (emailck == 1) {
                    String ed_pw_id = ed_find_pw_id.getText().toString();
                    String ed_pw_email = ed_find_pw_email.getText().toString();
                    Checkidemail(ed_pw_id, ed_pw_email);
                } else {
                    Toast.makeText(getActivity(), "???????????? ?????? ??????????????????", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }

    public void Checkidemail(String id, String email) {
        Map checkidemail = new HashMap();
        checkidemail.put("u_id", id);
        checkidemail.put("u_email", email);

        Call<Integer> CheckInfo = RetrofitClient.getApiService().usercheck(checkidemail);
        CheckInfo.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                Log.i("subin", "?????? ??????" + response.body());
                Integer posiblereset = response.body();
                if (posiblereset == 0) {
                    //??????
                    Log.i("subin", "???????????? ??????");
                    Toast.makeText(getActivity(), "??????????????? ???????????? ?????? ??????????????????", Toast.LENGTH_SHORT).show();
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putString("u_id", id);
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    ResetPw resetPw = new ResetPw();
                    resetPw.setArguments(bundle);
                    transaction.replace(R.id.container, resetPw);
                    transaction.commit();
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Log.i("subin", "?????? ??????: " + t.getMessage());
            }
        });
    }
}