package com.example.testweather.activity;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.testweather.util.CityDBHelper;
import com.example.testweather.entity.CityItem;
import com.example.testweather.util.FontItemManager;
import com.example.testweather.R;
import com.example.testweather.adapter.SelectAdapter;

import java.util.ArrayList;
import java.util.List;

public class SelectCity extends AppCompatActivity implements View.OnClickListener{
    //private List<CityItem> cities;
    //private RecyclerView rv_cities;
    private ImageView iv_back;
    private TextView tv_current_city;
    private ImageView iv_search;

    private List<String> province;
    private List<String> cities;
    private List<List<String>> pro_item;
    private ExpandableListView elv_cities;
    private BaseExpandableListAdapter adapter;

    //private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_city);

        //得到上一个activity传来的数据
        Intent it_receive=getIntent();
        String current_code = it_receive.getStringExtra("current_code");

        initView();
        initData();

        adapter=new SelectAdapter(this,province,pro_item);
        elv_cities.setAdapter(adapter);
        elv_cities.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                String code="";
                Intent it_back = new Intent();
                String name = pro_item.get(i).get(i1);
                Cursor cursor = CityDBHelper.getInstance().select("select code from cities where name=" + "'" + name + "'");
                if (cursor != null) {
                    cursor.moveToNext();
                    code= cursor.getString(cursor.getColumnIndex("code"));
                }
                it_back.putExtra("code",code );
                setResult(RESULT_OK, it_back);
                finish();
                return true;
            }
        });

/*        //直接在代码中加载数据
        cities = new ArrayList<>();
        Map map1 = new HashMap();
        map1.put("101210101","杭州");
        Map map2 = new HashMap();
        map2.put("101200101", "武汉");
        Map map3 = new HashMap();
        map3.put("101201401", "荆门");
        cities.add(map1);
        cities.add(map2);
        cities.add(map3);*/




/*        //创建数据库
        dbHelper=new DBHelper(this,"cities.db",null,2);
        SQLiteDatabase db=dbHelper.getWritableDatabase();

        //设置标题的城市
        Cursor cursor=db.rawQuery("select name from cities where code="+current_code,null);
        if (cursor.moveToFirst()){
            String current_name=cursor.getString(cursor.getColumnIndex("name"));
            tv_current_city.setText("当前城市:"+current_name);
        }*/

        List<CityItem> current_name=CityDBHelper.getInstance().selectCity("select * from cities where code=" + current_code);
        if (current_name != null&&current_name.size()!=0) {
            tv_current_city.setText("当前城市:"+current_name.get(0).getName());
        }


/*        SharedPreferences sharedPreferences = getSharedPreferences("judgeInsert", MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putBoolean("insertHad", false);
        editor.commit();

        SharedPreferences getSharedPreferences=getSharedPreferences("judgeInsert", MODE_PRIVATE);
        Boolean insertHad = getSharedPreferences.getBoolean("insertHad", true);
        if (insertHad==false){
            dbHelper.insert(db);//插入数据，刚安装app插入一次就行
            editor.clear();
            }*/
        //cities=dbHelper.select(db,"select * from cities");//查询数据

       /* cities=CityDBHelper.getInstance().select("select * from cities");//查询数据

        WeatherAdapter adapter = new WeatherAdapter(cities);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rv_cities.setLayoutManager(linearLayoutManager);

        //列表的点击事件
        adapter.setOnItemClickListener(new WeatherAdapter.onItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String pos_city=cities.get(position).getCode();
                Intent it_back = new Intent();
                it_back.putExtra("code",pos_city);
                setResult(RESULT_OK,it_back);
                finish();
            }
        });
        adapter.setOnItemLongClickListener(new WeatherAdapter.onItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int position) {
                Toast.makeText(SelectCity.this, "别按这么长时间傻屌", Toast.LENGTH_SHORT).show();
            }
        });
        rv_cities.setAdapter(adapter);
        rv_cities.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                super.onDraw(c, parent, state);
            }

            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.set(0,0,0,1);
            }
        });
*/
        iv_back.setOnClickListener(this);
        tv_current_city.setOnClickListener(this);
        iv_search.setOnClickListener(this);

    }

    private void initView() {
        //rv_cities = (RecyclerView) findViewById(R.id.rv_cities);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_current_city = (TextView) findViewById(R.id.tv_current_city);
        iv_search = (ImageView) findViewById(R.id.iv_search);
        elv_cities = (ExpandableListView) findViewById(R.id.elv_cities);

        FontItemManager.getInstance().changeFont(tv_current_city);
    }

    private void initData(){
        province = new ArrayList<>();
        pro_item = new ArrayList<>();
        Cursor cursor_pro = CityDBHelper.getInstance().select("select distinct province from cities");
        while (cursor_pro.moveToNext()) {
            province.add(cursor_pro.getString(cursor_pro.getColumnIndex("province")));
        }
        cursor_pro.close();

        for (int i=0;i<province.size();i++) {
            cities = new ArrayList<>();//每次for循环要重新初始化（用来清除之前存在list中的数据）
            Cursor cursor_city = CityDBHelper.getInstance().select("select name from cities where province=" + "'" + province.get(i) + "'");
            while (cursor_city.moveToNext()) {
                cities.add(cursor_city.getString(cursor_city.getColumnIndex("name")));
            }
            pro_item.add(cities);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:{
                finish();
                break;
            }
            case R.id.tv_current_city:{
                Intent it_start = new Intent(SelectCity.this,Search_city.class);
                /*Bundle bundle = new Bundle();
                bundle.putSerializable("dbHelper",dbHelper);
                it_start.putExtras(bundle);*/
                startActivityForResult(it_start,1);
                break;
            }
            case R.id.iv_search:{
                Intent it_start = new Intent(SelectCity.this,Search_city.class);
                startActivityForResult(it_start,1);
                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:{
                if (resultCode == RESULT_OK) {
                    String selected_code = data.getStringExtra("selected_code");
                    if (selected_code != null) {
                        Intent it_back_one = new Intent();
                        it_back_one.putExtra("code", selected_code);
                        setResult(RESULT_OK,it_back_one);
                        finish();
                    }
                }
            }
        }
    }
}
