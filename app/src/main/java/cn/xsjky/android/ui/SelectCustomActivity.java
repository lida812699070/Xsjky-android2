package cn.xsjky.android.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import cn.xsjky.android.R;
import cn.xsjky.android.adapter.ItemSimpleAdapterAdapter;
import cn.xsjky.android.model.Custom;
import cn.xsjky.android.util.LogU;

public class SelectCustomActivity extends Activity {

    private EditText mCustomInfo;
    private List<Custom> listCustomers;
    private String textComName;
    private ItemSimpleAdapterAdapter<String> adapterCustom;
    private ListView mLvContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        findViewById();
    }

    private void findViewById() {
        mCustomInfo = (EditText) findViewById(R.id.et_CustomInfo);
        mCustomInfo.addTextChangedListener(myedittextListener);
        mLvContent = (ListView) findViewById(R.id.lv_selectActivity_content);
    }

    private TextWatcher myedittextListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            textComName = s.toString();
            if (TextUtils.isEmpty(textComName)) {
                queryCustomer();
                return;
            }
            if (listCustomers != null)
                listCustomers.clear();
            listCustomers = Custom.queryByCustomerName(textComName);
            if (listCustomers != null)
                setCustomData();
        }
    };

    //当用户没有输入信息的时候就查询用户最近查看过的
    private void queryLast() {

    }

    private void queryCustomer() {
        List<Custom> customs = DataSupport.limit(10).find(Custom.class, true);
        if (customs == null || customs.size() == 0) {
            LogU.e("没有数据");
        } else {
            LogU.e("count=" + customs.toString());
        }
        //如果没网
        if (listCustomers != null) {
            listCustomers.clear();
        }
        listCustomers = customs;
        setCustomData();
        //如果网络正常就去查看数据是否有更新
       /* if (NetworkDetector.detect(this) && isCheck)
            checkSynData();*/
    }

    private void setCustomData() {
        ArrayList<String> customNames = new ArrayList<>();
        for (int i = 0; i < listCustomers.size(); i++) {
            customNames.add(listCustomers.get(i).getCustomerName());
        }
        adapterCustom = new ItemSimpleAdapterAdapter<String>(this, customNames);
        mLvContent.setAdapter(adapterCustom);
        adapterCustom.notifyDataSetChanged();
    }

}
