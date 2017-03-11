package cn.xsjky.android.ui;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.xsjky.android.BaseApplication;
import cn.xsjky.android.BaseSettings;
import cn.xsjky.android.R;
import cn.xsjky.android.http.Urls;
import cn.xsjky.android.model.AddressBook;
import cn.xsjky.android.model.BastLocationJson;
import cn.xsjky.android.model.CargoInfo;
import cn.xsjky.android.model.FindInProgressRequest;
import cn.xsjky.android.model.HandlerCoordinate;
import cn.xsjky.android.model.Login;
import cn.xsjky.android.model.LoginInfo;
import cn.xsjky.android.model.ProgressRequestXmlParser;
import cn.xsjky.android.util.AddressBookXml;
import cn.xsjky.android.util.CallBackString;
import cn.xsjky.android.util.GetMarkNamesXmlparser;
import cn.xsjky.android.util.HandlerCoordinateXmlParser;
import cn.xsjky.android.util.LocationXmlParser;
import cn.xsjky.android.util.LogU;
import cn.xsjky.android.util.MySoap;
import cn.xsjky.android.util.OnClickutils;
import cn.xsjky.android.util.RetrueCodeHandler;
import cn.xsjky.android.util.RetruenUtils;

public class SendQuestActivity extends BaseActivity implements View.OnClickListener {

