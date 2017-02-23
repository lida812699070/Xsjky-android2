package cn.xsjky.android.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapSerializationEnvelope;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import cn.xsjky.android.BaseApplication;
import cn.xsjky.android.BaseSettings;
import cn.xsjky.android.R;
import cn.xsjky.android.constant.SoapAction;
import cn.xsjky.android.constant.SoapEndpoint;
import cn.xsjky.android.constant.SoapInfo;
import cn.xsjky.android.http.Urls;
import cn.xsjky.android.model.HandoverDocument;
import cn.xsjky.android.model.LoginInfo;
import cn.xsjky.android.model.MyHandOverItem;
import cn.xsjky.android.model.MyHandOverRecord;
import cn.xsjky.android.model.QueryHandoverInfoResult;
import cn.xsjky.android.model.TruckLoadedCargo;
import cn.xsjky.android.util.HandoverDocumentXmlParser;
import cn.xsjky.android.util.LogU;
import cn.xsjky.android.util.MySoap;
import cn.xsjky.android.util.MyhandOverXmlPaser;
import cn.xsjky.android.util.MyhandOverXmlPaser2;
import cn.xsjky.android.util.QueryHandoverInfoXmlParser;
import cn.xsjky.android.util.RetrueCodeHandler;
import cn.xsjky.android.util.RetruenUtils;

public class AddHandoverActivity extends BaseActivity implements View.OnClickListener {

