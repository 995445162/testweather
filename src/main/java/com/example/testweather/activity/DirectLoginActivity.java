package com.example.testweather.activity;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.testweather.R;
import com.example.testweather.util.HttpCallbackListener;
import com.example.testweather.util.HttpUtil;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

public class DirectLoginActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText et_username;
    private EditText et_password;
    private Button btn_login;
    private String loginAddress = "http://10.0.2.2:8080/OldWeather/servlet/LoginServlet";
    private Handler loginHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String result = "";
            String msgobj = String .valueOf(msg.obj);
            if (msgobj.equals("OK")) {
                result = "登录成功";
            } else if (msgobj.equals("Wrong")) {
                result = "密码错误";
            } else if (msgobj.equals("no user")) {
                result = "用户不存在";
            }
            Log.e("login", result);
            Toast.makeText(DirectLoginActivity.this, result, Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direct_login);
        initView();
    }


    private void initView(){
        et_username = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_password);
        btn_login = (Button) findViewById(R.id.btn_login);

        btn_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:{
                String username = et_username.getText().toString();
                String password = et_password.getText().toString();
                HashMap<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("password", password);
                try {
                    String url = HttpUtil.getURLWithParams(loginAddress, params);
                    HttpUtil.sendHttpRequest(url, new HttpCallbackListener() {
                        @Override
                        public void onFinish(String response) {
                            Log.e("onFinish", response);
                            Message msg = new Message();
                            msg.obj = response;
                            loginHandler.sendMessage(msg);
                        }

                        @Override
                        public void onError(Exception e) {
                            Message msg = new Message();
                            msg.obj = e.toString();
                            loginHandler.sendMessage(msg);
                        }
                    });
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
