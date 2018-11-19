package com.example.testweather.util;

/**
 * Created by tujianhua on 2018/2/4.
 */

public interface HttpCallbackListener {
    void onFinish(String response);
    void onError(Exception e);
}
