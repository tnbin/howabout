package com.example.howabout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.howabout.API.RetrofitClient;
import com.example.howabout.Vo.UserVo;


import org.json.simple.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyPageActivity extends AppCompatActivity implements Serializable {

    DrawerLayout drawerLayout;
    View drawerView;
    EditText myNick;
    EditText myId;
    EditText myBirth;
    EditText myGender;
    UserVo user;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_page);

        SharedPreferences appData = getSharedPreferences("UserInfo", Activity.MODE_PRIVATE);
        Log.i("leehj", "appdata: "+appData.getString("u_id", null));


        myNick = findViewById(R.id.Et_nick);
        myId = findViewById(R.id.Et_id);
        myBirth = findViewById(R.id.Et_birth);
        myGender = findViewById(R.id.Et_gender);


        drawerLayout = findViewById(R.id.drawer_layout);
        drawerView = findViewById(R.id.drawer);
        ImageButton btn_open = findViewById(R.id.btn_open);
        btn_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(drawerView);
            }
        });

        drawerLayout.setDrawerListener(listener);
        drawerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                return true;
            }
        });

        Button btn_homebar = findViewById(R.id.btn_homebar);
        btn_homebar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawers();
                Intent intenth=new Intent(MyPageActivity.this,MainActivity.class);
                startActivity(intenth);
            }
        });
        Button btn_courcebar=findViewById(R.id.btn_courcebar);
        btn_courcebar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawers();
                Intent intentc=new Intent(MyPageActivity.this,FindActivity.class);
                startActivity(intentc);
            }
        });

        Button btn_mypagebar = findViewById(R.id.btn_mypagebar);
        btn_mypagebar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawers();
            }
        });
        Button btn_mycourcebar = findViewById(R.id.btn_mycourcebar);
        btn_mycourcebar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawers();
                Intent intentmc=new Intent(MyPageActivity.this,MyCourseActivity.class);
                startActivity(intentmc);
            }
        });
        //~~~~~~~~~~ 마이페이지 정보 가지고 오기 START ~~~~~~~~~~~~~~~~~~~~~~
        JSONObject row = new JSONObject();
        row.put("u_id", appData.getString("u_id", "unknown"));
        ArrayList<JSONObject> ajo= new ArrayList<>();
        ajo.add(row);
        Log.i("leehj","Test :: : :"+ ajo.get(0).get("u_id"));
        Call<UserVo> mydata = RetrofitClient.getApiService().myDataUp(ajo);
        mydata.enqueue(new Callback<UserVo>() {
            @Override
            public void onResponse(Call<UserVo> call, Response<UserVo> response) {
                user = response.body();
                Log.e("leehj", "update response(바뀐 비밀번호): "+user.getU_nick());
                myNick.setText(user.getU_nick());
                myId.setText(user.getU_id());
                myBirth.setText(user.getBirth());
                if(user.getGender()==1){
                    myGender.setText("남자");
                }else if(user.getGender()==0){
                    myGender.setText("여자");
                }
            }
            @Override
            public void onFailure(Call<UserVo> call, Throwable t) {
                Log.e("leehj","post response 실패: "+t);
            }
        });
        //``````````````` 마이페이지 정보 가지고 오기 END ~~~~~~~~~~~~~~~~~~~~~~~~~~~

        //  ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~  회원 탈퇴 BUTTON ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
        findViewById(R.id.withdrawal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //AlertDialog builder2 에서 비밀번호를 입력받을 EditTex
                EditText et = new EditText(MyPageActivity.this);
                //탈퇴사유를 받을 builder2
                AlertDialog.Builder builder = new AlertDialog.Builder(MyPageActivity.this);
                builder.setTitle("탈퇴 사유를 알려주세요");
                //라디오 박스 추가
                String[] whyWithdrawal = new String[] {"이용기회 상실", "이용방법이 어려움","서비스 불만족", "기타"};
                final String[] choiceRadio = new String[1];
                builder.setSingleChoiceItems(whyWithdrawal, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // 고른 Radio 값을 choiceRadio[0]에 넣어줌 //
                        choiceRadio[0] = whyWithdrawal[i];
                    }
                });
                //라디오박스 추가 END
                //bulder1 Positive BUtton
                builder.setPositiveButton("회원탈퇴", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int id)
                    {
                        String pwCheck;
                        Toast.makeText(getApplicationContext(), choiceRadio[0], Toast.LENGTH_SHORT).show();
                        //회원탈퇴 확인 비밀번호를 입력받을 builder2
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(MyPageActivity.this);
                        builder2.setTitle("회원탈퇴 확인");
                        builder2.setMessage("비밀번호를 눌러주세요");
                        builder2.setView(et);
                        //builder2 positiveButton
                        builder2.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String pwCheck = et.getText().toString();
                                //~~~~~~~~~~~~~~~~~~~~ 회원 탈퇴 요구 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
                                JSONObject row = new JSONObject();
                                row.put("u_id", appData.getString("u_id", "pzzz"));
                                row.put("u_pw", pwCheck);
                                row.put("reason", choiceRadio[0]);
                                Log.i("leehj", row.get("reason")+"");
                                ArrayList<JSONObject> ajo= new ArrayList<>();
                                ajo.add(row);
//                                // 회원탈퇴 RetrofitAPI인 withdrawal 에 userVo를 넣고 보내기
                                Call<Integer> all = RetrofitClient.getApiService().withdrawal(ajo);
                                all.enqueue(new Callback<Integer>() {
                                    @Override
                                    public void onResponse(Call<Integer> call, Response<Integer> response) {
                                        int allres = response.body();
                                        Log.i("subin", "all server 연결 성공" + allres);
                                        if (allres == 1) {
                                            //회원탈퇴 완료

                                            //SharedPreferences 지우기
                                            SharedPreferences.Editor editor = appData.edit();
                                            editor.clear();
                                            editor.commit();
                                            //메인페이지로 이동
                                            Intent intent=new Intent(MyPageActivity.this,MainActivity.class);
                                            startActivity(intent);
                                        } else {
                                            Toast.makeText(MyPageActivity.this,"비밀번호가 틀립니다.",Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Integer> call, Throwable t) {

                                        Log.i("subin", "연결실패" + t.getMessage());
                                    }
                                });
                                //~~~~~~~~~~~~~~~~~~~~~~~ 회원탈퇴 요구 END ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
                            }
                        });
                        //builder2 positiveButton END
                        builder2.show();
                    }
                });
                //~~~~~~~~~buildr1 Positive BUtton END ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
                //~~~~~~~~~builder1 Negative Button ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
                builder.setNegativeButton("취소", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int id)
                    {
                        Toast.makeText(getApplicationContext(), "Cancel Click", Toast.LENGTH_SHORT).show();
                    }
                });
                //~~~~~~~~ builder1 Negative Button END ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
                builder.show();
            }
        });
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ 회원 탈퇴 BUTTON END ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//


        //~~~~~~~~~~~~~~~~~~~~~ 정보수정 page 누를시 Activity 이동 ~~~~~~~~~~~~~~~~~~~~~~~~/
        findViewById(R.id.editInfo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(MyPageActivity.this, UpdateInfo.class);
                intent.putExtra("myInfo", user);
                startActivity(intent);
            }
        });
        //~~~~~~~~~~~~~~~~~~~~~ 정보수정 page 누를시 Activity 이동  END ~~~~~~~~~~~~~~~~~~~~~~~~/
    }

    DrawerLayout.DrawerListener listener = new DrawerLayout.DrawerListener() {
        @Override
        public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

        }

        @Override
        public void onDrawerOpened(@NonNull View drawerView) {

        }

        @Override
        public void onDrawerClosed(@NonNull View drawerView) {

        }

        @Override
        public void onDrawerStateChanged(int newState) {

        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == -1 ){
            if(resultCode == 1){
                finish();
                startActivity(intent);
            }
        }
    }
}