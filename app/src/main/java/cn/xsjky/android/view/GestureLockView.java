package cn.xsjky.android.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.View;

/**
 * Created by ${lida} on 2016/8/10.
 */
public class GestureLockView extends View {

    private final Paint mPaint;
    private final Path mArrowPath;
    private int mColorNoFingerInner;
    private int mColorNoFingerOutter;
    private int mColorFingerOn;
    private int mColorFingerUp;
    private int mWidth;
    private int mHeight;
    /**
     * 圆心坐标
     */
    private int mCenterX;
    private int mCenterY;
    /**
     * 外圆半径
     */
    private int mRadius;
    private float mArrowRate = 0.333f;
    private int mArrowDegree = -1;
    private int mStrokeWidth = 2;
    enum Mode {
        STATUS_NO_FINGER, STATUS_FINGER_ON, STATUS_FINGER_UP;
    }

    /**
     * GestureLockView的当前状态
     */
    private Mode mCurrentStatus = Mode.STATUS_NO_FINGER;

    public GestureLockView(Context context , int colorNoFingerInner , int colorNoFingerOutter , int colorFingerOn , int colorFingerUp )
    {
        super(context);
        this.mColorNoFingerInner = colorNoFingerInner;
        this.mColorNoFingerOutter = colorNoFingerOutter;
        this.mColorFingerOn = colorFingerOn;
        this.mColorFingerUp = colorFingerUp;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mArrowPath = new Path();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
        mWidth = mWidth < mHeight ? mWidth : mHeight;
        mRadius = mCenterX = mCenterY = mWidth / 2;
        float mArrowLength = mWidth / 2 * mArrowRate;
        mArrowPath.moveTo(mWidth / 2, mStrokeWidth + 2);
        mArrowPath.lineTo(mWidth / 2 - mArrowLength, mStrokeWidth + 2
                + mArrowLength);
        mArrowPath.lineTo(mWidth / 2 + mArrowLength, mStrokeWidth + 2
                + mArrowLength);
        mArrowPath.close();
        mArrowPath.setFillType(Path.FillType.WINDING);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        switch (mCurrentStatus){
            case STATUS_FINGER_ON:
                //绘制外圆
                break;
            case STATUS_FINGER_UP:
                //绘制外圆
                break;
            case STATUS_NO_FINGER:

                break;
        }
    }
}
