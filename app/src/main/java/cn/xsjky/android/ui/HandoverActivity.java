package cn.xsjky.android.ui;

import java.util.ArrayList;
import java.util.List;

import cn.xsjky.android.BaseApplication;
import cn.xsjky.android.BaseSettings;
import cn.xsjky.android.R;
import cn.xsjky.android.http.HttpCallback;
import cn.xsjky.android.model.Document;
import cn.xsjky.android.model.HandOverItem;
import cn.xsjky.android.model.HandOverRecord;
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
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class HandoverActivity extends Activity {
	private ImageView mFilp;
	private TextView mTitle;
	private ListView mListView;
	private Button mActorBtn;
	private TextView mActorCount;
	private EditText mToEmployee;
	private Context mContext;
	private HandoverAdapter mAdapter;
	private List<Document> mDocumentList;
	private List<HandOverItem> mHandOverItemList;
	private HandOverRecord mHandOverRecord;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_handover);
		mContext = this;
		mFilp = (ImageView)this.findViewById(R.id.head_flip);
		mTitle = (TextView)this.findViewById(R.id.head_title);
		mListView = (ListView) this.findViewById(R.id.handover_listview);
		mActorBtn = (Button) this.findViewById(R.id.handover_actor_btn);
		mActorCount = (TextView) this.findViewById(R.id.handover_actor_count);
		mToEmployee = (EditText) this.findViewById(R.id.handover_toemployee);
		setListViewHeightBasedOnChildren(mListView);
		setListener();
		initHandOverRecord();
	}
	
	private void initHandOverRecord(){
		String templet = TempletUtil.render(BaseSettings.NewHandoverRecord_TEMPLET, BaseApplication.getApplication().getSecurityInfo());
		BaseApplication.getApplication().getHttpPro().postAsync(templet, BaseSettings.NewHandoverRecord_ACTION, new HttpCallback(){

			@Override
			public void onHttpStart() {
				setHandler(SHOW_LOADING, "");
			}

			@Override
			public void onHttpFinish(String data) {
				if(StrKit.isBlank(data)){
					setHandler(SHOW_INFO, "请求数据失败!");
					setHandler(HIDE_LOADING, "");
					return;
				}
				String prefix = "/soap:Envelope/soap:Body/NewHandoverRecordResponse/NewHandoverRecordResult/Value/";
				mHandOverRecord = HandOverRecord.parserXml(data, prefix);
				//mAdapter.notifyDataSetChanged();
				setHandler(HIDE_LOADING, "");
				if(mHandOverRecord == null){
					setHandler(SHOW_INFO, "获取新交接记录失败!");
					HandoverActivity.this.finish();
				}
				init();
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
	
	private void init(){
		String templet = TempletUtil.render(BaseSettings.GetEmployeeDeliverableDocuments_TEMPLET, BaseApplication.getApplication().getSecurityInfo());
		//XmlTask.parserXml(templet, BaseSettings.GETAPPLYDOCUMENTS_ACTION, new HttpCallback(){
		BaseApplication.getApplication().getHttpPro().postAsync(templet, BaseSettings.GetEmployeeDeliverableDocuments_ACTION, new HttpCallback(){

			@Override
			public void onHttpStart() {
				setHandler(SHOW_LOADING, "");
			}

			@Override
			public void onHttpFinish(String data) {
				//Log.e("data", data);
				if(StrKit.isBlank(data)){
					setHandler(SHOW_INFO, "请求数据失败!");
					setHandler(HIDE_LOADING, "");
					return;
				}
				String prefix = "/soap:Envelope/soap:Body/GetEmployeeDeliverableDocumentsResponse/GetEmployeeDeliverableDocumentsResult/ReturnList/";
				mDocumentList = Document.parserXmlList(data, prefix);
				mHandOverItemList = new ArrayList<HandOverItem>();
				for(int i = 0; i < mDocumentList.size(); i++){
					HandOverItem hi = new HandOverItem();
					hi.setHandOverRecord("0");
					hi.setRecordId("0");
					hi.setDocumentId(mDocumentList.get(i).getRecordId());
					hi.setQuantity(mDocumentList.get(i).getQuantity());
					hi.setWeight(mDocumentList.get(i).getWeight());
					hi.setVolumn(mDocumentList.get(i).getVolumn());
					mHandOverItemList.add(hi);
				}
				mAdapter = new HandoverAdapter();
				mListView.setAdapter(mAdapter);
				mAdapter.notifyDataSetChanged();
				setListViewHeightBasedOnChildren(mListView);
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
				LoadingDialog.show(HandoverActivity.this, false, false);
				break;
			case HIDE_LOADING:
				LoadingDialog.dismiss();
				break;
			case SHOW_INFO:
				Toast.makeText(HandoverActivity.this, (String)msg.obj, Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};
	
	private void setListener(){
		mFilp.setVisibility(View.VISIBLE);
		mFilp.setImageResource(R.drawable.ic_flipper_head_back);
		mTitle.setText("批量转出");
		mFilp.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				HandoverActivity.this.finish();
			}
		});
	}

	
	class HandoverAdapter extends BaseAdapter{
		private LayoutInflater inflater;
		private ViewHolder mHolder;
		
		public HandoverAdapter(){
			inflater = LayoutInflater.from(mContext);
		}

		@Override
		public int getCount() {
			return mHandOverItemList.size();
		}

		@Override
		public Object getItem(int position) {
			return mHandOverItemList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView == null){
				convertView = inflater.inflate(R.layout.list_item_handover, null);
				mHolder = new ViewHolder();
				mHolder.mNumber = (TextView) convertView.findViewById(R.id.handover_item_num);
				mHolder.mQuantity = (TextView) convertView.findViewById(R.id.handover_item_quantity);
				mHolder.mWeight = (TextView) convertView.findViewById(R.id.handover_item_weight);
				mHolder.mVolumn = (TextView) convertView.findViewById(R.id.handover_item_volumn);
				convertView.setTag(mHolder);
			}else
				mHolder = (ViewHolder) convertView.getTag();
			HandOverItem h = mHandOverItemList.get(position);
			mHolder.mNumber.setText(h.getDocumentId());
			mHolder.mQuantity.setText(String.valueOf(h.getQuantity()));
			mHolder.mWeight.setText(String.valueOf(h.getWeight()));
			mHolder.mVolumn.setText(String.valueOf(h.getVolumn()));
			return convertView;
		}
		
		class ViewHolder{
			TextView mNumber;
			TextView mQuantity;
			TextView mWeight;
			TextView mVolumn;
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
}
