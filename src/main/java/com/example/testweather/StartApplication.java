package com.example.testweather;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by tujianhua on 2017/11/12.
 */

public class StartApplication extends Application{

    private static Typeface tf;
    //private String[] permissions;
    @Override
    public void onCreate() {
        super.onCreate();

        //startCall();
        copyDB();
        tf = Typeface.createFromAsset(getAssets(), "fonts/xingshu.TTF");
    }

    public void startCall(){
        Intent intent = new Intent();
        intent.setAction("android.intent.action.PHONE_STATE");
        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        sendBroadcast(intent);
    }

    public static Typeface getTf(){
        return tf;
    }



    //将assets文件夹下的数据库文件读到app中
    public void copyDB(){
         String DATABASE_PATH = "/data/data/com.example.testweather/databases/";
         String DATABASE_NAME="city.db";

        File db=new File(DATABASE_PATH+DATABASE_NAME);
        if (!db.exists()){
            File file = new File(DATABASE_PATH);
            if (!file.exists()) {
                file.mkdir();
            }
            try {
                InputStream is = getAssets().open("cities.db");
                FileOutputStream fos = new FileOutputStream(db);
                int len=0;
                byte[] buffer = new byte[1024];
                while((len=is.read(buffer))!=-1){
                    fos.write(buffer,0,len);
                }
                fos.flush();
                fos.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
