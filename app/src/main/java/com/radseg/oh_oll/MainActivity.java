package com.radseg.oh_oll;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private Button btn_ok;
    private int[] group={R.id.chk_tv1,R.id.chk_tv2,R.id.chk_tv3,R.id.chk_tv4,R.id.chk_tv5,R.id.chk_tv6,R.id.chk_tv7,R.id.chk_tv8};
    private ArrayList<String> chk_select = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        for (int id:group) {((AppCompatCheckBox)findViewById(id)).setOnClickListener(chkGroup_Onclick);}

        btn_ok = findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(btnOk_OnClick);


        SharedPreferences.Editor editor = getSharedPreferences("data",MODE_PRIVATE).edit();
        if (editor.putInt("chk_select",0)!=null){

        }

    }

    private final View.OnClickListener chkGroup_Onclick = v -> {
        if (((AppCompatCheckBox)findViewById(v.getId())).isChecked()){
            ((AppCompatCheckBox)findViewById(v.getId())).setBackground(getResources().getDrawable(R.drawable.button_shape_on));
        }else {
            ((AppCompatCheckBox)findViewById(v.getId())).setBackground(getResources().getDrawable(R.drawable.button_shape_off));
        }

    };

    private final View.OnClickListener btnOk_OnClick = v -> {

        chk_select.clear();
        for (int id:group) {
            if (((AppCompatCheckBox) findViewById(id)).isChecked())
                chk_value(id);
        }

        if (chk_select.size()==0){
            Toast.makeText(this,"請點選要練習的OLL群組",Toast.LENGTH_LONG).show();
        }else {
            Intent intent = new Intent(this,MainActivity2.class);
            intent.putExtra("chk_select", chk_select);
            startActivity(intent);
        }
    };

    private void chk_value(int id){
        switch (id) {
            case R.id.chk_tv1:
                chk_select.add("All Edges Oriented Correctly");
                break;
            case R.id.chk_tv2:
                chk_select.add("T , Square , C , W Shapes");
                break;
            case R.id.chk_tv3:
                chk_select.add("Oriented Corners and P Shapes");
                break;
            case R.id.chk_tv4:
                chk_select.add("I and fish Shapes");
                break;
            case R.id.chk_tv5:
                chk_select.add("Knight Move and Awkward Shapes");
                break;
            case R.id.chk_tv6:
                chk_select.add("L Shapes");
                break;
            case R.id.chk_tv7:
                chk_select.add("Lightning Bolt Shapes");
                break;
            case R.id.chk_tv8:
                chk_select.add("No Edges Oriented");
                break;
        }

    };



}