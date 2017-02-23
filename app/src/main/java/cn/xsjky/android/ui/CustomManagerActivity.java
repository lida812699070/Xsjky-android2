package cn.xsjky.android.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.lidroid.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

import cn.xsjky.android.BaseApplication;
import cn.xsjky.android.BaseSettings;
import cn.xsjky.android.R;
import cn.xsjky.android.adapter.DividerItemDecoration;
import cn.xsjky.android.adapter.ItemCustomManagerAdapter;
import cn.xsjky.android.adapter.ItemCustomerManagerAdapter;
import cn.xsjky.android.adapter.OnItemClickLitener;
import cn.xsjky.android.http.Urls;
import cn.xsjky.android.model.Custom;
import cn.xsjky.android.util.CallBackString;
import cn.xsjky.android.util.LogU;
import cn.xsjky.android.util.QueryCustomerXmlparser;
import cn.xsjky.android.util.RetruenUtils;
import cn.xsjky.android.view.EmptyRecyclerView;

public class CustomManagerActivity extends BaseActivity {
    private SwipeRefreshLayout customManagerSwr;
    private EmptyRecyclerView mListView;
    private LinearLayoutManager linearLayoutManager;
    private ItemCustomerManagerAdapter<Custom> mAdapter;
    private ArrayList<Custom> mCustoms;
    private ImageView mIvTitleAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_manager);
        initToolbar("客户列表");
        findviews();
        initdata();
    }

    private void findviews() {
        mIvTitleAdd = (ImageView) findViewById(R.id.head_refresh);
        mIvTitleAdd.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_add));
        mIvTitleAdd.setVisibility(View.VISIBLE);
        mIvTitleAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CustomManagerActivity.this, AddCutomerActivity.class));
            }
        });
        customManagerSwr = (SwipeRefreshLayout) findViewById(R.id.custom_manager_swr);
        mListView = (EmptyRecyclerView) findViewById(R.id.custom_manager_rcv);
        customManagerSwr.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        customManagerSwr.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mCustoms.clear();
                initdata();
            }
        });
        View inflate = LayoutInflater.from(this).inflate(R.layout.listview_empty_view, null);
        mListView.setEmptyView(inflate);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mListView.setLayoutManager(linearLayoutManager);

        mCustoms = new ArrayList<>();
        mAdapter = new ItemCustomerManagerAdapter<>(this, mCustoms, R.layout.item_custom_manager);
        mListView.setAdapter(mAdapter);
        mListView.addItemDecoration(new DividerItemDecoration(
                this, DividerItemDecoration.VERTICAL_LIST));

        mAdapter.setOnItemClickLitener(new OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(CustomManagerActivity.this, AddCutomerActivity.class);
                intent.putExtra("data", mCustoms.get(position));
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
      /*  mListView.setit(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });*/
    }

    private void initdata() {
        String url = Urls.GetHandleCustomers;
        RequestParams params = new RequestParams();
        params.addBodyParameter("sessionId", BaseApplication.loginInfo.getSessionId());
        params.addBodyParameter("userId", BaseApplication.loginInfo.getUserId() + "");
        params.addBodyParameter("clientName", BaseSettings.CLIENT_NAME);
        getData(url, params, new CallBackString() {
            @Override
            public void httFinsh(String data) {
                QueryCustomerXmlparser returnInfo = RetruenUtils.getReturnInfo(data, new QueryCustomerXmlparser());
                assert returnInfo != null;
                mCustoms.addAll(returnInfo.getList());
                mAdapter.notifyDataSetChanged();
                customManagerSwr.setRefreshing(false);
            }
        });
    }

}
