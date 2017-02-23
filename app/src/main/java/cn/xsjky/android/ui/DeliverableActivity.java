package cn.xsjky.android.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import cn.xsjky.android.BaseApplication;
import cn.xsjky.android.BaseSettings;
import cn.xsjky.android.R;
import cn.xsjky.android.adapter.ListItemDeliverableAdapter;
import cn.xsjky.android.constant.SoapAction;
import cn.xsjky.android.constant.SoapEndpoint;
import cn.xsjky.android.constant.SoapInfo;
import cn.xsjky.android.model.DocumentEntity;
import cn.xsjky.android.model.Infos;
import cn.xsjky.android.model.LoginInfo;
import cn.xsjky.android.util.CallBackString;
import cn.xsjky.android.util.DocumentHandler;
import cn.xsjky.android.util.MySoap;
import cn.xsjky.android.util.MySoapHttpRequest;
import cn.xsjky.android.util.StrKit;
import cn.xsjky.android.util.XmlParserUtil;
import cn.xsjky.android.weiget.LoadingDialog;

public class DeliverableActivity extends Activity {
    private final static int SCANNING_REQUEST_CODE = 1;
    private PullToRefreshListView mListView;
    private ImageView mFlip;
    private TextView mTitle;
    private Context mContext;
    private ListItemDeliverableAdapter mAdapter;
    private List<DocumentEntity> documents;
    private ImageView ivScan;
    private List<DocumentEntity> documentss;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_list);
        mContext = this;
        findViewById();
        getRemoteInfo();
    }

    private void findViewById() {
        mFlip = (ImageView) this.findViewById(R.id.head_flip);
        mFlip.setVisibility(View.VISIBLE);
        mFlip.setImageResource(R.drawable.ic_flipper_head_back);
        mFlip.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                DeliverableActivity.this.finish();
            }
        });
        mTitle = (TextView) this.findViewById(R.id.head_title);
        mTitle.setText("待派件");
        ivScan = (ImageView) findViewById(R.id.head_tool1);
        ivScan.setVisibility(View.VISIBLE);
        ivScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scan();
            }
        });
        mListView = (PullToRefreshListView) this.findViewById(R.id.list_listview);
        documents = new ArrayList<>();
        mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                getRemoteInfo();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

            }
        });

        mListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                //TODO 修改
                //getDoucmentDetail("755000028406");
                Toast.makeText(DeliverableActivity.this, "通过右上角条形码扫描进入相应的订单详情", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getRemoteInfo() {
        String endPoint = BaseSettings.WEBSERVICE_URL;
        // SOAP Action
        final LoginInfo loginInfo = BaseApplication.loginInfo;
        if (loginInfo == null)
            return;
        final String soapAction = "http://www.xsjky.cn/GetDeliverableDocuments";
        final MySoap transport = new MySoap(endPoint);
        String info = Infos.DELIVERABLE;
        info = info.replace("UserIdValue", loginInfo.getUserId() + "");
        info = info.replace("ClientNameValue", Infos.CLIENTNAME);
        info = info.replace("SessionIdValue", loginInfo.getSessionId());
        info = info.replace("RoleDataValue", "0");
        info = info.replace("returnDataValue", "true");
        /*info = info.replace("pageNumberValue", "1");
        info = info.replace("pageSizeValue", "10");*/
        //Log.e("info", info);
        transport.setinfo(info);
        transport.debug = true;
        new Thread() {
            @Override
            public void run() {
                try {
                    String call = transport.call(soapAction, null, null, "");
                    SAXParserFactory factory = SAXParserFactory.newInstance();
                    SAXParser parser = factory.newSAXParser();
                    DocumentHandler handler = new DocumentHandler();
                    ByteArrayInputStream tInputStringStream = new ByteArrayInputStream(call.getBytes());
                    parser.parse(tInputStringStream, handler);
                    documents = handler.getList();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter = new ListItemDeliverableAdapter<DocumentEntity>(DeliverableActivity.this, documents);
                            if (documents.size() == 0) {
                                Toast.makeText(DeliverableActivity.this, "暂时没有待派件", Toast.LENGTH_SHORT).show();
                            }
                            mListView.setAdapter(mAdapter);
                        }
                    });
                    // Log.e("documents", documents.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mListView.onRefreshComplete();
                        }
                    });
                }
                //mListView.onRefreshComplete();
            }
        }.start();

    }


    private void setHandler(int code, Object obj) {
        Message msg = new Message();
        msg.what = code;
        msg.obj = obj;
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
                    LoadingDialog.show(DeliverableActivity.this, false, false);
                    break;
                case HIDE_LOADING:
                    LoadingDialog.dismiss();
                    break;
                case SHOW_INFO:
                    Toast.makeText(DeliverableActivity.this, (String) msg.obj, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    private void scan() {
        Intent intent = new Intent();
        intent.setClass(DeliverableActivity.this, ScannerActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(intent, SCANNING_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SCANNING_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    //显示扫描到的内容
                    String result = bundle.getString("result");
                    if (TextUtils.isEmpty(result)) {
                        Toast.makeText(DeliverableActivity.this, "运单号不存在", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    getDoucmentDetail(result);
                }
                break;
        }
    }

    private void getDoucmentDetail(String result) {
        final String endPoint = SoapEndpoint.SET_IS_SIGNUP;
        final String soapAction = SoapAction.GETDOCUMENTBYNUM;
        final LoginInfo loginInfo = BaseApplication.loginInfo;
        String info = SoapInfo.GETDOCUMENTBYNUM;
        info = info.replace("UserIdValue", loginInfo.getUserId() + "");
        info = info.replace("ClientNameValue", Infos.CLIENTNAME);
        info = info.replace("SessionIdValue", loginInfo.getSessionId());
        info = info.replace("RoleDataValue", "0");
        info = info.replace("documentNumberValue", result);
        final String finalInfo = info;
        MySoapHttpRequest.getString(new CallBackString() {
            @Override
            public void httFinsh(final String data) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (StrKit.isBlank(data)) {
                            Toast.makeText(DeliverableActivity.this, "数据获取为空", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        XmlParserUtil parser = XmlParserUtil.getInstance();
                        parser.parse(data);
                        String prefix = "/soap:Envelope/soap:Body/GetDocumentByNumberResponse/GetDocumentByNumberResult/";
                        String code = parser.getNodeValue(prefix + "ReturnCode");
                        if (StrKit.isBlank(code) || !code.equals("0")) {
                            String error = parser.getNodeValue(prefix + "Error");
                            Toast.makeText(DeliverableActivity.this, "数据获取错误:" + error, Toast.LENGTH_SHORT).show();
                        } else {
                            prefix = prefix + "Value/";
                            DocumentEntity entity = new DocumentEntity();
                            try {
                                entity.setDocumentId(Integer.valueOf(parser.getNodeValue(prefix + "RecordId")));
                            } catch (Exception e) {
                                Toast.makeText(DeliverableActivity.this, "操作成功，但是没有记录", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            entity.setConsigneeName(parser.getNodeValue(prefix + "ConsigneeName"));
                            entity.setDocumentNumber(parser.getNodeValue(prefix + "DocumentNumber"));
                            entity.setConsigneeTel(parser.getNodeValue(prefix + "ConsigneePhoneNumber"));
                            String address1 = parser.getNodeValue(prefix + "ShipperAddress/Province")
                                    + parser.getNodeValue(prefix + "ShipperAddress/City")
                                    + parser.getNodeValue(prefix + "ShipperAddress/District")
                                    + parser.getNodeValue(prefix + "ShipperAddress/Address");
                            entity.setAddressLine1(address1);
                            String address2 = parser.getNodeValue(prefix + "ConsigneeAddress/Province")
                                    + parser.getNodeValue(prefix + "ConsigneeAddress/City")
                                    + parser.getNodeValue(prefix + "ConsigneeAddress/District");
                            entity.setAddressLine2(address2);
                            entity.setQuantity(parser.getNodeValue(prefix + "Quantity"));
                            entity.setWeight(parser.getNodeValue(prefix + "Weight"));
                            entity.setShippingMode(parser.getNodeValue(prefix + "ShippingMode/ModeName"));
                            entity.setShippingStatus(parser.getNodeValue(prefix + "ShippingStatus"));
                            if (TextUtils.isEmpty(entity.getDocumentNumber())) {
                                Toast.makeText(DeliverableActivity.this, "无法找到此运单号", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            Intent intent = new Intent(DeliverableActivity.this, ActivityDocumentDetailActivity.class);
                            intent.putExtra("document", (Serializable) entity);
                            startActivity(intent);
                        }
                    }
                });
            }
        }, endPoint, soapAction, finalInfo);

    }

}
