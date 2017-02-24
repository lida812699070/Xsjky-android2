package cn.xsjky.android.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.jq.printer.JQPrinter;
import com.jq.printer.Port.PORT_STATE;
import com.jq.printer.esc.ESC;
import com.jq.printer.jpl.Image.IMAGE_ROTATE;
import com.jq.printer.jpl.Page.PAGE_ROTATE;

import java.util.Map;

import cn.xsjky.android.BaseApplication;
import cn.xsjky.android.BaseSettings;
import cn.xsjky.android.R;
import cn.xsjky.android.db.ErrorDocument;
import cn.xsjky.android.db.SqlUtils;
import cn.xsjky.android.http.HttpCallback;
import cn.xsjky.android.model.Address;
import cn.xsjky.android.model.Document;
import cn.xsjky.android.model.PrintDocumentInfo;
import cn.xsjky.android.model.SimpleDocument;
import cn.xsjky.android.util.BarcodeUtils;
import cn.xsjky.android.util.BitmapUtils;
import cn.xsjky.android.util.LogU;
import cn.xsjky.android.util.SPUtils;
import cn.xsjky.android.util.StrKit;
import cn.xsjky.android.util.TempletUtil;
import cn.xsjky.android.util.XmlParserUtil;
import cn.xsjky.android.weiget.LoadingDialog;

public class FreightActivity extends Activity {
    private final static int REQUEST_BT_ENABLE = 0;
    private final static int REQUEST_BT_ADDR = 1;
    private ImageView mFilp;
    private TextView mTitle;
    private LinearLayout mHeadLayout;
    private TextView mConsigneeName;
    private TextView mConsigneePhone;
    private TextView mConsigneeAddress;
    private TextView mQuantity;
    private TextView mFromCity;
    private TextView mToCity;
    private ImageView mNumber;
    private TextView mNumberTxt;
    private TextView mInfo;
    private Button mConnect;
    private Button mPrint;
    private Button mConfirm;
    private EditText mStartPage;
    private Context mContext;
    private Document mDocument;
    private BluetoothAdapter btAdapter = null;
    private JQPrinter printer = null;
    private EditText mEndtPage;
    private String strDocuments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        this.setContentView(R.layout.activity_printer_freight);
        mDocument = BaseApplication.getApplication().getDocument();
        strDocuments = getIntent().getStringExtra("strDocuments");
        if (!TextUtils.isEmpty(strDocuments)) {
            errorDocumentPrint(strDocuments);
        }
        PrintDocumentInfo info = (PrintDocumentInfo) getIntent().getSerializableExtra("data");
        if (info != null)
            mDocument = PrintDocumentInfoTomDcoument(info);

