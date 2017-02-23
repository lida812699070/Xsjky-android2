package cn.xsjky.android.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.ArrayList;

import cn.xsjky.android.BaseApplication;
import cn.xsjky.android.BaseSettings;
import cn.xsjky.android.R;
import cn.xsjky.android.adapter.ItemQueryLvAdapter;
import cn.xsjky.android.http.Urls;
import cn.xsjky.android.model.QueryDocumentEntity;
import cn.xsjky.android.util.LogU;
import cn.xsjky.android.util.QueryDocumentXmlparser;
import cn.xsjky.android.util.RetruenUtils;

public class SearchActivityByNumber extends BaseActivity implements View.OnClickListener {

    private EditText mEtSearch;
    private ImageView mIvBack;
    private TextView mTvSearch;
    private String mDocumentNumber="";
    private PullToRefreshListView mListView;
    private ItemQueryLvAdapter adapter;
    private ArrayList<QueryDocumentEntity> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_activity_by_number);
        findViews();
    }

    private void findViews() {
        mEtSearch = (EditText) findViewById(R.id.etToolbarSearch);
        mIvBack = (ImageView) findViewById(R.id.ivToolbarBack);
        mTvSearch = (TextView) findViewById(R.id.btnToolbarSearch);
        mIvBack.setOnClickListener(this);
        mTvSearch.setOnClickListener(this);
        mListView = (PullToRefreshListView) findViewById(R.id.lv_search);
        mListView.setMode(PullToRefreshBase.Mode.BOTH);
        list = new ArrayList<>();
        adapter = new ItemQueryLvAdapter<>(this, list);
        mListView.setAdapter(adapter);
        mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                page = 1;
                list.clear();
                initData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                page++;
                initData();
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SearchActivityByNumber.this, QueryDocumentDetailActivity.class);
                intent.putExtra("documentNumber", list.get(position - 1).getDocumentNumber());
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivToolbarBack:
                finish();
                break;
            case R.id.btnToolbarSearch:
                search();
                break;
        }
    }

    private void search() {
        initData();
    }

    private int page = 1;

    private void initData() {
        mDocumentNumber = mEtSearch.getText().toString().trim();
        String url = Urls.QueryDocument;
        final RequestParams params = new RequestParams();
        params.addBodyParameter("sessionId", BaseApplication.loginInfo.getSessionId());
        params.addBodyParameter("userId", BaseApplication.loginInfo.getUserId() + "");
        params.addBodyParameter("clientName", BaseSettings.CLIENT_NAME);
        params.addBodyParameter("returnQueryResult", true + "");
        params.addBodyParameter("pageNumber", "" + page);
        params.addBodyParameter("pageSize", "10");
        params.addBodyParameter("sortProperty", "");
        params.addBodyParameter("descend", "true");
        params.addBodyParameter("documentNumber", mDocumentNumber);
        params.addBodyParameter("beginTime", "");
        params.addBodyParameter("endTime", "");
        params.addBodyParameter("checkStatus", "-1");
        params.addBodyParameter("findShipper", "");
        params.addBodyParameter("findConsigneer", "");
        params.addBodyParameter("documentState", "-1");
        params.addBodyParameter("fromCity", "");
        params.addBodyParameter("toCity", "");
        params.addBodyParameter("pickupBy", "");
        params.addBodyParameter("deliveryBy", "");
        params.addBodyParameter("balanceMode", "-1");
        params.addBodyParameter("balanceState", "-1");
        showProgressDialog();
        BaseApplication.mHttpUtils.send(HttpRequest.HttpMethod.POST,
                url,
                params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        QueryDocumentXmlparser parser = RetruenUtils.getReturnInfo(responseInfo.result, new QueryDocumentXmlparser());
                        if (parser != null) {
                            list.addAll(parser.getList());
                            adapter.notifyDataSetChanged();
                            Tos("目前找到" + list.size() + "条记录");
                        }
                        LogU.e(list.toString());
                        closeProgressDialog();
                        mListView.onRefreshComplete();
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        Tos(msg);
                        closeProgressDialog();
                        mListView.onRefreshComplete();
                    }
                });

    }
}
