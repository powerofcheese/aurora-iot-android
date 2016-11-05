package com.happylrd.aurora.todo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.happylrd.aurora.todo.colorMode.ColorHelper;

import java.util.List;

public class ModeView extends View {

    public final static int MY_NUM = 32;

    private Paint mPaint;

    private int[] Array_out;
    private String mTextPaint = "ModeName";
    private int mMinWidth = 1000;
    private int mWidth;
    private int mHeight;
    private ColorHelper colorHelper;

    public ModeView(Context context) {
        super(context);
        init();
    }

    public ModeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPaint = new Paint();

        /**设置数组的初始填充颜色*/
        Array_out = new int[MY_NUM];
        for (int i = 0; i < MY_NUM; i++) {
            Array_out[i] = Color.CYAN;
        }
        colorHelper = new ColorHelper();
    }

    /**
     * set color and name by patternName
     * @param colors
     * @param name
     * @param patternName
     */
    public void setColorAndName(List<Integer> colors, String name, String patternName){
        switch(patternName) {
            case "Single":
                Array_out = colorHelper.Single(colors);
                break;
            case "Gradation":
                Array_out = colorHelper.gradation(colors);
                break;
            case "ThinStripe":
                Array_out = colorHelper.thinStripe(colors);
                break;
            case "ThickStripe":
                Array_out = colorHelper.thickStripe(colors);
                break;
            case "HalfStripe":
                Array_out = colorHelper.halfStripe(colors);
                break;
            case "SkipStripe":
                Array_out = colorHelper.gradationskip(colors);
                break;
            case "GradationSkipping":
                Array_out = colorHelper.gradationskip(colors);
                break;
            case "Random":
                Array_out = colorHelper.random();
                break;
            case "RandomSkipping":
                Array_out = colorHelper.randomskip();
                break;
            case "Similar":
                Array_out = colorHelper.similar(colors);
                break;
            case "Custom":
                for (int i = 0; i < MY_NUM; i++) {
                    Array_out[i] = colors.get(i);
                }
                break;
            default:
                break;

        }
        mTextPaint = name;
        invalidate();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mPaint == null) {
            mPaint = new Paint();
        }

        mPaint.setStyle(Paint.Style.STROKE); //设置填充
        canvas.drawColor(Color.GRAY);

        RectF oval1 = new RectF(-(int) (mHeight * 0.3), 0,
                (int) (0.5 * mHeight), (int) (mHeight * 0.8));
        RectF oval2 = new RectF(-(int) (mHeight * 0.1), (int) (mHeight * 0.3),
                (int) (mHeight * 0.1), (int) (mHeight * 0.5));
        RectF oval3 = new RectF(-(int) (mHeight * 0.55), -(int) (mHeight * 0.25),
                (int) (mHeight * 0.75), (int) (mHeight * 1.05));
        RectF oval4 = new RectF(-(float)(mHeight*0.32),(int)(mHeight*0.08),(int)(mHeight*0.32),(int)(mHeight*0.72));
        int line1 = (int) (mHeight * 0.5);
        int line_string = mHeight / 60;
        mPaint.setStrokeWidth(line1);
        for (int i = 0; i < MY_NUM; i++) {
            mPaint.setColor(Array_out[i]);
            canvas.drawArc(oval1, (float) (-120 + i * 7.5), (float) 7.3, false, mPaint);
        }

        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth((float) (line1 * 0.8));
        canvas.drawArc(oval2, 0, 360, false, mPaint);

        mPaint.setStrokeWidth(line_string);
        canvas.drawArc(oval3, 0, 360, false, mPaint);
        canvas.drawLine(0, 4, mWidth, 4, mPaint);
        canvas.drawLine(0, mHeight - 4, mWidth, mHeight - 4, mPaint);

        /** mPaint.setColor(Color.YELLOW);
         mPaint.setStrokeWidth((float) (mHeight * 0.1));
         canvas.drawArc(oval4, 0, 360, false, mPaint);
         */

        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setTextSize(line_string * 15);
        mPaint.setStrokeWidth(line_string / 2);
        canvas.drawText(mTextPaint, (float) (0.4 * mWidth), (float) (0.46 * mHeight), mPaint);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(
                measureWidth(widthMeasureSpec),
                measureHeight(heightMeasureSpec, widthMeasureSpec)
        );
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
            result = (int) (measureWidth(widthMeasureSpec) / 3)
                    + getPaddingTop() + getPaddingBottom();
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
            result = mMinWidth + getPaddingLeft() + getPaddingRight();
            if (specMode == MeasureSpec.AT_MOST) {
                // Respect AT_MOST value if that was what is called for by measureSpec
                result = Math.min(result, specSize);
            }
        }
        mWidth = result - getPaddingLeft() - getPaddingRight();
        return result;
    }

    public void update(int[] c, String s) {
        Array_out = c;
        mTextPaint = s;
        invalidate();
    }

    public void setName(String name){
        mTextPaint = name;
        invalidate();
    }

    public void setColorByArray(int[] c){
        Array_out = c;
        invalidate();
    }
}
