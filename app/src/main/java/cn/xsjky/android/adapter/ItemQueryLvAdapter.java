package cn.xsjky.android.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.lidroid.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

import cn.xsjky.android.BaseApplication;
import cn.xsjky.android.BaseSettings;
import cn.xsjky.android.R;
import cn.xsjky.android.constant.SoapAction;
import cn.xsjky.android.constant.SoapEndpoint;
import cn.xsjky.android.http.Urls;
import cn.xsjky.android.model.Document;
import cn.xsjky.android.model.Infos;
import cn.xsjky.android.model.QueryDocumentEntity;
import cn.xsjky.android.model.WoodenFrame;
import cn.xsjky.android.ui.ApplyDocumentDetailActivity;
import cn.xsjky.android.ui.BaseActivity;
import cn.xsjky.android.ui.MadeFrameActivity;
import cn.xsjky.android.util.CallBackString;
import cn.xsjky.android.util.LogU;
import cn.xsjky.android.util.MySoapHttpRequest;
import cn.xsjky.android.util.RetrueCodeHandler;
import cn.xsjky.android.util.RetruenUtils;
import cn.xsjky.android.util.WoodenFrameXmlparser;

public class ItemQueryLvAdapter<T> extends BaseAdapter {

    private List<T> objects = new ArrayList<T>();

    private Context context;
    private LayoutInflater layoutInflater;
    private BaseActivity mActivity;

    public ItemQueryLvAdapter(Context context, List<T> objects) {
        this.objects = objects;
        this.context = context;
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
            convertView = layoutInflater.inflate(R.layout.item_query_lv, null);
            convertView.setTag(new ViewHolder(convertView));
        }
        initializeViews((T) getItem(position), (ViewHolder) convertView.getTag());
        return convertView;
    }

    private void initializeViews(T object, ViewHolder holder) {
        final QueryDocumentEntity document = (QueryDocumentEntity) object;
        holder.tvDocumentNumber.setText("" + document.getDocumentNumber());
        holder.tvFrameNum.setText("件数：" + document.getQuantity());
        if (TextUtils.isEmpty(document.getConsigneeContactPerson())) {
            holder.tvDocumentCreatorName.setText("接收人：" + "订单尚未接受");
        } else {
            holder.tvDocumentCreatorName.setText("接收人：" + document.getConsigneeContactPerson() + "\n" + "发件人：" + document.getShipperContactName());
        }
        holder.tvDocumentState.setText("状态：" + document.getShippingStatus());
        try{
            String time = document.getCreateTime().replace("T", "  ");
            time = time.substring(0, 17);
            holder.tvDocumentCreatorTime.setText("" + time);
        }catch (Exception e){

        }
        holder.tvqueryWood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity = (BaseActivity) context;
                String url = Urls.GetDocumentWoodenFrames;
                RequestParams params = new RequestParams();
                params.addBodyParameter("sessionId", BaseApplication.loginInfo.getSessionId());
                params.addBodyParameter("userId", BaseApplication.loginInfo.getUserId() + "");
                params.addBodyParameter("clientName", BaseSettings.CLIENT_NAME);
                params.addBodyParameter("documentNumber", document.getDocumentNumber());
                mActivity.getData(url, params, new CallBackString() {
                    @Override
                    public void httFinsh(String data) {
                        WoodenFrameXmlparser returnInfo = RetruenUtils.getReturnInfo(data, new WoodenFrameXmlparser());
                        assert returnInfo != null;
                        ArrayList<WoodenFrame> woodenFrames = returnInfo.getWoodenFrames();
                        Intent intent = new Intent(mActivity, MadeFrameActivity.class);
                        intent.putExtra("data", woodenFrames);
                        intent.putExtra("documentNumber", document.getDocumentNumber());
                        mActivity.startActivity(intent);
                    }
                });
            }
        });
        holder.tvQueryInitapply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity = (BaseActivity) context;
                initApplyDocuemntActivity(document.getDocumentNumber());
            }
        });
    }


    private void initApplyDocuemntActivity(String documentNumber) {
        mActivity.showProgressDialog();
        final String endPoint = SoapEndpoint.SET_IS_SIGNUP;
        final String soapAction = SoapAction.GetDocumentByNumber;
        String info = Infos.GetDocumentByNumber;
        info = info.replace("UserIdValue", BaseApplication.loginInfo.getUserId() + "");
        info = info.replace("ClientNameValue", BaseSettings.CLIENT_NAME);
        info = info.replace("SessionIdValue", BaseApplication.loginInfo.getSessionId());
        info = info.replace("documentNumberValue", documentNumber);
        final String finalInfo = info;
        MySoapHttpRequest.getString(new CallBackString() {
            @Override
            public void httFinsh(final String data) {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        RetrueCodeHandler returnInfo = RetruenUtils.getReturnInfo(data, new RetrueCodeHandler());
                        if (returnInfo != null && returnInfo.getString().equals("0")) {
                            Document mDocument = new Document();
                            mDocument.parser2XML(data);
                            BaseApplication.getApplication().setDocument(mDocument);
                            mActivity.startActivity(new Intent(mActivity, ApplyDocumentDetailActivity.class));
                        } else if (returnInfo != null && returnInfo.equals("-1")) {
                            mActivity.Tos(returnInfo.getError());
                        } else {
                            mActivity.Tos("数据获取错误");
                        }
                        mActivity.closeProgressDialog();
                    }
                });
            }
        }, endPoint, soapAction, finalInfo);
    }


    protected class ViewHolder {
        private final TextView tvDocumentCreatorTime;
        private final TextView tvqueryWood;
        private final TextView tvQueryInitapply;
        private TextView tvDocumentNumber;
        private TextView tvFrameNum;
        private TextView tvDocumentCreatorName;
        private TextView tvDocumentState;

        public ViewHolder(View view) {
            tvDocumentNumber = (TextView) view.findViewById(R.id.tv_document_number);
            tvFrameNum = (TextView) view.findViewById(R.id.tv_frame_num);
            tvDocumentCreatorName = (TextView) view.findViewById(R.id.tv_document_CreatorName);
            tvDocumentState = (TextView) view.findViewById(R.id.tv_document_state);
            tvDocumentCreatorTime = (TextView) view.findViewById(R.id.tv_document_creatTime);
            tvqueryWood = (TextView) view.findViewById(R.id.query_wood);
            tvQueryInitapply = (TextView) view.findViewById(R.id.query_initapply);
        }
    }
}
