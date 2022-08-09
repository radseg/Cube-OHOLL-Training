package com.radseg.oh_oll;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private final int[] group={R.id.chk_tv1,R.id.chk_tv2,R.id.chk_tv3,R.id.chk_tv4,R.id.chk_tv5,R.id.chk_tv6,R.id.chk_tv7,R.id.chk_tv8};
    private final ArrayList<String> chk_select = new ArrayList<>();
    private Set<String> SP_chkSelect = new HashSet<>();
    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn_ok = findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(btnOk_OnClick);
        for (int id:group) {(findViewById(id)).setOnClickListener(chkGroup_Onclick);}




        SharedPreferences chkSelectData = getSharedPreferences("data",MODE_PRIVATE);
        SP_chkSelect = chkSelectData.getStringSet("SP_chkSelect",SP_chkSelect);
        if (SP_chkSelect.size() > 0){
            for (String id:SP_chkSelect) {
                //setChecked(true)觸發不了View.OnClickListener chkGroup_Onclick方法要自己加
                ((AppCompatCheckBox)findViewById(Integer.parseInt(id))).setChecked(true);
                (findViewById(Integer.parseInt(id))).setBackground(getResources().getDrawable(R.drawable.button_shape_on));
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected void onPause() {
        super.onPause();
        putSpAndALData();
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private final View.OnClickListener chkGroup_Onclick = v -> {
        if (((AppCompatCheckBox)findViewById(v.getId())).isChecked()){
            (findViewById(v.getId())).setBackground(getResources().getDrawable(R.drawable.button_shape_on));
        }else {
            (findViewById(v.getId())).setBackground(getResources().getDrawable(R.drawable.button_shape_off));
        }

    };

    private final View.OnClickListener btnOk_OnClick = v -> {
        putSpAndALData();

        if (chk_select.size()==0){
            Toast.makeText(this,"請點選要練習的OLL群組",Toast.LENGTH_LONG).show();
        }else {
            Intent intent = new Intent(this,MainActivity2.class);
            intent.putExtra("chk_select", chk_select);
            startActivity(intent);
        }
    };

    private void chk_value(int id){
        if (id==R.id.chk_tv1){
            chk_select.add("All Edges Oriented Correctly");
        }else if (id==R.id.chk_tv2){
            chk_select.add("T , Square , C , W Shapes");
        }else if (id==R.id.chk_tv3){
            chk_select.add("Oriented Corners and P Shapes");
        }else if (id==R.id.chk_tv4){
            chk_select.add("I and fish Shapes");
        }else if (id==R.id.chk_tv5){
            chk_select.add("Knight Move and Awkward Shapes");
        }else if (id==R.id.chk_tv6){
            chk_select.add("L Shapes");
        }else if (id==R.id.chk_tv7){
            chk_select.add("Lightning Bolt Shapes");
        }else if (id==R.id.chk_tv8){
            chk_select.add("No Edges Oriented");
        }
    }

    private void putSpAndALData(){
        SharedPreferences chkSelectData = getSharedPreferences("data",MODE_PRIVATE);
        SP_chkSelect = new HashSet<>();
        chk_select.clear();
        for (int id:group) {
            if (((AppCompatCheckBox) findViewById(id)).isChecked()) {
                chk_value(id);
                SP_chkSelect.add(String.valueOf(id));
            }
        }
        chkSelectData.edit()
                .putStringSet("SP_chkSelect",SP_chkSelect)
                .apply();
    }



}