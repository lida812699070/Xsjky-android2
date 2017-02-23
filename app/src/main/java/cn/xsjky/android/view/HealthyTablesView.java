package cn.xsjky.android.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import cn.xsjky.android.R;

/**
 * Created by ${lida} on 2016/11/18.
 */
public class HealthyTablesView extends ViewGroup {

    private static final String TAG = "tag";

    private int mWidth;

    private int mHeight;

    /**
     * 坐标轴宽度
     */
    private int mCoordinatesLineWidth;

    /**
     * 坐标旁边文字颜色
     */
    private int mCoordinatesTextColor;

    /**
     * 坐标旁边文字大小
     */
    private int mCoordinatesTextSize;

    /**
     * 折线颜色
     */
    private int mLineColor;

    /**
     * 圆点半径
     */
    private int mCircleradius;

    /**
     * 背景色
     */
    private int mBgColor;

    /**
     * 折线宽度
     */
    private int mLineWidth;

    /**
     * 小圆填充色
     */
    private int mMincircleColor;

    /**
     * 绘制类型,比如,画步数,画心率,画睡眠等
     */
    private String mDrawType;

    /**
     * 大圆点填充色
     */
    private int mMaxcircleColor;

    private Paint xyPaint;

    private Rect textBound;

    private String[] weeks;

    private Paint textPaint;

    private int[] values;

    private Paint linePaint;

    private Paint maxCirclePaint;

    private Paint minCirclePaint;

    private int XScale;

    public HealthyTablesView(Context context) {
        this(context, null);
    }

    public HealthyTablesView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HealthyTablesView(Context context, AttributeSet attrs,
                             int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray array = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.HealthTableView, defStyleAttr, 0);
        int index = array.getIndexCount();
        for (int i = 0; i < index; i++) {
            int attr = array.getIndex(i);

            switch (attr) {
                case R.styleable.HealthTableView_coordinatesLineWidth:
                    // 这里将以px为单位,默认值为2px;
                    mCoordinatesLineWidth = array.getDimensionPixelSize(attr,
                            (int) TypedValue.applyDimension(
                                    TypedValue.COMPLEX_UNIT_PX, 2,
                                    getResources().getDisplayMetrics()));
                    break;
                case R.styleable.HealthTableView_coordinatesTextColor:
                    mCoordinatesTextColor = array.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.HealthTableView_coordinatesTextSize:
                    mCoordinatesTextSize = array.getDimensionPixelSize(attr,
                            (int) TypedValue.applyDimension(
                                    TypedValue.COMPLEX_UNIT_SP, 11,
                                    getResources().getDisplayMetrics()));
                    break;
                case R.styleable.HealthTableView_lineColor:
                    mLineColor = array.getColor(attr, Color.BLUE);
                    break;
                case R.styleable.HealthTableView_averageCircleradius:
                    mCircleradius = array.getDimensionPixelSize(attr,
                            (int) TypedValue.applyDimension(
                                    TypedValue.COMPLEX_UNIT_DIP, 10,
                                    getResources().getDisplayMetrics()));
                    break;
                case R.styleable.HealthTableView_bgColor:
                    mBgColor = array.getColor(attr, Color.WHITE);
                    break;
                case R.styleable.HealthTableView_lineWidth:
                    mLineWidth = array.getDimensionPixelSize(attr,
                            (int) TypedValue.applyDimension(
                                    TypedValue.COMPLEX_UNIT_DIP, 11,
                                    getResources().getDisplayMetrics()));
                    break;
                case R.styleable.HealthTableView_maxcircleColor:
                    mMaxcircleColor = array.getColor(attr, Color.GREEN);
                    break;
                case R.styleable.HealthTableView_mincircleColor:
                    mMincircleColor = array.getColor(attr, Color.WHITE);
                    break;
                case R.styleable.HealthTableView_tableType:
                    mDrawType = array.getString(attr);
                    break;
            }
        }
        // 记得释放资源
        array.recycle();

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    private void init() {

        xyPaint = new Paint();
        xyPaint.setAntiAlias(true);
        xyPaint.setColor(mCoordinatesTextColor);
        xyPaint.setStrokeWidth(mCoordinatesLineWidth);
        xyPaint.setStyle(Paint.Style.FILL);

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(mCoordinatesTextColor);
        textPaint.setTextSize(mCoordinatesTextSize);
        textPaint.setStyle(Paint.Style.STROKE);
        textBound = new Rect();

        minCirclePaint = new Paint();
        minCirclePaint.setStyle(Paint.Style.FILL);
        minCirclePaint.setColor(Color.WHITE);
        minCirclePaint.setAntiAlias(true);

        maxCirclePaint = new Paint();
        maxCirclePaint.setStyle(Paint.Style.FILL);
        maxCirclePaint.setColor(mMaxcircleColor);
        maxCirclePaint.setAntiAlias(true);

        linePaint = new Paint();
        linePaint.setColor(mLineColor);
        linePaint.setStrokeWidth(mLineWidth);
        linePaint.setAntiAlias(true);
        linePaint.setStyle(Paint.Style.STROKE);

    }


