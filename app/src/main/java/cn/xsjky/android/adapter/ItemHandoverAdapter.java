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
import cn.xsjky.android.model.MyHandOverRecord;

public class ItemHandoverAdapter<T> extends BaseAdapter {

    private List<T> objects = new ArrayList<T>();

    private Context context;
    private LayoutInflater layoutInflater;

    public ItemHandoverAdapter(Context context,List<T> objects) {
        this.context = context;
        this.objects=objects;
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
            convertView = layoutInflater.inflate(R.layout.item_handover, null);
            convertView.setTag(new ViewHolder(convertView));
        }
        initializeViews((T)getItem(position), (ViewHolder) convertView.getTag());
        return convertView;
    }

    private void initializeViews(T object, ViewHolder holder) {
        MyHandOverRecord handOverRecord = (MyHandOverRecord) object;
        holder.tvTime.setText("时间："+handOverRecord.getIssueTime());
        holder.tvDestination.setText("接收人："+handOverRecord.getReceiveEmployee());
        String isReceived = handOverRecord.getIsReceived();
        if (isReceived.equals("true")){
            holder.tvState.setText("是否已接收："+"已接收");
        }else {
            holder.tvState.setText("是否已接收："+"未接收");
        }
        holder.tvPoll.setText("票数："+handOverRecord.getListItem().size());
    }

    protected class ViewHolder {
        private TextView tvTime;
        private TextView tvDestination;
        private TextView tvState;
        private TextView tvPoll;

        public ViewHolder(View view) {
            tvTime = (TextView) view.findViewById(R.id.tv_time);
            tvDestination = (TextView) view.findViewById(R.id.tv_destination);
            tvState = (TextView) view.findViewById(R.id.tv_state);
            tvPoll = (TextView) view.findViewById(R.id.tv_poll);
        }
    }
}
