package cn.xsjky.android.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import cn.xsjky.android.R;
import cn.xsjky.android.model.CustomOtherInfos;

public class ListAcInfoAdapter<T> extends BaseAdapter {

    private List<T> objects = new ArrayList<T>();

    private Context context;
    private LayoutInflater layoutInflater;

    public ListAcInfoAdapter(Context context, List<T> objects) {
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
            convertView = layoutInflater.inflate(R.layout.list_ac_info, null);
            convertView.setTag(new ViewHolder(convertView));
        }
        initializeViews((T) getItem(position), (ViewHolder) convertView.getTag());
        return convertView;
    }

    private void initializeViews(T object, ViewHolder holder) {
        CustomOtherInfos otherInfos = (CustomOtherInfos) object;
        holder.itemAcInfoConginess.setText("目的城市："+otherInfos.getTocity());
        holder.itemAcInfoCustom.setText("公司名："+otherInfos.getCustomName());
        holder.itemAcInfoGoodsname.setText("货物名称："+otherInfos.getGoodsName());
        holder.itemAcInfoShipmode.setText("运输方式："+otherInfos.getShipMode());
        holder.itemAcInfoTocity.setText("收件公司："+otherInfos.getConsigneeName());
        holder.itemAcInfoShipName.setText("发件人："+otherInfos.getShipName());
    }

    protected class ViewHolder {
        private TextView itemAcInfoTocity;
        private TextView itemAcInfoCustom;
        private TextView itemAcInfoConginess;
        private TextView itemAcInfoShipmode;
        private TextView itemAcInfoGoodsname;
        private TextView itemAcInfoShipName;

        public ViewHolder(View view) {
            itemAcInfoTocity = (TextView) view.findViewById(R.id.item_ac_info_tocity);
            itemAcInfoShipName = (TextView) view.findViewById(R.id.item_ac_info_shipname);
            itemAcInfoCustom = (TextView) view.findViewById(R.id.item_ac_info_custom);
            itemAcInfoConginess = (TextView) view.findViewById(R.id.item_ac_info_conginess);
            itemAcInfoShipmode = (TextView) view.findViewById(R.id.item_ac_info_shipmode);
            itemAcInfoGoodsname = (TextView) view.findViewById(R.id.item_ac_info_goodsname);
        }
    }
}
