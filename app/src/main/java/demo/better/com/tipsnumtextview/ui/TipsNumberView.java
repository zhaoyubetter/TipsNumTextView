package demo.better.com.tipsnumtextview.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import demo.better.com.tipsnumtextview.R;

/**
 * 显示数字角标textview
 * Created by zhaoyu1 on 2016/9/8.
 */
public class TipsNumberView extends View {

    private final String HOLDER = "99";
    private final int ROUND_ANGLE = 12;

    private final int TYPE_FIll = 0;
    private final int TYPE_STOKEN = 1;

    /**
     * 颜色
     */
    private int mColor = Color.RED;

    /**
     * 描边颜色
     */
    private int mSideColor = Color.TRANSPARENT;

    /**
     * 类型
     */
    private int mType = TYPE_FIll;
    /**
     * 变宽，如果是过fill类型，sideSize设置无效
     */
    private int mSideSize = 0;

    private Paint mPaint;

    private boolean mIsCircle = true;

    private float mRound;

    private int mTextSize;

    private int mTextColor;

    private String mText;

    private Rect mRect = new Rect();


    public TipsNumberView(Context context) {
        this(context, null);
    }

    public TipsNumberView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TipsNumberView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        // 1.获取自定义属性
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TipsNumberTextView, defStyleAttr, 0);
        try {
            mColor = a.getColor(R.styleable.TipsNumberTextView_tips_color, Color.RED);
            mType = a.getInt(R.styleable.TipsNumberTextView_tips_type, TYPE_FIll);
            mSideSize = a.getDimensionPixelSize(R.styleable.TipsNumberTextView_tips_stroke_size, 0);
            mSideColor = a.getColor(R.styleable.TipsNumberTextView_tips_color_stroke, Color.TRANSPARENT);

            mTextSize = a.getDimensionPixelSize(R.styleable.TipsNumberTextView_tips_text_size, 12);
            mTextColor = a.getColor(R.styleable.TipsNumberTextView_tips_color_stroke, Color.WHITE);
            mText = a.getString(R.styleable.TipsNumberTextView_tips_text);
            if (mText == null) {
                mText = "";
            }
        } finally {
            if (a != null) {
                a.recycle();
            }
        }

        initView(context);
    }

    private void initView(Context ctx) {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRound = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, ROUND_ANGLE, ctx.getResources().getDisplayMetrics());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        mPaint.setTextSize(mTextSize);
        mPaint.getTextBounds(mText, 0, mText.length(), mRect);

        switch (widthMode) {
            case MeasureSpec.EXACTLY:
                width = widthSize;
                break;
            default:
                width = mRect.width() + getPaddingLeft() + getPaddingRight();
                break;
        }

        switch (heightMode) {
            case MeasureSpec.EXACTLY:
                height = heightSize;
                break;
            default:
                height = mRect.height() + getPaddingTop() + getPaddingBottom();
                break;
        }


        // 宽度 = 高度，圆形； 宽度 > 高度，圆角矩形
        // 如果 内容的长度，小于 holder 占位符，取 2着 最大值，否则设置宽度，不能超过最大宽度
        if (mText.length() <= HOLDER.length()) {
            height = Math.max(width, height);
            mIsCircle = true;
        } else {
            mIsCircle = false;
        }

        setMeasuredDimension(resolveSize(width, widthMeasureSpec), resolveSize(height, heightMeasureSpec));
    }

    /**
     * 设置显示内容
     *
     * @param text
     */
    public void setText(String text) {
        this.mText = text;
        requestLayout();
        invalidate();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mType == TYPE_FIll) {
            mPaint.setColor(mColor);
            mPaint.setStyle(Paint.Style.FILL);
            drawBg(canvas, mPaint);
        } else {
            mPaint.setColor(mColor);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(mSideSize);
            drawBg(canvas, mPaint);
        }

        mPaint.setColor(mTextColor);
        canvas.drawText(mText, (getWidth() - mRect.width()) / 2, (getHeight() + mRect.height()) / 2, mPaint);
    }

    /**
     * 画背景
     */
    private void drawBg(Canvas canvas, Paint paint) {
        // 去掉描边宽度
        int radius = getWidth() / 2 - mSideSize;
        if (mIsCircle) {                // 画圆
            canvas.drawCircle(radius + mSideSize, radius + mSideSize, radius - mSideSize, paint);
            if (mType == TYPE_FIll && mSideSize > 0) {   // 填充带描边
                paint.setColor(mSideColor);
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(mSideSize);
                canvas.drawCircle(radius + mSideSize, radius + mSideSize, radius - mSideSize, paint);
            }
        } else {                // 圆角矩形
            RectF rectF = new RectF(0 + mSideSize, 0 + mSideSize, getWidth() - mSideSize, getHeight() - mSideSize);
            canvas.drawRoundRect(rectF, mRound, mRound, paint);
            if (mType == TYPE_FIll && mSideSize > 0) {   // 填充带描边
                paint.setStyle(Paint.Style.STROKE);
                paint.setColor(mSideColor);
                paint.setStrokeWidth(mSideSize);
                canvas.drawRoundRect(rectF, mRound, mRound, paint);
            }
        }
    }
}