    public static final String ACTION = "cn.xsjky.android.ui.SendQuestActivity.getship";
    private ArrayList<String> listNames;
    private ArrayList<CheckBox> boxes;
    private LinearLayout ll_send;
    private LinearLayout ll_shipment_UpdateCargoInfos;
    private LinearLayout ll_sendPeople;
    private TextView tvSelectSendPeople;
    private LinearLayout ll_cancelDocument;
    private CardView canclecardView;
    private EditText etRemark;
    private EditText etVol;
    private Spinner spTime;
    private Button btnShipOK;
    private Spinner spVol;
    private LinearLayout llMarkNames;
    private LinearLayout llTime1;
    private Button btn_cancelDocument;
    private EditText etCancelReason;
    private Button btn_cancelDocumentOk;
    private TextView tv_status;
    private TextView tv_drive_address;
    private TextView tv_drive_num;
    private TextView tv_callNum;
    private TextView tv_distance;
    private ArrayList<String> dates;
    private ArrayList<Date> times;
    private ArrayList<String> timeTist;
    private ArrayList<String> data_list;
    private ArrayList<String> arrCars;
    private ArrayAdapter<String> arr_adapter;
    private TextView tvName;
    private TextView tvComName;
    private TextView tvCallNum;
    private TextView tvAddress;
    private Button btnDelete;
    private AddressBook mUser;
    private FindInProgressRequest progressRequest;
    private AlertDialog builder;
    private EditText etCancelRes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_quest);
        initToolbar("发货请求");
        findviews();
        getMarkNames();
        initTime();
        registRecive();
        getLastUser();
    }

    private void getLastUser() {
        RequestParams params = new RequestParams();
        params.addBodyParameter("sessionId", BaseApplication.loginInfo.getSessionId());
        params.addBodyParameter("userId", BaseApplication.loginInfo.getUserId() + "");
        params.addBodyParameter("clientName", BaseSettings.CLIENT_NAME);
        getData(Urls.GetDefault, params, new CallBackString() {
            @Override
            public void httFinsh(String data) {
                AddressBookXml parser = RetruenUtils.getReturnInfo(data, new AddressBookXml());
                if (parser != null) {
                    AddressBook user = parser.getUser();
                    BaseApplication.shipBook = user;
                    mUser = user;
                    ll_sendPeople.removeAllViews();
                    addView(ll_sendPeople, user);
                }
            }
        });
    }

    private void registRecive() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION);
        registerReceiver(myReceiver, filter);
    }

    private BroadcastReceiver myReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            ll_sendPeople.removeAllViews();
            mUser = (AddressBook) intent.getSerializableExtra("data");
            addView(ll_sendPeople, mUser);
        }
    };

    private void addView(LinearLayout linearLayout, AddressBook book) {
        if (book == null) {
            return;
        }
        View view = LayoutInflater.from(this).inflate(R.layout.item_addressinfo, null);
        tvName = (TextView) view.findViewById(R.id.tvName);
        tvComName = (TextView) view.findViewById(R.id.tvComName);
        tvCallNum = (TextView) view.findViewById(R.id.tvCallNum);
        tvAddress = (TextView) view.findViewById(R.id.tvAddress);
        btnDelete = (Button) view.findViewById(R.id.btn_delete);
        btnDelete.setVisibility(View.GONE);
        tvName.setText(book.getContactName());
        tvComName.setText(book.getCompanyName());
        tvCallNum.setText(book.getMobileNumber());
        tvAddress.setText(book.getAddress());
        linearLayout.addView(view);
    }

    private void findviews() {
        ll_send = (LinearLayout) findViewById(R.id.ll_send);
        ll_shipment_UpdateCargoInfos = (LinearLayout) findViewById(R.id.ll_shipment_UpdateCargoInfos);
        ll_shipment_UpdateCargoInfos.setOnClickListener(this);
        ll_sendPeople = (LinearLayout) findViewById(R.id.ll_sendPeople);
        tvSelectSendPeople = (TextView) findViewById(R.id.tv_selectSendPeople);
        tvSelectSendPeople.setClickable(true);
        tvSelectSendPeople.setOnClickListener(this);
        ll_cancelDocument = (LinearLayout) findViewById(R.id.ll_cancelDocument);
        canclecardView = (CardView) findViewById(R.id.cv);
        etRemark = (EditText) findViewById(R.id.et_remark);
        etVol = (EditText) findViewById(R.id.etVol);
        spTime = (Spinner) findViewById(R.id.spTime);
        btnShipOK = (Button) findViewById(R.id.btn_shipOk);
        btnShipOK.setOnClickListener(this);
        spVol = (Spinner) findViewById(R.id.spVol);
        llMarkNames = (LinearLayout) findViewById(R.id.ll_cks);
        llTime1 = (LinearLayout) findViewById(R.id.ll_time1);
        btn_cancelDocument = (Button) findViewById(R.id.btn_cancelDocument);
        etCancelReason = (EditText) findViewById(R.id.etCancelReason);
        btn_cancelDocumentOk = (Button) findViewById(R.id.btn_cancelDocumentOk);

        tv_status = (TextView) findViewById(R.id.tv_status);
        tv_drive_address = (TextView) findViewById(R.id.tv_drive_address);
        tv_drive_num = (TextView) findViewById(R.id.tv_drive_num);
        tv_callNum = (TextView) findViewById(R.id.tv_callNum);
        tv_distance = (TextView) findViewById(R.id.tv_distance);

        btn_cancelDocument.setOnClickListener(this);
        btn_cancelDocumentOk.setOnClickListener(this);
        initSpData();
        spVol.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                etVol.setText(arrCars.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ll_sendPeople.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvSelectSendPeople.performClick();
            }
        });
    }

    private void initSpData() {
        //数据
        data_list = new ArrayList<String>();
        data_list.add("选择");
        data_list.add("小型面包车(5)");
        data_list.add("中型面包车(10)");
        data_list.add("小型货车(15)");
        data_list.add("中型货车(20)");
        data_list.add("大型货车(50)");

        arrCars = new ArrayList<>();
        arrCars.add(0 + "");
        arrCars.add(5 + "");
        arrCars.add(10 + "");
        arrCars.add(15 + "");
        arrCars.add(20 + "");
        arrCars.add(50 + "");
        //适配器
        arr_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data_list);
        //设置样式
        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        spVol.setAdapter(arr_adapter);
    }

    private void getMarkNames() {
        showProgressDialog();
        String url = Urls.GetMarkNames;
        RequestParams params = new RequestParams();
        params.addBodyParameter("sessionId", BaseApplication.loginInfo.getSessionId());
        params.addBodyParameter("userId", BaseApplication.loginInfo.getUserId() + "");
        params.addBodyParameter("clientName", BaseSettings.CLIENT_NAME);
        getData(url, params, new CallBackString() {
            @Override
            public void httFinsh(String data) {
                RetrueCodeHandler retrueCode = RetruenUtils.getReturnInfo(data, new RetrueCodeHandler());
                if (retrueCode != null && retrueCode.getString().equals("0")) {
                    GetMarkNamesXmlparser namesXmlparser = RetruenUtils.getReturnInfo(data, new GetMarkNamesXmlparser());
                    if (namesXmlparser != null) {
                        listNames = namesXmlparser.getGetMarkNames().getListNames();
                        initMarkNames();
                    } else {
                        Tos("解析出错");
                    }
                } else if (retrueCode != null && retrueCode.getString().equals("-1")) {
                    Tos(retrueCode.getError());
                } else {
                    Tos("请求数据错误");
                }
            }
        });
    }

    private void initMarkNames() {
        boxes = new ArrayList<>();
        for (int i = 0; i < listNames.size(); i++) {
            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(listNames.get(i));
            llMarkNames.addView(checkBox);
            boxes.add(checkBox);
        }
    }

    private void initTime() {
        final Calendar c = Calendar.getInstance();
        long lTime = System.currentTimeMillis();
        dates = new ArrayList<>();
        times = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        timeTist = new ArrayList<>();
        for (int i = 1; i < 24; i++) {
            Date date = new Date(lTime + 1000 * 60 * 60 * i + 1000 * 60 * 5);
            this.dates.add(sdf.format(date));
            if (date.getMinutes() < 30) {
                date = new Date(lTime + 1000 * 60 * 60 * (i + 1));
                timeTist.add(date.getDate() + "号 " + date.getHours() + "点");
                times.add(date);
            } else {
                date = new Date(lTime + 1000 * 60 * 60 * (i + 2));
                timeTist.add(date.getDate() + "号 " + date.getHours() + "点");
                times.add(date);
            }
        }
        spTime.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, timeTist));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_shipOk:
                if (OnClickutils.isFastDoubleClick()) {
                    return;
                }

                sendGoods();
                break;/*
            case R.id.etTimeSecond:
                showTimePickerDialog();
                break;*/
            case R.id.ll_shipment_UpdateCargoInfos:
                startActivity(new Intent(this, UpdateCargoInfosActivity.class));
                break;
            case R.id.btn_cancelDocument:
                if (BaseApplication.progressRequest == null) {
                    Tos("当前无订单");
                    return;
                }

                if (builder == null) {
                    builder = new AlertDialog.Builder(this).create();
                    builder.setView(LayoutInflater.from(this).inflate(R.layout.dialog_et, null));
                    builder.setTitle("是否确定取消订单？");
                    builder.getWindow().setGravity(Gravity.CENTER);

                }
                builder.show();
                builder.findViewById(R.id.btn_dialog_ok).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cancelDocument();
                        builder.dismiss();
                    }
                });
                builder.findViewById(R.id.btn_dialog_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        builder.dismiss();
                    }
                });
                etCancelRes = (EditText) builder.findViewById(R.id.et_dialog_cancelRes);
                break;
            case R.id.btn_cancelDocumentOk:
                cancelDocument();
                break;
            case R.id.tv_selectSendPeople:
                Intent intent = new Intent(this, SelectAddressActivity.class);
                intent.putExtra("IsSend", SHIP);
                startActivity(intent);
                break;
        }
    }

    public static final int SHIP = 5;

    private void cancelDocument() {
        showProgressDialog();
        String url = Urls.CancelDeliveryRequest;
        RequestParams params = new RequestParams();
        params.addBodyParameter("sessionId", BaseApplication.loginInfo.getSessionId());
        params.addBodyParameter("userId", BaseApplication.loginInfo.getUserId() + "");
        params.addBodyParameter("clientName", BaseSettings.CLIENT_NAME);
        params.addBodyParameter("requestId", BaseApplication.progressRequest.getRequestId());
        if (builder == null) {
            return;
        }
        if (TextUtils.isEmpty(etCancelRes.getText().toString())) {
            closeProgressDialog();
            Tos("请写明取消原因");
            return;
        }
        params.addBodyParameter("cancelReason", etCancelRes.getText().toString());
        BaseApplication.mHttpUtils.send(HttpRequest.HttpMethod.POST,
                url, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        RetrueCodeHandler retrueCode = RetruenUtils.getReturnInfo(responseInfo.result, new RetrueCodeHandler());
                        if (retrueCode != null && retrueCode.getString().equals("0")) {
                            Tos("取消成功");
                            UpdateCargoInfosActivity.clearCargoInfos();
                            BaseApplication.progressRequest = null;
                            //TODO

                        } else if (retrueCode != null && retrueCode.getString().equals("-1")) {
                            Tos(retrueCode.getError());
                        } else {
                            Tos("请求数据错误");
                        }
                        closeProgressDialog();
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        closeProgressDialog();
                        Tos(msg);
                    }
                });
    }

    private void UpdateCargoInfos(String requestId) {

        ArrayList<CargoInfo> cargoInfos = UpdateCargoInfosActivity.getCargoInfos();
        if (cargoInfos == null || cargoInfos.size() == 0) {
            return;
        }
        String endPoint = "http://" + BaseSettings.VERSIONS + ".xsjky.cn/DeliveryRequest/DeliveryRequestService.asmx";
        // SOAP Action
        final LoginInfo loginInfo = BaseApplication.loginInfo;
        final String soapAction = "http://www.xsjky.cn/UpdateCargoInfos";
        final MySoap transport = new MySoap(endPoint);
        String info = BaseSettings.UpdateCargoInfos;
        info = info.replace("userIdValue", loginInfo.getUserId() + "");
        info = info.replace("clientNameValue", BaseSettings.CLIENT_NAME);
        info = info.replace("sessionIdValue", loginInfo.getSessionId());
        info = info.replace("requestIdValue", requestId);
        String cargoString = " <CargoInfo>\n" +
                "          <RecordId>0</RecordId>\n" +
                "          <ProductName>ProductNameValue</ProductName>\n" +
                "          <Length>LengthValue</Length>\n" +
                "          <Width>WidthValue</Width>\n" +
                "          <Height>HeightValue</Height>\n" +
                "          <Quantity>QuantityValue</Quantity>\n" +
                "          <Volumn>VolumnValue</Volumn>\n" +
                "          <Remarks>RemarksValue</Remarks>\n" +
                "        </CargoInfo>";
        String cargosValues = "";
        for (int i = 0; i < cargoInfos.size(); i++) {
            cargosValues += cargoString;
            cargosValues = cargosValues.replace("ProductNameValue", cargoInfos.get(i).getProductName());
            cargosValues = cargosValues.replace("LengthValue", cargoInfos.get(i).getLength());
            cargosValues = cargosValues.replace("WidthValue", cargoInfos.get(i).getWidth());
            cargosValues = cargosValues.replace("HeightValue", cargoInfos.get(i).getHeight());
            cargosValues = cargosValues.replace("QuantityValue", cargoInfos.get(i).getQuantity());
            cargosValues = cargosValues.replace("VolumnValue", cargoInfos.get(i).getVolumn());
            cargosValues = cargosValues.replace("RemarksValue", cargoInfos.get(i).getRemarks());

        }
        info = info.replace("cargosValues", cargosValues);
        transport.setinfo(info);
        transport.debug = true;
        new Thread() {
            @Override
            public void run() {
                try {
                    String call = transport.call(soapAction, null, null, "");
                    final RetrueCodeHandler returnInfo = RetruenUtils.getReturnInfo(call, new RetrueCodeHandler());
                    LogU.e(call);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (returnInfo != null && returnInfo.getString().equals("0")) {
                                LogU.e("成功");
                            } else if (returnInfo != null && returnInfo.getString().equals("-1")) {
                                Tos(returnInfo.getError());
                                saveInfo();
                            } else {
                                Tos("请求失败");
                            }
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    closeProgressDialog();
                }
                //mListView.onRefreshComplete();
            }
        }.start();
    }

    private void sendGoods() {
        showProgressDialog();
        String endPoint = "http://" + BaseSettings.VERSIONS + ".xsjky.cn/DeliveryRequest/DeliveryRequestService.asmx";
        // SOAP Action
        final LoginInfo loginInfo = BaseApplication.loginInfo;
        final String soapAction = "http://www.xsjky.cn/NewRequest";
        final MySoap transport = new MySoap(endPoint);
        String info = BaseSettings.NewRequest;
        info = info.replace("userIdValue", loginInfo.getUserId() + "");
        info = info.replace("clientNameValue", BaseSettings.CLIENT_NAME);
        info = info.replace("sessionIdValue", loginInfo.getSessionId());
        if (mUser == null) {
            Tos("请选择发件人");
            closeProgressDialog();
            return;
        }
        info = info.replace("longitudeValue", mUser.getCoordinate().getLongitude() + "");
        info = info.replace("latitudeValue", mUser.getCoordinate().getLatitude() + "");
        // String contactPersonValue = etSendPeople.getText().toString();
        info = info.replace("contactPersonValue", mUser.getContactName());
        //String contactPhoneValue = etPhone.getText().toString();
        info = info.replace("contactPhoneValue", mUser.getMobileNumber());
        info = info.replace("ShipperNameValue", mUser.getCompanyName());
        String appointmentTimeValue = "";
        appointmentTimeValue = dates.get(spTime.getSelectedItemPosition());
        Date date = times.get(spTime.getSelectedItemPosition());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH");
        String format = sdf.format(date);
        info = info.replace("appointmentTimeValue", format + ":00:00");
        info = info.replace("cargoWeightValue", "0");

        int selectedItemPosition = spVol.getSelectedItemPosition();
        if (selectedItemPosition == 0 && (TextUtils.isEmpty(etVol.getText().toString()) || etVol.getText().toString().equals("0"))) {
            Tos("请选择车型");
            closeProgressDialog();
            return;
        }
        info = info.replace("cargoVolumnValue", etVol.getText().toString());
        info = info.replace("toCityValue", "");
        String reMark = etRemark.getText().toString();
        if (TextUtils.isEmpty(reMark)) {
            reMark = "";
        }
        info = info.replace("remarksValue", reMark);
        String requestMarksValue = "";
        //TODO
        for (int i = 0; i < boxes.size(); i++) {
            if (boxes.get(i).isChecked()) {
                requestMarksValue += "<string>" + listNames.get(i) + "</string>";
            }
        }
        info = info.replace("requestMarksValue", requestMarksValue);
        //String addressValue = etGetLocation.getText().toString() + " " + etcity.getText().toString() + " " + etdes.getText().toString() + " " + etAddressDetail.getText().toString();
        info = info.replace("addressValue", mUser.getAddress());
        transport.setinfo(info);
        transport.debug = true;
        new Thread() {
            @Override
            public void run() {
                try {
                    String call = transport.call(soapAction, null, null, "");
                    final RetrueCodeHandler returnInfo = RetruenUtils.getReturnInfo(call, new RetrueCodeHandler());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (returnInfo != null && returnInfo.getString().equals("0")) {
                                Tos("请求成功");
                                //saveLastUser();
                                getProgressRequest();
                                saveInfo();
                            } else if (returnInfo != null && returnInfo.getString().equals("-1")) {
                                Tos(returnInfo.getError());
                                saveInfo();
                            } else {
                                Tos("请求失败");
                            }
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    closeProgressDialog();
                }
                //mListView.onRefreshComplete();
            }
        }.start();
    }

    private void saveInfo() {

    }

    private void getProgressRequest() {
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
                                UpdateCargoInfos(progressRequest.getRequestId());
                                flash();
                            } else {
                                Tos("解析错误");
                            }
                        } else if (retrueCode != null && retrueCode.getString().equals("-1")) {
                            Tos(retrueCode.getError());
                        } else {
                            Tos("请求数据错误");
                        }
                        closeProgressDialog();
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        closeProgressDialog();
                        Tos(msg);
                    }
                });


    }

    @Override
    protected void onDestroy() {
        if (myReceiver != null) {
            unregisterReceiver(myReceiver);
        }
        super.onDestroy();
    }

    @Override
    public void onResume() {
        flash();

        if (mUser != null) {
            ll_sendPeople.removeAllViews();
            addView(ll_sendPeople, mUser);
        }
        super.onResume();
    }

    private void getDocumentLocation(String requestId) {
        String url = Urls.GetRequestHandlerCoordinate;
        RequestParams params = new RequestParams();
        params.addBodyParameter("sessionId", BaseApplication.loginInfo.getSessionId());
        params.addBodyParameter("userId", BaseApplication.loginInfo.getUserId() + "");
        params.addBodyParameter("clientName", BaseSettings.CLIENT_NAME);
        params.addBodyParameter("requestId", requestId);
        BaseApplication.mHttpUtils.send(HttpRequest.HttpMethod.POST,
                url, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        LogU.e(responseInfo.result);
                        RetrueCodeHandler retrueCode = RetruenUtils.getReturnInfo(responseInfo.result, new RetrueCodeHandler());
                        if (retrueCode != null && retrueCode.getString().equals("0")) {
                            HandlerCoordinateXmlParser parser = RetruenUtils.getReturnInfo(responseInfo.result, new HandlerCoordinateXmlParser());
                            if (parser != null) {
                                final HandlerCoordinate coordinate = parser.getCoordinate();
                                BaseApplication.coordinate = coordinate;
                                if (TextUtils.isEmpty(coordinate.getDriverName())) {
                                    tv_status.setText("等待司机接收");
                                    tv_callNum.setText("");
                                    tv_drive_num.setText("");
                                    tv_drive_address.setText("");
                                    return;
                                }
                                tv_status.setText("司机：" + coordinate.getDriverName());
                                tv_drive_num.setText("车牌号：" + coordinate.getTruckNumber());
                                tv_callNum.setText("电话：" + coordinate.getDriverMobile());
                                tv_callNum.setTextColor(Color.BLUE);
                                tv_callNum.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        if (OnClickutils.isFastDoubleClick()) {
                                            return;
                                        }
                                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + coordinate.getDriverMobile()));
                                        if (TextUtils.isEmpty(coordinate.getDriverMobile())) {
                                            Toast.makeText(SendQuestActivity.this, "电话号码有误", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        startActivity(intent);
                                    }
                                });
                                LatLng latLng = new LatLng(Double.valueOf(coordinate.getCoordinate()[0]), Double.valueOf(coordinate.getCoordinate()[1]));
                                getAddress(latLng);
                                setDistance(latLng);
                            }
                        } else if (retrueCode != null && retrueCode.getString().equals("-1")) {
                            Tos(retrueCode.getError());
                        } else {
                            Tos("请求数据错误");
                        }
                        closeProgressDialog();
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        closeProgressDialog();
                        Tos(msg);
                    }
                });
    }

    //司机位置
    private void setDistance(LatLng latLng) {
        //目的地
        String latitude = BaseApplication.progressRequest.getLatitude();
        String longitude = BaseApplication.progressRequest.getLongitude();
        if (latitude.equals("0.0000") && longitude.equals("0.0000")) {
            String address = BaseApplication.progressRequest.getAddress().replace(" ", "");
            getBestLocaltion(address, latLng);
            return;
        }
        double distance = DistanceUtil.getDistance(latLng, new LatLng(Double.valueOf(latitude), Double.valueOf(longitude)));
        tv_distance.setText("距离接货地点还有：" + new Double(distance).intValue() + "米");
    }

    private void getBestLocaltion(String shipperStr, final LatLng latLng) {
        String url = BaseSettings.plaseUrl + shipperStr + BaseSettings.plaseUrlParameter;
        BaseApplication.mHttpUtils.send(HttpRequest.HttpMethod.POST,
                url,
                //params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        try {
                            JSONObject jsonObject = new JSONObject(responseInfo.result);
                            String status = jsonObject.getString("status");
                            if (status.equals("0")) {
                                List<BastLocationJson> list = BastLocationJson.arrayBastLocationJsonFromData(jsonObject.getString("results"));
                                if (list.size() == 0) {
                                    tv_distance.setText("录入的地址无法解析请进入地图查看");
                                    return;
                                }
                                double distance = DistanceUtil.getDistance(latLng, new LatLng(list.get(0).getLocation().getLat(), list.get(0).getLocation().getLng()));
                                tv_distance.setText("距离接货地点还有：" + new Double(distance).intValue() + "米");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(SendQuestActivity.this, "地址不明确无法定位", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        Toast.makeText(SendQuestActivity.this, "无法获取数据", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getAddress(final LatLng latLng) {
        String url = "http://api.map.baidu.com/geocoder/v2/?ak=sfuQIH09bvPcDELFp0vIkyQ0nGRd07Tw&callback=renderReverse&" +
                "location=" + latLng.latitude + "," + latLng.longitude + "&output=xml&pois=1";
        BaseApplication.mHttpUtils.send(HttpRequest.HttpMethod.GET, url, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                LocationXmlParser parser = RetruenUtils.getReturnInfo(responseInfo.result, new LocationXmlParser());
                assert parser != null;
                {
                    tv_drive_address.setText("地址：" + parser.getAddress());
                }

            }

            @Override
            public void onFailure(HttpException error, String msg) {

            }
        });
    }

    @Override
    public void flash() {
        if (BaseApplication.progressRequest != null && !BaseApplication.progressRequest.getStatus().equals("IsRequest")) {
            getDocumentLocation(BaseApplication.progressRequest.getRequestId());
        } else if (BaseApplication.progressRequest != null && (!BaseApplication.progressRequest.getStatus().equals("IsRespond") || !BaseApplication.progressRequest.getStatus().equals("IsRequest"))) {
            tv_status.setText("等待收货员接收");
            tv_callNum.setText("");
            tv_distance.setText("");
            tv_drive_address.setText("");
            tv_drive_num.setText("");
        }
        try {
            if (BaseApplication.progressRequest != null) {
                btn_cancelDocument.setText("取消订单");
                btnShipOK.setText("订单正在进行中(点击进入地图)");
                ll_send.setVisibility(View.GONE);
                ll_cancelDocument.setVisibility(View.VISIBLE);
                canclecardView.setVisibility(View.VISIBLE);

                btnShipOK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (BaseApplication.progressRequest.getStatus().equals("IsRequest")) {
                            Tos("等待收货员接收请求");
                            return;
                        }
                        inToMap();
                    }
                });
            } else {
                btn_cancelDocument.setText("当前无订单");
                etCancelReason.setVisibility(View.GONE);
                btn_cancelDocumentOk.setVisibility(View.GONE);
                ll_send.setVisibility(View.VISIBLE);
                ll_cancelDocument.setVisibility(View.GONE);
                canclecardView.setVisibility(View.GONE);
                btnShipOK.setText("确认发货");
                btnShipOK.setOnClickListener(this);
            }
        } catch (Exception e) {

        }

    }

    private void inToMap() {
        Intent intent = new Intent(this, MapActivity.class);
        intent.putExtra("requestId", BaseApplication.progressRequest.getRequestId());
        startActivity(intent);
    }
}
