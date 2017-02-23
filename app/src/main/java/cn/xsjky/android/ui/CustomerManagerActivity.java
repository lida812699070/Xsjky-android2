package cn.xsjky.android.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import cn.xsjky.android.R;
import cn.xsjky.android.adapter.BaseRecycleviewAdapter;
import cn.xsjky.android.adapter.CustomerManagerListviwAdapter;
import cn.xsjky.android.adapter.DividerItemDecoration;
import cn.xsjky.android.model.Custom;
import cn.xsjky.android.view.EmptyRecyclerView;

public class CustomerManagerActivity extends BaseActivity {

    private SwipeRefreshLayout swipeRefreshLayout;
    private EmptyRecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private List<Custom> shippingDocumentList;
    private CustomerManagerListviwAdapter<Custom> adapter;
    private int lastVisibleItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_manager);
        findviewById();
        initToolbar("客户管理");
        InitData();
    }

    private void findviewById() {
        EditText etInfo= (EditText) findViewById(R.id.et_custom_manager);
        etInfo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String textComName = s.toString();
                if (TextUtils.isEmpty(textComName)) {
                    return;
                }else {
                    querCustomByInfo(textComName);
                }
            }
        });
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.CustomManager_swiperefreshlayout);
        swipeRefreshLayout.setColorSchemeColors(Color.BLUE);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.clear();
                pagNumber = 0;
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

        //上拉加载更多
        adapter.setMode(BaseRecycleviewAdapter.MODE_BOTH);//设置既有下拉刷新也有上拉加载模式
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == adapter.getItemCount()) {
                    adapter.changeMoreStatus(BaseRecycleviewAdapter.LOADING_MORE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            adapter.addMoreItem(Custom.querData(++pagNumber));
                            adapter.changeMoreStatus(BaseRecycleviewAdapter.PULLUP_LOAD_MORE);
                        }
                    }, 1000);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
            }
        });
        recyclerView.addItemDecoration(new DividerItemDecoration(
                this, DividerItemDecoration.VERTICAL_LIST));
    }

    private void querCustomByInfo(String textComName) {
        adapter.clear();
        pagNumber=0;
        List<Custom> customs = Custom.queryByCustomerName(textComName);
        adapter.addItem(customs);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        flash();
    }

    public void flash() {
        adapter.clear();
        pagNumber = 0;
        InitData();
        swipeRefreshLayout.setRefreshing(false);
    }

    private int pagNumber = 0;

    private void InitData() {
        List<Custom> customs = Custom.querData(0);
        adapter.addItem(customs);
    }
}
