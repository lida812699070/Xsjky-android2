package cn.xsjky.android.ui;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.jq.printer.esc.Text;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.litepal.crud.DataSupport;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import cn.jpush.android.api.JPushInterface;
import cn.xsjky.android.BaseApplication;
import cn.xsjky.android.BaseSettings;
import cn.xsjky.android.R;
import cn.xsjky.android.R.id;
import cn.xsjky.android.adapter.ItemLvDialogAdapter;
import cn.xsjky.android.adapter.ItemSimpleAdapterAdapter;
import cn.xsjky.android.constant.SoapAction;
import cn.xsjky.android.constant.SoapEndpoint;
import cn.xsjky.android.constant.SoapInfo;
import cn.xsjky.android.db.ErrorDocument;
import cn.xsjky.android.db.NewDocumentDataCache;
import cn.xsjky.android.http.HttpCallback;
import cn.xsjky.android.http.Urls;
import cn.xsjky.android.model.Custom;
import cn.xsjky.android.model.Customer;
import cn.xsjky.android.model.Document;
import cn.xsjky.android.model.Employee;
import cn.xsjky.android.model.FindInProgressRequest;
import cn.xsjky.android.model.Infos;
import cn.xsjky.android.model.LoginInfo;
import cn.xsjky.android.model.PrintDocumentInfo;
import cn.xsjky.android.model.ProgressRequestXmlParser;
import cn.xsjky.android.model.ReceiverStatData;
import cn.xsjky.android.protectServce.protectService;
import cn.xsjky.android.service.GPSService;
import cn.xsjky.android.service.LocationIntface;
import cn.xsjky.android.util.DataFormatUtils;
import cn.xsjky.android.util.DateFormatUtils;
import cn.xsjky.android.util.DateMonthTime;
import cn.xsjky.android.util.LogU;
import cn.xsjky.android.util.MySoap;
import cn.xsjky.android.util.NetworkDetector;
import cn.xsjky.android.util.OnClickutils;
import cn.xsjky.android.util.PermissionUtil;
import cn.xsjky.android.util.QueryCustomerXmlparser;
import cn.xsjky.android.util.ReceiverStatDataXmlParser;
import cn.xsjky.android.util.RetrueCodeHandler;
import cn.xsjky.android.util.RetruenUtils;
import cn.xsjky.android.util.SPUtils;
import cn.xsjky.android.util.StrKit;
import cn.xsjky.android.util.TempletUtil;
import cn.xsjky.android.util.XmlParserUtil;
import cn.xsjky.android.weiget.LoadingDialog;


