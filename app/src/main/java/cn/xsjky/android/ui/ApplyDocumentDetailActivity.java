package cn.xsjky.android.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.exception.DbException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.xsjky.android.BaseApplication;
import cn.xsjky.android.BaseSettings;
import cn.xsjky.android.R;
import cn.xsjky.android.adapter.ItemHeadMadefragmAdapter;
import cn.xsjky.android.db.ErrorDocument;
import cn.xsjky.android.db.MyListItem;
import cn.xsjky.android.db.SqlUtils;
import cn.xsjky.android.http.HttpCallback;
import cn.xsjky.android.model.Document;
import cn.xsjky.android.model.ShipperCost;
import cn.xsjky.android.model.WoodenFrame;
import cn.xsjky.android.util.SPUtils;
import cn.xsjky.android.util.StrKit;
import cn.xsjky.android.util.TempletUtil;
import cn.xsjky.android.util.XmlParserUtil;
import cn.xsjky.android.weiget.LoadingDialog;
import cn.xsjky.android.weiget.MyListView;

public class ApplyDocumentDetailActivity extends Activity {
    private ImageView mFlip;
    private TextView mTitle;
    private ImageView mEdit;

    private TextView mNumber;
    private TextView mFromNetwrok;
    private TextView mToNetwork;
    private TextView mShipperName;
    private TextView mShipperContact;
    private TextView mShipperPhone;
    private TextView mShipperAddress;
    private TextView mConsigneeName;
    private TextView mConsigneeContact;
    private TextView mConsigneePhone;
    private TextView mConsigneeAddress;
    private TextView mShippingMode;
    private TextView mProductName;
    private TextView mProductVolum;
    private TextView mProductQuantity;
    private TextView mProductWeight;
    private TextView mInsuranceAmt;
    private TextView mNeedInsurance;
    private TextView mPremium;
    private ListView mListView;
    private TextView mCarriage;
    private TextView mTotal;
    private TextView mBlanceMode;
    private LinearLayout mMonthLyLayout;
    private TextView mMonlyId;
    private TextView mRemarks;
    private LinearLayout mInsuranceLayout;
    private Button mConfirmBtn;
    private Button mPrintBtn;
    private TextView mNeedDelivery;
    private TextView mDeliveryCharge;
    private int RESULT = 0;

