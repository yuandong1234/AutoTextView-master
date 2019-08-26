package com.yuong.textviewdemo;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * 自适应TextView
 */
public class AutoTextView extends AppCompatTextView {
    private final static String TAG = AutoTextView.class.getSimpleName();
    private int mLineSpacingExtra;//行间距
    private int mTextSize;//文字大小
    private int mTextColor;//文字颜色
    private int mPaddingLeft;//左内边距
    private int mPaddingTop;//上内边距
    private int mPaddingRight;//右内边距
    private int mPaddingBottom;//下内边距

    private int mViewWidth;
    private int mTextHeight;//文本高度
    private float mLineHeight;//每行高度
    private String mText;
    private List<String> mLines = new ArrayList<>();

    private Paint mPaint;

    public AutoTextView(Context context) {
        this(context, null);
    }

    public AutoTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AutoTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
        init();
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AutoTextView);
        mTextColor = typedArray.getColor(R.styleable.AutoTextView_textColor, Color.parseColor("#666666"));
        mPaddingLeft = (int) typedArray.getDimension(R.styleable.AutoTextView_paddingLeft, 0);
        mPaddingTop = (int) typedArray.getDimension(R.styleable.AutoTextView_paddingTop, 0);
        mPaddingRight = (int) typedArray.getDimension(R.styleable.AutoTextView_paddingRight, 0);
        mPaddingBottom = (int) typedArray.getDimension(R.styleable.AutoTextView_paddingBottom, 0);
        mLineSpacingExtra = (int) typedArray.getDimension(R.styleable.AutoTextView_lineSpacingExtra, 0);
        float textSize = typedArray.getFloat(R.styleable.AutoTextView_textSize, 14);
        mTextSize = sp2px(context, textSize);
        typedArray.recycle();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(mTextColor);
        mPaint.setTextSize(mTextSize);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mViewWidth = MeasureSpec.getSize(widthMeasureSpec);
        Log.e(TAG, "onMeasure....");
        Log.e(TAG, "mViewWidth : " + mViewWidth);
        computeLineTexts();
        Log.e(TAG, "mTextHeight : " + mTextHeight);
        if (mTextHeight != 0) {
            int height = mTextHeight + mPaddingTop + mPaddingBottom;
            setMeasuredDimension(mViewWidth, height);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.e(TAG, "onDraw....");
        drawText(canvas);
    }

    private void drawText(Canvas canvas) {
        if (mLines.size() > 0) {
            float startX = mPaddingLeft;
            float bottom = mPaint.getFontMetrics().bottom;
            for (int i = 0; i < mLines.size(); i++) {
                String text = mLines.get(i);
                /**
                 * 获取baseline     lineHeight * (i + 1) - bottom
                 * 行间距   mLineSpacingExtra * i
                 * view 上内边距   mPaddingTop
                 */
                float startY = mLineHeight * (i + 1) - bottom + mLineSpacingExtra * i + mPaddingTop;
                canvas.drawText(text, startX, startY, mPaint);
            }
        }
    }

    /**
     * 文本自适应宽度分成若干行
     *
     * @return
     */
    private void computeLineTexts() {

        mLines.clear();
        if (TextUtils.isEmpty(mText)) {
            return;
        }
        String lineText = "";//每一行要绘制的文本
        String tempText = mText;//需要绘制的文本

        /**
         * 获取实际最大可绘制的宽度
         */
        int innerWidth = mViewWidth - mPaddingLeft - mPaddingRight;
        if (mViewWidth == 0 || innerWidth == 0) return;
        /**
         * 循环计算文本的宽度，大于view的宽度时进行换行
         */
        while (!TextUtils.isEmpty(tempText)) {
            for (int i = 0; i < tempText.length(); i++) {
                String temp = tempText.substring(0, i + 1);
                /**
                 * 获取文本的宽度
                 * 方法 1   mPaint.measureText();
                 * 方法 2   mPaint.getTextBounds();
                 * 注意：方法1得到的值大于方法2得到的值
                 */
                float width = mPaint.measureText(temp);
//                Rect mBounds = new Rect();
//                mPaint.getTextBounds(temp, 0, temp.length(), mBounds);
//                float width = mBounds.width();
                if (width > innerWidth) {
                    lineText = tempText.substring(0, i);
                    mLines.add(lineText);
                    tempText = tempText.substring(i);
                    break;
                } else {
                    if (i == tempText.length() - 1) {
                        mLines.add(temp);
                        tempText = "";
                    }
                }
            }
        }

        Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
        float bottom = fontMetrics.bottom;
        float top = fontMetrics.top;
        mLineHeight = bottom - top;//每一行文本的高度

        float textHeight = mLines.size() * mLineHeight + (mLines.size() - 1) * mLineSpacingExtra;
        mTextHeight = Math.round(textHeight);
    }

    public void setText(String text) {
        this.mText = text;
        Log.e(TAG, "AutoTextView set txt.");
        if (mViewWidth > 0) {
            requestLayout();
        }
    }

    public int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
}
