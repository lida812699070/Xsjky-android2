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
import cn.xsjky.android.model.ReceiverStatData;

public class ItemStatisticsAdapter<T> extends BaseAdapter {

    private List<T> objects = new ArrayList<T>();

    private Context context;
    private LayoutInflater layoutInflater;

    public ItemStatisticsAdapter(Context context, List<T> objects) {
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
            convertView = layoutInflater.inflate(R.layout.item_statistics, null);
            convertView.setTag(new ViewHolder(convertView));
        }
        initializeViews((T) getItem(position), (ViewHolder) convertView.getTag());
        return convertView;
    }

    private void initializeViews(T object, ViewHolder holder) {
        ReceiverStatData data = (ReceiverStatData) object;
        holder.itemStatisticsMonth.setText(data.getStatPeriod());
        holder.itemStatisticsPrice.setText("运费："+data.getTotalPremium());
        holder.itemStatisticsQty.setText("票数："+data.getTicketCount());
        holder.itemStaristicsPremium.setText("保险："+data.getTotalPremium());
        holder.itemStaristicsQuantity.setText("件数："+data.getTotalQuantity());
        holder.itemStaristicsWeight.setText("重量："+data.getTotalWeight());
        holder.itemStaristicsVol.setText("体积："+data.getTotalVolumn());

    }

    protected class ViewHolder {
        private TextView itemStatisticsMonth;
        private TextView itemStatisticsPrice;
        private TextView itemStaristicsPremium;
        private TextView itemStaristicsQuantity;
        private TextView itemStaristicsWeight;
        private TextView itemStatisticsQty;
        private TextView itemStaristicsVol;

        public ViewHolder(View view) {
            itemStatisticsMonth = (TextView) view.findViewById(R.id.item_statistics_month);
            itemStatisticsPrice = (TextView) view.findViewById(R.id.item_statistics_price);
            itemStatisticsQty = (TextView) view.findViewById(R.id.item_statistics_qty);
            itemStaristicsPremium = (TextView) view.findViewById(R.id.item_staristics_premium);
            itemStaristicsQuantity = (TextView) view.findViewById(R.id.item_staristics_quantity);
            itemStaristicsWeight = (TextView) view.findViewById(R.id.item_staristics_weight);
            itemStaristicsVol = (TextView) view.findViewById(R.id.item_staristics_vol);
        }
    }
}
