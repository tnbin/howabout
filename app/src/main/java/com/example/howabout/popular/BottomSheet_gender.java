package com.example.howabout.popular;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.howabout.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheet_gender extends BottomSheetDialogFragment {
    Context context;

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

        RadioGroup radioGroup_gender=view.findViewById(R.id.radiogroup_gender);
        radioGroup_gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.gender_man:
                        Toast.makeText(context,"man",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.gender_woman:
                        Toast.makeText(context,"woman",Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });


        return view;
    }
}
