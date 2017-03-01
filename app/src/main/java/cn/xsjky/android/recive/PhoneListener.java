package cn.xsjky.android.recive;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.util.Log;

import cn.xsjky.android.service.GPSService;
import cn.xsjky.android.util.LogU;

/**
 * Created by ${lida} on 2017/3/1.
 */
//监听网络状态  断网了就断掉定位  有网就开启定位
public class PhoneListener extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.PHONE_STATE")) {
            TelephonyManager tm = (TelephonyManager) context
                    .getSystemService(Service.TELEPHONY_SERVICE);
            switch (tm.getCallState()) {
                case TelephonyManager.CALL_STATE_RINGING:
                    Log.e("hg", "电话状态……RINGING");
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    Log.e("hg", "电话状态……OFFHOOK");

                    context.stopService(new Intent(context, GPSService.class));
                    break;
                case TelephonyManager.CALL_STATE_IDLE:
                    Log.e("hg", "电话状态……IDLE");
                    context.startService(new Intent(context, GPSService.class));
                    break;
            }
        }
    }
}
