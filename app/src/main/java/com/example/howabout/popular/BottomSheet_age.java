package com.example.howabout.popular;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
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

public class BottomSheet_age extends BottomSheetDialogFragment {
    Context context;

    public BottomSheet_age(Context context) {
        this.context = context;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.bottonsheet_age, container, false);

        RadioGroup radioGroup_age=view.findViewById(R.id.radiogroup_age);
        radioGroup_age.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.age_10:
                        Toast.makeText(context,"10대",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.age_20:
                        Toast.makeText(context,"20대",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.age_30:
                        Toast.makeText(context,"30대",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.age_40:
                        Toast.makeText(context,"40대",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.age_50:
                        Toast.makeText(context,"50대 이상",Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

        return view;
    }
}
