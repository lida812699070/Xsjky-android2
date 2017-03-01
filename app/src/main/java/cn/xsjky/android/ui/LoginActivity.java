package cn.xsjky.android.ui;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Proxy;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.HashSet;
import java.util.Set;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import cn.xsjky.android.BaseApplication;
import cn.xsjky.android.BaseSettings;
import cn.xsjky.android.R;
import cn.xsjky.android.http.Urls;
import cn.xsjky.android.model.Login;
import cn.xsjky.android.model.LoginInfo;
import cn.xsjky.android.model.UpDataInfo;
import cn.xsjky.android.util.ClientVersion;
import cn.xsjky.android.util.FileUtil;
import cn.xsjky.android.util.LogU;
import cn.xsjky.android.util.PermissionUtil;
import cn.xsjky.android.util.RetrueCodeHandler;
import cn.xsjky.android.util.SPUtils;
import cn.xsjky.android.util.StrKit;
import cn.xsjky.android.util.VersionUpDataXmlPaser;
import cn.xsjky.android.util.XmlParserUtil;
import cn.xsjky.android.weiget.LoadingDialog;

/**
 * 登录界面
 *
 * @author Jerry
 */
public class LoginActivity extends BaseActivity {
    private EditText mUsername;
    private EditText mPassword;
    private Button mLogin;
    private TextView mInfo;
    private Login mLoginModel;
    private TextView tvVersions;
    private String apkUrl;
    private String device_id;
    private String username;
    private String password;
    private String versionName;
    private UpDataInfo upDataInfo;
    private String authinfo;
    private ImageView ivBg;
    private File f;
    private String version;
    private Button btnFrogetPsw;
    private static final int REQUEST_CONTACTS = 1;
    private static final String[] PERMISSIONS_CONTACT = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_PHONE_STATE};
    private String mNetworkName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //apkUrl = "http://develope.xsjky.cn/AppUpdateFiles/xsj.apk.apk";
        //upDataMessage = "在接收请求中加入了推送功能";
        String isDevelope = null;
        String strVersion = BaseSettings.WEBSERVICE_URL;
        if (strVersion.equals("http://develope.xsjky.cn/LogisticsServer.asmx")) {
            isDevelope = "测试版";
        } else {
            isDevelope = "正式版";
        }
        //initLocationTime();
        try {
            version = getPackageManager().getPackageInfo(
                    getPackageName(), 0).versionName;
            ClientVersion.ClientVersion = version + ".0";
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        long usableStorage = getUsableStorage();
        LogU.e(usableStorage + "");
        BaseApplication.isDevrlop = isDevelope;
        this.setContentView(R.layout.activity_login);
        mLoginModel = new Login();
        mUsername = (EditText) this.findViewById(R.id.login_username);
        mPassword = (EditText) this.findViewById(R.id.login_password);
        tvVersions = (TextView) findViewById(R.id.tv_login_versions);
        tvVersions.setText(isDevelope + version);
        mLogin = (Button) this.findViewById(R.id.login_btn);
        mInfo = (TextView) this.findViewById(R.id.login_info);
        btnFrogetPsw = (Button) findViewById(R.id.btnForgetPassword);
        btnFrogetPsw.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, FrogetPasswordActivity.class));
            }
        });
        SharedPreferences sp = getSharedPreferences("login", Context.MODE_PRIVATE);
        String data = sp.getString("data", "");
        String user = sp.getString("loginName", "");
        ivBg = (ImageView) findViewById(R.id.iv_welcome);
        mUsername.setText(user);
        mPassword.setText(data);
        /*
        mLoginModel.setUserId(sp.getInt("userId", 0));
		mLoginModel.setSessionId(sp.getString("sessionId", ""));
		BaseApplication.getApplication().setLogin(mLoginModel);
		Intent intent = new Intent(LoginActivity.this, MainActivity.class);
		LoginActivity.this.startActivity(intent);
		LoginActivity.this.finish();
		*/
        mLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                username = mUsername.getText().toString();
                password = mPassword.getText().toString();
                if (StrKit.isBlank(username) || StrKit.isBlank(password)) {
                    setInfo(SHOW_INFO, "用户名或密码不能为空!");
                    return;
                }
                if (Build.VERSION.SDK_INT >= 23) {
                    showContacts();
                } else {
                    login();
                }
            }
        });
        if (TextUtils.isEmpty(data) || TextUtils.isEmpty(user)) {
            ivBg.setVisibility(View.GONE);
        } else {
            mLogin.performClick();
        }

    }

    public void showContacts() {

        // Verify that all required contact permissions have been granted.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            // Contacts permissions have not been granted.
            ActivityCompat.requestPermissions(LoginActivity.this, PERMISSIONS_CONTACT, REQUEST_CONTACTS);//1是回调码switch中判断
        } else {
            login();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        boolean isScuess = true;
        for (int grantResult : grantResults) {
            if (grantResult == -1) {
                isScuess = false;
                break;
            }
        }
        if (requestCode == REQUEST_CONTACTS && isScuess) {
            //是否同意了权限
            if (grantResults.length == PERMISSIONS_CONTACT.length) {
//同意
                login();
            } else {
//拒绝 已经拒绝过也走这边
                Toast.makeText(this, "请同意相关权限避免以后功能出现问题", 1).show();
            }
        } else {
            Toast.makeText(this, "请同意相关权限避免以后功能出现问题", 1).show();
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void initLocationTime() {
        SharedPreferences sp = this.getSharedPreferences("time", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putLong("time", 0);
        edit.commit();

        SharedPreferences sp2 = this.getSharedPreferences("time2", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit2 = sp2.edit();
        edit2.putLong("time2", 0);
        edit2.commit();
    }

    private void login() {
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        device_id = tm.getDeviceId();
        LogU.e(device_id);
        showProgressDialog();
        String url = Urls.AppLogin;
        RequestParams params = new RequestParams();
        params.addBodyParameter("loginName", username);
        params.addBodyParameter("password", password);
        params.addBodyParameter("clientName", BaseSettings.CLIENT_NAME);
        params.addBodyParameter("clientVersion", ClientVersion.ClientVersion);
        params.addBodyParameter("deviceId", device_id);
        BaseApplication.mHttpUtils.send(HttpRequest.HttpMethod.POST,
                url,
                params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        closeProgressDialog();
                        String data = responseInfo.result;
                        if (StrKit.isBlank(data)) {
                            setInfo(SHOW_INFO, "获取数据失败!");
                            return;
                        }
                        XmlParserUtil parser = XmlParserUtil.getInstance();
                        parser.parse(data);
                        String code = parser.getNodeValue("/GetValueResultOfLoginSession/ReturnCode");
                        if (StrKit.notBlank(code) && code.equals("0")) {
                            SharedPreferences sp = getSharedPreferences("login", Context.MODE_PRIVATE);
                            SharedPreferences.Editor edit = sp.edit();
                            edit.putString("loginName", username);
                            edit.putString("data", password);
                            String userid = parser.getNodeValue("/GetValueResultOfLoginSession/Value/UserId");
                            String sessionid = parser.getNodeValue("/GetValueResultOfLoginSession/Value/SessionId");
                            String extraInfo = parser.getNodeValue("/GetValueResultOfLoginSession/Value/ExtraInfo");
                            getCarNum(extraInfo);
                            LogU.e(extraInfo + "");
                            LogU.e("sessionid", sessionid);
                            mLoginModel.setUserId(Integer.valueOf(userid));
                            mLoginModel.setSessionId(sessionid);
                            mLoginModel.setClientName(BaseSettings.CLIENT_NAME);
                            mLoginModel.save();
                            BaseApplication.getApplication().setLogin(mLoginModel);
                            LoginInfo loginInfo = new LoginInfo();
                            loginInfo.setSessionId(sessionid);
                            loginInfo.setUserId(Integer.valueOf(userid));
                            edit.putString("sessionid", sessionid);
                            edit.putString("userid", userid);
                            edit.commit();
                            BaseApplication.loginInfo = loginInfo;
                            getCurrVersion();
                            //intoMainActivity();
                        } else {
                            String error = parser.getNodeValue("GetValueResultOfLoginSession/Error");
                            setInfo(SHOW_INFO, error);
                            ivBg.setVisibility(View.GONE);
                        }
                        setInfo(HIDE_LOADING, "");
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        Toast.makeText(LoginActivity.this, "数据请求错误", Toast.LENGTH_SHORT).show();
                        ivBg.setVisibility(View.GONE);
                        closeProgressDialog();
                    }
                });
    }

    private void getCarNum(String extraInfo) {
        if (TextUtils.isEmpty(extraInfo))
            return;
        try {
            JSONObject jsonObject = new JSONObject(extraInfo);

            String city = jsonObject.getString("City");
            mNetworkName = jsonObject.getString("NetworkName");
            BaseApplication.getApplication().setCity(city);
            String truckNumber = jsonObject.getString("TruckNumber");
            if (!TextUtils.isEmpty(truckNumber)) {
                BaseApplication.userBindTool = truckNumber;
            }else {
                BaseApplication.userBindTool="";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getCurrVersion() {
        showProgressDialog();
        final LoginInfo loginInfo = BaseApplication.loginInfo;
        RequestParams params = new RequestParams();
        params.addBodyParameter("sessionId", loginInfo.getSessionId());
        params.addBodyParameter("userId", loginInfo.getUserId() + "");
        params.addBodyParameter("clientName", BaseSettings.CLIENT_NAME);
        params.addBodyParameter("appName", BaseSettings.APP_NAME);
        String url = Urls.GetLatestVersion;
        BaseApplication.mHttpUtils.send(HttpRequest.HttpMethod.POST,
                url,
                params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        //LogU.e(responseInfo.result);
                        SAXParserFactory factory = SAXParserFactory.newInstance();
                        SAXParser parser = null;
                        PackageInfo packageInfo = null;
                        try {
                            parser = factory.newSAXParser();
                            RetrueCodeHandler handler = new RetrueCodeHandler();
                            ByteArrayInputStream tInputStringStream = new ByteArrayInputStream(responseInfo.result.getBytes());
                            parser.parse(tInputStringStream, handler);
                            if (handler.getString().equals("0")) {
                                SAXParserFactory factory1 = SAXParserFactory.newInstance();
                                SAXParser parser1 = factory1.newSAXParser();
                                VersionUpDataXmlPaser handler1 = new VersionUpDataXmlPaser();
                                ByteArrayInputStream tInputStringStream1 = new ByteArrayInputStream(responseInfo.result.getBytes());
                                parser1.parse(tInputStringStream1, handler1);
                                upDataInfo = handler1.getUpDataInfo();
                            }
                            packageInfo = getPackageManager().getPackageInfo(
                                    getPackageName(), 0);
                            if (packageInfo != null && upDataInfo != null) {
                                versionName = packageInfo.versionName;
                                //Toast.makeText(LoginActivity.this,versionName,Toast.LENGTH_SHORT).show();
                                String appVersion = upDataInfo.getAppVersion();
                                Double myAppVersion = Double.valueOf(versionName.replace(".", "") + 00);
                                Double currAppVersion = Double.valueOf(appVersion.replace(".", ""));
                                LogU.e(versionName + "" + currAppVersion);
                                if (myAppVersion < currAppVersion) {
                                    showUpdataVersion();
                                } else {
                                    intoMainActivity();
                                }

                            } else {
                                intoMainActivity();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            closeProgressDialog();
                        }
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        Toast.makeText(LoginActivity.this, "数据获取失败", Toast.LENGTH_SHORT).show();
                        closeProgressDialog();
                    }
                });
    }

    private void intoMainActivity() {

        if (isFinishing()) {
            return;
        }

        initJpush();

        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        LoginActivity.this.startActivity(intent);
        LoginActivity.this.finish();
    }

    private void initJpush() {
        Boolean JpushIsRes = (boolean) SPUtils.get(getApplicationContext(), BaseApplication.loginInfo.getUserId() + "", false);
        if (JpushIsRes) {
            boolean pushStopped = JPushInterface.isPushStopped(getApplication());
            if (pushStopped) {
                JPushInterface.resumePush(getApplicationContext());
            }
            LogU.e("已经注册极光推送");
            return;
        }
        //注册极光服务器的tag
        HashSet<String> tags = new HashSet<>();
        tags.add(BaseSettings.TAG_EMPLOYEE);
        LogU.e(mNetworkName);
        if (!TextUtils.isEmpty(mNetworkName))
            tags.add(mNetworkName);
        //TODO 注册极光的标签和别名
        if (TextUtils.isEmpty(BaseApplication.userBindTool))
            tags.add(BaseSettings.TAG_SALESMAN);
        else
            tags.add(BaseSettings.TAG_DRIVER);
        String alias = "XSJ" + BaseApplication.loginInfo.getUserId();
        JPushInterface.setAliasAndTags(getApplicationContext(), alias, tags, new TagAliasCallback() {
            @Override
            public void gotResult(int code, String s, Set<String> set) {
                //注册成功
                if (code == 0) {
                    SPUtils.put(getApplicationContext(), BaseApplication.loginInfo.getUserId() + "", true);
                }
            }
        });
    }

    public static void delFile(String fileName) {
        File file = new File("/sdcard/" + fileName);
        if (file.isFile()) {
            file.delete();
        }
        file.exists();
    }

    private boolean isDownFinish(String file) {
        f = new File(file);
        if (f.exists()) {
            return true;
        } else {
            return false;
        }
    }

    private void showUpdataVersion() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);  //先得到构造器
        builder.setTitle("检测到版本更新"); //设置标题
        builder.setMessage("是否更新?"); //设置内容
        builder.setIcon(R.drawable.ic_launcher);//设置图标，图片id即可

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() { //设置确定按钮
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                Uri uri = Uri.parse("http://a.vmall.com/app/C10560700");
                Intent it = new Intent(Intent.ACTION_VIEW, uri);
                it.setData(uri);
                it.setAction(Intent.ACTION_VIEW);
                it.setClassName("com.android.browser", "com.android.browser.BrowserActivity");
                LoginActivity.this.startActivity(it);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() { //设置取消按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                intoMainActivity();
            }
        });
        //参数都设置完成了，创建并显示出来
        builder.create().show();
    }

    private ProgressDialog progressDialog;

    private void closeProgressDialogUpData() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    /*返回SD卡可用容量*/
    private static long getUsableStorage() {
        String sDcString = android.os.Environment.getExternalStorageState();

        if (sDcString.equals(android.os.Environment.MEDIA_MOUNTED)) {
            File pathFile = android.os.Environment
                    .getExternalStorageDirectory();

            android.os.StatFs statfs = new android.os.StatFs(pathFile.getPath());

            // 获取可供程序使用的Block的数量
            long nAvailaBlock = statfs.getAvailableBlocks();

            long nBlocSize = statfs.getBlockSize();

            // 计算 SDCard 剩余大小MB
            return nAvailaBlock * nBlocSize / 1024 / 1024;
        } else {
            return -1;
        }
    }

    private void showProgressDialogUpData() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setIcon(R.drawable.ic_launcher);
        progressDialog.setTitle("正在下载新版本...");
        progressDialog.setMessage("更新内容：" + upDataInfo.getUpdateDescription());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);//设置进度条对话框//样式（水平，旋转）
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

    public static String getFileMD5(File file) {
        if (!file.isFile()) {
            return null;
        }
        MessageDigest digest = null;
        FileInputStream in = null;
        byte buffer[] = new byte[1024];
        int len;
        try {
            digest = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            while ((len = in.read(buffer, 0, 1024)) != -1) {
                digest.update(buffer, 0, len);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        BigInteger bigInt = new BigInteger(1, digest.digest());
        return bigInt.toString(32);
    }

    private void setInfo(int code, String info) {
        Message msg = new Message();
        msg.what = code;
        msg.obj = info;
        handler.sendMessage(msg);
    }

    private final int SHOW_LOADING = 1;
    private final int HIDE_LOADING = 2;
    private final int SHOW_INFO = 3;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW_LOADING:
                    LoadingDialog.show(LoginActivity.this, false, false);
                    break;
                case HIDE_LOADING:
                    LoadingDialog.dismiss();
                    break;
                case SHOW_INFO:
                    mInfo.setText((String) msg.obj);
                    break;
            }
        }
    };


}
