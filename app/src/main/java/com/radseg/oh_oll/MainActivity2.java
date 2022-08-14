package com.radseg.oh_oll;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.radseg.oh_oll.DB.OHOLL_DBOpenHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

import static android.content.ContentValues.TAG;

public class MainActivity2 extends AppCompatActivity {
    private ImageView iv_OhOll;
    private TextView tv_ollNum,tv_group,tv_scramble,tv_solve;
    private SeekBar skBar_group;
    private SwitchMaterial swt_scramble;
    private LinearLayout conLayout;
    private final int[]btn = {R.id.btn_groupNext, R.id.btn_groupPrevious, R.id.btn_previous, R.id.btn_next};
    private final int[]picture = {R.drawable.oll_1,R.drawable.oll_2,R.drawable.oll_3,R.drawable.oll_4,R.drawable.oll_5,R.drawable.oll_6,R.drawable.oll_7,R.drawable.oll_8,R.drawable.oll_9,R.drawable.oll_10,R.drawable.oll_11,R.drawable.oll_12,R.drawable.oll_13,R.drawable.oll_14,R.drawable.oll_15,R.drawable.oll_16,R.drawable.oll_17,R.drawable.oll_18,R.drawable.oll_19,R.drawable.oll_20,
            R.drawable.oll_21,R.drawable.oll_22,R.drawable.oll_23,R.drawable.oll_24,R.drawable.oll_25,R.drawable.oll_26,R.drawable.oll_27,R.drawable.oll_28,R.drawable.oll_29,R.drawable.oll_30,R.drawable.oll_31,R.drawable.oll_32,R.drawable.oll_33,R.drawable.oll_34,R.drawable.oll_35,R.drawable.oll_36,R.drawable.oll_37,R.drawable.oll_38,R.drawable.oll_39,R.drawable.oll_40,
            R.drawable.oll_41,R.drawable.oll_42,R.drawable.oll_43,R.drawable.oll_44,R.drawable.oll_45,R.drawable.oll_46,R.drawable.oll_47,R.drawable.oll_48,R.drawable.oll_49,R.drawable.oll_50,R.drawable.oll_51,R.drawable.oll_52,R.drawable.oll_53,R.drawable.oll_54,R.drawable.oll_55,R.drawable.oll_56,R.drawable.oll_57};
    private int num=0;
    private final ArrayList<Integer> group_num = new ArrayList<>();//把選取的群組第一個索引值放入group_num
    private ArrayList<String> chk_select = new ArrayList<>();//接收MainActivity傳來的群組
    private final ArrayList<String> al_DBNum = new ArrayList<>();//搜尋完的到的列表的基本排序1~x
    private final ArrayList<String> al_OllNum = new ArrayList<>();
    private final ArrayList<String> al_group = new ArrayList<>();
    private final ArrayList<String> al_scramble = new ArrayList<>();
    private final ArrayList<String> al_solve = new ArrayList<>();
    private SQLiteDatabase mOhOllDB;
    private static final String DB_FILE = "ohOll.db" , DB_TABLE = "ohOll";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        for (int id:btn) {(findViewById(id)).setOnClickListener(btnClick);}
        iv_OhOll = findViewById(R.id.iv_oll);
        iv_OhOll.setImageResource(R.drawable.oll_1);
        tv_ollNum = findViewById(R.id.tv_ollnum);
        tv_group = findViewById(R.id.tv_group);
        tv_scramble = findViewById(R.id.tv_scramble);
        swt_scramble = findViewById(R.id.swt_scramble);
        swt_scramble.setOnCheckedChangeListener(swtChange);
        skBar_group = findViewById(R.id.skBar_group);
        skBar_group.setOnSeekBarChangeListener(skBarChange);
        tv_solve = findViewById(R.id.tv_solve);
        conLayout = findViewById(R.id.conLayout);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams)iv_OhOll.getLayoutParams();

        //運用切割畫面時檢視母框架的長來控制image的大小
        conLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int Height = conLayout.getMeasuredHeight();
                Log.d(TAG, "Height: " + Height);
                LinearLayout.LayoutParams layoutParams_new = layoutParams;
                if (Height > 0 && Height < 1300){
                    conLayout.getViewTreeObserver().removeOnGlobalLayoutListener( this);
                    layoutParams_new.height = 300;
                    layoutParams_new.width = 300;
                    iv_OhOll.setLayoutParams(layoutParams_new);

                }else{
                    conLayout.getViewTreeObserver().removeOnGlobalLayoutListener( this);
                    layoutParams_new.height = 600;
                    layoutParams_new.width = 600;
                    iv_OhOll.setLayoutParams(layoutParams_new);
                    
                }
            }
        });


        SharedPreferences switchData = getSharedPreferences("data",MODE_PRIVATE);

        if (switchData.getInt("switch_select",-1)==1)
            swt_scramble.setChecked(true);

        tv_solve.setOnClickListener(v -> {
            if (swt_scramble.isChecked()) {
                tv_solve.setInputType(InputType.TYPE_NULL);
                tv_solve.setBackgroundColor(getResources().getColor(R.color.teal_200));
            }
        });

        load();
        dataToObject(num);



    }

    private void load(){
        //把res/raw/ohOll_data.json裡面的內容全部讀入
        InputStream ollData = getResources().openRawResource(R.raw.oholl_data);
        Scanner scanner = new Scanner(ollData);
        StringBuilder builder = new StringBuilder();
        while (scanner.hasNextLine()){
            builder.append(scanner.nextLine());
        }
        parseJsonToDB(builder.toString());


    }


    private void parseJsonToDB(String str){
        OHOLL_DBOpenHelper ohOllDBOpenHelper = new OHOLL_DBOpenHelper(getApplicationContext() , DB_FILE , null , 1);
        mOhOllDB = ohOllDBOpenHelper.getWritableDatabase();

        Cursor c = mOhOllDB.query(true, DB_TABLE, new String[]{"_id","OHoll_num","OHoll_group","scramble","solve"},
                null, null, null, null, null, null);
        if (c == null)
            return;

        try {
            //把res/raw/ohOll_data.json資料解析成陣列並丟入資料庫
            if (c.getCount() == 0) {
                JSONObject root = new JSONObject(str);
                JSONArray ohOllS = root.getJSONArray("OHOLL");
                for (int i = 0; i < str.length(); i++) {
                    JSONObject oholl = ohOllS.getJSONObject(i);
                    ContentValues newRow = new ContentValues();
                    newRow.put("OHoll_num", oholl.getString("OHoll_num"));
                    newRow.put("OHoll_group",oholl.getString("OHoll_group"));
                    newRow.put("scramble", oholl.getString("scramble"));
                    newRow.put("solve", oholl.getString("solve"));
                    mOhOllDB.insert(DB_TABLE, null, newRow);
                }
            }
                Log.d("str.length()", String.valueOf(str.length()));
            } catch(JSONException e){
                e.printStackTrace();
            }
        c.close();
        putSelectData();

        //創建一個陣列放每一個小組的第一個數字
        //indexOf可以找尋ArrayList中的值，如果有重複會回傳第一個找尋到的int索引值(索引值從0開始)
        for (int i = 0; i < chk_select.size(); i++) {
            group_num.add(al_group.indexOf(chk_select.get(i)));//把選取的群組第一個索引值放入group_num
            Log.d(TAG, "chk_select.get(i): " + chk_select.get(i));
            Log.d(TAG, "group_num.get(i): " + group_num.get(i));
        }

    }

    private void putSelectData(){
        //拿到MainActivity傳來的ArrayList
        Intent intent = getIntent();
        chk_select = intent.getStringArrayListExtra("chk_select");
        al_DBNum.clear();
        al_OllNum.clear();
        al_group.clear();
        al_scramble.clear();
        al_solve.clear();
        //把搜尋群組的資料丟入ArrayList
        for (int i = 0; i < chk_select.size(); i++) {
            Cursor c = mOhOllDB.query(true, DB_TABLE, new String[]{"_id","OHoll_num","OHoll_group","scramble","solve"},
                    "OHoll_group=" + "\"" + chk_select.get(i)
                            + "\"", null, null, null, null, null);
            c.moveToFirst();
            al_DBNum.add(c.getString(0));
            al_OllNum.add(c.getString(1));
            al_group.add(c.getString(2));
            al_scramble.add(c.getString(3));
            al_solve.add(c.getString(4));

            while (c.moveToNext()) {
                al_DBNum.add(c.getString(0));
                al_OllNum.add(c.getString(1));
                al_group.add(c.getString(2));
                al_scramble.add(c.getString(3));
                al_solve.add(c.getString(4));
            }
            c.close();
        }


    }

    private void dataToObject(int num){
        Log.d(TAG, "num :"+ num);
        Log.d(TAG, "al_DBNum : "+ al_DBNum.get(num));
        iv_OhOll.setImageResource(picture[Integer.parseInt(al_DBNum.get(num))-1]);
        tv_ollNum.setText(al_OllNum.get(num));
        tv_group.setText(al_group.get(num));
        tv_scramble.setText(al_scramble.get(num));
        tv_solve.setText(al_solve.get(num));
        skBar_group.setProgress(num);
        skBar_group.setMax(al_DBNum.size()-1);

        if (swt_scramble.isChecked()) {
            tv_solve.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_WEB_PASSWORD);
        } else if (!swt_scramble.isChecked()){
            tv_solve.setInputType(InputType.TYPE_NULL);
        }
        tv_solve.setBackgroundColor(Color.TRANSPARENT);
    }

    private final View.OnClickListener btnClick = v -> {
        int nowGroupNum;
        //group_num[]代表每一個群組的第一個索引值
        if (v.getId()==R.id.btn_previous){
            num = loopGroupNum(al_DBNum.size(),-1,num);
            dataToObject(num);
        }else if (v.getId()==R.id.btn_next){
            num = loopGroupNum(al_DBNum.size(),1,num);
            dataToObject(num);
        }else if (v.getId()==R.id.btn_groupPrevious){
            if (chk_select.size()==1){
                Toast.makeText(this,"只有選擇一個群組",Toast.LENGTH_LONG).show();
            }else {
                int a = al_group.indexOf(tv_group.getText().toString());//找尋現在顯示畫面的群組第一個
                nowGroupNum = group_num.indexOf(a);//比對現在畫面是哪一組
                num = group_num.get(loopGroupNum(chk_select.size(),-1,nowGroupNum));
                Log.d(TAG, "groupPrevious: "+ num);
                dataToObject(num);
            }
        }else if (v.getId()==R.id.btn_groupNext){
            if (chk_select.size()==1){
                Toast.makeText(this,"只有選擇一個群組",Toast.LENGTH_LONG).show();
            }else {
                int b = al_group.indexOf(tv_group.getText().toString());//找尋現在顯示畫面的群組第一個
                nowGroupNum = group_num.indexOf(b);//比對現在畫面是哪一組
                num = group_num.get(loopGroupNum(chk_select.size(),1,nowGroupNum));
                Log.d(TAG, "groupNext: "+num);
                dataToObject(num);
            }
        }
    };

    //製作一個可以在陣列的索引值0~(arrayLength-1)一直循環的方法
    //ex:int a[3] 你把陣列的內容放在某個按鈕上，一直按會讓他0~2一直循環
    //arrayLength=a.length,plusOrMinusValue=增減值,now_num是代表現在在陣列中的哪個索引值0~(arrayLength-1)
    private int loopGroupNum(int arrayLength,int plusOrMinusValue,int now_num){
        if (now_num+plusOrMinusValue >= arrayLength){
            return (now_num+plusOrMinusValue)-arrayLength;
        }else if (now_num+plusOrMinusValue < 0){
            return (now_num+plusOrMinusValue)+arrayLength;
        }else {
            return now_num+plusOrMinusValue;
        }
    }

    private final SeekBar.OnSeekBarChangeListener skBarChange = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            dataToObject(progress);
            num = progress;
        }
        //按住時會觸發
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }
        //停止拖曳時觸發事件
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

    private final CompoundButton.OnCheckedChangeListener swtChange = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            SharedPreferences switchData = getSharedPreferences("data",MODE_PRIVATE);
            if (swt_scramble.isChecked()) {
                tv_solve.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_WEB_PASSWORD);
                switchData.edit().putInt("switch_select" , 1).apply();
            } else if (!swt_scramble.isChecked()){
                tv_solve.setInputType(InputType.TYPE_NULL);
                switchData.edit().putInt("switch_select" , 0).apply();
            }
            tv_solve.setBackgroundColor(Color.TRANSPARENT);
        }
    };


}