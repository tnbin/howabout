package com.example.howabout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.howabout.API.RetrofitClient;
import com.example.howabout.DTO.UserDTO;
import com.example.howabout.functions.HowAboutThere;


import org.json.simple.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyPageActivity extends AppCompatActivity implements Serializable {
    TextView et_nick, et_id, et_email, et_birth, et_gender;
    Button btn_editInfo, btn_withdrawal;
    RadioGroup withdrawal_radioG;
    UserDTO user_info;
    int CODE_checkPW;

    Intent intent;

    SharedPreferences sharedPreferences; //공유 프레퍼런스

    HowAboutThere FUNC = new HowAboutThere(); //함수 모음 클래스

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage);

        FUNC.sideBar(MyPageActivity.this); //사이드바

        sharedPreferences = getSharedPreferences("USER", Activity.MODE_PRIVATE);
        String token = "Bearer " + sharedPreferences.getString("token", null); //토큰

        Log.e("leehj", "token: " + token);

        getUserInfo(token); //서버에서 유저 정보 가져오기. 정보수정
        click_withdrawal(token); //회원탈퇴

    }

    //유저 정보 가져오기
    public void getUserInfo(String token) {
        Call<UserDTO> getUserInfo = RetrofitClient.getApiService().myDataUp(token);
        getUserInfo.enqueue(new Callback<UserDTO>() {
            @Override
            public void onResponse(Call<UserDTO> call, Response<UserDTO> response) {
                user_info = response.body();
                Log.i("leehj", "마이페이지 유저정보 가져오기: " + user_info);
                setUserInfo(user_info);
                click_editInfo(user_info, token); //정보수정 버튼 클릭 이벤트 처리
            }

            @Override
            public void onFailure(Call<UserDTO> call, Throwable t) {
                Log.i("leehj", "마이페이지 유저 정보 가져오기 실패: " + t);
            }
        });
    }

    //유저 정보 세팅하기
    public void setUserInfo(UserDTO user) {
        et_id = (TextView) findViewById(R.id.mypage_tv_id);
        et_nick = (TextView) findViewById(R.id.mypage_tv_nickname);
        et_email = (TextView) findViewById(R.id.mypage_tv_email);
        et_gender = (TextView) findViewById(R.id.mypage_tv_gender);
        et_birth = (TextView) findViewById(R.id.mypage_tv_birth);

        et_id.setSelected(true);
        et_nick.setSelected(true);
        et_email.setSelected(true);

        et_id.setText(user.getU_id());
        et_nick.setText(user.getU_nick());
        et_email.setText(user.getU_email());
        et_birth.setText(user.getBirth());
        Log.e("leehj", "------------------" + user.getU_id());

        if (user.getGender() == 1) {
            et_gender.setText("남자");
        } else if (user.getGender() == 0) {
            et_gender.setText("여자");
        }
    }

    //비밀번호 확인
    public void checkPw(String pw, String token) {
        Map<String, String> data_pw = new HashMap<>();
        data_pw.put("u_pw", pw);
        Log.i("leehj", "비밀번호 확인 data_pw.get(\"u_pw\") : " + data_pw.get("u_pw"));
        Call<Integer> checkPw = RetrofitClient.getApiService().checkPW(data_pw, token);
        checkPw.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                CODE_checkPW = response.body();
                Log.e("leehj", "마이페이지 비밀번호 확인(1이면 일치): " + CODE_checkPW);
                if (CODE_checkPW == 1) { //비밀번호가 일치하면
                    Intent intent = new Intent(MyPageActivity.this, UpdateInfo.class);
                    intent.putExtra("user_info", user_info);
                    startActivityForResult(intent, 1);
                } else {
                    Toast.makeText(MyPageActivity.this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Log.i("leehj", "마이페이지 비밀번호 확인 통신 실패: " + t);
            }
        });
    }

    //정보 수정 버튼 클릭 이벤트
    public void click_editInfo(UserDTO user, String token) {
        btn_editInfo = (Button) findViewById(R.id.mypage_btn_editInfo);
        btn_editInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //다이얼 로그 띄워서 비밀번호 확인 받고 정보 수정 페이지로 넘어가요
                Dialog dialog = new Dialog(MyPageActivity.this);
                dialog.setContentView(R.layout.mypage_dialog_inputpw);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                EditText et_inputPW = (EditText) dialog.findViewById(R.id.dialog_et_inputPW);
                Button btn_check = (Button) dialog.findViewById(R.id.dialog_btn_checkPW);
                btn_check.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        String pw = et_inputPW.getText().toString();
                        checkPw(pw, token);
                    }
                });
                dialog.show();
            }
        });
    }

    public void click_withdrawal(String token) {
        btn_withdrawal = (Button) findViewById(R.id.mypage_btn_withdrawal);
        btn_withdrawal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //다이얼로그 띄워서 비밀번호 확인하고 탈퇴사유 띄워줘요 (반대)
                //1. 탈퇴사유 띄워주기
                //2. 비밀번호 확인
                JSONObject withDrawal_data = new JSONObject();

                Dialog dialog = new Dialog(MyPageActivity.this);
                dialog.setContentView(R.layout.mypage_dialog_withdrawal);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                withdrawal_radioG = (RadioGroup) dialog.findViewById(R.id.dialog_with_radioGroup);
                withdrawal_radioG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int i) {
                        switch (i) {
                            case R.id.dialog_radio1:
                                withDrawal_data.put("reason", "이용기회 상실");
                                break;
                            case R.id.dialog_radio2:
                                withDrawal_data.put("reason", "이용방법이 어려움");
                                break;
                            case R.id.dialog_radio3:
                                withDrawal_data.put("reason", "서비스 불만족");
                                break;
                            case R.id.dialog_radio4:
                                withDrawal_data.put("reason", "기타");
                                break;
                        }
                        Log.e("leehj", "회원 탈퇴: " + withDrawal_data.get("reason"));
                    }
                });

                Button btn_withdrawal = (Button) dialog.findViewById(R.id.dialog_with_btn_withdrawal);
                btn_withdrawal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        Dialog dialog_checkPW = new Dialog(MyPageActivity.this);
                        dialog_checkPW.setContentView(R.layout.mypage_dialog_inputpw);
                        dialog_checkPW.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                        EditText et_pw = (EditText) dialog_checkPW.findViewById(R.id.dialog_et_inputPW);

                        Button btn_check_pw = (Button) dialog_checkPW.findViewById(R.id.dialog_btn_checkPW);
                        btn_check_pw.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                withDrawal_data.put("u_pw", et_pw.getText().toString());
                                Log.i("leehj", "비밀번호 확인(회원탈퇴) withDrawal_data.get(\"u_pw\") : " + withDrawal_data.get("u_pw"));
                                ArrayList<JSONObject> withdrawal_request_data = new ArrayList<>();
                                withdrawal_request_data.add(0, withDrawal_data);
                                Call<Integer> checkPw = RetrofitClient.getApiService().withdrawal(withdrawal_request_data, token);
                                checkPw.enqueue(new Callback<Integer>() {
                                    @Override
                                    public void onResponse(Call<Integer> call, Response<Integer> response) {
                                        CODE_checkPW = response.body();
                                        Log.e("leehj", "회원탈퇴 비밀번호 확인(1이면 일치): " + CODE_checkPW);
                                        if (CODE_checkPW == 1) { //회원탈퇴 성공
                                            dialog_checkPW.dismiss();
                                            finish();
                                            FUNC.activity_intent(MyPageActivity.this, MainActivity.class);
                                            //로그아웃
                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                            editor.clear();
                                            editor.commit();
                                        } else {
                                            dialog_checkPW.dismiss();
                                            Toast.makeText(MyPageActivity.this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Integer> call, Throwable t) {
                                        Log.i("leehj", "마이페이지 비밀번호 확인 통신 실패: " + t);
                                    }
                                });
                            }
                        });
                        dialog_checkPW.show();
                    }
                });
                dialog.show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == 1) {//수정 성공
                intent = getIntent();
                finish();
                startActivity(intent);
            }
        }
    }
}

