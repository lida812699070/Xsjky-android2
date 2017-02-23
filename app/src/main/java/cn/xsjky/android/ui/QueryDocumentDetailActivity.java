package cn.xsjky.android.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.http.RequestParams;

import java.util.ArrayList;

import cn.xsjky.android.BaseApplication;
import cn.xsjky.android.BaseSettings;
import cn.xsjky.android.R;
import cn.xsjky.android.adapter.ItemSimple2Adapter;
import cn.xsjky.android.constant.SoapAction;
import cn.xsjky.android.constant.SoapEndpoint;
import cn.xsjky.android.http.Urls;
import cn.xsjky.android.model.Document;
import cn.xsjky.android.model.Infos;
import cn.xsjky.android.model.LoginInfo;
import cn.xsjky.android.model.ShippingTraceData;
import cn.xsjky.android.model.ShippingTraceItem;
import cn.xsjky.android.util.CallBackString;
import cn.xsjky.android.util.LogU;
import cn.xsjky.android.util.MySoapHttpRequest;
import cn.xsjky.android.util.RetrueCodeHandler;
import cn.xsjky.android.util.RetruenUtils;
import cn.xsjky.android.util.ShippingTraceDataXmlparser;

public class QueryDocumentDetailActivity extends BaseActivity {

    private String documentNumber;
    private TextView mTvDocumentNumber;
    private TextView mTvPic;
    private ListView mLvMeg;
    private ShippingTraceData traceData;
    private ArrayList<ShippingTraceItem> items;
    private ItemSimple2Adapter<ShippingTraceItem> adapter;
    private Button mBtnPrintDocument;
    private Document mDocument;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_document_detail);
        initToolbar("跟踪运单");
        documentNumber = getIntent().getStringExtra("documentNumber");
        findViewById();
        initData();
    }

    private void findViewById() {
        mTvDocumentNumber = (TextView) findViewById(R.id.tvDocumentNum);
        mBtnPrintDocument = (Button) findViewById(R.id.btnPrintDocument);
        mTvPic = (TextView) findViewById(R.id.tv_picture);
        mLvMeg = (ListView) findViewById(R.id.lv_SpMessage);
        items = new ArrayList<>();
        adapter = new ItemSimple2Adapter<>(this, items);
        mLvMeg.setAdapter(adapter);
        mBtnPrintDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initApplyDocuemntActivity();
            }
        });
    }

    private void initApplyDocuemntActivity() {
        showProgressDialog();
        final String endPoint = SoapEndpoint.SET_IS_SIGNUP;
        final String soapAction = SoapAction.GetDocumentByNumber;
        String info = Infos.GetDocumentByNumber;
        info = info.replace("UserIdValue", BaseApplication.loginInfo.getUserId() + "");
        info = info.replace("ClientNameValue", BaseSettings.CLIENT_NAME);
        info = info.replace("SessionIdValue", BaseApplication.loginInfo.getSessionId());
        info = info.replace("documentNumberValue", documentNumber);
        final String finalInfo = info;
        MySoapHttpRequest.getString(new CallBackString() {
            @Override
            public void httFinsh(final String data) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        RetrueCodeHandler returnInfo = RetruenUtils.getReturnInfo(data, new RetrueCodeHandler());
                        if (returnInfo != null && returnInfo.getString().equals("0")) {
                            mDocument = new Document();
                            mDocument.parser2XML(data);
                            BaseApplication.getApplication().setDocument(mDocument);
                            startActivity(new Intent(QueryDocumentDetailActivity.this,ApplyDocumentDetailActivity.class));
                        } else if (returnInfo != null && returnInfo.equals("-1")) {
                            Tos(returnInfo.getError());
                        } else {
                            Tos("数据获取错误");
                        }
                        closeProgressDialog();
                    }
                });
            }
        }, endPoint, soapAction, finalInfo);
    }


    public void lookPic(View view) {
        Intent intent = new Intent(this, ImageDetailActivity.class);
        intent.putExtra("document", documentNumber);
        startActivity(intent);
    }

    private void initData() {
        showProgressDialog();
        final String endPoint = SoapEndpoint.SET_IS_SIGNUP;
        final String soapAction = SoapAction.QueryTraceData;
        String info = Infos.QueryTraceData;
        info = info.replace("userIdValue", BaseApplication.loginInfo.getUserId() + "");
        info = info.replace("clientNameValue", BaseSettings.CLIENT_NAME);
        info = info.replace("sessionIdValue", BaseApplication.loginInfo.getSessionId());
        info = info.replace("stringValue", documentNumber);
        final String finalInfo = info;
        MySoapHttpRequest.getString(new CallBackString() {
            @Override
            public void httFinsh(final String data) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        RetrueCodeHandler returnInfo = RetruenUtils.getReturnInfo(data, new RetrueCodeHandler());
                        if (returnInfo != null && returnInfo.getString().equals("0")) {
                            ShippingTraceDataXmlparser xmlparser = RetruenUtils.getReturnInfo(data, new ShippingTraceDataXmlparser());
                            if (xmlparser != null) {
                                traceData = xmlparser.getUser();
                                mTvDocumentNumber.setText("订单号：" + traceData.getDocumentNumber());
                                items.addAll(traceData.getShippingMessage().getList());
                                adapter.notifyDataSetChanged();
                            } else {
                                Tos("解析错误");
                            }
                        } else if (returnInfo != null && returnInfo.equals("-1")) {
                            Tos(returnInfo.getError());
                        } else {
                            Tos("数据获取错误");
                        }
                        closeProgressDialog();
                    }
                });
            }
        }, endPoint, soapAction, finalInfo);
    }


}
