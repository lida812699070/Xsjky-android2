package cn.xsjky.android.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import cn.xsjky.android.R;
import cn.xsjky.android.model.DeliveryRequest;
import cn.xsjky.android.model.SimpleDocumentBysales;
import cn.xsjky.android.ui.BaseActivity;

/**
 * Created by ${lida} on 2017/3/1.
 */
public class QueryBySalesmanAdapter<T> extends BaseRecycleviewAdapter {

    private BaseActivity mContext;
    public QueryBySalesmanAdapter(Context context, List mdatas, int layoutId) {
        super(context, mdatas, layoutId);
        this.mContext= (BaseActivity) context;
    }

    @Override
    ItemViewHolder getItemView(View view) {
        ItemViewHolder itemViewHolder = new ItemViewHolderSon(view);
        return itemViewHolder;
    }

    @Override
    protected void bindData(RecyclerView.ViewHolder holder, int position) {
        ItemViewHolderSon holderItem = (ItemViewHolderSon) holder;
        final SimpleDocumentBysales entity = (SimpleDocumentBysales) mdatas.get(position);
        holderItem.tvdocumentNumber.setText(entity.getDocumentNumber());
        holderItem.tvCreateTime.setText(entity.getCreateTime());
        holderItem.tvShipperName.setText("发货人："+entity.getShipperName());
        holderItem.tvConsigneeName.setText("接收单位："+entity.getConsigneeName());
        holderItem.tvConsigneeContactPerson.setText("接收人："+entity.getConsigneeContactPerson());
        holderItem.tvToCity.setText("目的城市："+entity.getToCity());
        holderItem.tvShippingMode.setText("运输方式:"+entity.getShippingMode());
        holderItem.tvShippingStatus.setText("运输状态："+entity.getShippingStatus());
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ItemViewHolderSon extends ItemViewHolder {

        private TextView tvdocumentNumber;
        private TextView tvCreateTime;
        private TextView tvShipperName;
        private TextView tvConsigneeName;
        private TextView tvConsigneeContactPerson;
        private TextView tvToCity;
        private TextView tvShippingMode;
        private TextView tvShippingStatus;

        public ItemViewHolderSon(View view) {
            super(view);
        }

        @Override
        void findViews(View view) {
            tvdocumentNumber = (TextView) view.findViewById(R.id.tv_documentNumber);
            tvCreateTime = (TextView) view.findViewById(R.id.tv_CreateTime);
            tvShipperName = (TextView) view.findViewById(R.id.tv_ShipperName);
            tvConsigneeName = (TextView) view.findViewById(R.id.tv_ConsigneeName);
            tvConsigneeContactPerson= (TextView) view.findViewById(R.id.tv_ConsigneeContactPerson);
            tvToCity = (TextView) view.findViewById(R.id.tv_ToCity);
            tvShippingMode = (TextView) view.findViewById(R.id.tv_ShippingMode);
            tvShippingStatus = (TextView) view.findViewById(R.id.tv_ShippingStatus);
        }
    }
}
