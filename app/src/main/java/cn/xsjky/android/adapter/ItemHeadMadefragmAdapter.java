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
import cn.xsjky.android.model.WoodenFrame;

public class ItemHeadMadefragmAdapter<T> extends BaseAdapter {

    private List<T> objects = new ArrayList<T>();

    private Context context;
    private LayoutInflater layoutInflater;

    public ItemHeadMadefragmAdapter(Context context, List<T> objects) {
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
            convertView = layoutInflater.inflate(R.layout.item_head_madefragm, null);
            convertView.setTag(new ViewHolder(convertView));
        }
        initializeViews((T) getItem(position), (ViewHolder) convertView.getTag());
        return convertView;
    }

    private void initializeViews(T object, ViewHolder holder) {
        WoodenFrame frame = (WoodenFrame) object;
        holder.tvMadeframeLength.setText(frame.getLength());
        holder.tvMadeframeWith.setText(frame.getWidth());
        holder.tvMadeframeHeight.setText(frame.getHeight());
        holder.tvMadeframeQty.setText(frame.getQuantity());
        holder.tvMadeframeRemark.setText("备注：" + frame.getRemarks());
    }

    protected class ViewHolder {
        private TextView tvMadeframeLength;
        private TextView tvMadeframeWith;
        private TextView tvMadeframeHeight;
        private TextView tvMadeframeQty;
        private TextView tvMadeframeWorker;
        private TextView tvMadeframeDel;
        private TextView tvMadeframeRemark;

        public ViewHolder(View view) {
            tvMadeframeLength = (TextView) view.findViewById(R.id.tv_madeframe_length);
            tvMadeframeRemark = (TextView) view.findViewById(R.id.tv_madeframe_remark);
            tvMadeframeWith = (TextView) view.findViewById(R.id.tv_madeframe_with);
            tvMadeframeHeight = (TextView) view.findViewById(R.id.tv_madeframe_height);
            tvMadeframeQty = (TextView) view.findViewById(R.id.tv_madeframe_qty);
            tvMadeframeDel = (TextView) view.findViewById(R.id.tv_madeframe_del);
        }
    }
}
