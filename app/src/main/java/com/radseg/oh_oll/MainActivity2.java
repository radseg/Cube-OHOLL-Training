package com.radseg.oh_oll;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import static android.text.InputType.TYPE_NULL;

public class MainActivity2 extends AppCompatActivity {
    private ImageView iv_OHOLL;
    private TextView tv_solve;
    private ArrayList<String> chk_select1 = new ArrayList<String>();
    private String oll[],group[],scramble[],solve[];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        iv_OHOLL = findViewById(R.id.iv_oll);
        iv_OHOLL.setImageResource(R.drawable.oll_1);
        tv_solve = findViewById(R.id.tv_solve);
        tv_solve.setOnClickListener(v -> {
            tv_solve.setInputType(TYPE_NULL);
            tv_solve.setBackgroundColor(getResources().getColor(R.color.teal_200));
        });


        Intent intent = getIntent();
        chk_select1 = intent.getStringArrayListExtra("chk_select");
    }

}