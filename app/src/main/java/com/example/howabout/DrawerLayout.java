//package com.example.howabout;
//
//import android.annotation.SuppressLint;
//import android.app.Activity;
//import android.content.Intent;
//import android.view.MotionEvent;
//import android.view.View;
//import android.widget.Button;
//
//public class DrawerLayout {
//    androidx.drawerlayout.widget.DrawerLayout drawerLayout;
//    View drawerView;
//    Activity activity;
//
//    public DrawerLayout(androidx.drawerlayout.widget.DrawerLayout drawerLayout, View drawerView,Activity activity) {
//        this.drawerLayout = drawerLayout;
//        this.drawerView = drawerView;
//        this.activity=activity;
//    }
//
//    @SuppressLint("NotConstructor")
//    public void DrawerLayout(Activity activity) {
//
//        drawerLayout = activity.findViewById(R.id.drawer_layout);
//        drawerView = activity.findViewById(R.id.drawer);
//
//        drawerLayout.setDrawerListener(listener);
//        drawerView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//
//                return true;
//            }
//        });
//
//        Button btn_homebar = activity.findViewById(R.id.btn_homebar);
//        btn_homebar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                drawerLayout.closeDrawers();
//                Intent intenth = new Intent(activity.this, MainActivity.class);
//                activity.startActivity(intenth);
//            }
//        });
//        Button btn_courcebar = activity.findViewById(R.id.btn_courcebar);
//        btn_courcebar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                drawerLayout.closeDrawers();
//            }
//        });
//
//        Button btn_mypagebar = activity.findViewById(R.id.btn_mypagebar);
//        btn_mypagebar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                drawerLayout.closeDrawers();
//                Intent intentmp = new Intent(activity.this, MyPageActivity.class);
//                activity.startActivity(intentmp);
//            }
//        });
//        Button btn_mycourcebar = activity.findViewById(R.id.btn_mycourcebar);
//        btn_mycourcebar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                drawerLayout.closeDrawers();
//                Intent intentmc = new Intent(activity.this, MyCourseActivity.class);
//                activity.startActivity(intentmc);
//            }
//        });
//    }
//
//}
