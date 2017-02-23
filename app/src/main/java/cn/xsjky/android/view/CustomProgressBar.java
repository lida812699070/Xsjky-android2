package cn.xsjky.android.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import cn.xsjky.android.R;


/**
 * Created by ${lida} on 2016/8/8.
 */
public class CustomProgressBar extends View {

    private int mFirstColor;
    private int mSecondColor;
    private float mCircleWidth;
    private int mSpeed;
    private Paint mPaint;
    private int mProgree;

    public CustomProgressBar(Context context) {
        this(context, null);
    }

    public CustomProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.customProgressBar, defStyleAttr, 0);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.customProgressBar_firstcolor:
                    mFirstColor = a.getColor(attr, Color.GREEN);
                    break;
                case R.styleable.customProgressBar_secondColor:
                    mSecondColor = a.getColor(attr, Color.RED);
                    break;
                case R.styleable.customProgressBar_circleWidth:
                    mCircleWidth = a.getDimension(attr, (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_PX, 20, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.customProgressBar_speed:
                    mSpeed = a.getInt(attr, 20);
                    break;
            }
        }
        a.recycle();
        mPaint = new Paint();
        new Thread() {
            @Override
            public void run() {
                while (true) {
                    mProgree++;
                    if (mProgree == 360) {
                        mProgree = 0;
                        if (!isNext) {
                            isNext = true;
                        } else {
                            isNext = false;
                        }
                    }
                    postInvalidate();
                    try {
                        Thread.sleep(mSpeed);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    private boolean isNext = false;

    @Override
    protected void onDraw(Canvas canvas) {
        int centre = getWidth() / 2;//圆心的坐标
        float radius = centre - mCircleWidth / 2;//半径
        mPaint.setStrokeWidth(mCircleWidth);
        mPaint.setAntiAlias(true);//消除锯齿
        mPaint.setStyle(Paint.Style.STROKE);
        RectF oval=new RectF(centre-radius,centre-radius,centre+radius,centre+radius);
        if (!isNext){
            mPaint.setColor(mFirstColor);
            canvas.drawCircle(centre, centre, radius, mPaint);
            mPaint.setColor(mSecondColor);
            canvas.drawArc(oval, -90, mProgree, false, mPaint);
        }else {
            mPaint.setColor(mSecondColor);
            canvas.drawCircle(centre, centre, radius, mPaint);
            mPaint.setColor(mFirstColor);
            canvas.drawArc(oval,-90,mProgree,false,mPaint);
        }
    }
}
