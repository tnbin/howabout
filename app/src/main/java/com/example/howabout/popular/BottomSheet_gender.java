package com.example.howabout.popular;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.howabout.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheet_gender extends BottomSheetDialogFragment {
    Context context;
    String gender = "전체";
    SharedPreferences preferences;

    public BottomSheet_gender(Context context) {
        this.context = context;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.bottonsheet_gender, container, false);
        preferences = context.getSharedPreferences("popular", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        RadioGroup radioGroup_gender = view.findViewById(R.id.radiogroup_gender);
        radioGroup_gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.gender_man:
                        Toast.makeText(context, "man", Toast.LENGTH_SHORT).show();
                        gender = "남성";
                        break;
                    case R.id.gender_woman:
                        Toast.makeText(context, "woman", Toast.LENGTH_SHORT).show();
                        gender = "여성";
                        break;
                }
            }
        });
        Button select_gender = view.findViewById(R.id.select_gender);
        Button btn_gender = getActivity().findViewById(R.id.btn_gender);
        select_gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_gender.setText(gender);

                editor.putString("p_gender", gender);
                editor.commit();
                editor.apply();
                dismissAllowingStateLoss();
            }
        });
        Button replay_gender = view.findViewById(R.id.replay_gender);
        replay_gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_gender.setText("성별");
                editor.putString("p_gender", "전체");
                editor.apply();
                editor.commit();
                Log.i("subin", "hahhahah" + preferences.getString("p_gender", "x"));
                radioGroup_gender.clearCheck();
            }
        });

        return view;
    }

}
