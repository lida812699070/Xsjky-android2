package cn.xsjky.android.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.thoughtworks.xstream.XStream;

import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import cn.xsjky.android.BaseApplication;
import cn.xsjky.android.BaseSettings;
import cn.xsjky.android.R;
import cn.xsjky.android.adapter.ItemSimpleAdapterAdapter;
import cn.xsjky.android.db.ErrorDocument;
import cn.xsjky.android.db.NewDocumentDataCache;
import cn.xsjky.android.db.SqlUtils;
import cn.xsjky.android.http.HttpCallback;
import cn.xsjky.android.http.Urls;
import cn.xsjky.android.model.Address;
import cn.xsjky.android.model.CityModel;
import cn.xsjky.android.model.Company;
import cn.xsjky.android.model.Consignee;
import cn.xsjky.android.model.Contact;
import cn.xsjky.android.model.CountWeightInfo;
import cn.xsjky.android.model.Custom;
import cn.xsjky.android.model.CustomJson;
import cn.xsjky.android.model.CustomOtherInfos;
import cn.xsjky.android.model.Customer;
import cn.xsjky.android.model.CustomerReceivers;
import cn.xsjky.android.model.Document;
import cn.xsjky.android.model.FreightItem;
import cn.xsjky.android.model.Network;
import cn.xsjky.android.model.Recivicer;
import cn.xsjky.android.model.ShipPerson;
import cn.xsjky.android.model.ShipperCost;
import cn.xsjky.android.model.ShippingMode;
import cn.xsjky.android.model.SimpleDocument;
import cn.xsjky.android.model.SynchronizeData;
import cn.xsjky.android.model.WoodenFrame;
import cn.xsjky.android.util.CallBackString;
import cn.xsjky.android.util.DateFormatUtils;
import cn.xsjky.android.util.DialoginOkCallBack;
import cn.xsjky.android.util.LogU;
import cn.xsjky.android.util.NetworkDetector;
import cn.xsjky.android.util.RetrueCodeHandler;
import cn.xsjky.android.util.RetruenUtils;
import cn.xsjky.android.util.SPUtils;
import cn.xsjky.android.util.SimpleDocumentXmlparser;
import cn.xsjky.android.util.StrKit;
import cn.xsjky.android.util.SynchronizeDataXmlparser;
import cn.xsjky.android.util.TempletUtil;
import cn.xsjky.android.util.XmlParserUtil;
import cn.xsjky.android.view.MyListView;
import cn.xsjky.android.weiget.LoadingDialog;
import cn.xsjky.android.weiget.ProvinceDialog;

public class NewActivity extends BaseActivity {
    public final static int SCANNING_REQUEST_CODE = 1;
    public final static int MADEFRAGM_REQUEST_CODE = 2;
    private static final int CAMERA_PERMISSION = 1;
    public static final int COUNTWEIGHT_ACTIVITY = 3;
    private ImageView mFlip;
    private TextView mTitle;
    private EditText mNumber;
    private Button mNumberBtn;
    private Button mScannerBtn;
    private Spinner mShippingMode;
    private Spinner mBalanceMode;
    private ListView mListView;
    private List<ShipperCost> shipperCostList;
    private CarriageAdapter mCarriageAdapter;
    //发件人
    private EditText mShipperName;
    private Button mShipperBtn;
    private EditText mShipperContact;
    private EditText mShipperProvince;
    private EditText mShipperAddress;
    private EditText mShipperArea;
    private EditText mShipperPhone;

    private EditText mFromNetwork;
    private EditText mToNetwork;
    private EditText mPickupBy;
    private EditText mDeliveredBy;

    //收	件人
    private EditText mConsigneeName;
    private Button mConsigneeBtn;
    private EditText mConsigneeContact;
    private EditText mConsigneeProvince;
    private EditText mConsigneeAddress;
    private EditText mConsigneeArea;
    private EditText mConsigneePhone;
    private Button mConsigneeContactBtn;

    //产品
    private EditText mProductName;
    private EditText mProductVolumn;
    private EditText mProductQuantity;
    private EditText mProductWeight;

    //保险
    private CheckBox mNeedInsurance;
    private EditText mInsuranceAmt;
    private EditText mPremium;
    private EditText mRemarks;
    private EditText mMontylyId;
    private LinearLayout mMontylyLayout;

    private EditText mCarriage;
    private EditText mTotal;
    private CheckBox mNeedDelivery;
    private CheckBox mNeedSignUpNotifyMessage;
    private EditText mDeliveryCharge;
    private Button mSaveBtn;
    private Context mContext;
    private ArrayAdapter<String> mBalanceModeAdapter;
    private ArrayAdapter<String> mShippingModeAdapter;
    private Document mDocument;
    private List<ShippingMode> mShippingModeList;
    private String[] mShippingModeStrs = {};
    private int mShippingModeId = -1;
    private int mBalanceModeId = -1;
    private String[] mBalanceModeNames = {"寄方付", "收方付", "月结客户"};
    private String[] mBalanceModeValues = {"SenderPay", "ReceiverPay", "Monthly"};
    private List<Customer> mCustomerList;
    private List<Network> mNetworkList;
    private List<CustomerReceivers> mCustomerReceiversList;
    //是否编辑模式
    private boolean isEdit = false;
    private CheckBox mNeedGOFloor;
    private CheckBox mGoodsControl;
    private EditText mShipperPhone2;

