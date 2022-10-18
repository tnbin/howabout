package com.example.howabout;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.example.howabout.API.RetrofitClient;
import com.example.howabout.MyPageActivity;

import com.example.howabout.Vo.UserVo;
import com.example.howabout.function.UserFunction;


import org.json.JSONException;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateInfo extends AppCompatActivity {

    EditText nick, id, pw, re_pw, birth;
    RadioGroup R_gender;
    RadioButton R_button;
    Button check, back;
    int gender;
    int CODE_check_pw;
    UserVo myInfo = null;
    UserFunction userFunc = new UserFunction();
    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_info);
        Log.e("choi", "TESTTTESTTESTTESTTESTTESTTESTTESTT");
        intent = getIntent();
        myInfo = (UserVo) intent.getSerializableExtra("myInfo");

        //닉네임
        nick = (EditText)findViewById(R.id.update_etNick);
        nick.setText(myInfo.getU_nick());

        //아이디
        id = (EditText)findViewById(R.id.update_etId);
        id.setText(myInfo.getU_id());

        //비밀번호
        pw = (EditText)findViewById(R.id.update_etPW);
        pw.setText(myInfo.getU_pw());

        //비밀번호
        re_pw = (EditText)findViewById(R.id.update_etRePW);
        re_pw.setText(myInfo.getU_pw());

        int id;
        if(myInfo.getGender() ==0) id=R.id.btn_woman;
        else id=R.id.btn_man;

        //성별 선택 라디오 버튼 클릭
        R_gender = (RadioGroup) findViewById(R.id.radioGender);
        if(myInfo.getGender()==1){
            R_gender.check(R.id.radio_man);
        }else if(myInfo.getGender()==0){
            R_gender.check(R.id.radio_woman);
        }
        //Radio클릭했을때 리스너
        R_gender.setOnCheckedChangeListener(check_gender);

        //생년월일 입력. birth EditText 클릭
        birth = (EditText) findViewById(R.id.update_etBirth);
        birth.setText(myInfo.getBirth());
        birth.setFocusable(true);
        birth.setOnClickListener(click_birth);

        //수정하기 버튼 클릭
        check = (Button) findViewById(R.id.update_btnCheck);
        check.setOnClickListener(click_check);

        //돌아가기 버튼 클릭
        back = (Button)findViewById(R.id.update_btnBack);
        back.setOnClickListener(click_back);
    }

    //gender radio button check event
    RadioGroup.OnCheckedChangeListener check_gender = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int i) {
            String result;
            if(i == R.id.radio_woman){
                gender = 0;
            }else{
                gender = 1;
            }
            Log.i("leehj", "gender: "+gender);
        }
    };

    //birth EditText click event
    View.OnClickListener click_birth = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
//            Calendar c = new Calendar.getInstance();
            Calendar c = Calendar.getInstance();

            DatePickerDialog datePickerDialog = new DatePickerDialog(UpdateInfo.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    try{
//                        Date d = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(
//                                year+"-"+(month+1)+"-"+day);
                        String m = "-"+(month+1);
                        String d = "-"+day;
                        if(month+1 <10){
                            m="-0"+(month+1);
                        }
                        if(day <10){
                            d="-0"+day;
                        }
                        String date = year +m+d;
                        Log.i("leehj", date);
                        birth.setText(date);

                    } catch (Exception e){
                        Log.i("leehj", "date Picker dialog error"+e);
                    }
                }
            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.getDatePicker().setCalendarViewShown(false);
            datePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            datePickerDialog.show();
        }
    };

    //check button click event
    View.OnClickListener click_check = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //비밀번호, 재확인 비밀번호 일치 확인
            pw = (EditText)findViewById(R.id.update_etPW);
            re_pw = (EditText)findViewById(R.id.update_etRePW);
            String pw_str = pw.getText().toString();
            String repw_str = re_pw.getText().toString();
            CODE_check_pw = userFunc.check_pw(pw_str, repw_str);
            Log.i("leehj", "비밀번호 변경 일치(:1), 불일치(:0): "+CODE_check_pw);

            //EditText에서 값 가져오기
            String nick_str = nick.getText().toString();
            String id_str = id.getText().toString();
            String birth_str = birth.getText().toString();

            if(CODE_check_pw == 1){ //재확인 비밀번호 일치
                UserVo update_user = new UserVo(nick_str, id_str, pw_str, birth_str, gender);
                Log.i("leehj", update_user.toString());

                //비밀번호 입력창
                AlertDialog.Builder dialog = new AlertDialog.Builder(UpdateInfo.this);
                dialog.setTitle("기존 비밀번호를 입력해주세요.");
                EditText input_pw = new EditText(UpdateInfo.this);
                TextView tv_pw = new TextView(UpdateInfo.this);
                dialog.setView(input_pw);
                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String wd_pw = input_pw.getText().toString();
                        Log.i("leehj", "dialog ok click: "+ wd_pw);

                        //id와 wd_pw를 서버로 보내서 비밀번호 확인 요청
                        String u_id = update_user.getU_id();
                        Log.e("leehj", "u_id 값 출력: "+u_id);

                        //JsonObject에 사용자 id와 사용자가 입력한 비밀번호 저장
                        JSONObject body = new JSONObject();
                        body.put("u_id", myInfo.getU_id());
                        body.put("u_pw", wd_pw);

                        //위에서 생성한 json object를 list에 저장.
                        List<JSONObject> list = new ArrayList<>();
                        list.add(body);
                        Log.e("leehj", String.valueOf(list));

                        //안드로이드 -> 서버로 정보수정 요청
                        Call<Integer> func = RetrofitClient.getApiService().checkPW(list);
                        func.enqueue(new Callback<Integer>() {
                            @Override
                            public void onResponse(Call<Integer> call, Response<Integer> response) {
                                int result = response.body();
                                Log.e("leehj", "응답값: "+result);
                                if(result == 1){
                                    Log.i("leehj", "user: "+update_user.toString());
                                    Call<UserVo> func = RetrofitClient.getApiService().updateUser(update_user);
                                    func.enqueue(new Callback<UserVo>() {
                                        @Override
                                        public void onResponse(Call<UserVo> call, Response<UserVo> response) {
                                            UserVo user = response.body();
                                            Log.e("leehj", "update response(바뀐 비밀번호): "+user.getU_pw());
                                            finish();
                                            intent = new Intent(UpdateInfo.this, MyPageActivity.class);
                                            startActivityForResult(intent,1);

                                            //마이페이지 화면으로 돌아가는 intent 처리. 마이페이지에 응답값 보내주기

                                        }

                                        @Override
                                        public void onFailure(Call<UserVo> call, Throwable t) {
                                            Log.e("leehj","post response 실패: "+t);
                                        }
                                    });
                                } else{
                                    Toast.makeText(UpdateInfo.this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Integer> call, Throwable t) {
                                Log.e("leehj", "post 에러: "+t);

                            }
                        });
                    }
                });

                dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.i("leehj", "dialog cancel click");
                    }
                });

                dialog.show();

            }else{ //재확인 비밀번호 불일치
                Toast.makeText(UpdateInfo.this, "재확인 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    };

    //back button click event
    View.OnClickListener click_back = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //intent finish() 처리
            finish();
        }
    };
}