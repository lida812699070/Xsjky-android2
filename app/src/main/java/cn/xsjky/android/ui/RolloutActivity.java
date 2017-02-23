package cn.xsjky.android.ui;

import java.util.List;

import cn.xsjky.android.BaseApplication;
import cn.xsjky.android.BaseSettings;
import cn.xsjky.android.R;
import cn.xsjky.android.http.HttpCallback;
import cn.xsjky.android.model.Document;
import cn.xsjky.android.util.StrKit;
import cn.xsjky.android.util.TempletUtil;
import cn.xsjky.android.weiget.LoadingDialog;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class RolloutActivity extends Activity{
	private ListView mListView;
	private ImageView mFlip;
	private TextView mTitle;
	private Context mContext;
	private ReceiveAdapter mAdapter;
	private List<Document> mDocumentList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_list);
		mContext = this;
		findViewById();
		init();
	}
	
	private void init(){
		String templet = TempletUtil.render(BaseSettings.NewHandoverRecord_TEMPLET, BaseApplication.getApplication().getSecurityInfo());
		//XmlTask.parserXml(templet, BaseSettings.GETAPPLYDOCUMENTS_ACTION, new HttpCallback(){
		BaseApplication.getApplication().getHttpPro().postAsync(templet, BaseSettings.NewHandoverRecord_ACTION, new HttpCallback(){

			@Override
			public void onHttpStart() {
				setHandler(SHOW_LOADING, "");
			}

			@Override
			public void onHttpFinish(String data) {
				Log.e("data", data);
				if(StrKit.isBlank(data)){
					setHandler(SHOW_INFO, "请求数据失败!");
					setHandler(HIDE_LOADING, "");
					return;
				}
				/*mDocumentList = Document.parserXmlList(data);
				mAdapter = new ReceiveAdapter();
				mListView.setAdapter(mAdapter);
				mAdapter.notifyDataSetChanged();*/
				Log.e("data",data);
				//mAdapter.notifyDataSetChanged();
				setHandler(HIDE_LOADING, "");
			}

			@Override
			public void onHttpError(String msg) {
				setHandler(SHOW_INFO, msg);
				setHandler(HIDE_LOADING, "");
			}

			@Override
			public void onHttpEnd() {}
			
		});
	}
	
	private void findViewById(){
		mFlip = (ImageView) this.findViewById(R.id.head_flip);
		mFlip.setVisibility(View.VISIBLE);
		mFlip.setImageResource(R.drawable.ic_flipper_head_back);
		mFlip.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				RolloutActivity.this.finish();
			}
		});
		mTitle = (TextView) this.findViewById(R.id.head_title);
		mTitle.setText("批量转出");
		mListView = (ListView) this.findViewById(R.id.list_listview);
		mListView.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
			}
		});
	}
	
	private void setHandler(int code, Object obj){
		Message msg = new Message();
		msg.what = code;
		msg.obj = obj;
		handler.sendMessage(msg);
	}
	
	private final int SHOW_LOADING = 1;
	private final int HIDE_LOADING = 2;
	private final int SHOW_INFO = 3;
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case SHOW_LOADING:
				LoadingDialog.show(RolloutActivity.this, false, false);
				break;
			case HIDE_LOADING:
				LoadingDialog.dismiss();
				break;
			case SHOW_INFO:
				Toast.makeText(RolloutActivity.this, (String)msg.obj, Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};
	
	private class ReceiveAdapter extends BaseAdapter{
		private LayoutInflater inflater;
		private ViewHolder mHolder;
		
		public ReceiveAdapter(){
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
			if(convertView == null){
				convertView = inflater.inflate(R.layout.list_item_deliverable, null);
				mHolder = new ViewHolder();
				mHolder.mName = (TextView) convertView.findViewById(R.id.tvConsigneeTel);
				mHolder.mPhone = (TextView) convertView.findViewById(R.id.tvAddressLine2);
				mHolder.mAddress = (TextView) convertView.findViewById(R.id.tvConsigneeTel);
				convertView.setTag(mHolder);
			}else
				mHolder = (ViewHolder) convertView.getTag();
			Document d = mDocumentList.get(position);
			mHolder.mName.setText(d.getConsigneeContactPerson());
			mHolder.mPhone.setText(d.getConsigneeAreaCode() + d.getConsigneePhoneNumber());
			if(d.getConsigneeAddress() != null)
			mHolder.mAddress.setText(d.getConsigneeAddress().getProvince()
					+ d.getConsigneeAddress().getCity()
					+ d.getConsigneeAddress().getDistrict()
					+ d.getConsigneeAddress().getAddress());
			return convertView;
		}
		
		class ViewHolder{
			TextView mName;
			TextView mPhone;
			TextView mAddress;
		}
		
	};

}
