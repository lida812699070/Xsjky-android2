package cn.xsjky.android.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import cn.xsjky.android.R;
import cn.xsjky.android.model.Custom;
import cn.xsjky.android.ui.BaseActivity;

/**
 * Created by ${lida} on 2016/11/24.
 */
public class ItemCustomerManagerAdapter<T> extends BaseRecycleviewAdapter {

    private BaseActivity mContext;

    public ItemCustomerManagerAdapter(Context context, List<T> mdatas, int layoutId) {
        super(context, mdatas, layoutId);
        this.mContext = (BaseActivity) context;
    }

    @Override
    ItemViewHolder getItemView(View view) {
        ItemViewHolder itemViewHolder = new ItemViewHolderSon(view);
        return itemViewHolder;
    }

    @Override
    protected void bindData(RecyclerView.ViewHolder holder, int position) {
        Custom custom = (Custom) mdatas.get(position);
        ItemViewHolderSon holderItem = (ItemViewHolderSon) holder;
        holderItem.itemCustomManagerCompany.setText("公司名：" + custom.getCustomerName());
        holderItem.itemCustomManagerName.setText("发件人：" + custom.getContactPerson());
        holderItem.itemCustomManagerTel.setText("电话：" + custom.getTel());
        holderItem.itemCustomManagerAddress.setText("地址：" + custom.getAddress());
    }

    public static class ItemViewHolderSon extends ItemViewHolder {

        private TextView itemCustomManagerCompany;
        private TextView itemCustomManagerName;
        private TextView itemCustomManagerTel;
        private TextView itemCustomManagerAddress;

        public ItemViewHolderSon(View view) {
            super(view);
        }

        @Override
        void findViews(View view) {
            itemCustomManagerCompany = (TextView) view.findViewById(R.id.item_custom_manager_company);
            itemCustomManagerName = (TextView) view.findViewById(R.id.item_custom_manager_name);
            itemCustomManagerTel = (TextView) view.findViewById(R.id.item_custom_manager_tel);
            itemCustomManagerAddress = (TextView) view.findViewById(R.id.item_custom_manager_address);
        }
    }
}
