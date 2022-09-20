package com.example.howabout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class RegistActivity extends AppCompatActivity {

    private boolean validate = false;
    private AlertDialog dialog;

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
        EditText signbirth1 = findViewById(R.id.signBirth);

        EditText signbirth2 = findViewById(R.id.signBirth2);
        EditText signbirth3 = findViewById(R.id.signBirth3);

        //비밀번호 일치확인
        EditText ed_repwcheck = findViewById(R.id.ed_repwcheck);

        //중복체크
        Button btn_namedoubleck = findViewById(R.id.btn_namedoubleck);
        btn_namedoubleck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String UserNick = ed_nickname.getText().toString();
                if (validate) {
                    Toast.makeText(RegistActivity.this, "검증완료", Toast.LENGTH_SHORT).show();
                    return;//검증완료
                }
                if (UserNick.equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegistActivity.this);
                    dialog = builder.setMessage("닉네임을 입력하세요.").setPositiveButton("확인", null).create();
                    dialog.show();
                    return;
                }
            }
        });
        Button btn_iddoubleck = findViewById(R.id.btn_iddoubleck);
        btn_iddoubleck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String UserId = ed_id.getText().toString();
                if (validate) {
                    Toast.makeText(RegistActivity.this, "검증완료", Toast.LENGTH_SHORT).show();
                    return;//검증완료
                }
                if (UserId.equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegistActivity.this);
                    dialog = builder.setMessage("아이디를 입력하세요.").setPositiveButton("확인", null).create();
                    dialog.show();
                    return;
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
                if (UserPw.equals(pwcheck)) {
                    Toast.makeText(RegistActivity.this, "비밀번호가 일치합니다", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RegistActivity.this, "일치하지 않습니다", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}