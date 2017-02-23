package cn.xsjky.android.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import cn.xsjky.android.R;
import cn.xsjky.android.adapter.ListAcInfoAdapter;
import cn.xsjky.android.model.CustomOtherInfos;
import cn.xsjky.android.util.LogU;

public class NewActivityInfoActivity extends BaseActivity {

    private ArrayList<CustomOtherInfos> mCustomOtherInfoses;
    private ListAcInfoAdapter<CustomOtherInfos> mAdapter;
    private String mCustomerName;
    private String mTocity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_activity_info);
        initToolbar("选择客户信息");
        Intent intent = getIntent();
        mCustomerName = intent.getStringExtra("customerName");
        mTocity = intent.getStringExtra("tocity");
        findViews();
        setData();
    }

    private void setData() {
        String sql = "tocity = '" + mTocity
                + "' and customName = '" + mCustomerName + "'";
        List<CustomOtherInfos> list = CustomOtherInfos.queryByToString(sql);
        LogU.e(mTocity+"--"+mCustomerName+""+list.toString());
        mCustomOtherInfoses.addAll(list);
        mAdapter.notifyDataSetChanged();
        if (list.size()==0){
            Tos("您还没有输入过该公司的运单");
            finish();
        }else if(list.size()==1){
            CustomOtherInfos customOtherInfos = mCustomOtherInfoses.get(0);
            Intent intent = new Intent();
            intent.putExtra("data",customOtherInfos);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    private void findViews() {
        ListView lv = (ListView) findViewById(R.id.lv_newAcInfo);
        mCustomOtherInfoses = new ArrayList<>();
        mAdapter = new ListAcInfoAdapter<>(this, mCustomOtherInfoses);
        lv.setAdapter(mAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CustomOtherInfos customOtherInfos = mCustomOtherInfoses.get(position);
                Intent intent = new Intent();
                intent.putExtra("data",customOtherInfos);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}
