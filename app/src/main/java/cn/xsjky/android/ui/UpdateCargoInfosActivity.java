package cn.xsjky.android.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.lidroid.xutils.http.RequestParams;

import java.util.ArrayList;

import cn.xsjky.android.BaseApplication;
import cn.xsjky.android.BaseSettings;
import cn.xsjky.android.R;
import cn.xsjky.android.http.Urls;
import cn.xsjky.android.model.CargoInfo;
import cn.xsjky.android.util.CallBackString;
import cn.xsjky.android.util.GetCargoInfosXmlparser;
import cn.xsjky.android.util.LogU;
import cn.xsjky.android.util.RetruenUtils;

public class UpdateCargoInfosActivity extends BaseActivity {

    private LinearLayout mLl_content;
    private Button mBtnAddview;
    private Button mBtnOk;
    private static ArrayList<CargoInfo> mCargoInfos;
    private String mRequestId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_cargo_infos);
        mRequestId = getIntent().getStringExtra("requestId");
        initToolbar();
        findviews();
    }

    public static ArrayList<CargoInfo> getCargoInfos() {
        return mCargoInfos;
    }

    public static void clearCargoInfos() {
        if (mCargoInfos != null){
            mCargoInfos.clear();
            mCargoInfos = null;
        }
    }

    private void initToolbar() {
        initToolbar("货物详情");
    }

    private void findviews() {
        mLl_content = (LinearLayout) findViewById(R.id.ll_updata_cargo_content);
        mBtnAddview = (Button) findViewById(R.id.btn_updatacargo_addview);
        mBtnAddview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addView();
            }
        });
        mBtnOk = (Button) findViewById(R.id.btn_updatacargo_ok);
        mBtnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setData();
            }
        });
        if (TextUtils.isEmpty(mRequestId)) {

            addView();
        } else {
            getCatgoInfos();
            findViewById(R.id.ll_btn_okAndAdd).setVisibility(View.GONE);
        }
    }

    private void getCatgoInfos() {
        RequestParams params = new RequestParams();
        params.addBodyParameter("sessionId", BaseApplication.loginInfo.getSessionId());
        params.addBodyParameter("userId", BaseApplication.loginInfo.getUserId() + "");
        params.addBodyParameter("clientName", BaseSettings.CLIENT_NAME);
        params.addBodyParameter("requestId", mRequestId);
        getData(Urls.GetCargoInfos, params, new CallBackString() {
            @Override
            public void httFinsh(String data) {
                GetCargoInfosXmlparser returnInfo = RetruenUtils.getReturnInfo(data, new GetCargoInfosXmlparser());
                if (returnInfo != null) {
                    ArrayList<CargoInfo> cargoInfos = returnInfo.getList();
                    if (cargoInfos != null && cargoInfos.size() != 0) {
                        LogU.e(cargoInfos.toString());
                        showData(cargoInfos);
                    } else {
                        Tos("没有物品详情");
                    }
                }
            }
        });
    }

    private void showData(ArrayList<CargoInfo> cargoInfos) {
        for (int i = 0; i < cargoInfos.size(); i++) {
            showAddView(cargoInfos.get(i));
        }
    }

    public static void setPricePoint(final EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0,
                                s.toString().indexOf(".") + 3);
                        editText.setText(s);
                        editText.setSelection(s.length());
                    }
                }
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    editText.setText(s);
                    editText.setSelection(2);
                }

                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        editText.setText(s.subSequence(0, 1));
                        editText.setSelection(1);
                        return;
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }

        });

    }

    private void setData() {
        mCargoInfos = new ArrayList<>();
        for (int i = 0; i < mLl_content.getChildCount(); i++) {
            View llItem = mLl_content.getChildAt(i);
            EditText etProductName = (EditText) llItem.findViewById(R.id.et_updata_productName);
            EditText etLength = (EditText) llItem.findViewById(R.id.et_updata_Length);
            EditText etWidth = (EditText) llItem.findViewById(R.id.et_updata_Width);
            EditText etHeight = (EditText) llItem.findViewById(R.id.et_updata_height);
            EditText etQty = (EditText) llItem.findViewById(R.id.et_updata_qty);
            EditText etVol = (EditText) llItem.findViewById(R.id.et_updata_Volumn);
            EditText etRemarks = (EditText) llItem.findViewById(R.id.et_updata_remarks);
            if (TextUtils.isEmpty(etProductName.getText().toString()) ||
                    TextUtils.isEmpty(etLength.getText().toString()) ||
                    TextUtils.isEmpty(etWidth.getText().toString()) ||
                    TextUtils.isEmpty(etHeight.getText().toString()) ||
                    TextUtils.isEmpty(etQty.getText().toString()) ||
                    TextUtils.isEmpty(etVol.getText().toString())) {
                Tos("信息不能为空");
                return;
            }
            CargoInfo cargoInfo = new CargoInfo();
            cargoInfo.setProductName(etProductName.getText().toString());
            cargoInfo.setVolumn(etVol.getText().toString());
            cargoInfo.setHeight(etHeight.getText().toString());
            cargoInfo.setLength(etLength.getText().toString());
            cargoInfo.setWidth(etWidth.getText().toString());
            cargoInfo.setQuantity(etQty.getText().toString());
            cargoInfo.setRemarks(etRemarks.getText().toString());
            mCargoInfos.add(cargoInfo);
        }
        finish();
    }

    private void addView() {
        final View inflate = LayoutInflater.from(this).inflate(R.layout.updata_cargo_item, null);
        Button btnDelete = (Button) inflate.findViewById(R.id.btn_updata_delete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLl_content.removeView(inflate);
            }
        });
        mLl_content.addView(inflate);
    }

    private void showAddView(CargoInfo cargoInfo) {
        final View llItem = LayoutInflater.from(this).inflate(R.layout.updata_cargo_item, null);
        llItem.findViewById(R.id.btn_updata_delete).setVisibility(View.INVISIBLE);
        EditText etProductName = (EditText) llItem.findViewById(R.id.et_updata_productName);
        etProductName.setVisibility(View.GONE);
        EditText etLength = (EditText) llItem.findViewById(R.id.et_updata_Length);
        EditText etWidth = (EditText) llItem.findViewById(R.id.et_updata_Width);
        EditText etHeight = (EditText) llItem.findViewById(R.id.et_updata_height);
        EditText etQty = (EditText) llItem.findViewById(R.id.et_updata_qty);
        EditText etVol = (EditText) llItem.findViewById(R.id.et_updata_Volumn);
        EditText etRemarks = (EditText) llItem.findViewById(R.id.et_updata_remarks);
        etRemarks.setVisibility(View.GONE);
        etProductName.setText(cargoInfo.getProductName());
        etLength.setText(cargoInfo.getLength() + "m长");
        etWidth.setText(cargoInfo.getWidth() + "m宽");
        etHeight.setText(cargoInfo.getHeight() + "m高");
        etQty.setText(cargoInfo.getQuantity() + "件");
        etVol.setText(cargoInfo.getVolumn() + "方");
        etRemarks.setText(cargoInfo.getRemarks());
        mLl_content.addView(llItem);
    }
}
