package cn.xsjky.android.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import com.jq.printer.esc.Text;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapSerializationEnvelope;

import cn.xsjky.android.BaseApplication;
import cn.xsjky.android.BaseSettings;
import cn.xsjky.android.R;
import cn.xsjky.android.constant.SoapAction;
import cn.xsjky.android.constant.SoapEndpoint;
import cn.xsjky.android.constant.SoapInfo;
import cn.xsjky.android.db.ErrorDocument;
import cn.xsjky.android.db.SqlUtils;
import cn.xsjky.android.http.HttpCallback;
import cn.xsjky.android.model.LoginInfo;
import cn.xsjky.android.model.SimpleDocument;
import cn.xsjky.android.ui.ErrorDocumentActivity;
import cn.xsjky.android.util.DialoginOkCallBack;
import cn.xsjky.android.util.LogU;
import cn.xsjky.android.util.MySoap;
import cn.xsjky.android.util.RetrueCodeHandler;
import cn.xsjky.android.util.RetruenUtils;
import cn.xsjky.android.util.ShowDialogUtils;
import cn.xsjky.android.util.SimpleDocumentXmlparser;
import cn.xsjky.android.util.StrKit;
import cn.xsjky.android.util.TempletUtil;
import cn.xsjky.android.util.XmlParserUtil;

public class ItemErrorDocumentLvAdapter<T> extends BaseAdapter {

    private List<T> objects = new ArrayList<T>();

    private Context context;
    private LayoutInflater layoutInflater;
    private ErrorDocumentActivity activity;

    public ItemErrorDocumentLvAdapter(Context context, List<T> objects) {
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
            convertView = layoutInflater.inflate(R.layout.item_error_document_lv, null);
            convertView.setTag(new ViewHolder(convertView));
        }
        initializeViews((T) getItem(position), (ViewHolder) convertView.getTag());
        return convertView;
    }

    private void initializeViews(T object, ViewHolder holder) {
        final ErrorDocument errorDocument = (ErrorDocument) object;
        holder.tvTitle.setText(errorDocument.getDocumentNumber());
        SimpleDocumentXmlparser xmlparser = RetruenUtils.getReturnInfo(errorDocument.getDocumentcontent(), new SimpleDocumentXmlparser());
        SimpleDocument simpleDocument = null;
        if (xmlparser != null) {
            simpleDocument = xmlparser.getUser();
            holder.tvConsignee.setText(simpleDocument.getConsigneeName());
        }
        holder.btnErrorLvDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialogUtils.showDialog(new DialoginOkCallBack() {
                    @Override
                    public void onClickOk(DialogInterface dialog, int which) {
                        SqlUtils.ErrorDocumentDel(errorDocument.getDocumentNumber());
                        activity = (ErrorDocumentActivity) context;
                        activity.AdapterNotf();
                    }
                }, "是否要删除此条数据？", context);
            }
        });

        holder.btnErrorDocumentLvCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    //保存订单
    private void saveDocument(final ErrorDocument errorDocument) {
        String endPoint = SoapEndpoint.SET_IS_SIGNUP;
        final String soapAction = BaseSettings.SAVEDOCUMENT_ACTION;
        final MySoap transport = new MySoap(endPoint);
        transport.setinfo(errorDocument.getDocumentcontent());
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
                    if (handler != null && handler.getString().equals("0")) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ErrorDocument upDataErrorDocument = new ErrorDocument();
                                upDataErrorDocument.setDocumentNumber(errorDocument.getDocumentNumber());
                                upDataErrorDocument.setDocumentcontent(call);
                                SqlUtils.ErrorDocumentUpDate(errorDocument.getDocumentNumber(),upDataErrorDocument);
                                //confirmPrint();
                                //Toast.makeText(context, "更新成功", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, "提交失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    LogU.e(call);
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
        }.start();
    }

    //打印成功提交订单
    private void confirmPrint(String RecordId) {
        if ("0".equals(RecordId) || TextUtils.isEmpty(RecordId)){
            Toast.makeText(context,"订单未保存",1).show();
            return;
        }
        Map<String, String> map = BaseApplication.getApplication().getSecurityInfo();
        map.put("documentId",RecordId);
        String templet = TempletUtil.render(BaseSettings.AfterPrintCargoLabel_TEMPLET, map);
        BaseApplication.getApplication().getHttpPro().postAsync(templet, BaseSettings.AfterPrintCargoLabel_ACTION, new HttpCallback() {
            @Override
            public void onHttpStart() {
            }

            @Override
            public void onHttpFinish(String data) {
                XmlParserUtil parser = XmlParserUtil.getInstance();
                parser.parse(data);
                String prefix = "/soap:Envelope/soap:Body/AfterPrintCargoLabelResponse/AfterPrintCargoLabelResult/";
                String code = parser.getNodeValue(prefix + "ReturnCode");
                if (StrKit.isBlank(code) || !code.equals("0")) {
                    final String error = parser.getNodeValue(prefix + "Error");
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, error, 1).show();
                        }
                    });
                } else {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context,"打印信息提交成功",1).show();
                        }
                    });
                }
            }

            @Override
            public void onHttpError(String msg) {
            }

            @Override
            public void onHttpEnd() {

            }
        });
    }

    protected class ViewHolder {
        private TextView tvTitle;
        private TextView tvConsignee;
        private Button btnErrorLvDel;
        private Button btnErrorDocumentLvCommit;

        public ViewHolder(View view) {
            tvTitle = (TextView) view.findViewById(R.id.tv_title_documentnum_dialog_lv);
            tvConsignee = (TextView) view.findViewById(R.id.tv_consignee_document_lv);
            btnErrorLvDel = (Button) view.findViewById(R.id.btn_error_lv_del);
            btnErrorDocumentLvCommit = (Button) view.findViewById(R.id.btn_error_document_lv_commit);
        }
    }
}
