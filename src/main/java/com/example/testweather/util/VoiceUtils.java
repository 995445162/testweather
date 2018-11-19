package com.example.testweather.util;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;

import java.util.ArrayList;

/**
 * Created by tujianhua on 2017/11/10.
 */

public class VoiceUtils {
    public static VoiceUtils voiceUtils;
    public String result;

    //单例模式（懒汉式）
    public static VoiceUtils getInstance(){
        if (voiceUtils==null){
            synchronized (VoiceUtils.class) {
                if (voiceUtils == null) {
                    voiceUtils=new VoiceUtils();
                }
            }
        }
        return voiceUtils;
    }

    //初始化
    public void initSpeech(final Context mcontext,final Handler handler){
        final RecognizerDialog recognizer = new RecognizerDialog(mcontext, null);
        recognizer.setParameter(SpeechConstant.DOMAIN,"poi");//poi代表地图
        recognizer.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        recognizer.setParameter(SpeechConstant.ACCENT, "mandarin");
        recognizer.setParameter(SpeechConstant.ASR_PTT,"0");//设置标点，0表示不显示，1表示显示
        recognizer.setListener(new RecognizerDialogListener() {
            @Override
            public void onResult(RecognizerResult recognizerResult, boolean isLast) {
                //判断，来去掉最后的，不管你有没有设置不显示标点，它总是最后会多一个空的
                if (!isLast){
                result=(parJson(recognizerResult.getResultString()));
                    Log.e("shuzhi", result);
                    /*SharedPreferences spf = mcontext.getSharedPreferences("voice", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = spf.edit();
                    editor.putString("voice_result", result);
                    editor.commit();*/
                    class MyThread extends Thread{
                        @Override
                        public void run() {
                            Message msg=new Message();
                            msg.what=1;
                            msg.obj=result;
                            handler.sendMessage(msg);
                        }
                    }
                    Thread thread=new Thread(new MyThread());
                    thread.start();
                }
            }

            @Override
            public void onError(SpeechError speechError) {

            }
        });
        recognizer.show();
    }

    //利用Gson解析Json
    private String parJson(String data){
        Gson gson=new Gson();
        Voice voicebean = gson.fromJson(data, Voice.class);
        StringBuffer sb = new StringBuffer();
        ArrayList<Voice.WSBean> ws=voicebean.ws;
        for (Voice.WSBean wsBean : ws) {
            String text=wsBean.cw.get(0).w;
            sb.append(text);
        }
        return sb.toString();
    }
    public class Voice{
        ArrayList<WSBean>ws;
        class WSBean{
            public ArrayList<CWBean>cw;
        }
        class CWBean{
            public String w;
        }
    }

}