    public void setData(String[] weeks, int[] values) {
        this.weeks = weeks;
        this.values = values;
        init();
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        /**
         * 自定义控件的宽高必须由调用者自己指定具体的数值
         */
        if (widthSpecMode == MeasureSpec.EXACTLY) {
            mWidth = widthSpecSize;
        } else {
            mWidth = 300;
        }

        if (heightSpecMode == MeasureSpec.EXACTLY) {
            // 高是宽的3/5,这样好吗?
            mHeight = (mWidth / 5) * 3;
        } else {
            mHeight = 230;
        }
        Log.i(TAG, "width=" + mWidth + "...height=" + mHeight);
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (weeks == null || values == null) {
            return;
        }
        XScale = (mWidth - getPaddingRight() - getPaddingLeft() - 40) / weeks.length;
        canvas.drawColor(mBgColor);
        drawCoordinates(canvas);
        drawCoordinatesXvalues(canvas);
        drawTypeValues(canvas);
    }

    private void drawTypeValues(Canvas canvas) {

        int[] YValues = cacluterYValues(getArrayMax(), getArrayMin());

        for (int i = 0; i < YValues.length; i++) {
            Log.i(TAG, mDrawType + YValues[i]);
        }
        // YValues[4]-YValues[0] 差值就是全部高度
        drawYValues(canvas, YValues[4] - YValues[0], YValues);
        drawLine(canvas, YValues[4] - YValues[0], YValues[0]);

    }

    /**
     * @param canvas
     * @param arraymax
     * @param yMin
     */
    public ArrayList<String> points = new ArrayList<>();

    public ArrayList<String> getPoints() {
        return points;
    }

    public void setPoints(ArrayList<String> points) {
        this.points = points;
    }

    private void drawLine(Canvas canvas, float arraymax, float yMin) {

        // 这里是整个Y轴可用高度除以最大值,就是每个值占有刻度上的几等分;
        float YScale = ((mHeight - getPaddingBottom() - getPaddingTop() - 40))
                / arraymax;
        for (int i = 0; i < values.length; i++) {
            // 为什么是values[i] - arraymin(数据值-Y坐标最小值)?
            // 因为圆点是以数据值来画得,数据值和Y轴坐标最小值的差就是整个数据的区间;
            int scale = (int) (values[i] - yMin);

            int j;
            /**
             * 画折线
             */
            if (i < values.length - 1) {
                int textScale = (int) (values[i + 1] - yMin);
                j = i + 1;
                canvas.drawLine(getPaddingLeft() + (XScale * i),
                        mHeight - getPaddingBottom() - (YScale * scale),
                        getPaddingLeft() + (XScale * j),
                        mHeight - getPaddingBottom() - (YScale * textScale),
                        linePaint);

                if (i == 0) {
                    points.add((float) (getPaddingLeft() + (XScale * i))
                            + "," + (float) (mHeight - getPaddingBottom() - (YScale * scale)));
                    points.add((float) (getPaddingLeft() + (XScale * j))
                            + "," + (float) (mHeight - getPaddingBottom() - (YScale * textScale)));
                } else {
                    points.add((float) (getPaddingLeft() + (XScale * j))
                            + "," + (float) (mHeight - getPaddingBottom() - (YScale * textScale)));
                }

            }

            String text = String.valueOf(values[i]);
            textPaint.getTextBounds(text, 0, text.length(), textBound);
            canvas.drawText(text,
                    getPaddingLeft() + (XScale * i) - textBound.width() / 2,
                    mHeight - getPaddingBottom() - (YScale * scale) - 15,
                    textPaint);

            /**
             * 两个小圆点
             */
            canvas.drawCircle(getPaddingLeft() + (XScale * i),
                    mHeight - getPaddingBottom() - (YScale * scale), 10,
                    maxCirclePaint);

            canvas.drawCircle(getPaddingLeft() + (XScale * i),
                    mHeight - getPaddingBottom() - (YScale * scale), 10 - 2,
                    minCirclePaint);

        }

    }

