package com.example.howabout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;

public class RegistActivity extends AppCompatActivity {

    //중복여부
    private boolean validate = true;
    private AlertDialog dialog;
    private Spinner spinner_m;
    private Spinner spinner_d;
    static int click1 = 0;
    static int click2 = 0;
    static int click3 = 0;
    int gender = 0;

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

        //경고창 textview
        TextView warning_name = findViewById(R.id.warning_name);
        TextView warning_id = findViewById(R.id.warning_id);
        TextView warning_pw = findViewById(R.id.warning_pw);
        TextView warning_pwck = findViewById(R.id.warning_pwck);
        TextView spinner = findViewById(R.id.spinner);
        TextView radio = findViewById(R.id.radio);

        //버튼
        Button btn_namedoubleck = findViewById(R.id.btn_namedoubleck);
        Button btn_iddoubleck = findViewById(R.id.btn_iddoubleck);
        Button btn_regist = findViewById(R.id.btn_regist);

        //gender=1 일 경우 남성,gender=2 일 경우 여성
        radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.btn_man) {
                    gender = 1;
                } else if (i == R.id.btn_woman) {
                    gender = 2;
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

        //비밀번호 일치확인
        EditText ed_repwcheck = findViewById(R.id.ed_repwcheck);

        //비밀번호 일치확인
        ed_repwcheck.setFilters(filters2);
        ed_repwcheck.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                final String UserPw = ed_pw.getText().toString();
                final String pwcheck = ed_repwcheck.getText().toString();
                //b가 editText 포커스 됐을때
                if (b) {

                } else {
                    if (UserPw.equals(pwcheck)) {
                        warning_pwck.setText("");
                        click3 = 1;
                        //비밀번호 중복체크 click3 0,1
                        Log.i("subin", "click30: " + click3);
                    } else {
                        warning_pwck.setText("비밀번호가 일치하지 않습니다");
                    }
                }
            }
        });
        //닉네임 중복체크
        btn_namedoubleck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String UserNick = ed_nickname.getText().toString();
                //validate true 중복이 없음
                if (UserNick.equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegistActivity.this);
                    dialog = builder.setMessage("닉네임을 입력하세요.").setPositiveButton("확인", null).create();
                    dialog.show();
                    return;
                } else {
                    if (UserNick.length() < 6) {
                        warning_name.setText("닉네임은 6자이상 입력해야합니다");
                    } else if (validate) {
                        warning_name.setText(" ");
                        click1 = 1;
                        //닉네임 중복체크 click1 0,1
                        Log.i("subin", "click1:" + click1);
                        Toast.makeText(RegistActivity.this, "검증완료", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        warning_name.setText("이미 사용중이거나 탈퇴한 닉네임 입니다.");
                    }
                }
            }

        });
        //아이디 중복체크
        btn_iddoubleck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String UserId = ed_id.getText().toString();

                if (UserId.equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegistActivity.this);
                    dialog = builder.setMessage("아이디를 입력하세요.").setPositiveButton("확인", null).create();
                    dialog.show();
                    return;
                } else {
                    if (UserId.length() < 8) {
                        warning_id.setText("아이디는 8자 이상 입력해야 합니다");
                    } else if (validate) {
                        warning_id.setText(" ");
                        click2 = 1;
                        //click2 아이디 중복 체크 0,1
                        Toast.makeText(RegistActivity.this, "검증완료", Toast.LENGTH_SHORT).show();
                        return;//검증완료
                    } else {
                        warning_id.setText("이미 사용중이거나 탈퇴한 아이디 입니다.");
                    }
                }
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
                final String Birth = birth.getText().toString();
                final String spinnerjm = spinner_m.getSelectedItem().toString();
                final String spinnerjd = spinner_d.getSelectedItem().toString();

                if (UserName.equals("")) {
                    warning_name.setText("닉네임을 입력해주세요");
                } else {
                    warning_name.setText("");
                    //닉네임 정보 전달 UserName
                    if (UserName.length() < 6) {
                        warning_name.setText("닉네임은 6자이상 입력해야합니다");
                    } else if (click1 != 1) {
                        warning_name.setText("닉네임 중복 확인을 해주세요");
                    }
                }
                if (UserId.equals("")) {
                    warning_id.setText("아이디를 입력해주세요");
                } else {
                    warning_id.setText("");
                    //아이디 id intent 시 정보 전달
                    if (UserId.length() < 8) {
                        warning_id.setText("아이디는 8자 이상 입력해야 합니다");
                    } else if (click2 != 1) {
                        warning_id.setText("아이디 중복 확인을 해주세요");
                    }
                }
                if (UserPw.equals("")) {
                    warning_pw.setText("비밀번호를 입력해주세요");
                } else {
                    warning_pw.setText("");
                    if (UserPw.length() < 8) {
                        warning_pw.setText("비밀번호는 8자 이상 입력해야 합니다");
                    }
                }
                if (pwcheck.equals("")) {
                    click3 = 0;
                    warning_pwck.setText("비밀번호를 재확인을 입력 해주세요");
                    Log.i("subin", "click31: " + click3);
                } else {
                    Log.i("subin", "click32: " + click3);
                    if (click3 != 1) {
                        warning_pwck.setText("비밀번호가 일치하지 않습니다");
                        Log.i("subin", "click33: " + click3);
                    } else if (click3 == 1) {
                        warning_pwck.setText("비밀번호가 일치합니다");
                        //click3이 비밀번호 일치 시 UserPw intent시 정보전달

                    }
                }
                if (Birth.equals("")) {
                    spinner.setText("생년월일을 입력해주세요");
                } else {
                    if (1900 > Integer.parseInt(Birth) || 2023 < Integer.parseInt(Birth)) {
                        spinner.setText("올바른 생년월일을 입력해주세요");
                    } else {
                        spinner.setText("");
                        Log.i("subin", "B" + Birth);
                        Log.i("subin", "M" + spinnerjm);
                        Log.i("subin", "D" + spinnerjd);
                        //Birth 정보 intent 시 정보 전달
                        //spinner_m.getItemAtPosition(i),spinner_d.getItemAtPosition(i) 값도 전달
                    }

                }

                if (gender == 0) {
                    radio.setText("성별을 선택해주세요");
                } else {
                    radio.setText("");
                    //성별 gender =1,2 intent 될때 정보 전달
                }
            }
        });

        //Spinner 월/일
        spinner_m = findViewById(R.id.spinner_m);

        final ArrayList<String> list_m = new ArrayList<>();
        for (
                int i = 1;
                i < 13; i++) {
            list_m.add(i + "월");
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
            list_d.add(i + "일");
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
