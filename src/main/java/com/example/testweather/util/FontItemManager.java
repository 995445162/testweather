package com.example.testweather.util;

import android.graphics.Typeface;
import android.widget.TextView;

import com.example.testweather.StartApplication;

/**
 * Created by tujianhua on 2017/11/15.
 */

public class FontItemManager {
    private static FontItemManager fontItemManager;
    private static Typeface tf;
    public static FontItemManager getInstance(){
        if (fontItemManager==null){
            synchronized (FontItemManager.class) {
                if (fontItemManager == null) {
                    fontItemManager=new FontItemManager();
                    tf = StartApplication.getTf();
                }
            }
        }
        return fontItemManager;
    }

    public  void changeFont(TextView tv){
        tv.setTypeface(tf);
    }

}
