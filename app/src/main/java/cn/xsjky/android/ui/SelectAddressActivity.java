package cn.xsjky.android.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

import cn.xsjky.android.BaseApplication;
import cn.xsjky.android.BaseSettings;
import cn.xsjky.android.R;
import cn.xsjky.android.adapter.ItemAddressinfoAdapter;
import cn.xsjky.android.http.Urls;
import cn.xsjky.android.model.AddressBook;
import cn.xsjky.android.util.AddressBookXmlParser;
import cn.xsjky.android.util.CallBackString;
import cn.xsjky.android.util.LogU;
import cn.xsjky.android.util.RetruenUtils;

public class SelectAddressActivity extends BaseActivity {

    public static final String ACTION="cn.xsjky.android.ui.SelectAddressActivity.action";
    private PullToRefreshListView lv;
    public static ArrayList<AddressBook> books;
    private ItemAddressinfoAdapter<AddressBook> adapter;
    private int page;
    public static int isSend;
    private ImageView mIvAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_address);
        initToolbar("发件人地址");
        isSend = getIntent().getIntExtra("IsSend", 0);
        findViewById();
        initData();
        registRecive();
    }
    private void registRecive() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION);
        registerReceiver(myReceiver, filter);
    }

    private BroadcastReceiver myReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (books != null) {
                    books.clear();
                }
                initData();
            } catch (Exception e) {

            }
        }
    };
    private void findViewById() {
        lv = (PullToRefreshListView) findViewById(R.id.lv_selectAddress);
        mIvAdd = (ImageView) findViewById(R.id.head_refresh);
        mIvAdd.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_add));
        mIvAdd.setVisibility(View.VISIBLE);
        mIvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SelectAddressActivity.this, AddAddressActivity.class));
            }
        });
        lv.setMode(PullToRefreshBase.Mode.BOTH);
        books = new ArrayList<>();
        adapter = new ItemAddressinfoAdapter<>(this, books);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                position = position - 1;
                /*if (isSend == FragmentNewDocumentFragment.SEND) {
                    MyAppLocation.shipBook = books.get(position);
                } else if (isSend == FragmentNewDocumentFragment.GET) {
                    MyAppLocation.getBook = books.get(position);
                } else if (isSend == SendQuestActivity.SHIP) {
                    BaseApplication.shipBook = books.get(position);
                }*/

                Intent intent = new Intent();
                intent.setAction(SendQuestActivity.ACTION);
                intent.putExtra("data",books.get(position));
                sendBroadcast(intent);
                finish();//此处一定要调用finish()方法
            }
        });
        lv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                page = 1;
                books.clear();
                initData();

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                page++;
                initData();
            }
        });
    }

    public void initData() {
        RequestParams params = new RequestParams();
        params.addBodyParameter("sessionId", BaseApplication.loginInfo.getSessionId());
        params.addBodyParameter("userId", BaseApplication.loginInfo.getUserId()+"");
        params.addBodyParameter("clientName", BaseSettings.CLIENT_NAME);
        params.addBodyParameter("pageNumber", page + "");
        params.addBodyParameter("pageSize", 10 + "");
        params.addBodyParameter("searchData", "");
        getData(Urls.Query, params, new CallBackString() {
            @Override
            public void httFinsh(String data) {
                LogU.e(data);
                lv.onRefreshComplete();
                AddressBookXmlParser parser = RetruenUtils.getReturnInfo(data, new AddressBookXmlParser());
                if (parser != null) {
                    List<AddressBook> list = parser.getList();
                    LogU.e(list.toString());
                    books.addAll(list);
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(myReceiver);
        super.onDestroy();
    }
}
