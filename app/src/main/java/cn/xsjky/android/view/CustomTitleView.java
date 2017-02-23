package cn.xsjky.android.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import cn.xsjky.android.R;


/**
 * Created by ${lida} on 2016/8/8.
 */
public class CustomTitleView extends View {

    private String mTitleText;
    private int mTitleTextcolor;
    private int mTitleTextSize;
    private Rect mbound;
    private Paint paint;

    public CustomTitleView(Context context) {
        this(context, null);
    }

    public CustomTitleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomTitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomTitleView, defStyleAttr, 0);
        int indexCount = typedArray.getIndexCount();
        for (int i = 0; i < indexCount; i++) {
            int attr = typedArray.getIndex(i);
            switch (attr) {
                case R.styleable.CustomTitleView_titleTextcus:
                    mTitleText = typedArray.getString(attr);
                    break;
                case R.styleable.CustomTitleView_titleTextColorcus:
                    mTitleTextcolor = typedArray.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.CustomTitleView_titleTextSizecus:
                    mTitleTextSize = typedArray.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()));
                    break;
            }
        }
        typedArray.recycle();
        paint = new Paint();
        paint.setColor(mTitleTextcolor);
        mbound = new Rect();
        paint.setTextSize(mTitleTextSize);
        paint.getTextBounds(mTitleText,0,mTitleText.length(),mbound);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width;
        int height ;
        //确定宽高时  match_patent 或者 设置dp
        if (widthMode == MeasureSpec.EXACTLY)
        {
            width = widthSize;
        }
        //wrp_content
        else
        {
            paint.setTextSize(mTitleTextSize);
            paint.getTextBounds(mTitleText, 0, mTitleText.length(), mbound);
            float textWidth = mbound.width();
            int desired = (int) (getPaddingLeft() + textWidth + getPaddingRight());
            width = desired;
        }

        if (heightMode == MeasureSpec.EXACTLY)
        {
            height = heightSize;
        } else
        {
            paint.setTextSize(mTitleTextSize);
            paint.getTextBounds(mTitleText, 0, mTitleText.length(), mbound);
            float textHeight = mbound.height();
            int desired = (int) (getPaddingTop() + textHeight + getPaddingBottom());
            height = desired;
        }



        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        paint.setColor(Color.GRAY);
        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), paint);
        paint.setColor(mTitleTextcolor);
        canvas.drawText(mTitleText,getWidth()/2-mbound.width()/2,getHeight()/2+mbound.height()/2,paint);
    }
}
