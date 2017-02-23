package cn.xsjky.android.ui;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;
import com.jq.printer.esc.Text;
import com.lidroid.xutils.http.RequestParams;

import org.xml.sax.SAXException;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import cn.xsjky.android.BaseApplication;
import cn.xsjky.android.BaseSettings;
import cn.xsjky.android.R;
import cn.xsjky.android.constant.SoapAction;
import cn.xsjky.android.constant.SoapEndpoint;
import cn.xsjky.android.constant.SoapInfo;
import cn.xsjky.android.http.Urls;
import cn.xsjky.android.model.DocumentEntity;
import cn.xsjky.android.model.Infos;
import cn.xsjky.android.model.LoginInfo;
import cn.xsjky.android.service.GPSService;
import cn.xsjky.android.service.GetLocationService;
import cn.xsjky.android.service.LocationIntface;
import cn.xsjky.android.util.BitmapUtils;
import cn.xsjky.android.util.CallBackString;
import cn.xsjky.android.util.DataFormatUtils;
import cn.xsjky.android.util.DialoginOkCallBack;
import cn.xsjky.android.util.LogU;
import cn.xsjky.android.util.MySoapHttpRequest;
import cn.xsjky.android.util.OnClickutils;
import cn.xsjky.android.util.PermissionUtil;
import cn.xsjky.android.util.ReadImgToBinary2;
import cn.xsjky.android.util.RetrueCodeHandler;
import cn.xsjky.android.util.StrKit;
import cn.xsjky.android.util.XmlParserUtil;