    /**
     * 画Y轴上的数值
     *
     * @param canvas
     */
    private void drawYValues(Canvas canvas, float max, int[] value) {
        // 这里除以max这个最大值是为了有多大的去见就分成多少等分,是的后面折线的点更精准,否者就会对不齐刻度,
        float YScale = ((float) mHeight - getPaddingBottom() - getPaddingTop()
                - 40) / max;
        for (int i = 0; i < value.length; i++) {
            Log.i(TAG, "activity=" + value[i] / 1000f);

            String text = mDrawType.equals("activity") ? String.format("%.1f", value[i] / 1000f) + "k"
                    : value[i] + "";
            int scale = value[i] - value[0];
            textPaint.getTextBounds(text, 0, text.length(), textBound);
            // +textBound.height()/2 主要是为了让字体和间断线居中
            canvas.drawText(text,
                    getPaddingLeft() - 100, mHeight - getPaddingBottom()
                            - (YScale * scale) + textBound.height() / 2,
                    textPaint);
            // 和X轴重合的线不画
            if (i != 0) {
                canvas.drawLine(getPaddingLeft(),
                        mHeight - getPaddingBottom() - (YScale * scale),
                        mWidth - getPaddingRight(),
                        mHeight - getPaddingBottom() - (YScale * scale),
                        xyPaint);
            }
        }

    }

    /**
     * 绘制X轴上的数值
     *
     * @param canvas
     */
    private void drawCoordinatesXvalues(Canvas canvas) {
        // -40 为X轴留点边界。 /6分成7等分

        for (int i = 0; i < weeks.length; i++) {
            textPaint.getTextBounds(weeks[i], 0, weeks[i].length(), textBound);
            // 画建断线
            canvas.drawLine(getPaddingLeft() + (i * XScale),
                    mHeight - getPaddingBottom() - 10,
                    getPaddingLeft() + (i * XScale),
                    mHeight - getPaddingBottom(), xyPaint);
            // -textBound.width()/2 是为了让字体居中与间断线
            canvas.drawText(weeks[i],
                    getPaddingLeft() + (i * XScale) - textBound.width() / 2,
                    mHeight - getPaddingBottom() + 30, textPaint);
        }

    }

    /**
     * 画坐标系
     *
     * @param canvas
     */
    private void drawCoordinates(Canvas canvas) {

        // X轴
        Log.i(TAG, "drawCoordinates");
        canvas.drawLine(getPaddingLeft(), mHeight - getPaddingBottom(),
                mWidth - getPaddingRight(), mHeight - getPaddingBottom(),
                xyPaint);
        // X轴上的箭头
        canvas.drawLine(mWidth - getPaddingRight() - 20,
                mHeight - getPaddingBottom() - 10, mWidth - getPaddingRight(),
                mHeight - getPaddingBottom(), xyPaint);
        canvas.drawLine(mWidth - getPaddingRight() - 20,
                mHeight - getPaddingBottom() + 10, mWidth - getPaddingRight(),
                mHeight - getPaddingBottom(), xyPaint);

        // 绘制Y轴
        canvas.drawLine(getPaddingLeft(), getPaddingTop(), getPaddingLeft(),
                mHeight - getPaddingBottom(), xyPaint);

        // Y轴上的箭头
        canvas.drawLine(getPaddingLeft() - 10, getPaddingTop() + 20,
                getPaddingLeft(), getPaddingTop(), xyPaint);
        canvas.drawLine(getPaddingLeft() + 10, getPaddingTop() + 20,
                getPaddingLeft(), getPaddingTop(), xyPaint);
    }

