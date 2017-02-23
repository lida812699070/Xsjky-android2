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
import cn.xsjky.android.model.Custom;

public class ItemCustomManagerAdapter<T> extends BaseAdapter {

    private List<T> objects = new ArrayList<T>();

    private Context context;
    private LayoutInflater layoutInflater;

    public ItemCustomManagerAdapter(Context context, List<T> objects) {
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
            convertView = layoutInflater.inflate(R.layout.item_custom_manager, null);
            convertView.setTag(new ViewHolder(convertView));
        }
        initializeViews((T) getItem(position), (ViewHolder) convertView.getTag());
        return convertView;
    }

    private void initializeViews(T object, ViewHolder holder) {
        Custom custom = (Custom) object;
        holder.itemCustomManagerCompany.setText("公司名：" + custom.getCustomerName());
        holder.itemCustomManagerName.setText("发件人：" + custom.getContactPerson());
        holder.itemCustomManagerTel.setText("电话：" + custom.getTel());
        holder.itemCustomManagerAddress.setText("地址：" + custom.getAddress());
    }

    protected class ViewHolder {
        private TextView itemCustomManagerCompany;
        private TextView itemCustomManagerName;
        private TextView itemCustomManagerTel;
        private TextView itemCustomManagerAddress;

        public ViewHolder(View view) {
            itemCustomManagerCompany = (TextView) view.findViewById(R.id.item_custom_manager_company);
            itemCustomManagerName = (TextView) view.findViewById(R.id.item_custom_manager_name);
            itemCustomManagerTel = (TextView) view.findViewById(R.id.item_custom_manager_tel);
            itemCustomManagerAddress = (TextView) view.findViewById(R.id.item_custom_manager_address);
        }
    }
}
