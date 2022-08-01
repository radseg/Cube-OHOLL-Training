package com.radseg.oh_oll;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import static android.text.InputType.TYPE_NULL;

public class MainActivity2 extends AppCompatActivity {
    private ImageView iv_OHOLL;
    private TextView solve;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        iv_OHOLL = findViewById(R.id.iv_oll);
        iv_OHOLL.setImageResource(R.drawable.oll_1);
        solve = findViewById(R.id.tv_solve);
        solve.setOnClickListener(v -> {
            solve.setInputType(TYPE_NULL);
            solve.setBackgroundColor(getResources().getColor(R.color.teal_200));
        });


    }
}