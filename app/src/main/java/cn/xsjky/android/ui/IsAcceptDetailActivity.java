package cn.xsjky.android.ui;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.io.ByteArrayInputStream;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import cn.xsjky.android.BaseApplication;
import cn.xsjky.android.BaseSettings;
import cn.xsjky.android.R;
import cn.xsjky.android.http.Urls;
import cn.xsjky.android.model.AcceptRequestResult;
import cn.xsjky.android.model.CancelDeliveryRequest;
import cn.xsjky.android.model.DeliveryRequest;
import cn.xsjky.android.model.LoginInfo;
import cn.xsjky.android.service.GPSService;
import cn.xsjky.android.util.AcceptRequestResultXmlParser;
import cn.xsjky.android.util.CancelDeliveryRequestXmlParser;
import cn.xsjky.android.util.DialoginOkCallBack;
import cn.xsjky.android.util.OnClickutils;
import cn.xsjky.android.util.RetrueCodeHandler;
import cn.xsjky.android.util.RetruenUtils;

public class IsAcceptDetailActivity extends BaseActivity implements View.OnClickListener {

    private static final int LOCATON_PERMISSION = 1;
    private TextView tvIntoDetailDocumentNum;
    private TextView tvIntoDetailSendName;
    private TextView tvIntoDetailActors;
    private TextView tvIntoDetailQty;
    private DeliveryRequest deliveryRequest;
    private ImageView mBack;
    private TextView mTitle;
    private Button btnAccept;
    private Button btnRefuse;
    private RadioGroup rg_acceptDetail;
    private EditText etRefuse;
    private TextView tvIntoDetailStatus;
    private Dialog dialog;
    private LinearLayout layout;
    private Button btnDialogOk;
    private Button btnDialogCancel;
    private Button btnLbs;
    private Button btnFinish;
    private TextView tvIntoDetailRemarks;
    private AcceptRequestResult acceptRequestResult;
    private Button btnNewDocument;
    private TextView tvIntoDetailRequestRemarks;
    private TextView tvIntoDetailCargoVolumn;
    private TextView tvIntoDetailShipperName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_is_accept_detail);
        deliveryRequest = (DeliveryRequest) getIntent().getSerializableExtra("data");
        findview();
        setlistener();
    }

    private int currId = -1;
    private void setlistener() {
        btnAccept.setOnClickListener(this);
        btnRefuse.setOnClickListener(this);
        rg_acceptDetail.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                currId = checkedId;
                if (checkedId == R.id.rb_other) {
                    etRefuse.setVisibility(View.VISIBLE);
                    etRefuse.setFocusable(true);
                    etRefuse.setEnabled(true);
                } else {
                    etRefuse.setVisibility(View.GONE);
                }
            }
        });
        btnLbs.setOnClickListener(this);
        btnFinish.setOnClickListener(this);
        btnNewDocument.setOnClickListener(this);
    }

    private void findview() {
        builderDialog();
        tvIntoDetailDocumentNum = (TextView) findViewById(R.id.tv_IntoDetail_DocumentNum);
        tvIntoDetailShipperName = (TextView) findViewById(R.id.tv_IntoDetail_ShipperName);
        tvIntoDetailShipperName.setText("寄件单位："+deliveryRequest.getShipperName());
        tvIntoDetailCargoVolumn = (TextView) findViewById(R.id.tv_IntoDetail_CargoVolumn);
        tvIntoDetailCargoVolumn.setText("最大体积：" + deliveryRequest.getCargoVolumn());
        tvIntoDetailRequestRemarks = (TextView) findViewById(R.id.tv_IntoDetail_RequestRemarks);
        List<String> list = deliveryRequest.getRequestMarks().getList();
        String requestMarks = "备注:";
        for (int i = 0; i < list.size(); i++) {
            requestMarks += (i + 1) + "." + list.get(i);
        }
        tvIntoDetailRequestRemarks.setText(requestMarks);
        tvIntoDetailStatus = (TextView) findViewById(R.id.tv_IntoDetail_status);
        tvIntoDetailRemarks = (TextView) findViewById(R.id.tv_IntoDetail_Remarks);
        tvIntoDetailRemarks.setText("备注：" + deliveryRequest.getRemarks());
        String status = "";
        if (deliveryRequest.getStatus().equals("IsAccept")) {
            status = "已接受";
            /*if (!BaseApplication.isAccept) {
                BaseApplication.getApplication().stopLocationService();
                BaseApplication.getApplication().startLocationService(true);
            }*/
        } else if (deliveryRequest.getStatus().equals("IsReject")) {
            status = "已请求";

        } else if (deliveryRequest.getStatus().equals("IsOnWay")) {
            status = "在路上";
        }
        tvIntoDetailStatus.setText("状态：" + status);
        tvIntoDetailSendName = (TextView) findViewById(R.id.tv_IntoDetail_SendName);
        tvIntoDetailActors = (TextView) findViewById(R.id.tv_IntoDetail_Actors);
        tvIntoDetailQty = (TextView) findViewById(R.id.tv_IntoDetail_Qty);
        btnAccept = (Button) findViewById(R.id.btn_acceptDetail_accept);
        btnRefuse = (Button) findViewById(R.id.btn_acceptDetail_refuse);
        btnLbs = (Button) findViewById(R.id.btn_acceptDetail_Lbs);
        btnFinish = (Button) findViewById(R.id.btn_acceptDetail_finish);
        btnNewDocument = (Button) findViewById(R.id.btn_acceptDetail_new_document);
        if (deliveryRequest.getStatus().equals("IsAccept") || deliveryRequest.getStatus().equals("IsOnWay")) {
            btnAccept.setVisibility(View.GONE);
            btnLbs.setVisibility(View.VISIBLE);
            btnFinish.setVisibility(View.VISIBLE);
        }
        etRefuse = (EditText) dialog.findViewById(R.id.et_refuse_reason);
        btnDialogOk = (Button) dialog.findViewById(R.id.btn_Ok);
        btnDialogCancel = (Button) dialog.findViewById(R.id.btn_cancel);
        btnDialogCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnDialogOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickDialogOk();
            }
        });
        rg_acceptDetail = (RadioGroup) dialog.findViewById(R.id.rg_acceptDetail);
        tvIntoDetailDocumentNum.setText("寄件人：" + deliveryRequest.getContactPerson());
        tvIntoDetailSendName.setText("电话号码：" + deliveryRequest.getContactNumber());
        tvIntoDetailSendName.setTextColor(Color.BLUE);
        tvIntoDetailSendName.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        tvIntoDetailSendName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(OnClickutils.isFastDoubleClick()){
                    return;
                }
                callPhone();
            }
        });
        tvIntoDetailActors.setText("时间：" + deliveryRequest.getAppointment());
        tvIntoDetailQty.setText("地址：" + deliveryRequest.getAddress());
        mBack = (ImageView) this.findViewById(R.id.head_flip);
        mBack.setImageResource(R.drawable.ic_flipper_head_back);
        mBack.setVisibility(View.VISIBLE);
        mBack.setOnClickListener(this);
        mTitle = (TextView) this.findViewById(R.id.head_title);
        mTitle.setText("接收请求详情");
    }

    private void callPhone() {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + deliveryRequest.getContactNumber()));
        if (TextUtils.isEmpty(deliveryRequest.getContactNumber())) {
            Toast.makeText(IsAcceptDetailActivity.this, "电话号码有误", Toast.LENGTH_SHORT).show();
            return;
        }
        startActivity(intent);
    }

    private void builderDialog() {

        WindowManager wm = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        LayoutInflater inflaterDl = LayoutInflater.from(this);
        layout = (LinearLayout) inflaterDl.inflate(R.layout.dialog_refush, null);
        //对话框

        dialog = new AlertDialog.Builder(this).create();
        dialog.show();
        dialog.getWindow().clearFlags(
                WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dialog.setContentView(layout);
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = (int) (width * 0.8);
        params.height = (int) (height * 0.6);
        dialog.getWindow().setAttributes(params);
        dialog.dismiss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_flip:
                finish();
                break;
            case R.id.btn_acceptDetail_accept:
                if (deliveryRequest.getStatus().equals("IsAccept")) {
                    return;
                }
                accept();
                break;
            case R.id.btn_acceptDetail_refuse:
                dialog.show();
                break;
            case R.id.btn_acceptDetail_finish:
                showDialog(new DialoginOkCallBack() {
                    @Override
                    public void onClickOk(DialogInterface dialog, int which) {
                        documentFinish();
                    }
                },"是否确定完成？");
                break;
            case R.id.btn_acceptDetail_new_document:
                newDocument();
                break;
            case R.id.btn_acceptDetail_Lbs:
                if (Build.VERSION.SDK_INT >= 23) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(IsAcceptDetailActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATON_PERMISSION);
                    }else {
                        initMapLbs();
                    }
                }else {
                    initMapLbs();
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case LOCATON_PERMISSION:
//是否同意了权限
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//同意
                    initMapLbs();
                } else {
//拒绝 已经拒绝过也走这边
                    Toast.makeText(this, "拒绝了定位相关的权限", 1).show();
                    showDialog(new DialoginOkCallBack() {
                        @Override
                        public void onClickOk(DialogInterface dialog, int which) {
                            getAppDetailSettingIntent(IsAcceptDetailActivity.this);
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
            localIntent.setClassName("com.android.settings","com.android.settings.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", getPackageName());
        }
        startActivity(localIntent);
    }
    private void newDocument() {
        Intent intent = new Intent(IsAcceptDetailActivity.this, NewActivity.class);
        intent.putExtra("name", deliveryRequest.getContactPerson());
        intent.putExtra("phone", deliveryRequest.getContactNumber());
        intent.putExtra("address", deliveryRequest.getAddress());
        startActivity(intent);
    }

    private void initMapLbs() {
        Intent intent = new Intent(this, MapApplyLocation.class);
        intent.putExtra("name", deliveryRequest.getContactPerson());
        intent.putExtra("date", deliveryRequest.getAppointment());
        intent.putExtra("shipperStr", deliveryRequest.getAddress());
        intent.putExtra("phone", deliveryRequest.getContactNumber());
        if (!deliveryRequest.getLatitude().equals("0") && !deliveryRequest.getLongitude().equals("0")) {
            intent.putExtra("point", deliveryRequest.getLatitude() + "," + deliveryRequest.getLongitude());
        }
        startActivity(intent);
    }

    private void documentFinish() {
        final DeliveryRequest request = deliveryRequest;
        LoginInfo loginInfo = BaseApplication.loginInfo;
        RequestParams params = new RequestParams();
        params.addBodyParameter("sessionId", loginInfo.getSessionId());
        params.addBodyParameter("userId", loginInfo.getUserId() + "");
        params.addBodyParameter("clientName", BaseSettings.CLIENT_NAME);
        params.addBodyParameter("requestId", request.getRequestId() + "");
        params.addBodyParameter("handler", loginInfo.getUserId() + "");
        BaseApplication.mHttpUtils.send(HttpRequest.HttpMethod.POST,
                Urls.CompleteRequest,
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

                              /*  BaseApplication.getApplication().stopLocationService();
                                BaseApplication.getApplication().startLocationService(false);*/
                                Intent intent = new Intent(IsAcceptDetailActivity.this, NewActivity.class);
                                intent.putExtra("name", request.getContactPerson());
                                intent.putExtra("phone", request.getContactNumber());
                                intent.putExtra("address", request.getAddress());
                                startActivity(intent);
                                btnFinish.setVisibility(View.GONE);
                                tvIntoDetailStatus.setText("状态：已完成");
                                btnNewDocument.setVisibility(View.VISIBLE);
                            } else {
                                Toast.makeText(IsAcceptDetailActivity.this, handler.getError(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(IsAcceptDetailActivity.this, "数据解析出错", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        Toast.makeText(IsAcceptDetailActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void clickDialogOk() {
        if (currId == -1) {
            Toast.makeText(this, "请选择一个拒绝的原因", Toast.LENGTH_SHORT).show();
        } else {
            refuse();
        }
        dialog.dismiss();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        finish();
        return super.onKeyDown(keyCode, event);
    }

    private void refuse() {
        showProgressDialog();
        String url = Urls.RejectRequest;
        RequestParams params = new RequestParams();
        LoginInfo loginInfo = BaseApplication.loginInfo;
        params.addBodyParameter("sessionId", loginInfo.getSessionId());
        params.addBodyParameter("userId", loginInfo.getUserId() + "");
        params.addBodyParameter("clientName", BaseSettings.CLIENT_NAME);
        params.addBodyParameter("requestId", deliveryRequest.getRequestId());
        String rejectReason = "";
        if (currId == 0) {
            rejectReason = "本人有事";
        } else if (currId == 1) {
            rejectReason = "车子问题";
        } else if (currId == 2) {
            rejectReason = etRefuse.getText().toString();
        }
        params.addBodyParameter("rejectReason", rejectReason);
        params.addBodyParameter("handlerId", "0");
        BaseApplication.mHttpUtils.send(HttpRequest.HttpMethod.POST,
                url,
                params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        CancelDeliveryRequestXmlParser handler = RetruenUtils.getReturnInfo(responseInfo.result, new CancelDeliveryRequestXmlParser());
                        if (handler != null) {
                            CancelDeliveryRequest request = handler.getCancelDeliveryRequest();
                            if (request.getReturnCode().equals("0")) {
                                Toast.makeText(IsAcceptDetailActivity.this, "拒绝已提交", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(IsAcceptDetailActivity.this, request.getError(), Toast.LENGTH_SHORT).show();
                            }
                        } else {

                            Toast.makeText(IsAcceptDetailActivity.this, "提交错误", Toast.LENGTH_SHORT).show();
                        }
                        closeProgressDialog();
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {

                        Toast.makeText(IsAcceptDetailActivity.this, "网络请求失败", Toast.LENGTH_SHORT).show();
                        closeProgressDialog();
                    }
                });

    }

    private void accept() {
        showProgressDialog();
        String url = Urls.AcceptRequest;
        RequestParams params = new RequestParams();
        LoginInfo loginInfo = BaseApplication.loginInfo;
        params.addBodyParameter("sessionId", loginInfo.getSessionId());
        params.addBodyParameter("userId", loginInfo.getUserId() + "");
        params.addBodyParameter("clientName", BaseSettings.CLIENT_NAME);
        params.addBodyParameter("requestId", deliveryRequest.getRequestId());
        BaseApplication.mHttpUtils.send(HttpRequest.HttpMethod.POST,
                url,
                params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        RetrueCodeHandler parser = RetruenUtils.getReturnInfo(responseInfo.result, new RetrueCodeHandler());
                        if (parser != null && parser.getString().equals("0")) {
                            AcceptRequestResultXmlParser acceptRequestResultXmlParser = RetruenUtils.getReturnInfo(responseInfo.result, new AcceptRequestResultXmlParser());
                            if (acceptRequestResultXmlParser != null) {
                                acceptRequestResult = acceptRequestResultXmlParser.getAcceptRequestResult();
                                tvIntoDetailRemarks.setText("备注：" + acceptRequestResult.getRemarks());
                                String status = "";
                                if (acceptRequestResult.getStatus().equals("IsAccept")) {
                                    status = "已接受";
                                } else if (acceptRequestResult.getStatus().equals("IsReject")) {
                                    status = "已请求";
                                } else if (acceptRequestResult.getStatus().equals("IsOnWay")) {
                                    status = "在路上";
                                }
                                tvIntoDetailCargoVolumn.setText("最大体积：" + acceptRequestResult.getCargoVolumn());
                                tvIntoDetailStatus.setText("状态" + status);
                                btnAccept.setVisibility(View.GONE);
                                btnLbs.setVisibility(View.VISIBLE);
                                btnFinish.setVisibility(View.VISIBLE);
                            }
                            Toast.makeText(IsAcceptDetailActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
                        } else {
                            if (parser != null)
                                Toast.makeText(IsAcceptDetailActivity.this, parser.getError(), Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(IsAcceptDetailActivity.this, "解析错误", Toast.LENGTH_SHORT).show();
                        }
                        closeProgressDialog();

                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {

                        Toast.makeText(IsAcceptDetailActivity.this, "网络请求失败", Toast.LENGTH_SHORT).show();
                        closeProgressDialog();
                    }
                });
    }

}
