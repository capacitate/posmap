package com.example.junhong.posmap;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Junhong on 2015-09-27.
 */
public class DBhandle extends SQLiteOpenHelper {

    public DBhandle(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table posmap (" +
                "_id integer primary key autoincrement, " +
                "msg text, " +
                "latitude double, " +
                "longitude double);";

        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /*
        db 버전이 업그레이드 되었을 때 실행되는 메소드
        현재 method는 기존의 데이터를 모두 지우고 다시 만드는 형태
         */
        String sql = "drop table if exists posmap";
        db.execSQL(sql);

        onCreate(db);
    }
}
