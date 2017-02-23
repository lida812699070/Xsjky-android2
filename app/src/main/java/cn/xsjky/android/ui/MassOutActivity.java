package cn.xsjky.android.ui;

import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapSerializationEnvelope;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import cn.xsjky.android.BaseApplication;
import cn.xsjky.android.BaseSettings;
import cn.xsjky.android.R;
import cn.xsjky.android.adapter.ItemMassOutAdapter;
import cn.xsjky.android.constant.SoapAction;
import cn.xsjky.android.constant.SoapEndpoint;
import cn.xsjky.android.constant.SoapInfo;
import cn.xsjky.android.http.Urls;
import cn.xsjky.android.model.HandoverDocument;
import cn.xsjky.android.model.LoginInfo;
import cn.xsjky.android.model.QueryHandoverInfoResult;
import cn.xsjky.android.util.HandoverDocumentXmlParser;
import cn.xsjky.android.util.LogU;
import cn.xsjky.android.util.MySoap;
import cn.xsjky.android.util.QueryHandoverInfoXmlParser;
import cn.xsjky.android.util.RetrueCodeHandler;
import cn.xsjky.android.util.RetruenUtils;

public class MassOutActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout mainReceiveLayout;
    private LinearLayout llAddDownloadActors;
    private LinearLayout llModeFlag2;
    private TextView tvSelectTracking;
    private ListView lvTracking;
    private String toCity;
    private ItemMassOutAdapter<HandoverDocument> adapter;
    private TextView mTitle;
    private ImageView mBack;
    private ArrayList<String> listDownLoadActors;
    private CheckBox checkBoxTrue;
    private CheckBox checkBoxFalse;
    private Button btnReturnAll;
    private String truckNum="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mass_out);
        editTextDownLoadActors = new ArrayList<>();
        findViewById();
        InitData();
        addDownLoadActors();
    }

    private void InitData() {
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
                                Toast.makeText(MassOutActivity.this, "无可交接记录", Toast.LENGTH_SHORT).show();
                                finish();//关闭窗口
                            }
                        } else {
                            Toast.makeText(MassOutActivity.this, "数据解析失败", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        //LogU.e(msg);
                        Toast.makeText(MassOutActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }

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
                        Toast.makeText(MassOutActivity.this, msg, Toast.LENGTH_SHORT).show();
                        closeProgressDialog();
                    }
                });
    }

    private ArrayList<HandoverDocument> list=new ArrayList<>();
    private void setData(ResponseInfo<String> responseInfo) {
        HandoverDocumentXmlParser parser = RetruenUtils.getReturnInfo(responseInfo.result, new HandoverDocumentXmlParser());
        if (parser != null) {
            list.addAll(parser.getList());
            if (list.size() == 0) {
                Toast.makeText(MassOutActivity.this, "无记录", Toast.LENGTH_SHORT).show();
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
            //Todo Adapter
            adapter = new ItemMassOutAdapter<>(MassOutActivity.this, list);
            lvTracking.setAdapter(adapter);
            closeProgressDialog();
        } else {
            Toast.makeText(MassOutActivity.this, "数据解析错误", Toast.LENGTH_SHORT).show();
            closeProgressDialog();
        }
    }

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
        toCity = getEtSearchAddress().getText().toString();
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
                        Toast.makeText(MassOutActivity.this, msg, Toast.LENGTH_SHORT).show();
                        closeProgressDialog();
                    }
                });
    }

    private void findViewById() {
        mTitle = (TextView) this.findViewById(R.id.head_title);
        mTitle.setText("批量转出模式");
        mBack = (ImageView) this.findViewById(R.id.head_flip);
        mBack.setImageResource(R.drawable.ic_flipper_head_back);
        mBack.setVisibility(View.VISIBLE);
        mBack.setOnClickListener(this);
        mainReceiveLayout = (LinearLayout) findViewById(R.id.main_receive_layout);
        findViewById(R.id.btn_addhandover_scanner).setOnClickListener(this);
        llAddDownloadActors = (LinearLayout) findViewById(R.id.ll_add_downloadActors);
        findViewById(R.id.btn_add_downloadActors).setOnClickListener(this);
        findViewById(R.id.btn_search_address).setOnClickListener(this);
        llModeFlag2 = (LinearLayout) findViewById(R.id.ll_mode_flag2);
        findViewById(R.id.btn_return_all).setOnClickListener(this);
        tvSelectTracking = (TextView) findViewById(R.id.tv_select_tracking);
        lvTracking = (ListView) findViewById(R.id.lv_tracking);
        checkBoxTrue = (CheckBox) findViewById(R.id.ckb_select_all_true);
        checkBoxFalse = (CheckBox) findViewById(R.id.ckb_select_all_false);

        checkBoxTrue.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                adapter.selectAll();
            }
        });
        checkBoxFalse.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                adapter.selectFalseAll();
            }
        });
    }

    private EditText getHandoverToemployee() {
        return (EditText) findViewById(R.id.handover_toemployee);
    }

    private EditText getEtSearchAddress() {
        return (EditText) findViewById(R.id.et_search_address);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_addhandover_scanner:
                //TODO implement
                Toast.makeText(MassOutActivity.this,"目前暂时不支持此功能",Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_add_downloadActors:
                addDownLoadActors();
                break;
            case R.id.btn_search_address:
                if (!TextUtils.isEmpty(BaseApplication.userBindTool)) {
                    Toast.makeText(MassOutActivity.this, "货车无法目的查询", Toast.LENGTH_SHORT).show();
                    return;
                }
                toCity = getEtSearchAddress().getText().toString();
                list.clear();
                InitData();
                break;
            case R.id.btn_return_all:
                //TODO implement
                addHandover();
                break;
            case R.id.head_flip:
                finish();
                break;
        }
    }

    private ArrayList<EditText> editTextDownLoadActors;
    private void addDownLoadActors() {
        final LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        Button button = new Button(this);
        button.setText("删除搬运工");
        linearLayout.addView(button);
        final EditText editText = new EditText(this);
        editText.setHint("输入搬运工工号");
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        editText.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        linearLayout.addView(editText);
        llAddDownloadActors.addView(linearLayout);
        editTextDownLoadActors.add(editText);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llAddDownloadActors.removeView(linearLayout);
                editTextDownLoadActors.remove(editText);
            }
        });
    }
    public void addHandover() {
        String strToemployee = getHandoverToemployee().getText().toString();
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
        postOut();
    }

    private void postOut() {
        String documnetValue = "";
        for (int i = 0; i < list.size(); i++) {
            if (!ItemMassOutAdapter.isSelected.get(i)){
                continue;
            }
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
        info = info.replace("TakeoverCodeValue", getHandoverToemployee().getText().toString());
        info = info.replace("returnValueValue", true + "");

        info = info.replace("DocumentsValue", documnetValue);
        String ActorsValue = "";
        for (int i = 0; i < listDownLoadActors.size(); i++) {
            ActorsValue += "<EmployeeInfo>\n" +
                    "            <EmplId>0</EmplId>\n" +
                    "            <EmplCode>" + listDownLoadActors.get(i) + "</EmplCode>\n" +
                    "            <EmplName></EmplName>\n" +
                    "            <ContactNumber></ContactNumber>\n" +
                    "          </EmployeeInfo>";
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
                                    Toast.makeText(MassOutActivity.this, "批量转出成功", Toast.LENGTH_SHORT).show();
                                } else {
                                    String error = handler.getError();
                                    Toast.makeText(MassOutActivity.this, error, Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(MassOutActivity.this, "网络请求错误", Toast.LENGTH_SHORT).show();
                            closeProgressDialog();
                        }
                    });
                    //LogU.e(e.getMessage());
                }
            }
        }.start();
    }
}