//    HowAboutThere FUNC = new HowAboutThere();
//
//    EditText myNick;
//    EditText myId;
//    EditText myBirth;
//    EditText myGender;
//    UserDTO user;
//    Intent intent;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.my_page);
//
//        FUNC.sideBar(MyPageActivity.this);
//
//        SharedPreferences appData = getSharedPreferences("UserInfo", Activity.MODE_PRIVATE);
//        Log.i("leehj", "appdata: "+appData.getString("u_id", null));


//        myNick = findViewById(R.id.Et_nick);
//        myId = findViewById(R.id.Et_id);
//        myBirth = findViewById(R.id.Et_birth);
//        myGender = findViewById(R.id.Et_gender);

//~~~~~~~~~~ 마이페이지 정보 가지고 오기 START ~~~~~~~~~~~~~~~~~~~~~~
//        JSONObject row = new JSONObject();
//        row.put("u_id", appData.getString("u_id", "unknown"));
//        ArrayList<JSONObject> ajo= new ArrayList<>();
//        ajo.add(row);
//        Log.i("leehj","Test :: : :"+ ajo.get(0).get("u_id"));
//        Call<UserDTO> mydata = RetrofitClient.getApiService().myDataUp(ajo);
//        mydata.enqueue(new Callback<UserDTO>() {
//            @Override
//            public void onResponse(Call<UserDTO> call, Response<UserDTO> response) {
//                user = response.body();
//                Log.e("leehj", "update response(바뀐 비밀번호): "+user.getU_nick());
//                myNick.setText(user.getU_nick());
//                myId.setText(user.getU_id());
//                myBirth.setText(user.getBirth());
//                if(user.getGender()==1){
//                    myGender.setText("남자");
//                }else if(user.getGender()==0){
//                    myGender.setText("여자");
//                }
//            }
//            @Override
//            public void onFailure(Call<UserDTO> call, Throwable t) {
//                Log.e("leehj","post response 실패: "+t);
//            }
//        });
////        ``````````````` 마이페이지 정보 가지고 오기 END ~~~~~~~~~~~~~~~~~~~~~~~~~~~
//
////          ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~  회원 탈퇴 BUTTON ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
//        findViewById(R.id.withdrawal).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //AlertDialog builder2 에서 비밀번호를 입력받을 EditTex
//                EditText et = new EditText(MyPageActivity.this);
//                //탈퇴사유를 받을 builder2
//                AlertDialog.Builder builder = new AlertDialog.Builder(MyPageActivity.this);
//                builder.setTitle("탈퇴 사유를 알려주세요");
//                //라디오 박스 추가
//                String[] whyWithdrawal = new String[] {"이용기회 상실", "이용방법이 어려움","서비스 불만족", "기타"};
//                final String[] choiceRadio = new String[1];
//                builder.setSingleChoiceItems(whyWithdrawal, 0, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        // 고른 Radio 값을 choiceRadio[0]에 넣어줌 //
//                        choiceRadio[0] = whyWithdrawal[i];
//                    }
//                });
//                //라디오박스 추가 END
//                //bulder1 Positive BUtton
//                builder.setPositiveButton("회원탈퇴", new DialogInterface.OnClickListener(){
//                    @Override
//                    public void onClick(DialogInterface dialog, int id)
//                    {
//                        String pwCheck;
//                        Toast.makeText(getApplicationContext(), choiceRadio[0], Toast.LENGTH_SHORT).show();
//                        //회원탈퇴 확인 비밀번호를 입력받을 builder2
//                        AlertDialog.Builder builder2 = new AlertDialog.Builder(MyPageActivity.this);
//                        builder2.setTitle("회원탈퇴 확인");
//                        builder2.setMessage("비밀번호를 눌러주세요");
//                        builder2.setView(et);
//                        //builder2 positiveButton
//                        builder2.setPositiveButton("확인", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                String pwCheck = et.getText().toString();
//                                //~~~~~~~~~~~~~~~~~~~~ 회원 탈퇴 요구 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
//                                JSONObject row = new JSONObject();
//                                row.put("u_id", appData.getString("u_id", "pzzz"));
//                                row.put("u_pw", pwCheck);
//                                row.put("reason", choiceRadio[0]);
//                                Log.i("leehj", row.get("reason")+"");
//                                ArrayList<JSONObject> ajo= new ArrayList<>();
//                                ajo.add(row);
////                                // 회원탈퇴 RetrofitAPI인 withdrawal 에 userVo를 넣고 보내기
//                                Call<Integer> all = RetrofitClient.getApiService().withdrawal(ajo);
//                                all.enqueue(new Callback<Integer>() {
//                                    @Override
//                                    public void onResponse(Call<Integer> call, Response<Integer> response) {
//                                        int allres = response.body();
//                                        Log.i("subin", "all server 연결 성공" + allres);
//                                        if (allres == 1) {
//                                            //회원탈퇴 완료
//
//                                            //SharedPreferences 지우기
//                                            SharedPreferences.Editor editor = appData.edit();
//                                            editor.clear();
//                                            editor.commit();
//                                            //메인페이지로 이동
//                                            Intent intent=new Intent(MyPageActivity.this,MainActivity.class);
//                                            startActivity(intent);
//                                        } else {
//                                            Toast.makeText(MyPageActivity.this,"비밀번호가 틀립니다.",Toast.LENGTH_SHORT).show();
//                                        }
//                                    }
//
//                                    @Override
//                                    public void onFailure(Call<Integer> call, Throwable t) {
//
//                                        Log.i("subin", "연결실패" + t.getMessage());
//                                    }
//                                });
//                                //~~~~~~~~~~~~~~~~~~~~~~~ 회원탈퇴 요구 END ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
//                            }
//                        });
//                        //builder2 positiveButton END
//                        builder2.show();
//                    }
//                });
//                //~~~~~~~~~buildr1 Positive BUtton END ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
//                //~~~~~~~~~builder1 Negative Button ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
//                builder.setNegativeButton("취소", new DialogInterface.OnClickListener(){
//                    @Override
//                    public void onClick(DialogInterface dialog, int id)
//                    {
//                        Toast.makeText(getApplicationContext(), "Cancel Click", Toast.LENGTH_SHORT).show();
//                    }
//                });
//                //~~~~~~~~ builder1 Negative Button END ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
//                builder.show();
//            }
//        });
////~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ 회원 탈퇴 BUTTON END ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
//
//
//        //~~~~~~~~~~~~~~~~~~~~~ 정보수정 page 누를시 Activity 이동 ~~~~~~~~~~~~~~~~~~~~~~~~/
//        findViewById(R.id.editInfo).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                intent = new Intent(MyPageActivity.this, UpdateInfo.class);
//                intent.putExtra("myInfo", user);
//                startActivity(intent);
//            }
//        });
//        //~~~~~~~~~~~~~~~~~~~~~ 정보수정 page 누를시 Activity 이동  END ~~~~~~~~~~~~~~~~~~~~~~~~/
//    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode == -1 ){
//            if(resultCode == 1){
//                finish();
//                startActivity(intent);
//            }
//        }
//    }
//}