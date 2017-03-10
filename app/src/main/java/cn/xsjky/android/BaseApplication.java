package cn.xsjky.android;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.model.LatLng;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.HttpUtils;
import com.wanjian.cockroach.Cockroach;

import org.litepal.LitePalApplication;
import org.litepal.tablemanager.Connector;

import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import cn.jpush.android.api.JPushInterface;
import cn.xsjky.android.http.HttpPro;
import cn.xsjky.android.model.AddressBook;
import cn.xsjky.android.model.CityModel;
import cn.xsjky.android.model.CustomReceivers;
import cn.xsjky.android.model.Document;
import cn.xsjky.android.model.Employee;
import cn.xsjky.android.model.FindInProgressRequest;
import cn.xsjky.android.model.HandlerCoordinate;
import cn.xsjky.android.model.Login;
import cn.xsjky.android.model.LoginInfo;
import cn.xsjky.android.model.ProvinceModel;
import cn.xsjky.android.model.Recivicer;
import cn.xsjky.android.model.SendPeroson;
import cn.xsjky.android.util.XmlParserHandler;
import okhttp3.OkHttpClient;

public class BaseApplication extends LitePalApplication {
    public static HttpUtils mHttpUtils;
    public static HandlerCoordinate coordinate;
    public static Recivicer customReceivers;
    public static FindInProgressRequest progressRequest;
    public static SendPeroson customShipper;
    public static AddressBook shipBook;
    public static BDLocation Location;
    private static BaseApplication mBaseApplication;
    public static LoginInfo loginInfo;
    private String fromcity;
    public static LatLng latLng;
    private String toCity;
    public static OkHttpClient client;
    public static BitmapUtils mBitmapUtils;

    public String getToCity() {
        return toCity;
    }

    public void setToCity(String toCity) {
        this.toCity = toCity;
    }

    public String getCity() {
        return fromcity;
    }

    public void setCity(String city) {
        this.fromcity = city;
    }

    public static List<Activity> activityList = new LinkedList<Activity>();
    public static boolean isDebug = true;
    private HttpPro mHttpPro;
    private SharedPreferences mSharedPreferences;
    private Login mLogin;
    private Document mDocument;
    private Employee mEmployee;
    private BluetoothAdapter mBluetoothAdapter;
    private List<ProvinceModel> provinceList;
    private List<CityModel> cityList;
    public static String isDevrlop = null;
    private String lastDevice;
    public static String userOwnNetwork;
    public static String userBindTool;
    public static String cityName;
    public static DbUtils mdbUtils;
    public static Boolean printStatus = false;
    public static SQLiteDatabase db;

    @Override
    public void onCreate() {
        super.onCreate();
        Cockroach.install(new Cockroach.ExceptionHandler() {

            // handlerException内部建议手动try{  你的异常处理逻辑  }catch(Throwable e){ } ，以防handlerException内部再次抛出异常，导致循环调用handlerException

            @Override
            public void handlerException(final Thread thread, final Throwable throwable) {
                Log.e("AndroidRuntime", "--->CockroachException:" + thread + "<---", throwable);
            }
        });
        JPushInterface.setDebugMode(false);
        JPushInterface.init(getApplicationContext());
        mBaseApplication = this;
        mSharedPreferences = this.getSharedPreferences("xsjky", Context.MODE_PRIVATE);
        configUtils();
        createDb();
        SDKInitializer.initialize(this);
        db = Connector.getDatabase();
        initOkHttp();
        mBitmapUtils = new BitmapUtils(this);
        mBitmapUtils.configDefaultLoadingImage(R.drawable.ic_launcher);//默认背景图片
        mBitmapUtils.configDefaultBitmapConfig(Bitmap.Config.RGB_565);//设置图片压缩类型

    }

    public static void exic() {
        if (activityList.size() > 0) {
            for (Activity activitys : BaseApplication.activityList) {
                try {
                    activitys.finish();
                } catch (Exception e) {
                }
            }
        }
        System.exit(0);
    }

    private void initOkHttp() {
        client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .build();
    }

    public static boolean isServiceRunning(Context mContext, String className) {

        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager)
                mContext.getSystemService(Context.ACTIVITY_SERVICE);
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

    public static String getCurProcessName(Context context) {

        int pid = android.os.Process.myPid();

        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
                .getRunningAppProcesses()) {

            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

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

    protected void createDb() {
        mdbUtils = DbUtils.create(this);
    }

    public List<ProvinceModel> getProvinceList() {
        if (provinceList == null)
            parserProvince();
        return provinceList;
    }

    public List<CityModel> getCtiyList() {
        if (cityList == null)
            parserProvince();
        return cityList;
    }

    private void parserProvince() {
        AssetManager asset = this.getAssets();
        try {
            InputStream input = asset.open("province_data.xml");
            // 创建一个解析xml的工厂对象
            SAXParserFactory spf = SAXParserFactory.newInstance();
            // 解析xml
            SAXParser parser = spf.newSAXParser();
            XmlParserHandler handler = new XmlParserHandler();
            parser.parse(input, handler);
            input.close();
            provinceList = handler.getDataList();
            cityList = handler.getCityList();
        } catch (Throwable e) {

        }
    }

    public static BaseApplication getApplication() {
        return mBaseApplication;
    }

    public static void exitApp() {
        System.exit(0);
    }

    public HttpPro getHttpPro() {
        if (mHttpPro == null)
            mHttpPro = new HttpPro();

        return mHttpPro;
    }

    public BluetoothAdapter getmBluetoothAdapter() {
        return mBluetoothAdapter;
    }

    public void setmBluetoothAdapter(BluetoothAdapter mBluetoothAdapter) {
        this.mBluetoothAdapter = mBluetoothAdapter;
    }

    public Employee getEmployee() {
        return mEmployee;
    }

    public void setEmployee(Employee mEmployee) {
        this.mEmployee = mEmployee;
    }

    public Document getDocument() {
        return mDocument;
    }

    public void setDocument(Document mDocument) {
        this.mDocument = mDocument;
    }

    public Login getLogin() {
        return mLogin;
    }

    public void setLogin(Login login) {
        this.mLogin = login;
    }

    public String getLastDevice() {
        return mSharedPreferences.getString("lastDevice", "");
    }

    public void setLastDevice(String lastDevice) {
        Editor e = mSharedPreferences.edit();
        e.putString("lastDevice", lastDevice);
        e.commit();
    }

    public Map<String, String> getSecurityInfo() {
        Map<String, String> map = new HashMap<String, String>();
        if (loginInfo != null) {
            map.put("userId", String.valueOf(loginInfo.getUserId()));
            map.put("sessionId", loginInfo.getSessionId());
        }
        return map;
    }

    public SharedPreferences getSharedPreferences() {
        return mSharedPreferences;
    }
}
