package cn.xsjky.android.adapter;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapSerializationEnvelope;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import cn.xsjky.android.BaseApplication;
import cn.xsjky.android.BaseSettings;
import cn.xsjky.android.R;
import cn.xsjky.android.constant.SoapAction;
import cn.xsjky.android.constant.SoapEndpoint;
import cn.xsjky.android.constant.SoapInfo;
import cn.xsjky.android.model.LoginInfo;
import cn.xsjky.android.model.MyHandOverRecord;
import cn.xsjky.android.ui.IntoDocumentsActivty;
import cn.xsjky.android.util.CallBackString;
import cn.xsjky.android.util.LogU;
import cn.xsjky.android.util.MySoap;
import cn.xsjky.android.util.RetrueCodeHandler;
import cn.xsjky.android.util.RetruenUtils;

public class ItemIntoDocLvAdapter<T> extends BaseAdapter {

    private List<T> objects = new ArrayList<T>();

    private Context context;
    private LayoutInflater layoutInflater;

    public ItemIntoDocLvAdapter(Context context, List<T> objects) {
        this.context = context;
        this.objects = objects;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public T getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_into_doc_lv, null);
            convertView.setTag(new ViewHolder(convertView));
        }
        initializeViews((T) getItem(position), (ViewHolder) convertView.getTag());
        return convertView;
    }

    private void initializeViews(T object, ViewHolder holder) {
        final MyHandOverRecord overRecord = (MyHandOverRecord) object;
        holder.tvDestination.setText("目的地："+overRecord.getListItem().get(0).getToCity());
        holder.tvPoll.setText("票数："+overRecord.getListItem().size());
        String isReceived = overRecord.getIsReceived();
        if (isReceived.equals("true")){
            holder.tvState.setText("是否已接收："+"已接收");
        }else {
            holder.tvState.setText("是否已接收：" + "未接收");
        }
        holder.tvTime.setText("时间："+overRecord.getIssueTime());
        holder.btnRevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                receiveOk(overRecord);
            }
        });
    }

    private void receiveOk(MyHandOverRecord overRecord) {
        //TODO 确认接收接口
        final LoginInfo loginInfo = BaseApplication.loginInfo;
        String endPoint = SoapEndpoint.Handovers_EndPoint;
        final String soapAction = SoapAction.ConfirmReceived;
        final MySoap transport = new MySoap(endPoint);
        String info = SoapInfo.ConfirmReceived;
        info = info.replace("userIdValue", loginInfo.getUserId() + "");
        info = info.replace("clientNameValue", BaseSettings.CLIENT_NAME);
        info = info.replace("sessionIdValue", loginInfo.getSessionId());
        info = info.replace("returnValueValue", ""+false);
        info = info.replace("handoverIdValue", overRecord.getRecordId());
        if (TextUtils.isEmpty(BaseApplication.userBindTool) && !TextUtils.isEmpty(BaseApplication.userOwnNetwork)){
            info = info.replace("receiveTruckValue", "");
            info = info.replace("receiveNetworkValue", BaseApplication.userOwnNetwork);
        }else if (!TextUtils.isEmpty(BaseApplication.userBindTool) && !TextUtils.isEmpty(BaseApplication.userOwnNetwork)){
            if (IntoDocumentsActivty.IsCarFlag){
                info = info.replace("receiveTruckValue", BaseApplication.userBindTool);
                info = info.replace("receiveNetworkValue", "");
            }else {
                info = info.replace("receiveTruckValue", "");
                info = info.replace("receiveNetworkValue", BaseApplication.userOwnNetwork);
            }
        }else {
            Toast.makeText(context, "无网点，无货车", Toast.LENGTH_SHORT).show();
            return;
        }
        List<String> actorsList = overRecord.getDownLoadActors().getList();
        String actorsValue="";
        for (int i = 0; i < actorsList.size(); i++) {
            String[] split = actorsList.get(i).split(",");
            actorsValue+="<string>" +split[0] + "</string>";
        }
        info = info.replace("actorsValue", actorsValue);
        transport.setinfo(info);
        transport.debug = true;
        new Thread() {
            @Override
            public void run() {
                final SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

                // 设置是否调用的是dotNet开发的WebService
                envelope.dotNet = true;
                try {
                    final String call = transport.call(soapAction, envelope, null, "");
                    RetrueCodeHandler handler = RetruenUtils.getReturnInfo(call, new RetrueCodeHandler());
                    if (handler!=null && handler.getString().equals("0")){
                        final IntoDocumentsActivty context = (IntoDocumentsActivty) ItemIntoDocLvAdapter.this.context;
                        context.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context,"提交成功",Toast.LENGTH_SHORT).show();
                                if (callBackString!=null){
                                    callBackString.httFinsh("");
                                }
                            }
                        });
                    }else if (handler!=null && handler.getString().equals("-1")){
                        Toast.makeText(context,handler.getError(),Toast.LENGTH_SHORT).show();
                    }
                    LogU.e(call);
                } catch (Exception e) {
                    e.printStackTrace();
                    final Activity context = (Activity) ItemIntoDocLvAdapter.this.context;
                    context.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "提交失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }.start();
    }
    private CallBackString callBackString;
    public void setCallBack(CallBackString callBack){
        this.callBackString=callBack;
    }
    protected class ViewHolder {
        private TextView tvTime;
        private TextView tvDestination;
        private TextView tvPoll;
        private TextView tvState;
        private Button btnRevice;

        public ViewHolder(View view) {
            tvTime = (TextView) view.findViewById(R.id.tv_time);
            tvDestination = (TextView) view.findViewById(R.id.tv_destination);
            tvPoll = (TextView) view.findViewById(R.id.tv_poll);
            tvState = (TextView) view.findViewById(R.id.tv_state);
            btnRevice = (Button) view.findViewById(R.id.btn_revice);
        }
    }
}