    private TextView mTitle;
    private ImageView mBack;
    private Button btnSave;
    private Button mBtnAddDownLoadActors;
    private LinearLayout llAddDownLoadActor;
    private EditText etToemployee;
    private ArrayList<EditText> editTextDownLoadActors;
    private ArrayList<String> listDownLoadActors;
    private boolean flag;
    private MyHandOverRecord handOverRecord;
    private ListView lvTracking;
    private ArrayList<HandoverDocument> list;
    private ItemTrackingLvAdapter<HandoverDocument> adapter;
    private Button btnscanner;
    private List<MyHandOverRecord> list1;
    private ItemTrackingLvAdapter<MyHandOverItem> adapter2;
    private List<String> actors;
    private TextView tvSelect;
    private TextView tvReturnOut;
    private ImageView ivAllReturnOut;
    private boolean flag2;
    private LinearLayout llModeFlag2;
    private CheckBox ckbSellectAllTrue;
    private CheckBox ckbSellectAllFalse;
    private Button btnReturnAll;
    private ArrayList<String> documentNums;
    private String title;
    private EditText etSearchAddress;
    private Button btnSearchAddress;
    private String truckNum = "";
    private String sourceNetwork = "";
    private Button btnRtAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_handover);
        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        flag = intent.getBooleanExtra("flag", false);
        flag2 = intent.getBooleanExtra("flag2", false);
        editTextDownLoadActors = new ArrayList<>();
        findViewById();
        setListener();
        btnSave.setVisibility(View.GONE);
        if (flag) {
            handOverRecord = (MyHandOverRecord) intent.getSerializableExtra("data");
            actors = handOverRecord.getDownLoadActors().getList();
            for (int i = 0; i < actors.size(); i++) {
                mBtnAddDownLoadActors.performClick();
            }
            mBtnAddDownLoadActors.setClickable(false);
            etToemployee.setText(handOverRecord.getIssueUserName());
            //LogU.e(handOverRecord.toString());
        } else {
            mBtnAddDownLoadActors.performClick();
        }
        if (flag) {
            getNewWorkData2();
        } else {
            getNewWorkData();
        }
    }

    private void getNewWorkData() {
        String url = Urls.QueryHandoverInfo;
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
                        //setData(responseInfo);
                        LogU.e(responseInfo.result);
                        QueryHandoverInfoXmlParser parser = RetruenUtils.getReturnInfo(responseInfo.result, new QueryHandoverInfoXmlParser());
                        if (parser != null) {
                            QueryHandoverInfoResult infoResult = parser.getQueryHandoverInfoResult();
                            LogU.e(infoResult.toString());
                            if (!TextUtils.isEmpty(infoResult.getFromTruck())) {
                                //TODO 通过货车
                                getTruckLoadedDocuments(infoResult.getFromTruck());
                                truckNum = infoResult.getFromTruck();
                            } else if (!"0".equals(infoResult.getNetworkDocumentCount())) {
                                //TODO 通过网点
                                getNetworkDocuments();
                            } else {
                                Toast.makeText(AddHandoverActivity.this, "无可交接记录", Toast.LENGTH_SHORT).show();
                                finish();//关闭窗口
                            }
                        } else {
                            Toast.makeText(AddHandoverActivity.this, "数据解析失败", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        //LogU.e(msg);
                        Toast.makeText(AddHandoverActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private String toCity = "";

    private void getNetworkDocuments() {
        showProgressDialog();
        String url = Urls.GetNetworkDocuments;
        RequestParams params = new RequestParams();
        //TODO 添加参数
        params.addBodyParameter("sessionId", BaseApplication.loginInfo.getSessionId());
        params.addBodyParameter("userId", BaseApplication.loginInfo.getUserId() + "");
        params.addBodyParameter("clientName", BaseSettings.CLIENT_NAME);
        params.addBodyParameter("pageNumber", 0 + "");
        params.addBodyParameter("pageSize", "0");
        toCity = etSearchAddress.getText().toString();
        params.addBodyParameter("toCity", toCity);
        BaseApplication.mHttpUtils.send(HttpRequest.HttpMethod.POST,
                url,
                params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        LogU.e(responseInfo.result);
                        setData(responseInfo);

                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        Toast.makeText(AddHandoverActivity.this, msg, Toast.LENGTH_SHORT).show();
                        closeProgressDialog();
                    }
                });
    }

    private int page = 1;

    private void getTruckLoadedDocuments(String truckNumber) {
        showProgressDialog();
        String url = Urls.GetTruckLoadedDocuments;
        RequestParams params = new RequestParams();
        params.addBodyParameter("sessionId", BaseApplication.loginInfo.getSessionId());
        params.addBodyParameter("userId", BaseApplication.loginInfo.getUserId() + "");
        params.addBodyParameter("clientName", BaseSettings.CLIENT_NAME);
        params.addBodyParameter("pageNumber", 0 + "");
        params.addBodyParameter("pageSize", "0");
        params.addBodyParameter("truckNumber", truckNumber);
        BaseApplication.mHttpUtils.send(HttpRequest.HttpMethod.POST,
                url,
                params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        setData(responseInfo);
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        Toast.makeText(AddHandoverActivity.this, msg, Toast.LENGTH_SHORT).show();
                        closeProgressDialog();
                    }
                });
    }

    private void setData(ResponseInfo<String> responseInfo) {
        HandoverDocumentXmlParser parser = RetruenUtils.getReturnInfo(responseInfo.result, new HandoverDocumentXmlParser());
        if (parser != null) {
            list.addAll(parser.getList());
            if (list.size() == 0) {
                Toast.makeText(AddHandoverActivity.this, "无记录", Toast.LENGTH_SHORT).show();
            }
            ArrayList<HandoverDocument> cargos = new ArrayList<>();
            ArrayList<String> cityList = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                String toCity = list.get(i).getToCity();
                if (!cityList.contains(toCity)) {
                    cityList.add(toCity);
                }
            }
            for (int i = 0; i < cityList.size(); i++) {
                String city = cityList.get(i);
                for (int j = 0; j < list.size(); j++) {
                    if (city.equals(list.get(j).getToCity())) {
                        cargos.add(list.get(j));
                    }
                }
            }
            list.clear();
            list.addAll(cargos);
            adapter.notifyDataSetChanged();
            closeProgressDialog();
        } else {
            Toast.makeText(AddHandoverActivity.this, "数据解析错误", Toast.LENGTH_SHORT).show();
            closeProgressDialog();
        }
    }

    private void getNewWorkData2() {
        showProgressDialog();
        RequestParams params = new RequestParams();
        params.addBodyParameter("sessionId", BaseApplication.loginInfo.getSessionId());
        params.addBodyParameter("userId", BaseApplication.loginInfo.getUserId() + "");
        params.addBodyParameter("clientName", BaseSettings.CLIENT_NAME);
        params.addBodyParameter("returnList", true + "");
        params.addBodyParameter("pageNumber", 0 + "");
        params.addBodyParameter("pageSize", 0 + "");
        params.addBodyParameter("sortPropertyName", "");
        params.addBodyParameter("sortDesending", false + "");
        params.addBodyParameter("recordId", handOverRecord.getRecordId());
        params.addBodyParameter("beginTime", "");
        params.addBodyParameter("endTime", "");
        params.addBodyParameter("receiveState", 1 + "");
        BaseApplication.mHttpUtils.send(HttpRequest.HttpMethod.POST,
                Urls.DURLGetHandOverRecords,
                params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        try {
                            String call = responseInfo.result;
                            SAXParserFactory factory1 = SAXParserFactory.newInstance();
                            SAXParser parser1 = factory1.newSAXParser();
                            RetrueCodeHandler handler1 = new RetrueCodeHandler();
                            ByteArrayInputStream tInputStringStream1 = new ByteArrayInputStream(call.getBytes());
                            parser1.parse(tInputStringStream1, handler1);
                            if (handler1.getString().equals("0")) {
                                SAXParserFactory factory = SAXParserFactory.newInstance();
                                SAXParser parser = factory.newSAXParser();
                                MyhandOverXmlPaser handler = new MyhandOverXmlPaser();
                                ByteArrayInputStream tInputStringStream = new ByteArrayInputStream(call.getBytes());
                                parser.parse(tInputStringStream, handler);
                                list1.addAll(handler.getList());
                                ArrayList<MyHandOverRecord> records = new ArrayList<>();
                                ArrayList<String> cityList = new ArrayList<>();
                                for (int i = 0; i < list1.size(); i++) {
                                    String tocity = list1.get(i).getListItem().get(0).getToCity();
                                    if (!cityList.contains(tocity)) {
                                        cityList.add(tocity);
                                    }
                                }
                                for (int i = 0; i < cityList.size(); i++) {
                                    String city = cityList.get(i);
                                    for (int j = 0; j < list1.size(); j++) {
                                        if (city.equals(list1.get(j).getListItem().get(0).getToCity())) {
                                            records.add(list1.get(j));
                                        }
                                    }
                                }
                                list1.clear();
                                list1.addAll(records);
                                adapter2 = new ItemTrackingLvAdapter<>(AddHandoverActivity.this, list1.get(0).getListItem());
                                lvTracking.setAdapter(adapter2);
                            } else {
                                Toast.makeText(AddHandoverActivity.this, handler1.getError(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(AddHandoverActivity.this, "数据错误", Toast.LENGTH_SHORT).show();
                        } finally {
                            closeProgressDialog();
                        }
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        Toast.makeText(AddHandoverActivity.this, "网络数据获取错误", Toast.LENGTH_SHORT).show();
                        closeProgressDialog();
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_flip:
                finish();
                break;
            case R.id.handover_save_btn:
                //addHandover("");
                break;
            case R.id.btn_add_downloadActors:
                addDownLoadActors();
                break;
            case R.id.btn_search_address:
                if (!TextUtils.isEmpty(BaseApplication.userBindTool)) {
                    Toast.makeText(AddHandoverActivity.this, "货车无法目的查询", Toast.LENGTH_SHORT).show();
                    return;
                }
                toCity = etSearchAddress.getText().toString();
                list.clear();
                list1.clear();
                getNewWorkData();
                break;
            case R.id.head_refresh:
                Intent intent = new Intent(this, MassOutActivity.class);
                if (!flag2) {
                    intent.putExtra("flag2", true);
                    intent.putExtra("title", "批量转出模式");
                }
                startActivity(intent);
                finish();
                break;
        }
    }

    private void setListener() {
        btnSearchAddress.setOnClickListener(this);
        mBack.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        mBtnAddDownLoadActors.setOnClickListener(this);
        ivAllReturnOut.setOnClickListener(this);
        ckbSellectAllTrue.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                for (int i = 0; i < list.size(); i++) {
                    View childAt = lvTracking.getChildAt(i);
                    CheckBox checkBox = (CheckBox) childAt.findViewById(R.id.checkbox_item_tracking);
                    checkBox.setChecked(isChecked);
                }
            }
        });
        ckbSellectAllFalse.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                for (int i = 0; i < list.size(); i++) {
                    CheckBox checkBox = (CheckBox) lvTracking.getChildAt(i).findViewById(R.id.checkbox_item_tracking);
                    checkBox.setChecked(!checkBox.isChecked());
                }
            }
        });
        documentNums = new ArrayList<>();
        btnReturnAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < AddHandoverActivity.this.list.size(); i++) {
                    CheckBox checkBox = (CheckBox) lvTracking.getChildAt(i).findViewById(R.id.checkbox_item_tracking);
                    if (checkBox.isChecked()) {
                        documentNums.add(list.get(i).getDocumentNumber());
                        addHandover("", null, 0);
                    }
                }
            }
        });
    }

    private void findViewById() {
        llModeFlag2 = (LinearLayout) findViewById(R.id.ll_mode_flag2);
        etSearchAddress = (EditText) findViewById(R.id.et_search_address);
        btnSearchAddress = (Button) findViewById(R.id.btn_search_address);
        ckbSellectAllTrue = (CheckBox) findViewById(R.id.ckb_select_all_true);//全选
        ckbSellectAllFalse = (CheckBox) findViewById(R.id.ckb_select_all_false);//反选
        btnReturnAll = (Button) findViewById(R.id.btn_return_all);//全部转出
        btnRtAll = (Button) findViewById(R.id.btn_returnAll);//全部转出
        btnRtAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 全部转出
                addHandover("全部转出", null, -1);
            }
        });
        if (flag2) {
            llModeFlag2.setVisibility(View.VISIBLE);
        }
        mTitle = (TextView) this.findViewById(R.id.head_title);
        etToemployee = (EditText) findViewById(R.id.handover_toemployee);
        ivAllReturnOut = (ImageView) findViewById(R.id.head_refresh);
        ivAllReturnOut.setVisibility(View.VISIBLE);
        if (TextUtils.isEmpty(title)) {
            title = "新增转出运单";
        }
        mTitle.setText(title);
        mBack = (ImageView) this.findViewById(R.id.head_flip);
        btnSave = (Button) findViewById(R.id.handover_save_btn);
        tvReturnOut = (TextView) findViewById(R.id.tv_returnOut_);
        btnscanner = (Button) findViewById(R.id.btn_addhandover_scanner);
        tvSelect = (TextView) findViewById(R.id.tv_select_tracking);
        if (flag2) {
            tvSelect.setVisibility(View.VISIBLE);
            tvReturnOut.setVisibility(View.GONE);
        } else {
            tvSelect.setVisibility(View.GONE);
            tvReturnOut.setVisibility(View.VISIBLE);
        }

        if (flag) {
            btnscanner.setClickable(false);
        }
        btnSave.setOnClickListener(this);
        mBack.setImageResource(R.drawable.ic_flipper_head_back);
        mBack.setVisibility(View.VISIBLE);
        mBtnAddDownLoadActors = (Button) findViewById(R.id.btn_add_downloadActors);
        llAddDownLoadActor = (LinearLayout) findViewById(R.id.ll_add_downloadActors);
        lvTracking = (ListView) findViewById(R.id.lv_tracking);
        lvTracking.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
        list = new ArrayList<>();
        list1 = new ArrayList<>();
        adapter = new ItemTrackingLvAdapter<>(this, list);

        if (flag) {

        } else {
            lvTracking.setAdapter(adapter);
        }
    }

    private int index = 0;

    private void addDownLoadActors() {
        final LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        Button button = new Button(this);
        button.setText("删除搬运工");
        linearLayout.addView(button);
        final EditText editText = new EditText(this);
        editText.setHint("输入搬运工工号");
        if (flag) {
            editText.setText(actors.get(index++));
        }
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        editText.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        linearLayout.addView(editText);
        llAddDownLoadActor.addView(linearLayout);
        editTextDownLoadActors.add(editText);
        if (!flag) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    llAddDownLoadActor.removeView(linearLayout);
                    editTextDownLoadActors.remove(editText);
                }
            });
        } else {
            button.setClickable(false);
        }
    }

    public void addHandover(String documentsValue, final View v, int postion) {
        String strToemployee = etToemployee.getText().toString();
        if (TextUtils.isEmpty(strToemployee)) {
            Toast.makeText(this, "编辑框不可以为空", Toast.LENGTH_SHORT).show();
            return;
        }
        listDownLoadActors = new ArrayList<>();

        for (int i = 0; i < editTextDownLoadActors.size(); i++) {
            String s = editTextDownLoadActors.get(i).getText().toString();
            if (TextUtils.isEmpty(s)) {
                Toast.makeText(this, "编辑框不可以为空", Toast.LENGTH_SHORT).show();
                return;
            }
            listDownLoadActors.add(s);
        }

        postOut(postion, v, strToemployee);
    }

    private void postOut(int postion, final View v, String strToemployee) {
        String documnetValue = "";
        if (postion == -1) {
            for (int i = 0; i < list.size(); i++) {
                HandoverDocument handoverDocument = list.get(i);
                documnetValue += "<HandoverDocument>\n" +
                        "            <DocumentId>" + handoverDocument.getDocumentId() + "</DocumentId>\n" +
                        "            <HandoverQty>" + handoverDocument.getHandoverQty() + "</HandoverQty>\n" +
                        "            <HandoverVolumn>" + handoverDocument.getHandoverVolumn() + "</HandoverVolumn>\n" +
                        "            <DocumentNumber>" + handoverDocument.getDocumentNumber() + "</DocumentNumber>\n" +
                        "            <ToCity>" + handoverDocument.getToCity() + "</ToCity>\n" +
                        "            <Remarks></Remarks>\n" +
                        "          </HandoverDocument>";
            }
        } else {
            HandoverDocument handoverDocument = list.get(postion);
            if (!flag) {
                documnetValue = "<HandoverDocument>\n" +
                        "            <DocumentId>" + handoverDocument.getDocumentId() + "</DocumentId>\n" +
                        "            <HandoverQty>" + handoverDocument.getHandoverQty() + "</HandoverQty>\n" +
                        "            <HandoverVolumn>" + handoverDocument.getHandoverVolumn() + "</HandoverVolumn>\n" +
                        "            <DocumentNumber>" + handoverDocument.getDocumentNumber() + "</DocumentNumber>\n" +
                        "            <ToCity>" + handoverDocument.getToCity() + "</ToCity>\n" +
                        "            <Remarks></Remarks>\n" +
                        "          </HandoverDocument>";
            }
        }
        final LoginInfo loginInfo = BaseApplication.loginInfo;
        String endPoint = SoapEndpoint.Handovers_EndPoint;
        final String soapAction = SoapAction.Handovers;
        final MySoap transport = new MySoap(endPoint);
        String info = SoapInfo.Handovers;
        info = info.replace("userIdValue", loginInfo.getUserId() + "");
        info = info.replace("clientNameValue", BaseSettings.CLIENT_NAME);
        info = info.replace("sessionIdValue", loginInfo.getSessionId());
        if (TextUtils.isEmpty(truckNum)){
            info = info.replace("SourceNetworkValue", BaseApplication.userOwnNetwork);
        }else {
            info = info.replace("SourceNetworkValue", "");
        }
        info = info.replace("SourceTruckValue", truckNum);
        info = info.replace("TakeoverCodeValue", strToemployee);
        info = info.replace("returnValueValue", true + "");

        info = info.replace("DocumentsValue", documnetValue);
        String ActorsValue = "";

        if (!flag) {
            for (int i = 0; i < listDownLoadActors.size(); i++) {
                ActorsValue += "<EmployeeInfo>\n" +
                        "            <EmplId>0</EmplId>\n" +
                        "            <EmplCode>" + listDownLoadActors.get(i) + "</EmplCode>\n" +
                        "            <EmplName></EmplName>\n" +
                        "            <ContactNumber></ContactNumber>\n" +
                        "          </EmployeeInfo>";
            }

        }
        info = info.replace("ActorsValue", ActorsValue);
        transport.setinfo(info);
        transport.debug = true;
        final LoginInfo finalLoginInfo = loginInfo;
        showProgressDialog();
        new Thread() {
            @Override
            public void run() {
                final SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

                // 设置是否调用的是dotNet开发的WebService
                envelope.dotNet = true;
                try {
                    final String call = transport.call(soapAction, envelope, null, "");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            LogU.e(call);
                            SAXParserFactory factory = SAXParserFactory.newInstance();
                            SAXParser parser = null;
                            try {
                                parser = factory.newSAXParser();
                                RetrueCodeHandler handler = new RetrueCodeHandler();
                                ByteArrayInputStream tInputStringStream = new ByteArrayInputStream(call.getBytes());
                                parser.parse(tInputStringStream, handler);
                                String returnCode = handler.getString();
                                if (returnCode.equals("0")) {
                                    Toast.makeText(AddHandoverActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                                    if (v != null) {
                                        v.setBackground(getResources().getDrawable(R.drawable.ic_blue_btn_press));
                                        v.setClickable(false);
                                    }
                                } else {
                                    String error = handler.getError();
                                    Toast.makeText(AddHandoverActivity.this, error, Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                closeProgressDialog();
                            }
                        }
                    });
                    //LogU.e(call);
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(AddHandoverActivity.this, "网络请求错误", Toast.LENGTH_SHORT).show();
                            closeProgressDialog();
                        }
                    });
                    //LogU.e(e.getMessage());
                }
            }
        }.start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case NewActivity.SCANNING_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    //显示扫描到的内容
                    etToemployee.setText(bundle.getString("result"));
                }
                break;
        }
    }

    public void scanner(View view) {
        Intent intent = new Intent();
        intent.setClass(AddHandoverActivity.this, ScannerActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(intent, NewActivity.SCANNING_REQUEST_CODE);
    }

    class ItemTrackingLvAdapter<T> extends BaseAdapter {

        private List<T> objects = new ArrayList<T>();
        private Context context;
        private LayoutInflater layoutInflater;

        public ItemTrackingLvAdapter(Context context, List<T> objects) {
            this.objects = objects;
            this.context = context;
            this.layoutInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return objects.size();
        }

        @Override
        public T getItem(int position) {
            return objects.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.item_tracking_lv, null);
                convertView.setTag(new ViewHolder(convertView));
            }
            initializeViews((T) getItem(position), (ViewHolder) convertView.getTag(), position);

            return convertView;
        }

        private void initializeViews(T object, final ViewHolder holder, final int position) {

            if (flag) {
                MyHandOverItem record = (MyHandOverItem) object;
                holder.tvTrackingLvDocuNum.setText(record.getDocumentNumber());
                holder.tvTrackingLvTocity.setText(record.getToCity());
                holder.tvTrackingLvQty.setText(record.getQuantity());
                holder.btnReturnOutItemTracking.setClickable(false);
            } else {
                HandoverDocument cargo = (HandoverDocument) object;
                holder.tvTrackingLvDocuNum.setText(cargo.getDocumentNumber());
                holder.tvTrackingLvTocity.setText(cargo.getToCity());
                holder.tvTrackingLvQty.setText(cargo.getHandoverQty());
                holder.btnReturnOutItemTracking.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AddHandoverActivity addHandoverActivity = (AddHandoverActivity) ItemTrackingLvAdapter.this.context;
                        addHandoverActivity.addHandover(holder.tvTrackingLvDocuNum.getText().toString(), v, position);
                    }
                });
            }
        }

        protected class ViewHolder {
            private CheckBox checkboxItemTracking;
            private TextView tvTrackingLvTocity;
            private TextView tvTrackingLvDocuNum;
            private TextView tvTrackingLvQty;
            private Button btnReturnOutItemTracking;

            public ViewHolder(View view) {
                checkboxItemTracking = (CheckBox) view.findViewById(R.id.checkbox_item_tracking);
                tvTrackingLvTocity = (TextView) view.findViewById(R.id.tv_tracking_lv_tocity);
                tvTrackingLvDocuNum = (TextView) view.findViewById(R.id.tv_tracking_lv_docuNum);
                tvTrackingLvQty = (TextView) view.findViewById(R.id.tv_tracking_lv_qty);
                btnReturnOutItemTracking = (Button) view.findViewById(R.id.btn_returnOut_item_tracking);
                if (flag2) {
                    checkboxItemTracking.setVisibility(View.VISIBLE);
                    btnReturnOutItemTracking.setVisibility(View.GONE);
                } else {
                    checkboxItemTracking.setVisibility(View.GONE);
                    btnReturnOutItemTracking.setVisibility(View.VISIBLE);
                }
                if (AddHandoverActivity.this.flag) {
                    btnReturnOutItemTracking.setClickable(false);
                }
            }
        }
    }

}
