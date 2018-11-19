package com.example.testweather.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.example.testweather.util.AudioUtils;
import com.example.testweather.view.LightTextView;

/**
 * Created by tujianhua on 2017/11/25.
 */

public class CallService extends Service{
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                final Context mcontext = (Context) intent.getSerializableExtra("mcontext");
                TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
                PhoneStateListener listener=new PhoneStateListener(){
                    @Override
                    public void onCallStateChanged(int state, String incomingNumber) {
                        super.onCallStateChanged(state, incomingNumber);
                        switch (state) {
                            case TelephonyManager.CALL_STATE_RINGING:{
                                AudioUtils.getInstance().init(mcontext);
                                AudioUtils.getInstance().speakText(incomingNumber);
                            }
                        }
                    }
                };
                tm.listen(listener,PhoneStateListener.LISTEN_CALL_STATE);
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
        stopSelf();
        return START_NOT_STICKY;
    }
}
