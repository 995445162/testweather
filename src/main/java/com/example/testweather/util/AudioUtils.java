package com.example.testweather.util;

import android.content.Context;
import android.os.Bundle;

import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;

/**
 * Created by tujianhua on 2017/11/8.
 */

public class AudioUtils {
    private static AudioUtils audioUtils;
    private SpeechSynthesizer mySynthesizer;
    public static AudioUtils getInstance(){
        if (audioUtils == null) {
            synchronized (AudioUtils.class) {
                if (audioUtils == null) {
                    audioUtils = new AudioUtils();
                }
            }
        }
        return  audioUtils;
    }
    private InitListener initListener=new InitListener() {
        @Override
        public void onInit(int i) {

        }
    };

    public void init(Context context){
        mySynthesizer = SpeechSynthesizer.createSynthesizer(context, initListener);
        mySynthesizer.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan");
        mySynthesizer.setParameter(SpeechConstant.PITCH, "50");
        mySynthesizer.setParameter(SpeechConstant.VOLUME, "50");
    }

    public void speakText(String message){
        int code=mySynthesizer.startSpeaking(message, new SynthesizerListener() {
            @Override
            public void onSpeakBegin() {

            }

            @Override
            public void onBufferProgress(int i, int i1, int i2, String s) {

            }

            @Override
            public void onSpeakPaused() {

            }

            @Override
            public void onSpeakResumed() {

            }

            @Override
            public void onSpeakProgress(int i, int i1, int i2) {

            }

            @Override
            public void onCompleted(SpeechError speechError) {

            }

            @Override
            public void onEvent(int i, int i1, int i2, Bundle bundle) {

            }
        });
    }
}
