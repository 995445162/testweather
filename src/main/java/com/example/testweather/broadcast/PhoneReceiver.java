package com.example.testweather.broadcast;

import android.app.IntentService;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;

import com.example.testweather.service.CallService;

import java.io.Serializable;

/**
 * Created by tujianhua on 2017/11/25.
 */

public class PhoneReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {

        }else {
            Intent intent1 = new Intent(context, CallService.class);
            intent.putExtra("mcontext", (Serializable) context);
            context.startService(intent1);
        }
    }
}

