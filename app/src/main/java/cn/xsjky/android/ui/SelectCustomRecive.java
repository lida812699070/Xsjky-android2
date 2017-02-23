package cn.xsjky.android.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import cn.xsjky.android.BaseApplication;
import cn.xsjky.android.BaseSettings;
import cn.xsjky.android.R;
import cn.xsjky.android.adapter.AdapterSelectcustomReciveAdapter;
import cn.xsjky.android.constant.SoapAction;
import cn.xsjky.android.constant.SoapEndpoint;
import cn.xsjky.android.constant.SoapInfo;
import cn.xsjky.android.model.CustomReceivers;
import cn.xsjky.android.model.LoginInfo;
import cn.xsjky.android.model.Recivicer;
import cn.xsjky.android.model.SendPeroson;
import cn.xsjky.android.util.GetCustomerReceiversXmlparser;
import cn.xsjky.android.util.LogU;
import cn.xsjky.android.util.MySoap;
import cn.xsjky.android.util.RetruenUtils;

public class SelectCustomRecive extends BaseActivity {

    private List<Recivicer> list;
    private String customId;
    private PullToRefreshListView listView;
    private AdapterSelectcustomReciveAdapter<Recivicer> adapter;
    private ImageView mFlip;
    private TextView mTitle;
    private EditText mEtInfos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_custom_recive);
        findViewById();
        customId = getIntent().getStringExtra("customerId");
        getRecives();
        mEtInfos.setText(BaseApplication.getApplication().getToCity());
    }

    private void findViewById() {
        mFlip = (ImageView) this.findViewById(R.id.head_flip);
        mFlip.setVisibility(View.VISIBLE);
        mFlip.setImageResource(R.drawable.ic_flipper_head_back);
        mFlip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTitle = (TextView) this.findViewById(R.id.head_title);
        mTitle.setText("选择收件人");
        listView = (PullToRefreshListView) findViewById(R.id.lv_activitySelectCustomRecive);
        list = new ArrayList<>();
        adapter = new AdapterSelectcustomReciveAdapter<>(this, list);
        listView.setAdapter(adapter);
        mEtInfos = (EditText) findViewById(R.id.et_customAc_selectInfo);
        mEtInfos.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString())){
                    getRecives();
                    return;
                }
                List<Recivicer> recivicers = null;
                try {
                    recivicers = DataSupport.where("ReceiverName like " + "\'%" + s.toString() + "%\'" + " or Address like " + "\'%" + s.toString() + "%\'" + " or ContactPerson like '%" + s.toString() + "%'").find(Recivicer.class);
                } catch (Exception e) {
                    LogU.e("报错===" + e.getMessage());
                }
                if (recivicers == null || recivicers.size() == 0)
                    LogU.e("没有数据");
                else {
                    list.clear();
                    list.addAll(recivicers);
                    adapter.notifyDataSetChanged();
                }
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BaseApplication.customReceivers = list.get(position - 1);
                Intent intent = new Intent();
                intent.setAction(NewActivity.ACTIONRECIVR);
                sendBroadcast(intent);
                finish();
            }
        });
    }

    private void getRecives() {
        List<Recivicer> sendPerosonsList = DataSupport.where("ReceiverId = ?", customId).find(Recivicer.class);
        list.clear();
        list.addAll(sendPerosonsList);
        adapter.notifyDataSetChanged();
        /*final LoginInfo loginInfo = BaseApplication.loginInfo;
        String endPoint = SoapEndpoint.SET_IS_SIGNUP;
        final MySoap transport = new MySoap(endPoint);
        String info = SoapInfo.GetCustomerReceivers;
        info = info.replace("UserIdValue", loginInfo.getUserId() + "");
        info = info.replace("ClientNameValue", BaseSettings.CLIENT_NAME);
        info = info.replace("SessionIdValue", loginInfo.getSessionId());
        info = info.replace("RoleDataValue", "0");
        info = info.replace("customerIdValue", customId);
        info = info.replace("pageNuberValue", page + "");
        transport.setinfo(info);
        transport.debug = true;
        myTask = new MyTask();
        myTask.execute(transport);*/
    }
/*
    private class MyTask extends AsyncTask<MySoap, Integer, String> {

        //onPreExecute方法用于在执行后台任务前做一些UI操作
        @Override
        protected void onPreExecute() {
            showProgressDialog();
        }

        //doInBackground方法内部执行后台任务,不可在此方法内修改UI
        @Override
        protected String doInBackground(MySoap... params) {
            final SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            // 设置是否调用的是dotNet开发的WebService
            envelope.dotNet = true;
            try {
                final String soapAction = SoapAction.GetCustomerReceivers;
                final String call = params[0].call(soapAction, envelope, null, "");
                return call;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        //onProgressUpdate方法用于更新进度信息
        @Override
        protected void onProgressUpdate(Integer... progresses) {

        }

        //onPostExecute方法用于在执行完后台任务后更新UI,显示结果
        @Override
        protected void onPostExecute(String result) {
            GetCustomerReceiversXmlparser receiversXmlparser = RetruenUtils.getReturnInfo(result, new GetCustomerReceiversXmlparser());
            assert receiversXmlparser != null;
            list.addAll(receiversXmlparser.getList());
            adapter.notifyDataSetChanged();
            listView.onRefreshComplete();
            closeProgressDialog();
        }

        //onCancelled方法用于在取消执行中的任务时更改UI
        @Override
        protected void onCancelled() {
            closeProgressDialog();
            listView.onRefreshComplete();
        }
    }*/
}
