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
        //~~~~~~~~~~ ??????????????? ?????? ????????? ?????? START ~~~~~~~~~~~~~~~~~~~~~~
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
                Log.e("leehj", "update response(?????? ????????????): "+user.getU_nick());
                myNick.setText(user.getU_nick());
                myId.setText(user.getU_id());
                myBirth.setText(user.getBirth());
                if(user.getGender()==1){
                    myGender.setText("??????");
                }else if(user.getGender()==0){
                    myGender.setText("??????");
                }
            }
            @Override
            public void onFailure(Call<UserVo> call, Throwable t) {
                Log.e("leehj","post response ??????: "+t);
            }
        });
        //``````````````` ??????????????? ?????? ????????? ?????? END ~~~~~~~~~~~~~~~~~~~~~~~~~~~

        //  ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~  ?????? ?????? BUTTON ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
        findViewById(R.id.withdrawal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //AlertDialog builder2 ?????? ??????????????? ???????????? EditTex
                EditText et = new EditText(MyPageActivity.this);
                //??????????????? ?????? builder2
                AlertDialog.Builder builder = new AlertDialog.Builder(MyPageActivity.this);
                builder.setTitle("?????? ????????? ???????????????");
                //????????? ?????? ??????
                String[] whyWithdrawal = new String[] {"???????????? ??????", "??????????????? ?????????","????????? ?????????", "??????"};
                final String[] choiceRadio = new String[1];
                builder.setSingleChoiceItems(whyWithdrawal, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // ?????? Radio ?????? choiceRadio[0]??? ????????? //
                        choiceRadio[0] = whyWithdrawal[i];
                    }
                });
                //??????????????? ?????? END
                //bulder1 Positive BUtton
                builder.setPositiveButton("????????????", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int id)
                    {
                        String pwCheck;
                        Toast.makeText(getApplicationContext(), choiceRadio[0], Toast.LENGTH_SHORT).show();
                        //???????????? ?????? ??????????????? ???????????? builder2
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(MyPageActivity.this);
                        builder2.setTitle("???????????? ??????");
                        builder2.setMessage("??????????????? ???????????????");
                        builder2.setView(et);
                        //builder2 positiveButton
                        builder2.setPositiveButton("??????", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String pwCheck = et.getText().toString();
                                //~~~~~~~~~~~~~~~~~~~~ ?????? ?????? ?????? ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
                                JSONObject row = new JSONObject();
                                row.put("u_id", appData.getString("u_id", "pzzz"));
                                row.put("u_pw", pwCheck);
                                row.put("reason", choiceRadio[0]);
                                Log.i("leehj", row.get("reason")+"");
                                ArrayList<JSONObject> ajo= new ArrayList<>();
                                ajo.add(row);
//                                // ???????????? RetrofitAPI??? withdrawal ??? userVo??? ?????? ?????????
                                Call<Integer> all = RetrofitClient.getApiService().withdrawal(ajo);
                                all.enqueue(new Callback<Integer>() {
                                    @Override
                                    public void onResponse(Call<Integer> call, Response<Integer> response) {
                                        int allres = response.body();
                                        Log.i("subin", "all server ?????? ??????" + allres);
                                        if (allres == 1) {
                                            //???????????? ??????

                                            //SharedPreferences ?????????
                                            SharedPreferences.Editor editor = appData.edit();
                                            editor.clear();
                                            editor.commit();
                                            //?????????????????? ??????
                                            Intent intent=new Intent(MyPageActivity.this,MainActivity.class);
                                            startActivity(intent);
                                        } else {
                                            Toast.makeText(MyPageActivity.this,"??????????????? ????????????.",Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Integer> call, Throwable t) {

                                        Log.i("subin", "????????????" + t.getMessage());
                                    }
                                });
                                //~~~~~~~~~~~~~~~~~~~~~~~ ???????????? ?????? END ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
                            }
                        });
                        //builder2 positiveButton END
                        builder2.show();
                    }
                });
                //~~~~~~~~~buildr1 Positive BUtton END ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
                //~~~~~~~~~builder1 Negative Button ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
                builder.setNegativeButton("??????", new DialogInterface.OnClickListener(){
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
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ ?????? ?????? BUTTON END ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//


        //~~~~~~~~~~~~~~~~~~~~~ ???????????? page ????????? Activity ?????? ~~~~~~~~~~~~~~~~~~~~~~~~/
        findViewById(R.id.editInfo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(MyPageActivity.this, UpdateInfo.class);
                intent.putExtra("myInfo", user);
                startActivity(intent);
            }
        });
        //~~~~~~~~~~~~~~~~~~~~~ ???????????? page ????????? Activity ??????  END ~~~~~~~~~~~~~~~~~~~~~~~~/
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