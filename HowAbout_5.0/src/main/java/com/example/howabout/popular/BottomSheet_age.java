package com.example.howabout.popular;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.howabout.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheet_age extends BottomSheetDialogFragment {
    Context context;
    String age = "전체";
    SharedPreferences preferences;

    public BottomSheet_age(Context context) {
        this.context = context;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.popular_bottomsheet_age, container, false);
        preferences = context.getSharedPreferences("popular", MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        RadioGroup radioGroup_age = view.findViewById(R.id.radiogroup_age);
        radioGroup_age.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case -1:
                        Toast.makeText(context, "취소", Toast.LENGTH_SHORT).show();
                        return;
                    case R.id.age_10:
                        Toast.makeText(context, "10대", Toast.LENGTH_SHORT).show();
                        age = "10대";
                        break;
                    case R.id.age_20:
                        Toast.makeText(context, "20대", Toast.LENGTH_SHORT).show();
                        age = "20대";
                        break;
                    case R.id.age_30:
                        Toast.makeText(context, "30대", Toast.LENGTH_SHORT).show();
                        age = "30대";
                        break;
                    case R.id.age_40:
                        Toast.makeText(context, "40대", Toast.LENGTH_SHORT).show();
                        age = "40대";
                        break;
                    case R.id.age_50:
                        Toast.makeText(context, "50대 이상", Toast.LENGTH_SHORT).show();
                        age = "50대 이상";
                        break;
                }
            }
        });
        Button select_age = view.findViewById(R.id.select_age);
        Button btn_age = getActivity().findViewById(R.id.btn_age);

        select_age.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("subin", "age값 : " + age);
                btn_age.setText(age);

                edit.putString("p_age", age);
                edit.apply();
                edit.commit();
                Log.i("subin", preferences.getString("p_age", "x"));
                dismissAllowingStateLoss();
            }
        });
        Button replay_age = view.findViewById(R.id.replay_age);
        replay_age.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_age.setText("나이");
                edit.putString("p_age", "전체");
                edit.apply();
                edit.commit();
                Log.i("subin", "hahhahah" + preferences.getString("p_age", "x"));
                radioGroup_age.clearCheck();
            }
        });
        return view;
    }

}
