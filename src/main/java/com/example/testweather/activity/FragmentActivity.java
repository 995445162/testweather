package com.example.testweather.activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.example.testweather.fragment.MessageFragment;
import com.example.testweather.fragment.WeatherFragment;
import com.example.testweather.util.BitmapManager;
import com.example.testweather.util.CityDBHelper;
import com.example.testweather.entity.CityItem;
import com.example.testweather.util.DBHelper;
import com.example.testweather.util.DepthPageTransformer;
import com.example.testweather.adapter.FragmentAdapter;
import com.example.testweather.util.LocationManager;
import com.example.testweather.fragment.MainFragment;
import com.example.testweather.R;

import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FragmentActivity extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    //private ViewPager viewPager;
    private DBHelper dbHelper;
    private SQLiteDatabase db;
    //private FragmentAdapter adapter;

    private ImageView iv_drawer;
    private RadioGroup rg_bottom;

    private List<CityItem> current_cities;
    private List<Fragment> fragment_list;
    private ImageView iv_setting;
    private DrawerLayout dl_user_message;

    private Fragment weather_fragment;
    private Fragment message_fragment;
    private List<Fragment> bottom_fragments;

    private String[] permissions;

    private TextView tv_title;

    //drawer的控件
    private ImageView iv_user_image;
    private TextView tv_user_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //this.setTheme(R.style.StartTheme);
        super.onCreate(savedInstanceState);

        setTheme(R.style.AppTheme);
        SystemClock.sleep(1000);
        setContentView(R.layout.activity_fragment);

        init();

        initList();

        //默认的启动的是weather这个fragment
        rg_bottom.check(R.id.rb_weather);
        bottom_fragments.add(weather_fragment);
        FragmentManager manager=getSupportFragmentManager();
        FragmentTransaction transaction=manager.beginTransaction();
        transaction.add(R.id.fl_main, weather_fragment);
        transaction.commit();

        checkPermissions(permissions);

        checkUser();

        iv_setting.setOnClickListener(this);
        iv_drawer.setOnClickListener(this);
        rg_bottom.setOnCheckedChangeListener(this);

        tv_title.setOnClickListener(this);

        iv_user_image.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("onResume", "onResume");
    }

    private void init(){
        tv_title = (TextView) findViewById(R.id.tv_title);

        iv_setting = (ImageView) findViewById(R.id.iv_setting);
        iv_drawer = (ImageView) findViewById(R.id.iv_drawer);
        rg_bottom = (RadioGroup) findViewById(R.id.rg_bottom);
        dl_user_message = (DrawerLayout) findViewById(R.id.dl_user_message);

        iv_user_image = (ImageView) findViewById(R.id.iv_user_image);
        tv_user_name = (TextView) findViewById(R.id.tv_user_name);

        weather_fragment=new WeatherFragment();
        message_fragment=new MessageFragment();
        bottom_fragments = new ArrayList<>();



       // viewPager = (ViewPager) findViewById(R.id.vp_fragment);

        dbHelper = new DBHelper(this, "current_cities", null, 1);
        db=dbHelper.getWritableDatabase();

        permissions=new String[]{Manifest.permission.READ_PHONE_STATE,Manifest.permission.PROCESS_OUTGOING_CALLS};

        IntentFilter filter = new IntentFilter();
        filter.addAction("LOGIN");
        registerReceiver(new setDrawerBroadcast(), filter);
    }

    private void initList(){
        fragment_list = new ArrayList<>();
        current_cities = new ArrayList<>();

        boolean isNull = dbHelper.checkIsNull(db);
        if (isNull) {
            LocationManager manager = new LocationManager(new MyLocationListener(), this);
            manager.initClient();
        }else {
            current_cities = dbHelper.select(db, "select * from current_cities");
            for (int i=0;i<current_cities.size();i++) {
                MainFragment fragment=new MainFragment();
                Bundle bundle = new Bundle();
                bundle.putString("code", current_cities.get(i).getCode());
                fragment.setArguments(bundle);
                fragment_list.add(fragment);
            }
            //adapter = new FragmentAdapter(getSupportFragmentManager(), fragment_list);
           // viewPager.setAdapter(adapter);
        }
    }

    public void checkUser(){
        SharedPreferences preferences = this.getSharedPreferences("user", MODE_PRIVATE);
        String username = preferences.getString("username", null);
        if (username == null) {
            tv_user_name.setText("未登录");
            iv_user_image.setImageResource(R.drawable.widget_w_0);
        }
    }

    public void checkPermissions(String[] permissions){
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                Log.e("log", "checkPermissons");
                ActivityCompat.requestPermissions(this, permissions, 2000);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int isGrant : grantResults) {
            if (isGrant != PackageManager.PERMISSION_GRANTED) {
                openAppDetails();
            }
        }
    }

    private void openAppDetails(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("权限不可用")
                .setMessage("请在-应用设置-权限-中，开启读取手机状态，去电权限")
                .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                    }
                })
                .setPositiveButton("取消", null);
        builder.show();
    }

    private class MyLocationListener implements BDLocationListener{

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            String name=bdLocation.getCity();
            name = name.substring(0, name.length() - 1);
            current_cities = CityDBHelper.getInstance().selectCity("select * from cities where name='" + name + "'");
            if (current_cities != null) {
                String code = current_cities.get(0).getCode();
                dbHelper.insertCity(db, code, name);
                MainFragment fragment=new MainFragment();
                Bundle bundle = new Bundle();
                bundle.putString("code",code);
                fragment.setArguments(bundle);
                fragment_list.add(fragment);
                FragmentAdapter adapter=new FragmentAdapter(getSupportFragmentManager(),fragment_list);
                //viewPager.setAdapter(adapter);
                //viewPager.setPageTransformer(true, new DepthPageTransformer());
            }

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_setting:{
                Log.e("log", "设置");
                Intent intent = new Intent(FragmentActivity.this, SettingActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("fragments", (Serializable) fragment_list);
                startActivityForResult(intent,100);
                break;
            }
            case R.id.iv_drawer:{
                Log.e("log", "点击了drawer");
                if (dl_user_message.isDrawerOpen(Gravity.START)) {
                dl_user_message.closeDrawer(Gravity.START);
                }else {
                    dl_user_message.openDrawer(Gravity.START);
                }
                break;
            }
            case R.id.tv_title:{
                Log.e("log", "点击了tv");
                if (dl_user_message.isDrawerOpen(Gravity.START)) {
                    dl_user_message.closeDrawer(Gravity.START);
                }else {
                    dl_user_message.openDrawer(Gravity.START);
                }
                break;
            }
            case R.id.iv_user_image:{
                SharedPreferences preferences = this.getSharedPreferences("user", MODE_PRIVATE);
                String username = preferences.getString("username", null);
                if (username == null) {
                    Intent intent = new Intent(this, DirectLoginActivity.class);
                    startActivityForResult(intent,10);
                }
            }
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkId) {
        switch (checkId) {
            case R.id.rb_weather:{
                Log.e("log", "这是天气界面");
                FragmentManager manager=getSupportFragmentManager();
                FragmentTransaction transaction=manager.beginTransaction();
                transaction.hide(bottom_fragments.get(1));
                transaction.show(bottom_fragments.get(0));
                transaction.commit();
                break;
            }
            case R.id.rb_message:{
                Log.e("log", "这是消息界面");
                FragmentManager manager=getSupportFragmentManager();
                FragmentTransaction transaction=manager.beginTransaction();
                transaction.hide(bottom_fragments.get(0));
                if (bottom_fragments.size() == 1) {
                    bottom_fragments.add(message_fragment);
                    transaction.add(R.id.fl_main, message_fragment);
                    transaction.commit();
                }else {
                    transaction.show(bottom_fragments.get(1));
                    transaction.commit();
                }
                break;
            }
        }
    }

    private class setDrawerBroadcast extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("昵称", intent.getStringExtra("nickname"));
            tv_user_name.setText(intent.getStringExtra("nickname"));
            iv_user_image.setImageBitmap(BitmapManager.getBitmap(intent.getStringExtra("imgpath")));
        }
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        for (int i=0;i<fragment_list.size();i++) {
            if (requestCode == i) {
                MainFragment fragment = (MainFragment) fragment_list.get(i);
                fragment.onActivityResult(requestCode,resultCode,data);
            }
        }
        switch (requestCode) {
            case 100:{
                Log.e("result", "result100");
                if (resultCode == 1) {
                    //viewPager.setCurrentItem(data.getIntExtra("position",0));
                } else if (resultCode == 2) {
                    String code = data.getStringExtra("code");
                    String name=CityDBHelper.getInstance().selectCity("select * from cities where code='"+code+"'").get(0).getName();
                    dbHelper.insertCity(db,code,name);
                    MainFragment fragment = new MainFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("code", code);
                    fragment.setArguments(bundle);
                    fragment_list.add(fragment);
                    //adapter.notifyDataSetChanged();
                    //viewPager.setCurrentItem(fragment_list.size() - 1);
                }

                bottom_fragments.get(0).onActivityResult(requestCode, resultCode, data);
            }
            /*case 10:{
                tv_user_name.setText(data.getStringExtra("nickname"));
                iv_user_image.setImageBitmap(BitmapManager.getBitmap(data.getStringExtra("imgpath")));
            }*/
        }

    }


}
