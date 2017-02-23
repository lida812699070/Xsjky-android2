package cn.xsjky.android.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.exception.DbException;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import cn.xsjky.android.BaseApplication;
import cn.xsjky.android.R;
import cn.xsjky.android.adapter.ItemErrorDocumentLvAdapter;
import cn.xsjky.android.db.ErrorDocument;
import cn.xsjky.android.model.PrintDocumentInfo;
import cn.xsjky.android.util.DP_PX;
import cn.xsjky.android.util.LogU;

public class ErrorDocumentActivity extends BaseActivity implements View.OnClickListener {

    private ListView lvErrorContent;
    private ImageView ivBack;
    private TextView tvTitle;
    private List<ErrorDocument> errorDocuments;
    private ItemErrorDocumentLvAdapter<ErrorDocument> adapter;
    private ArrayList<ErrorDocument> list;
    private LinearLayout mLl_errorPrint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error_document);
        findViewById();
        setData();
        initData();
    }

    private void initData() {
        try {
            errorDocuments = BaseApplication.mdbUtils.findAll(ErrorDocument.class);
            list.addAll(errorDocuments);
            adapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void AdapterNotf() {
        if (list == null || adapter == null)
            return;
        try {
            errorDocuments = BaseApplication.mdbUtils.findAll(ErrorDocument.class);
            list.clear();
            list.addAll(errorDocuments);
            adapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setData() {
        list = new ArrayList<>();
        adapter = new ItemErrorDocumentLvAdapter<>(this, this.list);
        lvErrorContent.setAdapter(adapter);

        try {
            List<PrintDocumentInfo> all = DataSupport.limit(10).find(PrintDocumentInfo.class);
            setErrorPrint(all);
        } catch (Exception e) {

        }
    }

    private void setErrorPrint(final List<PrintDocumentInfo> infos) {
        if (infos != null && infos.size() != 0) {
            for (int i = 0; i < infos.size(); i++) {
                LinearLayout linearLayout = new LinearLayout(this);
                linearLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                TextView textView = new TextView(this);
                final PrintDocumentInfo info = infos.get(i);
                textView.setText(info.getDocumentNumber());
                textView.setTextSize(DP_PX.dip2px(ErrorDocumentActivity.this,15));
                linearLayout.addView(textView);
                linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ErrorDocumentActivity.this, FreightActivity.class);
                        intent.putExtra("data", info);
                        startActivity(intent);
                    }
                });
                mLl_errorPrint.addView(linearLayout);
            }
        }
    }

    private void findViewById() {
        lvErrorContent = (ListView) findViewById(R.id.lv_errorDocument_content);
        mLl_errorPrint = (LinearLayout) findViewById(R.id.ll_printError);
        ivBack = (ImageView) findViewById(R.id.head_flip);
        ivBack.setImageResource(R.drawable.ic_flipper_head_back);
        ivBack.setOnClickListener(this);
        ivBack.setVisibility(View.VISIBLE);
        tvTitle = (TextView) findViewById(R.id.head_title);
        tvTitle.setText("异常订单");
        lvErrorContent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ErrorDocumentActivity.this, NewActivity.class);
                intent.putExtra("strDocuments", list.get(position).getDocumentNumber());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        AdapterNotf();

        llNotif();
        super.onResume();
    }

    private void llNotif() {
        try {
            mLl_errorPrint.removeAllViews();
            List<PrintDocumentInfo> all = DataSupport.limit(10).find(PrintDocumentInfo.class);
            setErrorPrint(all);
        } catch (Exception e) {

        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.head_flip:
                finish();
                break;

        }
    }
}
