package cn.xsjky.android.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import com.lidroid.xutils.http.RequestParams;

import java.util.ArrayList;

import cn.xsjky.android.BaseApplication;
import cn.xsjky.android.BaseSettings;
import cn.xsjky.android.R;
import cn.xsjky.android.adapter.BaseRecycleviewAdapter;
import cn.xsjky.android.adapter.CustomerManagerListviwAdapter;
import cn.xsjky.android.adapter.DividerItemDecoration;
import cn.xsjky.android.adapter.QueryRecycleAdapter;
import cn.xsjky.android.http.Urls;
import cn.xsjky.android.model.Custom;
import cn.xsjky.android.model.DeliveryRequest;
import cn.xsjky.android.model.LoginInfo;
import cn.xsjky.android.util.CallBackString;
import cn.xsjky.android.util.CancelDeliveryRequestXmlParser;
import cn.xsjky.android.util.DataFormatUtils;
import cn.xsjky.android.util.DateFormatUtils;
import cn.xsjky.android.util.GetUnfinishRequestXmlParser;
import cn.xsjky.android.util.LogU;
import cn.xsjky.android.util.RetruenUtils;
import cn.xsjky.android.view.EmptyRecyclerView;

public class QueryUnFinishDocumentActivity extends BaseActivity {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private EmptyRecyclerView mRecyclerView;
    private int pagNumber;
    private ArrayList<DeliveryRequest> mList;
    private QueryRecycleAdapter<DeliveryRequest> mAdapter;
    private int lastVisibleItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_un_finish_document);
        findViewById();
        initToolbar("查询订单");
        initData();
    }

    private void findViewById() {
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.query_unfinish_swiperefreshlayout);
        mRecyclerView = (EmptyRecyclerView) findViewById(R.id.query_unfinish_recycler);
        View inflate = LayoutInflater.from(this).inflate(R.layout.listview_empty_view, null);
        mRecyclerView.setEmptyView(inflate);
        mList = new ArrayList<>();
        mSwipeRefreshLayout.setColorSchemeColors(Color.BLUE);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mList.clear();
                pagNumber = 0;
                initData();
            }
        });

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(mAdapter = new QueryRecycleAdapter<>(this, mList, R.layout.item_about_lv));

        //上拉加载更多
        mAdapter.setMode(BaseRecycleviewAdapter.MODE_BOTH);//设置既有下拉刷新也有上拉加载模式
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == mAdapter.getItemCount()) {
                    mAdapter.changeMoreStatus(BaseRecycleviewAdapter.LOADING_MORE);
                    pagNumber++;
                    initData();
                    mAdapter.changeMoreStatus(BaseRecycleviewAdapter.PULLUP_LOAD_MORE);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
            }
        });
        mRecyclerView.addItemDecoration(new DividerItemDecoration(
                this, DividerItemDecoration.VERTICAL_LIST));
    }

    private void initData() {
        RequestParams params = new RequestParams();
        LoginInfo loginInfo= BaseApplication.loginInfo;
        params.addBodyParameter("sessionId",loginInfo.getSessionId());
        params.addBodyParameter("userId",loginInfo.getUserId()+"");
        params.addBodyParameter("clientName", BaseSettings.CLIENT_NAME);
        params.addBodyParameter("pageNumber",""+pagNumber);
        params.addBodyParameter("pageSize","10");
        String beginTime="";
        beginTime=DateFormatUtils.getData(System.currentTimeMillis()-(long)(3*24*60*60*1000));
        params.addBodyParameter("beginTime",beginTime);
        String endTime="";
        endTime= DataFormatUtils.getDate(System.currentTimeMillis()+(long)(1*60*60*1000));
        params.addBodyParameter("endTime",endTime);
        params.addBodyParameter("customerId","0");
        params.addBodyParameter("unfishedRequestFlag","false");
        getData(Urls.QueryCustomerRequest, params, new CallBackString() {
            @Override
            public void httFinsh(String data) {
                if (TextUtils.isEmpty(data)){
                    mSwipeRefreshLayout.setRefreshing(false);
                    return;
                }
                GetUnfinishRequestXmlParser parser = RetruenUtils.getReturnInfo(data, new GetUnfinishRequestXmlParser());
                if (parser!=null){
                    mList.addAll(parser.getList());
                    LogU.e(mList.toString());
                    mAdapter.notifyDataSetChanged();
                }
                mSwipeRefreshLayout.setRefreshing(false);
            }
        },false);
    }
}
