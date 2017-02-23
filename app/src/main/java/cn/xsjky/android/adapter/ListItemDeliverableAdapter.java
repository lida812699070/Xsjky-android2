package cn.xsjky.android.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import cn.xsjky.android.R;
import cn.xsjky.android.model.DocumentEntity;
import cn.xsjky.android.ui.ApplyDocumentActivity;
import cn.xsjky.android.ui.DeliverableActivity;
import cn.xsjky.android.ui.MapApplyLocation;
import cn.xsjky.android.util.OnClickutils;

public class ListItemDeliverableAdapter<T> extends BaseAdapter {

    private List<T> objects = new ArrayList<T>();
    private Context context;
    private LayoutInflater layoutInflater;

    public ListItemDeliverableAdapter(Context context, List<T> objects) {
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
            convertView = layoutInflater.inflate(R.layout.list_item_deliverable, null);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.tvDocumentNumber = (TextView) convertView.findViewById(R.id.tvDocumentNumber);
            viewHolder.tvConsigneeTel = (TextView) convertView.findViewById(R.id.tvConsigneeTel);
            viewHolder.tvAddressLine2 = (TextView) convertView.findViewById(R.id.tvAddressLine2);
            viewHolder.tvWeight = (TextView) convertView.findViewById(R.id.tvWeight);
            viewHolder.tvConsigneeName = (TextView) convertView.findViewById(R.id.tv_consigneeName);
            viewHolder.tvConsigneePersonName = (TextView) convertView.findViewById(R.id.tv_consigneePersonName);
            viewHolder.tvQuantity = (TextView) convertView.findViewById(R.id.tvQuantity);
            viewHolder.btnToDrivice=(Button)convertView.findViewById(R.id.btn_toDrive);
            convertView.setTag(viewHolder);
        }
        initializeViews((T) getItem(position), (ViewHolder) convertView.getTag());
        return convertView;
    }

    private void initializeViews(T object, ViewHolder holder) {
        final DocumentEntity entity = (DocumentEntity) object;
        String address="";
        if (!TextUtils.isEmpty(entity.getAddressLine1())){
            address+=entity.getAddressLine1();
        }
        if (!TextUtils.isEmpty(entity.getAddressLine2())){
            address+=entity.getAddressLine2();
        }
        holder.tvAddressLine2.setText("地址："+address);
        holder.tvConsigneeName.setText("收件单位：" + entity.getConsigneeName());
        holder.tvConsigneePersonName.setText("收件人：" + entity.getConsigneeContactName());
        holder.tvConsigneeTel.setText("电话：" + entity.getConsigneeTel() + "");
        holder.tvConsigneeTel.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线

        holder.tvConsigneeTel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + entity.getConsigneeTel()));
                if (TextUtils.isEmpty(entity.getConsigneeTel())) {
                    Toast.makeText(context, "电话号码有误", Toast.LENGTH_SHORT).show();
                    return;
                }
                context.startActivity(intent);
            }
        });
        holder.tvDocumentNumber.setText("单号：" + entity.getDocumentNumber() + "");
        holder.tvWeight.setText("重量："+entity.getWeight() + "");
        holder.tvQuantity.setText("件数："+entity.getQuantity() + "");
        final String finalAddress = address;
        holder.btnToDrivice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (OnClickutils.isFastDoubleClick()) {
                    return;
                }
                DeliverableActivity activity = (DeliverableActivity) ListItemDeliverableAdapter.this.context;
                Intent intent = new Intent(activity, MapApplyLocation.class);
                intent.putExtra("name", entity.getConsigneeContactName());
                intent.putExtra("date", "");
                intent.putExtra("shipperStr", finalAddress);
                intent.putExtra("phone", entity.getConsigneeTel());
                activity.startActivity(intent);
            }
        });
    }

    protected class ViewHolder {
        private TextView tvDocumentNumber;
        private TextView tvConsigneePersonName;
        private TextView tvConsigneeName;
        private TextView tvConsigneeTel;
        private TextView tvAddressLine2;
        private TextView tvWeight;
        private TextView tvQuantity;
        private Button btnToDrivice;
    }
}
