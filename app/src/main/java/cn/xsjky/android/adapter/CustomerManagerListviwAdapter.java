package cn.xsjky.android.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import cn.xsjky.android.BaseApplication;
import cn.xsjky.android.BaseSettings;
import cn.xsjky.android.R;
import cn.xsjky.android.model.Custom;
import cn.xsjky.android.model.Infos;
import cn.xsjky.android.model.LoginInfo;
import cn.xsjky.android.ui.AddCutomerActivity;
import cn.xsjky.android.ui.BaseActivity;
import cn.xsjky.android.util.DialoginOkCallBack;
import cn.xsjky.android.util.LogU;
import cn.xsjky.android.util.MySoap;
import cn.xsjky.android.util.RetrueCodeHandler;
import cn.xsjky.android.util.RetruenUtils;

/**
 * Created by ${lida} on 2016/9/6.
 */
public class CustomerManagerListviwAdapter<T> extends BaseRecycleviewAdapter {

    private BaseActivity mContext;

    public CustomerManagerListviwAdapter(Context context, List<T> mdatas, int layoutId) {
        super(context, mdatas, layoutId);
        this.mContext = (BaseActivity) context;
    }

    @Override
    ItemViewHolder getItemView(View view) {
        ItemViewHolder itemViewHolder = new ItemViewHolderSon(view);
        return itemViewHolder;
    }

    @Override
    protected void bindData(RecyclerView.ViewHolder holder, final int position) {
        final Custom handoverDocument = (Custom) mdatas.get(position);
        ItemViewHolderSon holderItem = (ItemViewHolderSon) holder;
        holderItem.tvCustomerName.setText(handoverDocument.getCustomerName());
        holderItem.tvCustomerPerson.setText(handoverDocument.getContactPerson());
        holderItem.tvAddress.setText("地址：" + handoverDocument.getCustomerName());
        holderItem.tvTel.setText("电话：" + handoverDocument.getTel());
      /*  holderItem.btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                change(handoverDocument);
            }
        });
        holderItem.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete(handoverDocument, position);
            }
        });*/
    }

    /*private void delete(final Custom handoverDocument, final int position) {
        mContext.showDialog(new DialoginOkCallBack() {
            @Override
            public void onClickOk(DialogInterface dialog, int which) {

                //删除本地的
                Custom.delete(handoverDocument);
                //adapter唤醒
                removeData(position);
                //删除服务器的
                getRemoteInfo(handoverDocument.getCustomerId());


            }
        }, "是否删除？");

    }*/

    public void getRemoteInfo(String customerId) {
        mContext.showProgressDialog();
        String endPoint = BaseSettings.WEBSERVICE_URL;
        // SOAP Action
        final LoginInfo loginInfo = BaseApplication.loginInfo;
        if (loginInfo == null)
            return;
        final String soapAction = "http://www.xsjky.cn/DeleteCustomer";
        final MySoap transport = new MySoap(endPoint);
        String info = Infos.DeleteCustomer;
        info = info.replace("UserIdValue", loginInfo.getUserId() + "");
        info = info.replace("ClientNameValue", Infos.CLIENTNAME);
        info = info.replace("SessionIdValue", loginInfo.getSessionId());
        info = info.replace("RoleDataValue", "0");
        info = info.replace("customerIdValue", customerId);
        transport.setinfo(info);
        transport.debug = true;
        new Thread() {
            @Override
            public void run() {
                try {
                    final String call = transport.call(soapAction, null, null, "");
                    mContext.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            RetrueCodeHandler returnInfo = RetruenUtils.getReturnInfo(call, new RetrueCodeHandler());
                            assert returnInfo != null;
                            if (returnInfo.getString().equals("0")) {
                                mContext.Tos("删除成功");
                            } else {
                                mContext.Tos("删除失败");
                                LogU.e(call);
                            }
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    mContext.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            notifyDataSetChanged();
                        }
                    });
                    mContext.closeProgressDialog();
                }
                //mListView.onRefreshComplete();
            }
        }.start();

    }

    /*private void change(Custom handoverDocument) {
        Intent intent = new Intent(mContext, AddCutomerActivity.class);
        intent.putExtra("data", handoverDocument);
        LogU.e(handoverDocument.toString());
        mContext.startActivity(intent);
    }*/

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ItemViewHolderSon extends ItemViewHolder {
        private TextView tvCustomerName;
        private TextView tvCustomerPerson;
        private TextView tvAddress;
        private TextView tvTel;

        public ItemViewHolderSon(View view) {
            super(view);
        }

        @Override
        void findViews(View view) {
            tvCustomerName = (TextView) view.findViewById(R.id.item_customer_manager_CustomerName);
            tvCustomerPerson = (TextView) view.findViewById(R.id.item_customer_manager_CustomerPerson);
            tvAddress = (TextView) view.findViewById(R.id.item_customer_manager_Address);
            tvTel = (TextView) view.findViewById(R.id.item_customer_manager_Tel);
        }
    }
}
