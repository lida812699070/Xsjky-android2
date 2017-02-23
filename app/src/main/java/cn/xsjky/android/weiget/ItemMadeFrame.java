package cn.xsjky.android.weiget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.xsjky.android.R;

/**
 * Created by OK on 2016/4/19.
 */
public class ItemMadeFrame extends LinearLayout{

    private TextView tvMadeframeLength;
    private TextView tvMadeframeWith;
    private TextView tvMadeframeHeight;
    private TextView tvMadeframeQty;
    private TextView tvMadeframeWorker;

    public TextView getTvMadeframeLength() {
        return tvMadeframeLength;
    }

    public TextView getTvMadeframeDel() {
        return tvMadeframeDel;
    }

    public TextView getTvMadeframeQty() {
        return tvMadeframeQty;
    }

    public TextView getTvMadeframeWorker() {
        return tvMadeframeWorker;
    }

    public TextView getTvMadeframeHeight() {
        return tvMadeframeHeight;
    }

    public TextView getTvMadeframeWith() {
        return tvMadeframeWith;
    }

    private TextView tvMadeframeDel;

    public ItemMadeFrame(Context context) {
        super(context);
        initView(context,null,0);
    }

    public ItemMadeFrame(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs, 0);
    }
    private void initView(Context context, AttributeSet attrs, int defStyleAttr) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_head_madefragm,null );
        tvMadeframeLength = (TextView) view.findViewById(R.id.tv_madeframe_length);
        tvMadeframeWith = (TextView) view.findViewById(R.id.tv_madeframe_with);
        tvMadeframeHeight = (TextView) view.findViewById(R.id.tv_madeframe_height);
        tvMadeframeQty = (TextView) view.findViewById(R.id.tv_madeframe_qty);
        tvMadeframeDel = (TextView) view.findViewById(R.id.tv_madeframe_del);
    }

}
