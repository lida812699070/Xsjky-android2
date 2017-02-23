package cn.xsjky.android.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.List;

import cn.xsjky.android.R;
import cn.xsjky.android.model.MyHandOverItem;
import cn.xsjky.android.model.MyHandOverRecord;

public class IntoDocumentActivityDetail extends Activity implements View.OnClickListener {

    private MyHandOverRecord handOverRecord;
    private TextView tvIntoDetailDocumentNum;
    private TextView tvIntoDetailSendName;
    private TextView tvIntoDetailActors;
    private TextView tvIntoDetailQty;
    private TextView tvIntoDetailWeight;
    private TextView tvIntoDetailVol;
    private ImageView mBack;
    private TextView mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_into_document_activity_detail);
        Intent intent = getIntent();
        handOverRecord = (MyHandOverRecord) intent.getSerializableExtra("data");
        findViewById();
        setListener();
        initData();
    }

    private void setListener() {
        mBack.setOnClickListener(this);
    }

    private void initData() {
        if (handOverRecord==null){
            return;
        }
        List<MyHandOverItem> listItem = handOverRecord.getListItem();
        String documentNum="";
        for (int i = 0; i < listItem.size(); i++) {
            documentNum+=listItem.get(i).getDocumentNumber()+" ";
        }
        tvIntoDetailDocumentNum.setText("单号："+documentNum);
        tvIntoDetailSendName.setText("发送人："+handOverRecord.getIssueUserId());
        String actors="";
        List<String> actorsList = handOverRecord.getDownLoadActors().getList();
        for (int i = 0; i < actorsList.size(); i++) {
            actors+=actorsList.get(i)+" ";
        }
        tvIntoDetailActors.setText("搬运工："+actors);
        String strQtyNum="";
        for (int i = 0; i < listItem.size(); i++) {
            strQtyNum+=listItem.get(i).getQuantity()+" ";
        }
        tvIntoDetailQty.setText("件数："+strQtyNum);
        String strWeight="";
        for (int i = 0; i < listItem.size(); i++) {
            strWeight+=listItem.get(i).getWeight()+" ";
        }
        tvIntoDetailWeight.setText("重量："+strWeight);
        String strVol="";
        for (int i = 0; i < listItem.size(); i++) {
            strVol+=listItem.get(i).getVolumn()+" ";
        }
        tvIntoDetailVol.setText("体积："+strVol);
    }

    private void findViewById() {
        tvIntoDetailDocumentNum = (TextView) findViewById(R.id.tv_IntoDetail_DocumentNum);
        tvIntoDetailSendName = (TextView) findViewById(R.id.tv_IntoDetail_SendName);
        tvIntoDetailActors = (TextView) findViewById(R.id.tv_IntoDetail_Actors);
        tvIntoDetailQty = (TextView) findViewById(R.id.tv_IntoDetail_Qty);
        tvIntoDetailWeight = (TextView) findViewById(R.id.tv_IntoDetail_weight);
        tvIntoDetailVol = (TextView) findViewById(R.id.tv_IntoDetail_vol);
        mBack = (ImageView) this.findViewById(R.id.head_flip);
        mBack.setImageResource(R.drawable.ic_flipper_head_back);
        mBack.setVisibility(View.VISIBLE);
        mTitle = (TextView) this.findViewById(R.id.head_title);
        mTitle.setText("批量转入详情");
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
