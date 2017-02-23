package cn.xsjky.android.protectServce;

import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.xsjky.android.R;
import cn.xsjky.android.service.GPSService;
import cn.xsjky.android.ui.LoginActivity;
import cn.xsjky.android.ui.MainActivity;
import cn.xsjky.android.util.LogU;

/**
 * Created by ${lida} on 2016/7/7.
 */
public class protectService extends Service {

    public static final String STATICACTION = "cn.xsjky.android.protectServce.protectService";
    private Timer timer;
    private long time;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static boolean isServiceRunning(Context mContext, String className) {

        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList
                = activityManager.getRunningServices(30);

        if (!(serviceList.size() > 0)) {
            return false;
        }

        for (int i = 0; i < serviceList.size(); i++) {
            if (serviceList.get(i).service.getClassName().equals(className) == true) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (timer != null) {
            timer.cancel();
        }
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                SharedPreferences sp = getSharedPreferences("time2", Context.MODE_PRIVATE);
                time = sp.getLong("time2", 0);
                if (!isServiceRunning(protectService.this, "cn.xsjky.android.service.GPSService")) {
                    restartApp(sp);
                    return;
                }
                if (time != 0 && System.currentTimeMillis() - time > 5 * 60 * 1000) {
                    restartApp(sp);
                }
            }
        }, 0, 45 * 1000);
        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentTitle("新世纪");
        builder.setContentText("检查定位进程。。。");
        builder.setSmallIcon(R.drawable.ic_launcher);
        Notification notification = builder.getNotification();
        flags = START_STICKY;
        startForeground(0x112, notification);
        return super.onStartCommand(intent, flags, startId);
    }

    private void restartApp(SharedPreferences sp) {
        stopService(new Intent(protectService.this, GPSService.class));
        //startAndExit();
        /*wakeUpAndUnlock(protectService.this);
        Intent dialogIntent = new Intent(getBaseContext(), LoginActivity.class);
        dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplication().startActivity(dialogIntent);
        SharedPreferences.Editor edit = sp.edit();
        edit.putLong("time2", System.currentTimeMillis());
        edit.commit();*/
    }

    protected void startAndExit() {
        Intent intent = new Intent(this, MainActivity.class);
        ComponentName cmpName = intent.resolveActivity(getPackageManager());
        boolean bIsExist = false;
        if (cmpName != null) { // 说明系统中存在这个activity
            ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> taskInfoList = am.getRunningTasks(10);
            for (ActivityManager.RunningTaskInfo taskInfo : taskInfoList) {
                if (taskInfo.baseActivity.equals(cmpName)) { // 说明它已经启动了
                    bIsExist = true;
                    break;
                }
            }
        }
        if (bIsExist) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("isExit", (Boolean) true); //让它自行关闭
            this.startActivity(intent);
        }
    }

    public static void wakeUpAndUnlock(Context context) {
        KeyguardManager km = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        final KeyguardManager.KeyguardLock kl = km.newKeyguardLock("unLock");
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

    private void sendBroadcast() {
        Intent intent = new Intent();
        intent.setAction("cn.xsjky.android.restartReceiver");        //设置Action
        sendBroadcast(intent);
        stopSelf();
    }

    @Override
    public void onDestroy() {
        LogU.e("保护关闭");
        if (timer != null) {
            timer.cancel();
        }
        super.onDestroy();
    }

    @Override
    public void onCreate() {
        LogU.e("保护开启");
        acquireWakeLock();
        super.onCreate();
    }

    PowerManager.WakeLock wakeLock = null;

    //获取电源锁，保持该服务在屏幕熄灭时仍然获取CPU时，保持运行
    private void acquireWakeLock() {

        if (null == wakeLock) {
            PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
            wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK | PowerManager.ON_AFTER_RELEASE, "protectService");
            if (null != wakeLock) {
                wakeLock.acquire();
            }
        }
    }

}
