package cn.xsjky.android.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import cn.xsjky.android.R;
import cn.xsjky.android.model.DeliveryRequest;
import cn.xsjky.android.ui.BaseActivity;

/**
 * Created by ${lida} on 2016/10/11.
 */
public class QueryRecycleAdapter<T> extends BaseRecycleviewAdapter {

    private BaseActivity mContext;
    public QueryRecycleAdapter(Context context, List<T> mdatas, int layoutId) {
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
        final DeliveryRequest deliveryRequest = (DeliveryRequest) mdatas.get(position);
        holderItem.tv_RequestId.setText("单号："+deliveryRequest.getRequestId());
        holderItem.tv_Appointment.setText("预约时间："+deliveryRequest.getAppointment());
        holderItem.tv_CargoVolumn.setText("体积："+deliveryRequest.getCargoVolumn());
        holderItem.tv_StatusDescription.setText("状态："+deliveryRequest.getStatusDescription());
        holderItem.tv_TruckNumber.setText("司机车牌："+deliveryRequest.getTruckNumber());
        holderItem.tv_Requester.setText("发件人："+deliveryRequest.getRequestUser());

    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ItemViewHolderSon extends ItemViewHolder {

        private TextView tv_RequestId;
        private TextView tv_Appointment;
        private TextView tv_CargoVolumn;
        private TextView tv_StatusDescription;
        private TextView tv_TruckNumber;
        private TextView tv_Requester;

        public ItemViewHolderSon(View view) {
            super(view);
        }

        @Override
        void findViews(View view) {
            tv_RequestId = (TextView) view.findViewById(R.id.tv_RequestId);
            tv_Appointment = (TextView) view.findViewById(R.id.tv_Appointment);
            tv_CargoVolumn = (TextView) view.findViewById(R.id.tv_CargoVolumn);
            tv_StatusDescription = (TextView) view.findViewById(R.id.tv_StatusDescription);
            tv_TruckNumber= (TextView) view.findViewById(R.id.tv_TruckNumber);
            tv_Requester = (TextView) view.findViewById(R.id.tv_Requester);
        }
    }
}
