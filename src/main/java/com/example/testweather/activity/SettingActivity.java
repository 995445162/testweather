package com.example.testweather.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.testweather.R;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener{
    private LinearLayout ll_manager_city;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();

        ll_manager_city.setOnClickListener(this);
    }
    private void initView(){
        ll_manager_city = (LinearLayout) findViewById(R.id.ll_manager_city);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_manager_city:{
                Intent intent = new Intent(this, CityManager.class);
                startActivityForResult(intent,1);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:{
                Intent intent = new Intent();
                if (resultCode==1) {
                    intent.putExtra("position",data.getIntExtra("position",0));
                    setResult(1,intent);
                    finish();
                } else if (resultCode == 2) {
                    intent.putExtra("code", data.getStringExtra("code"));
                    setResult(2, intent);
                    finish();
                }
            }
        }
    }
}
