package cn.xsjky.android.ui;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.http.RequestParams;

import org.litepal.crud.DataSupport;

import java.lang.ref.WeakReference;
import java.util.List;

import cn.xsjky.android.BaseApplication;
import cn.xsjky.android.BaseSettings;
import cn.xsjky.android.R;
import cn.xsjky.android.http.Urls;
import cn.xsjky.android.model.Company;
import cn.xsjky.android.model.Consignee;
import cn.xsjky.android.model.Recivicer;
import cn.xsjky.android.model.SendPeroson;
import cn.xsjky.android.model.SynchronizeData;
import cn.xsjky.android.util.CallBackString;
import cn.xsjky.android.util.DateFormatUtils;
import cn.xsjky.android.util.LogU;
import cn.xsjky.android.util.RetruenUtils;
import cn.xsjky.android.util.SPUtils;
import cn.xsjky.android.util.SynchronizeDataXmlparser;

public class SynDataActivity extends BaseActivity implements View.OnClickListener {

    private TextView mTitle;
    private ImageView mBack;
    private TextView tvSendPersonData;
    private TextView tvSendPersonDataTime;
    private Button btnSendPersonData;
    private long synTime3;
    private String lastSynTime3;
    private long synTime2;
    private String lastSynTime2;
    private TextView tvRecivePersonData;
    private TextView tvRecivePersonDataTime;
    private Button btnRecivePersonData;
    private MyTask mMyTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_syn_data);
        findViewById();
        setListener();
    }

    private void setListener() {
        btnSendPersonData.setOnClickListener(this);
    }

    private void findViewById() {
        mTitle = (TextView) this.findViewById(R.id.head_title);
        mTitle.setText("同步数据");
        mBack = (ImageView) this.findViewById(R.id.head_flip);
        mBack.setImageResource(R.drawable.ic_flipper_head_back);
        mBack.setVisibility(View.VISIBLE);
        mBack.setOnClickListener(this);
        synTime3 = (long) SPUtils.get(this, "synTime3", (long) 0);
        if (synTime3 == 0)
            lastSynTime3 = "";
        else
            lastSynTime3 = DateFormatUtils.getData(synTime3);
        synTime2 = (long) SPUtils.get(this, "synTime2", (long) 0);

        if (synTime2 == 0)
            lastSynTime2 = "";
        else
            lastSynTime2 = DateFormatUtils.getData(synTime2);

        tvSendPersonData = (TextView) findViewById(R.id.tv_syn_sendPersonData);
        tvSendPersonDataTime = (TextView) findViewById(R.id.tv_syn_sendPersonData_time);
        if (!TextUtils.isEmpty(lastSynTime3)) {
            tvSendPersonDataTime.setText(lastSynTime3);
        }
        btnSendPersonData = (Button) findViewById(R.id.btn_syn_sendPersonData);

        tvRecivePersonData = (TextView) findViewById(R.id.tv_syn_recivePersonData);
        tvRecivePersonDataTime = (TextView) findViewById(R.id.tv_syn_recivePersonData_time);
        if (!TextUtils.isEmpty(lastSynTime2)) {
            tvRecivePersonDataTime.setText(lastSynTime2);
        }
        btnRecivePersonData = (Button) findViewById(R.id.btn_syn_recivePersonData);
        btnRecivePersonData.setOnClickListener(this);
    }

    private int pageNum = 0;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_flip:
                finish();
                break;
            case R.id.btn_syn_sendPersonData:
                SynData(3, pageNum);
                break;
            case R.id.btn_syn_recivePersonData:
                SynData(2, pageNum);
                break;
        }
    }

    private String lastTime = "";

    private void SynData(final int dataId, int page) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("sessionId", BaseApplication.loginInfo.getSessionId());
        params.addBodyParameter("userId", BaseApplication.loginInfo.getUserId() + "");
        params.addBodyParameter("clientName", BaseSettings.CLIENT_NAME);
        params.addBodyParameter("pageNumber", page + "");
        params.addBodyParameter("pageSize", 10 + "");
        params.addBodyParameter("dataId", dataId + "");
        //如果是发件人同步
        if (dataId == 3)
            lastTime = lastSynTime3;
        //收件人同步
        if (dataId == 2)
            lastTime = lastSynTime2;
        //params.addBodyParameter("lastSynchronizedTime", "");
        params.addBodyParameter("lastSynchronizedTime", lastTime);
        params.addBodyParameter("dataFilter", "1");
        showProgressDialog();
        getData(Urls.SynchronizeData, params, new CallBackString() {
            @Override
            public void httFinsh(String data) {
                int size = 0;
                SynchronizeDataXmlparser xmlparser = RetruenUtils.getReturnInfo(data, new SynchronizeDataXmlparser());
                if (xmlparser != null) {
                    SynchronizeData synchronizeData = xmlparser.getUser();
                    //新增或者修改数据  json格式
                    String modifyData = synchronizeData.getModifyData();
                    //更新本地的同步时间
                    if (dataId == 3) {
                        List<SendPeroson> sendPerosons = SendPeroson.arraySendPerosonFromData(modifyData);
                        if (sendPerosons != null && sendPerosons.size() != 0) {
                            SendPeroson.saveList(sendPerosons);
                            saveInCompany(sendPerosons);
                            List<SendPeroson> customs = DataSupport.limit(10).find(SendPeroson.class, true);
                            LogU.e(customs.toString());
                            //删除的数据  json格式
                            String deleteData = synchronizeData.getDeleteData();
                            SPUtils.put(SynDataActivity.this, "synTime3", System.currentTimeMillis());
                            size = sendPerosons.size();
                        }
                        closeProgressDialog();
                        tvSendPersonDataTime.setText("已同步" + size + "条数据");

                    } else if (dataId == 2) {
                        mMyTask = new MyTask();
                        mMyTask.execute(modifyData);
                    }
                }

                /*//如果数据量超过十条再次访问，递归
                if (size >= 10) {
                    pageNum++;
                    SynData(dataId, pageNum);
                }*/
            }
        },true);
    }




    private class MyTask extends AsyncTask<String, Integer, String> {
        private int size=0;
        //onPreExecute方法用于在执行后台任务前做一些UI操作
        @Override
        protected void onPreExecute() {
        }

        //doInBackground方法内部执行后台任务,不可在此方法内修改UI
        @Override
        protected String doInBackground(String... params) {
            List<Recivicer> recivicers = Recivicer.arrayRecivicerFromData(params[0]);
            if (recivicers != null && recivicers.size() != 0) {
                Recivicer.saveList(recivicers);
                List<Recivicer> customs = DataSupport.limit(10).find(Recivicer.class, true);
                LogU.e(customs.toString());
                saveInConsignee(recivicers);
                //删除的数据  json格式
                SPUtils.put(SynDataActivity.this, "synTime2", System.currentTimeMillis());
                size = recivicers.size();
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
            closeProgressDialog();
            tvRecivePersonDataTime.setText("已同步" + size + "条数据");
        }

        //onCancelled方法用于在取消执行中的任务时更改UI
        @Override
        protected void onCancelled() {

        }
    }

    @Override
    protected void onDestroy() {
        if (mMyTask!=null && !mMyTask.isCancelled()){
            mMyTask.onCancelled();
            mMyTask=null;
        }
        super.onDestroy();
    }
//Consignee{id=1, companyName='新世纪', person='李达', city='安徽省,安庆市,枞阳县',
//addressDetail='哈哈哈', areacode='0556', mobileNum='64484848', otherPhone=''}]
    private void saveInConsignee(List<Recivicer> customs) {
        for (int i = 0; i < customs.size(); i++) {
            Recivicer recivicer = customs.get(i);
            Consignee consignee = new Consignee();
            try{
                String[] split = recivicer.getAddress().split(" ", 4);
                consignee.setCity(split[0]+","+split[1]+","+split[2]);
                consignee.setAddressDetail(split[3]);
            }catch (Exception e){
                e.printStackTrace();
            }
            consignee.setTocity(recivicer.getCity());
            consignee.setCompanyName(recivicer.getReceiverName());
            consignee.setShipperName(recivicer.getCustomerName());
            consignee.setPerson(recivicer.getContactPerson());
            consignee.setMobileNum(recivicer.getMobile());
            consignee.setAreacode(recivicer.getAreaCode());
            consignee.setOtherPhone(recivicer.getTelNumber());
            if (!DataSupport.isExist(Consignee.class, "person = ?", recivicer.getContactPerson()) && !DataSupport.isExist(Consignee.class, "companyName = ?", recivicer.getReceiverName())){
                consignee.save();
            }
        }
    }

    private void saveInCompany(List<SendPeroson> sendPerosons) {
        for (int i = 0; i < sendPerosons.size(); i++) {
            SendPeroson sendPeroson = sendPerosons.get(i);
            Company company = new Company();
            company.setName(sendPeroson.getCompanyName());
            company.setTel(sendPeroson.getTel() + "");
            company.setOtherTel(sendPeroson.getMobileNumber());
            //company.setProvince(sendPeroson.get);
            company.setPeople(sendPeroson.getContactName());
            company.setAddress(sendPeroson.getAddress());
            //company.setAreaCode(sendPeroson.get);
            if (!DataSupport.isExist(Company.class, "people = ?", sendPeroson.getContactName()) && !DataSupport.isExist(Company.class, "name = ?", sendPeroson.getCompanyName())){
                company.save();
            }
        }
    }
}
