package com.example.howabout;


import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


import com.example.howabout.API.RetrofitClient;
import com.example.howabout.DTO.UserDTO;
import com.example.howabout.functions.User;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateInfo extends AppCompatActivity {

    EditText nick, id, email, pw, re_pw, birth;
    RadioGroup R_gender;
    RadioButton R_button;
    Button check, back;
    int gender;
    int CODE_check_pw, CODE_SUCCESS=1, CODE_BACK=2;
    UserDTO myInfo = null;
    User userFunc = new User();
    Intent intent;
    SharedPreferences sharedPreferences; //공유 프레퍼런스

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_info);

        intent = getIntent();
        UserDTO myInfo = (UserDTO) intent.getSerializableExtra("user_info");

        //닉네임
        nick = (EditText)findViewById(R.id.update_etNick);
        nick.setText(myInfo.getU_nick());

        //아이디
        id = (EditText)findViewById(R.id.update_etId);
        id.setText(myInfo.getU_id());

        //이메일
        email = (EditText)findViewById(R.id.update_etEmail);
        email.setText(myInfo.getU_email());

        //비밀번호란 null로 초기화
        myInfo.setU_pw(null);

        //비밀번호
        pw = (EditText)findViewById(R.id.update_etPW);
        pw.setText("");

        //비밀번호
        re_pw = (EditText)findViewById(R.id.update_etRePW);
        re_pw.setText("");

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
            sharedPreferences = getSharedPreferences("USER", Activity.MODE_PRIVATE);
            String token = "Bearer "+sharedPreferences.getString("token", null); //토큰

            pw = (EditText)findViewById(R.id.update_etPW);
            re_pw = (EditText)findViewById(R.id.update_etRePW);
            String pw_str = pw.getText().toString();
            String repw_str = re_pw.getText().toString();
            CODE_check_pw = userFunc.check_pw(pw_str, repw_str);
            Log.i("leehj", "비밀번호 변경 일치(:1), 불일치(:0): "+CODE_check_pw);

            //EditText에서 값 가져오기
            String nick_str = nick.getText().toString();
            String email_str = email.getText().toString();
            String id_str = id.getText().toString();
            String birth_str = birth.getText().toString();

            if(CODE_check_pw == 1){ //재확인 비밀번호 일치
                //이메일 입력란 추가.
                if(pw_str.equals("")){
                    pw_str=null;
                }
                UserDTO update_user = new UserDTO(nick_str, id_str, pw_str, birth_str, gender, email_str);
                Log.i("leehj", update_user.toString());

                Call<Map<String, String>> updateInfo = RetrofitClient.getApiService().updateUser(update_user, token);
                updateInfo.enqueue(new Callback<Map<String, String>>() {
                    @Override
                    public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                        Log.e("leehj", "정보수정 데이터 변경 성공: "+response.body());
                        Toast.makeText(UpdateInfo.this, "정보가 변경되었습니다.", Toast.LENGTH_SHORT).show();
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("token", response.body().get("jwt"));
                        editor.commit();
                        Log.e("leehj", "정보수정 성공 시 반환 토큰 출력: "+sharedPreferences.getString("token", null));
                        setResult(CODE_SUCCESS);
                        finish();
                    }

                    @Override
                    public void onFailure(Call<Map<String, String>> call, Throwable t) {

                    }
                });

            }else{ //재확인 비밀번호 불일치
                Toast.makeText(UpdateInfo.this, "재확인 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    };

    //back button click event
    View.OnClickListener click_back = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            setResult(CODE_BACK);
            finish();
        }
    };
}