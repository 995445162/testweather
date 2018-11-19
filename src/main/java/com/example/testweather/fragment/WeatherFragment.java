package com.example.testweather.fragment;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.example.testweather.R;
import com.example.testweather.activity.FragmentActivity;
import com.example.testweather.adapter.FragmentAdapter;
import com.example.testweather.entity.CityItem;
import com.example.testweather.util.CityDBHelper;
import com.example.testweather.util.DBHelper;
import com.example.testweather.util.DepthPageTransformer;
import com.example.testweather.util.LocationManager;
import com.iflytek.cloud.InitListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tujianhua on 2017/12/2.
 */

public class WeatherFragment extends Fragment {
    private ViewPager viewPager;
    private DBHelper dbHelper;
    private SQLiteDatabase db;
    private List<Fragment> fragment_list;
    private List<CityItem> current_cities;
    private FragmentAdapter adapter;
    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.weather_fragment, container, false);
        init(view);
        initList();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("log", "weather resume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e("log", "weather pause");
    }

    private void init(View view) {
        viewPager = view.findViewById(R.id.vp_fragment);
        dbHelper = new DBHelper(getContext(), "current_cities", null, 1);
        db = dbHelper.getWritableDatabase();
    }

    private void initList() {
        fragment_list = new ArrayList<>();
        current_cities = new ArrayList<>();


        boolean isNull = dbHelper.checkIsNull(db);
        if (isNull) {
            LocationManager manager = new LocationManager(new MyLocationListener(), getContext());
            manager.initClient();
        } else {
            current_cities = dbHelper.select(db, "select * from current_cities");
            for (int i = 0; i < current_cities.size(); i++) {
                MainFragment fragment = new MainFragment();
                Bundle bundle = new Bundle();
                bundle.putString("code", current_cities.get(i).getCode());
                fragment.setArguments(bundle);
                fragment_list.add(fragment);
            }
            adapter = new FragmentAdapter(getActivity().getSupportFragmentManager(), fragment_list);
            viewPager.setAdapter(adapter);
        }
    }


    private class MyLocationListener implements BDLocationListener {

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
                FragmentAdapter adapter=new FragmentAdapter(getActivity().getSupportFragmentManager(),fragment_list);
                viewPager.setAdapter(adapter);
                viewPager.setPageTransformer(true, new DepthPageTransformer());
            }

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("log", "weatherFragment result");
        switch (requestCode) {
            case 100:{
                if (resultCode == 1) {
                    viewPager.setCurrentItem(data.getIntExtra("position",0));
                } else if (resultCode == 2) {
                    String code = data.getStringExtra("code");
                    String name=CityDBHelper.getInstance().selectCity("select * from cities where code='"+code+"'").get(0).getName();
                    MainFragment fragment = new MainFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("code", code);
                    fragment.setArguments(bundle);
                    fragment_list.add(fragment);
                    adapter.notifyDataSetChanged();
                    viewPager.setCurrentItem(fragment_list.size() - 1);
                }else {
                    Log.e("log", "delete");
                    fragment_list.clear();
                    current_cities=null;
                    current_cities = new ArrayList<>();


                    boolean isNull = dbHelper.checkIsNull(db);
                    if (isNull) {
                        LocationManager manager = new LocationManager(new MyLocationListener(), getContext());
                        manager.initClient();
                    } else {
                        current_cities = dbHelper.select(db, "select * from current_cities");
                        for (int i = 0; i < current_cities.size(); i++) {
                            MainFragment fragment = new MainFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("code", current_cities.get(i).getCode());
                            fragment.setArguments(bundle);
                            fragment_list.add(fragment);
                        }
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        }
    }
}