    public static final String ACTIONRECIVR = "cn.xsjky.android.ui.NewActivity.refreshRecivr";
    public static final String ACTIONSEND = "cn.xsjky.android.ui.NewActivity.refreshSend";
    public static final String ACTIONOTHERINFO = "cn.xsjky.android.ui.NewActivity.otherinfos";
    public static final String PHONE_CONTENT = ",";
    private TextView tvPhoneContent;
    private EditText mConsigneePhone2;
    private TextView tvPhoneContent2;
    private int spIndex;
    private TextView mTitleRight;
    private ArrayList<WoodenFrame> WoodenFrames;
    private String name;
    private String phone;
    private String address;
    private Button btnSelectCustom;
    private List<Custom> listCustomers;
    private ItemSimpleAdapterAdapter<String> adapterCustom;
    private EditText etCustomNametx;
    private BroadcastReceiver myReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (intent.getAction().equals(ACTIONRECIVR)) {
                    mConsigneeName.setText(BaseApplication.customReceivers.getReceiverName());
                    String strAddress = BaseApplication.customReceivers.getAddress();
                    String[] split = strAddress.split(" ", 4);
                    mConsigneeAddress.setText(split[3]);
                    mConsigneeProvince.setText(split[0] +
                            "," + split[1] +
                            "," + split[2]);
                    mConsigneeArea.setText(BaseApplication.customReceivers.getAreaCode());
                    mConsigneePhone.setText(BaseApplication.customReceivers.getMobile());
                    if (TextUtils.isEmpty(BaseApplication.customReceivers.getMobile())) {
                        mConsigneePhone.setText(BaseApplication.customReceivers.getTelNumber());
                    }
                    IsSelectCustom = true;
                } else if (intent.getAction().equals(ACTIONSEND)) {
                    mShipperName.setText(BaseApplication.customShipper.getCompanyName());
                    mShipperContact.setText(BaseApplication.customShipper.getContactName());
                    mShipperPhone.setText(BaseApplication.customShipper.getMobileNumber());
                    String address = BaseApplication.customShipper.getAddress();
                    String[] split = address.split(" ", 4);
                    mShipperProvince.setText(split[0] + "," + split[1] + "," + split[2]);
                    mShipperAddress.setText(split[3]);
                }
            } catch (Exception e) {

            }
        }

    };
    private View customer_list;
    private AlertDialog dialog;
    private AlertDialog dialogListItem;
    private ListView lvListItem;
    private String strDocuments;
    private MyListView mLvShipp;
    private MyListView mLvCon;
    private ArrayList<String> mShipList;
    private ArrayAdapter<String> mShippAdapter;
    private ArrayList<String> mConList;
    private ArrayAdapter<String> mConAdapter;
    private List<Company> mshipCompanies;
    private List<Company> mConCompanies;
    private Custom mCustom;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private TextView mTvCustom;
    private Button mButtontnDelCustom;
    private List<Company> mConsignees;
    private Button mClearShipperBtn;
    private Button mClearConsigneeBtn;
    private List<Consignee> mConsigneeList;
    private List<Custom> mCustoms;
    private EditText mCountWeight;
    private Button mBtnCountWeight;
    private EditText mConsigneeMobile;

    @Override
    protected void onDestroy() {
        if (myReceiver != null) {
            try {
                unregisterReceiver(myReceiver);
            } catch (Exception e) {

            }
        }
        super.onDestroy();
        if (mContext != null) {
            mContext = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_new);
        mContext = this;
        findViewById();
        setListener();
        isEdit = this.getIntent().getBooleanExtra("isEdit", false);
        try {
            if (isEdit) {
                mDocument = BaseApplication.getApplication().getDocument();
                initDocumentData();
                mTitle.setText("编辑运单");
                mSaveBtn.setText("更新");
            } else {
                registerRecive();
                Intent intent = getIntent();
                name = intent.getStringExtra("name");
                phone = intent.getStringExtra("phone");
                address = intent.getStringExtra("address");
                if (!TextUtils.isEmpty(name)) {
                    try {
                        init();
                    } catch (Exception e) {
                        Toast.makeText(NewActivity.this, "收件人中部分信息为空", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    spIndex = 0;
                    try {
                        init();
                    } catch (Exception e) {
                        LogU.e(e.toString());
                    }
                }
            }
        } catch (Exception e) {
            Toast.makeText(NewActivity.this, "请确认填入的信息是否正确", Toast.LENGTH_SHORT).show();
        }

    }

    private void registerRecive() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTIONRECIVR);
        filter.addAction(ACTIONSEND);
        registerReceiver(myReceiver, filter);
    }

    private List<Contact> mConsigneeContactList;
    private List<Contact> mShipperContactList;

    //收件人列表
    private void getConsigneeContacts() {
        /*if(mContactList != null){
            selectContact();
			return;
		}*/
        //Integer cid = (Integer)mShipperName.getTag();
        String findValue = "", findCity = "";
        if (StrKit.notBlank(mConsigneeName.getText().toString()))
            findValue = mConsigneeName.getText().toString();
        else if (StrKit.notBlank(mConsigneeContact.getText().toString()))
            findValue = mConsigneeContact.getText().toString();
        findCity = mToNetwork.getText().toString();
        Map<String, String> params = BaseApplication.getApplication().getSecurityInfo();
        params.put("findValue", findValue);
        params.put("findCity", findCity);
        String templet = TempletUtil.render(BaseSettings.FindContacts_TEMPLET, params);
        BaseApplication.getApplication().getHttpPro().postAsync(templet, BaseSettings.FindContacts_ACTION, new HttpCallback() {
            @Override
            public void onHttpStart() {
                handler.sendEmptyMessage(SHOW_LOADING);
            }

            @Override
            public void onHttpFinish(String data) {
                mConsigneeContactList = Contact.parserXmlList(data);
                handler.sendEmptyMessage(HIDE_LOADING);
                selectConsigneeContact();
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

    //获取发件人列表
    private void getShipperContact() {
        String findValue = "", findCity = "";
        if (StrKit.notBlank(mShipperName.getText().toString()))
            findValue = mShipperName.getText().toString();
        else if (StrKit.notBlank(mShipperContact.getText().toString()))
            findValue = mShipperContact.getText().toString();
        findCity = mFromNetwork.getText().toString();
        Map<String, String> params = BaseApplication.getApplication().getSecurityInfo();
        params.put("findValue", findValue);
        params.put("findCity", findCity);
        String templet = TempletUtil.render(BaseSettings.FindContacts_TEMPLET, params);
        BaseApplication.getApplication().getHttpPro().postAsync(templet, BaseSettings.FindContacts_ACTION, new HttpCallback() {
            @Override
            public void onHttpStart() {
                handler.sendEmptyMessage(SHOW_LOADING);
            }

            @Override
            public void onHttpFinish(String data) {
                mShipperContactList = Contact.parserXmlList(data);
                handler.sendEmptyMessage(HIDE_LOADING);
                selectShipperContact();
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


    //查找客户
    private void selectShipperContact() {
        mConsignees = Company.findAll(Company.class);
        final ShipperContactAdapter adapter = new ShipperContactAdapter();
        adapter.notifyDataSetChanged();
        View view = View.inflate(mContext, R.layout.customer_list, null);
        ListView lv = (ListView) view.findViewById(R.id.customer_listview);
        lv.setAdapter(adapter);
        EditText etSelectInfo = (EditText) view.findViewById(R.id.et_list_item);
        Builder builder = new Builder(mContext);
        final AlertDialog dialog = builder
                .setTitle("选择客户")
                .setView(view)
                .setNegativeButton("关闭", null)
                .show();
        etSelectInfo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    if (!TextUtils.isEmpty(s.toString())) {
                        List<Company> companies = DataSupport.where("name like \'%" + s.toString() + "%\'").find(Company.class);
                        mConsignees.clear();
                        mConsignees.addAll(companies);
                        adapter.notifyDataSetChanged();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        lv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Company c = mConsignees.get(position);
                mShipperName.setText(c.getName());
                mShipperContact.setText(c.getPeople());
                mShipperAddress.setText(c.getAddress());
                mShipperProvince.setText(c.getProvince());
                mShipperArea.setText(c.getAreaCode());
                mShipperPhone.setText(c.getTel());
                //mConsigneeName.setTag(c.getCustomerId());

                if (mLvShipp != null) {
                    mLvShipp.setVisibility(View.GONE);
                }
                Intent intent = new Intent(NewActivity.this, NewActivityInfoActivity.class);
                intent.putExtra("customerName", c.getName());
                intent.putExtra("tocity", mToNetwork.getText().toString());
                startActivityForResult(intent, CUSTOMINFOINT);
                if (dialog != null)
                    dialog.dismiss();

            }
        });
    }

    //选收件人
    private void selectConsigneeContact() {
        final String toCity = mToNetwork.getText().toString().trim();
        final String shiperName = mShipperName.getText().toString().trim();
        //查询
        if (TextUtils.isEmpty(toCity) || TextUtils.isEmpty(shiperName)) {
            Tos("请输入目的地和公司");
            return;
        }
        if (mConsigneeList == null) {
            mConsigneeList = new ArrayList<>();
        } else {
            mConsigneeList.clear();
        }

       /* List<Recivicer> all = DataSupport.findAll(Recivicer.class, true);
        List<Consignee> all1 = DataSupport.findAll(Consignee.class, true);
        LogU.e(all.toString());
        LogU.e(all1.toString());*/
       /* if (all.size() == 0) {
            Tos("你还没有同步数据请进行同步数据操作");
            startActivity(new Intent(this, SynDataActivity.class));
        }*/

        mConsigneeList.addAll(DataSupport.where("tocity = '" + toCity + "' and shipperName = '" + shiperName + "'").find(Consignee.class));
        final ConsigneeContactAdapter adapter = new ConsigneeContactAdapter();
        adapter.notifyDataSetChanged();
        View view = View.inflate(mContext, R.layout.customer_list, null);
        EditText etSelectInfo = (EditText) view.findViewById(R.id.et_list_item);
        ListView lv = (ListView) view.findViewById(R.id.customer_listview);
        lv.setAdapter(adapter);
        Builder builder = new Builder(mContext);
        final AlertDialog dialog = builder
                .setTitle("选择客户")
                .setView(view)
                .setNegativeButton("关闭", null)
                .show();
        etSelectInfo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s.toString())) {
                    List<Consignee> companies = DataSupport.where("tocity = '" + toCity + "' and shipperName = '" + shiperName + "' and " + "companyName like '%" + s.toString() + "%'").find(Consignee.class);
                    mConsigneeList.clear();
                    mConsigneeList.addAll(companies);
                    adapter.notifyDataSetChanged();
                }
            }
        });

        lv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Consignee c = mConsigneeList.get(position);
                mConsigneeName.setText(c.getCompanyName());
                mConsigneeContact.setText(c.getPerson());
                mConsigneeAddress.setText(c.getAddressDetail());
                mConsigneeProvince.setText(c.getCity());
                mConsigneeArea.setText(c.getAreacode());
                if (!TextUtils.isEmpty(c.getMobileNum())) {
                    mConsigneePhone.setText(c.getMobileNum());
                } else {
                    mConsigneePhone.setText(c.getOtherPhone());
                }
                if (mLvCon != null) {
                    mLvCon.setVisibility(View.GONE);
                } else {

                }
                /*
                Intent intent = new Intent(NewActivity.this, NewActivityInfoActivity.class);
                intent.putExtra("customerName", c.getName());
                intent.putExtra("tocity", mToNetwork.getText().toString());
                startActivityForResult(intent, CUSTOMINFOINT);*/

                if (dialog != null)
                    dialog.dismiss();

            }
        });
    }

    private int shipperlength = 0;
    private int conlength = 0;
    private boolean flagTextChange = true;
    private boolean flagConTextChange = true;

    private void findViewById() {
        btnSelectCustom = (Button) findViewById(R.id.btn_newactivity_selectCustom);
        etCustomNametx = (EditText) findViewById(R.id.et_newactivity_customNametx);
        btnSelectCustom.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                queryCustomer(true);
            }
        });
        mFlip = (ImageView) this.findViewById(R.id.head_flip);
        mTitleRight = (TextView) this.findViewById(R.id.head_woodframe);
        mTitle = (TextView) this.findViewById(R.id.head_title);
        mFlip.setVisibility(View.VISIBLE);
        mFlip.setImageResource(R.drawable.ic_flipper_head_back);
        mTitle.setText("新增运单");

        mShippingMode = (Spinner) this.findViewById(R.id.new_shipping_mode);
        mBalanceMode = (Spinner) this.findViewById(R.id.new_balance_mode);
        mListView = (ListView) this.findViewById(R.id.new_listview);
        mNumber = (EditText) this.findViewById(R.id.new_number);
        mNumberBtn = (Button) this.findViewById(R.id.new_number_btn);
        mClearConsigneeBtn = (Button) this.findViewById(R.id.btn_clear_consignee);
        mClearShipperBtn = (Button) this.findViewById(R.id.btn_clear_shipper);
        mClearConsigneeBtn.setOnClickListener(listener);
        mClearShipperBtn.setOnClickListener(listener);
        mLvShipp = (MyListView) findViewById(R.id.new_ship_lv);
        mLvCon = (MyListView) findViewById(R.id.new_consignee_lv);
        mShipList = new ArrayList<>();
        mConList = new ArrayList<>();
        mShippAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mShipList);
        mConAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mConList);
        mLvShipp.setAdapter(mShippAdapter);
        mLvCon.setAdapter(mConAdapter);
        mLvShipp.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mShippAdapter.getItem(position).startsWith("(客)")) {
                    String item = mShippAdapter.getItem(position);
                    String customerName = item.replace("(客)", "");
                    Custom selectCustom = null;
                    for (Custom custom : mCustoms) {
                        if (custom.getCustomerName().equals(customerName)) {
                            selectCustom = custom;
                        }
                    }
                    if (selectCustom == null) {
                        Toast.makeText(NewActivity.this, "没有所选条目", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    selectCustom(selectCustom);
                    Intent intent = new Intent(NewActivity.this, NewActivityInfoActivity.class);
                    intent.putExtra("customerName", customerName);
                    intent.putExtra("tocity", mToNetwork.getText().toString());
                    startActivityForResult(intent, CUSTOMINFOINT);
                } else {
                    setShipp(mshipCompanies.get(position));
                    flagTextChange = false;
                    mShipperName.setText(mshipCompanies.get(position).getName());
                    flagTextChange = true;

                }

                mLvShipp.setVisibility(View.GONE);
            }
        });
        mLvCon.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setConSignee(mConCompanies.get(position));
                flagConTextChange = false;
                mConsigneeName.setText(mConCompanies.get(position).getName());
                flagConTextChange = true;
                mLvCon.setVisibility(View.GONE);
            }
        });
        mNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                EditText editText = (EditText) v;
                String number = editText.getText().toString();
                if (TextUtils.isEmpty(number)) {
                    return;
                }
                if (!hasFocus) {
                    checkNumIsExit(number);
                }
            }
        });
        mScannerBtn = (Button) this.findViewById(R.id.new_scanner_btn);
        mButtontnDelCustom = (Button) this.findViewById(R.id.btn_delCustom);
        mButtontnDelCustom.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mTvCustom.setText("");
                mShipperName.setEnabled(true);
                mCustom = null;
            }
        });
        mTvCustom = (TextView) findViewById(R.id.tv_custom);

        mShipperName = (EditText) this.findViewById(R.id.new_shipper_name);
        mShipperName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    mLvShipp.setVisibility(View.GONE);
                }
            }
        });
        mShipperName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String str = s.toString();
                if (!TextUtils.isEmpty(str) && flagTextChange) {
                    mshipCompanies = DataSupport.where("name like \'%" + s.toString() + "%\'").find(Company.class);
                    mCustoms = Custom.queryByCustomerName(str);
                    LogU.e(mshipCompanies.toString());
                    if ((mshipCompanies == null || mshipCompanies.size() == 0) && (mCustoms == null || mCustoms.size() == 0)) {
                        mLvShipp.setVisibility(View.GONE);
                    } else if (mshipCompanies.size() >= 1 || (mCustoms != null && mCustoms.size() >= 1)) {
                        mLvShipp.setVisibility(View.VISIBLE);
                        showShippList(mshipCompanies, str);
                    } else if (TextUtils.isEmpty(str)) {
                        mLvShipp.setVisibility(View.GONE);
                    }

                    //设置选择客户的和shipperCode
                    Custom custom = DataSupport.where("customerName = '" + str+"'").findFirst(Custom.class);
                    if (custom != null) {
                        mCustom = custom;
                        mTvCustom.setText(mCustom.getCustomerName());
                    }else {
                        mCustom=null;
                        mTvCustom.setText("");
                    }
                }
                shipperlength = str.length();

            }
        });
        mShipperBtn = (Button) this.findViewById(R.id.new_shipper_btn);
        mShipperContact = (EditText) this.findViewById(R.id.new_shipper_contactname);
        mShipperProvince = (EditText) this.findViewById(R.id.new_shipper_province);
        mShipperAddress = (EditText) this.findViewById(R.id.new_shipper_address);
        mShipperArea = (EditText) this.findViewById(R.id.new_shipper_area);
        mShipperPhone = (EditText) this.findViewById(R.id.new_shipper_phone1);
        mShipperPhone2 = (EditText) this.findViewById(R.id.new_shipper_phone2);

        mFromNetwork = (EditText) this.findViewById(R.id.new_fromNetwork);
        String city = BaseApplication.getApplication().getCity();
        if (!TextUtils.isEmpty(city)) {
            mFromNetwork.setText(city);
        }
        mToNetwork = (EditText) this.findViewById(R.id.new_toNetwork);
        mToNetwork.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String str = s.toString();
                if (!TextUtils.isEmpty(str)) {
                    BaseApplication.getApplication().setToCity(str);
                }
            }
        });
        mPickupBy = (EditText) this.findViewById(R.id.new_pickupBy);
        mDeliveredBy = (EditText) this.findViewById(R.id.new_deliveredBy);

        //收	件人
        mConsigneeName = (EditText) this.findViewById(R.id.new_consignee_name);
        mConsigneeName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    mLvCon.setVisibility(View.GONE);
                }
            }
        });
        mConsigneeName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String str = s.toString();
                if (!TextUtils.isEmpty(str) && flagTextChange) {
                    LogU.e(conlength + "---" + str.length());
                    mConCompanies = DataSupport.where("name like \'%" + s.toString() + "%\'").find(Company.class);
                    LogU.e(mConCompanies.toString());
                    if (mConCompanies == null || mConCompanies.size() == 0) {
                        mLvCon.setVisibility(View.GONE);
                    } else if (mConCompanies.size() >= 1) {
                        mLvCon.setVisibility(View.VISIBLE);
                        showConList(mConCompanies);
                    }
                } else if (TextUtils.isEmpty(str)) {
                    mLvCon.setVisibility(View.GONE);
                }
                conlength = str.length();
            }
        });

        mConsigneeBtn = (Button) this.findViewById(R.id.new_consignee_btn);
        mConsigneeContact = (EditText) this.findViewById(R.id.new_consignee_contactname);
        mConsigneeProvince = (EditText) this.findViewById(R.id.new_consignee_province);
        mConsigneeProvince.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                startActivityForResult(new Intent(NewActivity.this, Demo1.class), 0);
                return false;
            }
        });
        mConsigneeAddress = (EditText) this.findViewById(R.id.new_consignee_address);
        mConsigneeArea = (EditText) this.findViewById(R.id.new_consignee_area);
        mConsigneePhone = (EditText) this.findViewById(R.id.new_consignee_phone);
        mConsigneePhone2 = (EditText) this.findViewById(R.id.new_consignee_phone2);
        mConsigneeContactBtn = (Button) this.findViewById(R.id.new_consignee_contact_btn);

        //产品
        mProductName = (EditText) this.findViewById(R.id.new_product_name);
        mCountWeight = (EditText) this.findViewById(R.id.new_count_weight);
        mConsigneeMobile = (EditText) this.findViewById(R.id.new_consignee_mobile);
        mProductVolumn = (EditText) this.findViewById(R.id.new_product_volumn);
        mProductQuantity = (EditText) this.findViewById(R.id.new_product_quantity);
        mProductWeight = (EditText) this.findViewById(R.id.new_product_weight);

        //保险
        mNeedInsurance = (CheckBox) this.findViewById(R.id.new_needInsurance);
        mInsuranceAmt = (EditText) this.findViewById(R.id.new_insuranceAmt);
        mPremium = (EditText) this.findViewById(R.id.new_premium);

        mTotal = (EditText) this.findViewById(R.id.new_total);

        mRemarks = (EditText) this.findViewById(R.id.new_remarks);
        mMontylyId = (EditText) this.findViewById(R.id.new_monthlyid);
        mMontylyLayout = (LinearLayout) this.findViewById(R.id.new_monthly_layout);
        mNeedDelivery = (CheckBox) this.findViewById(R.id.new_needDelivery);
        mNeedGOFloor = (CheckBox) this.findViewById(R.id.new_needGOFloor);
        mGoodsControl = (CheckBox) this.findViewById(R.id.new_goodsControl);
        mBtnCountWeight = (Button) findViewById(R.id.btn_weight_count);
        mNeedSignUpNotifyMessage = (CheckBox) this.findViewById(R.id.new_needSignUpNotifyMessage);
        mDeliveryCharge = (EditText) this.findViewById(R.id.new_deliveryCharge);
        mSaveBtn = (Button) this.findViewById(R.id.new_save_btn);
        tvPhoneContent = (TextView) findViewById(R.id.tv_phone_content);
        tvPhoneContent2 = (TextView) findViewById(R.id.tv_phone_content2);
        tvPhoneContent.setText(PHONE_CONTENT);
        tvPhoneContent2.setText(PHONE_CONTENT);
        mBtnCountWeight.setOnClickListener(listener);
    }

    private void showShippList(final List<Company> companies, String s) {
        mLvShipp.setVisibility(View.VISIBLE);
        mShipList.clear();
        mShippAdapter.notifyDataSetChanged();
        mCustoms = Custom.queryByCustomerName(s);
        for (int i = 0; i < companies.size(); i++) {
            mShipList.add(companies.get(i).getName());
        }
        for (Custom custom : mCustoms) {
            mShipList.add("(客)" + custom.getCustomerName());
        }
        mShippAdapter.notifyDataSetChanged();
    }

    private void showConList(final List<Company> companies) {
        mLvCon.setVisibility(View.VISIBLE);
        mConList.clear();
        mConAdapter.notifyDataSetChanged();
        for (int i = 0; i < companies.size(); i++) {
            mConList.add(companies.get(i).getName());
        }
        mConAdapter.notifyDataSetChanged();
    }

    private void setConSignee(Company company) {
        mConsigneeContact.setText(company.getPeople());
        mConsigneeProvince.setText(company.getProvince());
        mConsigneeAddress.setText(company.getAddress());
        mConsigneeArea.setText(company.getAreaCode());
        mConsigneePhone.setText(company.getTel());
        mConsigneePhone2.setText(company.getOtherTel());
    }

    private void setShipp(Company company) {
        mShipperContact.setText(company.getPeople());
        mShipperProvince.setText(company.getProvince());
        mShipperAddress.setText(company.getAddress());
        mShipperArea.setText(company.getAreaCode());
        mShipperPhone.setText(company.getTel());
        mShipperPhone2.setText(company.getOtherTel());
    }


    private SimpleDocument user;

    private void setDataDocumentContent(SimpleDocument user) {
        this.user = user;
        mNumber.setText(user.getDocumentNumber());
        mFromNetwork.setText(user.getFromCity());
        mToNetwork.setText(user.getToCity());
        mShipperName.setText(user.getShipperName());
        if (!user.getShipperCode().equals("0")) {
            mTvCustom.setText(user.getShipperName());
            mShipperName.setEnabled(false);
        }
        mShipperContact.setText(user.getShipperContactName());
        mNeedSignUpNotifyMessage.setChecked(user.isNeedSignUpNotifyMessage());
        if (user.getShipperAddress() != null) {
            mShipperProvince.setText(user.getShipperAddress().getProvince() + "," + user.getShipperAddress().getCity() + "," + user.getShipperAddress().getDistrict());
            mShipperAddress.setText(user.getShipperAddress().getAddress());
        }
        try {
            String[] split = user.getShipperPhoneNumber().split(",");
            mShipperPhone.setText(split[0]);
            mShipperPhone2.setText(split[1]);
        } catch (Exception e) {

        }
        mConsigneeName.setText(user.getConsigneeName());
        mConsigneeContact.setText(user.getConsigneeContactPerson());
        if (user.getConsigneeAddress() != null) {
            mConsigneeProvince.setText(user.getConsigneeAddress().getProvince() + "," + user.getConsigneeAddress().getCity() + "," + user.getConsigneeAddress().getDistrict());
            mConsigneeAddress.setText(user.getConsigneeAddress().getAddress());
        }
        try {
            String[] split1 = user.getConsigneePhoneNumber().split(",");
            mConsigneePhone.setText(split1[0]);
            mConsigneePhone2.setText(split1[1]);
        } catch (Exception e) {

        }
        mProductName.setText(user.getProductName());
        mCountWeight.setText(user.getCountWeight());
        mProductQuantity.setText(user.getQuantity());
        mProductWeight.setText(user.getWeight());
        mProductVolumn.setText(user.getVolumn());
        mNeedInsurance.setChecked(user.isNeedInsurance());
        mInsuranceAmt.setText(user.getInsuranceAmt());
        //干线运费
        List<ShipperCost> carriageItems = user.getCarriageItems();
        for (int i = 0; i < shipperCostList.size(); i++) {
            for (int j = 0; j < carriageItems.size(); j++) {
                if (shipperCostList.get(i).getFreightItem().getItemId() == carriageItems.get(j).getFreightItem().getItemId()) {
                    shipperCostList.get(i).setChargeValue(carriageItems.get(j).getChargeValue());
                }
            }
        }
        mCarriageAdapter.notifyDataSetChanged();
        mNeedDelivery.setChecked(user.isNeedDelivery());
        mNeedGOFloor.setChecked(user.isUpstair());
        mGoodsControl.setChecked(user.isDeliveryAfterNotify());
        mDeliveryCharge.setText(user.getDeliveryCharge());
        mShipperArea.setText(user.getShipperAreaCode());
        mConsigneeArea.setText(user.getConsigneeAreaCode());
    }

    private void checkNumIsExit(String num) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("sessionId", BaseApplication.loginInfo.getSessionId());
        params.addBodyParameter("userId", BaseApplication.loginInfo.getUserId() + "");
        params.addBodyParameter("clientName", BaseSettings.CLIENT_NAME);
        params.addBodyParameter("checkNumber", num);
        BaseApplication.mHttpUtils.send(HttpRequest.HttpMethod.POST, Urls.DocumentNumberExist, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                LogU.e(responseInfo.result);
                RetrueCodeHandler parser = RetruenUtils.getReturnInfo(responseInfo.result, new RetrueCodeHandler());
                if (parser != null && parser.getString().equals("0")) {
                    StringXmlPaser stringXmlPaser = new StringXmlPaser();
                    stringXmlPaser.setName("Value");
                    StringXmlPaser returnInfo = RetruenUtils.getReturnInfo(responseInfo.result, stringXmlPaser);
                    assert returnInfo != null;
                    String string = returnInfo.getString();
                    if ("true".equals(string)) {
                        Toast.makeText(getApplicationContext(), "单号已存在", Toast.LENGTH_SHORT).show();
                    }
                } else if (parser != null && parser.getString().equals("-1")) {
                    Toast.makeText(NewActivity.this, parser.getError(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(NewActivity.this, "数据获取错误", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                Toast.makeText(NewActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
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
                queryCustomer(false);
                return;
            }
            if (listCustomers != null)
                listCustomers.clear();
            listCustomers = Custom.queryByCustomerName(textComName);
            if (listCustomers != null)
                setCustomData();
        }
    };
    private String textComName = "";

    private void queryCustomer(Boolean isCheck) {
        if (TextUtils.isEmpty(mToNetwork.getText().toString())) {
            Tos("请先填写目的地");
            return;
        }
        List<Custom> customs = DataSupport.limit(11).find(Custom.class, true);
        if (customs == null || customs.size() == 0) {
            LogU.e("没有数据");
        } else {
            LogU.e("count=" + DataSupport.count(Custom.class));
        }
        //如果没网
        if (listCustomers != null) {
            listCustomers.clear();
        }
        listCustomers = customs;
        setCustomData();
        //如果网络正常就去查看数据是否有更新
      /*  if (NetworkDetector.detect(this) && isCheck)
            checkSynData();*/
    }

    private void checkSynData() {
        RequestParams params = new RequestParams();
        params.addBodyParameter("sessionId", BaseApplication.loginInfo.getSessionId());
        params.addBodyParameter("userId", BaseApplication.loginInfo.getUserId() + "");
        params.addBodyParameter("clientName", BaseSettings.CLIENT_NAME);
        params.addBodyParameter("pageNumber", 0 + "");
        params.addBodyParameter("pageSize", 10 + "");
        params.addBodyParameter("dataId", 1 + "");
        long synTime = (long) SPUtils.get(this, "synTime", (long) 0);
        String lastSynTime = DateFormatUtils.getData(synTime);
        params.addBodyParameter("lastSynchronizedTime", lastSynTime);
        params.addBodyParameter("dataFilter", "1");
        getData(Urls.SynchronizeData, params, new CallBackString() {
            @Override
            public void httFinsh(String data) {
                LogU.e(data);
                SynchronizeDataXmlparser xmlparser = RetruenUtils.getReturnInfo(data, new SynchronizeDataXmlparser());
                if (xmlparser != null) {
                    SynchronizeData synchronizeData = xmlparser.getUser();
                    String modifyData = synchronizeData.getModifyData();
                    List<CustomJson> modifyDataCustomJsons = CustomJson.arrayCustomJsonFromData(modifyData);
                    if (modifyDataCustomJsons != null)
                        Custom.savemodifyDataCustomJsons(modifyDataCustomJsons);
                    String deleteData = synchronizeData.getDeleteData();
                    List<CustomJson> deleteDataCustomJsons = CustomJson.arrayCustomJsonFromData(deleteData);
                    if (deleteDataCustomJsons != null)
                        Custom.deleteDataCustomJsons(deleteDataCustomJsons);
                    SPUtils.put(NewActivity.this, "synTime", System.currentTimeMillis());
                }
            }
        });
    }

    private void setCustomData() {
        ArrayList<String> customNames = new ArrayList<>();
        for (int i = 0; i < listCustomers.size(); i++) {
            customNames.add(listCustomers.get(i).getCustomerName());
        }
        if (customer_list == null) {
            initDialogSelCustom(customNames);
        } else {
            adapterCustom = new ItemSimpleAdapterAdapter<String>(NewActivity.this, customNames);
            lvListItem.setAdapter(adapterCustom);
            adapterCustom.notifyDataSetChanged();
            dialogListItem.show();
        }
    }

    private void initDialogSelCustom(ArrayList<String> customNames) {
        adapterCustom = new ItemSimpleAdapterAdapter<>(NewActivity.this, customNames);
        customer_list = View.inflate(mContext, R.layout.customer_list, null);
        lvListItem = (ListView) customer_list.findViewById(R.id.customer_listview);
        EditText etListItem = (EditText) customer_list.findViewById(R.id.et_list_item);
        lvListItem.setAdapter(adapterCustom);
        etListItem.addTextChangedListener(myedittextListener);
        Builder builder = new Builder(mContext);
        dialogListItem = builder
                .setTitle("选择客户")
                .setView(customer_list)
                .setNegativeButton("关闭", null)
                .show();
        lvListItem.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //mConsigneeContact.setText(listCustomers.get(position).getContactPerson());
                //TODO 选择客户  ss
                selectCustom(position);
                Intent intent = new Intent(NewActivity.this, NewActivityInfoActivity.class);
                intent.putExtra("customerName", listCustomers.get(position).getCustomerName());
                intent.putExtra("tocity", mToNetwork.getText().toString());
                startActivityForResult(intent, CUSTOMINFOINT);
                dialogListItem.dismiss();
            }
        });
    }

    public static final int CUSTOMINFOINT = 4;

    private void selectCustom(int position) {
        try {
            mCustom = listCustomers.get(position);
            LogU.e(mCustom.toString());
            mShipperName.setText(mCustom.getCustomerName());
            mShipperContact.setText(mCustom.getContactPerson());
            mShipperPhone.setText(mCustom.getTel());
            String address = mCustom.getAddress();
            mTvCustom.setText(mCustom.getCustomerName());
            mShipperName.setEnabled(false);
            String[] split = address.split(" ", 4);
            mShipperProvince.setText(split[0] + "," + split[1] + "," + split[2]);
            mShipperAddress.setText(split[3]);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void selectCustom(Custom mCustom) {
        try {
            mShipperName.setText(mCustom.getCustomerName());
            mShipperContact.setText(mCustom.getContactPerson());
            mShipperPhone.setText(mCustom.getTel());
            String address = mCustom.getAddress();
            mTvCustom.setText(mCustom.getCustomerName());
            mShipperName.setEnabled(false);
            String[] split = address.split(" ", 4);
            mShipperProvince.setText(split[0] + "," + split[1] + "," + split[2]);
            mShipperAddress.setText(split[3]);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setListener() {
        //运单生成
        mTitleRight.setOnClickListener(listener);
        mNumberBtn.setOnClickListener(listener);
        mShipperBtn.setOnClickListener(listener);
        mConsigneeBtn.setOnClickListener(listener);
        mShipperProvince.setOnClickListener(listener);
        mConsigneeProvince.setOnClickListener(listener);
        mFlip.setOnClickListener(listener);
        mNeedInsurance.setOnClickListener(listener);
        mSaveBtn.setOnClickListener(listener);
        mConsigneeContactBtn.setOnClickListener(listener);
        mScannerBtn.setOnClickListener(listener);
        mNeedDelivery.setOnClickListener(listener);
        mNeedGOFloor.setOnClickListener(listener);
        //加载第一个收件人
       /* mToNetwork.setOnEditorActionListener(new OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                handler.sendEmptyMessage(UPDATE_TONETWORK);
                return false;
            }
        });*/
       /* mToNetwork.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    handler.sendEmptyMessage(UPDATE_TONETWORK);
                }
            }
        });*/
    }

    private void updateToNetwork() {
        String str = mToNetwork.getText().toString();
        if (StrKit.isBlank(str))
            return;
        String newCity = "";
        List<CityModel> list = BaseApplication.getApplication().getCtiyList();
        for (int i = 0; i < list.size(); i++) {
            CityModel cm = list.get(i);
            if (cm.getAreaCode() != null && cm.getAreaCode().equals(str)) {
                newCity = cm.getName();
                break;
            } else if (cm.getName() != null && cm.getName().startsWith(str)) {
                newCity = cm.getName();
                break;
            }
        }
        if (StrKit.isBlank(newCity)) {
            Toast.makeText(NewActivity.this, "目的地不存在", Toast.LENGTH_SHORT).show();
            mToNetwork.setText("");
        } else {
            mToNetwork.setText(newCity);
            getCustomerReceivers(false);
        }
    }

    private OnClickListener listener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.new_shipper_province:
                    String str = mShipperProvince.getText().toString();
                    if (str.split(",").length != 3) {
                        str = "";
                    }
                    ProvinceDialog dialog = new ProvinceDialog(mContext, str);
                    dialog.setListener(new ProvinceDialog.OnProvinceClick() {
                        @Override
                        public void onData(String province, String areaCode, String code) {
                            mShipperProvince.setText(province);
                            mShipperProvince.setTag(code);
                            mShipperArea.setText(areaCode);
                        }
                    });
                    try {
                        dialog.show();
                    } catch (Exception e) {
                        Toast.makeText(NewActivity.this, "地址解析错误", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.new_consignee_province:
                    ProvinceDialog dialog1 = new ProvinceDialog(mContext, mConsigneeProvince.getText().toString());
                    dialog1.setListener(new ProvinceDialog.OnProvinceClick() {
                        @Override
                        public void onData(String province, String areaCode, String code) {
                            mConsigneeProvince.setText(province);
                            mConsigneeProvince.setTag(code);
                            mConsigneeArea.setText(areaCode);
                        }
                    });
                    dialog1.show();
                    break;
                case R.id.new_shipper_btn:

                    /*if (!NetworkDetector.detect(NewActivity.this)){
                        Tos("当前没有网络，请检查网络");
                        return;
                    }*/
                    //getShipperContact();
                    selectShipperContact();
                    break;
                case R.id.new_number_btn:
                    //getDocumentNumber();
                case R.id.new_consignee_btn:
                    /*if (!NetworkDetector.detect(NewActivity.this)){
                        Tos("当前没有网络，请检查网络");
                        return;
                    }*/
                    //getConsigneeContacts();
                    selectConsigneeContact();
                    break;
                case R.id.head_flip:
                    showDialog(new DialoginOkCallBack() {
                        @Override
                        public void onClickOk(DialogInterface dialog, int which) {
                            NewActivity.this.finish();
                        }
                    }, "退出后信息将清空是否确认退出？");
                    break;
                case R.id.new_needInsurance:
                    isNeedInsurance();
                    break;
                case R.id.new_needDelivery:
                    mDeliveryCharge.setEnabled(mNeedDelivery.isChecked());
                    if (!mNeedDelivery.isChecked()) {
                        mNeedGOFloor.setChecked(false);
                        mDeliveryCharge.setText("");
                    }
                    break;
                case R.id.new_needGOFloor:
                    if (!mNeedDelivery.isChecked()) {
                        //Toast.makeText(NewActivity.this, "请先选中上门服务才可以用户上楼", Toast.LENGTH_SHORT).show();
                        mNeedDelivery.setChecked(true);
                        mDeliveryCharge.setEnabled(true);
                        mNeedGOFloor.setChecked(true);
                        return;
                    }
                    flag = !flag;
                    mNeedGOFloor.setChecked(flag);
                    break;
                case R.id.new_save_btn:
                    try {
                        saveDocument();
                    } catch (Exception e) {
                        LogU.e(e.toString());
                    }

                    break;
                case R.id.new_consignee_contact_btn:
                    getCustomerReceivers(true);
                    break;
                case R.id.head_woodframe:
                    Intent intentmade = new Intent(NewActivity.this, MadeFrameActivity.class);
                    ArrayList<WoodenFrame> woodenFrames = (ArrayList<WoodenFrame>) mDocument.getWoodenFrames();
                    intentmade.putExtra("data", woodenFrames);
                    startActivityForResult(intentmade, MADEFRAGM_REQUEST_CODE);
                    break;
                case R.id.new_scanner_btn:

                    if (Build.VERSION.SDK_INT >= 23) {
                        //检查权限
                        if (ActivityCompat.checkSelfPermission(NewActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            //如果没有权限就去申请  回调在onRequestPermissionsResult方法中
                            ActivityCompat.requestPermissions(NewActivity.this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION);//1是回调码switch中判断
                        } else {
                            scanner();
                        }
                    } else {

                        scanner();
                    }
                    break;
                case R.id.btn_clear_consignee:
                    clearConsignee();
                    break;
                case R.id.btn_clear_shipper:
                    clearshipper();
                    break;
                case R.id.btn_weight_count:
                    if (mCustom == null) {
                        Tos("还没有选择客户");
                        return;
                    }
                    Intent intent = new Intent(NewActivity.this, CountWeightActivity.class);
                    intent.putExtra("customerId", mCustom.getCustomerId());
                    startActivityForResult(intent, COUNTWEIGHT_ACTIVITY);
                    break;
            }
        }
    };

    private void scanner() {
        Intent intent = new Intent();
        intent.setClass(NewActivity.this, ScannerActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(intent, SCANNING_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case CAMERA_PERMISSION:
//是否同意了权限
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//同意
                    scanner();
                } else {
//拒绝
                    Toast.makeText(this, "您拒绝了摄像头的权限,请手动设置", Toast.LENGTH_SHORT).show();
                    showDialog(new DialoginOkCallBack() {
                        @Override
                        public void onClickOk(DialogInterface dialog, int which) {
                            getAppDetailSettingIntent(NewActivity.this);
                        }
                    }, "进入权限->相机权限开通相关权限即可");
                }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void getAppDetailSettingIntent(Context context) {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", getPackageName());
        }
        startActivity(localIntent);
    }

    private void clearshipper() {
        mShipperName.setText("");
        mShipperContact.setText("");
        mShipperAddress.setText("");
        mShipperArea.setText("");
        mShipperPhone.setText("");
        mShipperPhone2.setText("");
    }

    private void clearConsignee() {
        mConsigneeName.setText("");
        mConsigneeAddress.setText("");
        mConsigneeContact.setText("");
        mConsigneeArea.setText("");
        mConsigneePhone.setText("");
        mConsigneePhone2.setText("");
    }

    private boolean flag = false;

    private void showCustomerReceiversDialog() {
        CustomerReceiverAdapter adapter = new CustomerReceiverAdapter();
        adapter.notifyDataSetChanged();
        View view = View.inflate(mContext, R.layout.customer_list, null);
        ListView lv = (ListView) view.findViewById(R.id.customer_listview);
        lv.setAdapter(adapter);
        Builder builder = new Builder(mContext);
        final AlertDialog dialog = builder
                .setTitle("选择收件人")
                .setView(view)
                .setNegativeButton("关闭", null)
                .show();
        lv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                CustomerReceivers c = mCustomerReceiversList.get(position);
                mConsigneeName.setText(c.getReceiverName());
                mConsigneeContact.setText(c.getContactPerson());
                mConsigneeAddress.setText(c.getAddress().getAddress());
                mConsigneeProvince.setText(c.getAddress().toProvince());
                mConsigneeArea.setText(c.getAreaCode());
                mComsigneePhoneSet(c);
                dialog.dismiss();
            }
        });
    }

    private void mComsigneePhoneSet(CustomerReceivers c) {
        String telNumber = c.getTelNumber();
        if (telNumber.contains(PHONE_CONTENT)) {
            String[] split = telNumber.split(PHONE_CONTENT);
            mConsigneePhone.setText(split[0]);
            mConsigneePhone2.setText(split[1]);
        } else {
            mConsigneePhone.setText(c.getTelNumber());
        }
    }

    private void getCustomerReceivers(final boolean isShow) {
        Integer cid = (Integer) mShipperName.getTag();
        if (cid == null || cid < 1) {
            showError("请先选择寄件单位");
            return;
        }

        if (mCustomerReceiversList != null && mCustomerReceiversList.size() > 0) {
            if (mCustomerReceiversList.get(0).getCustomerId() == cid) {
                if (isShow)
                    showCustomerReceiversDialog();
                else {
                    CustomerReceivers c = mCustomerReceiversList.get(0);
                    mConsigneeName.setText(c.getReceiverName());
                    mConsigneeContact.setText(c.getContactPerson());
                    mConsigneeAddress.setText(c.getAddress().getAddress());
                    mConsigneeProvince.setText(c.getAddress().toProvince());
                    mConsigneeArea.setText(c.getAreaCode());
                    mComsigneePhoneSet(c);
                }
                return;
            }
        }

        Map<String, String> map = BaseApplication.getApplication().getSecurityInfo();
        map.put("customerId", String.valueOf(cid));
        map.put("toCity", mToNetwork.getText().toString());
        String templet = TempletUtil.render(BaseSettings.FindCustomerReceiver_TEMPLET, map);
        BaseApplication.getApplication().getHttpPro().postAsync(templet, BaseSettings.FindCustomerReceiver_ACTION, new HttpCallback() {
            @Override
            public void onHttpStart() {
                handler.sendEmptyMessage(SHOW_LOADING);
            }

            @Override
            public void onHttpFinish(String data) {
                if (StrKit.isBlank(data)) {
                    errorFinish("获取数据失败！");
                    return;
                }
                mCustomerReceiversList = CustomerReceivers.parserXmlList(data);
                if (isShow)
                    showCustomerReceiversDialog();
                else {
                    if (mCustomerReceiversList != null && mCustomerReceiversList.size() > 0) {
                        CustomerReceivers c = mCustomerReceiversList.get(0);
                        mConsigneeName.setText(c.getReceiverName());
                        mConsigneeContact.setText(c.getContactPerson());
                        mConsigneeAddress.setText(c.getAddress().getAddress());
                        mConsigneeProvince.setText(c.getAddress().toProvince());
                        mConsigneeArea.setText(c.getAreaCode());
                        mComsigneePhoneSet(c);
                    }
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

    public void openKeybord(EditText mEditText, Context mContext) {
        mEditText.requestFocusFromTouch();
        mEditText.requestFocus();

        InputMethodManager imm = (InputMethodManager) mContext
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(mEditText, InputMethodManager.RESULT_SHOWN);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
                InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    private boolean IsSelectCustom = false;

    //保存运单
    private void saveDocument() {
        String strAddress1 = mShipperProvince.getText().toString();
        String strAddress2 = mConsigneeProvince.getText().toString();
        if (TextUtils.isEmpty(mConsigneeProvince.getText().toString())) {
            strAddress2 = " , , ";
        }
        String[] split1 = strAddress1.split(",");
        String[] split2 = strAddress2.split(",");
        if (split1.length != 3 || split2.length != 3) {
            Toast.makeText(this, "选择城市地址错误，请手动输入", Toast.LENGTH_SHORT).show();
            return;
        }
        final String documentNumber = this.mNumber.getText().toString();
        if (StrKit.isBlank(documentNumber)) {
            openKeybord(mNumber, this);
            showError("运单号不能为空!");
            return;
        }
        String DeliveryCharge = this.mDeliveryCharge.getText().toString();
        if (mNeedDelivery.isChecked() && StrKit.isBlank(DeliveryCharge)) {
            showError("派送费不能为空!");
            openKeybord(mDeliveryCharge, this);
            return;
        }
        mDocument.setNeedDelivery(mNeedDelivery.isChecked());
        mDocument.setUpstair(mNeedGOFloor.isChecked());
        mDocument.setDeliveryAfterNotify(mGoodsControl.isChecked());
        mDocument.setDeliveryCharge(StrKit.isBlank(DeliveryCharge) ? 0 : Double.valueOf(DeliveryCharge));
        mDocument.setNeedSignUpNotifyMessage(this.mNeedSignUpNotifyMessage.isChecked());
        mDocument.setDocumentNumber(documentNumber);

        String formNetwork = this.mFromNetwork.getText().toString();
        if (StrKit.isBlank(formNetwork)) {
            showError("始发地不能为空!");
            openKeybord(mFromNetwork, this);
            return;
        }
        mDocument.setFromCity(formNetwork);
        String toNetwork = this.mToNetwork.getText().toString();
        if (StrKit.isBlank(formNetwork)) {
            showError("目的地不能为空!");
            openKeybord(mToNetwork, this);
            return;
        }
        mDocument.setToCity(toNetwork);
        String pickupby = this.mPickupBy.getText().toString();
        mDocument.setPickupBy(pickupby);
        String deliveredBy = this.mDeliveredBy.getText().toString();
        mDocument.setDeliveredBy(deliveredBy);

        //发件人
        String shipperName = this.mShipperName.getText().toString();
        if (StrKit.isBlank(shipperName)) {
            showError("发件人不能为空!");
            openKeybord(mShipperName, this);
            return;
        }
        mDocument.setShipperName(shipperName);
        Customer shipperC = getCustomer(shipperName);
        if (shipperC != null) {
            mDocument.setShipperAddress(shipperC.getAddress());
            mDocument.setShipperCode(shipperC.getCustomerId());
            mDocument.setShipperAreaCode(shipperC.getTelAreaCode());
            mDocument.setShipperPhoneNumber(shipperC.getTel());
            mDocument.setShipperContactName(shipperC.getContactPerson());
        } else {
            String shipperProvince = this.mShipperProvince.getText().toString();
            if (TextUtils.isEmpty(shipperProvince)) {
                shipperProvince = " , , ";
            }
            String[] provinces = shipperProvince.split(",");
            if (provinces.length <= 2) {
                Toast.makeText(NewActivity.this, "选择城市需要有省市区三级", Toast.LENGTH_SHORT).show();
                return;
            }
            String shipperAddress = this.mShipperAddress.getText().toString();
            String shipperPhone = this.mShipperPhone.getText().toString();
            String shipperPhone2 = this.mShipperPhone2.getText().toString();
            String sipperContact = this.mShipperContact.getText().toString();
            if (provinces.length > 2 || StrKit.notBlank(shipperAddress)
                    || StrKit.isBlank(sipperContact)
                    || StrKit.isBlank(shipperPhone + shipperPhone2)) {
                Address a = new Address();
                a.setAddressId(0);
                a.setProvince(provinces[0]);
                a.setCity(provinces[1]);
                a.setDistrict(provinces[2]);
                a.setAddress(shipperAddress);
                a.setPostCode((String) mShipperProvince.getTag());
                mDocument.setShipperAddress(a);
                mDocument.setShipperCode(0);
                String shipperArea = this.mShipperArea.getText().toString();
                mDocument.setShipperAreaCode(shipperArea);
                if (TextUtils.isEmpty(shipperPhone2)) {
                    mDocument.setShipperPhoneNumber(shipperPhone);
                } else {
                    mDocument.setShipperPhoneNumber(shipperPhone + PHONE_CONTENT + shipperPhone2);
                }
                mDocument.setShipperContactName(sipperContact);
            } else {
                showError("发件人信息填写不完整!");
                return;
            }
        }

        //收件人
        String consigneeName = this.mConsigneeName.getText().toString();
        /*
        if(StrKit.isBlank(consigneeName)){
			showError("收件人不能为空!");
			return;
		}*/
        mDocument.setConsigneeName(consigneeName);
        Customer consigneeC = getCustomer(consigneeName);
        if (consigneeC != null) {
            mDocument.setConsigneeAddress(consigneeC.getAddress());
            mDocument.setConsigneeCode(consigneeC.getCustomerId());
            mDocument.setConsigneeAreaCode(consigneeC.getTelAreaCode());
            mDocument.setConsigneePhoneNumber(consigneeC.getTel());
            mDocument.setConsigneeContactPerson(consigneeC.getContactPerson());
        } else {
            String consigneeProvince = this.mConsigneeProvince.getText().toString();
            if (TextUtils.isEmpty(consigneeProvince)) {
                consigneeProvince = " , , ";
            }
            String[] provinces = consigneeProvince.split(",");
            String consigneeAddress = this.mConsigneeAddress.getText().toString();
            String consigneeContactName = this.mConsigneeContact.getText().toString();
            String consigneePhone = this.mConsigneePhone.getText().toString();
            String consigneePhone2 = this.mConsigneePhone2.getText().toString();
            String consigneeMobile = this.mConsigneeMobile.getText().toString();
            if (provinces.length > 2 || StrKit.notBlank(consigneeAddress)
                    || StrKit.isBlank(consigneeContactName)
                    || StrKit.isBlank(consigneePhone)) {
                Address a = new Address();
                a.setAddressId(0);
                a.setProvince(provinces[0]);
                a.setCity(provinces[1]);
                a.setDistrict(provinces[2]);
                a.setAddress(consigneeAddress);
                a.setPostCode((String) mConsigneeProvince.getTag());
                mDocument.setConsigneeAddress(a);
                mDocument.setConsigneeCode(0);
                String consigneeArea = this.mConsigneeArea.getText().toString();
                mDocument.setConsigneeAreaCode(consigneeArea);
                if (TextUtils.isEmpty(consigneePhone2)) {
                    mDocument.setConsigneePhoneNumber(consigneeMobile + ";" + consigneePhone);
                } else {
                    mDocument.setConsigneePhoneNumber(consigneeMobile + ";" + consigneePhone + PHONE_CONTENT + consigneePhone2);
                }
                mDocument.setConsigneeContactPerson(consigneeContactName);
            } else {
                showError("收件人信息填写不完整!");
                return;
            }
        }

        ShippingMode shippingMode = mShippingModeList.get(mShippingModeId);
        mDocument.setShippingMode(shippingMode);
        String productName = this.mProductName.getText().toString();
        String countWeight = this.mCountWeight.getText().toString();
        /*if(StrKit.isBlank(productName)){
            showError("产品名称不能为空!");
			return;
		}*/
        mDocument.setProductName(productName);
        mDocument.setCountWeight(countWeight);
        String volumn = this.mProductVolumn.getText().toString();
        if (StrKit.isBlank(volumn)) {
            showError("体积不能为空!");
            openKeybord(mProductVolumn, this);
            return;
        }
        mDocument.setVolumn(Double.valueOf(volumn));
        String quantity = this.mProductQuantity.getText().toString();
        if (StrKit.isBlank(quantity)) {
            showError("件数不能为空!");
            openKeybord(mProductQuantity, this);
            return;
        }
        mDocument.setQuantity(Integer.valueOf(quantity));
        String weight = this.mProductWeight.getText().toString();
        if (StrKit.isBlank(weight)) {
            showError("重量不能为空!");
            openKeybord(mProductWeight, this);
            return;
        }
        mDocument.setWeight(Double.valueOf(weight));
        boolean needInsurance = this.mNeedInsurance.isChecked();
        mDocument.setNeedInsurance(needInsurance);
        String insuranceAmt = this.mInsuranceAmt.getText().toString();
        if (needInsurance && StrKit.isBlank(insuranceAmt)) {
            showError("声明价值不能为空!");
            openKeybord(mInsuranceAmt, this);
            return;
        }
        if (StrKit.notBlank(insuranceAmt))
            mDocument.setInsuranceAmt(Integer.valueOf(insuranceAmt));
        List<ShipperCost> shipperCostList = this.shipperCostList;
        mDocument.setCarriageItems(shipperCostList);
        String balanceMode = mBalanceModeValues[mBalanceModeId];
        mDocument.setBalanceMode(balanceMode);
        String monthlyId = this.mMontylyId.getText().toString();
        if (StrKit.notBlank(monthlyId))
            mDocument.setMonthlyBalanceAccount(Integer.valueOf(monthlyId));
        String remarks = this.mRemarks.getText().toString();
        mDocument.setRemarks(remarks);
        if (mCustom != null)
            mDocument.setShipperCode(Integer.valueOf(mCustom.getCustomerId()));
        if (WoodenFrames != null) {
            mDocument.setWoodenFrames(WoodenFrames);
        }
        if (IsSelectCustom) {
            mDocument.setShipperCode(Integer.valueOf(BaseApplication.customReceivers.getReceiverId()));
        }
        if (TextUtils.isEmpty(mToNetwork.getText().toString().trim())) {
            mDocument.setToCity("");
        } else {
            mDocument.setToCity(mToNetwork.getText().toString().trim());
        }
        XStream xstream = new XStream();
        xstream.setMode(XStream.NO_REFERENCES);
        xstream.alias("document", Document.class);
        xstream.alias("ShipperCost", ShipperCost.class);
        xstream.alias("WoodenFrame", WoodenFrame.class);
        xstream.alias("FreightItem", FreightItem.class);
        String xml = xstream.toXML(mDocument);

        Map<String, String> map = BaseApplication.getApplication().getSecurityInfo();
        map.put("document", xml);
        final String templet = TempletUtil.render(BaseSettings.SAVEDOCUMENT_TEMPLET, map);
        //templet.toString().replace("NeedDelivery", " <Upstair>true</Upstair>\n");
        //saveDocuments();

        BaseApplication.getApplication().getHttpPro().postAsync(templet, BaseSettings.SAVEDOCUMENT_ACTION, new HttpCallback() {

            @Override
            public void onHttpStart() {
                handler.sendEmptyMessage(SHOW_LOADING);
            }

            @Override
            public void onHttpFinish(String data) {
                if (StrKit.isBlank(data)) {
                    Toast.makeText(NewActivity.this, "数据获取失败请检查网络", Toast.LENGTH_SHORT).show();
                    return;
                }
                XmlParserUtil parser = XmlParserUtil.getInstance();
                parser.parse(data);
                String prefix = "/soap:Envelope/soap:Body/SaveDocumentResponse/SaveDocumentResult/";
                String code = parser.getNodeValue(prefix + "ReturnCode");
                if ("-1001".equals(code)) {
                    showReLoginDialog();
                } else if (StrKit.isBlank(code) || !"0".equals(code)) {
                    try {
                        String error = parser.getNodeValue(prefix + "Error");
                        showError(error);
                    } catch (Exception e) {

                    }
                } else {
                    //成功提交
                    if (user != null) {
                        SqlUtils.ErrorDocumentDel(mDocument.getDocumentNumber());
                    }
                    //TODO 成功后保存公司信息
                    saveCompany();
                    //保存发件人
                    saveShipPerson();
                    //保存收件人
                    saveDocumentInfo();
                    //TODO 保存 目的地 公司名 收件人信息（地址等） 运输方式  品名  建立数据表进行关联
                    saveCustomOtherInfos();
                    mDocument.parserEditXml(data);
                    BaseApplication.getApplication().setDocument(mDocument);
                    Intent intent = new Intent(NewActivity.this, ApplyDocumentDetailActivity.class);

                    NewActivity.this.startActivityForResult(intent, 0);

                    NewActivity.this.finish();
                }
                handler.sendEmptyMessage(HIDE_LOADING);
            }

            @Override
            public void onHttpError(String msg) {
                showError(msg);
                ErrorDocument errorDocument = new ErrorDocument();
                errorDocument.setDocumentNumber(mDocument.getDocumentNumber());
                errorDocument.setDocumentcontent(templet);
                try {
                    List<ErrorDocument> errorDocuments = BaseApplication.mdbUtils.findAll(ErrorDocument.class);
                    if (errorDocuments == null) {
                        errorDocuments = new ArrayList<ErrorDocument>();
                    } else {
                        int size = errorDocuments.size();
                        for (int i = 0; i < size; i++) {
                            if (mDocument.getDocumentNumber().equals(errorDocuments.get(i).getDocumentNumber())) {
                                errorDocument.setId(errorDocuments.get(i).getId());
                                BaseApplication.mdbUtils.update(errorDocument, "documentcontent");
                                isUpdate = true;
                            }
                        }
                    }
                    if (!isUpdate) {
                        BaseApplication.mdbUtils.save(errorDocument);
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showDialog(new DialoginOkCallBack() {
                                @Override
                                public void onClickOk(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(NewActivity.this, FreightActivity.class);
                                    intent.putExtra("strDocuments", mDocument.getDocumentNumber());
                                    startActivity(intent);
                                }
                            }, "保存失败是否继续打印？");
                        }
                    });

                } catch (DbException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Tos("本地保存失败");
                        }
                    });
                    e.printStackTrace();

                }

                handler.sendEmptyMessage(HIDE_LOADING);
            }

            @Override
            public void onHttpEnd() {

            }
        });
        /*
        if(StrKit.isBlank(documentNumber)){
			msg = "运单不能为空!";
		}else if(StrKit.isBlank(formNetwork)){
			msg = "原寄地不能为空!";
		}else if(getNetwork(formNetwork) == null){
			msg = "原寄地不存在!";
		}*/
        //mDocument.setShipperName(mShipperName.getText().toString());
        /*mDocument
        private Button mShipperBtn;
		private EditText mShipperContact;
		private EditText mShipperProvince;
		private EditText mShipperAddress;
		private EditText mShipperArea;
		private EditText mShipperPhone;*/

    }

    private void saveShipPerson() {
        ShipPerson shipPerson = new ShipPerson();
        shipPerson.setName(mShipperContact.getText().toString());
        shipPerson.setCity(mShipperProvince.getText().toString());
        shipPerson.setAddressDetail(mShipperAddress.getText().toString());
        shipPerson.setAreaCode(mShipperArea.getText().toString());
        shipPerson.setMobile(mShipperPhone.getText().toString());
        shipPerson.setOtherPhone(mShipperPhone2.getText().toString());
        ShipPerson.saveIfNotExit(shipPerson);
    }

    private void saveCustomOtherInfos() {
        CustomOtherInfos customOtherInfos = new CustomOtherInfos();
        customOtherInfos.setTocity(mToNetwork.getText().toString());
        customOtherInfos.setCustomName(mShipperName.getText().toString());
        customOtherInfos.setConsigneeName(mConsigneeName.getText().toString());
        customOtherInfos.setShipMode(mDocument.getShippingMode().getModeName());
        customOtherInfos.setGoodsName(mProductName.getText().toString());
        customOtherInfos.setCountWeight(mCountWeight.getText().toString());
        customOtherInfos.setShipName(mShipperContact.getText().toString());
        customOtherInfos.setInsurance(mNeedInsurance.isChecked());
        CustomOtherInfos.saveIfNotExit(customOtherInfos);
    }

    //保存收件人信息
    private void saveDocumentInfo() {
        try {
            Consignee consignee = new Consignee();
            consignee.setCompanyName(mConsigneeName.getText().toString());
            consignee.setPerson(mConsigneeContact.getText().toString());
            consignee.setCity(mConsigneeProvince.getText().toString());
            consignee.setAddressDetail(mConsigneeAddress.getText().toString());
            consignee.setAreacode(mConsigneeArea.getText().toString());
            consignee.setMobileNum(mConsigneePhone.getText().toString());
            consignee.setOtherPhone(mConsigneePhone2.getText().toString());
            consignee.setIsUpdoor(mNeedDelivery.isChecked());
            consignee.setIsUpfloor(mNeedGOFloor.isChecked());
            consignee.setTocity(mToNetwork.getText().toString().trim());
            consignee.setShipperName(mShipperName.getText().toString().trim());
            Consignee.saveIfNotExit(consignee);
            List<Consignee> all = Consignee.findAll(Consignee.class);
            LogU.e("收件人：" + all.toString());
        } catch (Exception e) {

        }
    }

    //保存收件人发件人
    private void saveCompany() {
        try {
            Company companyShippment = new Company();
            companyShippment.setName(mShipperName.getText().toString());
            companyShippment.setPeople(mShipperContact.getText().toString());
            companyShippment.setProvince(mShipperProvince.getText().toString());
            companyShippment.setAddress(mShipperAddress.getText().toString());
            companyShippment.setAreaCode(mShipperArea.getText().toString());
            companyShippment.setTel(mShipperPhone.getText().toString());
            companyShippment.setOtherTel(mShipperPhone2.getText().toString());
            Company.save(companyShippment);
           /* Company companyRecivicer = new Company();
            companyRecivicer.setName(mConsigneeName.getText().toString());
            companyRecivicer.setPeople(mConsigneeContact.getText().toString());
            companyRecivicer.setProvince(mConsigneeProvince.getText().toString());
            companyRecivicer.setAddress(mConsigneeAddress.getText().toString());
            companyRecivicer.setAreaCode(mConsigneeArea.getText().toString());
            companyRecivicer.setTel(mConsigneePhone.getText().toString());
            companyRecivicer.setOtherTel(mConsigneePhone2.getText().toString());
            Company.save(companyRecivicer);
            List<Company> all = DataSupport.findAll(Company.class);
            LogU.e(all.toString());*/
        } catch (Exception e) {

        }

    }

    private boolean isUpdate = false;
    public static final String STATICACTION = "cn.xsjky.android.restartReceiver";

    private void showReLoginDialog() {
        showDialog(new DialoginOkCallBack() {
            @Override
            public void onClickOk(DialogInterface dialog, int which) {
                sendBroadcast();
            }
        }, "服务器正在重启是否重新登录");
    }

    private void sendBroadcast() {
        Intent intent = new Intent();
        intent.setAction(STATICACTION);        //设置Action
        sendBroadcast(intent);
    }

    private Customer getCustomer(String name) {
        if (mCustomerList == null || mCustomerList.size() < 1)
            return null;
        for (int i = 0; i < mCustomerList.size(); i++)
            if (mCustomerList.get(i).getCustomerName().equals(name))
                return mCustomerList.get(i);

        return null;
    }

    private ShippingMode getShippingMode(int id) {
        if (mShippingModeList == null || mShippingModeList.size() < 1)
            return null;
        for (int i = 0; i < mShippingModeList.size(); i++)
            if (mShippingModeList.get(i).getModeId() == id)
                return mShippingModeList.get(i);

        return null;
    }

    private Network getNetwork(String code) {
        for (int i = 0; i < mNetworkList.size(); i++) {
            if (mNetworkList.get(i).getNetworkCode().equals(code))
                return mNetworkList.get(i);
        }
        return null;
    }

    private void isNeedInsurance() {
        mInsuranceAmt.setEnabled(mNeedInsurance.isChecked());
    }

    private void getDocumentNumber() {
        Map<String, String> map = BaseApplication.getApplication().getSecurityInfo();
        Network n = mDocument.getFromNetwork();
        map.put("fromNetwork.networkId", String.valueOf(n.getNetworkId()));
        map.put("fromNetwork.networkCode", n.getNetworkCode());
        map.put("fromNetwork.networkName", n.getNetworkName());
        map.put("fromNetwork.parentId", String.valueOf(n.getParentId()));
        map.put("fromNetwork.remarks", n.getRemarks());
        map.put("fromNetwork.managerId", String.valueOf(n.getManagerId()));
        map.put("fromNetwork.isDisabled", String.valueOf(n.isIsDisabled()));
        map.put("fromNetwork.tel", n.getTel());
        map.put("fromNetwork.fax", n.getFax());
        map.put("fromNetwork.pinYinCode", n.getPinYinCode());
        map.put("fromNetwork.wuBiCode", n.getWuBiCode());
        String templet = TempletUtil.render(BaseSettings.GETDOCUMENTNUMBER_TEMPLET, map);
        BaseApplication.getApplication().getHttpPro().postAsync(templet, BaseSettings.GETDOCUMENTNUMBER_ACTION, new HttpCallback() {
            @Override
            public void onHttpStart() {
                handler.sendEmptyMessage(SHOW_LOADING);
            }

            @Override
            public void onHttpFinish(String data) {
                if (StrKit.isBlank(data)) {
                    errorFinish("获取数据失败！");
                    return;
                }
                XmlParserUtil parser = XmlParserUtil.getInstance();
                parser.parse(data);
                String value = parser.getNodeValue("/soap:Envelope/soap:Body/GetDocumentNumberResponse/GetDocumentNumberResult/Value");
                mNumber.setText(value);
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

    private void init() {
        if (hasInitCache()) return;
        //没有缓存则去加载
        String templet = TempletUtil.render(BaseSettings.NEWDOCUMEN_TEMPLET, BaseApplication.getApplication().getSecurityInfo());
        BaseApplication.getApplication().getHttpPro().postAsync(templet, BaseSettings.NEWDOCUMENT_ACTION, new HttpCallback() {
            @Override
            public void onHttpStart() {
            }

            @Override
            public void onHttpFinish(String data) {
                mDocument = new Document();
                mDocument.parserXML(data);
                if (mDocument == null) {
                    errorFinish("获取数据失败！");
                    return;
                }
                saveDocumentInitCache(data);
                try {
                    initDocumentData();
                } catch (Exception e) {

                }
                if (!isEdit && !TextUtils.isEmpty(name)) {
                    String[] addresss = address.split(" ", 4);
                    mShipperContact.setText(name);
                    mShipperPhone.setText(phone);
                    mShipperAddress.setText(addresss[3]);
                    mShipperProvince.setText(addresss[0] + "," + addresss[1] + "," + addresss[2]);
                }
            }

            @Override
            public void onHttpError(String msg) {
                errorFinish(msg);
            }

            @Override
            public void onHttpEnd() {
            }
        });
    }

    private boolean hasInitCache() {
        //如果有缓存
        try {
            NewDocumentDataCache newDocumentDataCache = BaseApplication.mdbUtils.findById(NewDocumentDataCache.class, 1);
            if (newDocumentDataCache != null) {
                if (TextUtils.isEmpty(newDocumentDataCache.getDocumentInit())) {
                    return false;
                }
                String data = newDocumentDataCache.getDocumentInit();
                mDocument = new Document();
                mDocument.parserXML(data);
                if (mDocument == null) {
                    errorFinish("获取数据失败！");
                    return true;
                }
                try {
                    initDocumentData();
                } catch (Exception e) {

                }
                if (!isEdit && !TextUtils.isEmpty(name)) {
                    String[] addresss = address.split(" ", 4);
                    mShipperContact.setText(name);
                    mShipperPhone.setText(phone);
                    mShipperAddress.setText(addresss[3]);
                    mShipperProvince.setText(addresss[0] + "," + addresss[1] + "," + addresss[2]);
                }
                return true;
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void saveDocumentInitCache(String data) {

        try {
            NewDocumentDataCache newDocumentDataCache = BaseApplication.mdbUtils.findById(NewDocumentDataCache.class, 1);
            if (newDocumentDataCache == null) {
                newDocumentDataCache = new NewDocumentDataCache();
                newDocumentDataCache.setDocumentInit(data);
                BaseApplication.mdbUtils.save(newDocumentDataCache);
            } else {
                newDocumentDataCache.setDocumentInit(data);
                BaseApplication.mdbUtils.update(newDocumentDataCache, "documentInit");
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    //初始化网点
    private void initNetwork() {
        String templet = TempletUtil.render(BaseSettings.GETNETWORKS_TEMPLET, BaseApplication.getApplication().getSecurityInfo());
        BaseApplication.getApplication().getHttpPro().postAsync(templet, BaseSettings.GETNETWORKS_ACTION, new HttpCallback() {

            @Override
            public void onHttpStart() {

            }

            @Override
            public void onHttpFinish(String data) {
                mNetworkList = Network.parserXmlList(data);
                if (mNetworkList == null || mNetworkList.size() < 1) {
                    errorFinish("获取网点数据失败！");
                    return;
                }
            }

            @Override
            public void onHttpError(String msg) {

            }

            @Override
            public void onHttpEnd() {
            }
        });
    }

    //初始化表单数据
    private void initDocumentData() {

        /*
        if(mDocument.getFromNetwork() != null)
			mFromNetwork.setText(mDocument.getFromNetwork().getNetworkCode());
		*/
        if (mDocument.isNeedInsurance()) {
            //this.mNeedInsurance.setChecked(true);
            this.mInsuranceAmt.setEnabled(true);
        }
        //this.mNeedDelivery.setChecked(mDocument.isNeedDelivery());
        if (mDocument.isNeedDelivery()) {
            this.mDeliveryCharge.setEnabled(true);
            //this.mDeliveryCharge.setText(String.valueOf(mDocument.getDeliveryCharge()));
        }
        this.mNeedSignUpNotifyMessage.setChecked(mDocument.isNeedSignUpNotifyMessage());
        ShippingMode shippingMode = mDocument.getShippingMode();
        String modeName = shippingMode.getModeName();
        if (isEdit) {
            if (!TextUtils.isEmpty(modeName)) {
                if (modeName.equals("卡航")) {
                    spIndex = 0;
                } else if (modeName.equals("航空")) {
                    spIndex = 1;
                } else if (modeName.equals("陆运")) {
                    spIndex = 2;
                } else if (modeName.equals("航空-36H")) {
                    spIndex = 3;
                }
            }
        }
        if (isEdit) {
            mProductQuantity.setText(mDocument.getQuantity() + "");
            mProductWeight.setText(mDocument.getWeight() + "");
            mProductVolumn.setText(mDocument.getVolumn() + "");
            mInsuranceAmt.setText(mDocument.getInsuranceAmt() + "");
            mDeliveryCharge.setText(mDocument.getDeliveryCharge() + "");
            mNeedGOFloor.setChecked(mDocument.isUpstair());
            mNeedDelivery.setChecked(mDocument.isNeedDelivery());
            mGoodsControl.setChecked(mDocument.isDeliveryAfterNotify());
            this.mFromNetwork.setText(mDocument.getFromCity());
            this.mToNetwork.setText(mDocument.getToCity());
        }
        //运费项目
        initShipperCost();
        shipperCostList = mDocument.getCarriageItems();
        if (shipperCostList == null || shipperCostList.size() < 1) {
            mListView.setVisibility(View.GONE);
        } else {
            mCarriageAdapter = new CarriageAdapter();
            mListView.setAdapter(mCarriageAdapter);
            setListViewHeightBasedOnChildren(mListView);
            mListView.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    final ShipperCost sc = shipperCostList.get(position);
                    if (!sc.getFreightItem().isAllowEdit())
                        return;
                    final EditText et = new EditText(mContext);
                    et.setInputType(InputType.TYPE_CLASS_NUMBER);
                    et.requestFocus(); //edittext是一个EditText控件
                    Timer timer = new Timer(); //设置定时器
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() { //弹出软键盘的代码
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.showSoftInput(et, InputMethodManager.RESULT_SHOWN);
                            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                        }
                    }, 300);
                    if (sc.getChargeValue() > 0)
                        et.setText(String.valueOf(sc.getChargeValue()));
                    new Builder(mContext).setTitle(sc.getFreightItem().getItemName())
                            .setView(et)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    String input = et.getText().toString();
                                    if (input.equals("")) {
                                        sc.setChargeValue(0);
                                    } else
                                        sc.setChargeValue(Double.valueOf(input));
                                    mCarriageAdapter.notifyDataSetChanged();
                                    mProductQuantity.setFocusable(true);
                                    mProductQuantity.requestFocus();
                                }
                            })
                            .setNegativeButton("取消", null)
                            .show();
                }
            });
        }


        //mBalanceModeId = mDocument.getShippingMode().getModeId();
        if (user == null) {
            this.mNumber.setText(mDocument.getDocumentNumber());
        }
        this.mShipperName.setText(mDocument.getShipperName());
        if (mLvShipp != null) {
            mLvShipp.setVisibility(View.GONE);
        }
        this.mShipperName.setTag(mDocument.getShipperCode());
        this.mShipperContact.setText(mDocument.getShipperContactName());
        String shipperStr = "";
        if (StrKit.notBlank(mDocument.getShipperAddress().getProvince()))
            shipperStr += mDocument.getShipperAddress().getProvince() + ",";
        if (StrKit.notBlank(mDocument.getShipperAddress().getCity()))
            shipperStr += mDocument.getShipperAddress().getCity() + ",";
        if (StrKit.notBlank(mDocument.getShipperAddress().getDistrict()))
            shipperStr += mDocument.getShipperAddress().getDistrict();
        this.mShipperProvince.setText(shipperStr);
        this.mShipperAddress.setText(mDocument.getShipperAddress().getAddress());
        this.mShipperArea.setText(mDocument.getShipperAreaCode());
        try {
            String shipperPhoneNumber = mDocument.getShipperPhoneNumber();
            if (shipperPhoneNumber.contains(PHONE_CONTENT)) {
                String[] split = shipperPhoneNumber.split(PHONE_CONTENT);
                mShipperPhone.setText(split[0]);
                mShipperPhone2.setText(split[1]);
            } else {
                this.mShipperPhone.setText(shipperPhoneNumber);
            }
        } catch (Exception e) {
            e.toString();
        }
        this.mConsigneeName.setText(mDocument.getConsigneeName());
        this.mConsigneeContact.setText(mDocument.getConsigneeContactPerson());
        String consigneStr = "";
        if (StrKit.notBlank(mDocument.getConsigneeAddress().getProvince()))
            consigneStr += mDocument.getConsigneeAddress().getProvince() + ",";
        if (StrKit.notBlank(mDocument.getConsigneeAddress().getCity()))
            consigneStr += mDocument.getConsigneeAddress().getCity() + ",";
        if (StrKit.notBlank(mDocument.getConsigneeAddress().getDistrict()))
            consigneStr += mDocument.getConsigneeAddress().getDistrict();
        this.mConsigneeProvince.setText(consigneStr);
        this.mConsigneeAddress.setText(mDocument.getConsigneeAddress().getAddress());
        this.mConsigneeArea.setText(mDocument.getConsigneeAreaCode());
        String consigneePhoneNumber = mDocument.getConsigneePhoneNumber();
        try {
            if (consigneePhoneNumber.contains(PHONE_CONTENT)) {
                String[] split = consigneePhoneNumber.split(PHONE_CONTENT);
                mConsigneePhone.setText(split[0]);
                mConsigneePhone2.setText(split[1]);
            } else {
                this.mConsigneePhone.setText(consigneePhoneNumber);
            }
        } catch (Exception e) {
            e.toString();
        }

        //运输方式
        for (int i = 0; i < mBalanceModeValues.length; i++) {
            if (mBalanceModeValues[i].equals(mDocument.getShippingMode().getModeName())) {
                this.mBalanceModeId = i;
                this.mShippingMode.setSelection(i);
                break;
            }
        }
        if (user == null)
            this.mProductName.setText(mDocument.getProductName());
        this.mCountWeight.setText(mDocument.getCountWeight());
        if (mDocument.isNeedInsurance()) {
            // TODO  保险默认不要设置打钩
            // this.mNeedInsurance.setChecked(true);
        }
        //this.mInsuranceAmt.setText(String.valueOf(mDocument.getInsuranceAmt()));
        double premium = mDocument.getPremium();
        if (premium > 0) {
            this.mPremium.setText(String.valueOf(mDocument.getPremium()));
        }
        //网络原因产生的异常订单处理

        strDocuments = NewActivity.this.getIntent().getStringExtra("strDocuments");
        if (!TextUtils.isEmpty(strDocuments)) {

            try {
                List<ErrorDocument> errorDocuments = BaseApplication.mdbUtils.findAll(ErrorDocument.class);
                if (errorDocuments == null) {
                    return;
                }
                for (int i = 0; i < errorDocuments.size(); i++) {
                    if (strDocuments.equals(errorDocuments.get(i).getDocumentNumber())) {
                        SimpleDocumentXmlparser xmlparser = RetruenUtils.getReturnInfo(errorDocuments.get(i).getDocumentcontent(), new SimpleDocumentXmlparser());
                        if (xmlparser != null) {
                            SimpleDocument user = xmlparser.getUser();
                            setDataDocumentContent(user);
                        } else
                            Tos("解析失败");
                    }
                }
            } catch (DbException e) {
                e.printStackTrace();
            }
        }
        //运输方式
        initShippingMode();
        //付款方式
        initBalanceMode();
        if (!isEdit)
            return;
        /*double carriage = mDocument.getCarriage();
        this.mCarriage.setText(String.valueOf(carriage));
        this.mTotal.setText(String.valueOf(mDocument.getTotalCharge()));
        this.mProductQuantity.setText(String.valueOf(mDocument.getQuantity()));
        this.mProductVolumn.setText(String.valueOf(mDocument.getVolumn()));
        this.mProductWeight.setText(String.valueOf(mDocument.getWeight()));
        */
    }

    private void initShippingMode() {

        if (hasShippingModeCache()) return;

        String templet = TempletUtil.render(BaseSettings.GETSHIPPINGMODES_TEMPLET, BaseApplication.getApplication().getSecurityInfo());
        BaseApplication.getApplication().getHttpPro().postAsync(templet, BaseSettings.GETSHIPPINGMODES_ACTION, new HttpCallback() {

            @Override
            public void onHttpStart() {
                handler.sendEmptyMessage(SHOW_LOADING);
            }

            @Override
            public void onHttpFinish(String data) {
                mShippingModeList = ShippingMode.parserXmlList(data);
                if (mShippingModeList == null || mShippingModeList.size() < 1) {
                    errorFinish("获取计费数据失败！");
                    return;
                }
                saveShippingModeCache(data);
                String str = "";
                for (int i = 0; i < mShippingModeList.size(); i++) {
                    str = str + mShippingModeList.get(i).getModeName() + "#";
                }
                mShippingModeStrs = str.split("#");
                mShippingModeAdapter = new ArrayAdapter<String>(NewActivity.this, android.R.layout.simple_spinner_item, mShippingModeStrs);
                mShippingModeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mShippingMode.setAdapter(mShippingModeAdapter);
                mShippingMode.setOnItemSelectedListener(sippingModeListener);
                mShippingMode.setVisibility(View.VISIBLE);
                if (user != null) {
                    String modeName = user.getShippingMode().getModeName();
                    for (int i = 0; i < mShippingModeStrs.length; i++) {
                        if (modeName.equals(mShippingModeStrs[i])) {
                            spIndex = i;
                        }
                    }
                }
                mShippingMode.setSelection(spIndex);
                handler.sendEmptyMessage(HIDE_LOADING);
            }

            @Override
            public void onHttpError(String msg) {

            }

            @Override
            public void onHttpEnd() {
            }
        });
    }

    private boolean hasShippingModeCache() {
        try {
            NewDocumentDataCache newDocumentDataCache = BaseApplication.mdbUtils.findById(NewDocumentDataCache.class, 1);
            if (TextUtils.isEmpty(newDocumentDataCache.getInitShippingModeCache())) {
                return false;
            }
            if (newDocumentDataCache != null && !TextUtils.isEmpty(newDocumentDataCache.getInitShippingModeCache())) {
                String data = newDocumentDataCache.getInitShippingModeCache();
                mShippingModeList = ShippingMode.parserXmlList(data);
                if (mShippingModeList == null || mShippingModeList.size() < 1) {
                    errorFinish("获取计费数据失败！");
                    return true;
                }
                String str = "";
                for (int i = 0; i < mShippingModeList.size(); i++) {
                    str = str + mShippingModeList.get(i).getModeName() + "#";
                }
                mShippingModeStrs = str.split("#");
                mShippingModeAdapter = new ArrayAdapter<String>(NewActivity.this, android.R.layout.simple_spinner_item, mShippingModeStrs);
                mShippingModeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mShippingMode.setAdapter(mShippingModeAdapter);
                mShippingMode.setOnItemSelectedListener(sippingModeListener);
                mShippingMode.setVisibility(View.VISIBLE);
                if (user != null) {
                    String modeName = user.getShippingMode().getModeName();
                    for (int i = 0; i < mShippingModeStrs.length; i++) {
                        if (modeName.equals(mShippingModeStrs[i])) {
                            spIndex = i;
                        }
                    }
                }
                mShippingMode.setSelection(spIndex);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void saveShippingModeCache(String data) {
        try {
            NewDocumentDataCache newDocumentDataCache = BaseApplication.mdbUtils.findById(NewDocumentDataCache.class, 1);
            if (newDocumentDataCache == null) {
                newDocumentDataCache = new NewDocumentDataCache();
                newDocumentDataCache.setInitShippingModeCache(data);
                BaseApplication.mdbUtils.save(newDocumentDataCache);
            } else {
                newDocumentDataCache.setInitShippingModeCache(data);
                BaseApplication.mdbUtils.update(newDocumentDataCache, "initShippingModeCache");
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    private final int SHOW_LOADING = 1;
    private final int HIDE_LOADING = 2;
    private final int SHOW_INFO = 3;
    private final int UPDATE_TONETWORK = 4;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW_LOADING:
                    LoadingDialog.show(NewActivity.this, false, false);
                    break;
                case HIDE_LOADING:
                    LoadingDialog.dismiss();
                    break;
                case SHOW_INFO:
                    Toast.makeText(NewActivity.this, (String) msg.obj, Toast.LENGTH_SHORT).show();
                    break;
                case UPDATE_TONETWORK:
                    updateToNetwork();
                    break;
            }
        }
    };

    private void initShipperCost() {
        shipperCostList = mDocument.getCarriageItems();
        if (shipperCostList == null || shipperCostList.size() < 1) {
            mListView.setVisibility(View.GONE);
            return;
        }
        mCarriageAdapter = new CarriageAdapter();
        mListView.setAdapter(mCarriageAdapter);
        setListViewHeightBasedOnChildren(mListView);
        //mCarriageAdapter.notifyDataSetChanged();
        mListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                final ShipperCost sc = shipperCostList.get(position);
                if (!sc.getFreightItem().isAllowEdit())
                    return;
                final EditText et = new EditText(mContext);
                et.setInputType(InputType.TYPE_CLASS_NUMBER);
                if (sc.getChargeValue() > 0)
                    et.setText(String.valueOf(sc.getChargeValue()));
                new Builder(mContext).setTitle(sc.getFreightItem().getItemName())
                        .setView(et)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                String input = et.getText().toString();
                                if (input.equals("")) {
                                    sc.setChargeValue(0);
                                } else
                                    sc.setChargeValue(Double.valueOf(input));
                                mCarriageAdapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
            }
        });
    }

    private void initBalanceMode() {
        mBalanceModeAdapter = new ArrayAdapter<String>(NewActivity.this, android.R.layout.simple_spinner_item, mBalanceModeNames);
        mBalanceModeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBalanceMode.setAdapter(mBalanceModeAdapter);
        mBalanceMode.setOnItemSelectedListener(balanceModeListener);
        if (user != null) {
            String balanceMode = user.getBalanceMode();
            for (int i = 0; i < mBalanceModeValues.length; i++) {
                if (balanceMode.equals(mBalanceModeValues[i])) {
                    mBalanceMode.setSelection(i);
                }
            }
        }
        mBalanceMode.setVisibility(View.VISIBLE);
    }

    public void showError(String info) {
        Message msg = new Message();
        msg.what = SHOW_INFO;
        msg.obj = info;
        handler.sendMessage(msg);
    }

    private void errorFinish(String info) {
        showError(info);
        this.finish();
    }

    //付款方式
    private OnItemSelectedListener balanceModeListener = new OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view,
                                   int position, long id) {
            mBalanceModeId = position;
            if (position == 2)
                mMontylyLayout.setVisibility(View.VISIBLE);
            else
                mMontylyLayout.setVisibility(View.GONE);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    //运输方式
    private OnItemSelectedListener sippingModeListener = new OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view,
                                   int position, long id) {
            //Log.e("mShippingModeId", String.valueOf(mShippingModeId));
            mShippingModeId = position;
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.

    }

    @Override
    public void onStop() {
        super.onStop();

    }


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
            if (!sc.getFreightItem().isAllowEdit()) {
                mHolder.mName.setTextColor(Color.parseColor("#dcdcdc"));
                mHolder.mValue.setTextColor(Color.parseColor("#dcdcdc"));
            }
            mHolder.mName.setText(sc.getFreightItem().getItemName());
            mHolder.mValue.setText(String.valueOf(sc.getChargeValue()));
            return convertView;
        }

        class ViewHolder {
            TextView mName;
            TextView mValue;
        }

    }

    private class ShipperContactAdapter extends BaseAdapter {
        private ViewHolder mHolder;

        @Override
        public int getCount() {
            return mConsignees.size();
        }

        @Override
        public Object getItem(int position) {
            return mConsignees.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(mContext, R.layout.shipperselect, null);
                mHolder = new ViewHolder();
                mHolder.mName = (TextView) convertView.findViewById(R.id.carriage_item_name);
                mHolder.mValue = (TextView) convertView.findViewById(R.id.carriage_item_value);
                mHolder.tvDel = (TextView) convertView.findViewById(R.id.btn_carriage_item_del);
                convertView.setTag(mHolder);
            } else
                mHolder = (ViewHolder) convertView.getTag();
            final Company sc = mConsignees.get(position);
            mHolder.mName.setText(sc.getName());
            mHolder.mValue.setText(String.valueOf(sc.getTel()));
            mHolder.tvDel.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialog(new DialoginOkCallBack() {
                        @Override
                        public void onClickOk(DialogInterface dialog, int which) {
                            //删除数据库
                            Company.deleteAll(Company.class, "name = ?", sc.getName());
                            mConsignees = Company.findAll(Company.class);
                            notifyDataSetChanged();
                        }
                    }, "是否确定？");
                }
            });
            return convertView;
        }

        class ViewHolder {
            TextView mName;
            TextView mValue;
            TextView tvDel;
        }

    }

    private class ConsigneeContactAdapter extends BaseAdapter {
        private ViewHolder mHolder;

        @Override
        public int getCount() {
            return mConsigneeList.size();
        }

        @Override
        public Object getItem(int position) {
            return mConsigneeList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(mContext, R.layout.list_item_carriage, null);
                mHolder = new ViewHolder();
                mHolder.mName = (TextView) convertView.findViewById(R.id.carriage_item_name);
                mHolder.mValue = (TextView) convertView.findViewById(R.id.carriage_item_value);
                convertView.setTag(mHolder);
            } else
                mHolder = (ViewHolder) convertView.getTag();
            Consignee sc = mConsigneeList.get(position);
            mHolder.mName.setText(sc.getCompanyName());
            mHolder.mValue.setText(sc.getPerson());
            return convertView;
        }

        class ViewHolder {
            TextView mName;
            TextView mValue;
        }

    }

    private class CustomerReceiverAdapter extends BaseAdapter {
        private ViewHolder mHolder;

        public CustomerReceiverAdapter() {
            if (mCustomerReceiversList == null)
                mCustomerReceiversList = new ArrayList<CustomerReceivers>();
        }

        @Override
        public int getCount() {
            return mCustomerReceiversList.size();
        }

        @Override
        public Object getItem(int position) {
            return mCustomerReceiversList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(mContext, R.layout.list_item_contact, null);
                mHolder = new ViewHolder();
                mHolder.mName = (TextView) convertView.findViewById(R.id.contact_item_name);
                mHolder.mPhone = (TextView) convertView.findViewById(R.id.contact_item_phone);
                mHolder.mContact = (TextView) convertView.findViewById(R.id.contact_item_contact);
                mHolder.mAddress = (TextView) convertView.findViewById(R.id.contact_item_address);
                convertView.setTag(mHolder);
            } else
                mHolder = (ViewHolder) convertView.getTag();

            CustomerReceivers sc = mCustomerReceiversList.get(position);
            mHolder.mName.setText(sc.getReceiverName());
            mHolder.mContact.setText(sc.getContactPerson());
            String shipperStr = "";
            if (StrKit.notBlank(sc.getAddress().getDistrict()))
                shipperStr += sc.getAddress().getDistrict();
            if (StrKit.notBlank(sc.getAddress().getAddress()))
                shipperStr += sc.getAddress().getAddress();
            mHolder.mAddress.setText(shipperStr);
            mHolder.mPhone.setText(sc.getAreaCode() + "-" + sc.getTelNumber());
            return convertView;
        }

        class ViewHolder {
            TextView mName;
            TextView mPhone;
            TextView mContact;
            TextView mAddress;
        }

    }

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SCANNING_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    //显示扫描到的内容
                    mNumber.setText(bundle.getString("result"));
                }
                break;
            case 0:
                if (resultCode == RESULT_OK) {
                    mConsigneeProvince.setText(data.getStringExtra("result"));
                }
                break;
            case MADEFRAGM_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    WoodenFrames = (ArrayList<WoodenFrame>) data.getSerializableExtra("result");
                }
                break;
            case COUNTWEIGHT_ACTIVITY:
                if (resultCode == RESULT_OK) {
                    CountWeightInfo info = (CountWeightInfo) data.getSerializableExtra("result");
                    mProductQuantity.setText(info.getCount() + "");
                    mProductVolumn.setText(info.getVol() + "");
                    mCountWeight.setText(info.getCountWeight() + "");
                    mRemarks.setText(info.getRemark());
                }
                break;
            case CUSTOMINFOINT:
                if (resultCode == RESULT_OK) {
                    CustomOtherInfos otherInfos = (CustomOtherInfos) data.getSerializableExtra("data");
                    mNeedInsurance.setChecked(otherInfos.isInsurance());
                    mProductName.setText(otherInfos.getGoodsName());
                    mCountWeight.setText(otherInfos.getCountWeight());
                    for (int i = 0; i < mShippingModeStrs.length; i++) {
                        if (mShippingModeStrs[i].equals(otherInfos.getShipMode()))
                            mShippingMode.setSelection(i);
                    }

                    Consignee consignee = Consignee.queryByName(otherInfos.getConsigneeName());
                    if (consignee != null) {
                        mConsigneeName.setText(consignee.getCompanyName());
                        mConsigneeContact.setText(consignee.getPerson());
                        mConsigneeProvince.setText(consignee.getCity());
                        mConsigneeAddress.setText(consignee.getAddressDetail());
                        mConsigneeArea.setText(consignee.getAreacode());
                        mConsigneePhone.setText(consignee.getMobileNum());
                        mConsigneePhone2.setText(consignee.getOtherPhone());
                        mNeedDelivery.setChecked(consignee.isUpdoor());
                        mNeedGOFloor.setChecked(consignee.isUpfloor());
                    }

                    ShipPerson shipPerson = ShipPerson.queryByName(otherInfos.getShipName());
                    if (shipPerson != null) {
                        mShipperContact.setText(shipPerson.getName());
                        mShipperProvince.setText(shipPerson.getCity());
                        mShipperAddress.setText(shipPerson.getAddressDetail());
                        mShipperArea.setText(shipPerson.getAreaCode());
                        mShipperPhone.setText(shipPerson.getMobile());
                        mShipperPhone2.setText(shipPerson.getOtherPhone());
                    }
                    if (mLvCon != null) {
                        mLvCon.setVisibility(View.GONE);
                    }
                    if (mLvShipp != null) {
                        mLvShipp.setVisibility(View.GONE);
                    }
                }
                break;
        }
    }
}
