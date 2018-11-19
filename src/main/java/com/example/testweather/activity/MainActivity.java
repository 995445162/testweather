package com.example.testweather.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.testweather.util.AudioUtils;
import com.example.testweather.util.CheckNet;
import com.example.testweather.util.CityDBHelper;
import com.example.testweather.entity.CityItem;
import com.example.testweather.util.DBHelper;
import com.example.testweather.util.FontManager;
import com.example.testweather.R;
import com.example.testweather.entity.TodayWeather;
import com.example.testweather.util.VoiceUtils;
import com.iflytek.cloud.SpeechUtility;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageView iv_cities;
    private ImageView iv_location;
    private ImageView iv_update;
    private ImageView iv_weather;

    private TextView tv_title;
    private TextView tv_city;
    private TextView tv_updatetime;
    private TextView tv_humidity;
    private TextView tv_pm;
    private TextView tv_aqi;
    private TextView tv_quality;
    private TextView tv_data;
    private TextView tv_temperature;
    private TextView tv_weather;
    private TextView tv_wind;
    private TextView tv_low_high;

    private TextView tv_data1;
    private TextView tv_low_high1;
    private TextView tv_weather1;
    private TextView tv_direction1;
    private TextView tv_wind1;
    private TextView tv_data2;
    private TextView tv_low_high2;
    private TextView tv_weather2;
    private TextView tv_direction2;
    private TextView tv_wind2;
    private TextView tv_data3;
    private TextView tv_low_high3;
    private TextView tv_weather3;
    private TextView tv_direction3;
    private TextView tv_wind3;

    private Button btn_report_first;
    private Button btn_report_second;
    private Button btn_report_third;
    private Button btn_voice;

    private Button btn_go;


    private Animation update_anim;
    private TodayWeather todayWeather=null;

    private String city_code;

    private DBHelper dbHeler;
    private SQLiteDatabase db;


    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:{
                    updateWeather((TodayWeather) msg.obj);
                    break;
                }
                default:{
                    break;
                }
            }
        }
    };

    //TodayWeather todayWeather=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        SystemClock.sleep(100);
        setContentView(R.layout.activity_main);

        initView();

        //initDatabase();
        //初始化讯飞语音
        SpeechUtility.createUtility(getApplicationContext(), "appid=5a01d363");
        
        city_code="101010100";

        //检查有无网络
        if (CheckNet.getNetState(this) == CheckNet.NET_NONE) {
            updateWeatherNoNet();
            Toast.makeText(this,"加载失败，请检查网络或稍后再试",Toast.LENGTH_SHORT).show();
        }
        else {
            //Toast.makeText(this, "网络OK", Toast.LENGTH_SHORT).show();

            SharedPreferences sharedPreferences = getSharedPreferences("last_code", MODE_PRIVATE);
            String last_code = sharedPreferences.getString("code","");
            if (last_code != null&&last_code!="") {
                city_code=last_code;
            }
            getWeatherDataFromNet(city_code);
        }
        //initView();
        //语音播报
        /*AudioUtils.getInstance().init(MainActivity.this); //初始化语音对象
        AudioUtils.getInstance().speakText(todayWeather.getCity()+"今天天气"+todayWeather.getType()+","+todayWeather.getLow()+","
                +todayWeather.getHigh()+","+todayWeather.getFengxiang()+todayWeather.getFengli()); //播放语音*/

        iv_update.setOnClickListener(this);
        iv_cities.setOnClickListener(this);
        iv_location.setOnClickListener(this);
        btn_report_first.setOnClickListener(this);
        btn_report_second.setOnClickListener(this);
        btn_report_third.setOnClickListener(this);
        btn_voice.setOnClickListener(this);

        btn_go.setOnClickListener(this);
    }



    private void initView() {
        iv_cities = (ImageView) findViewById(R.id.iv_cities);
        iv_location = (ImageView) findViewById(R.id.iv_location);
        iv_update = (ImageView) findViewById(R.id.iv_update);
        iv_weather = (ImageView) findViewById(R.id.iv_weather);

        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_city = (TextView) findViewById(R.id.tv_city);
        tv_updatetime= (TextView) findViewById(R.id.tv_updatetime);
        tv_humidity = (TextView) findViewById(R.id.tv_humidity);
        tv_pm = (TextView) findViewById(R.id.tv_pm);
        tv_aqi = (TextView) findViewById(R.id.tv_aqi);
        tv_quality = (TextView) findViewById(R.id.tv_quality);
        tv_data = (TextView) findViewById(R.id.tv_data);
        tv_temperature = (TextView) findViewById(R.id.tv_temperature);
        tv_weather = (TextView) findViewById(R.id.tv_weather);
        tv_wind = (TextView) findViewById(R.id.tv_wind);
        tv_low_high = (TextView) findViewById(R.id.tv_low_high);

        tv_data1 = (TextView) findViewById(R.id.tv_data1);
        tv_low_high1 = (TextView) findViewById(R.id.tv_tv_low_high1);
        tv_weather1 = (TextView) findViewById(R.id.tv_weather1);
        tv_direction1 = (TextView) findViewById(R.id.tv_direction1);
        tv_wind1 = (TextView) findViewById(R.id.tv_wind1);

        tv_data2 = (TextView) findViewById(R.id.tv_data2);
        tv_low_high2 = (TextView) findViewById(R.id.tv_tv_low_high2);
        tv_weather2 = (TextView) findViewById(R.id.tv_weather2);
        tv_direction1 = (TextView) findViewById(R.id.tv_direction2);
        tv_wind2 = (TextView) findViewById(R.id.tv_wind2);

        tv_data3 = (TextView) findViewById(R.id.tv_data3);
        tv_low_high3 = (TextView) findViewById(R.id.tv_tv_low_high3);
        tv_weather3 = (TextView) findViewById(R.id.tv_weather3);
        tv_direction2 = (TextView) findViewById(R.id.tv_direction3);
        tv_wind3 = (TextView) findViewById(R.id.tv_wind3);

        btn_report_first = (Button) findViewById(R.id.btn_report_first);
        btn_report_second = (Button) findViewById(R.id.btn_report_second);
        btn_report_third = (Button) findViewById(R.id.btn_report_third);
        btn_voice = (Button) findViewById(R.id.btn_voice);

        btn_go = (Button) findViewById(R.id.btn_go);

        ViewGroup vg_all = (ViewGroup) findViewById(R.id.rl_all);

        //改变字体
        /*Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/wawa.TTF");
        tv_city.setTypeface(typeface);*/

        FontManager.changeFonts(vg_all);

        //加载动画
        update_anim = AnimationUtils.loadAnimation(MainActivity.this, R.anim.update_anim);

        iv_update.startAnimation(update_anim);
    }

