package com.example.testweather.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by tujianhua on 2017/10/18.
 */

public class CheckNet {
    public static final int NET_NONE=0;
    public static final int NET_WIFI=1;
    public static final int NET_MOBILE=2;
    public static int getNetState(Context context){
        ConnectivityManager connectivityManager= (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
        if (networkInfo==null){
            return NET_NONE;
        }
        int type= networkInfo.getType();
        if (type == ConnectivityManager.TYPE_WIFI) {
            return NET_WIFI;
        }
        if (type == ConnectivityManager.TYPE_MOBILE) {
            return NET_MOBILE;
        }
        return NET_MOBILE;
    }
}
