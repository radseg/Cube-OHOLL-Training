package com.radseg.oh_oll.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class OHOLL_DBOpenHelper extends SQLiteOpenHelper {
    private static final String DB_FILE = "oholl.db" , DB_TABLE = "oholl";
    public OHOLL_DBOpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + DB_TABLE + " (" +
                "_id INTEGER PRIMARY KEY," +
                "OHoll_num TEXT NOT NULL," +
                "OHoll_group TEXT NOT NULL," +
                "scramble TEXT NOT NULL," +
                "solve TEXT NOT NULL);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
