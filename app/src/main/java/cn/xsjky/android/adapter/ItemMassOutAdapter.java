package cn.xsjky.android.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import cn.xsjky.android.R;
import cn.xsjky.android.model.HandoverDocument;

public class ItemMassOutAdapter<T> extends BaseAdapter {

    public static HashMap<Integer, Boolean> isSelected;
    private List<T> objects = new ArrayList<T>();

    private Context context;
    private LayoutInflater layoutInflater;

    public ItemMassOutAdapter(Context context, List<T> objects) {
        this.objects = objects;
        this.context = context;
        isSelected = new HashMap<Integer, Boolean>();
        initDate();
        this.layoutInflater = LayoutInflater.from(context);
    }
    // 初始化isSelected的数据
    private void initDate() {
        for (int i = 0; i < objects.size(); i++) {
            getIsSelected().put(i, false);
        }
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
            convertView = layoutInflater.inflate(R.layout.item_mass_out, null);
            convertView.setTag(new ViewHolder(convertView));
        }
        initializeViews((T) getItem(position), (ViewHolder) convertView.getTag(),position);
        return convertView;
    }

    private void initializeViews(T object, ViewHolder holder, final int position) {
        //TODO implement
        HandoverDocument h= (HandoverDocument) object;
        holder.tvTrackingLvDocuNum.setText(h.getDocumentNumber());
        holder.tvTrackingLvQty.setText(h.getHandoverQty());
        holder.tvTrackingLvTocity.setText(h.getToCity());
        holder.checkboxItemTracking.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                if (isSelected.get(position)) {
                    isSelected.put(position, false);
                    setIsSelected(isSelected);
                } else {
                    isSelected.put(position, true);
                    setIsSelected(isSelected);
                }

            }
        });

        // 根据isSelected来设置checkbox的选中状况
        holder.checkboxItemTracking.setChecked(getIsSelected().get(position));
    }
    protected class ViewHolder {
        private CheckBox checkboxItemTracking;
        private TextView tvTrackingLvTocity;
        private TextView tvTrackingLvDocuNum;
        private TextView tvTrackingLvQty;

        public ViewHolder(View view) {
            checkboxItemTracking = (CheckBox) view.findViewById(R.id.checkbox_item_tracking);
            tvTrackingLvTocity = (TextView) view.findViewById(R.id.tv_tracking_lv_tocity);
            tvTrackingLvDocuNum = (TextView) view.findViewById(R.id.tv_tracking_lv_docuNum);
            tvTrackingLvQty = (TextView) view.findViewById(R.id.tv_tracking_lv_qty);
        }
    }
    public static HashMap<Integer, Boolean> getIsSelected() {
        return isSelected;
    }

    public static void setIsSelected(HashMap<Integer, Boolean> isSelected) {
        ItemMassOutAdapter.isSelected = isSelected;
    }
    public void selectAll(){
        for (int i = 0; i < isSelected.size(); i++) {
            isSelected.put(i,true);
        }
        this.notifyDataSetChanged();
    }
    public void selectFalseAll(){
        for (int i = 0; i < isSelected.size(); i++) {
            Boolean aBoolean = isSelected.get(i);
            isSelected.put(i,!aBoolean);
        }
        this.notifyDataSetChanged();
    }
}
