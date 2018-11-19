package com.example.testweather.util;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.testweather.StartApplication;

/**
 * Created by tujianhua on 2017/11/11.
 */

public class FontManager {

    public static void changeFonts(ViewGroup group){
        Typeface tf = StartApplication.getTf();
        for (int i=0;i<group.getChildCount();i++) {
            View child=group.getChildAt(i);
            if (child instanceof TextView) {
                ((TextView) child).setTypeface(tf);
            }
            else if (child instanceof Button) {
                ((Button) child).setTypeface(tf);
            } else if (child instanceof EditText) {
                ((EditText) child).setTypeface(tf);
            } else if (child instanceof ViewGroup) {
                changeFonts((ViewGroup) child);
            }
        }
    }
}
