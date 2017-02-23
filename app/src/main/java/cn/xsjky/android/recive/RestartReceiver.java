package cn.xsjky.android.recive;

import android.app.ActivityManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.List;

import cn.xsjky.android.BaseApplication;
import cn.xsjky.android.BaseSettings;
import cn.xsjky.android.http.Urls;
import cn.xsjky.android.protectServce.protectService;
import cn.xsjky.android.service.GPSService;
import cn.xsjky.android.util.ClientVersion;
import cn.xsjky.android.util.LogU;
import cn.xsjky.android.util.OnClickutils;
import cn.xsjky.android.util.StrKit;
import cn.xsjky.android.util.XmlParserUtil;

/**
 * Created by ${lida} on 2016/6/11.
 */
public class RestartReceiver extends BroadcastReceiver {
    private Context context;
    private String device_id;
    private HttpUtils mHttpUtils;
    private Intent intent1;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        init(context, intent);
        RestartService(context, intent);
    }

    private synchronized void RestartService(Context context, Intent intent) {
        if (isServiceRunning(context, "cn.xsjky.android.service.GPSService")) {
            context.stopService(intent);
            context.stopService(new Intent(context, protectService.class));
        }
        LogU.e("通过广播重启定位服务--------------------");
        loginAndRestart();
    }

    private void init(Context context, Intent intent) {
        if (intent1 == null) {
            intent1 = new Intent(context, GPSService.class);
            intent.setFlags(Service.START_STICKY);
        }
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

    PhoneStateListener listener = new PhoneStateListener() {

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
//注意，方法必须写在super方法后面，否则incomingNumber无法获取到值。
            super.onCallStateChanged(state, incomingNumber);
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE:
                    if (OnClickutils.isFastDoubleClick()) {
                        return;
                    }
                    System.out.println("挂断");
                    if (!isServiceRunning(context, "cn.xsjky.android.service.GPSService")) {
                        context.startService(intent1);
                    }
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    if (OnClickutils.isFastDoubleClick()) {
                        return;
                    }
                    System.out.println("接听");
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    if (OnClickutils.isFastDoubleClick()) {
                        return;
                    }
                    System.out.println("响铃:来电号码" + incomingNumber);
//输出来电号码
                    break;
            }
        }
    };

    protected void configUtils() {

        mHttpUtils = new HttpUtils();//最好整个应用一个实例
//设置线程池数量
        mHttpUtils.configRequestThreadPoolSize(4);

//设置请求重试次数
        mHttpUtils.configRequestRetryCount(3);

//设置响应编码
        mHttpUtils.configResponseTextCharset("utf-8");

//设置请求超时时间
        mHttpUtils.configTimeout(30000);

    }

    private boolean IsRuning = false;

    private void loginAndRestart() {
        if (IsRuning) {
            return;
        }
        if (BaseApplication.userBindTool==null)
            return;
        IsRuning = true;
        SharedPreferences sp = context.getSharedPreferences("login", Context.MODE_PRIVATE);
        String data = sp.getString("data", "");
        String user = sp.getString("loginName", "");
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        device_id = tm.getDeviceId();
        String url = Urls.AppLogin;
        RequestParams params = new RequestParams();
        params.addBodyParameter("loginName", user);
        params.addBodyParameter("password", data);
        params.addBodyParameter("clientName", BaseSettings.CLIENT_NAME);
        params.addBodyParameter("clientVersion", ClientVersion.ClientVersion);
        params.addBodyParameter("deviceId", device_id);
        if (mHttpUtils == null) {
            configUtils();
        }
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
                            String sessionid = parser.getNodeValue("/GetValueResultOfLoginSession/Value/SessionId");
                            String userid = parser.getNodeValue("/GetValueResultOfLoginSession/Value/UserId");
                            SharedPreferences sp = context.getSharedPreferences("login", Context.MODE_PRIVATE);
                            SharedPreferences.Editor edit = sp.edit();
                            edit.putString("sessionid", sessionid);
                            edit.putString("userid", userid);
                            edit.commit();
                            if (GPSService.loginInfo != null)
                                GPSService.loginInfo.setSessionId(sessionid);
                            context.startService(new Intent(context, GPSService.class));
                            Toast.makeText(context, "登录成功,请继续之前的操作", Toast.LENGTH_SHORT).show();
                        }
                        IsRuning = false;
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        Toast.makeText(context, "登录失败", Toast.LENGTH_SHORT).show();
                        IsRuning = false;
                    }
                });
    }

}
