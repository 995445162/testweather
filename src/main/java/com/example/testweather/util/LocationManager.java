package com.example.testweather.util;

import android.content.Context;

import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

/**
 * Created by tujianhua on 2017/11/23.
 */

public class LocationManager {
    private BDLocationListener bdLocationListener;
    private Context mcontext;

    public LocationManager(BDLocationListener bdLocationListener, Context mcontext) {
        this.bdLocationListener = bdLocationListener;
        this.mcontext = mcontext;
    }

    public void initClient(){
        LocationClient locationClient= new LocationClient(mcontext);
        locationClient.registerLocationListener(bdLocationListener);
        locationClient.setLocOption(initOption());
        locationClient.start();
    }
    private LocationClientOption initOption(){
        LocationClientOption option = new LocationClientOption();
        option.setIsNeedAddress(true);
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setOpenGps(true);
        option.setCoorType("bd0911");
        option.setScanSpan(100);
        return option;
    }
}
