package com.happylrd.aurora.todo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class ModeView extends View {

    public final static int MY_NUM = 32;

    private Paint mPaint; //画笔,包含了画几何图形、文本等的样式和颜色信息

    private int[] Array_out;
    private String mTextPaint = "我是图片名称";
    private int mMinWidth = 1000;
    private int mWidth;
    private int mHeight;

    public ModeView(Context context) {
        super(context);
        init();
    }

    public ModeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void init() {
        mPaint = new Paint();

        /**设置数组的初始填充颜色*/
        Array_out = new int[MY_NUM];
        for (int i = 0; i < MY_NUM; i++) {
            Array_out[i] = Color.CYAN;
        }
    }

    public void update(int[] c, String s) {
        Array_out = c;
        mTextPaint = s;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(mPaint == null){
            mPaint  = new Paint();
            mPaint.setStyle(Paint.Style.STROKE); //设置填充
        }
        canvas.drawColor(Color.GRAY);

        RectF oval1 = new RectF(-(int)(mHeight * 0.8),-(int)(mHeight * 0.25),
                (int)(0.8 * mHeight),(int)(mHeight * 0.95));
        RectF oval2 = new RectF(-(int)(mHeight * 0.3),(int)(mHeight * 0.25),
                (int)(mHeight * 0.3),(int)(mHeight * 0.45));
        RectF oval3 = new RectF(-(int)(mHeight * 1.05),-(int)(mHeight * 0.5),
                (int)(mHeight * 1.05),(int)(mHeight * 1.2));
        int line1 = mHeight / 2;
        int line_string = mHeight / 60;
        mPaint.setStrokeWidth(line1);
        for (int i = 0; i < MY_NUM; i++) {
            mPaint.setColor(Array_out[i]);
            canvas.drawArc(oval1, -90 + i * 6, (float) 5.5, false, mPaint);
        }
        mPaint.setColor(Color.BLACK);
        canvas.drawArc(oval2, 0, 360, false, mPaint);
        mPaint.setStrokeWidth(line_string);
        canvas.drawArc(oval3, 0, 360, false, mPaint);
        mPaint.setTextSize(line_string * 15);
        mPaint.setStrokeWidth(line_string / 2);
        canvas.drawText(mTextPaint,  (float) (0.4 * mWidth),(float) (0.45 * mHeight), mPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWidth(widthMeasureSpec),
                measureHeight(heightMeasureSpec, widthMeasureSpec));
    }

    private int measureHeight(int measureSpec, int widthMeasureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            // We were told how big to be
            result = specSize;
        } else {
            // Measure the text
            result = (int) (measureWidth(widthMeasureSpec) / 3) + getPaddingTop()
                    + getPaddingBottom();
            if (specMode == MeasureSpec.AT_MOST) {
                // Respect AT_MOST value if that was what is called for by measureSpec
                result = Math.min(result, specSize);
            }
        }
        mHeight = result - getPaddingTop() - getPaddingBottom();
        return result;
    }

    private int measureWidth(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            // We were told how big to be
            result = specSize;
        } else {
            // Measure the text (beware: ascent is a negative number)
            result = mMinWidth + getPaddingLeft()
                    + getPaddingRight();
            if (specMode == MeasureSpec.AT_MOST) {
                // Respect AT_MOST value if that was what is called for by measureSpec
                result = Math.min(result, specSize);
            }
        }
        mWidth = result - getPaddingLeft() - getPaddingRight();
        return result;
    }
}
