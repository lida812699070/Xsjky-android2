package cn.xsjky.android.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jq.printer.esc.Text;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import cn.xsjky.android.BaseApplication;
import cn.xsjky.android.R;
import cn.xsjky.android.constant.SoapAction;
import cn.xsjky.android.constant.SoapEndpoint;
import cn.xsjky.android.constant.SoapInfo;
import cn.xsjky.android.model.Document;
import cn.xsjky.android.model.Infos;
import cn.xsjky.android.model.LoginInfo;
import cn.xsjky.android.model.WoodenFrame;
import cn.xsjky.android.util.CallBackString;
import cn.xsjky.android.util.DataFormatUtils;
import cn.xsjky.android.util.LogU;
import cn.xsjky.android.util.MySoapHttpRequest;
import cn.xsjky.android.util.RetrueCodeHandler;
import cn.xsjky.android.util.RetruenUtils;
import cn.xsjky.android.weiget.ItemMadeFrame;

public class MadeFrameActivity extends BaseActivity implements View.OnClickListener {

    private ImageView mBack;
    private TextView mTitle;
    private LinearLayout mllContent;
    private Button mbtnAdd;
    private Button mConfirm;
    private ArrayList<View> items;
    private ArrayList<WoodenFrame> woodenFrames;
    private ArrayList<WoodenFrame> list;
    private boolean falg = false;
    private boolean falgRecordId = true;//true= +,false= -
    private HashMap<String, Boolean> map;
    private TextView tvWorker;
    private Button btnSelectWorker;
    private String documentNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_made_frame);
        Intent intent = getIntent();
        map = new HashMap<>();
        list = (ArrayList<WoodenFrame>) intent.getSerializableExtra("data");
        documentNumber = intent.getStringExtra("documentNumber");
        items = new ArrayList<>();
        woodenFrames = new ArrayList<>();
        findViewById();
        setListener();
        if (this.list != null && list.size() != 0) {
            for (int i = 0; i < list.size(); i++) {
                map.put(list.get(i).getRecordId(), true);
            }
            falg = true;
            initView();
        }
    }

    private void initView() {
        for (int i = 0; i < list.size(); i++) {
            mbtnAdd.performClick();
            WoodenFrame woodenFrame = list.get(i);
            final View view = items.get(i);
            EditText etLength = (EditText) view.findViewById(R.id.et_madeframe_length);

            etLength.setText(woodenFrame.getLength());

            EditText etWith = (EditText) view.findViewById(R.id.et_madeframe_with);
            etWith.setText(woodenFrame.getWidth());
            EditText etHeight = (EditText) view.findViewById(R.id.et_madeframe_height);
            etHeight.setText(woodenFrame.getHeight());
            EditText etQty = (EditText) view.findViewById(R.id.et_madeframe_qty);
            etQty.setText(woodenFrame.getQuantity());
            EditText etRemark = (EditText) view.findViewById(R.id.et_madeframe_remark);
            final Button etDel = (Button) view.findViewById(R.id.btn_madeframe_del);
            final int finalI = i;
            etDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String recordId = list.get(finalI).getRecordId();
                    if (map.get(recordId)) {
                        view.setBackgroundColor(Color.GRAY);
                    } else {
                        view.setBackgroundColor(Color.WHITE);
                    }
                    map.put(recordId, !map.get(recordId + ""));
                }
            });
            etRemark.setText(woodenFrame.getRemarks());
            etLength.setEnabled(false);
            etWith.setEnabled(false);
            etHeight.setEnabled(false);
            etQty.setEnabled(false);
            etRemark.setEnabled(false);
        }
    }

    private void setListener() {
        mBack.setOnClickListener(this);
        mbtnAdd.setOnClickListener(this);
        mConfirm.setOnClickListener(this);
    }

    private void findViewById() {
        mTitle = (TextView) this.findViewById(R.id.head_title);
        mbtnAdd = (Button) this.findViewById(R.id.btn_mf_add_item);
        mConfirm = (Button) this.findViewById(R.id.btn_mf_confirm);
        mllContent = (LinearLayout) this.findViewById(R.id.ll_made_frame);
        mTitle.setText("打货架信息");
        mBack = (ImageView) this.findViewById(R.id.head_flip);
        mBack.setImageResource(R.drawable.ic_flipper_head_back);
        mBack.setVisibility(View.VISIBLE);
        tvWorker = (TextView) findViewById(R.id.tv_madeframe_worker);
        btnSelectWorker = (Button) findViewById(R.id.btn_madeframe_select_worker);
        btnSelectWorker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
                startActivity(new Intent(MadeFrameActivity.this, SelectWorkerDialogActivity.class));
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_flip:
                Toast.makeText(MadeFrameActivity.this, "信息未保存", Toast.LENGTH_SHORT).show();
                finish();
                break;
            case R.id.btn_mf_add_item:
                final View item = LayoutInflater.from(this).inflate(R.layout.item_madefragm, null);
                mllContent.addView(item);
                items.add(item);
                Button btnDel = (Button) item.findViewById(R.id.btn_madeframe_del);
                btnDel.setTextColor(Color.BLUE);
                btnDel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mllContent.removeView(item);
                        items.remove(item);
                    }
                });
                break;
            case R.id.btn_mf_confirm:
                Boolean frames = getFrames();
                if (!frames) {
                    return;
                }
                Intent resultIntent = new Intent();
                Bundle bundle = new Bundle();
                if (!falg) {
                    bundle.putSerializable("result", woodenFrames);
                } else {
                    if (!isCommited) {
                        for (int i = 0; i < map.size(); i++) {
                            if (!map.get(list.get(i).getRecordId())) {
                                list.get(i).setRecordId("-" + list.get(i).getRecordId());
                            }
                        }
                        int sizeWood = woodenFrames.size();
                        int sizeList = list.size();
                        for (int i = 0; i < sizeWood - sizeList; i++) {
                            list.add(woodenFrames.get(sizeList + i));
                        }
                        bundle.putSerializable("result", list);
                    }
                }
                //有DocumentNumber说明是通过运单列表传入的数据
                if (!TextUtils.isEmpty(documentNumber)) {
                    saveDocument(documentNumber);
                    return;
                }
                resultIntent.putExtras(bundle);
                this.setResult(RESULT_OK, resultIntent);
                Toast.makeText(MadeFrameActivity.this, "以保存打货架信息", Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
    }

    private void saveDocument(String documentNumber) {
        showProgressDialog();
        final String endPoint = SoapEndpoint.SET_IS_SIGNUP;
        final String soapAction = SoapAction.SaveWoodenFrames;
        final LoginInfo loginInfo = BaseApplication.loginInfo;
        String info = SoapInfo.SaveWoodFrames;
        info = info.replace("userIdValue", loginInfo.getUserId() + "");
        info = info.replace("clientNameValue", Infos.CLIENTNAME);
        info = info.replace("sessionIdValue", loginInfo.getSessionId());
        info = info.replace("documentNumberValue", documentNumber);
        String WoodenFramesValue = "";
        if (list.size() == 0) {
            list = woodenFrames;
        }
        for (int i = 0; i < list.size(); i++) {
            WoodenFramesValue += "<WoodenFrame>";
            if (TextUtils.isEmpty(list.get(i).getRecordId())) {
                WoodenFramesValue += "<RecordId>" + "0" + "</RecordId>";
            } else
                WoodenFramesValue += "<RecordId>" + list.get(i).getRecordId() + "</RecordId>";
            WoodenFramesValue += "<DocumentNumber>" + documentNumber + "</DocumentNumber>";
            WoodenFramesValue += "<Length>" + list.get(i).getLength() + "</Length>";
            WoodenFramesValue += "<Width>" + list.get(i).getWidth() + "</Width>";
            WoodenFramesValue += "<Height>" + list.get(i).getHeight() + "</Height>";
            WoodenFramesValue += "<Quantity>" + list.get(i).getQuantity() + "</Quantity>";
            WoodenFramesValue += "<Remarks>" + list.get(i).getRemarks() + "</Remarks>";
            WoodenFramesValue += "<Creator>" + "</Creator>";
            WoodenFramesValue += "<CreateTime>" + "2016-11-23T00:00:00" + "</CreateTime>";
            WoodenFramesValue += "<HandlerId>" + "0" + "</HandlerId>";
            WoodenFramesValue += "<Handler>" + "</Handler>";
            WoodenFramesValue += "<State>" + "</State>";
            WoodenFramesValue += "<AssignTime>" + "</AssignTime>";
            WoodenFramesValue += "<AcceptTime>" + "</AcceptTime>";
            WoodenFramesValue += "<FinishedTime>" + "</FinishedTime>";
            WoodenFramesValue += "</WoodenFrame>";
        }
        info = info.replace("WoodenFramesValue", WoodenFramesValue);
        final String finalInfo = info;
        LogU.e(info);
        MySoapHttpRequest.getString(new CallBackString() {
            @Override
            public void httFinsh(final String data) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        LogU.e(data);
                        closeProgressDialog();
                        RetrueCodeHandler returnInfo = RetruenUtils.getReturnInfo(data, new RetrueCodeHandler());
                        assert returnInfo != null;
                        if (returnInfo.getString().equals("0")) {
                            Tos("提交成功");
                            finish();
                        } else if (returnInfo.getString().equals("-1")) {
                            Tos(returnInfo.getError());
                        } else {
                            Tos("提交失败");
                            isCommited = true;
                        }
                    }
                });
            }
        }, endPoint, soapAction, finalInfo);
    }

    private boolean isCommited = false;

    private Boolean getFrames() {
        woodenFrames.clear();
        try {
            for (int i = 0; i < items.size(); i++) {
                WoodenFrame woodenFrame = new WoodenFrame();
                View view = items.get(i);
                EditText etLength = (EditText) view.findViewById(R.id.et_madeframe_length);
                String length = etLength.getText().toString();
                if (TextUtils.isEmpty(length)) {
                    Toast.makeText(MadeFrameActivity.this, "编辑栏不能为空", Toast.LENGTH_SHORT).show();
                    return false;
                }
                woodenFrame.setLength(length);

                EditText etWith = (EditText) view.findViewById(R.id.et_madeframe_with);
                String with = etWith.getText().toString();
                if (TextUtils.isEmpty(with)) {
                    Toast.makeText(MadeFrameActivity.this, "编辑栏不能为空", Toast.LENGTH_SHORT).show();
                    return false;
                }
                woodenFrame.setWidth(with);


                EditText etHeight = (EditText) view.findViewById(R.id.et_madeframe_height);
                String height = etHeight.getText().toString();
                if (TextUtils.isEmpty(height)) {
                    Toast.makeText(MadeFrameActivity.this, "编辑栏不能为空", Toast.LENGTH_SHORT).show();
                    return false;
                }
                woodenFrame.setHeight(height);
                EditText etQty = (EditText) view.findViewById(R.id.et_madeframe_qty);
                String qty = etQty.getText().toString();
                if (TextUtils.isEmpty(qty)) {
                    Toast.makeText(MadeFrameActivity.this, "编辑栏不能为空", Toast.LENGTH_SHORT).show();
                    return false;
                }
                woodenFrame.setQuantity(qty);
                EditText etRemark = (EditText) view.findViewById(R.id.et_madeframe_remark);
                woodenFrame.setRemarks(etRemark.getText().toString());
                woodenFrames.add(woodenFrame);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
