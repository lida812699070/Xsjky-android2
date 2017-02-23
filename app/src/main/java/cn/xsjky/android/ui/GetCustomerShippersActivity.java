package cn.xsjky.android.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jq.printer.esc.Text;
import com.lidroid.xutils.http.RequestParams;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import cn.xsjky.android.BaseApplication;
import cn.xsjky.android.BaseSettings;
import cn.xsjky.android.R;
import cn.xsjky.android.adapter.AdapterSelectcustomReciveAdapter;
import cn.xsjky.android.http.Urls;
import cn.xsjky.android.model.AddressBook;
import cn.xsjky.android.model.LoginInfo;
import cn.xsjky.android.model.SendPeroson;
import cn.xsjky.android.util.AddressBookXmlParser;
import cn.xsjky.android.util.CallBackString;
import cn.xsjky.android.util.LogU;
import cn.xsjky.android.util.RetruenUtils;

public class GetCustomerShippersActivity extends BaseActivity {

    private ImageView mFlip;
    private TextView mTitle;
    private String customId;
    private PullToRefreshListView listView;
    private int page;
    private ArrayList<SendPeroson> list;
    private AdapterSelectcustomReciveAdapter<SendPeroson> adapter;
    private Button mBtnSelectRecive;
    private Button mBtnSeletOk;
    private String mCustomerName;
    private TextView mTvCustomName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_customer_shippers);
        findViewById();
        customId = getIntent().getStringExtra("customerId");
        mCustomerName = getIntent().getStringExtra("customerName");
        LogU.e(customId);
        if (!TextUtils.isEmpty(mCustomerName)){
            mTvCustomName.setText(mCustomerName);
        }
        getShipers();
    }

    private void getShipers() {
        List<SendPeroson> sendPerosonsList = DataSupport.where("CompanyId = ?", customId).find(SendPeroson.class);
        list.addAll(sendPerosonsList);
        adapter.notifyDataSetChanged();
    }

    private void findViewById() {
        mFlip = (ImageView) this.findViewById(R.id.head_flip);
        mFlip.setVisibility(View.VISIBLE);
        mFlip.setImageResource(R.drawable.ic_flipper_head_back);
        mFlip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTitle = (TextView) this.findViewById(R.id.head_title);
        mTvCustomName = (TextView) this.findViewById(R.id.tv_customName);
        mBtnSelectRecive = (Button) findViewById(R.id.btn_selectRecive);
        mBtnSeletOk = (Button) findViewById(R.id.btn_selectOk);
        mBtnSeletOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mBtnSelectRecive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(NewActivity.ACTIONSEND);
                sendBroadcast(intent);
                Intent intent1 = new Intent(GetCustomerShippersActivity.this, SelectCustomRecive.class);
                intent1.putExtra("customerId", customId);
                startActivity(intent1);
                finish();
            }
        });
        mTitle.setText("选择发件人");
        listView = (PullToRefreshListView) findViewById(R.id.lv_activityGetCustomerShippers);
        listView.setMode(PullToRefreshBase.Mode.DISABLED);
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                list.clear();
                page = 1;
                getShipers();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                page++;
                getShipers();
            }
        });
        list = new ArrayList<>();
        adapter = new AdapterSelectcustomReciveAdapter<>(this, list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BaseApplication.customShipper = list.get(position - 1);
                Intent intent = new Intent();
                intent.setAction(NewActivity.ACTIONSEND);
                sendBroadcast(intent);
                Intent intent1 = new Intent(GetCustomerShippersActivity.this, SelectCustomRecive.class);
                intent1.putExtra("customerId", customId);
                startActivity(intent1);
                finish();
            }
        });
    }

    public void synSendPerson(View view) {
        startActivity(new Intent(GetCustomerShippersActivity.this, SynDataActivity.class));
    }
}