//新增客户接口   和    一般定客户保存的订单查询
public class MainActivity extends BaseActivity {
    private static final String TAG = "TAg";
    private ImageView mFlip;
    private ImageView mAdd;
    private TextView mTitle;
    private Employee mEmployee;
    private TextView mTxt;
    private LinearLayout mApplyDocumentLayout;
    private Button mApplyDocumentBtn;
    private LinearLayout mDeliverableLayout;
    private Button mDeliverableBtn;
    //private Intent intent;
    private Button btnService;
    private TextView mServiceStatue;
    private Button mAcceptRequestBtn;
    private LinearLayout mAcceptRequestLayout;
    private String currVersion;
    private String apkUrl;
    private String upDataMessage;
    private boolean serviceRunning;
    private ListView lvLeft;
    private Intent intent;
    private static final int REQUEST_CONTACTS = 1;
    private static final String[] PERMISSIONS_CONTACT = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_PHONE_STATE,
    };
    private List<ErrorDocument> errorDocuments;
    private LinearLayout llErrorDocuments;
    private Button btnErrorDocuments;
    private ItemLvDialogAdapter<String> simpleAdapterAdapter;
    private ArrayList<String> listDoucmentNums;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private DrawerLayout drawLayout;
    private ArrayList<String> mLvLeftList;
    private ArrayAdapter<String> mMenuAdapter;
    private LinearLayout llMainContentUserBindTool;
    private LinearLayout llCustomManagerlayout;
    private LinearLayout llMainContentUnUserBindTool;
    private LinearLayout llSendQuestLayout;
    private FindInProgressRequest progressRequest;
    private LinearLayout llQuery;
    private ImageView mIvMenu;
    private TextView itemStatisticsPrice;
    private ImageView mItemStatisticsflash;
    private RotateAnimation mRotateAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        /*if (Build.VERSION.SDK_INT >= 23) {
            showContacts();
        } else {

        }*/
        init();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
       // client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void showContacts() {
        Log.e(TAG, "Show contacts button pressed. Checking permissions.");
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
            Log.e(TAG, "Contact permissions has NOT been granted. Requesting permissions.");
            requestContactsPermissions();
        } else {
            // Contact permissions have been granted. Show the contacts fragment.
            Log.e(TAG, "Contact permissions have already been granted. Displaying contact details.");
            init();
        }
    }

    private void requestContactsPermissions() {
        // BEGIN_INCLUDE(contacts_permission_request)
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                || ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                || ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                || ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                || ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_PHONE_STATE)
                ) {

            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // For example, if the request has been denied previously.
            Log.i(TAG,
                    "Displaying contacts permission rationale to provide additional context.");

            // Display a SnackBar with an explanation and a button to trigger the request.
          /*  Snackbar.make(v, "permission_contacts_rationale",
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction("ok", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ActivityCompat.requestPermissions(GetLocationActivity.this, PERMISSIONS_CONTACT,
                                    REQUEST_CONTACTS);
                        }
                    })
                    .show();*/
        } else {
            // Contact permissions have not been granted yet. Request them directly.
            ActivityCompat.requestPermissions(this, PERMISSIONS_CONTACT, REQUEST_CONTACTS);
        }
        // END_INCLUDE(contacts_permission_request)
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == REQUEST_CONTACTS) {
            if (PermissionUtil.verifyPermissions(grantResults)) {

                init();

            } else {
                Tos("不同意");
            }


        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void init() {
        intent = new Intent(MainActivity.this, GPSService.class);
        intent.setFlags(Service.START_STICKY);
        startLocationService();
        findViewById();
        setListener();
        GetCounts();
        //initTruck();
        //getCarNum();
        getNewWorkNum();
        queryCustomer(BaseApplication.getApplication().getCity(), 1);
        //统计今天运单
        initTodayDocument();

    }

    private void initTodayDocument() {
        String endTime = null;
        String firstDateForMonth = null;
        try {
            endTime = DataFormatUtils.getDate(System.currentTimeMillis());
            //firstDateForMonth = DateMonthTime.getBeforeFirstMonthdate(monthNumber);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assert firstDateForMonth != null;
        String url = Urls.GetReceiverStatData;
        RequestParams params = new RequestParams();
        params.addBodyParameter("sessionId", BaseApplication.loginInfo.getSessionId());
        params.addBodyParameter("userId", BaseApplication.loginInfo.getUserId() + "");
        params.addBodyParameter("clientName", BaseSettings.CLIENT_NAME);
        params.addBodyParameter("statUser", "0");
        params.addBodyParameter("period", "Day");
        params.addBodyParameter("beginDate", endTime);
        params.addBodyParameter("endDate", endTime);
        BaseApplication.mHttpUtils.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                RetrueCodeHandler parser = RetruenUtils.getReturnInfo(responseInfo.result, new RetrueCodeHandler());
                if (parser != null && parser.getString().equals("0")) {
                    ReceiverStatDataXmlParser returnInfo = RetruenUtils.getReturnInfo(responseInfo.result, new ReceiverStatDataXmlParser());
                    ArrayList<ReceiverStatData> receiverStatDatas = returnInfo.getDatas();
                    setdataToDay(receiverStatDatas);
                } else if (parser != null && parser.getString().equals("-1")) {
                    Toast.makeText(MainActivity.this, parser.getError(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "数据获取错误", Toast.LENGTH_SHORT).show();
                }
                stopreflash(true);

            }

            @Override
            public void onFailure(HttpException error, String msg) {
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                stopreflash(false);
            }
        });
    }

    //停止统计数据的刷新操作
    private static final int STOP_FRFLASH = 0;
    private Handler flashHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case STOP_FRFLASH:
                    mItemStatisticsflash.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.refresh));
                    mItemStatisticsflash.setClickable(true);
                    break;
            }
        }
    };

    //停止刷新动画
    private void stopreflash(boolean isSuccess) {
        if (mItemStatisticsflash != null && mRotateAnimation != null) {
            mItemStatisticsflash.clearAnimation();
            if (isSuccess){
                mItemStatisticsflash.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.success));
            }else {
                Tos("加载失败");
            }
            flashHandler.sendEmptyMessageDelayed(STOP_FRFLASH, 1000);
        }
    }

    private void setdataToDay(ArrayList<ReceiverStatData> receiverStatDatas) {

        if (receiverStatDatas == null || receiverStatDatas.size() == 0){
            Tos("未查询到今天的已签收的订单");
            return;
        }
        ReceiverStatData data = receiverStatDatas.get(0);
        itemStatisticsPrice.setText("今日累计运费：" + data.getTotalPremium()+"共"+data.getTicketCount()+"票");
    }

    private void queryCustomer(final String city, final int pageNumber) {
        LogU.e(pageNumber + "---");
        Boolean isExit = (Boolean) SPUtils.get(this, city, false);
        LogU.e("isExit=" + isExit);
        RequestParams params = new RequestParams();
        params.addBodyParameter("sessionId", BaseApplication.loginInfo.getSessionId());
        params.addBodyParameter("userId", BaseApplication.loginInfo.getUserId() + "");
        params.addBodyParameter("clientName", BaseSettings.CLIENT_NAME);
        params.addBodyParameter("pageNumber", pageNumber + "");
        params.addBodyParameter("pageSize", 50 + "");
        params.addBodyParameter("sortProperty", "");
        params.addBodyParameter("sortDecend", true + "");
        params.addBodyParameter("returnList", true + "");
        params.addBodyParameter("customerId", -1 + "");
        params.addBodyParameter("searchName", "");
        params.addBodyParameter("searchAddress", city);
        params.addBodyParameter("forbiddenFlag", "Disabled");
       /* String url=Urls.QueryCustomers+"?sessionId="+BaseApplication.loginInfo.getSessionId()+
                "&userId="+ BaseApplication.loginInfo.getUserId()+
                "&clientName="+ BaseSettings.CLIENT_NAME+
                "&pageNumber="+ pageNumber+
                "&pageSize="+ 10+
                "&sortProperty="+""+
                "&sortDecend="+ "true"+
                "&returnList="+ "true"+
                "&customerId="+ "-1"+
                "&searchName="+ ""+
                "&searchAddress="+ city;*/
        BaseApplication.mHttpUtils.send(HttpRequest.HttpMethod.POST, Urls.QueryCustomers,params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                QueryCustomerXmlparser customerXmlparser = RetruenUtils.getReturnInfo(responseInfo.result, new QueryCustomerXmlparser());
                StringXmlPaser stringXmlPaser = new StringXmlPaser();
                stringXmlPaser.setName("RecordCount");
                stringXmlPaser = RetruenUtils.getReturnInfo(responseInfo.result, stringXmlPaser);
                assert stringXmlPaser != null;
                if (customerXmlparser != null) {
                    List<Custom> list = customerXmlparser.getList();
                    //把没有的保存下来
                    Custom.updataListIntoDb(list);
                }
                String RecordCount = stringXmlPaser.getString();
                LogU.e(RecordCount + "---");
                if (Integer.valueOf(RecordCount) > (pageNumber) * 50) {
                    queryCustomer(city, pageNumber + 1);
                } else {
                    SPUtils.put(MainActivity.this, city, true);
                    SPUtils.put(MainActivity.this, "synTime", System.currentTimeMillis());
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                Tos(msg);
            }
        });
    }

    private void getNewWorkNum() {
        final LoginInfo loginInfo = BaseApplication.loginInfo;
        String endPoint = SoapEndpoint.SET_IS_SIGNUP;
        final String soapAction = SoapAction.GetUserOwnNetwork;
        final MySoap transport = new MySoap(endPoint);
        String info = SoapInfo.GetUserOwnNetwork;
        info = info.replace("UserIdValue", loginInfo.getUserId() + "");
        info = info.replace("ClientNameValue", BaseSettings.CLIENT_NAME);
        info = info.replace("SessionIdValue", loginInfo.getSessionId());
        info = info.replace("RoleDataValue", "0");
        transport.setinfo(info);
        transport.debug = true;
        new Thread() {
            @Override
            public void run() {
                final SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                // 设置是否调用的是dotNet开发的WebService
                envelope.dotNet = true;
                try {
                    final String call = transport.call(soapAction, envelope, null, "");
                    RetrueCodeHandler handler = RetruenUtils.getReturnInfo(call, new RetrueCodeHandler());
                    if (handler != null && handler.getString().equals("0")) {
                        String getUserOwnNetwork = handler.getGetUserOwnNetwork();
                        BaseApplication.userOwnNetwork = getUserOwnNetwork;
                        BaseApplication.cityName = handler.getCityName();
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, "提交失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    LogU.e(call);
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
        }.start();
    }


    @Override
    protected void onResume() {
        initerrDocument();
        super.onResume();
    }

    private void getCarNum() {
        final LoginInfo loginInfo = BaseApplication.loginInfo;
        String endPoint = SoapEndpoint.SET_IS_SIGNUP;
        final String soapAction = SoapAction.GetUserTruck;
        final MySoap transport = new MySoap(endPoint);
        String info = SoapInfo.GetUserTruck;
        info = info.replace("UserIdValue", loginInfo.getUserId() + "");
        info = info.replace("ClientNameValue", BaseSettings.CLIENT_NAME);
        info = info.replace("SessionIdValue", loginInfo.getSessionId());
        info = info.replace("RoleDataValue", "0");
        transport.setinfo(info);
        transport.debug = true;
        new Thread() {
            @Override
            public void run() {
                final SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                // 设置是否调用的是dotNet开发的WebService
                envelope.dotNet = true;
                try {
                    final String call = transport.call(soapAction, envelope, null, "");
                    LogU.e(call);
                    RetrueCodeHandler handler = RetruenUtils.getReturnInfo(call, new RetrueCodeHandler());
                    if (handler != null && handler.getString().equals("0")) {
                        String userBindTool = handler.getValue();
                        BaseApplication.userBindTool = userBindTool;

                        //TODO 目前车牌号获取接口有问题
                      /*  if (TextUtils.isEmpty(userBindTool)){
                            mLvLeftList.add("新增客户");
                            mMenuAdapter.notifyDataSetChanged();
                        }*/
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, "提交失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    LogU.e(call);
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
        }.start();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    //刷新
    private void GetCounts() {
        String templet = TempletUtil.render(BaseSettings.GETLOGINEMPLOYEE_TEMPLET, BaseApplication.getApplication().getSecurityInfo());
        BaseApplication.getApplication().getHttpPro().postAsync(templet, BaseSettings.GETLOGINEMPLOYEE_ACTION, new HttpCallback() {
            @Override
            public void onHttpStart() {
                setHandler(SHOW_LOADING, "");
            }

            @Override
            public void onHttpFinish(String data) {
                XmlParserUtil parser = XmlParserUtil.getInstance();
                parser.parse(data);
                String code = parser.getNodeValue("/soap:Envelope/soap:Body/GetLoginEmployeeResponse/GetLoginEmployeeResult/ReturnCode");
                if (StrKit.isBlank(code) || !code.equals("0")) {
                    String error = parser.getNodeValue("/soap:Envelope/soap:Body/GetLoginEmployeeResponse/GetLoginEmployeeResult/Error");
                    setHandler(SHOW_INFO, error);
                } else {
                    mEmployee = new Employee();
                    String prefix = "/soap:Envelope/soap:Body/GetLoginEmployeeResponse/GetLoginEmployeeResult/Value/";
                    try {
                        mEmployee.setEmplId(Integer.valueOf(parser.getNodeValue(prefix + "EmplId")));
                        mEmployee.setEmplCode(parser.getNodeValue(prefix + "EmplCode"));
                        mEmployee.setEmplName(parser.getNodeValue(prefix + "EmplName"));
                        mEmployee.setIsLeft(Boolean.valueOf(parser.getNodeValue(prefix + "IsLeft")));
                        mEmployee.setNetworkId(Integer.valueOf(parser.getNodeValue(prefix + "NetworkId")));
                        mEmployee.setMobileNumber(parser.getNodeValue(prefix + "MobileNumber"));
                        mEmployee.setWebChat(parser.getNodeValue(prefix + "WebChat"));
                        mEmployee.setQQ(parser.getNodeValue(prefix + "QQ"));
                        mEmployee.setIsDeliver(Boolean.valueOf(parser.getNodeValue(prefix + "IsDriver")));
                        mEmployee.setIsBusiness(Boolean.valueOf(parser.getNodeValue(prefix + "IsBusiness")));
                        mEmployee.setIsDeliver(Boolean.valueOf(parser.getNodeValue(prefix + "IsDeliver")));
                        mEmployee.setIsReceiver(Boolean.valueOf(parser.getNodeValue(prefix + "IsReceiver")));
                        mEmployee.setTruckNumber(parser.getNodeValue(prefix + "TruckNumber"));
                    } catch (Exception e) {
                        //如果使用的admin账号的时候里面数据都是空。所以就会异常
                    }
                    int tools = 0;
                    if (parser.getNodeValue(prefix + "BindTools") != null && !parser.getNodeValue(prefix + "BindTools").equals("null")
                            && StrKit.notBlank(parser.getNodeValue(prefix + "BindTools")))
                        tools = Integer.valueOf(parser.getNodeValue(prefix + "BindTools"));
                    mEmployee.setBindTools(tools);
                    setHandler(UPDATE_NAME, "");
                    BaseApplication.getApplication().setEmployee(mEmployee);
                }
                initAcceptRequestCount();
                initApplyDuctoment();
                initDeliverableDocument();
                //initIntoDocument();
                //异常订单
                initerrDocument();
                setHandler(HIDE_LOADING, "");
            }

            @Override
            public void onHttpError(String msg) {
                setHandler(HIDE_LOADING, "");
                setHandler(SHOW_INFO, msg);
            }

            @Override
            public void onHttpEnd() {
            }
        });
        String endPoint = SoapEndpoint.SET_IS_SIGNUP;
        // SOAP Action
        final String soapAction = SoapAction.GetDriverLoadedDocumentCount;
        final MySoap transport = new MySoap(endPoint);
        try {
            final LoginInfo loginInfo = BaseApplication.loginInfo;
            String info = SoapInfo.GetDriverLoadedDocumentCount;
            info = info.replace("UserIdValue", loginInfo.getUserId() + "");
            info = info.replace("ClientNameValue", Infos.CLIENTNAME);
            info = info.replace("SessionIdValue", loginInfo.getSessionId());
            LogU.e(loginInfo.toString());
            info = info.replace("RoleDataValue", "0");
            transport.setinfo(info);
            transport.debug = true;
            final LoginInfo finalLoginInfo = loginInfo;
        } catch (Exception e) {
            LogU.e("ssss");
        }
    }

    private void initerrDocument() {
        try {
            errorDocuments = BaseApplication.mdbUtils.findAll(ErrorDocument.class);
            if (errorDocuments != null) {
                errorDocumentsCount = errorDocuments.size();
            } else {
                llErrorDocuments.setBackgroundColor(Color.WHITE);
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        int count = DataSupport.count(PrintDocumentInfo.class);
        btnErrorDocuments.setText(errorDocumentsCount + "+" + count);
        if (errorDocumentsCount != 0) {
            llErrorDocuments.setBackgroundColor(Color.RED);
        } else {
            llErrorDocuments.setBackgroundColor(Color.WHITE);
        }
    }

    /*private void initTruck() {
        RequestParams params = new RequestParams();
        params.addBodyParameter("sessionId", BaseApplication.loginInfo.getSessionId());
        params.addBodyParameter("userId", BaseApplication.loginInfo.getUserId() + "");
        params.addBodyParameter("clientName", BaseSettings.CLIENT_NAME);
        params.addBodyParameter("returnList", false + "");
        params.addBodyParameter("pageNumber", 0 + "");
        params.addBodyParameter("pageSize", 0 + "");
        params.addBodyParameter("sortPropertyName", "");
        params.addBodyParameter("sortDesending", false + "");
        params.addBodyParameter("recordId", "0");
        params.addBodyParameter("beginTime", "");
        params.addBodyParameter("endTime", "");
        params.addBodyParameter("receiveState", 1 + "");
        BaseApplication.mHttpUtils.send(HttpRequest.HttpMethod.POST,
                Urls.DURLGetHandOverRecords,
                params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        RetrueCodeHandler handler = RetruenUtils.getReturnInfo(responseInfo.result, new RetrueCodeHandler());
                        final String returnCode = handler.getRecordCount();
                        mTruckBtn.setText(returnCode + "");
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        Toast.makeText(MainActivity.this, "网络数据获取错误", Toast.LENGTH_SHORT).show();
                    }
                });
    }
*/
    private void initAcceptRequestCount() {
        LoginInfo loginInfo = BaseApplication.loginInfo;
        RequestParams params = new RequestParams();
        params.addBodyParameter("sessionId", loginInfo.getSessionId());
        params.addBodyParameter("userId", loginInfo.getUserId() + "");
        params.addBodyParameter("clientName", BaseSettings.CLIENT_NAME);
        params.addBodyParameter("returnList", "false");
        params.addBodyParameter("pageNumber", 0 + "");
        params.addBodyParameter("pageSize", 0 + "");
        BaseApplication.mHttpUtils.send(HttpRequest.HttpMethod.POST,
                Urls.GetUnfinishRequest,
                params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        SAXParserFactory factory = SAXParserFactory.newInstance();
                        SAXParser parser = null;
                        try {
                            parser = factory.newSAXParser();
                            RetrueCodeHandler handler = new RetrueCodeHandler();
                            ByteArrayInputStream tInputStringStream = new ByteArrayInputStream(responseInfo.result.getBytes());
                            parser.parse(tInputStringStream, handler);
                            if (handler.getString().equals("0")) {
                                mAcceptRequestBtn.setText(handler.getRecordCount() + "");
                            } else {
                                Toast.makeText(MainActivity.this, handler.getError(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this, "数据解析出错", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {

                        Toast.makeText(MainActivity.this, "批量转入：网络数据获取失败", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /*private void initIntoDocument() {
        RequestParams params = new RequestParams();
        params.addBodyParameter("sessionId", BaseApplication.loginInfo.getSessionId());
        params.addBodyParameter("userId", BaseApplication.loginInfo.getUserId() + "");
        params.addBodyParameter("clientName", BaseSettings.CLIENT_NAME);
        params.addBodyParameter("returnList", false + "");
        params.addBodyParameter("pageNumber", 0 + "");
        params.addBodyParameter("pageSize", 0 + "");
        params.addBodyParameter("sortPropertyName", "");
        params.addBodyParameter("sortDesending", false + "");
        params.addBodyParameter("recordId", "0");
        params.addBodyParameter("beginTime", "");
        params.addBodyParameter("endTime", "");
        params.addBodyParameter("receiveState", 1 + "");
        BaseApplication.mHttpUtils.send(HttpRequest.HttpMethod.POST,
                Urls.QueryReceiveHandOverRecords,
                params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        SAXParserFactory factory = SAXParserFactory.newInstance();
                        SAXParser parser = null;
                        try {
                            parser = factory.newSAXParser();
                            RetrueCodeHandler handler = new RetrueCodeHandler();
                            ByteArrayInputStream tInputStringStream = new ByteArrayInputStream(responseInfo.result.getBytes());
                            parser.parse(tInputStringStream, handler);
                            final String returnCode = handler.getString();
                            if (returnCode.equals("0")) {
                                mRollinBtn.setText(handler.getRecordCount() + "");
                            } else {
                                Toast.makeText(MainActivity.this, handler.getError(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this, "数据解析出错", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {

                        Toast.makeText(MainActivity.this, "批量转入：网络数据获取失败", Toast.LENGTH_SHORT).show();
                    }
                });
    }*/

    private void startLocationService() {
        if (TextUtils.isEmpty(BaseApplication.userBindTool)) {
            return;
        }
        if (isServiceRunning(this, "cn.xsjky.android.service.GPSService")) {
            stopLocationService();
        }
        startService(intent);
    }

    private void stopLocationService() {
        isServiceRunning(this, "cn.xsjky.android.service.GPSService");
        stopService(intent);
        //stopService(new Intent(MainActivity.this, protectService.class));
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

    private void initDeliverableDocument() {
        String templet = TempletUtil.render(BaseSettings.GETDELIVERABLEDOCUMENTCOUNT_TEMPLET, BaseApplication.getApplication().getSecurityInfo());
        BaseApplication.getApplication().getHttpPro().postAsync(templet, BaseSettings.GETDELIVERABLEDOCUMENTCOUNT_ACTION, new HttpCallback() {

            @Override
            public void onHttpStart() {
            }

            @Override
            public void onHttpFinish(String data) {
                XmlParserUtil parser = XmlParserUtil.getInstance();
                parser.parse(data);
                String value = parser.getNodeValue("/soap:Envelope/soap:Body/GetDeliverableDocumentCountResponse/GetDeliverableDocumentCountResult/Value");
                mDeliverableBtn.setText(value);
            }

            @Override
            public void onHttpError(String msg) {
                setHandler(SHOW_INFO, msg);
            }

            @Override
            public void onHttpEnd() {
            }

        });
    }

    private void initApplyDuctoment() {
        String templet = TempletUtil.render(BaseSettings.GETAPPLYDOCUMENTCOUNT_TEMPLET, BaseApplication.getApplication().getSecurityInfo());
        BaseApplication.getApplication().getHttpPro().postAsync(templet, BaseSettings.GETAPPLYDOCUMENTCOUNT_ACTION, new HttpCallback() {

            @Override
            public void onHttpStart() {
            }

            @Override
            public void onHttpFinish(String data) {
                XmlParserUtil parser = XmlParserUtil.getInstance();
                parser.parse(data);
                String code = parser.getNodeValue("/soap:Envelope/soap:Body/GetApplyDocumentCountResponse/GetApplyDocumentCountResult/ReturnCode");
                if (StrKit.isBlank(code) || !code.equals("0")) {
                    String error = parser.getNodeValue("/soap:Envelope/soap:Body/GetApplyDocumentCountResponse/GetApplyDocumentCountResult/Error");
                    setHandler(SHOW_INFO, error);
                } else {
                    String value = parser.getNodeValue("/soap:Envelope/soap:Body/GetApplyDocumentCountResponse/GetApplyDocumentCountResult/Value");
                    mApplyDocumentBtn.setText(value);
                }
            }

            @Override
            public void onHttpError(String msg) {
                setHandler(SHOW_INFO, msg);
            }

            @Override
            public void onHttpEnd() {
            }

        });
    }

    private void setListener() {
        mApplyDocumentLayout.setOnClickListener(clickListener);
        mDeliverableLayout.setOnClickListener(clickListener);
      /*  mTruckLayout.setOnClickListener(clickListener);
        mRollinLayout.setOnClickListener(clickListener);*/
        mAdd.setOnClickListener(clickListener);
        mAcceptRequestLayout.setOnClickListener(clickListener);
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    private OnClickListener clickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == id.main_receive_layout) {
                Intent intent = new Intent(MainActivity.this, ApplyDocumentActivity.class);
                MainActivity.this.startActivity(intent);
            } else if (v.getId() == id.main_deliverable_layout) {
                Intent intent = new Intent(MainActivity.this, DeliverableActivity.class);
                MainActivity.this.startActivity(intent);
            } else if (v.getId() == id.head_tool1) {
                Intent intent = new Intent(MainActivity.this, NewActivity.class);
                MainActivity.this.startActivity(intent);
            } else if (v.getId() == id.main_AcceptRequest_layout) {
                Intent intent = new Intent(MainActivity.this, ApplyDocumentActivity.class);
                intent.putExtra("IsAcceptRequest", true);
                MainActivity.this.startActivity(intent);
            } else if (v.getId() == id.btn_main_menu) {
                if (drawLayout.isDrawerOpen(lvLeft)) {
                    drawLayout.closeDrawers();
                } else {
                    drawLayout.openDrawer(lvLeft);
                }
            }
        }
    };

    public void setHandler(int code, Object obj) {
        Message msg = new Message();
        msg.what = code;
        msg.obj = obj;
        handler.sendMessage(msg);
    }

    private boolean serviceOpen = true;

    private void showListViewDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_listview, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);  //先得到构造器
        ListView lvDialog = (ListView) view.findViewById(id.lv_dialog_lv);
        listDoucmentNums = new ArrayList<>();
        for (int i = 0; i < errorDocuments.size(); i++) {
            listDoucmentNums.add(errorDocuments.get(i).getDocumentNumber());
        }
        simpleAdapterAdapter = new ItemLvDialogAdapter<>(this, listDoucmentNums);
        lvDialog.setAdapter(simpleAdapterAdapter);
        builder.setView(view);
        builder.setTitle("选择你要处理的订单");
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        lvDialog.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, NewActivity.class);
                intent.putExtra("strDocuments", errorDocuments.get(position).getDocumentNumber());
                startActivity(intent);
                alertDialog.dismiss();
            }
        });
    }

    public void simpleAdapterAdapterNotf() {
        if (simpleAdapterAdapter != null) {
            try {
                errorDocuments = BaseApplication.mdbUtils.findAll(ErrorDocument.class);
                if (errorDocuments != null) {
                    errorDocumentsCount = errorDocuments.size();
                }
            } catch (DbException e) {
                e.printStackTrace();
            }
            btnErrorDocuments.setText(errorDocumentsCount + "");
            if (errorDocumentsCount != 0) {
                llErrorDocuments.setBackgroundColor(Color.RED);
            }
            listDoucmentNums.clear();
            for (int i = 0; i < errorDocuments.size(); i++) {
                listDoucmentNums.add(errorDocuments.get(i).getDocumentNumber());
            }
            simpleAdapterAdapter.notifyDataSetChanged();
        }
    }

    private int errorDocumentsCount = 0;

    private void findViewById() {
        drawLayout = (DrawerLayout) findViewById(id.drawer_layout);
        mIvMenu = (ImageView) findViewById(id.btn_main_menu);
        mIvMenu.setOnClickListener(clickListener);
        llErrorDocuments = (LinearLayout) findViewById(id.ll_main_rollin_error_documents);
        llQuery = (LinearLayout) findViewById(id.ll_queryDocument);
        llQuery.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, QueryUnFinishDocumentActivity.class));
            }
        });
        llMainContentUserBindTool = (LinearLayout) findViewById(id.main_content_userBindTool);
        llMainContentUnUserBindTool = (LinearLayout) findViewById(id.main_content_unUserBindTool);
        llMainContentUnUserBindTool = (LinearLayout) findViewById(id.main_content_unUserBindTool);
        llCustomManagerlayout = (LinearLayout) findViewById(id.ll_CustomManager_layout);
        llCustomManagerlayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CustomerManagerActivity.class));
            }
        });
        llSendQuestLayout = (LinearLayout) findViewById(id.ll_sendQuest_layout);
        llSendQuestLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this, SendQuestActivity.class));
            }
        });


        btnErrorDocuments = (Button) findViewById(id.btn_main_error_documents);
        initerrDocument();
        try {
            List<PrintDocumentInfo> all = DataSupport.limit(10).find(PrintDocumentInfo.class);
            LogU.e(all.toString());
        } catch (Exception e) {

        }
        llErrorDocuments.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = DataSupport.count(PrintDocumentInfo.class);
                if (count == 0 && (errorDocuments == null || errorDocuments.size() == 0)) {
                    Tos("当前无订单");
                    return;
                }
                //showListViewDialog();
                startActivity(new Intent(MainActivity.this, ErrorDocumentActivity.class));

            }
        });
        mApplyDocumentLayout = (LinearLayout) this.findViewById(id.main_receive_layout);
        lvLeft = (ListView) findViewById(id.main_left_lv);
        mLvLeftList = new ArrayList<>();
        mLvLeftList.add("退出登录");
        mLvLeftList.add("修改密码");
        mLvLeftList.add("清除缓存");
        mLvLeftList.add("同步数据");
        //TODO  测试阶段默认添加
        // if (TextUtils.isEmpty(BaseApplication.userBindTool))
        mLvLeftList.add("客户管理");
        mLvLeftList.add("查询运单");
        mLvLeftList.add("统计");
        //TODO 添加侧滑菜单
        mMenuAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mLvLeftList);
        lvLeft.setAdapter(mMenuAdapter);
        mAcceptRequestLayout = (LinearLayout) this.findViewById(id.main_AcceptRequest_layout);
        mApplyDocumentBtn = (Button) this.findViewById(id.main_applydocument_count);
        mDeliverableLayout = (LinearLayout) this.findViewById(id.main_deliverable_layout);
        mDeliverableBtn = (Button) this.findViewById(id.main_deliverable_count);
        mAcceptRequestBtn = (Button) this.findViewById(id.main_AcceptRequest_count);
        mFlip = (ImageView) findViewById(id.head_flip);
        mFlip.setVisibility(View.VISIBLE);
        mFlip.setImageResource(R.drawable.ic_flipper_head_refresh);
        mFlip.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                GetCounts();
            }
        });
        mAdd = (ImageView) this.findViewById(id.head_tool1);
        mAdd.setVisibility(View.VISIBLE);
        mTitle = (TextView) this.findViewById(id.head_title);
        mServiceStatue = (TextView) this.findViewById(id.tv_main_service_status);
        btnService = (Button) findViewById(id.btn_main_service);
        btnService.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (OnClickutils.isFastDoubleClick()) {
                    return;
                }
                if (serviceOpen) {
                    stopLocationService();
                    btnService.setText("定位服务关闭");
                    mServiceStatue.setText("定位已关闭");
                    serviceOpen = false;
                } else {
                    startLocationService();
                    btnService.setText("定位服务已开启");
                    serviceOpen = true;
                }
            }
        });
        //TODO 通过有无车牌判断是否是司机   显示不同功能
        if (TextUtils.isEmpty(BaseApplication.userBindTool)) {
            llMainContentUserBindTool.setVisibility(View.GONE);
            llMainContentUnUserBindTool.setVisibility(View.VISIBLE);

            loginSuccess();

        } else {
            llMainContentUserBindTool.setVisibility(View.VISIBLE);
            llMainContentUnUserBindTool.setVisibility(View.GONE);
        }
        GPSService.setLoacationListener(new LocationIntface() {
            @Override
            public void locationStatus(final boolean IsSuccess, final String error) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mServiceStatue == null) {
                            return;
                        }
                        if (IsSuccess) {
                            mServiceStatue.setText("定位成功" + error);
                            //queryCustomer(BaseApplication.getApplication().getCity(), 0);
                            if (TextUtils.isEmpty(BaseApplication.userBindTool)) {
                                stopLocationService();
                                stopService(new Intent(MainActivity.this, protectService.class));
                            }
                        } else {
                            mServiceStatue.setText("定位失败" + error);
                            mServiceStatue.setTextColor(Color.BLUE);
                        }
                    }
                });
            }
        });
        lvLeft.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    logout();
                } else if (position == 1) {
                    changePsw();
                } else if (position == 2) {
                    try {
                        NewDocumentDataCache newDocumentDataCache = new NewDocumentDataCache();
                        newDocumentDataCache.setId(1);
                        BaseApplication.mdbUtils.update(newDocumentDataCache);
                        SPUtils.put(MainActivity.this, BaseApplication.getApplication().getCity(), false);
                        DataSupport.deleteAll(Custom.class);
                        Tos("清除成功");
                    } catch (DbException e) {
                        Tos("清除失败");
                        e.printStackTrace();
                    }
                } else if (position == 3) {
                    startActivity(new Intent(MainActivity.this, SynDataActivity.class));
                } else if (position == 4) {
                    startActivity(new Intent(MainActivity.this, CustomManagerActivity.class));
                } else if (position == 5) {
                    startActivity(new Intent(MainActivity.this, QueryDocument.class));
                } else if (position == 6) {
                    startActivity(new Intent(MainActivity.this, StatisticsDocumentActivity.class));
                }
                drawLayout.closeDrawers();
            }
        });


        mRotateAnimation = new RotateAnimation(0, 359,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        mRotateAnimation.setDuration(2000);
        mRotateAnimation.setRepeatCount(3);
        mItemStatisticsflash = (ImageView) findViewById(id.statistics_flash);
        itemStatisticsPrice = (TextView) findViewById(R.id.item_statistics_price);
        itemStatisticsPrice.setText("目前暂无统计");
        mItemStatisticsflash.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //动画
                mItemStatisticsflash.setClickable(false);
                mItemStatisticsflash.startAnimation(mRotateAnimation);
                //设置数据
                initTodayDocument();
            }
        });
    }

    private void loginSuccess() {
        String url = Urls.FindInProgressRequest;
        RequestParams params = new RequestParams();
        params.addBodyParameter("sessionId", BaseApplication.loginInfo.getSessionId());
        params.addBodyParameter("userId", BaseApplication.loginInfo.getUserId() + "");
        params.addBodyParameter("clientName", BaseSettings.CLIENT_NAME);

        BaseApplication.mHttpUtils.send(HttpRequest.HttpMethod.POST,
                url,
                params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        LogU.e(responseInfo.result);
                        RetrueCodeHandler retrueCode = RetruenUtils.getReturnInfo(responseInfo.result, new RetrueCodeHandler());
                        if (retrueCode != null && retrueCode.getString().equals("0")) {
                            ProgressRequestXmlParser parser = RetruenUtils.getReturnInfo(responseInfo.result, new ProgressRequestXmlParser());
                            if (parser != null) {
                                progressRequest = parser.getProgressRequest();
                                BaseApplication.progressRequest = progressRequest;
                            } else {
                                Tos("解析错误");
                            }
                        } else if (retrueCode != null && retrueCode.getString().equals("-1")) {
                            Tos(retrueCode.getError());
                        } else {
                            Tos("请求数据错误");
                        }
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        Tos(msg);
                    }
                });
    }

    private void changePsw() {
        startActivity(new Intent(this, ChangePswActivity.class));
    }

    private void logout() {
        SharedPreferences sp = getSharedPreferences("login", MODE_PRIVATE);

        SharedPreferences.Editor editor = sp.edit();

        editor.remove("data");

        editor.remove("loginName");

        editor.commit();
        stopLocationService();
        isServiceRunning(this, "cn.xsjky.android.protectServce.protectService");
        stopService(new Intent(this, protectService.class));
        boolean pushStopped = JPushInterface.isPushStopped(getApplication());
        if (!pushStopped) {
            JPushInterface.stopPush(getApplicationContext());
        }
        finish();
    }

    private final int UPDATE_NAME = 0;
    private final int UPDATE_DATA = 1;
    private final int SHOW_LOADING = 2;
    private final int HIDE_LOADING = 3;
    private final int SHOW_INFO = 4;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_NAME:
                    mTitle.setText(mEmployee.getEmplName() + "       " + BaseApplication.isDevrlop);
                    break;
                case UPDATE_DATA:
                    break;
                case SHOW_LOADING:
                    LoadingDialog.show(MainActivity.this, false, false);
                    break;
                case HIDE_LOADING:
                    LoadingDialog.dismiss();
                    break;
                case SHOW_INFO:
                    Toast.makeText(MainActivity.this, (String) msg.obj, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

}
