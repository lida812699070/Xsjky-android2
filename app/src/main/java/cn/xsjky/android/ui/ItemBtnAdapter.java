package cn.xsjky.android.ui;

import java.util.ArrayList;
import java.util.List;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;

import cn.xsjky.android.BaseApplication;
import cn.xsjky.android.R;
import it.sephiroth.android.library.imagezoom.ImageViewTouch;

public class ItemBtnAdapter<T> extends BaseAdapter {

    private List<T> objects = new ArrayList<T>();

    private Context context;
    private LayoutInflater layoutInflater;
    private Intent mIntent;

    public ItemBtnAdapter(Context context, List<T> objects) {
        this.context = context;
        this.objects = objects;
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
            convertView = layoutInflater.inflate(R.layout.item_btn, null);
            convertView.setTag(new ViewHolder(convertView));
        }
        initializeViews((T) getItem(position), (ViewHolder) convertView.getTag(),position);
        return convertView;
    }

    private void initializeViews(T object, ViewHolder holder, int position) {
        AActivity.User user= (AActivity.User) object;
        holder.tv.setText(user.getName());
        BaseApplication.mBitmapUtils.display(holder.iv,user.getUrl());
    }

    protected class ViewHolder {
        private TextView tv;
        private ImageView iv;

        public ViewHolder(View view) {
            tv = (TextView) view.findViewById(R.id.tv);
            iv = (ImageView) view.findViewById(R.id.iv);
        }
    }


}
