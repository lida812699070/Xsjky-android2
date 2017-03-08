package cn.xsjky.android.service;

import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.List;

import cn.xsjky.android.BaseApplication;
import cn.xsjky.android.BaseSettings;
import cn.xsjky.android.R;
import cn.xsjky.android.http.Urls;
import cn.xsjky.android.model.LoginInfo;
import cn.xsjky.android.protectServce.protectService;
import cn.xsjky.android.ui.LoginActivity;
import cn.xsjky.android.ui.MainActivity;
import cn.xsjky.android.util.LogU;
import cn.xsjky.android.util.RetrueCodeHandler;
import cn.xsjky.android.util.RetruenUtils;

/**
 * Created by OK on 2016/3/29.
 */
public class GPSService extends Service {

    public static final String STATICACTION = "cn.xsjky.android.restartReceiver";
    private LocationClient mLocationClient;
    private double longitude;
    private double latitude;
    public static BDLocation location = null;
    private LocationClientOption option;
    private HttpUtils mHttpUtils;
    private long currentTimeMillis;
    private long lastTimeMillis;
    public static LoginInfo loginInfo;
    private String device_id;
    private Date date;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        if (mLocationClient != null) {
            mLocationClient.stop();
        }
        if (mHttpUtils != null) {
            mHttpUtils = null;
        }
        super.onDestroy();
        //releaseWakeLock();
        writeFileToSD("onDestroy");
        //newnotifition();
        if (mPlayer != null) {
            mPlayer = null;
        }
    }

    @Override
    public void onCreate() {
        loginInfo = BaseApplication.loginInfo;
        SharedPreferences sp = GPSService.this.getSharedPreferences("time", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putLong("time", 0);
        edit.commit();

        SharedPreferences sp2 = GPSService.this.getSharedPreferences("time2", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit2 = sp2.edit();
        edit2.putLong("time2", 0);
        edit2.commit();
        acquireWakeLock();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mLocationClient != null && mLocationClient.isStarted()) {
            writeFileToSD("mLocationClient stop!-----");
            mLocationClient.stop();
        }
        configUtils();
        mLocationClient = new LocationClient(getApplicationContext());     //声明LocationClient类
        mLocationClient.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation location) {

                if (!MainActivity.isServiceRunning(GPSService.this, protectService.STATICACTION)) {
                    startService(new Intent(GPSService.this, protectService.class));
                    writeFileToSD("保护重启");
                }
                GPSService.location = location;
                //city = location.getCity();
                //province = location.getProvince();
                longitude = location.getLongitude();
                latitude = location.getLatitude();
                //LogU.e("定位成功" + latitude + "--" + longitude);
                try {
                    getRemoteInfo2();
                } catch (Exception e) {
                    writeFileToSD("Exception " + e.getMessage());
                }
                //getRemoteInfoVolley();
                writeFileToSD("定位成功：" + latitude + "----" + longitude + "---" + new Date().getHours() + ":" + new Date().getMinutes());
            }
        });
        initLocation();
        NotificationManager nm = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentTitle("新世纪");
        builder.setContentText("后台定位进程。。。");
        builder.setSmallIcon(R.drawable.ic_launcher);
        Notification notification = builder.getNotification();
        startForeground(0x111, notification);
        mLocationClient.start();
        flags = START_STICKY;
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 根据两点间经纬度坐标（double值），计算两点间距离，
     *
     * @return 距离：单位为米
     */
    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    private static final double EARTH_RADIUS = 6378137;

    public static double DistanceOfTwoPoints(double lat1, double lng1,
                                             double lat2, double lng2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(radLat1) * Math.cos(radLat2)
                * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s;
    }

    private void initLocation() {
        option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        option.setScanSpan(2 * 60 * 1000);
        // option.setLocationMode(LocationClientOption.LocationMode.Battery_Saving);//低功耗模式开启定位
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(false);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(false);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(false);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }

    private Boolean isLog = false;

    private void writeFileToSD(String call) {
        LogU.e(call);
        if (!isLog) {
            return;
        }
        call = call + "/\n";
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
            Log.d("TestFile", "SD card is not avaiable/writeable right now.");
            return;
        }

        try {
            String pathName = "/sdcard/log/";
            String fileName = "file.txt";
            File path = new File(pathName);
            File file = new File(pathName + fileName);
            if (!path.exists()) {
                Log.d("TestFile", "Create the path:" + pathName);
                path.mkdir();
            }
            if (!file.exists()) {
                Log.d("TestFile", "Create the file:" + fileName);
                file.createNewFile();
            }
            FileOutputStream stream = new FileOutputStream(file, true);
            byte[] buf = call.getBytes();
            stream.write(buf);
            stream.close();

        } catch (Exception e) {
            Log.e("TestFile", "Error on writeFilToSD.");
            e.printStackTrace();
        }
    }

    protected void configUtils() {

        mHttpUtils = new HttpUtils();//最好整个应用一个实例
//设置线程池数量
        mHttpUtils.configRequestThreadPoolSize(5);

//设置请求重试次数
        mHttpUtils.configRequestRetryCount(5);

//设置响应编码
        mHttpUtils.configResponseTextCharset("utf-8");

//设置请求超时时间
        mHttpUtils.configTimeout(60000);
        mHttpUtils.configSoTimeout(60000);


    }

    boolean isFirst = true;

    public void getRemoteInfo2() {
        currentTimeMillis = System.currentTimeMillis();
        String strUrl = Urls.strUrl;
        if (loginInfo == null) {
            writeFileToSD("loginInfo=null");
            setLogin();
        }
        SharedPreferences sp = GPSService.this.getSharedPreferences("time", Context.MODE_PRIVATE);
        time = sp.getLong("time", 0);
        if (time != 0 && System.currentTimeMillis() - time < 60 * 1000) {
            writeFileToSD("time<60000");
            return;
        }
        SharedPreferences.Editor edit = sp.edit();
        edit.putLong("time", System.currentTimeMillis());
        edit.commit();
        time = System.currentTimeMillis();
        writeFileToSD("requestParamsing...");
        RequestParams requestParams = new RequestParams();
        requestParams.addBodyParameter("sessionId", loginInfo.getSessionId());
        requestParams.addBodyParameter("userId", loginInfo.getUserId() + "");
        requestParams.addBodyParameter("RoleData", 0 + "");
        requestParams.addBodyParameter("clientName", BaseSettings.CLIENT_NAME);
        requestParams.addBodyParameter("longitude", longitude + "");
        requestParams.addBodyParameter("latitude", latitude + "");
        requestParams.addBodyParameter("province", location.getProvince());
        requestParams.addBodyParameter("city", location.getCity());
        requestParams.addBodyParameter("district", location.getDistrict());
        requestParams.addBodyParameter("address", location.getAddrStr());
        date = new Date(System.currentTimeMillis());
        writeFileToSD("requestParams finished...------" + date.getHours() + "--" + date.getMinutes());
        if (callBack == null) {
            initcallback();
        }
        mHttpUtils.send(HttpRequest.HttpMethod.POST, strUrl, requestParams, callBack);
    }

    private void initcallback() {
        callBack = new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                writeFileToSD("onSuccess");
                try {
                    RetrueCodeHandler returnInfo = RetruenUtils.getReturnInfo(responseInfo.result, new RetrueCodeHandler());
                    if (returnInfo != null && returnInfo.getString().equals("0")) {
                        SharedPreferences sp = GPSService.this.getSharedPreferences("time2", Context.MODE_PRIVATE);
                        SharedPreferences.Editor edit = sp.edit();
                        edit.putLong("time2", System.currentTimeMillis());
                        edit.commit();
                        writeFileToSD("xutils成功");
                        if (locationIntface != null) {
                            locationIntface.locationStatus(true, "");
                        }
                    } else if (returnInfo != null && returnInfo.getString().equals("-1")) {
                        writeFileToSD("xutils  -1   " + responseInfo.result);
                        //sendBroadcast();
                        stopSelf();
                        if (locationIntface != null) {
                            locationIntface.locationStatus(false, "");
                        }

                       /* if (returnInfo.getError().contains("已在其他终端登录")) {
                            if (locationIntface != null) {
                                locationIntface.locationStatus(false, "其他终端登录");
                            }
                            newnotifition();
                            GPSService.this.stopService(new Intent(GPSService.this, GPSService.class));
                            return;
                        }*/

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    writeFileToSD("catch=" + responseInfo.result);
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                if (error.getMessage().equals("Internal Server Error")) {
                    return;
                }
                writeFileToSD("xutils---error----" + error.getMessage());
                writeFileToSD(Urls.strUrl + "---" + loginInfo.toString() + "---" + locationIntface.toString() + "---" + location.toString());
                locationIntface.locationStatus(false, "");
                wakeUpAndUnlock(GPSService.this);
                if (time != 0 && System.currentTimeMillis() - time > 6 * 60 * 1000) {
                    newnotifition();
                    writeFileToSD("time >  6000*60------超时未连接");
                    stopSelf();
                }
                /*ConnectivityManager con=(ConnectivityManager)getSystemService(Activity.CONNECTIVITY_SERVICE);
                boolean wifi=con.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
                boolean internet=con.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();
                if(wifi|internet){
                    newnotifition();
                }else{
                    newnotifition();
                }*/
            }
        };
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
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //锁屏
                kl.reenableKeyguard();
            }
        }, 5000);
    }

    private RequestCallBack callBack;

    private void setLogin() {
        try {
            SharedPreferences sp = getSharedPreferences("login", Context.MODE_PRIVATE);
            String sessionid = sp.getString("sessionid", "");
            String userid = sp.getString("userid", "");
            if (TextUtils.isEmpty(userid) || TextUtils.isEmpty(sessionid)) {
                stopSelf();
                return;
            }
            loginInfo=new LoginInfo();
            loginInfo.setUserId(Integer.valueOf(userid));
            loginInfo.setSessionId(sessionid);
        } catch (Exception e) {

        }

    }

    private MediaPlayer mPlayer = null;

    private void newnotifition() {
        player();
        LogU.e("player");
        Context context = GPSService.this;
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder mBuilder = new Notification.Builder(context);
        mBuilder.setTicker("定位已中断！");
        mBuilder.setSmallIcon(R.drawable.ic_launcher);
        mBuilder.setContentTitle("定位已中断");
        mBuilder.setContentText("定位已中断");
        //设置点击一次后消失（如果没有点击事件，则该方法无效。）
        mBuilder.setAutoCancel(true);
        mBuilder.setDefaults(Notification.DEFAULT_ALL);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        /*if (!isAppOnForeground(context)) {
            Intent resultIntent = new Intent(context, LoginActivity.class);
            stackBuilder.addParentStack(LoginActivity.class);
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent pIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(pIntent);
        }*/
        nm.notify(3, mBuilder.build());
    }

    private void player() {
        //音量控制,初始化定义
        final AudioManager mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
//最大音量
        int maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        final int currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume, 0);
        mPlayer = MediaPlayer.create(this, R.raw.my);
        mPlayer.setLooping(false);
        mPlayer.start();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume, 0);
            }
        }, 5000);
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

    /*private void loginAndRestart() {
        SharedPreferences sp = getSharedPreferences("login", Context.MODE_PRIVATE);
        String data = sp.getString("data", "");
        String user = sp.getString("loginName", "");
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        device_id = tm.getDeviceId();
        String url = Urls.AppLogin;
        RequestParams params = new RequestParams();
        params.addBodyParameter("loginName", user);
        params.addBodyParameter("password", data);
        params.addBodyParameter("clientName", BaseSettings.CLIENT_NAME);
        params.addBodyParameter("clientVersion", ClientVersion.ClientVersion);
        params.addBodyParameter("deviceId", device_id);
        mHttpUtils.send(HttpRequest.HttpMethod.POST,
                url,
                params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        String data = responseInfo.result;
                        XmlParserUtil parser = XmlParserUtil.getInstance();
                        parser.parse(data);
                        String code = parser.getNodeValue("/GetValueResultOfLoginSession/ReturnCode");
                        if (StrKit.notBlank(code) && code.equals("0")) {
                            SharedPreferences sp = getSharedPreferences("login", Context.MODE_PRIVATE);
                            SharedPreferences.Editor edit = sp.edit();
                            String userid = parser.getNodeValue("/GetValueResultOfLoginSession/Value/UserId");
                            String sessionid = parser.getNodeValue("/GetValueResultOfLoginSession/Value/SessionId");
                            loginInfo.setSessionId(sessionid);
                            loginInfo.setUserId(Integer.valueOf(userid));
                            edit.putString("sessionid", sessionid);
                            edit.putString("userid", userid);
                            edit.commit();
                            LogU.e("sessionid  重启", sessionid);
                            sendBroadcast();
                        } else {
                            LogU.e("重启服务---" + responseInfo.result);
                        }
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        LogU.e("sessionid  重启", "登录失败");
                        if ("Internal Server Error".equals(msg)) {
                            return;
                        }
                        newnotifition();
                    }
                });
    }*/

    public long time = 0;

    private void sendBroadcast() {
        Intent intent = new Intent();
        intent.setAction(STATICACTION);        //设置Action
        sendBroadcast(intent);
        stopSelf();
    }

    private static LocationIntface locationIntface;

    public static void setLoacationListener(LocationIntface locationIntface) {
        GPSService.locationIntface = locationIntface;
    }

    PowerManager.WakeLock wakeLock = null;

    //获取电源锁，保持该服务在屏幕熄灭时仍然获取CPU时，保持运行
    private void acquireWakeLock() {

        if (null == wakeLock) {
            PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
            wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK | PowerManager.ON_AFTER_RELEASE , "GPSService");
            if (null != wakeLock) {
                wakeLock.acquire();
            }
        }
    }


    //获取电源锁，保持该服务在屏幕熄灭时仍然获取CPU时，保持运行
    private void acquireWakeLock2() {
        if (null == wakeLock) {
            PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
            wakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ON_AFTER_RELEASE, "GPSService");
            if (null != wakeLock) {
                wakeLock.acquire();
            }
        }
    }

    //释放设备电源锁
    private void releaseWakeLock() {
        writeFileToSD("释放锁");
        if (null != wakeLock) {
            wakeLock.release();
            wakeLock = null;
        }
    }

}
