package cn.xsjky.android.recive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import cn.xsjky.android.service.GPSService;
import cn.xsjky.android.util.LogU;

/**
 * Created by ${lida} on 2017/3/1.
 */
//监听网络状态  断网了就断掉定位  有网就开启定位
public class NetWorkListener extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        //获取移动网连接状态
        NetworkInfo mobileNetworkInfo = connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        //获取wifi连接状态
        NetworkInfo wifiNetworkInfo = connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (!mobileNetworkInfo.isConnected() && !wifiNetworkInfo.isConnected()) {
            LogU.e("断网了");
            context.stopService(new Intent(context, GPSService.class));
        } else {
            context.startService(new Intent(context, GPSService.class));
            LogU.e("有网了");
        }
    }

    /**
     * 获取当前的网络状态 ：没有网络0：WIFI网络1：3G网络2：2G网络3
     *
     * @param context
     * @return
     */
    private static int getAPNType(Context context) {
        int netType = 0;
        ConnectivityManager connMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo == null) {
            return netType;
        }
        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_WIFI) {
            netType = 1;// wifi
        } else if (nType == ConnectivityManager.TYPE_MOBILE) {
            int nSubType = networkInfo.getSubtype();
            TelephonyManager mTelephony = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            if (nSubType == TelephonyManager.NETWORK_TYPE_UMTS
                    && !mTelephony.isNetworkRoaming()) {
                netType = 2;// 3G
            } else {
                netType = 3;// 2G
            }
        }
        return netType;
    }
}
