package cn.xsjky.android.ui;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.io.ByteArrayInputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import cn.xsjky.android.BaseApplication;
import cn.xsjky.android.BaseSettings;
import cn.xsjky.android.R;
import cn.xsjky.android.adapter.ItemGetunfinishAdapter;
import cn.xsjky.android.constant.SoapAction;
import cn.xsjky.android.constant.SoapEndpoint;
import cn.xsjky.android.constant.SoapInfo;
import cn.xsjky.android.http.Urls;
import cn.xsjky.android.model.DeliveryRequest;
import cn.xsjky.android.model.Document;
import cn.xsjky.android.model.Infos;
import cn.xsjky.android.model.LoginInfo;
import cn.xsjky.android.util.CallBackString;
import cn.xsjky.android.util.DataFormatUtils;
import cn.xsjky.android.util.DialoginOkCallBack;
import cn.xsjky.android.util.GetUnfinishRequestXmlParser;
import cn.xsjky.android.util.LogU;
import cn.xsjky.android.util.MySoapHttpRequest;
import cn.xsjky.android.util.OnClickutils;
import cn.xsjky.android.util.RetrueCodeHandler;
import cn.xsjky.android.util.StrKit;
import cn.xsjky.android.weiget.LoadingDialog;

public class ApplyDocumentActivity extends BaseActivity {
    private static final int LOCATON_PERMISSION = 1;
    private ProgressDialog progressDialog;
    private PullToRefreshListView mListView;
    private ImageView mFlip;
    private TextView mTitle;
    private Context mContext;
    private ReceiveAdapter mAdapter;
    private List<Document> mDocumentList;
    private final int REFRESH_LIST = 1;
    private boolean isAcceptRequest;
    private String title;
    private ItemGetunfinishAdapter mAdapter2;
    private ArrayList<DeliveryRequest> list2;
    private ImageView mIvHeaderRight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_list);
        Intent intent = getIntent();
        isAcceptRequest = intent.getBooleanExtra("IsAcceptRequest", false);
        title = "";
        if (isAcceptRequest) {
            title = "接收请求";
        } else {
            title = "待收件";
        }
        mContext = this;
        mDocumentList = new ArrayList<Document>();
        mContext = this;
        mAdapter = new ReceiveAdapter();
        findViewById();

    }

    private int degg = 0;

    private void findViewById() {
        mFlip = (ImageView) this.findViewById(R.id.head_flip);
        mIvHeaderRight = (ImageView) this.findViewById(R.id.head_refresh);
        mIvHeaderRight.setImageResource(R.drawable.up);
        mIvHeaderRight.setVisibility(View.VISIBLE);
        mIvHeaderRight.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAcceptRequest) {
                    return;
                }
                flag = !flag;
                degg += 180;
                mIvHeaderRight.animate().rotation(degg).setDuration(300);
                mDocumentList.clear();
                receivedOk();
            }
        });
        mFlip.setVisibility(View.VISIBLE);
        mFlip.setImageResource(R.drawable.ic_flipper_head_back);
        mFlip.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ApplyDocumentActivity.this.finish();
            }
        });
        mTitle = (TextView) this.findViewById(R.id.head_title);
        mTitle.setText(title);
        mListView = (PullToRefreshListView) this.findViewById(R.id.list_listview);
        mListView.setMode(PullToRefreshBase.Mode.BOTH);
        mAdapter = new ReceiveAdapter();
        list2 = new ArrayList<>();
        mAdapter2 = new ItemGetunfinishAdapter<>(this, list2);
        if (isAcceptRequest) {
            mListView.setAdapter(mAdapter2);
        } else {
            mListView.setAdapter(mAdapter);
        }
        mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                mDocumentList.clear();
                page = 1;
                if (isAcceptRequest) {
                    list2.clear();
                    GetUnfinishRequest();
                } else {
                    receivedOk();
                }
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                page++;
                if (isAcceptRequest) {
                    GetUnfinishRequest();
                } else {
                    receivedOk();
                }
            }
        });

        mListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (isAcceptRequest) {
                    mPostion = position;
                    if (Build.VERSION.SDK_INT >= 23) {
                        if (ActivityCompat.checkSelfPermission(ApplyDocumentActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(ApplyDocumentActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATON_PERMISSION);
                        } else {
                            intoMap(position);
                        }
                    } else {
                        intoMap(position);
                    }
                    return;
                }
                BaseApplication.getApplication().setDocument(mDocumentList.get(position - 1));
                Intent intent = new Intent(ApplyDocumentActivity.this, ApplyDocumentDetailActivity.class);
                ApplyDocumentActivity.this.startActivityForResult(intent, 0);
            }
        });
    }

    private int mPostion = -1;

    private void intoMap(int position) {
        DeliveryRequest deliveryRequest = list2.get(position - 1);
        Intent intent = new Intent(ApplyDocumentActivity.this, IsAcceptDetailActivity.class);
        intent.putExtra("data", deliveryRequest);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case LOCATON_PERMISSION:
//是否同意了权限
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//同意
                    intoMap(mPostion);
                } else {
//拒绝 已经拒绝过也走这边
                    Toast.makeText(this, "拒绝了定位相关的权限", Toast.LENGTH_SHORT).show();
                    showDialog(new DialoginOkCallBack() {
                        @Override
                        public void onClickOk(DialogInterface dialog, int which) {
                            getAppDetailSettingIntent(ApplyDocumentActivity.this);
                        }
                    }, "进入权限->定位权限开通相关权限即可");
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

    @Override
    protected void onResume() {
        mDocumentList.clear();
        list2.clear();
        if (isAcceptRequest) {
            GetUnfinishRequest();
        } else {
            receivedOk();
        }
        super.onResume();
    }

    /*private void init() {

        String templet = TempletUtil.render(BaseSettings.GETAPPLYDOCUMENTS_TEMPLET, BaseApplication.getApplication().getSecurityInfo());

        //XmlTask.parserXml(templet, BaseSettings.GETAPPLYDOCUMENTS_ACTION, new HttpCallback(){

        BaseApplication.getApplication().getHttpPro().postAsync(templet, BaseSettings.GETAPPLYDOCUMENTS_ACTION, new HttpCallback() {

            @Override
            public void onHttpStart() {
                setHandler(SHOW_LOADING, "");
            }

            @Override
            public void onHttpFinish(String data) {
                //Log.e("data", data);
                if (StrKit.isBlank(data)) {
                    setHandler(SHOW_INFO, "请求数据失败!");
                    return;
                }
                mDocumentList = Document.parserXmlList(data, "/soap:Envelope/soap:Body/GetApplyDocumentsResponse/GetApplyDocumentsResult/ReturnList/");
                mAdapter.notifyDataSetChanged();
                //mAdapter.notifyDataSetChanged();
                setHandler(HIDE_LOADING, "");
                mListView.onRefreshComplete();
            }

            @Override
            public void onHttpError(String msg) {
                setHandler(SHOW_INFO, msg);
                setHandler(HIDE_LOADING, "");
                mListView.onRefreshComplete();
            }

            @Override
            public void onHttpEnd() {
            }

        });
    }*/

    private int page = 1;
    private boolean flag = false;

    private void receivedOk() {
        showProgressDialog();
        final String endPoint = SoapEndpoint.SET_IS_SIGNUP;
        final String soapAction = SoapAction.GET_APPLY_DOCUMENT;
        final LoginInfo loginInfo = BaseApplication.loginInfo;
        String info = SoapInfo.GET_APPLY_DOCUMENT;
        info = info.replace("UserIdValue", loginInfo.getUserId() + "");
        info = info.replace("ClientNameValue", Infos.CLIENTNAME);
        info = info.replace("SessionIdValue", loginInfo.getSessionId());
        info = info.replace("RoleDataValue", "0");
        info = info.replace("pageNumberValue", page + "");
        info = info.replace("pageSizeValue", "10");
        String date = DataFormatUtils.getDate(System.currentTimeMillis());
        info = info.replace("signUpTimeValue", date);
        info = info.replace("sortDesendFlagValue", "" + flag);
        final String finalInfo = info;
        MySoapHttpRequest.getString(new CallBackString() {
            @Override
            public void httFinsh(final String data) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        mDocumentList.addAll(Document.parserXmlList(data, "/soap:Envelope/soap:Body/GetApplyDocumentsResponse/GetApplyDocumentsResult/ReturnList/"));
                        mAdapter.notifyDataSetChanged();
                        setHandler(HIDE_LOADING, "");
                        mListView.onRefreshComplete();
                    }
                });

                //mAdapter.notifyDataSetChanged();
                setHandler(HIDE_LOADING, "");

            }
        }, endPoint, soapAction, finalInfo);
    }

    private void GetUnfinishRequest() {
        showProgressDialog();
        LoginInfo loginInfo = BaseApplication.loginInfo;
        RequestParams params = new RequestParams();
        params.addBodyParameter("sessionId", loginInfo.getSessionId());
        params.addBodyParameter("userId", loginInfo.getUserId() + "");
        params.addBodyParameter("clientName", BaseSettings.CLIENT_NAME);
        params.addBodyParameter("returnList", "true");
        params.addBodyParameter("pageNumber", page + "");
        params.addBodyParameter("pageSize", "10");
        BaseApplication.mHttpUtils.send(HttpRequest.HttpMethod.POST,
                Urls.GetUnfinishRequest,
                params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        SAXParserFactory factory = SAXParserFactory.newInstance();
                        SAXParser parser = null;
                        try {
                            parser = factory.newSAXParser();
                            RetrueCodeHandler handler = new RetrueCodeHandler();
                            ByteArrayInputStream tInputStringStream = new ByteArrayInputStream(responseInfo.result.getBytes());
                            parser.parse(tInputStringStream, handler);
                            if (handler.getString().equals("0")) {
                                SAXParserFactory factory1 = SAXParserFactory.newInstance();
                                SAXParser parser1 = factory1.newSAXParser();
                                GetUnfinishRequestXmlParser handler1 = new GetUnfinishRequestXmlParser();
                                ByteArrayInputStream tInputStringStream1 = new ByteArrayInputStream(responseInfo.result.getBytes());
                                parser1.parse(tInputStringStream1, handler1);
                                list2.addAll(handler1.getList());
                                LogU.e(list2.toString());
                                mAdapter2.notifyDataSetChanged();
                                setHandler(HIDE_LOADING, "");
                                mListView.onRefreshComplete();
                            } else {
                                Toast.makeText(ApplyDocumentActivity.this, handler.getError(), Toast.LENGTH_SHORT).show();
                                mListView.onRefreshComplete();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(ApplyDocumentActivity.this, "数据解析出错", Toast.LENGTH_SHORT).show();
                        } finally {
                            mListView.onRefreshComplete();
                            closeProgressDialog();
                        }

                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        mListView.onRefreshComplete();
                        Toast.makeText(ApplyDocumentActivity.this, "网络数据获取失败", Toast.LENGTH_SHORT).show();
                        closeProgressDialog();
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == REFRESH_LIST)
            receivedOk();
    }

    public void showProgressDialog() {
        if (isFinishing()) {
            return;
        }
        if ((!isFinishing()) && (this.progressDialog == null)) {
            this.progressDialog = new ProgressDialog(this);
        }
        this.progressDialog.setTitle("正在更新数据");
        this.progressDialog.setMessage("请稍后");
        this.progressDialog.setCanceledOnTouchOutside(false);
        this.progressDialog.show();

    }

    public void closeProgressDialog() {
        if (this.progressDialog != null)
            this.progressDialog.dismiss();
    }

    public void setHandler(int code, Object obj) {
        Message msg = new Message();
        msg.what = code;
        msg.obj = obj;
        handler.sendMessage(msg);
    }

    private final int SHOW_LOADING = 1;
    private final int HIDE_LOADING = 2;
    private final int SHOW_INFO = 3;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW_LOADING:
                    LoadingDialog.show(ApplyDocumentActivity.this, false, false);
                    break;
                case HIDE_LOADING:
                    LoadingDialog.dismiss();
                    break;
                case SHOW_INFO:
                    Toast.makeText(ApplyDocumentActivity.this, (String) msg.obj, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    private class ReceiveAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        private ViewHolder mHolder;
        private SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");

        public ReceiveAdapter() {
            inflater = LayoutInflater.from(mContext);
        }

        @Override
        public int getCount() {
            return mDocumentList.size();
        }

        @Override
        public Object getItem(int position) {
            return mDocumentList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                mHolder = new ViewHolder();
                convertView = inflater.inflate(R.layout.list_item_applydocument, null);
                mHolder.mName = (TextView) convertView.findViewById(R.id.applydocument_item_name);
                mHolder.mPhone = (TextView) convertView.findViewById(R.id.applydocument_item_phone);
                mHolder.mTime = (TextView) convertView.findViewById(R.id.applydocument_item_time);
                mHolder.mAddress = (TextView) convertView.findViewById(R.id.applydocument_item_address);
                mHolder.btnShowLocation = (Button) convertView.findViewById(R.id.applydocument_item_location_btn);
                convertView.setTag(mHolder);
            } else
                mHolder = (ViewHolder) convertView.getTag();

            final Document d = mDocumentList.get(position);
            String name = "";
            if (StrKit.notBlank(d.getShipperName()))
                name = d.getShipperName() + ",";
            name += d.getShipperContactName();
            mHolder.mName.setText(name);
            mHolder.mPhone.setText(d.getShipperAreaCode() + d.getShipperPhoneNumber());
            Date date = null;
            try {
                date = new SimpleDateFormat("yyyy-MM-dd").parse(d.getCreateTime());
                mHolder.mTime.setText(format.format(date));
            } catch (ParseException e) {
                mHolder.mTime.setText("");
            }
            String shipperStr = "";
            if (d.getConsigneeAddress() != null) {

                if (StrKit.notBlank(d.getShipperAddress().getDistrict()))
                    shipperStr += d.getShipperAddress().getDistrict();
                shipperStr += d.getShipperAddress().getAddress();
                mHolder.mAddress.setText(shipperStr);
            }
            final String finalName = name;
            final String finalShipperStr = shipperStr;
            final Date finalDate = date;
            mHolder.btnShowLocation.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (OnClickutils.isFastDoubleClick()) {
                        return;
                    }
                    Intent intent = new Intent(ApplyDocumentActivity.this, MapApplyLocation.class);
                    intent.putExtra("name", finalName);
                    intent.putExtra("date", format.format(finalDate));
                    intent.putExtra("shipperStr", finalShipperStr);
                    intent.putExtra("phone", d.getShipperAreaCode() + d.getShipperPhoneNumber());
                    startActivity(intent);
                }
            });
            return convertView;
        }

        class ViewHolder {
            TextView mName;
            TextView mPhone;
            TextView mTime;
            TextView mAddress;
            Button btnShowLocation;
        }

    }

}