/*    public void initDatabase(){
        dbHeler = new DBHelper(this, "cities.db", null, 2);
        db=dbHeler.getWritableDatabase();

        SharedPreferences get_sp = getSharedPreferences("judgeInsert", MODE_PRIVATE);
        Boolean isinsert = get_sp.getBoolean("isinsert", false);
        if (isinsert == false) {
            dbHeler.insert(db);
        }
        SharedPreferences sp_isinsert = getSharedPreferences("judgeInsert", MODE_PRIVATE);
        SharedPreferences.Editor editor=sp_isinsert.edit();
        editor.putBoolean("isinsert",true);
        editor.commit();


    }*/


    public void getWeatherDataFromNet(String cityCode){
        final String address="http://wthrcdn.etouch.cn/WeatherApi?citykey="+cityCode;
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection httpURLConnection = null;
                //BufferedReader reader = null;
                try {
                    Log.e("start", "start");
                    URL url = new URL(address);
                    httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setConnectTimeout(8000);
                    httpURLConnection.setReadTimeout(8000);
                    InputStream in = httpURLConnection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuffer builder = new StringBuffer();
                    String string;
                    if ((string = reader.readLine()) != null) {
                        builder.append(string);
                    }
                    String response = builder.toString();
                    //TodayWeather todayWeather = parseXML(response);
                    todayWeather = parseXML(response);

                    if (todayWeather != null) {
                        Message message = new Message();
                        message.what = 1;
                        message.obj = todayWeather;
                        handler.sendMessage(message);//心疼自己，就是少了这，妈的
                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                } /*finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }*/
            }
        }).start();
    }

    //更新界面
    private void updateWeather(TodayWeather todayWeather) {
        tv_city.setText(todayWeather.getCity());
        tv_data.setText(todayWeather.getData());
        tv_humidity.setText("湿度:"+todayWeather.getShidu());
        tv_quality.setText(todayWeather.getQuality());
        tv_temperature.setText("现在温度:"+todayWeather.getWendu()+"℃");
        tv_updatetime.setText(todayWeather.getUpdatetime());
        tv_wind.setText("风力:"+todayWeather.getFengli());
        tv_weather.setText(todayWeather.getType());
        tv_aqi.setText(todayWeather.getPm25());
        tv_low_high.setText(todayWeather.getLow()+"~"+todayWeather.getHigh());

        tv_data1.setText(todayWeather.getData1());
        tv_data2.setText(todayWeather.getData2());
        tv_data3.setText(todayWeather.getData3());
        tv_low_high1.setText(todayWeather.getLow1()+"~"+todayWeather.getHigh1());
        tv_low_high2.setText(todayWeather.getLow2()+"~"+todayWeather.getHigh2());
        tv_low_high3.setText(todayWeather.getLow3()+"~"+todayWeather.getHigh3());
        tv_weather1.setText(todayWeather.gettype1());
        tv_weather2.setText(todayWeather.gettype2());
        tv_weather3.setText(todayWeather.gettype3());
        tv_wind1.setText(todayWeather.getFengli1());
        tv_wind2.setText(todayWeather.getFengli2());
        tv_wind3.setText(todayWeather.getFengli3());

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, "更新成功", Toast.LENGTH_SHORT).show();
            }
        },700);

    }

    //没网络时显示之前保存的数据
    private void updateWeatherNoNet(){
        SharedPreferences getMainData = getSharedPreferences("maindata", MODE_PRIVATE);

        tv_city.setText(getMainData.getString("city",""));
        tv_data.setText(getMainData.getString("data",""));
        tv_humidity.setText(getMainData.getString("shidu",""));
        tv_quality.setText(getMainData.getString("quality",""));
        tv_temperature.setText("现在温度:"+getMainData.getString("wendu","")+"℃");
        tv_updatetime.setText(getMainData.getString("updatetime","")+"更新");
        tv_wind.setText(getMainData.getString("fengli",""));
        tv_weather.setText(getMainData.getString("type",""));
        tv_aqi.setText(getMainData.getString("pm25",""));
        tv_low_high.setText(getMainData.getString("low","")+"~"+getMainData.getString("high",""));

        tv_data1.setText(getMainData.getString("date1",""));
        tv_data2.setText(getMainData.getString("date2",""));
        tv_data3.setText(getMainData.getString("date3",""));
        tv_low_high1.setText(getMainData.getString("low1","")+"~"+getMainData.getString("high1",""));
        tv_low_high2.setText(getMainData.getString("low2","")+"~"+getMainData.getString("high2",""));
        tv_low_high3.setText(getMainData.getString("low3","")+"~"+getMainData.getString("high3",""));
        tv_weather1.setText(getMainData.getString("type1",""));
        tv_weather2.setText(getMainData.getString("type2",""));
        tv_weather3.setText(getMainData.getString("type3",""));
        tv_wind1.setText(getMainData.getString("fengli1",""));
        tv_wind2.setText(getMainData.getString("fengli2",""));
        tv_wind3.setText(getMainData.getString("fengli3",""));
    }

    //解析从网络上获取的xml文件
    public TodayWeather parseXML(String xmlData) throws XmlPullParserException, IOException {
        int fengxiangcount=0;
        int dateCount = 0;
        int highCount = 0;
        int lowCount = 0;
        //看xml文件type和fengli一天有两次所以用这个++不行
        int typeCount = 0;
        int fenglicount=0;

        TodayWeather todayWeather=null;
        XmlPullParserFactory factory=XmlPullParserFactory.newInstance();
        XmlPullParser xmlParser=factory.newPullParser();
        xmlParser.setInput(new StringReader(xmlData));
        //保存数据的
        SharedPreferences sp_main_data = getSharedPreferences("maindata", MODE_PRIVATE);
        SharedPreferences.Editor editor=sp_main_data.edit();


        int eventType=xmlParser.getEventType();

        while (eventType != XmlPullParser.END_DOCUMENT) {
            switch (eventType) {
                case XmlPullParser.START_DOCUMENT:{
                    break;
                }
                case XmlPullParser.START_TAG:{
                    if(xmlParser.getName().equals("resp")){
                        todayWeather=new TodayWeather();
                    }
                    if (todayWeather!=null){
                    if (xmlParser.getName().equals("city")) {
                        eventType=xmlParser.next();//这个要放在下一行的前面
                        todayWeather.setCity(xmlParser.getText());
                        editor.putString("city",xmlParser.getText());
                        //eventType=xmlParser.next();
                    }
                    else if (xmlParser.getName().equals("updatetime")) {
                        eventType=xmlParser.next();
                        todayWeather.setUpdatetime(xmlParser.getText());
                        editor.putString("updatetime",xmlParser.getText());
                    }
                    else if (xmlParser.getName().equals("wendu")) {
                        eventType=xmlParser.next();
                        todayWeather.setWendu(xmlParser.getText());
                        editor.putString("wendu",xmlParser.getText());
                    } else if (xmlParser.getName().equals("fengli")&&fenglicount==0) {
                        eventType=xmlParser.next();
                        String fengli=xmlParser.getText();
                        if (fengli.contains("<")){
                            fengli = fengli.replace("<", "低于");
                        }
                        if (fengli.contains(">")) {
                            fengli = fengli.replace(">", "高于");
                        }
                        todayWeather.setFengli(fengli);
                        editor.putString("fengli",fengli);
                    } else if (xmlParser.getName().equals("shidu")) {
                        eventType = xmlParser.next();
                        todayWeather.setShidu(xmlParser.getText());
                        editor.putString("shidu",xmlParser.getText());
                    }  else if (xmlParser.getName().equals("pm25")) {
                            eventType = xmlParser.next();
                            todayWeather.setPm25(xmlParser.getText());
                        editor.putString("pm25",xmlParser.getText());
                    } else if (xmlParser.getName().equals("quality")) {
                            eventType = xmlParser.next();
                            todayWeather.setQuality(xmlParser.getText());
                        editor.putString("quality",xmlParser.getText());
                    } else if (xmlParser.getName().equals("shidu")) {
                            eventType=xmlParser.next();
                            todayWeather.setShidu(xmlParser.getText());
                        editor.putString("shidu",xmlParser.getText());
                    } else if(xmlParser.getName().equals("date") && dateCount == 0) {
                                eventType = xmlParser.next();
                                todayWeather.setData(xmlParser.getText());
                        editor.putString("date",xmlParser.getText());
                                dateCount++;
                    } else if (xmlParser.getName().equals("high") && highCount == 0) {
                                eventType = xmlParser.next();
                                todayWeather.setHigh(xmlParser.getText());
                        editor.putString("high",xmlParser.getText());
                                highCount++;
                    } else if (xmlParser.getName().equals("low") && lowCount == 0) {
                                eventType = xmlParser.next();
                                todayWeather.setLow(xmlParser.getText());
                        editor.putString("low",xmlParser.getText());
                                lowCount++;
                    } else if (xmlParser.getName().equals("type") && typeCount == 0) {
                                eventType = xmlParser.next();
                                todayWeather.setType(xmlParser.getText());
                        editor.putString("type",xmlParser.getText());
                                typeCount++;
                    } else if (xmlParser.getName().equals("fengxiang") && fengxiangcount == 0) {
                        eventType = xmlParser.next();
                        todayWeather.setFengxiang(xmlParser.getText());
                        editor.putString("fengxiang",xmlParser.getText());
                        fengxiangcount++;
                    }else if (xmlParser.getName().equals("date") && dateCount == 1) {
                        eventType=xmlParser.next();
                        todayWeather.setData1(xmlParser.getText());
                        editor.putString("date1",xmlParser.getText());
                        dateCount++;
                    } else if (xmlParser.getName().equals("low") && lowCount == 1) {
                        eventType=xmlParser.next();
                        todayWeather.setLow1(xmlParser.getText());
                        editor.putString("low1",xmlParser.getText());
                        lowCount++;
                    } else if (xmlParser.getName().equals("high")&& highCount == 1) {
                        eventType=xmlParser.next();
                        todayWeather.setHigh1(xmlParser.getText());
                        editor.putString("high1",xmlParser.getText());
                        highCount++;
                    } else if (xmlParser.getName().equals("type") && (typeCount == 1||typeCount==3||typeCount==5)) {
                        eventType=xmlParser.next();
                        typeCount++;
                    } else if (xmlParser.getName().equals("fengli") && (fenglicount == 1||fenglicount==3||fenglicount==5)) {
                            eventType=xmlParser.next();
                            fenglicount++;
                     }else if (xmlParser.getName().equals("fengxiang") && (fenglicount == 1||fenglicount==3||fenglicount==5)) {
                        eventType = xmlParser.next();
                        fengxiangcount++;
                    } else if (xmlParser.getName().equals("date") && dateCount == 2) {
                            eventType=xmlParser.next();
                            todayWeather.setData2(xmlParser.getText());
                        editor.putString("date2",xmlParser.getText());
                        dateCount++;
                        } else if (xmlParser.getName().equals("low") && lowCount == 2) {
                            eventType=xmlParser.next();
                            todayWeather.setLow2(xmlParser.getText());
                        editor.putString("low2",xmlParser.getText());
                        lowCount++;
                        } else if (xmlParser.getName().equals("high")&& highCount == 2) {
                            eventType=xmlParser.next();
                            todayWeather.setHigh2(xmlParser.getText());
                        editor.putString("high2",xmlParser.getText());
                        highCount++;
                        } else if (xmlParser.getName().equals("type") && typeCount == 2) {
                            eventType=xmlParser.next();
                            todayWeather.settype1(xmlParser.getText());
                        editor.putString("type1",xmlParser.getText());
                        typeCount++;
                        } else if (xmlParser.getName().equals("fengli") && fenglicount == 2) {
                            eventType=xmlParser.next();
                        String fengli1=xmlParser.getText();
                        if (fengli1.contains("<")){
                            fengli1 = fengli1.replace("<", "低于");
                        }
                        if (fengli1.contains(">")) {
                            fengli1 = fengli1.replace(">", "高于");
                        }
                            todayWeather.setFengli1(fengli1);
                        editor.putString("fengli1",fengli1);
                        fenglicount++;
                        }else if (xmlParser.getName().equals("fengxiang") && fengxiangcount == 2) {
                        eventType = xmlParser.next();
                        todayWeather.setFengxiang(xmlParser.getText());
                        editor.putString("fengxiang1",xmlParser.getText());
                        fengxiangcount++;
                    }else if (xmlParser.getName().equals("date") && dateCount == 3) {
                            eventType=xmlParser.next();
                            todayWeather.setData3(xmlParser.getText());
                        editor.putString("date3",xmlParser.getText());
                            dateCount++;
                        } else if (xmlParser.getName().equals("low") && lowCount == 3) {
                            eventType=xmlParser.next();
                            todayWeather.setLow3(xmlParser.getText());
                        editor.putString("low3",xmlParser.getText());
                        lowCount++;
                        } else if (xmlParser.getName().equals("high")&& highCount == 3) {
                            eventType=xmlParser.next();
                            todayWeather.setHigh3(xmlParser.getText());
                        editor.putString("high3",xmlParser.getText());
                        highCount++;
                        } else if (xmlParser.getName().equals("type") && typeCount == 4) {
                            eventType=xmlParser.next();
                            todayWeather.settype2(xmlParser.getText());
                        editor.putString("type2",xmlParser.getText());
                        typeCount++;
                        } else if (xmlParser.getName().equals("fengli") && fenglicount == 4) {
                            eventType=xmlParser.next();
                        String fengli2=xmlParser.getText();
                        if (fengli2.contains("<")){
                            fengli2 = fengli2.replace("<", "低于");
                        }
                        if (fengli2.contains(">")) {
                            fengli2 = fengli2.replace(">", "高于");
                        }
                            todayWeather.setFengli2(fengli2);
                        editor.putString("fengli2",fengli2);
                        fenglicount++;
                        }else if (xmlParser.getName().equals("fengxiang") && fengxiangcount == 4) {
                        eventType = xmlParser.next();
                        todayWeather.setFengxiang(xmlParser.getText());
                        editor.putString("fengxiang2",xmlParser.getText());
                        fengxiangcount++;
                    }else if (xmlParser.getName().equals("type") && typeCount == 6) {
                        eventType=xmlParser.next();
                        todayWeather.settype3(xmlParser.getText());
                        editor.putString("type3",xmlParser.getText());
                        typeCount++;
                    } else if (xmlParser.getName().equals("fengli") && fenglicount == 6) {
                        eventType=xmlParser.next();
                        String fengli3=xmlParser.getText();
                        if (fengli3.contains("<")){
                            fengli3 = fengli3.replace("<", "低于");
                        }
                        if (fengli3.contains(">")) {
                            fengli3 = fengli3.replace(">", "高于");
                        }
                        todayWeather.setFengli3(fengli3);
                        editor.putString("fengli3",fengli3);
                        fenglicount++;
                    }else if (xmlParser.getName().equals("fengxiang") && fengxiangcount == 0) {
                        eventType = xmlParser.next();
                        todayWeather.setFengxiang(xmlParser.getText());
                        editor.putString("fengxiang3",xmlParser.getText());
                        fengxiangcount++;
                    }

                    }
                    }
                    break;



                case XmlPullParser.END_TAG:{
                    break;
                }
            }
            eventType = xmlParser.next();
        }
        editor.commit();
        return todayWeather;
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_update:{
                iv_update.startAnimation(update_anim);
                if (CheckNet.getNetState(MainActivity.this) == CheckNet.NET_NONE) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "网络不给力", Toast.LENGTH_SHORT).show();
                        }
                    },700);
                }
                else {
                    getWeatherDataFromNet(city_code);
                }
                break;
            }
            case R.id.iv_cities:{
                Intent it_select_cities = new Intent(MainActivity.this, SelectCity.class);
                it_select_cities.putExtra("current_code",city_code);
                startActivityForResult(it_select_cities,1);
                break;
            }
            case R.id.iv_location:{
                LocationClient mLocationClient = new LocationClient(getApplicationContext());
                MyLocationListener mLocationListener=new MyLocationListener();
                mLocationClient.registerLocationListener(mLocationListener);
                LocationClientOption option=initLocation();
                mLocationClient.setLocOption(option);
                mLocationClient.start();
                break;
            }
            case R.id.btn_report_first:{
                if(CheckNet.getNetState(MainActivity.this)!=CheckNet.NET_NONE){
                AudioUtils.getInstance().init(MainActivity.this); //初始化语音对象
                AudioUtils.getInstance().speakText(todayWeather.getCity()+"今天天气"+todayWeather.getType()+","+todayWeather.getLow()+","+todayWeather.getHigh()+","+todayWeather.getFengxiang()+todayWeather.getFengli()); //播放语音
                    }
                    else {
                    Toast.makeText(MainActivity.this, "网络不给力", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case R.id.btn_report_second:{
                if (CheckNet.getNetState(MainActivity.this)!= CheckNet.NET_NONE){
                    AudioUtils.getInstance().init(MainActivity.this);
                    AudioUtils.getInstance().speakText(todayWeather.getCity()+"明天天气"+todayWeather.gettype1()+","+todayWeather.getLow1()+","+todayWeather.getHigh1());
                }
                else {
                    Toast.makeText(MainActivity.this, "网络不给力", Toast.LENGTH_SHORT).show();
                }
                break;
                }
            case R.id.btn_voice:{

                if (CheckNet.getNetState(MainActivity.this)!= CheckNet.NET_NONE){

                    //String result = getSharedPreferences("voice", MODE_PRIVATE).getString("voice_result", "");
                    Log.e("tishi", "nnbnbnbnbnnbnb");

                    Handler handler1 = new Handler(){
                        @Override
                        public void handleMessage(Message msg) {
                            switch (msg.what) {
                                case 1:{
                                    String result= (String) msg.obj;
                                    Log.e("result", (String) msg.obj);
                                    List<CityItem> city = CityDBHelper.getInstance().selectCity("select * from cities where name REGEXP "+"'"+result+"'");
                                    if (city != null&&city.size()!=0) {
                                        city_code=city.get(0).getCode();
                                        getWeatherDataFromNet(city_code);
                                        //保存数据，以便下次启动app直接获得之前的数据
                                        SharedPreferences sharedPreferences = getSharedPreferences("last_code", MODE_PRIVATE);
                                        SharedPreferences.Editor editor=sharedPreferences.edit();
                                        editor.putString("code", city_code);
                                        editor.commit();
                                    }
                                    else {
                                        AudioUtils.getInstance().init(MainActivity.this);
                                        AudioUtils.getInstance().speakText("请再说一遍城市名称！");
                                    }
                                    break;
                                }
                            }
                            super.handleMessage(msg);
                        }
                    };
                    VoiceUtils.getInstance().initSpeech(MainActivity.this,handler1);
                }
                else {
                    Toast.makeText(MainActivity.this, "网络不给力", Toast.LENGTH_SHORT).show();
                }
                break;
            }

            case R.id.btn_go:{
                Intent intent=new Intent(MainActivity.this, com.example.testweather.activity.FragmentActivity.class);
                startActivity(intent);
            }
        }
    }

    public LocationClientOption initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setIsNeedAddress(true);
        option.setOpenGps(true);
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setCoorType("bd0911");
        option.setScanSpan(200);
        return option;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:{
                if (resultCode == RESULT_OK) {
                    //这样是没用的，这是下一个activity获得上一个activity的方法,上一个获得下一个的直接是他的参数data;
                    //Intent it_receive=getIntent();
                    city_code = data.getStringExtra("code");
                    //保存数据，以便下次启动app直接获得之前的数据
                    SharedPreferences sharedPreferences = getSharedPreferences("last_code", MODE_PRIVATE);
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putString("code", city_code);
                    editor.commit();

                    if (CheckNet.getNetState(MainActivity.this) == CheckNet.NET_NONE) {
                        Toast.makeText(MainActivity.this,"网络不给力",  Toast.LENGTH_SHORT).show();
                        return;
                    }
                    getWeatherDataFromNet(city_code);
                }
            }
        }
    }
    public class MyLocationListener implements BDLocationListener{

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            String name=bdLocation.getCity();
            name=name.substring(0,name.length()-1);
            List<CityItem> city= CityDBHelper.getInstance().selectCity("select * from cities where name='" + name+"'");
            Log.e("log", name);
            if (city!=null){
                city_code=city.get(0).getCode();
                SharedPreferences sp = getSharedPreferences("last_code", MODE_PRIVATE);
                SharedPreferences.Editor editor=sp.edit();
                editor.putString("code", city_code);
                editor.commit();
                getWeatherDataFromNet(city_code);
            }
        }
    }
}
