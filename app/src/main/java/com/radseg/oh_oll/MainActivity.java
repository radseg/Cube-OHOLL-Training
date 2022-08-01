package com.radseg.oh_oll;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;

public class MainActivity extends AppCompatActivity {
    private Button btn_ok;
    private AppCompatCheckBox group_1,group_2,group_3,group_4,group_5,group_6,group_7,group_8;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        group_1 = (AppCompatCheckBox)findViewById(R.id.chk_tv1);
        group_2 = findViewById(R.id.chk_tv2);
        group_3 = findViewById(R.id.chk_tv3);
        group_4 = findViewById(R.id.chk_tv4);
        group_5 = findViewById(R.id.chk_tv5);
        group_6 = findViewById(R.id.chk_tv6);
        group_7 = findViewById(R.id.chk_tv7);
        group_8 = findViewById(R.id.chk_tv8);
        group_1.setOnClickListener(mgroup);
        group_2.setOnClickListener(mgroup);
        group_3.setOnClickListener(mgroup);
        group_4.setOnClickListener(mgroup);
        group_5.setOnClickListener(mgroup);
        group_6.setOnClickListener(mgroup);
        group_7.setOnClickListener(mgroup);
        group_8.setOnClickListener(mgroup);

        btn_ok = findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this,MainActivity2.class);
            startActivity(intent);
        });



    }

    private final View.OnClickListener mgroup = v -> {

        if (((AppCompatCheckBox)findViewById(v.getId())).isChecked()){
            ((AppCompatCheckBox)findViewById(v.getId())).setBackgroundColor(getResources().getColor(R.color.teal_200));
        }else {
            ((AppCompatCheckBox)findViewById(v.getId())).setBackgroundColor(getResources().getColor(R.color.white));
        }

    };


}