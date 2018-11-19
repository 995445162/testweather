package com.example.testweather.activity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.testweather.util.DBHelper;
import com.example.testweather.R;
import com.example.testweather.adapter.AddAdapter;
import com.example.testweather.entity.CityItem;

import java.util.ArrayList;
import java.util.List;

public class CityManager extends AppCompatActivity implements View.OnClickListener{
    private ListView lv_cities;
    private ImageView iv_add;
    private ImageView iv_back;
    private AddAdapter addAdapter;
    private DBHelper dbHelper;
    private SQLiteDatabase db;
    private List<CityItem> current_cities;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_manager);

        initView();

        current_cities = new ArrayList<>();
        current_cities = dbHelper.select(db, "select * from current_cities");
        addAdapter = new AddAdapter(current_cities, this);
        //lv_cities.setDivider();
        lv_cities.setAdapter(addAdapter);
        addAdapter.setOnDeleteClickListener(new AddAdapter.onDeleteClickListener() {
            @Override
            public void onClick(View view, int position) {
                dbHelper.deleteCity(db,"code",new String[]{current_cities.get(position).getCode()});
                current_cities.remove(position);
                addAdapter.notifyDataSetChanged();
            }
        });

        iv_back.setOnClickListener(this);
        iv_add.setOnClickListener(this);
    }
    private void initView(){
        lv_cities = (ListView) findViewById(R.id.lv_cities);
        iv_add = (ImageView) findViewById(R.id.iv_add);
        iv_back = (ImageView) findViewById(R.id.iv_back);

        dbHelper = new DBHelper(this, "current_cities", null, 1);
        db=dbHelper.getWritableDatabase();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:{
                finish();
                break;
            }
            case R.id.iv_add:{
                Intent intent=new Intent(this,SelectCity.class);
                startActivityForResult(intent,6);
                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 6:{
                if (resultCode == RESULT_OK) {
                    Intent intent = new Intent();
                    String code = data.getStringExtra("code");
                    for (int i=0;i<current_cities.size();i++) {
                        if (code.equals(current_cities.get(i).getCode())) {
                            intent.putExtra("position",i);
                            setResult(1,intent);
                            finish();
                            return;
                        }
                    }
                    intent.putExtra("code", code);
                    setResult(2, intent);
                    finish();
                }
            }
        }
    }
}
