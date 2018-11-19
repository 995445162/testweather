package com.example.testweather.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.testweather.entity.CityItem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tujianhua on 2017/10/29.
 */

public class DBHelper extends SQLiteOpenHelper implements Serializable {
    public static final String CREATE_CITIES="create table current_cities("
            +"id integer primary key autoincrement,"
            +"code varchar(9) , "
            +"name varchar(10) )";

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_CITIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists cities");
        onCreate(sqLiteDatabase);
    }

    public boolean checkIsNull(SQLiteDatabase sqLiteDatabase) {
        Cursor cursor = sqLiteDatabase.rawQuery("select * from current_cities", null);
        if (!cursor.moveToFirst()) {
            return true;
        }
        return false;
    }

    public void insertCity(SQLiteDatabase db,String code, String name) {
        ContentValues values = new ContentValues();
        values.put("code", code);
        values.put("name", name);
        db.insert("current_cities", null, values);
    }

    public void deleteCity(SQLiteDatabase db,String column,String []arg){
        db.delete("current_cities", column + "=?", arg);
    }
    public void insert(SQLiteDatabase db){
        ContentValues values = new ContentValues();

        values.put("id",1);
        values.put("code","101010100");
        values.put("name","北京");
        db.insert("cities",null,values);
        values.clear();

        values.put("id",2);
        values.put("code","101200101");
        values.put("name","武汉");
        db.insert("cities",null,values);
        values.clear();

        values.put("id",3);
        values.put("code","101200501");
        values.put("name","黄冈");
        db.insert("cities",null,values);
        values.clear();

        values.put("id",4);
        values.put("code","101201401");
        values.put("name","荆门");
        db.insert("cities",null,values);
        values.clear();

        values.put("id",5);
        values.put("code","101210101");
        values.put("name","杭州");
        db.insert("cities",null,values);
        values.clear();

        values.put("id",6);
        values.put("code","101210801");
        values.put("name","丽水");
        db.insert("cities",null,values);
        values.clear();

        values.put("id",7);
        values.put("code","101210401");
        values.put("name","宁波");
        db.insert("cities",null,values);
        values.clear();

        values.put("id",8);
        values.put("code","101210701");
        values.put("name","温州");
        db.insert("cities",null,values);
        values.clear();

        values.put("id",9);
        values.put("code","101210501");
        values.put("name","绍兴");
        db.insert("cities",null,values);
        values.clear();

        values.put("id",10);
        values.put("code","101040100");
        values.put("name","重庆");
        db.insert("cities",null,values);
        values.clear();

        values.put("id",11);
        values.put("code","101280601");
        values.put("name","深圳");
        db.insert("cities",null,values);
        values.clear();

        values.put("id",12);
        values.put("code","101281601");
        values.put("name","东莞");
        db.insert("cities",null,values);
        values.clear();

        values.put("id",13);
        values.put("code","101280101");
        values.put("name","广州");
        db.insert("cities",null,values);
        values.clear();

        values.put("id",14);
        values.put("code","101281601");
        values.put("name","东莞");
        db.insert("cities",null,values);
        values.clear();

        values.put("id",15);
        values.put("code","101280501");
        values.put("name","汕头");
        db.insert("cities",null,values);
        values.clear();

        values.put("id",16);
        values.put("code","101300301");
        values.put("name","柳州");
        db.insert("cities","id",values);
        values.clear();

        values.put("id",17);
        values.put("code","101300501");
        values.put("name","桂林");
        db.insert("cities",null,values);
        values.clear();

        values.put("id",18);
        values.put("code","101291401");
        values.put("name","丽江");
        db.insert("cities",null,values);
        values.clear();
    }

    public List<CityItem> select(SQLiteDatabase db, String sql){
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
}
