package com.example.testweather.util;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

/**
 * Created by tujianhua on 2018/2/4.
 * 和服务器端连接
 */

public class HttpUtil {
    public static void sendHttpRequest(final String address, final HttpCallbackListener listener) {
        new Thread(new Runnable() {
            /**
             *
             */
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    URL url = new URL(address);
                    Log.e("url666", address);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    //connection.connect();
                    //获得返回结果
                    InputStream inputStream = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder builder = new StringBuilder();
                    String line;
                    while ((line = reader.readLine())!=null) {
                        builder.append(line);
                    }
                    Log.e("结果", builder.toString());
                    if (listener != null) {
                        listener.onFinish(builder.toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    if (listener != null) {
                        listener.onError(e);
                }
                }finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }


    /**
     * @param address
     * @param params
     * @return
     * @throws UnsupportedEncodingException
     * 通过地址加参数拼接获得url
     */
    public static String getURLWithParams(String address, HashMap<String, String> params) throws UnsupportedEncodingException {
        String encode = "utf-8";
        StringBuilder url = new StringBuilder(address);
        url.append("?");
        for (HashMap.Entry<String, String> entry : params.entrySet()) {
            url.append(entry.getKey()).append("=");
            url.append(URLEncoder.encode(entry.getValue(), encode));
            url.append("&");
        }
        url.deleteCharAt(url.length() - 1);
        return url.toString();
    }
}
