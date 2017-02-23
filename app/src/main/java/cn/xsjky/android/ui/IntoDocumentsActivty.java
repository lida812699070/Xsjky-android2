package cn.xsjky.android.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
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

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import cn.xsjky.android.BaseApplication;
import cn.xsjky.android.BaseSettings;
import cn.xsjky.android.R;
import cn.xsjky.android.adapter.ItemIntoDocLvAdapter;
import cn.xsjky.android.http.HttpCallback;
import cn.xsjky.android.http.Urls;
import cn.xsjky.android.model.MyHandOverRecord;
import cn.xsjky.android.util.CallBack;
import cn.xsjky.android.util.CallBackString;
import cn.xsjky.android.util.DialoginOkCallBack;
import cn.xsjky.android.util.LogU;
import cn.xsjky.android.util.MyhandOverXmlPaser;
import cn.xsjky.android.util.RetrueCodeHandler;

public class IntoDocumentsActivty extends BaseActivity implements View.OnClickListener {

    private ImageView mBack;
    private TextView mTitle;
    private PullToRefreshListView lvIntoDocuments;
    private ItemIntoDocLvAdapter<MyHandOverRecord> adapter;
    private ArrayList<MyHandOverRecord> list;
    private TextView tvAdress;
    private Button btnChangeAdress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_into_documents_activty);
        findViewById();
        setListener();
        netWorkInitData();
    }

    private int page = 1;

    private void netWorkInitData() {
        initIntoDocument();
    }

    private void initIntoDocument() {
        showProgressDialog();
        RequestParams params = new RequestParams();
        params.addBodyParameter("sessionId", BaseApplication.loginInfo.getSessionId());
        params.addBodyParameter("userId", BaseApplication.loginInfo.getUserId() + "");
        params.addBodyParameter("clientName", BaseSettings.CLIENT_NAME);
        params.addBodyParameter("returnList", true + "");
        params.addBodyParameter("pageNumber", page + "");
        params.addBodyParameter("pageSize", 10 + "");
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
                                SAXParserFactory factory2 = SAXParserFactory.newInstance();
                                SAXParser parser2 = factory2.newSAXParser();
                                MyhandOverXmlPaser handler2 = new MyhandOverXmlPaser();
                                ByteArrayInputStream tInputStringStream2 = new ByteArrayInputStream(responseInfo.result.getBytes());
                                parser2.parse(tInputStringStream2, handler2);
                                List<MyHandOverRecord> list1 = handler2.getList();
                                list.addAll(list1);
                                adapter.notifyDataSetChanged();
                                LogU.e(list1.toString());
                            } else {
                                Toast.makeText(IntoDocumentsActivty.this, handler.getError(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(IntoDocumentsActivty.this, "数据解析出错", Toast.LENGTH_SHORT).show();
                        } finally {
                            lvIntoDocuments.onRefreshComplete();
                            closeProgressDialog();
                        }

                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        lvIntoDocuments.onRefreshComplete();
                        Toast.makeText(IntoDocumentsActivty.this, "网络数据获取失败", Toast.LENGTH_SHORT).show();
                        closeProgressDialog();
                    }
                });
    }


    private void setListener() {
        mBack.setOnClickListener(this);
        mTitle.setOnClickListener(this);
        lvIntoDocuments.setMode(PullToRefreshBase.Mode.BOTH);
        lvIntoDocuments.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                page = 1;
                list.clear();
                initIntoDocument();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                page++;
                initIntoDocument();
            }
        });
        lvIntoDocuments.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(IntoDocumentsActivty.this, AddHandoverActivity.class);
                intent.putExtra("flag", true);
                intent.putExtra("title", "批量转入详情");
                intent.putExtra("data", list.get(position - 1));
                startActivity(intent);
            }
        });
    }

    private void findViewById() {
        mBack = (ImageView) this.findViewById(R.id.head_flip);
        mBack.setImageResource(R.drawable.ic_flipper_head_back);
        mBack.setVisibility(View.VISIBLE);
        mTitle = (TextView) this.findViewById(R.id.head_title);
        mTitle.setText("批量转入列表");
        lvIntoDocuments = (PullToRefreshListView) findViewById(R.id.lv_intoDocuments);
        tvAdress = (TextView) findViewById(R.id.tv_intoAdress);
        btnChangeAdress = (Button) findViewById(R.id.btn_change);
        if (!TextUtils.isEmpty(BaseApplication.userBindTool)) {
            tvAdress.setText(BaseApplication.userBindTool);
        } else if (!TextUtils.isEmpty(BaseApplication.cityName)) {
            tvAdress.setText(BaseApplication.cityName);
        } else {
            tvAdress.setText("当前无可转入目标");
        }
        if (!TextUtils.isEmpty(BaseApplication.userBindTool) && !TextUtils.isEmpty(BaseApplication.cityName)) {
            btnChangeAdress.setVisibility(View.VISIBLE);
        }
        btnChangeAdress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IsCarFlag = !IsCarFlag;
                showDialog(new DialoginOkCallBack() {
                    @Override
                    public void onClickOk(DialogInterface dialog, int which) {

                    }
                }, "转入地点已切换");
                if (IsCarFlag) {
                    btnChangeAdress.setText("当前转到货车");
                    tvAdress.setText(BaseApplication.userBindTool);
                } else {
                    btnChangeAdress.setText("当前转到网点");
                    tvAdress.setText(BaseApplication.cityName);
                }
            }
        });
        list = new ArrayList<>();
        adapter = new ItemIntoDocLvAdapter<>(this, list);
        adapter.setCallBack(new CallBackString() {
            @Override
            public void httFinsh(String data) {
                list.clear();
                page = 1;
                initIntoDocument();
            }
        });
        lvIntoDocuments.setAdapter(adapter);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (IsCarFlag) {
            btnChangeAdress.setText("当前转到货车");
            tvAdress.setText(BaseApplication.userBindTool);
        } else {
            btnChangeAdress.setText("当前转到网点");
            tvAdress.setText(BaseApplication.cityName);
        }
    }

    public static boolean IsCarFlag = true;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_flip:
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        IsCarFlag = true;
    }
}
