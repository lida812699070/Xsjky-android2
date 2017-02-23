package cn.xsjky.android.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import cn.xsjky.android.BaseApplication;
import cn.xsjky.android.R;
import cn.xsjky.android.constant.SoapAction;
import cn.xsjky.android.constant.SoapEndpoint;
import cn.xsjky.android.constant.SoapInfo;
import cn.xsjky.android.model.Custom;
import cn.xsjky.android.model.Document;
import cn.xsjky.android.model.Infos;
import cn.xsjky.android.model.LoginInfo;
import cn.xsjky.android.util.CallBackString;
import cn.xsjky.android.util.DataFormatUtils;
import cn.xsjky.android.util.LogU;
import cn.xsjky.android.util.MySoapHttpRequest;
import cn.xsjky.android.util.RetrueCodeHandler;
import cn.xsjky.android.util.RetruenUtils;
import cn.xsjky.android.util.SaveCutomerXmlparser;
import cn.xsjky.android.weiget.ProvinceDialog;
import views.NiceSpinner;

public class AddCutomerActivity extends BaseActivity implements View.OnClickListener {

    private NiceSpinner mSmsReciviceSpinner;
    private EditText mEtSelectProvince;

    private String telAreaCode = "";
    private EditText mEtAddress;
    private EditText mEtCustomerName;
    private EditText mEtFax;
    private EditText mEtPerson;
    private EditText mEtTel;
    private EditText mEtRemarks;
    private List<String> mDataset;
    private LinkedList<String> mDatasets;
    private Context context;
    private Custom mCustom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cutomer);
        Intent intent = getIntent();
        mCustom = (Custom) intent.getSerializableExtra("data");
        context = this;
        if (mCustom == null)
            initToolbar("新增客户");
        else
            initToolbar("修改客户");
        findviews();
    }

    private void findviews() {
        initSpinner();
        mEtSelectProvince = (EditText) findViewById(R.id.et_addCustomerAc_shipper_province);
        mEtCustomerName = (EditText) findViewById(R.id.et_addcustomerAc_CustomerName);
        mEtFax = (EditText) findViewById(R.id.et_addcustomerAc_Fax);
        mEtPerson = (EditText) findViewById(R.id.et_addcustomerAc_ContactPerson);
        mEtTel = (EditText) findViewById(R.id.et_addcustomerAc_Tel);
        mEtRemarks = (EditText) findViewById(R.id.et_addcustomerAc_Remarks);
        mEtAddress = (EditText) findViewById(R.id.et_addCustomerAc_shipper_Address);
        Button btnAdd = (Button) findViewById(R.id.btn_addCustomerAc_add);
        mEtSelectProvince.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
        if (mCustom != null) {
            mEtCustomerName.setText(mCustom.getCustomerName());
            mEtFax.setText(mCustom.getFax());
            mEtPerson.setText(mCustom.getContactPerson());
            mEtTel.setText(mCustom.getTel());
            mEtRemarks.setText(mCustom.getRemarks());
            String address = mCustom.getAddress();
            try {
                String[] split = address.split(" ", 4);
                mEtSelectProvince.setText(split[0] + "," + split[1] + "," + split[2]);
                mEtAddress.setText(split[3]);
            } catch (Exception e) {

            }
        }
    }

    private void initSpinner() {
        mSmsReciviceSpinner = (NiceSpinner) findViewById(R.id.nice_spinner_addCustomAc_SmsReciviceSelect);
        mDataset = new LinkedList<>(Arrays.asList("自动", "启用", "禁用"));
        mDatasets = new LinkedList<>(Arrays.asList("Auto", "Enabled", "Disabled"));
        mSmsReciviceSpinner.attachDataSource(mDataset);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.et_addCustomerAc_shipper_province:
                String str = mEtSelectProvince.getText().toString();
                if (str.split(",").length != 3) {
                    str = "";
                }
                ProvinceDialog dialog = new ProvinceDialog(this, str);
                dialog.setListener(new ProvinceDialog.OnProvinceClick() {
                    @Override
                    public void onData(String province, String areaCode, String code) {
                        mEtSelectProvince.setText(province);
                        mEtSelectProvince.setTag(code);
                        telAreaCode = areaCode;
                    }
                });
                try {
                    dialog.show();
                } catch (Exception e) {
                    Toast.makeText(this, "地址解析错误", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_addCustomerAc_add:
                addCustomerNetWork();
                break;

        }
    }

    private void addCustomerNetWork() {
        showProgressDialog();
        final String endPoint = SoapEndpoint.SET_IS_SIGNUP;
        final String soapAction = SoapAction.SaveCustomer;
        final LoginInfo loginInfo = BaseApplication.loginInfo;
        String info = SoapInfo.SaveCustomer;
        info = info.replace("UserIdValue", loginInfo.getUserId() + "");
        if (mCustom == null) {

            info = info.replace("CustomerIdValue", "0");
        } else {

            info = info.replace("CustomerIdValue", mCustom.getCustomerId());
        }
        info = info.replace("ClientNameValue", Infos.CLIENTNAME);
        info = info.replace("SessionIdValue", loginInfo.getSessionId());
        info = info.replace("RoleDataValue", "0");
        info = info.replace("CustomerNameValue", mEtCustomerName.getText().toString());
        info = info.replace("FaxValue", mEtFax.getText().toString());
        info = info.replace("TelAreaCodeValue", telAreaCode);
        info = info.replace("TelValue", mEtTel.getText().toString());
        info = info.replace("ContactPersonValue", mEtPerson.getText().toString());
        info = info.replace("RemarksValue", mEtRemarks.getText().toString());
        String strProvince = mEtSelectProvince.getText().toString();
        String strAddress = mEtAddress.getText().toString();
        strProvince = strProvince.replace(",", " ");

        info = info.replace("AddressValue", strProvince + "  " + strAddress);
        info = info.replace("SendSmsToReceiverValue", mDatasets.get(mSmsReciviceSpinner.getSelectedIndex()));
        final String finalInfo = info;
        MySoapHttpRequest.getString(new CallBackString() {
            @Override
            public void httFinsh(final String data) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        LogU.e(data);
                        closeProgressDialog();
                        setHandler(HIDE_LOADING, "");
                        RetrueCodeHandler parser = RetruenUtils.getReturnInfo(data, new RetrueCodeHandler());
                        if (parser != null && parser.getString().equals("0")) {
                            SaveCutomerXmlparser saveCutomerXmlparser = RetruenUtils.getReturnInfo(data, new SaveCutomerXmlparser());
                            if (saveCutomerXmlparser != null) {
                                Custom custom = saveCutomerXmlparser.getUser();
                                LogU.e(custom.toString());
                                ArrayList<Custom> customs = new ArrayList<>();
                                customs.add(custom);
                                if (mCustom == null) {
                                    Custom.updataListIntoDb(customs);
                                } else {
                                    Custom customNews = new Custom();
                                    customNews.setCustomerName(mEtCustomerName.getText().toString());
                                    customNews.setCustomerId(mCustom.getCustomerId());
                                    customNews.setFax(mEtFax.getText().toString());
                                    customNews.setContactPerson(mEtPerson.getText().toString());
                                    customNews.setTel(mEtTel.getText().toString().trim());
                                    customNews.setRemarks(mEtRemarks.getText().toString());
                                    customNews.setAddress(mEtSelectProvince.getText().toString().replace(",", " ") + " " + mEtAddress.getText());
                                    Custom.updata(customNews);
                                }
                                Tos("保存完成");
                                finish();
                            }
                        } else if (parser != null && parser.getString().equals("-1")) {
                            Toast.makeText(context, parser.getError(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "数据获取错误", Toast.LENGTH_SHORT).show();
                        }
                        closeProgressDialog();
                    }
                });
                //mAdapter.notifyDataSetChanged();
                setHandler(HIDE_LOADING, "");

            }
        }, endPoint, soapAction, finalInfo);
    }
}
