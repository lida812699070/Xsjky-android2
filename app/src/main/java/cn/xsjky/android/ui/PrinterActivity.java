package cn.xsjky.android.ui;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import cn.xsjky.android.BaseApplication;
import cn.xsjky.android.R;
import cn.xsjky.android.model.Document;
import cn.xsjky.android.util.BarcodeUtils;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PrinterActivity extends Activity {
	private ImageView mFilp;
	private TextView mTitle;
	private LinearLayout mHeadLayout;
	private TextView mShipperName;
	private TextView mShipperPhone;
	private TextView mShipperAddress;
	private TextView mConsigneeName;
	private TextView mConsigneePhone;
	private TextView mConsigneeAddress;
	private TextView mRemarks;
	private TextView mQuantity;
	private TextView mProduct;
	private TextView mReceiverPay;
	private TextView mCollecting;
	private TextView mInsuranceAmt;
	private TextView mCreateTime;
	private ImageView mNumber;
	private TextView mNumberTxt;
	
	private Document mDocument;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_printer);
		mDocument = BaseApplication.getApplication().getDocument();
		findViewById();
		init();
	}
	
	private void findViewById(){
		mFilp = (ImageView)this.findViewById(R.id.head_flip);
		mTitle = (TextView)this.findViewById(R.id.head_title);
		mHeadLayout = (LinearLayout)this.findViewById(R.id.printer_layout);
		mShipperName = (TextView)this.findViewById(R.id.printer_shipper_name);
		mShipperPhone = (TextView)this.findViewById(R.id.printer_shipper_phone);
		mShipperAddress = (TextView)this.findViewById(R.id.printer_shipper_address);
		mConsigneeName = (TextView)this.findViewById(R.id.printer_consignee_name);
		mConsigneePhone = (TextView)this.findViewById(R.id.printer_consignee_phone);
		mConsigneeAddress = (TextView)this.findViewById(R.id.printer_consignee_address);
		mRemarks = (TextView)this.findViewById(R.id.printer_remarks);
		mQuantity = (TextView)this.findViewById(R.id.printer_quantity);
		mProduct = (TextView)this.findViewById(R.id.printer_product);
		mReceiverPay = (TextView)this.findViewById(R.id.printer_receiverPay);
		mCollecting = (TextView)this.findViewById(R.id.printer_collecting);
		mInsuranceAmt = (TextView)this.findViewById(R.id.printer_insuranceAmt);
		mCreateTime = (TextView) this.findViewById(R.id.printer_createtime);
		mNumber = (ImageView) this.findViewById(R.id.printer_number_img);
		mNumberTxt = (TextView) this.findViewById(R.id.printer_number_text);
	}
	
	private void init(){
		mFilp.setVisibility(View.VISIBLE);
		mFilp.setImageResource(R.drawable.ic_flipper_head_back);
		mTitle.setText("打印标签");
		mShipperName.setText(mDocument.getShipperContactName());
		mShipperPhone.setText(mDocument.getShipperAreaCode() + mDocument.getShipperPhoneNumber());
		mShipperAddress.setText(mDocument.getShipperAddress().getProvince()
				+ mDocument.getShipperAddress().getDistrict()
				+ mDocument.getShipperAddress().getCity()
				+ mDocument.getShipperAddress().getAddress());
		
		mConsigneeName.setText(mDocument.getConsigneeContactPerson());
		mConsigneePhone.setText(mDocument.getConsigneeAreaCode() + mDocument.getConsigneePhoneNumber());
		mConsigneeAddress.setText(mDocument.getConsigneeAddress().getProvince()
				+ mDocument.getConsigneeAddress().getDistrict()
				+ mDocument.getConsigneeAddress().getCity()
				+ mDocument.getConsigneeAddress().getAddress());
		
		mRemarks.setText(mDocument.getRemarks());
		mQuantity.setText(String.valueOf(mDocument.getQuantity()));
		mProduct.setText(mDocument.getProductName());
		if(mDocument.getBalanceMode().equals("ReceiverPay"))
			mReceiverPay.setText(String.valueOf(mDocument.getCarriage()));
		else
			mReceiverPay.setText("0");
		mCollecting.setText("0");
		mInsuranceAmt.setText(String.valueOf(mDocument.getInsuranceAmt()));
		SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日"); 
		try {
			Date date = new SimpleDateFormat("yyyy-MM-dd").parse(mDocument.getCreateTime());
			mCreateTime.setText(format.format(date));
		} catch (ParseException e) {
			mCreateTime.setText("");
		} 
		WindowManager wm = (WindowManager)this.getSystemService(Context.WINDOW_SERVICE);

		int width = wm.getDefaultDisplay().getWidth();
		Bitmap codeBitmap = BarcodeUtils.creatBarcode(this, mDocument.getDocumentNumber(), width / 2,width / 4, false);
		mNumber.setImageBitmap(codeBitmap);
		mNumberTxt.setText(mDocument.getDocumentNumber());
	}

}
