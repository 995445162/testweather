package com.example.testweather.util;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.testweather.entity.CityItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tujianhua on 2017/11/12.
 */

public class CityDBHelper {
    private String DATABASE_PATH = "/data/data/com.example.testweather/databases/";
    private String DATABASE_NAME="city.db";
    private static CityDBHelper dbHelper;

    public static CityDBHelper getInstance(){
        if (dbHelper == null) {
            synchronized (CityDBHelper.class) {
                if (dbHelper == null) {
                    dbHelper=new CityDBHelper();
                }
            }
        }
        return dbHelper;
    }

    public List<CityItem> selectCity(String sql){
        SQLiteDatabase db= SQLiteDatabase.openOrCreateDatabase(DATABASE_PATH + DATABASE_NAME, null);
        int i=1;
        List<CityItem> cities_list=new ArrayList<>();
        Cursor cursor=db.rawQuery(sql,null);
        while (cursor.moveToNext()){
            String code = cursor.getString(cursor.getColumnIndex("code"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            CityItem city=new CityItem(i,code,name);
            i++;
            cities_list.add(city);
        }
        cursor.close();
        return cities_list;
    }

    public Cursor select(String sql){
        SQLiteDatabase db= SQLiteDatabase.openOrCreateDatabase(DATABASE_PATH + DATABASE_NAME, null);
        Cursor cursor = db.rawQuery(sql, null);
        return cursor;
    }
}
