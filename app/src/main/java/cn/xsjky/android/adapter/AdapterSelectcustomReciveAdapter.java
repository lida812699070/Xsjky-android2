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
import cn.xsjky.android.model.AddressBook;
import cn.xsjky.android.model.CustomReceivers;
import cn.xsjky.android.model.CustomerReceivers;
import cn.xsjky.android.model.Recivicer;
import cn.xsjky.android.model.SendPeroson;

public class AdapterSelectcustomReciveAdapter<T> extends BaseAdapter {

    private List<T> objects = new ArrayList<T>();

    private Context context;
    private LayoutInflater layoutInflater;

    public AdapterSelectcustomReciveAdapter(Context context, List<T> objects) {
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
            convertView = layoutInflater.inflate(R.layout.adapter_selectcustom_recive, null);
            convertView.setTag(new ViewHolder(convertView));
        }
        initializeViews((T) getItem(position), (ViewHolder) convertView.getTag());
        return convertView;
    }

    private void initializeViews(T object, ViewHolder holder) {

        if (object instanceof AddressBook){
            AddressBook obj= (AddressBook) object;
            holder.tvAddress.setText(obj.getAddress());
            holder.tvName.setText(obj.getContactName());
            holder.tvPhone.setText(obj.getMobileNumber());
        }else if (object instanceof SendPeroson){
            SendPeroson obj= (SendPeroson) object;
            holder.tvAddress.setText(obj.getAddress());
            holder.tvName.setText(obj.getContactName());
            holder.tvPhone.setText(obj.getMobileNumber());
        }else {
            Recivicer obj= (Recivicer) object;
            holder.tvAddress.setText(obj.getAddress());
            holder.tvName.setText(obj.getReceiverName());
            holder.tvPhone.setText(obj.getMobile());
        }

    }

    protected class ViewHolder {
        private TextView tvName;
        private TextView tvPhone;
        private TextView tvAddress;

        public ViewHolder(View view) {
            tvName = (TextView) view.findViewById(R.id.tv_name);
            tvPhone = (TextView) view.findViewById(R.id.tv_phone);
            tvAddress = (TextView) view.findViewById(R.id.tv_address);
        }
    }
}
