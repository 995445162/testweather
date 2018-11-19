package com.example.testweather.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;

import com.example.testweather.activity.LoginActivity;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by tujianhua on 2018/1/29.
 */

public class QQLogin implements IUiListener {
    private Tencent mtencent;
    private Context context;

    public QQLogin(Tencent mtencent, Context context) {
        this.mtencent = mtencent;
        this.context = context;
    }

    @Override
    public void onComplete(Object o) {
        Toast.makeText(context, "授权成功！", Toast.LENGTH_LONG).show();
        JSONObject jsonObject = (JSONObject) o;
        initOpenidAndToken(jsonObject);
        getUserInfo();

    }

    @Override
    public void onError(UiError uiError) {
        Toast.makeText(context, "授权取消！", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCancel() {
        Toast.makeText(context, "授权取消！", Toast.LENGTH_LONG).show();
    }
    private void initOpenidAndToken(JSONObject jsonObject) {
        try {
            String openId = jsonObject.getString("openid");
            String token = jsonObject.getString("access_token");
            String expires = jsonObject.getString("expires_in");

            mtencent.setAccessToken(token, expires);
            mtencent.setOpenId(openId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getUserInfo(){
        QQToken qqToken = mtencent.getQQToken();
        UserInfo userInfo = new UserInfo(context, qqToken);
        final Intent intent = new Intent("LOGIN");
        userInfo.getUserInfo(new IUiListener() {
            @Override
            public void onComplete(Object o) {
                final JSONObject jsonObject = (JSONObject) o;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String nickname = null;
                        try {
                            nickname = jsonObject.getString("nickname");
                            Log.e("nickname涂建华", nickname);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        intent.putExtra("nickname", nickname);
                        if (jsonObject.has("figureurl")) {
                            try {
                                String imagePath = jsonObject.getString("figureurl");
                                intent.putExtra("imgpath", imagePath);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        context.sendBroadcast(intent);
                    }
                });
            }

            @Override
            public void onError(UiError uiError) {

            }

            @Override
            public void onCancel() {

            }
        });
    }
}
