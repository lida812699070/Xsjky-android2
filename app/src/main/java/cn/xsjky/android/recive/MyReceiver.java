package cn.xsjky.android.recive;

import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Log;


import java.util.List;

import cn.jpush.android.api.JPushInterface;
import cn.xsjky.android.R;
import cn.xsjky.android.service.GPSService;
import cn.xsjky.android.ui.LoginActivity;
import cn.xsjky.android.ui.MainActivity;
import cn.xsjky.android.util.LogU;

/**
 * Created by ${lida} on 2016/5/7.
 */
public class MyReceiver extends BroadcastReceiver {
    private static final String TAG = "JPush";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(500);
            String msg = bundle.getString(JPushInterface.EXTRA_EXTRA);
            LogU.e(msg);
            resultActivityBackApp(context, msg);

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            String msg = bundle.getString(JPushInterface.EXTRA_EXTRA);
            LogU.e(msg);
            Log.e(TAG, "[MyReceiver] 接收到推送下来的通知");

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            //TODO
            if (!isAppOnForeground(context)) {
                openNof(context);
            }
        }

        if (!MainActivity.isServiceRunning(context, "cn.xsjky.android.service.GPSService")) {
            SharedPreferences sp = context.getSharedPreferences("login", Context.MODE_PRIVATE);
            String sessionid = sp.getString("sessionid", "");
            String userid = sp.getString("userid", "");
            if (TextUtils.isEmpty(userid) || TextUtils.isEmpty(sessionid)) {
                return;
            }
            intent = new Intent(context, GPSService.class);
            intent.setFlags(Service.START_STICKY);
            context.startService(intent);
            LogU.e("重启服务");
        }

    }

    private void openNof(Context context) {
        Intent intent = new Intent();
        intent.setClass(context.getApplicationContext(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.getApplicationContext().startActivity(intent);
    }

    public void resultActivityBackApp(Context context, String text) {
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder mBuilder = new Notification.Builder(context);
        mBuilder.setTicker("自定义通知");
        mBuilder.setSmallIcon(R.drawable.ic_launcher);
        mBuilder.setContentTitle("自定义通知");
        mBuilder.setContentText(text);

        //设置点击一次后消失（如果没有点击事件，则该方法无效。）
        mBuilder.setAutoCancel(true);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        //点击通知之后需要跳转的页面
        if (!isAppOnForeground(context)) {
            Intent resultIntent = new Intent(context, LoginActivity.class);
            stackBuilder.addParentStack(LoginActivity.class);
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent pIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(pIntent);
        }

        //使用TaskStackBuilder为“通知页面”设置返回关系
        //为点击通知后打开的页面设定 返回 页面。（在manifest中指定）

        // mId allows you to update the notification later on.
        nm.notify(2, mBuilder.build());
    }

    private boolean isAppOnForeground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getApplicationContext().getSystemService(
                Context.ACTIVITY_SERVICE);
        String packageName = context.getApplicationContext().getPackageName();
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null)
            return false;
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }
        return false;
    }

    public static void wakeUpAndUnlock(Context context) {
        KeyguardManager km = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock kl = km.newKeyguardLock("unLock");
        //解锁
        kl.disableKeyguard();
        //获取电源管理器对象
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        //获取PowerManager.WakeLock对象,后面的参数|表示同时传入两个值,最后的是LogCat里用的Tag
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK, "bright");
        //点亮屏幕
        wl.acquire();
        //释放
        wl.release();
    }
}