public class ActivityDocumentDetailActivity extends BaseActivity {
    private static final String TAG = "my";
    private TextView tvName;
    private TextView tvDocumentDetailDocumentNumber;
    private TextView tvDocumentDaetailTel;
    private TextView tvDocumentDaetailAddress1;
    private TextView tvDocumentDaetailAddress2;
    private TextView tvDocumentDaetailQuantity;
    private TextView tvDocumentDaetailWeight;
    private TextView tvDocumentDaetailShippingMode;
    private TextView tvDocumentDaetailShippingStatus;
    private DocumentEntity document;
    private Button btnReceived;
    private EditText etReceivedName;
    private ImageView mFlip;
    private TextView tvInfo;
    private ImageView ivPhoto;
    private Uri photoUri;
    private File myCaptureFile;
    public static String mFilePath = null;
    private String imgToBase64;
    private Intent serviceIntent;
    private LatLng latLng;
    private static final int REQUEST_CONTACTS = 1;
    private static final String[] PERMISSIONS_CONTACT = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_PHONE_STATE,
    };
    private Button btnReceivedReset;
    private Bitmap mBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_detail);
        mFilePath = getExternalCacheDir().getPath() + "/xsj.jpg";
        if (Build.VERSION.SDK_INT >= 23) {
            showContacts();
        } else {
            init();
        }
    }

    public void showContacts() {
        Log.e(TAG, "Show contacts button pressed. Checking permissions.");
        // Verify that all required contact permissions have been granted.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            // Contacts permissions have not been granted.
            Log.e(TAG, "Contact permissions has NOT been granted. Requesting permissions.");
            requestContactsPermissions();

        } else {
            // Contact permissions have been granted. Show the contacts fragment.
            Log.e(TAG,
                    "Contact permissions have already been granted. Displaying contact details.");
            init();
        }
    }

    private void requestContactsPermissions() {
        // BEGIN_INCLUDE(contacts_permission_request)
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                || ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                || ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                || ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_PHONE_STATE)
                ) {

            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // For example, if the request has been denied previously.
            Log.i(TAG,
                    "Displaying contacts permission rationale to provide additional context.");

            // Display a SnackBar with an explanation and a button to trigger the request.
          /*  Snackbar.make(v, "permission_contacts_rationale",
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction("ok", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ActivityCompat.requestPermissions(GetLocationActivity.this, PERMISSIONS_CONTACT,
                                    REQUEST_CONTACTS);
                        }
                    })
                    .show();*/
        } else {
            // Contact permissions have not been granted yet. Request them directly.
            ActivityCompat.requestPermissions(this, PERMISSIONS_CONTACT, REQUEST_CONTACTS);
        }
        // END_INCLUDE(contacts_permission_request)
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == REQUEST_CONTACTS) {
            if (PermissionUtil.verifyPermissions(grantResults)) {

                init();

            } else {

            }

        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void init() {
        serviceIntent = new Intent(this, GetLocationService.class);
        GetLocationService.setLoacationListener(new LocationIntface() {
            @Override
            public void locationStatus(boolean IsSuccess, String error) {
                if (IsSuccess) {
                    String[] split = error.split(",");
                    latLng = new LatLng(Double.valueOf(split[0]), Double.valueOf(split[1]));
                    LogU.e(latLng.toString());
                }
            }
        });
        startService(serviceIntent);
        Intent intent = getIntent();
        document = (DocumentEntity) intent.getSerializableExtra("document");
        findViewById();
        setListener();
        initData();
    }

    private void setListener() {
        btnReceived.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (OnClickutils.isFastDoubleClick()) {
                    return;
                }
                if (TextUtils.isEmpty(etReceivedName.getText().toString()) && TextUtils.isEmpty(imgToBase64)) {
                    Toast.makeText(ActivityDocumentDetailActivity.this, "收件人不可以为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                showDialog(new DialoginOkCallBack() {
                    @Override
                    public void onClickOk(DialogInterface dialog, int which) {

                        receivedOk();
                    }
                }, "是否确认收货?");

            }
        });
        btnReceivedReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (OnClickutils.isFastDoubleClick()) {
                    return;
                }
                if (TextUtils.isEmpty(etReceivedName.getText().toString()) && TextUtils.isEmpty(imgToBase64)) {
                    Toast.makeText(ActivityDocumentDetailActivity.this, "收件人不可以为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                showDialog(new DialoginOkCallBack() {
                    @Override
                    public void onClickOk(DialogInterface dialog, int which) {

                        receivedOkReset();
                    }
                }, "是否上传多张签收图?");
            }
        });
    }

    private void receivedOkReset() {
        String url = Urls.UploadSignupPicture;
        RequestParams params = new RequestParams();
        LoginInfo loginInfo = BaseApplication.loginInfo;
        params.addBodyParameter("sessionId", loginInfo.getSessionId());
        params.addBodyParameter("userId", loginInfo.getUserId() + "");
        params.addBodyParameter("clientName", BaseSettings.CLIENT_NAME);
        params.addBodyParameter("docNumber", document.getDocumentNumber());
        params.addBodyParameter("pictureData", imgToBase64);
        params.addBodyParameter("description", "");
        params.addBodyParameter("isSignUp", true + "");
        params.addBodyParameter("updateRecId", "0");
        getData(url, params, new CallBackString() {
            @Override
            public void httFinsh(String data) {
                if (StrKit.isBlank(data)) {
                    tvInfo.setText("无法获取数据");
                    closeProgressDialog();
                    return;
                }
                SAXParserFactory factory = SAXParserFactory.newInstance();
                SAXParser parser = null;
                try {
                    parser = factory.newSAXParser();
                    RetrueCodeHandler handler = new RetrueCodeHandler();
                    ByteArrayInputStream tInputStringStream = new ByteArrayInputStream(data.getBytes());
                    parser.parse(tInputStringStream, handler);
                    String returnCode = handler.getString();
                    if (returnCode.equals("-1")) {
                        tvInfo.setText(handler.getError());
                    } else if (returnCode.equals("0")) {
                        tvInfo.setText("上传成功");
                    }
                    closeProgressDialog();
                } catch (Exception e) {
                    e.printStackTrace();
                    tvInfo.setText("数据异常");
                    closeProgressDialog();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        stopService(serviceIntent);
        LogU.e("ondestory");
        if (mBitmap != null) {
            mBitmap.recycle();
        }
        super.onDestroy();
    }

    private ProgressDialog progressDialog;

    public void showProgressDialog() {
        if (isFinishing()) {
            return;
        }
        if ((!isFinishing()) && (this.progressDialog == null)) {
            this.progressDialog = new ProgressDialog(this);
        }
        this.progressDialog.setTitle("正在上传图片");
        this.progressDialog.setMessage("请稍后");
        this.progressDialog.setCanceledOnTouchOutside(false);
        this.progressDialog.show();
    }

    public void closeProgressDialog() {
        if (this.progressDialog != null)
            this.progressDialog.dismiss();
    }

    private void receivedOk() {
        final String endPoint = SoapEndpoint.SET_IS_SIGNUP;
        final String soapAction = SoapAction.SET_IS_SIGNUP;
        final LoginInfo loginInfo = BaseApplication.loginInfo;
        String info = SoapInfo.SET_IS_SIGNUP;
        info = info.replace("UserIdValue", loginInfo.getUserId() + "");
        info = info.replace("ClientNameValue", Infos.CLIENTNAME);
        info = info.replace("SessionIdValue", loginInfo.getSessionId());
        info = info.replace("RoleDataValue", "0");
        info = info.replace("documentIdValue", document.getDocumentId() + "");
        info = info.replace("returnUpdateRecordValue", "true");
        info = info.replace("signNameValue", etReceivedName.getText().toString());
        info = info.replace("latValue", latLng.latitude + "");
        info = info.replace("lonValue", latLng.longitude + "");
        if (TextUtils.isEmpty(imgToBase64)) {
            Toast.makeText(ActivityDocumentDetailActivity.this, "还没有图片资源", Toast.LENGTH_SHORT).show();
            return;
        }
        info = info.replace("signUpPictureDataValue", imgToBase64);
        String date = DataFormatUtils.getDate(System.currentTimeMillis());
        info = info.replace("signUpTimeValue", date);
        final String finalInfo = info;
        showProgressDialog();
        MySoapHttpRequest.getString(new CallBackString() {
            @Override
            public void httFinsh(final String data) {
                //LogU.e(data);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (StrKit.isBlank(data)) {
                            tvInfo.setText("无法获取数据");
                            closeProgressDialog();
                            return;
                        }
                        SAXParserFactory factory = SAXParserFactory.newInstance();
                        SAXParser parser = null;
                        try {
                            parser = factory.newSAXParser();
                            RetrueCodeHandler handler = new RetrueCodeHandler();
                            ByteArrayInputStream tInputStringStream = new ByteArrayInputStream(data.getBytes());
                            parser.parse(tInputStringStream, handler);
                            String returnCode = handler.getString();
                            if (returnCode.equals("-1")) {
                                tvInfo.setText(handler.getError());
                            } else if (returnCode.equals("0")) {
                                tvInfo.setText("上传成功");
                            }
                            closeProgressDialog();
                        } catch (Exception e) {
                            e.printStackTrace();
                            tvInfo.setText("数据异常");
                            closeProgressDialog();
                        }
                    }
                });
            }
        }, endPoint, soapAction, finalInfo);
    }

    private void initData() {
        if (document != null) {
            tvName.setText("姓名：" + document.getConsigneeName());
            tvDocumentDetailDocumentNumber.setText("单号：" + document.getDocumentNumber());
            tvDocumentDaetailTel.setText("电话：" + document.getConsigneeTel());
            tvDocumentDaetailAddress1.setText("发货地址：" + document.getAddressLine1());
            tvDocumentDaetailAddress2.setText("收货地址：" + document.getAddressLine2());
            tvDocumentDaetailQuantity.setText("发件数量：" + document.getQuantity());
            tvDocumentDaetailWeight.setText("重量：" + document.getWeight());
            tvDocumentDaetailShippingMode.setText("发货方式：" + document.getShippingMode());
            String shippingStatus = document.getShippingStatus();
            if (shippingStatus.equals("IsReceived")) {
                tvDocumentDaetailShippingStatus.setText("发货状态：" + "已收件");
            } else if (shippingStatus.equals("IsApplied")) {
                tvDocumentDaetailShippingStatus.setText("发货状态：" + "收到请求");
            } else if (shippingStatus.equals("ReceiverAssigned")) {
                tvDocumentDaetailShippingStatus.setText("发货状态：" + "已通知收货员");
            } else if (shippingStatus.equals("IsStowageParts")) {
                tvDocumentDaetailShippingStatus.setText("发货状态：" + " 已预配部分");
            } else if (shippingStatus.equals("IsStowage")) {
                tvDocumentDaetailShippingStatus.setText("发货状态：" + "已预配");
            } else if (shippingStatus.equals("IsShipping")) {
                tvDocumentDaetailShippingStatus.setText("发货状态：" + "已启运");
            } else if (shippingStatus.equals("IsArrived")) {
                tvDocumentDaetailShippingStatus.setText("发货状态：" + "已到目的站");
            } else if (shippingStatus.equals("IsDelivering")) {
                tvDocumentDaetailShippingStatus.setText("发货状态：" + "派件中");
            } else if (shippingStatus.equals("IsSignUp")) {
                tvDocumentDaetailShippingStatus.setText("发货状态：" + "已签收");
            }
        }
    }

    private void findViewById() {
        tvName = (TextView) findViewById(R.id.tvName);
        tvDocumentDetailDocumentNumber = (TextView) findViewById(R.id.tv_document_detail_DocumentNumber);
        tvDocumentDaetailTel = (TextView) findViewById(R.id.tv_document_daetail_tel);
        tvDocumentDaetailAddress1 = (TextView) findViewById(R.id.tv_document_daetail_address1);
        tvDocumentDaetailAddress2 = (TextView) findViewById(R.id.tv_document_daetail_address2);
        tvDocumentDaetailQuantity = (TextView) findViewById(R.id.tv_document_daetail_Quantity);
        tvDocumentDaetailWeight = (TextView) findViewById(R.id.tv_document_daetail_weight);
        tvDocumentDaetailShippingMode = (TextView) findViewById(R.id.tv_document_daetail_shipping_mode);
        tvDocumentDaetailShippingStatus = (TextView) findViewById(R.id.tv_document_daetail_shippingStatus);
        btnReceived = (Button) findViewById(R.id.btn_doc_detail_received);
        btnReceivedReset = (Button) findViewById(R.id.btn_doc_detail_received_reset);
        etReceivedName = (EditText) findViewById(R.id.et_doc_detail_received_name);
        ivPhoto = (ImageView) findViewById(R.id.iv_document_detail_order);
        tvInfo = (TextView) findViewById(R.id.docuemnt_detail_info);
        mFlip = (ImageView) this.findViewById(R.id.head_flip);
        mFlip.setVisibility(View.VISIBLE);
        mFlip.setImageResource(R.drawable.ic_flipper_head_back);
        mFlip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityDocumentDetailActivity.this.finish();
            }
        });

    }

    public void showDialog(final DialoginOkCallBack callBack, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);  //先得到构造器
        builder.setTitle("提示"); //设置标题
        builder.setMessage(message + ""); //设置内容
        //builder.setIcon(R.mipmap.ic_launcher);//设置图标，图片id即可
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() { //设置确定按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //dialog.dismiss(); //关闭dialog
                //Toast.makeText(BaseMapActivity.this, "确认" + which, Toast.LENGTH_SHORT).show();
                callBack.onClickOk(dialog, which);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() { //设置取消按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //Toast.makeText(BaseMapActivity.this, "取消" + which, Toast.LENGTH_SHORT).show();
            }
        });
        //参数都设置完成了，创建并显示出来
        builder.create().show();
    }

    final int TAKE_PICTURE = 1;

    public void photo(View view) {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            myCaptureFile = new File(mFilePath);
            Uri uri = Uri.fromFile(myCaptureFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(intent, TAKE_PICTURE);
        } else {
            Toast.makeText(getApplicationContext(), "请确认已经插入SD卡", Toast.LENGTH_LONG).show();
        }
    }

    //超过100kb就信息压缩
    private Bitmap ImageCompressL(Bitmap bitmap) {
        double targetwidth = Math.sqrt(100.00 * 1000);
        if (bitmap.getWidth() > targetwidth || bitmap.getHeight() > targetwidth) {
            // 创建操作图片用的matrix对象
            Matrix matrix = new Matrix();
            // 计算宽高缩放率
            double x = Math.max(targetwidth / bitmap.getWidth(), targetwidth
                    / bitmap.getHeight());
            // 缩放图片动作
            matrix.postScale((float) x, (float) x);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                    bitmap.getHeight(), matrix, true);
        }
        return bitmap;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            //Bitmap bitmap=ImageCompressL(BitmapFactory.decodeFile(ActivityDocumentDetailActivity.mFilePath));
            //压缩成图片
            mBitmap = BitmapUtils.getBitmap(mFilePath);
            saveBitmap2file(mBitmap, mFilePath);
            imgToBase64 = ReadImgToBinary2.imgToBase64(ActivityDocumentDetailActivity.mFilePath, mBitmap, "");
           /* Bitmap bitmap1 = BitmapFactory.decodeFile(ActivityDocumentDetailActivity.mFilePath);
            bitmap=ColorToGray(bitmap1);*/
            ivPhoto.setImageBitmap(mBitmap);
            ivPhoto.setClickable(true);
            ivPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(ActivityDocumentDetailActivity.this, PhotoShowActivity.class));
                }
            });
        }
    }

    public Bitmap ColorToGray(Bitmap myBitmap) {
        // Create new array
        int width = myBitmap.getWidth();
        int height = myBitmap.getHeight();
        int[] pix = new int[width * height];
        myBitmap.getPixels(pix, 0, width, 0, 0, width, height);

        // Apply pixel-by-pixel change
        int index = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int r = (pix[index] >> 16) & 0xff;
                int g = (pix[index] >> 8) & 0xff;
                int b = pix[index] & 0xff;
                int gray = (int) (0.3 * r + 0.59 * g + 0.11 * b);
                r = gray;
                g = gray;
                b = gray;
                pix[index] = 0xff000000 | (r << 16) | (g << 8) | b;
                index++;
            } // x
        } // y
        // Change bitmap to use new array
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        bitmap.setPixels(pix, 0, width, 0, 0, width, height);
        myBitmap = null;
        pix = null;
        return bitmap;
    }

    //保存成本地实例化文件
    static boolean saveBitmap2file(Bitmap bmp, String filename) {
        Bitmap.CompressFormat format = Bitmap.CompressFormat.JPEG;
        int quality = 50;
        OutputStream stream = null;
        try {
            stream = new FileOutputStream(filename);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return bmp.compress(format, quality, stream);
    }

    private int mDegree = 90;

    public void rotate(View view) {
        if (mBitmap == null) {
            Toast.makeText(ActivityDocumentDetailActivity.this, "您还没有拍摄签收图", Toast.LENGTH_SHORT).show();
            return;
        }
        mBitmap = rotateBitmap(this, mBitmap, mDegree);
        saveBitmap2file(mBitmap, mFilePath);
        imgToBase64 = ReadImgToBinary2.imgToBase64(ActivityDocumentDetailActivity.mFilePath, mBitmap, "");
        ivPhoto.setImageBitmap(mBitmap);
    }

    private Bitmap rotateBitmap(Context context, Bitmap bitmap, float degree) {
        Matrix matrix = new Matrix();
        matrix.reset();
        matrix.setRotate(degree, bitmap.getWidth() / 2, bitmap.getHeight() / 2);
        Bitmap bitmapr = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return bitmapr;
    }
}
