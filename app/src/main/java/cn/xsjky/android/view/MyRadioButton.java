package cn.xsjky.android.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.RadioButton;

/**
 * Created by ${lida} on 2016/11/18.
 */
public class MyRadioButton extends RadioButton {

    private Paint linePaint;

    public MyRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    public MyRadioButton(Context context) {
        super(context);
        init(context, null, 0);
    }

    public MyRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }


    private void init(Context context, AttributeSet attrs, int i) {

        linePaint = new Paint();
        linePaint.setColor(Color.BLUE);
        linePaint.setStrokeWidth(10);
        linePaint.setAntiAlias(true);
        linePaint.setStyle(Paint.Style.FILL);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        boolean checked = super.isChecked();
        Log.e("tag",checked+"====");
        if (checked) {
            canvas.drawLine(0, getHeight(), getWidth(), getHeight(), linePaint);
        }
    }

    @Override
    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        super.setOnCheckedChangeListener(listener);
    }

}