    /**
     * 获取数值中的最大值
     *
     * @return
     */
    private float getArrayMax() {

        float max = 0;
        for (int i = 0; i < values.length; i++) {
            float pre = Float.parseFloat(values[i] + "");
            max = Math.max(max, pre);
        }
        return max;
    }

    /**
     * 获取最小值
     *
     * @return
     */
    private float getArrayMin() {
        float min = 999999;
        for (int i = 0; i < values.length; i++) {
            float pre = Float.parseFloat(values[i] + "");
            min = Math.min(min, pre);
        }

        return min;
    }

    /**
     * 传入数组中的最大值和最小值,计算出在Y轴上数值的区间
     *
     * @param max
     * @param min
     * @return
     */
    private int[] cacluterYValues(float max, float min) {
        int[] values;
        int min1;
        int max1;
        int resultNum = getResultNum(min); // 计算出的最小值
        max1 = getResultNum(max); // 计算出最大值
        if (resultNum <= 20) // 如果小于等于20 就不要减20,否则Y最小值是0了
        {
            min1 = resultNum - 10;
        } else {

            min1 = resultNum - 20;
        }

        //步行特殊处理
        if (resultNum >= 1000) {
            min1 = resultNum - 1000;
        }

        if (resultNum <= 10 || resultNum == 0) // 如果小于10 就不用再减了,否则就是负数了
        {
            min1 = 0;
        }

        // 将计算出的数值均分为5等分
        double ceil = Math.ceil((max1 - min1) / 4);
        values = new int[]
                {min1, (int) (min1 + ceil), (int) (min1 + ceil * 2),
                        (int) (min1 + ceil * 3), (int) (min1 + ceil * 4)};
        return values;

    }

    /**
     * 最高位 为什么要取出最高值,这里主要是通过计算动态的算出Y轴上的数值区间,
     * 比如心率是60-100,不计算给死就是0-180,这样折线的所有点就全部落在中间一点的地带,上下都有较大的空白,影响美观(心率一般在60-
     * 100之间) 比如计步的幅度很大,如果不通过动态计算就不知道Y轴画的数值给多少合适,比如Y轴数值写死为0-20000,
     * 那么如果运动量偏少,比如都是1000步左右,折线就显得几乎和X=0平齐了
     *
     * @param num
     * @return
     */
    private int getResultNum(float num) {
        int resultNum;
        int gw = 0; // 个位
        int sw = 0; // 十位
        int bw = 0; // 百位
        int qw = 0; // 千位
        int ww = 0; // 万位

        if (num > 0) {
            gw = (int) (num % 10 / 1);
        }
        if (num > 10) {
            sw = (int) (num % 100 / 10);
        }

        if (num > 100) {
            bw = (int) (num % 1000 / 100);
        }

        if (num > 1000) {
            qw = (int) (num % 10000 / 1000);
        }

        if (num > 10000) {
            ww = (int) (num % 100000 / 10000);
        }
        /*********************************/
        if (ww >= 1) {
            resultNum = qw > 5 ? ww * 10000 + 10000 : ww * 10000 + 5000;
        } else if (qw >= 1) {
            resultNum = bw > 5 ? qw * 1000 + 1000 : qw * 1000 + 500;
        } else if (bw >= 1) {
            resultNum = bw * 100 + sw * 10 + 10;

        } else if (sw >= 1) {

            resultNum = gw > 5 ? sw * 10 + 20 : sw * 10 + 10;
        } else {
            resultNum = 0;
        }

        return resultNum;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                float xDown = getX();
                float yDown = getY();

                break;
        }
        return super.onTouchEvent(event);
    }
}