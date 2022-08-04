package com.radseg.oh_oll;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.radseg.oh_oll.DB.OHOLL_DBOpenHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

import static android.text.InputType.TYPE_NULL;

public class MainActivity2 extends AppCompatActivity {
    private ImageView iv_OHOLL;
    private TextView tv_solve;
    private ArrayList<String> chk_select1 = new ArrayList<String>();
    private String oll[] = new String[57];
    private String group[] = new String[57];
    private String scramble[] = new String[57];
    private String solve[] = new String[57];
    private SQLiteDatabase mOHollDB;
    private static final String DB_FILE = "oholl.db" , DB_TABLE = "oholl";
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

        OHOLL_DBOpenHelper OHollDBOpenHelper =
                new OHOLL_DBOpenHelper(getApplicationContext() , DB_FILE , null , 1);
        mOHollDB = OHollDBOpenHelper.getWritableDatabase();

        Intent intent = getIntent();
        chk_select1 = intent.getStringArrayListExtra("chk_select");

        load();
        putDataToDB();
    }


    private void putDataToDB(){
        Cursor c = mOHollDB.query(true, DB_TABLE, new String[]{"_id","OHoll_num","OHoll_group","scramble","solve"},
                null, null, null, null, null, null);
        if (c == null)
            return;

        if (c.getCount() == 0) {
            for (int i = 0; i < oll.length; i++) {
                ContentValues newRow = new ContentValues();
                newRow.put("OHoll_num", oll[i]);
                newRow.put("OHoll_group",group[i]);
                newRow.put("scramble", scramble[i]);
                newRow.put("solve", solve[i]);
                mOHollDB.insert(DB_TABLE, null, newRow);
            }
        }
    }

    private void load(){
        InputStream ollData = getResources().openRawResource(R.raw.oholl_data);
        Scanner scanner = new Scanner(ollData);
        StringBuilder builder = new StringBuilder();
        while (scanner.hasNextLine()){
            builder.append(scanner.nextLine());
        }
        parseJson(builder.toString());
    }


    private void parseJson(String str){
        try {
            JSONObject root = new JSONObject(str);
            JSONArray oholls = root.getJSONArray("OHOLL");
            for (int i = 0; i < str.length(); i++) {
                JSONObject oholl = oholls.getJSONObject(i);
                oll[i] = oholl.getString("OHoll_num");
                group[i] = oholl.getString("OHoll_group");
                scramble[i] = oholl.getString("scramble");
                solve[i] = oholl.getString("solve");
            }
            Log.d("str.length()", String.valueOf(str.length()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}