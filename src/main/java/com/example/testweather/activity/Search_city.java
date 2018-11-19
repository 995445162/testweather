package com.example.testweather.activity;

import android.content.Intent;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.testweather.util.CityDBHelper;
import com.example.testweather.entity.CityItem;
import com.example.testweather.util.FontItemManager;
import com.example.testweather.R;
import com.example.testweather.adapter.WeatherAdapter;

import java.util.List;

public class Search_city extends AppCompatActivity implements View.OnClickListener{
    private ImageView iv_back_two;
    private ImageView iv_delete;
    private ImageView iv_search;
    private EditText et_message;
    private RecyclerView rv_result;


    //private DBHelper dbHelper;
    private List<CityItem> cities;
    private WeatherAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_city);

        init();
        iv_delete.setVisibility(View.INVISIBLE);
        rv_result.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.set(0,0,0,1);
            }
        });
        //从上一个activity传来的对象没法用，不知道为啥
        //dbHelper = (DBHelper) getIntent().getExtras().getSerializable("dbHelper");
        //dbHelper=new DBHelper(this,"cities.db",null,2);


        iv_back_two.setOnClickListener(this);
        iv_delete.setOnClickListener(this);
        iv_search.setOnClickListener(this);
        et_message.addTextChangedListener(textWatcher);
    }

    private void init() {
        iv_back_two = (ImageView) findViewById(R.id.iv_back_two);
        iv_delete = (ImageView) findViewById(R.id.iv_delete);
        iv_search = (ImageView) findViewById(R.id.iv_search);
        et_message = (EditText) findViewById(R.id.et_message);
        rv_result = (RecyclerView) findViewById(R.id.rv_result);

        FontItemManager.getInstance().changeFont(et_message);
    }

    TextWatcher textWatcher=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (TextUtils.isEmpty(et_message.getText())){
                iv_delete.setVisibility(View.INVISIBLE);
                //adapter=new WeatherAdapter(null); adapter中有getitemcount(),null会报错——空指针异常，直接让recyclerview不可见就行
                //rv_result.setVisibility(View.INVISIBLE);
            }
            else {
                rv_result.setVisibility(View.VISIBLE);
                iv_delete.setVisibility(View.VISIBLE);
                /*SQLiteDatabase db=dbHelper.getWritableDatabase();
                cities=dbHelper.select(db,"select * from cities where name like '%"+et_message.getText()+"%'");*/

                cities= CityDBHelper.getInstance().selectCity("select * from cities where name like '%"+et_message.getText()+"%'");
                adapter = new WeatherAdapter(cities);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Search_city.this);
                rv_result.setLayoutManager(linearLayoutManager);
                rv_result.setAdapter(adapter);

                rv_result.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        InputMethodManager imm= (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        return false;
                    }
                });

                adapter.setOnItemClickListener(new WeatherAdapter.onItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        String code=cities.get(position).getCode();
                        Intent it_back_second = new Intent();
                        it_back_second.putExtra("selected_code", code);
                        setResult(RESULT_OK,it_back_second);
                        finish();
                    }
                });
            }
            /*LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Search_city.this);
            rv_result.setLayoutManager(linearLayoutManager);
            rv_result.setAdapter(adapter);*/
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back_two:{
                finish();
                break;
            }
            case R.id.iv_delete:{
                et_message.setText("");
                break;
            }
            case R.id.iv_search:{
                break;
            }
        }
    }
}
