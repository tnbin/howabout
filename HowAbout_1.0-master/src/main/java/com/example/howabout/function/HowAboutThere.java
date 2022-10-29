package com.example.howabout.function;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.drawerlayout.widget.DrawerLayout;

import com.example.howabout.FindActivity;
import com.example.howabout.MainActivity;
import com.example.howabout.MyCourseActivity;
import com.example.howabout.MyPageActivity;
import com.example.howabout.R;

public class HowAboutThere {

    Intent intent;
    SharedPreferences sharedPreferences;

    //intent 함수, startActivity
    public void activity_intent(Activity firstActivity, Class secondActivity) {
        intent = new Intent(firstActivity, secondActivity);
        firstActivity.startActivity(intent);
    }

    public void sideBar(Activity activity) {
        //DrawerLayout Menu
        ImageButton btn_open = activity.findViewById(R.id.btn_open); //sidebar open button
        Button btn_courcebar = activity.findViewById(R.id.btn_courcebar); //find course tab
        Button btn_mypagebar = activity.findViewById(R.id.btn_mypagebar); //myPage tab
        Button btn_mycourcebar = activity.findViewById(R.id.btn_mycourcebar); //myCourse tab
        Button logout = activity.findViewById(R.id.logout); //logout tab

        sharedPreferences = activity.getSharedPreferences("USER", Activity.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);

        btn_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DrawerLayout drawerLayout = activity.findViewById(R.id.drawer_layout);
                View drawerView = activity.findViewById(R.id.drawer);
                drawerLayout.openDrawer(drawerView);

                Log.e("leehj", "activity : " + activity.getLocalClassName());

                //drawer layout menu buttons
                Button btn_homebar = activity.findViewById(R.id.btn_homebar);
                btn_homebar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (activity.getLocalClassName().equals("MainActivity")) {
                            drawerLayout.closeDrawers();
                        } else {
                            activity.finish();
                            activity_intent(activity, MainActivity.class);
                        }
                    }
                });
                btn_courcebar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (activity.getLocalClassName().equals("FindActivity")) {
                            drawerLayout.closeDrawers();
                        } else {
//                            activity.finish();
                            activity_intent(activity, FindActivity.class);
                        }
                    }
                });
                btn_mypagebar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (token == null) {
                            Toast.makeText(activity, "로그인 후 이용가능한 서비스 입니다.", Toast.LENGTH_SHORT).show();
                        } else {
                            if (activity.getLocalClassName().equals("MyPageActivity")) {
                                drawerLayout.closeDrawers();
                            } else {
//                                activity.finish();
                                activity_intent(activity, MyPageActivity.class);
                            }
                        }
                    }
                });
                btn_mycourcebar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (token == null) {
                            Toast.makeText(activity, "로그인 후 이용가능한 서비스 입니다.", Toast.LENGTH_SHORT).show();
                        } else {
                            if (activity.getLocalClassName().equals("MyCourseActivity")) {
                                drawerLayout.closeDrawers();
                            } else {
//                                activity.finish();
                                activity_intent(activity, MyCourseActivity.class);
                            }
                        }
                    }
                });


//                    logout.setVisibility(View.VISIBLE);
                logout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (token == null) {
                            Toast.makeText(activity, "로그인 후 이용가능한 서비스 입니다.", Toast.LENGTH_SHORT).show();
                        } else {
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.clear();
                            editor.commit();
                            Log.i("leehj", sharedPreferences.getString("token", null));

                            Toast.makeText(activity, "로그아웃 되었습니다. ", Toast.LENGTH_SHORT).show();

                            if (activity.getLocalClassName().equals("TEST")) {
                                drawerLayout.closeDrawers();
                            } else {
                                activity.finish();
                                activity_intent(activity, MainActivity.class);
                            }
                        }
                    }
                });
            }
        });
    }
}
