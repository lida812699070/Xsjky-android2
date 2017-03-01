package cn.xsjky.android.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.http.RequestParams;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.xsjky.android.BaseApplication;
import cn.xsjky.android.BaseSettings;
import cn.xsjky.android.R;
import cn.xsjky.android.adapter.BaseRecycleviewAdapter;
import cn.xsjky.android.adapter.DividerItemDecoration;
import cn.xsjky.android.adapter.QueryBySalesmanAdapter;
import cn.xsjky.android.http.Urls;
import cn.xsjky.android.model.LoginInfo;
import cn.xsjky.android.model.SimpleDocumentBysales;
import cn.xsjky.android.util.CallBackString;
import cn.xsjky.android.util.DataFormatUtils;
import cn.xsjky.android.util.DateFormatUtils;
import cn.xsjky.android.util.LogU;
import cn.xsjky.android.util.RetruenUtils;
import cn.xsjky.android.util.SimpleDocumentListXmlParser;
import cn.xsjky.android.view.EmptyRecyclerView;

public class DocumentQueryBySalesmanActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.head_flip)
    ImageView mHeadFlip;
    @Bind(R.id.head_title)
    TextView mHeadTitle;
    @Bind(R.id.btnToolbarSearch)
    TextView mBtnToolbarSearch;
    @Bind(R.id.head_tool1)
    ImageView mHeadTool1;
    @Bind(R.id.head_tool2)
    ImageView mHeadTool2;
    @Bind(R.id.head_refresh)
    ImageView mHeadRefresh;
    @Bind(R.id.ivToolbarBack)
    TextView ivToolbarBack;
    @Bind(R.id.etToolbarSearch)
    EditText mEdSearch;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private EmptyRecyclerView mRecyclerView;
    private QueryBySalesmanAdapter<SimpleDocumentBysales> mAdapter;
    private ArrayList<SimpleDocumentBysales> mList;
    private int pagNumber;
    private int lastVisibleItem;
    private String mBeginTime;
    private String mEndTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_query_by_salesman);
        ButterKnife.bind(this);
        init();
        mSwipeRefreshLayout.setRefreshing(true);
        initData();
    }

    private void init() {
        initToolbar("查询运单");
        mHeadRefresh.setVisibility(View.VISIBLE);
        mHeadRefresh.setImageResource(R.drawable.icon_delete);
        mHeadRefresh.setOnClickListener(this);
        mBtnToolbarSearch.setOnClickListener(this);
        ivToolbarBack.setOnClickListener(this);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.queryBySalesmanSR);
        mRecyclerView = (EmptyRecyclerView) findViewById(R.id.queryqueryBySalesmanRecycler);
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
        mRecyclerView.setAdapter(mAdapter = new QueryBySalesmanAdapter<>(this, mList, R.layout.item_query_by_salesman));

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
        LoginInfo loginInfo = BaseApplication.loginInfo;
        params.addBodyParameter("sessionId", loginInfo.getSessionId());
        params.addBodyParameter("userId", loginInfo.getUserId() + "");
        params.addBodyParameter("clientName", BaseSettings.CLIENT_NAME);
        params.addBodyParameter("pageNumber", "" + pagNumber);
        params.addBodyParameter("pageSize", "10");
        if (TextUtils.isEmpty(mBeginTime))
            mBeginTime = DateFormatUtils.getData(System.currentTimeMillis() - (long) (2l * 365l * 24l * 60l * 60l * 1000l));//默认时间
        params.addBodyParameter("beginTime", mBeginTime);
        if (TextUtils.isEmpty(mEndTime))
            mEndTime = DateFormatUtils.getData(System.currentTimeMillis());
        params.addBodyParameter("endTime", mEndTime);
        params.addBodyParameter("sortProperty", "");
        params.addBodyParameter("sortDescend", "true");
        params.addBodyParameter("returnList", "true");
        params.addBodyParameter("searchValue", mEdSearch.getText().toString());
        getData(Urls.DocumentQueryBySalesman, params, new CallBackString() {
            @Override
            public void httFinsh(String data) {
                if (TextUtils.isEmpty(data)) {
                    mSwipeRefreshLayout.setRefreshing(false);
                    return;
                }
                SimpleDocumentListXmlParser parser = RetruenUtils.getReturnInfo(data, new SimpleDocumentListXmlParser());
                if (parser != null) {
                    LogU.e(parser.getList().toString());
                    mList.addAll(parser.getList());
                    LogU.e(mList.toString());
                    mAdapter.notifyDataSetChanged();
                }
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }, false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnToolbarSearch:
                mSwipeRefreshLayout.setRefreshing(true);
                mList.clear();
                initData();
                break;
            case R.id.ivToolbarBack:
                showDatePickerDialog();
                break;
            case R.id.head_refresh:
                mBeginTime="";
                mEndTime="";
                ivToolbarBack.setText("选择时间");
                mEdSearch.setText("");
                Tos("已清空");
                break;
        }
    }

    public void showDatePickerDialog() {
        DatePickerFragment datePicker = new DatePickerFragment();
        datePicker.setDateCallBack(new DatePickerFragment.DateCallBack() {
            @Override
            public void getDate(String date) {
                ivToolbarBack.setText(date);
                mBeginTime=date+" 00:00:00";
                mEndTime=date+" 23:59:59";
                mSwipeRefreshLayout.setRefreshing(true);
                mList.clear();
                initData();
            }
        });
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        datePicker.show(fragmentTransaction, "datePicker");

    }
}
