package com.example.app_todolist;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DataBase extends SQLiteOpenHelper {

    public DataBase(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }

    //   có 2 dạng DataBase
    // 1=> truy  vấn không trả kết quả : CREATE,INSERT,UPDATE,DELETER....  < NÓ CHỈ THỰC THI THÔI >
    public void QueryData_(String sql) {      // void : không trả kết quả
        SQLiteDatabase database = getWritableDatabase();   // truy vấn - ghi vào dữ liệu       //getWritableDatabase : trong lúc ghi nó có thể đọc được luôn
        database.execSQL(sql);
    }

    // 2=> truy vấn có trả kết quả : SELECT     <mk cần lấy dữ liệu ra>
    public Cursor GetData_(String sql) {
        SQLiteDatabase database = getReadableDatabase();   // getReadableDatabase : chỉ dọc được thôi
        return database.rawQuery(sql, null); // trả con trỏ chứa nlhieeuf dữ liệu hơn
    }
}
