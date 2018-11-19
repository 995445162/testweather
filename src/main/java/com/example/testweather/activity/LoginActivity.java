package com.example.testweather.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.testweather.R;
import com.example.testweather.util.QQLogin;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView iv_qqlogin;
    private Tencent mtencent;
    private QQLogin mListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
        iv_qqlogin.setOnClickListener(this);
    }

    private void init(){
        iv_qqlogin = (ImageView) findViewById(R.id.iv_qqlogin);
        mtencent = Tencent.createInstance("1106490991",getApplicationContext());
        mListener = new QQLogin(mtencent,LoginActivity.this);
    }

    private class qqLogin implements IUiListener{
        @Override
        public void onComplete(Object o) {
            Toast.makeText(LoginActivity.this, "授权成功！", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onError(UiError uiError) {

        }

        @Override
        public void onCancel() {

        }
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_qqlogin:{
                Log.e("登录", "login");
                if (!mtencent.isSessionValid()) {Log.e("登录1", "login");
                    mtencent.login(LoginActivity.this, "all", mListener);
                    finish();
                }
                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == Constants.REQUEST_LOGIN&&resultCode==RESULT_OK) {
            mtencent.handleLoginData(data, mListener);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
