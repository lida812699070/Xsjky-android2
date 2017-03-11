package cn.xsjky.android.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

import cn.xsjky.android.BaseApplication;
import cn.xsjky.android.BaseSettings;
import cn.xsjky.android.R;
import cn.xsjky.android.adapter.CustomerManagerListviwAdapter;
import cn.xsjky.android.adapter.DividerItemDecoration;
import cn.xsjky.android.adapter.OnItemClickLitener;
import cn.xsjky.android.http.Urls;
import cn.xsjky.android.model.Custom;
import cn.xsjky.android.model.Infos;
import cn.xsjky.android.model.LoginInfo;
import cn.xsjky.android.util.CallBackString;
import cn.xsjky.android.util.DialoginOkCallBack;
import cn.xsjky.android.util.LogU;
import cn.xsjky.android.util.MySoap;
import cn.xsjky.android.util.MySoapHttpRequest;
import cn.xsjky.android.util.QueryCustomerXmlparser;
import cn.xsjky.android.util.RetrueCodeHandler;
import cn.xsjky.android.util.RetruenUtils;
import cn.xsjky.android.view.EmptyRecyclerView;

public class CustomerManagerActivity extends BaseActivity {

    public static final String ACTION = "cn.xsjky.android.ui.CustomerManagerActivity";
    private SwipeRefreshLayout swipeRefreshLayout;
    private EmptyRecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private List<Custom> shippingDocumentList;
    private CustomerManagerListviwAdapter<Custom> adapter;
    private int lastVisibleItem;
    private EditText mEtInfo;
    private ImageView mIvTitleAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_manager);
        findviewById();
        initToolbar("客户管理");
        InitData();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION);
        registerReceiver(myReceiver, filter);
    }

    private BroadcastReceiver myReceiver = new BroadcastReceiver(){

        @Override
        public void onReceive(Context context, Intent intent) {
            shippingDocumentList.clear();
            InitData();
        }

    };
    private String seachValue = "";

    private void findviewById() {
        mEtInfo = (EditText) findViewById(R.id.et_custom_manager);
        TextView btnToolbarSearch = (TextView) findViewById(R.id.btnToolbarSearch);
        btnToolbarSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seachValue = mEtInfo.getText().toString().trim();
                shippingDocumentList.clear();
                InitData();
            }
        });

        mIvTitleAdd = (ImageView) findViewById(R.id.head_refresh);
        mIvTitleAdd.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_add));
        mIvTitleAdd.setVisibility(View.VISIBLE);
        mIvTitleAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CustomerManagerActivity.this, AddCutomerActivity.class));
            }
        });
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.CustomManager_swiperefreshlayout);
        swipeRefreshLayout.setColorSchemeColors(Color.BLUE);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.clear();
                InitData();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        recyclerView = (EmptyRecyclerView) findViewById(R.id.CustomManager_recycler);
        View inflate = LayoutInflater.from(this).inflate(R.layout.listview_empty_view, null);
        recyclerView.setEmptyView(inflate);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        shippingDocumentList = new ArrayList<>();
        recyclerView.setAdapter(adapter = new CustomerManagerListviwAdapter<>(this, shippingDocumentList, R.layout.item_customer_manager));
        recyclerView.addItemDecoration(new DividerItemDecoration(
                this, DividerItemDecoration.VERTICAL_LIST));
        adapter.setOnItemClickLitener(new OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(CustomerManagerActivity.this, AddCutomerActivity.class);
                intent.putExtra("data", shippingDocumentList.get(position));
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, final int position) {
                showDialog(new DialoginOkCallBack() {
                    @Override
                    public void onClickOk(DialogInterface dialog, int which) {
                        getRemoteInfo(shippingDocumentList.get(position).getCustomerId());
                    }
                },"是否要删除？");
            }
        });
    }

    public void getRemoteInfo(String customerId) {
        showProgressDialog();
        String endPoint = BaseSettings.WEBSERVICE_URL;
        // SOAP Action
        final LoginInfo loginInfo = BaseApplication.loginInfo;
        if (loginInfo == null)
            return;
        final String soapAction = "http://www.xsjky.cn/DeleteCustomer";
        String info = Infos.DeleteCustomer;
        info = info.replace("UserIdValue", loginInfo.getUserId() + "");
        info = info.replace("ClientNameValue", Infos.CLIENTNAME);
        info = info.replace("SessionIdValue", loginInfo.getSessionId());
        info = info.replace("RoleDataValue", "0");
        info = info.replace("customerIdValue", customerId);
        MySoapHttpRequest.getString(new CallBackString() {
            @Override
            public void httFinsh(final String data) {
                RetrueCodeHandler parser = RetruenUtils.getReturnInfo(data, new RetrueCodeHandler());
                if (parser != null && parser.getString().equals("0")) {
                    mHandler.sendEmptyMessage(0);
                } else if (parser != null && parser.getString().equals("-1")) {
                    mHandler.obtainMessage(-1, parser.getError());
                } else {
                    mHandler.sendEmptyMessage(-2);
                }
                closeProgressDialog();
            }
        }, endPoint, soapAction, info);
    }

    private static final int SUCCESS_CALLBACK = 0;
    private static final int ERROR_CALLBACK = -1;
    private static final int FEILE_CALLBACK = -2;

    MyHandler mHandler=new MyHandler();

    private class MyHandler extends Handler{

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case SUCCESS_CALLBACK:
                    Tos("删除成功");
                    shippingDocumentList.clear();
                    InitData();
                    break;
                case ERROR_CALLBACK:
                    Tos(msg.obj.toString());
                    break;
                case FEILE_CALLBACK:
                    Tos("网络请求错误");
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }

    private void InitData() {
        String url = Urls.CustomerQueryBySalesman;
        RequestParams params = new RequestParams();
        params.addBodyParameter("sessionId", BaseApplication.loginInfo.getSessionId());
        params.addBodyParameter("userId", BaseApplication.loginInfo.getUserId() + "");
        params.addBodyParameter("clientName", BaseSettings.CLIENT_NAME);
        params.addBodyParameter("searchValue", seachValue);
        getData(url, params, new CallBackString() {
            @Override
            public void httFinsh(String data) {
                QueryCustomerXmlparser returnInfo = RetruenUtils.getReturnInfo(data, new QueryCustomerXmlparser());
                if (returnInfo != null) {
                    LogU.e(returnInfo.getList().toString());
                    shippingDocumentList.addAll(returnInfo.getList());
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }
}
