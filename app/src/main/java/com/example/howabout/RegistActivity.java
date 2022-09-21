package com.example.howabout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
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
    static int click1=0;
    static int click2=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.regist);

        //회원가입 정보
        EditText ed_nickname = findViewById(R.id.ed_renickname);
        EditText ed_id = findViewById(R.id.ed_reid);
        EditText ed_pw = findViewById(R.id.ed_repw);
        RadioButton btn_man = findViewById(R.id.btn_man);
        RadioButton btn_woman = findViewById(R.id.btn_woman);
        EditText birth = findViewById(R.id.signBirth);

        TextView warning_name = findViewById(R.id.warning_name);
        TextView warning_id = findViewById(R.id.warning_id);
        TextView warning_pw=findViewById(R.id.warning_pw);
        TextView warning_pwck=findViewById(R.id.warning_pwck);
        TextView spinner=findViewById(R.id.spinner);
        TextView radio=findViewById(R.id.radio);

//        //텍스트 입력 제한
//        if (ed_nickname.length()<21){
//
//        }else {
//            warning_name.setText("최대 20자까지 입력가능합니다");
//        }
        //비밀번호 일치확인
        EditText ed_repwcheck = findViewById(R.id.ed_repwcheck);
        ed_repwcheck.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                final String UserPw = ed_pw.getText().toString();
                final String pwcheck = ed_repwcheck.getText().toString();
                //b가 editText 포커스 됐을때
                if (b){

                }else {
                    if (UserPw.equals(pwcheck)) {
                        warning_pwck.setText("");
                    } else {
                        warning_pwck.setText("비밀번호가 일치하지 않습니다");
                    }
                }
            }
        });
        //닉네임 중복체크
        Button btn_namedoubleck = findViewById(R.id.btn_namedoubleck);

        btn_namedoubleck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String UserNick = ed_nickname.getText().toString();

                if (UserNick.equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegistActivity.this);
                    dialog = builder.setMessage("닉네임을 입력하세요.").setPositiveButton("확인", null).create();
                    dialog.show();
                    return;
                } else {
                    //validate true 중복이 없음
                    if (validate == true) {
                        warning_name.setText(" ");
                        click1=1;
                        Log.i("subin","click1:"+click1);
                        Toast.makeText(RegistActivity.this, "검증완료", Toast.LENGTH_SHORT).show();
                        return;
                    } else if (validate == false) {
                        warning_name.setText("이미 사용중이거나 탈퇴한 닉네임 입니다.");
                    }
                }
            }

        });
        //아이디 중복체크
        Button btn_iddoubleck = findViewById(R.id.btn_iddoubleck);
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
                    if (validate == true) {
                        warning_id.setText(" ");
                        click2=1;
                        Log.i("subin","click1:"+click1);
                        Log.i("subin","click2: "+click2);
                        Toast.makeText(RegistActivity.this, "검증완료", Toast.LENGTH_SHORT).show();
                        return;//검증완료
                    } else if (validate == false) {
                        warning_id.setText("이미 사용중이거나 탈퇴한 아이디 입니다.");
                    }
                }
            }
        });

        //회원가입 버튼
        Button btn_regist = findViewById(R.id.btn_regist);
        btn_regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String UserPw = ed_pw.getText().toString();
                final String pwcheck = ed_repwcheck.getText().toString();
                final String UserName=ed_nickname.getText().toString();
                final String UserId=ed_id.getText().toString();
                final String Birth=birth.getText().toString();

                Log.i("subin","click1:"+click1);
                Log.i("subin","click2: "+click2);

                if (UserName.equals("")){
                    warning_name.setText("닉네임을 입력해주세요");
                }else if (UserPw.equals("")){
                    warning_pw.setText("비밀번호를 입력해주세요");
                }else  if (UserId.equals("")){
                    warning_id.setText("아이디를 입력해주세요");
                }else if (Birth.equals("")){
                    spinner.setText("생년월일을 입력해주세요");
                }else if (pwcheck.equals("")){
                    warning_pwck.setText("비밀번호를 재확인 해주세요");
                }else{
                    Toast.makeText(RegistActivity.this,"완료",Toast.LENGTH_SHORT).show();
                }

                if (click1!=1){
                    warning_name.setText("중복 확인을 해주세요");
                }
                if (click2!=1){
                    warning_id.setText("중복 확인을 해주세요");
                }

            }
        });

        //Spinner 월/일
        spinner_m = findViewById(R.id.spinner_m);
        final ArrayList<String> list_m = new ArrayList<>();
        for (int i = 1; i < 13; i++) {
            list_m.add(i + "월");
        }
        ArrayAdapter spinnerAdapter1 = new ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, list_m);
        spinner_m.setAdapter(spinnerAdapter1);

        //spinner가 클릭되었을때
        spinner_m.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(RegistActivity.this, "" + spinner_m.getItemAtPosition(i), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinner_d = findViewById(R.id.spinner_d);
        final ArrayList<String> list_d = new ArrayList<>();
        for (int i = 1; i < 32; i++) {
            list_d.add(i + "일");
        }
        ArrayAdapter spinnerAdapter2 = new ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, list_d);
        spinner_d.setAdapter(spinnerAdapter2);


        spinner_d.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(RegistActivity.this, "" + spinner_d.getItemAtPosition(i), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }
}
