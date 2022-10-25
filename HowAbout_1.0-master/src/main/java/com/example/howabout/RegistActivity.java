package com.example.howabout;


import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.example.howabout.API.RetrofitClient;
import com.example.howabout.Vo.IdVo;
import com.example.howabout.Vo.NickNameVo;
import com.example.howabout.Vo.UserVo;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistActivity extends AppCompatActivity {

    //중복여부
    private Spinner spinner_m;
    private Spinner spinner_d;
    static int click1 = 0;
    static int click2 = 0;
    static int click3 = 0;
    static int gender = 1;
    static int checkBirth = 0;
    String inputID;
    String inputName;
    String inputPw;
    String inputPwCk;
    String inputbirth;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.regist);

        //회원가입 정보
        EditText ed_nickname = findViewById(R.id.ed_renickname);
        EditText ed_id = findViewById(R.id.ed_reid);
        EditText ed_pw = findViewById(R.id.ed_repw);
        RadioGroup radiogroup = findViewById(R.id.radiogroup);
        EditText birth = findViewById(R.id.signBirth);
        EditText ed_repwcheck = findViewById(R.id.ed_repwcheck);

        //경고창 textview
        TextView warning_name = findViewById(R.id.warning_name);
        TextView warning_id = findViewById(R.id.warning_id);
        TextView warning_pw = findViewById(R.id.warning_pw);
        TextView warning_pwck = findViewById(R.id.warning_pwck);
        TextView spinner = findViewById(R.id.spinner);

        //버튼
        Button btn_namedoubleck = findViewById(R.id.btn_namedoubleck);
        Button btn_iddoubleck = findViewById(R.id.btn_iddoubleck);
        Button btn_regist = findViewById(R.id.btn_regist);

        //gender=1 일 경우 남성,gender=0 일 경우 여성
        radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.btn_man) {
                    gender = 1;
                } else if (i == R.id.btn_woman) {
                    gender = 0;
                }
            }
        });
        //입력창 조건 부여
        InputFilter[] filters1 = new InputFilter[]{
                new InputFilter.LengthFilter(20),
        };
        ed_nickname.setFilters(filters1);
        InputFilter[] filters2 = new InputFilter[]{
                new InputFilter.LengthFilter(30),
        };
        InputFilter[] filters3 = new InputFilter[]{
                new InputFilter.LengthFilter(4),
        };
        ed_id.setFilters(filters2);
        ed_pw.setFilters(filters2);
        birth.setFilters(filters3);

        //비밀번호
        ed_pw.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onFocusChange(View view, boolean b) {
                final String UserPw = ed_pw.getText().toString();
                if (b) {
                    if (UserPw.equals("")) {
                        warning_pw.setText("8자 이상 30자 이하로 비밀번호를 입력해주세요");
                    } else {
                        warning_pw.setText("");
                    }
                } else {
                    try {
                        if (UserPw.equals("")) {
                            warning_pw.setText("비밀번호를 입력해주세요");
                        } else {
                            warning_pw.setText("");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        //비밀번호 확인
        ed_repwcheck.setFilters(filters2);
        ed_repwcheck.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                final String UserPw = ed_pw.getText().toString();
                final String pwcheck = ed_repwcheck.getText().toString();
                //b가 editText 포커스 됐을때
                if (b) {
                    if (UserPw.equals("")) {
                        warning_pw.setText("비밀번호를 입력해주세요");
                    } else {
                        warning_pw.setText("");
                        if (UserPw.length() < 8) {
                            warning_pw.setText("비밀번호는 8자 이상 입력해야 합니다");
                        } else {
                            inputPw = UserPw;
                        }
                    }
                } else {
                    try {
                        if (UserPw.equals(pwcheck)) {
                            warning_pwck.setText("");
                            inputPwCk = pwcheck;
                            click3 = 1;
                        } else {
                            warning_pwck.setText("비밀번호가 일치하지 않습니다");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        //닉네임 중복체크
        btn_namedoubleck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String UserNick = ed_nickname.getText().toString();
                int checkName = 0;

                try {
                    if (UserNick.equals("")) {
                        warning_name.setText("닉네임을 입력해주세요");
                        return;
                    } else {
                        if (UserNick.length() < 3) {
                            warning_name.setText("닉네임은 3자이상 입력해야합니다");
                        }
                        //닉네임 3자 이상 입력
                        else {
                            checkName = 1;
                        }
                        if (checkName == 1) {
                            NickNameVo input = new NickNameVo(UserNick);
                            Log.i("subin", "NameCk: " + input);

                            Call<Integer> test1 = RetrofitClient.getApiService().nickcheck(input);
                            test1.enqueue(new Callback<Integer>() {
                                @Override
                                public void onResponse(Call<Integer> call, Response<Integer> response) {
                                    int res = response.body();
                                    Log.i("subin", "Name post 성공");
                                    Log.i("subin", "NCK:" + res);
                                    if (res == 1) {
                                        warning_name.setText(" ");
                                        click1 = 1;
                                        inputName = UserNick;
                                        //닉네임 중복체크 click1 0,1
                                        Log.i("subin", "click1:" + click1);
                                        Toast.makeText(RegistActivity.this, "검증완료", Toast.LENGTH_SHORT).show();
                                        return;
                                    } else {
                                        warning_name.setText("이미 사용중이거나 탈퇴한 닉네임 입니다.");
                                    }
                                }
                                @Override
                                public void onFailure(Call<Integer> call, Throwable t) {
                                    Log.i("subin", "NCK post 실패 : " + t.getMessage());
                                }
                            });
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });
        //아이디 중복체크
        btn_iddoubleck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String UserId = ed_id.getText().toString();
                int checkId = 0;

                if (UserId.equals("")) {
                    warning_id.setText("아이디를 입력해주세요");
                    return;
                } else {
                    if (UserId.length() < 8) {
                        warning_id.setText("아이디는 8자 이상 입력해야 합니다");
                    } else {
                        //아이디 8자 이상 입력 완료
                        checkId = 1;
                    }
                    Log.i("subin", "" + checkId);
                    if (checkId == 1) {
                        Log.i("subin", "" + checkId);
                        IdVo inputId = new IdVo(UserId);
                        Call<Integer> test2 = RetrofitClient.getApiService().idcheck(inputId);
                        test2.enqueue(new Callback<Integer>() {
                            @Override
                            public void onResponse(Call<Integer> call, Response<Integer> response) {
                                Log.i("subin", "id server 연결 성공");
                                int resid = response.body();
                                Log.i("subin", "Id response" + resid);
                                if (resid == 1) {
                                    warning_id.setText(" ");
                                    click2 = 1;
                                    inputID = UserId;
                                    Toast.makeText(RegistActivity.this, "검증완료", Toast.LENGTH_SHORT).show();
                                    return;//검증완료
                                } else if (resid == 0) {
                                    warning_id.setText("이미 사용중이거나 탈퇴한 아이디 입니다.");
                                }
                            }

                            @Override
                            public void onFailure(Call<Integer> call, Throwable t) {
                                Log.i("subin", "id post 실패" + t.getMessage());
                            }
                        });

                    }

                }
            }
        });
        birth.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String Birth = birth.getText().toString();
                try {
                    if (Birth.equals("")) {
                        spinner.setText("생년월일을 입력해주세요");
                    } else {
                        if (1900 > Integer.parseInt(Birth) || 2023 < Integer.parseInt(Birth)) {
                            spinner.setText("올바른 생년월일을 입력해주세요");
                        } else {
                            spinner.setText("");
                            checkBirth = 1;

                            Log.i("subin", "b" + checkBirth);
                        }
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //회원가입 버튼
        btn_regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String UserPw = ed_pw.getText().toString();
                final String pwcheck = ed_repwcheck.getText().toString();
                final String UserName = ed_nickname.getText().toString();
                final String UserId = ed_id.getText().toString();
                final String BirthY = birth.getText().toString();
                final String spinnerjm = spinner_m.getSelectedItem().toString();
                final String spinnerjd = spinner_d.getSelectedItem().toString();

                final String Birth = BirthY + "-" + spinnerjm + "-" + spinnerjd;

                Log.i("subin", "001");
                if (!inputID.equals(UserId)) {
                    click1 = 0;
                    warning_id.setText("다시 아이디 작성해주세요");
                }
                Log.i("subin", "002");
                if (!inputName.equals(UserName)) {
                    click2 = 0;
                    warning_name.setText("다시 닉네임 입력해주세요");
                }
                Log.i("subin", "003");
                if (!inputPw.equals(UserPw) && !inputPwCk.equals(pwcheck)) {
                    click3 = 0;
                    warning_pw.setText("다시 비밀번호 입력해주세요");
                }
//
//                if (!inputbirth.equals(BirthY)){
//                    checkBirth=0;
//                    spinner.setText("생년월일을 다시 입력해주세요");
//                }

                try {
                    if (click1 == click2 && click2 == click3 && click3 == checkBirth && click1 == 1) {
                        Log.i("subin", "로그인성공");

                        UserVo inputuser = new UserVo(UserName, UserId, UserPw, Birth, gender);
                        Call<Integer> all = RetrofitClient.getApiService().all(inputuser);
                        all.enqueue(new Callback<Integer>() {
                            @Override
                            public void onResponse(Call<Integer> call, Response<Integer> response) {
                                int allres = response.body();
                                Log.i("subin", "all server 연결 성공" + allres);
                                if (allres == 1) {
                                    Intent intent = new Intent(RegistActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    Log.i("subin", "success!!");

                                } else {
                                    //로그인 실패
                                    Toast.makeText(RegistActivity.this, "로그인 실패", Toast.LENGTH_SHORT).show();

                                    Log.i("subin", "로그인 실패");
                                }
                            }

                            @Override
                            public void onFailure(Call<Integer> call, Throwable t) {

                                Log.i("subin", "연결실패" + t.getMessage());
                            }
                        });

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        //Spinner 월/일
        spinner_m = findViewById(R.id.spinner_m);

        final ArrayList<String> list_m = new ArrayList<>();
        for (
                int i = 1;
                i < 13; i++) {
            list_m.add(i + "");
        }

        ArrayAdapter spinnerAdapter1 = new ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, list_m);
        spinner_m.setAdapter(spinnerAdapter1);

        //spinner가 클릭되었을때
        spinner_m.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinner_d = findViewById(R.id.spinner_d);

        final ArrayList<String> list_d = new ArrayList<>();
        for (
                int i = 1;
                i < 32; i++) {
            list_d.add(i + "");
        }

        ArrayAdapter spinnerAdapter2 = new ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, list_d);
        spinner_d.setAdapter(spinnerAdapter2);


        spinner_d.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}
