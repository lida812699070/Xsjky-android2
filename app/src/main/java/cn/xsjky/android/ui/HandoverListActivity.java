package cn.xsjky.android.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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
import cn.xsjky.android.adapter.ItemHandoverAdapter;
import cn.xsjky.android.http.Urls;
import cn.xsjky.android.model.Document;
import cn.xsjky.android.model.HandOverItem;
import cn.xsjky.android.model.MyHandOverRecord;
import cn.xsjky.android.util.LogU;
import cn.xsjky.android.util.MyhandOverXmlPaser;
import cn.xsjky.android.util.RetrueCodeHandler;

public class HandoverListActivity extends BaseActivity implements AdapterView.OnItemClickListener, View.OnClickListener {

    private PullToRefreshListView lvHandover;
    private ImageView mAdd;
    private TextView mTitle;
    private ItemHandoverAdapter<MyHandOverRecord> adapter;
    private ImageView mBack;
    private List<Document> mDocumentList;
    private ArrayList<HandOverItem> mHandOverItemList;
    private List<MyHandOverRecord> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handover_list);
        initView();
        initData();
        setListener();
    }


    private void initData() {
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
                Urls.DURLGetHandOverRecords,
                params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        try {
                            String call = responseInfo.result;
                            SAXParserFactory factory1 = SAXParserFactory.newInstance();
                            SAXParser parser1 = factory1.newSAXParser();
                            RetrueCodeHandler handler1 = new RetrueCodeHandler();
                            ByteArrayInputStream tInputStringStream1 = new ByteArrayInputStream(call.getBytes());
                            parser1.parse(tInputStringStream1, handler1);
                            if (handler1.getString().equals("0")) {
                                SAXParserFactory factory = SAXParserFactory.newInstance();
                                SAXParser parser = factory.newSAXParser();
                                MyhandOverXmlPaser handler = new MyhandOverXmlPaser();
                                ByteArrayInputStream tInputStringStream = new ByteArrayInputStream(call.getBytes());
                                parser.parse(tInputStringStream, handler);
                                list.addAll(handler.getList());
                                //LogU.e(list.toString());
                                lvHandover.onRefreshComplete();
                                adapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(HandoverListActivity.this, handler1.getError(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(HandoverListActivity.this, "数据错误", Toast.LENGTH_SHORT).show();
                        } finally {
                            lvHandover.onRefreshComplete();
                            closeProgressDialog();
                        }
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        Toast.makeText(HandoverListActivity.this, "网络数据获取错误", Toast.LENGTH_SHORT).show();
                        closeProgressDialog();
                    }
                });
    }

    private void setListener() {
        lvHandover.setOnItemClickListener(this);
        mBack.setOnClickListener(this);
    }

    private int page=1;
    private void initView() {
        list = new ArrayList<>();
        lvHandover = (PullToRefreshListView) findViewById(R.id.lv_Handover);
        lvHandover.setMode(PullToRefreshBase.Mode.BOTH);
        lvHandover.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                page=1;
                list.clear();
                initData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                page++;
                initData();
            }
        });
        mAdd = (ImageView) this.findViewById(R.id.head_tool1);
        mBack = (ImageView) this.findViewById(R.id.head_flip);
        mBack.setImageResource(R.drawable.ic_flipper_head_back);
        mAdd.setOnClickListener(this);
        mBack.setVisibility(View.VISIBLE);
        mAdd.setVisibility(View.VISIBLE);
        mTitle = (TextView) this.findViewById(R.id.head_title);
        mTitle.setText("批量转出列表");
        adapter = new ItemHandoverAdapter<>(this, list);
        lvHandover.setAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, AddHandoverActivity.class);
        intent.putExtra("flag", true);
        intent.putExtra("title", "批量转出详情");
        intent.putExtra("data", list.get(position - 1));
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_flip:
                finish();
                break;
            case R.id.head_tool1:
                addHandover();
                break;
        }
    }

    private void addHandover() {
        Intent intent = new Intent(this, AddHandoverActivity.class);
        startActivity(intent);
    }
}
