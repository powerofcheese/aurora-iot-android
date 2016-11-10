package com.happylrd.aurora.todo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.happylrd.aurora.todo.colorMode.ColorHelper;

import java.util.List;

public class ModeView extends View {

    private static final String TAG = "ModeView";

    public final static int MY_NUM = 32;

    private Paint mPaint;

    private int[] Array_out;
    private String mTextPaint = "ModeName";
    private int mMinWidth = 1000;
    private int mWidth;
    private int mHeight;
    private ColorHelper colorHelper;
    private Typeface mTypeface;

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
        mTypeface = Typeface.SANS_SERIF;
        mPaint.setTextAlign(Paint.Align.CENTER);
    }

    /**
     * set color and name by patternName
     *
     * @param colors
     * @param name
     * @param patternName
     */
    public void setColorAndName(List<Integer> colors, String name, String patternName) {

        Log.d(TAG, "setColorAndName() called");

        switch (patternName) {
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

        Log.d(TAG, "onDraw() called");

        if (mPaint == null) {
            mPaint = new Paint();
        }

        canvas.drawColor(Color.argb(255, 80, 80, 80));
        mPaint.setColor(Color.argb(255, 60, 60, 60));
        canvas.drawRect(0, 0, mWidth, (float) (0.5 * mHeight), mPaint);

        mPaint.setStyle(Paint.Style.STROKE); //设置填充
        RectF oval1 = new RectF(-(int) (mHeight * 0.6), -(int) (mHeight * 1.2),
                (int) (1.8 * mHeight), (int) (mHeight * 1.2));
        int line1 = (int) (mHeight * 0.6);
        int line_string = mHeight / 60;
        mPaint.setStrokeWidth(line1);
        for (int i = 0; i < MY_NUM; i++) {
            mPaint.setColor(Array_out[i]);
            canvas.drawArc(oval1, (float) (i * 5.625), (float) 5.5, false, mPaint);
        }

        mPaint.setColor(Color.YELLOW);
        mPaint.setStrokeWidth((float) (mHeight * 0.06));
        canvas.drawCircle((int) (0.6 * mHeight), 0, (int) (0.85 * mHeight), mPaint);

        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth((float) (line_string * 1.5));
        canvas.drawCircle((int) (0.6 * mHeight), 0, (int) (1.5 * mHeight), mPaint);
        canvas.drawCircle((int) (0.6 * mHeight), 0, (int) (0.8 * mHeight), mPaint);
        canvas.drawLine(0, 4, mWidth, 4, mPaint);
        canvas.drawLine(0, mHeight - 4, mWidth, mHeight - 4, mPaint);
        canvas.drawLine((float) (0.9 * mWidth), (float) (0.8 * mHeight), (float) (0.95 * mWidth), (float) (0.8 * mHeight), mPaint);
        canvas.drawLine((float) (0.9 * mWidth), (float) (0.85 * mHeight), (float) (0.95 * mWidth), (float) (0.85 * mHeight), mPaint);
        canvas.drawLine((float) (0.9 * mWidth), (float) (0.9 * mHeight), (float) (0.95 * mWidth), (float) (0.9 * mHeight), mPaint);

        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setTypeface(mTypeface);
        mPaint.setTextSize(line_string * 12);
        mPaint.setStrokeWidth(line_string / 6);
        canvas.drawText(mTextPaint, (float) (0.7 * mHeight), (float) (0.3 * mHeight), mPaint);

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

    public void setName(String name) {
        mTextPaint = name;
        invalidate();
    }

    public void setColorByArray(int[] c) {
        Array_out = c;
        invalidate();
    }
}