        findViewById();
        init();
        initBluetooth();
    }

    //把打印订单的信息进行缓存避免打印一半死机关机问题
    private void savemDocument(Document document) {
        PrintDocumentInfo info = new PrintDocumentInfo();
        info.setDocumentNumber(document.getDocumentNumber());
        info.setQuantity(document.getQuantity() + "");
        info.setRecordId(document.getRecordId() + "");
        info.setFromCity(document.getFromCity());
        info.setToCity(document.getToCity());
        info.setConsigneeContactPerson(document.getConsigneeContactPerson());
        info.setConsigneePhoneNumber(document.getConsigneePhoneNumber());
        info.setConsigneeAddress(document.getConsigneeAddress().getAddress());
        info.setDistrict(document.getConsigneeAddress().getDistrict());
        info.save();
    }

    //把PrintDocumentInfo转成mDcoument
    private Document PrintDocumentInfoTomDcoument(PrintDocumentInfo info) {
        Document mDocument = new Document();
        mDocument.setDocumentNumber(info.getDocumentNumber());
        mDocument.setQuantity(Integer.valueOf(info.getQuantity()));
        mDocument.setRecordId(Integer.valueOf(info.getRecordId()));
        mDocument.setFromCity(info.getFromCity());
        mDocument.setToCity(info.getToCity());
        mDocument.setConsigneeContactPerson(info.getConsigneeContactPerson());
        mDocument.setConsigneePhoneNumber(info.getConsigneePhoneNumber());
        Address address = new Address();
        address.setAddress(info.getConsigneeAddress());
        address.setDistrict(info.getDistrict());
        mDocument.setConsigneeAddress(address);
        return mDocument;
    }

    private void errorDocumentPrint(String strDocuments) {
        SimpleDocument simpleDocument = SqlUtils.queryErrorDocument(strDocuments);
        mDocument = new Document();
        mDocument.setDocumentNumber(simpleDocument.getDocumentNumber());
        mDocument.setQuantity(Integer.valueOf(simpleDocument.getQuantity()));
        mDocument.setRecordId(Integer.valueOf(simpleDocument.getRecordId()));
        mDocument.setFromCity(simpleDocument.getFromCity());
        mDocument.setToCity(simpleDocument.getToCity());
        mDocument.setConsigneeContactPerson(simpleDocument.getConsigneeContactPerson());
        mDocument.setConsigneePhoneNumber(simpleDocument.getConsigneePhoneNumber());
        mDocument.setConsigneeAddress(simpleDocument.getConsigneeAddress());
    }

    private void initBluetooth() {
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        BaseApplication.getApplication().setmBluetoothAdapter(btAdapter);
        if (btAdapter == null) {
            setInfo(true, "本机没有找到蓝牙硬件或驱动！");
            return;
        }
        if (!btAdapter.isEnabled()) {
            btAdapter.enable();
            setInfo(false, "本地蓝牙已打开");
            if (StrKit.notBlank(BaseApplication.getApplication().getLastDevice())) {
                printer = new JQPrinter(btAdapter, BaseApplication.getApplication().getLastDevice());
                //确定打印机已经连接了
                if (!printer.open(com.jq.printer.JQPrinter.PRINTER_TYPE.ULT113x)) {
                    setInfo(true, "打印机打开失败");
                    return;
                }
                if (!printer.wakeUp()) {
                    return;
                }
                setInfo(false, "成功连接打印机");
            }
        }
    }

    private void setInfo(boolean isError, String info) {
        if (isError)
            info = "<font color=#ff0000>" + info + "</font>";
        else
            info = "<font color=#006600>" + info + "</font>";
        Message msg = new Message();
        msg.what = SHOW_INFO;
        msg.obj = info;
        handler.sendMessage(msg);
    }

    private final int SHOW_INFO = 1;
    private final int SHOW_DIALOG = 2;
    private final int HIDE_DIALOG = 3;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW_INFO:
                    mInfo.setText(Html.fromHtml((String) msg.obj));
                    break;
                case SHOW_DIALOG:
                    LoadingDialog.show(mContext, false, false);
                    break;
                case HIDE_DIALOG:
                    LoadingDialog.dismiss();
                    break;
            }
        }
    };

    private long mLastTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && (event.getAction() == KeyEvent.ACTION_DOWN)) {
            if (System.currentTimeMillis() - mLastTime > 2000) {
                System.out.println(Toast.LENGTH_LONG);
                Toast.makeText(this, "请再按一次返回退出", Toast.LENGTH_LONG).show();
                mLastTime = System.currentTimeMillis();
            } else
                exit();
            return true;

        }
        return super.onKeyDown(keyCode, event);
    }

    //蓝牙按键点击 ,此函数需要在 activity_main.xml文件中注册
    public void onConnectClick(View view) {
        if (btAdapter == null) {
            setInfo(true, "蓝牙未打开");
            return;
        }
        Intent myIntent = new Intent(FreightActivity.this, BtConfigActivity.class);
        startActivityForResult(myIntent, REQUEST_BT_ADDR);
    }

    private void findViewById() {
        mFilp = (ImageView) this.findViewById(R.id.head_flip);
        mTitle = (TextView) this.findViewById(R.id.head_title);
        mHeadLayout = (LinearLayout) this.findViewById(R.id.freight_layout);
        mFromCity = (TextView) this.findViewById(R.id.freight_fromcity);
        mToCity = (TextView) this.findViewById(R.id.freight_tocity);
        mConsigneeName = (TextView) this.findViewById(R.id.freight_consignee_name);
        mConsigneePhone = (TextView) this.findViewById(R.id.freight_consignee_phone);
        mConsigneeAddress = (TextView) this.findViewById(R.id.freight_consignee_address);
        mQuantity = (TextView) this.findViewById(R.id.freight_quantity);
        mNumber = (ImageView) this.findViewById(R.id.freight_number_img);
        mNumberTxt = (TextView) this.findViewById(R.id.freight_number_text);
        mInfo = (TextView) this.findViewById(R.id.freight_info);
        mConnect = (Button) this.findViewById(R.id.freight_connect);
        mPrint = (Button) this.findViewById(R.id.freight_print);
        mConfirm = (Button) this.findViewById(R.id.freight_confirm);
        mStartPage = (EditText) this.findViewById(R.id.freight_startpage);
        mEndtPage = (EditText) this.findViewById(R.id.freight_endpage);
        mEndtPage.setText(mDocument.getQuantity() + "");
        mStartPage.setText("1");
        mStartPage.setOnEditorActionListener(new OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                String str = mStartPage.getText().toString();
                if (StrKit.isBlank(str))
                    mStartPage.setText("1");
                else {
                    int i = Integer.valueOf(str);
                    if (i > mDocument.getQuantity())
                        mStartPage.setText(String.valueOf(mDocument.getQuantity()));
                }
                return false;
            }
        });
        mConfirm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmPrint();
            }
        });
        mConnect.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btAdapter == null)
                    return;
                Intent myIntent = new Intent(FreightActivity.this, BtConfigActivity.class);
                startActivityForResult(myIntent, REQUEST_BT_ADDR);
            }

        });
        mPrint.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (printer == null) {
                    setInfo(true, "请先连接打印机");
                    return;
                }
                if (!getPrinterState()) {
                    return;
                } else {
                    //软件无法判断当前打印的内容是否打印完好，所以需要重新打印当前张。你可以增加一个按钮来决定是打当前张还是打下一张。
                    rePrint = false;
                }
                handler.sendEmptyMessage(SHOW_DIALOG);
                handler.postDelayed(new Runnable() {
                    public void run() {
                        print();
                    }
                }, 200);
            }
                /*
                new AsyncTask<Void, Void, String>(){

					@Override
					protected void onPreExecute() {
						handler.sendEmptyMessage(SHOW_DIALOG);
					}

					@Override
					protected String doInBackground(Void... params) {
						print();
						handler.sendEmptyMessage(HIDE_DIALOG);
						return "";
					}
					
					@Override
					protected void onPostExecute(String result) {
					}

					
				}.execute();
			}*/
        });
    }

    //打印标签完成
    private void confirmPrint() {
        delPrintDocumentInfo(mDocument.getDocumentNumber());
        Map<String, String> map = BaseApplication.getApplication().getSecurityInfo();
        map.put("documentId", String.valueOf(this.mDocument.getRecordId()));
        String templet = TempletUtil.render(BaseSettings.AfterPrintCargoLabel_TEMPLET, map);
        BaseApplication.getApplication().getHttpPro().postAsync(templet, BaseSettings.AfterPrintCargoLabel_ACTION, new HttpCallback() {
            @Override
            public void onHttpStart() {
                handler.sendEmptyMessage(SHOW_DIALOG);
            }

            @Override
            public void onHttpFinish(String data) {
                if (StrKit.isBlank(data)) {
                    setInfo(true, "提交数据失败！");
                    if (!TextUtils.isEmpty(strDocuments)) {
                        ErrorDocument errorDocument = SqlUtils.errorDocumentQuery(mDocument.getDocumentNumber());
                        if (errorDocument != null) {
                            errorDocument.setIsPrint(true);
                            SPUtils.put(FreightActivity.this, strDocuments, true);
                            SqlUtils.ErrorDocumentUpDate(errorDocument.getDocumentNumber(), errorDocument);
                            exit();
                        }
                    }
                    return;
                }
                XmlParserUtil parser = XmlParserUtil.getInstance();
                parser.parse(data);
                String prefix = "/soap:Envelope/soap:Body/AfterPrintCargoLabelResponse/AfterPrintCargoLabelResult/";
                String code = parser.getNodeValue(prefix + "ReturnCode");
                if (StrKit.isBlank(code) || !code.equals("0")) {
                    String error = parser.getNodeValue(prefix + "Error");
                    setInfo(true, error);
                } else {
                    setInfo(false, "打印确认成功");
                    mConfirm.setVisibility(View.GONE);
                    exit();
                }
                handler.sendEmptyMessage(HIDE_DIALOG);
            }

            @Override
            public void onHttpError(String msg) {
                setInfo(true, msg);
                handler.sendEmptyMessage(HIDE_DIALOG);
            }

            @Override
            public void onHttpEnd() {

            }
        });
    }

    private void delPrintDocumentInfo(String documentNumber) {
        int i = PrintDocumentInfo.deleteAll(PrintDocumentInfo.class, "documentNumber = ?", documentNumber);
        LogU.e("删除printdocument" + i);
    }

    private void init() {
        mFilp.setVisibility(View.VISIBLE);
        mFilp.setImageResource(R.drawable.ic_flipper_head_back);
        mTitle.setText("打印标签");
        mFilp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                exit();
            }
        });

        mFromCity.setText(mDocument.getFromCity());
        mToCity.setText(mDocument.getToCity());
        mConsigneeName.setText(mDocument.getConsigneeContactPerson());
        mConsigneePhone.setText(mDocument.getConsigneeAreaCode() + "-" + mDocument.getConsigneePhoneNumber());
        mConsigneeAddress.setText(mDocument.getConsigneeAddress().getDistrict()
                + mDocument.getConsigneeAddress().getAddress());

        mQuantity.setText(String.valueOf(mDocument.getQuantity()) + "/1");
        //SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);

        int width = wm.getDefaultDisplay().getWidth();
        Bitmap codeBitmap = BarcodeUtils.creatBarcode(this, mDocument.getDocumentNumber(), width, width / 8, false);
        mNumber.setImageBitmap(codeBitmap);
        mNumberTxt.setText(mDocument.getDocumentNumber());
    }

    private void exit() {
        if (printer != null) {
            printer.close();
            printer = null;
        }
        if (btAdapter != null) {
            if (btAdapter.isEnabled()) {
                btAdapter.disable();
            }
        }
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_BT_ENABLE) {
            if (resultCode == RESULT_OK) {
                setInfo(false, "蓝牙已打开");
            } else if (resultCode == RESULT_CANCELED) {
                setInfo(true, "不允许蓝牙开启");
                exit();
                return;
            }
        } else if (requestCode == REQUEST_BT_ADDR) {
            if (resultCode == Activity.RESULT_OK) {
                String btDeviceString = data.getStringExtra(BaseSettings.EXTRA_BLUETOOTH_DEVICE_ADDRESS);
                if (btDeviceString != null) {
                    setInfo(false, "已连接:" + data.getStringExtra(BaseSettings.EXTRA_BLUETOOTH_DEVICE_NAME));
                    if (btAdapter.isDiscovering())
                        btAdapter.cancelDiscovery();
                    if (printer != null) {
                        printer.close();
                    }

                    printer = new JQPrinter(btAdapter, btDeviceString);
                    if (!printer.open(com.jq.printer.JQPrinter.PRINTER_TYPE.ULT113x)) {
                        setInfo(true, "打印机打开失败");
                        return;
                    }

                    if (!printer.wakeUp())
                        return;
                    BaseApplication.getApplication().setLastDevice(btDeviceString);
                    setInfo(false, "成功连接打印机");

                    //	Toast.makeText(this, " 找到外部蓝牙设备", Toast.LENGTH_LONG).show();

                    IntentFilter filter = new IntentFilter();
                    filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);//蓝牙断开
                    registerReceiver(mReceiver, filter);
                }
            } else {
                setInfo(false, "选择打印机");
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mReceiver != null) {
            try {
                unregisterReceiver(mReceiver);
            } catch (Exception e) {

            }
        }
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(android.content.Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
                if (printer != null) {
                    if (printer.isOpen)
                        printer.close();
                }
            }
        }

        ;
    };

    boolean rePrint = false;//是否需要重新打印

    private int index = 1;

    private boolean print() {
        savemDocument(mDocument);
        int end = 0;
        if (!TextUtils.isEmpty(mEndtPage.getText().toString())) {
            end = Integer.valueOf(mEndtPage.getText().toString());
        }
        if (end > mDocument.getQuantity()) {
            end = mDocument.getQuantity();
        }
        int pages = Integer.valueOf(mStartPage.getText().toString());
        for (int i = pages; i <= end; i++) {
            BaseApplication.printStatus = false;
            String pageTxt = String.valueOf(mDocument.getQuantity()) + "/" + String.valueOf(i);
            mQuantity.setText(pageTxt);
            BitmapUtils.saveViewBitmap(mHeadLayout);
            printer.jpl.page.start(0, 0, 576, 650, PAGE_ROTATE.x0);

            printer.esc.barcode.code128_auto_printOut(JQPrinter.ALIGN.CENTER,
                    ESC.BAR_UNIT.x4, 56, ESC.BAR_TEXT_POS.BOTTOM,
                    ESC.BAR_TEXT_SIZE.ASCII_12x24, mDocument.getDocumentNumber());
            //printer.jpl.image.drawOut(0, 0, BitmapUtils.convertToBitmap(576, 650), IMAGE_ROTATE.ANGLE_0);
            printer.esc.feedEnter();

            String toCity = mDocument.getToCity();
            LogU.e(toCity);
            printer.esc.text.printOut(JQPrinter.ALIGN.CENTER, ESC.FONT_HEIGHT.x24, false,
                    ESC.TEXT_ENLARGE.HEIGHT_WIDTH_DOUBLE, mDocument.getFromCity() + "-->" + toCity);
            printer.esc.feedEnter();
            printer.esc.text.printOut(JQPrinter.ALIGN.LEFT, ESC.FONT_HEIGHT.x24, false,
                    ESC.TEXT_ENLARGE.HEIGHT_WIDTH_DOUBLE, "件数：" + mDocument.getQuantity() + "/" + i);

            printer.esc.text.printOut(JQPrinter.ALIGN.LEFT, ESC.FONT_HEIGHT.x16, false,
                    ESC.TEXT_ENLARGE.HEIGHT_WIDTH_DOUBLE, "收件人：" + mDocument.getConsigneeContactPerson());

            printer.esc.text.printOut(JQPrinter.ALIGN.LEFT, ESC.FONT_HEIGHT.x16, false,
                    ESC.TEXT_ENLARGE.HEIGHT_WIDTH_DOUBLE, "电话：" + mDocument.getConsigneeAreaCode() + "-" + mDocument.getConsigneePhoneNumber());

            printer.esc.text.printOut(JQPrinter.ALIGN.LEFT, ESC.FONT_HEIGHT.x16, false,
                    ESC.TEXT_ENLARGE.HEIGHT_WIDTH_DOUBLE, "地址：" + mDocument.getConsigneeAddress().getDistrict()
                            + mDocument.getConsigneeAddress().getAddress());
            printer.esc.feedEnter();
            ESC.LINE_POINT[] lines = new ESC.LINE_POINT[1];
            lines[0] = new ESC.LINE_POINT(0, 575);
            printer.esc.graphic.linedrawOut(lines);
            printer.esc.feedEnter();
            printer.esc.text.printOut(JQPrinter.ALIGN.CENTER, ESC.FONT_HEIGHT.x24, false,
                    ESC.TEXT_ENLARGE.HEIGHT_WIDTH_DOUBLE, "新世纪快运");
            printer.esc.feedEnter();
            printer.esc.text.printOut(JQPrinter.ALIGN.CENTER, ESC.FONT_HEIGHT.x24, false,
                    ESC.TEXT_ENLARGE.HEIGHT_WIDTH_DOUBLE, "400-908-9888");

            boolean flag = printer.jpl.page.end();
            //boolean flag = printer.jpl.page.print();
            printer.jpl.feedMarkOrGap(0);//printer.jpl.feedNextLabelEnd(48);//printer.jpl.feedNextLabelBegin();
            if (i == end) {
                BaseApplication.printStatus = true;
            }

            if (!getPrinterState()) {
                BaseApplication.printStatus = false;
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (flag)
                continue;
            int j = 0;
            for (j = 0; j < 10; j++) {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (!printer.getPrinterState(200))//此处的读超时需要算上打印内容的时间。请根据打印内容调整,如果你打印的内容更多，就需要设置更多的时间。
                {
                    BaseApplication.printStatus = false;
                    setInfo(true, "获取打印机状态失败");
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    continue;
                }
                if (printer.printerInfo.isCoverOpen) {
                    BaseApplication.printStatus = false;
                    setInfo(true, "纸仓未关--重新打印");
                    rePrint = true;
                    handler.sendEmptyMessage(HIDE_DIALOG);
                    return true;
                } else if (printer.printerInfo.isNoPaper) {
                    BaseApplication.printStatus = false;
                    setInfo(true, "缺纸--重新打印");
                    rePrint = true;
                    handler.sendEmptyMessage(HIDE_DIALOG);
                    return true;
                }
                if (printer.printerInfo.isPrinting) {
                    setInfo(false, "正在打印");
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    break;
                }
            }

        }
        handler.sendEmptyMessage(HIDE_DIALOG);
        setInfo(false, "打印结束");
        if (mConfirm.isShown() && BaseApplication.printStatus)
            confirmPrint();
        return true;
    }

    private boolean getPrinterState() {
        if (printer.getPortState() != PORT_STATE.PORT_OPEND) {
            setInfo(true, "蓝牙错误");
            return false;
        }

        if (!printer.getPrinterState(3000)) {
            setInfo(true, "获取打印机状态失败");
            return false;
        }

        if (printer.printerInfo.isCoverOpen) {
            setInfo(true, "打印机纸仓盖未关闭");
            return false;
        } else if (printer.printerInfo.isNoPaper) {
            setInfo(true, "打印机缺纸");
            return false;
        }
        return true;
    }
}
