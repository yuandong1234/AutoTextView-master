package com.yuong.textviewdemo;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * 自适应TextView
 */
public class AutoTextView extends View {
    private final static String TAG = AutoTextView.class.getSimpleName();
    private int mLineSpacingExtra = 5;//行间距
    private int mLineHeight;//行高
    private int mTextSize;//文字大小
    private int mTextColor;//文字颜色
    private int mPaddingLeft;//左内边距
    private int mPaddingTop;//上内边距
    private int mPaddingRight;//右内边距
    private int mPaddingBottom;//下内边距

    private int mViewWidth;
    private int mTextHeight;//文本高度
    private String mText;

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
        Log.e(TAG, "mPaddingBottom : " + mPaddingBottom);
        float textSize = typedArray.getFloat(R.styleable.AutoTextView_textSize, 14);
        mLineHeight = mTextSize = sp2px(context, textSize);
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
        Log.e(TAG, "mViewWidth : " + mViewWidth);
        int height = mTextHeight != 0 ? (mTextHeight + mPaddingTop + mPaddingBottom) : (1 + mPaddingTop + mPaddingBottom);
        setMeasuredDimension(mViewWidth, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawText(canvas);
    }

    private void drawText(Canvas canvas) {
        List<String> lines = computeLineTexts();
        if (lines != null) {
            for (int i = 0; i < lines.size(); i++) {
                String text = lines.get(i);
                float startX = mPaddingLeft;
                float startY = mLineHeight * (i + 1) + mLineSpacingExtra * i + mPaddingTop;
                canvas.drawText(text, startX, startY, mPaint);
            }
            mTextHeight = lines.size() * mLineHeight + (lines.size() - 1) * mLineSpacingExtra;
            requestLayout();
        }
    }

    /**
     * 文本自适应宽度分成若干行
     *
     * @return
     */
    private List<String> computeLineTexts() {
        if (TextUtils.isEmpty(mText)) {
            return null;
        }
        List<String> lines = new ArrayList<>();

        String lineText = "";//每一行要绘制的文本
        String tempText = mText;//需要绘制的文本


        /**
         * 获取实际最大可绘制的宽度
         */
        int innerWidth = mViewWidth - mPaddingLeft - mPaddingRight;

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
                    lines.add(lineText);
                    tempText = tempText.substring(i);
                    break;
                } else {
                    if (i == tempText.length() - 1) {
                        lines.add(temp);
                        tempText = "";
                    }
                }
            }
        }
        return lines;
    }

    public void setText(String text) {
        this.mText = text;
        invalidate();
    }

    public int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
}
