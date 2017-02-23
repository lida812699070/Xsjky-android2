package cn.xsjky.android.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import cn.xsjky.android.R;


/**
 * Created by ${lida} on 2016/8/9.
 */
public class CustomVolumControlBar extends View {

    private int mFirstColor;
    private int mSecondColor;
    private Bitmap mImage;
    private float mCircleWidth;
    private int mCount;
    private int mSplitSize;
    private Paint mPaint;
    private Rect mRect;
    private int mCurrentCount = 3;
    public CustomVolumControlBar(Context context) {
        this(context, null);
    }

    public CustomVolumControlBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomVolumControlBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomVolumControlBar, defStyleAttr, 0);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.CustomVolumControlBar_firstcolor:
                    mFirstColor = a.getColor(attr, Color.BLUE);
                    break;
                case R.styleable.CustomVolumControlBar_secondColor:
                    mSecondColor = a.getColor(attr, Color.RED);
                    break;
                case R.styleable.CustomVolumControlBar_bg:
                    mImage = BitmapFactory.decodeResource(getResources(), a.getResourceId(attr, 0));
                    break;
                case R.styleable.CustomVolumControlBar_circleWidth:
                    mCircleWidth = a.getDimension(attr, (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_PX, 20, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.CustomVolumControlBar_dotCount:
                    mCount = a.getInt(attr, 20);
                    break;
                case R.styleable.CustomVolumControlBar_splitSize:
                    mSplitSize = a.getInt(attr, 20);
                    break;
            }
        }
        a.recycle();
        mPaint = new Paint();
        mRect = new Rect();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(mCircleWidth);
        mPaint.setStrokeCap(Paint.Cap.ROUND);//设置线段边缘为圆形
        mPaint.setStyle(Paint.Style.STROKE);
        int center = getWidth() / 2;
        int radius = (int)(center - mCircleWidth / 2);
        drawOval(canvas,center,radius);
        int relRadius  = (int)(radius - mCircleWidth / 2);
        mRect.left = (int) ((int) (relRadius - Math.sqrt(2) * 1.0f / 2 * relRadius) + mCircleWidth);
        /**
         * 内切正方形的距离左边 = mCircleWidth + relRadius - √2 / 2
         */
        mRect.top = (int) ((int) (relRadius - Math.sqrt(2) * 1.0f / 2 * relRadius) + mCircleWidth);
        mRect.bottom = (int) (mRect.left + Math.sqrt(2) * relRadius);
        mRect.right = (int) (mRect.left + Math.sqrt(2) * relRadius);

        /**
         * 如果图片比较小，那么根据图片的尺寸放置到正中心
         */
        if (mImage.getWidth() < Math.sqrt(2) * relRadius)
        {
            mRect.left = (int) (mRect.left + Math.sqrt(2) * relRadius * 1.0f / 2 - mImage.getWidth() * 1.0f / 2);
            mRect.top = (int) (mRect.top + Math.sqrt(2) * relRadius * 1.0f / 2 - mImage.getHeight() * 1.0f / 2);
            mRect.right = (int) (mRect.left + mImage.getWidth());
            mRect.bottom = (int) (mRect.top + mImage.getHeight());

        }
        // 绘图
        canvas.drawBitmap(mImage, null, mRect, mPaint);
    }

    private void drawOval(Canvas canvas, int centre, int radius)
    {
        /**
         * 根据需要画的个数以及间隙计算每个块块所占的比例*360
         */
        float itemSize = (360 * 1.0f - mCount * mSplitSize) / mCount;

        RectF oval = new RectF(centre - radius, centre - radius, centre + radius, centre + radius); // 用于定义的圆弧的形状和大小的界限

        mPaint.setColor(mFirstColor); // 设置圆环的颜色
        for (int i = 0; i < mCount; i++)
        {
            canvas.drawArc(oval, i * (itemSize + mSplitSize), itemSize, false, mPaint); // 根据进度画圆弧
        }

        mPaint.setColor(mSecondColor); // 设置圆环的颜色
        for (int i = 0; i < mCurrentCount; i++)
        {
            canvas.drawArc(oval, i * (itemSize + mSplitSize), itemSize, false, mPaint); // 根据进度画圆弧
        }
    }

    public void up()
    {
        mCurrentCount++;
        postInvalidate();
    }

    /**
     * 当前数量-1
     */
    public void down()
    {
        mCurrentCount--;
        postInvalidate();
    }

    private int xDown, xUp;

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {

        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                xDown = (int) event.getY();
                break;

            case MotionEvent.ACTION_UP:
                xUp = (int) event.getY();
                if (xUp > xDown)// 下滑
                {
                    down();
                } else
                {
                    up();
                }
                break;
        }

        return true;
    }
}
