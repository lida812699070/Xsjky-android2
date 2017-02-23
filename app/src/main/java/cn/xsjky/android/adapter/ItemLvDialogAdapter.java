package cn.xsjky.android.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import com.lidroid.xutils.exception.DbException;

import cn.xsjky.android.BaseApplication;
import cn.xsjky.android.R;
import cn.xsjky.android.db.ErrorDocument;
import cn.xsjky.android.model.SimpleDocument;
import cn.xsjky.android.ui.MainActivity;
import cn.xsjky.android.util.RetruenUtils;
import cn.xsjky.android.util.SimpleDocumentXmlparser;

public class ItemLvDialogAdapter<T> extends BaseAdapter {

    private List<T> objects = new ArrayList<T>();

    private Context context;
    private LayoutInflater layoutInflater;

    public ItemLvDialogAdapter(Context context,List<T> objects) {
        this.objects=objects;
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
            convertView = layoutInflater.inflate(R.layout.item_lv_dialog, null);
            convertView.setTag(new ViewHolder(convertView));
        }
        initializeViews((T)getItem(position), (ViewHolder) convertView.getTag());
        return convertView;
    }

    private void initializeViews(T object, ViewHolder holder) {
        final String strDocuments= (String) object;
        holder.tvDialogLv.setText(strDocuments);
        holder.btnDialogLvDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    List<ErrorDocument> errorDocuments = BaseApplication.mdbUtils.findAll(ErrorDocument.class);
                    if (errorDocuments == null) {
                        return;
                    }
                    for (int i = 0; i < errorDocuments.size(); i++) {
                        if (strDocuments.equals(errorDocuments.get(i).getDocumentNumber())) {
                            BaseApplication.mdbUtils.delete(errorDocuments.get(i));
                            Toast.makeText(context,"删除成功",1).show();
                            MainActivity activity= (MainActivity) context;
                            activity.simpleAdapterAdapterNotf();
                        }
                    }
                } catch (DbException e) {
                    e.printStackTrace();
                    Toast.makeText(context,"删除失败",1).show();
                }
            }
        });
    }

    protected class ViewHolder {
        private TextView tvDialogLv;
        private Button btnDialogLvDel;

        public ViewHolder(View view) {
            tvDialogLv = (TextView) view.findViewById(R.id.tv_dialog_lv);
            btnDialogLvDel = (Button) view.findViewById(R.id.btn_dialog_lv_del);
        }
    }
}
