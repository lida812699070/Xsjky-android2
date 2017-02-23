package cn.xsjky.android.adapter;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import com.jq.printer.esc.Text;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import cn.xsjky.android.BaseApplication;
import cn.xsjky.android.BaseSettings;
import cn.xsjky.android.R;
import cn.xsjky.android.http.Urls;
import cn.xsjky.android.model.DeliveryRequest;
import cn.xsjky.android.model.LoginInfo;
import cn.xsjky.android.ui.ApplyDocumentActivity;
import cn.xsjky.android.ui.IsAcceptDetailActivity;
import cn.xsjky.android.ui.MapApplyLocation;
import cn.xsjky.android.ui.NewActivity;
import cn.xsjky.android.util.GetUnfinishRequestXmlParser;
import cn.xsjky.android.util.LogU;
import cn.xsjky.android.util.OnClickutils;
import cn.xsjky.android.util.RetrueCodeHandler;

public class ItemGetunfinishAdapter<T> extends BaseAdapter {

    private List<T> objects = new ArrayList<T>();

    private Context context;
    private LayoutInflater layoutInflater;
    private DeliveryRequest mRequest;

    public ItemGetunfinishAdapter(Context context, List<T> objects) {
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
            convertView = layoutInflater.inflate(R.layout.item_getunfinish, null);
            convertView.setTag(new ViewHolder(convertView));
        }
        initializeViews((T) getItem(position), (ViewHolder) convertView.getTag());
        return convertView;
    }

    private void initializeViews(T object, final ViewHolder holder) {
        mRequest = (DeliveryRequest) object;
        holder.applydocumentItemAddress.setText("地址：" + mRequest.getAddress());
        holder.applydocumentItemTime.setText("时间：" + mRequest.getAppointment());
        holder.applydocumentItemName.setText("寄件人：" + mRequest.getContactPerson());
        holder.applydocumentItemPhone.setText(" 电话：" + mRequest.getContactNumber());
        holder.applydocumentItemOKBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginInfo loginInfo = BaseApplication.loginInfo;
                RequestParams params = new RequestParams();
                params.addBodyParameter("sessionId", loginInfo.getSessionId());
                params.addBodyParameter("userId", loginInfo.getUserId() + "");
                params.addBodyParameter("clientName", BaseSettings.CLIENT_NAME);
                params.addBodyParameter("requestId", mRequest.getRequestId() + "");
                params.addBodyParameter("handler", loginInfo.getUserId() + "");
                BaseApplication.mHttpUtils.send(HttpRequest.HttpMethod.POST,
                        Urls.CompleteRequest,
                        params,
                        new RequestCallBack<String>() {
                            @Override
                            public void onSuccess(ResponseInfo<String> responseInfo) {
                                SAXParserFactory factory = SAXParserFactory.newInstance();
                                SAXParser parser = null;
                                try {
                                    parser = factory.newSAXParser();
                                    RetrueCodeHandler handler = new RetrueCodeHandler();
                                    ByteArrayInputStream tInputStringStream = new ByteArrayInputStream(responseInfo.result.getBytes());
                                    parser.parse(tInputStringStream, handler);
                                    if (handler.getString().equals("0")) {
                                        holder.applydocumentItemOKBtn.setText("已完成");
                                        holder.applydocumentItemOKBtn.setClickable(false);
                                        Intent intent = new Intent(context, NewActivity.class);
                                        intent.putExtra("name", mRequest.getContactPerson());
                                        intent.putExtra("phone", mRequest.getContactNumber());
                                        intent.putExtra("address", mRequest.getAddress());
                                        context.startActivity(intent);
                                    } else {
                                        Toast.makeText(context, handler.getError(), Toast.LENGTH_SHORT).show();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Toast.makeText(context, "数据解析出错", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(HttpException error, String msg) {
                                Toast.makeText(context, "网络异常", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
        holder.applydocumentItemLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (OnClickutils.isFastDoubleClick()) {
                    return;
                }
                if (Build.VERSION.SDK_INT >= 23) {
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions((Activity)context, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                    }else {
                        initMapLbs();
                    }
                }else {
                    initMapLbs();
                }

            }
        });
        holder.applydocumentItemTellBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mRequest.getContactNumber()));
                if (TextUtils.isEmpty(mRequest.getContactNumber())) {
                    Toast.makeText(context, "电话号码有误", Toast.LENGTH_SHORT).show();
                    return;
                }
                context.startActivity(intent);
            }
        });
    }


    private void initMapLbs() {
        ApplyDocumentActivity applyDocumentActivity = (ApplyDocumentActivity) ItemGetunfinishAdapter.this.context;
        Intent intent = new Intent(applyDocumentActivity, MapApplyLocation.class);
        intent.putExtra("name", mRequest.getContactPerson());
        intent.putExtra("date", mRequest.getAppointment());
        intent.putExtra("shipperStr", mRequest.getAddress());
        intent.putExtra("phone", mRequest.getContactNumber());
        if (!mRequest.getLatitude().equals("0") && !mRequest.getLongitude().equals("0")) {
            intent.putExtra("point",mRequest.getLatitude()+","+mRequest.getLongitude());
        }
        applyDocumentActivity.startActivity(intent);
    }

    protected class ViewHolder {
        private Button applydocumentItemTellBtn;
        private TextView applydocumentItemName;
        private TextView applydocumentItemPhone;
        private TextView applydocumentItemTime;
        private TextView applydocumentItemAddress;
        private Button applydocumentItemLocationBtn;
        private Button applydocumentItemOKBtn;

        public ViewHolder(View view) {
            applydocumentItemName = (TextView) view.findViewById(R.id.applydocument_item_name);
            applydocumentItemPhone = (TextView) view.findViewById(R.id.applydocument_item_phone);
            applydocumentItemTime = (TextView) view.findViewById(R.id.applydocument_item_time);
            applydocumentItemAddress = (TextView) view.findViewById(R.id.applydocument_item_address);
            applydocumentItemLocationBtn = (Button) view.findViewById(R.id.applydocument_item_location_btn);
            applydocumentItemOKBtn = (Button) view.findViewById(R.id.applydocument_item_OK_btn);
            applydocumentItemTellBtn = (Button) view.findViewById(R.id.applydocument_item_tell_phone);
        }
    }
}