    private Document mDocument;
    private List<ShipperCost> shipperCostList;
    private Context mContext;
    private MyListView mlvMadeFragm;
    private TextView mCountWeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_doc_details);
        mContext = this;
        findViewById();
        mDocument = BaseApplication.getApplication().getDocument();
        init();
        setListener();
    }

    private void init() {
        mNumber.setText(mDocument.getDocumentNumber());
        mFromNetwrok.setText(mDocument.getFromCity());
        mToNetwork.setText(mDocument.getToCity());
        mShipperName.setText(mDocument.getShipperName());
        mShipperContact.setText(mDocument.getShipperContactName());
        mShipperPhone.setText(mDocument.getShipperAreaCode() + mDocument.getShipperPhoneNumber());
        mShipperAddress.setText(mDocument.getShipperAddress().getProvince()
                + mDocument.getShipperAddress().getCity()
                + mDocument.getShipperAddress().getDistrict()
                + mDocument.getShipperAddress().getAddress());
        mConsigneeName.setText(mDocument.getConsigneeName());
        mConsigneeContact.setText(mDocument.getConsigneeContactPerson());
        String consigneeAreaCode = mDocument.getConsigneeAreaCode();
        if (TextUtils.isEmpty(consigneeAreaCode) || consigneeAreaCode.equals("null")) {
            consigneeAreaCode = "";
        }
        mConsigneePhone.setText(consigneeAreaCode + mDocument.getConsigneePhoneNumber());
        mConsigneeAddress.setText(mDocument.getConsigneeAddress().getProvince()
                + mDocument.getConsigneeAddress().getCity()
                + mDocument.getConsigneeAddress().getDistrict()
                + mDocument.getConsigneeAddress().getAddress());
        mConsigneeName.setText(mDocument.getConsigneeName());
        mShippingMode.setText(mDocument.getShippingMode().getModeName());
        mProductName.setText(mDocument.getProductName());
        mCountWeight.setText(mDocument.getCountWeight());
        mProductVolum.setText(String.valueOf(mDocument.getVolumn()));
        mProductQuantity.setText(String.valueOf(mDocument.getQuantity()));
        mProductWeight.setText(String.valueOf(mDocument.getWeight()));
        if (mDocument.isNeedDelivery()) {
            mNeedDelivery.setText("送货上门");
            if (mDocument.isUpstair()) {
                mNeedDelivery.setText("送货上楼");
            }
            mDeliveryCharge.setText(String.valueOf(mDocument.getDeliveryCharge()));
        } else {
            mNeedDelivery.setText("自提");
            mDeliveryCharge.setText("0");
        }

        if (mDocument.isNeedInsurance()) {
            mInsuranceAmt.setText(String.valueOf(mDocument.getInsuranceAmt()));
            mPremium.setText(String.valueOf(mDocument.getPremium()));
        } else
            mInsuranceLayout.setVisibility(View.GONE);
        mCarriage.setText(String.valueOf(mDocument.getCarriage()));
        mTotal.setText(String.valueOf(mDocument.getTotalCharge()));
        String mode = "";
        if (mDocument.getBalanceMode().equals("Monthly")) {
            mMonthLyLayout.setVisibility(View.VISIBLE);
            mMonlyId.setText(String.valueOf(mDocument.getMonthlyBalanceAccount()));
            mode = "月结客户";
        }
        if (mDocument.getBalanceMode().equals("SenderPay"))
            mode = "寄方付";
        else
            mode = "收方付";
        mBlanceMode.setText(mode);
        if (StrKit.isBlank(mDocument.getRemarks()))
            mRemarks.setVisibility(View.GONE);
        mRemarks.setText(mDocument.getRemarks());

        if (mDocument.getCarriageItems() == null)
            shipperCostList = new ArrayList<ShipperCost>();
        else
            shipperCostList = mDocument.getCarriageItems();
        mListView.setAdapter(new CarriageAdapter());
        setListViewHeightBasedOnChildren(mListView);
        ItemHeadMadefragmAdapter<WoodenFrame> adapter = new ItemHeadMadefragmAdapter<>(ApplyDocumentDetailActivity.this, mDocument.getWoodenFrames());
        mlvMadeFragm.setAdapter(adapter);
    }

    private void findViewById() {
        mNumber = (TextView) findViewById(R.id.document_detail_number);
        mlvMadeFragm = (MyListView) findViewById(R.id.lv_applyDocumentDetail_madeFragm);
        mFromNetwrok = (TextView) findViewById(R.id.document_detail_fromNetwork);
        mToNetwork = (TextView) findViewById(R.id.document_detail_toNetwork);
        mShipperName = (TextView) findViewById(R.id.document_detail_shipper_name);
        mShipperContact = (TextView) findViewById(R.id.document_detail_shipper_contactname);
        mShipperPhone = (TextView) findViewById(R.id.document_detail_shipper_phone);
        mShipperAddress = (TextView) findViewById(R.id.document_detail_shipper_address);
        mConsigneeName = (TextView) findViewById(R.id.document_detail_consignee_name);
        mConsigneeContact = (TextView) findViewById(R.id.document_detail_consignee_contactname);
        mConsigneePhone = (TextView) findViewById(R.id.document_detail_consignee_phone);
        mConsigneeAddress = (TextView) findViewById(R.id.document_detail_consignee_address);
        mShippingMode = (TextView) findViewById(R.id.document_detail_shipping_mode);
        mProductName = (TextView) findViewById(R.id.document_detail_product_name);
        mCountWeight = (TextView) findViewById(R.id.document_detail_count_weight);
        mProductVolum = (TextView) findViewById(R.id.document_detail_product_volumn);
        mProductQuantity = (TextView) findViewById(R.id.document_detail_product_quantity);
        mProductWeight = (TextView) findViewById(R.id.document_detail_product_weight);
        mInsuranceAmt = (TextView) findViewById(R.id.document_detail_insuranceAmt);
        mNeedInsurance = (TextView) findViewById(R.id.document_detail_needInsurance);
        mPremium = (TextView) findViewById(R.id.document_detail_premium);
        mListView = (ListView) findViewById(R.id.document_detail_listview);
        mCarriage = (TextView) findViewById(R.id.document_detail_carriage);
        mTotal = (TextView) findViewById(R.id.document_detail_total);
        mBlanceMode = (TextView) findViewById(R.id.document_detail_balance_mode);
        mMonthLyLayout = (LinearLayout) findViewById(R.id.document_detail_monthly_layout);
        mMonlyId = (TextView) findViewById(R.id.document_detail_monthlyid);
        mRemarks = (TextView) findViewById(R.id.document_detail_remarks);
        mInsuranceLayout = (LinearLayout) findViewById(R.id.document_detail_insuranceLayout);
        mConfirmBtn = (Button) this.findViewById(R.id.document_detail_confirm_btn);
        mPrintBtn = (Button) this.findViewById(R.id.document_detail_print_btn);
        mNeedDelivery = (TextView) this.findViewById(R.id.document_detail_needDelivery);
        mDeliveryCharge = (TextView) this.findViewById(R.id.document_detail_deliveryCharge);
        mFlip = (ImageView) this.findViewById(R.id.head_flip);
        mFlip.setVisibility(View.VISIBLE);
        mFlip.setImageResource(R.drawable.ic_flipper_head_back);
        mTitle = (TextView) this.findViewById(R.id.head_title);
        mTitle.setText("详细信息");
        mEdit = (ImageView) this.findViewById(R.id.head_tool1);
        mEdit.setVisibility(View.VISIBLE);
        mEdit.setImageResource(R.drawable.ic_edit);
        mEdit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ApplyDocumentDetailActivity.this, NewActivity.class);
                intent.putExtra("isEdit", true);
                ApplyDocumentDetailActivity.this.startActivity(intent);
            }
        });
    }

    private void setListener() {
        mFlip.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ApplyDocumentDetailActivity.this.setResult(RESULT, ApplyDocumentDetailActivity.this.getIntent());
                ApplyDocumentDetailActivity.this.finish();
            }
        });
        mConfirmBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isPrint = (boolean) SPUtils.get(ApplyDocumentDetailActivity.this, mDocument.getDocumentNumber(), false);
                if (isPrint) {
                    confirmPrint();
                    return;
                }
               /* if (!BaseApplication.printStatus) {
                    Toast.makeText(ApplyDocumentDetailActivity.this, "请确认打印完标签", Toast.LENGTH_SHORT).show();
                    return;
                }*/
                Confirm();
            }
        });

        mPrintBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ApplyDocumentDetailActivity.this, FreightActivity.class);
                ApplyDocumentDetailActivity.this.startActivity(intent);
            }
        });
    }

    private void confirmPrint() {
        Map<String, String> map = BaseApplication.getApplication().getSecurityInfo();
        map.put("documentId", String.valueOf(this.mDocument.getRecordId()));
        String templet = TempletUtil.render(BaseSettings.AfterPrintCargoLabel_TEMPLET, map);
        BaseApplication.getApplication().getHttpPro().postAsync(templet, BaseSettings.AfterPrintCargoLabel_ACTION, new HttpCallback() {
            @Override
            public void onHttpStart() {
                handler.sendEmptyMessage(SHOW_LOADING);
            }

            @Override
            public void onHttpFinish(String data) {
                if (StrKit.isBlank(data)) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ApplyDocumentDetailActivity.this, "提交数据失败", 1).show();
                        }
                    });
                    return;
                }
                XmlParserUtil parser = XmlParserUtil.getInstance();
                parser.parse(data);
                String prefix = "/soap:Envelope/soap:Body/AfterPrintCargoLabelResponse/AfterPrintCargoLabelResult/";
                String code = parser.getNodeValue(prefix + "ReturnCode");
                if (StrKit.isBlank(code) || !code.equals("0")) {
                    String error = parser.getNodeValue(prefix + "Error");
                    showError(error);
                } else {
                    Confirm();
                }
                handler.sendEmptyMessage(HIDE_LOADING);
            }

            @Override
            public void onHttpError(String msg) {
                showError(msg);
                handler.sendEmptyMessage(HIDE_LOADING);
            }

            @Override
            public void onHttpEnd() {

            }
        });
    }

    private void Confirm() {
        Map<String, String> map = BaseApplication.getApplication().getSecurityInfo();
        map.put("documentId", String.valueOf(mDocument.getRecordId()));
        String templet = TempletUtil.render(BaseSettings.SETISRECEIVED_TEMPLET, map);
        BaseApplication.getApplication().getHttpPro().postAsync(templet, BaseSettings.SETISRECEIVED_ACTION, new HttpCallback() {
            @Override
            public void onHttpStart() {
                handler.sendEmptyMessage(SHOW_LOADING);
            }

            @Override
            public void onHttpFinish(String data) {
                if (StrKit.isBlank(data)) {
                    showError("确认收货失败,请重试！");
                    return;
                }
                XmlParserUtil parser = XmlParserUtil.getInstance();
                parser.parse(data);
                String prefix = "/soap:Envelope/soap:Body/SetIsReceivedResponse/SetIsReceivedResult/";
                String code = parser.getNodeValue(prefix + "ReturnCode");
                if (StrKit.isBlank(code) || !code.equals("0")) {
                    String error = parser.getNodeValue(prefix + "Error");
                    showError(error);
                } else {
                    showError("确认收货成功！");
                    SPUtils.remove(ApplyDocumentDetailActivity.this, mDocument.getDocumentNumber());
                    SqlUtils.ErrorDocumentDel(mDocument.getDocumentNumber());
                    ApplyDocumentDetailActivity.this.finish();
                }
                handler.sendEmptyMessage(HIDE_LOADING);
                RESULT = 1;
            }

            @Override
            public void onHttpError(String msg) {
                showError(msg);
                handler.sendEmptyMessage(HIDE_LOADING);
            }

            @Override
            public void onHttpEnd() {

            }
        });
    }

    private void showError(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private final int SHOW_LOADING = 1;
    private final int HIDE_LOADING = 2;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW_LOADING:
                    LoadingDialog.show(ApplyDocumentDetailActivity.this, false, false);
                    break;
                case HIDE_LOADING:
                    LoadingDialog.dismiss();
                    break;
            }
        }
    };

    private class CarriageAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        private ViewHolder mHolder;

        public CarriageAdapter() {
            inflater = LayoutInflater.from(mContext);
        }

        @Override
        public int getCount() {
            return shipperCostList.size();
        }

        @Override
        public Object getItem(int position) {
            return shipperCostList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.list_item_carriage, null);
                mHolder = new ViewHolder();
                mHolder.mName = (TextView) convertView.findViewById(R.id.carriage_item_name);
                mHolder.mValue = (TextView) convertView.findViewById(R.id.carriage_item_value);
                convertView.setTag(mHolder);
            } else
                mHolder = (ViewHolder) convertView.getTag();

            ShipperCost sc = shipperCostList.get(position);
            mHolder.mName.setText(sc.getFreightItem().getItemName());
            mHolder.mValue.setText(String.valueOf(sc.getChargeValue()));
            return convertView;
        }

        class ViewHolder {
            TextView mName;
            TextView mValue;
        }

    }

    ;

    public void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) { // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

}
