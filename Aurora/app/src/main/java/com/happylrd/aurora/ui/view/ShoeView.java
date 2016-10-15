package com.happylrd.aurora.ui.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class ShoeView extends View {

    private LayerDrawable mDrawables;

    public final static int NUM = 32;

    private String[] LC = new String[NUM];
    private int tempColor = Color.WHITE;

    private int temp = 0;
    private String send;

    public ShoeView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mDrawables = (LayerDrawable) getBackground();
        for (int i = 0; i < NUM; i++) {
            LC[i] = colorToHexString(Color.BLACK);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Drawable drawable = null;
        Bitmap bitmap = null;
        Bitmap rbitmap = null;
        int hit = 0;

        final float x = event.getX();
        final float y = event.getY();

        int i;
        if (temp >= NUM - 1) {
            i = NUM - 1;
        } else {
            i = temp + 1;
        }

        if (i < 2) {
            i = 2;
        }
        for (int t = 3; t > 0; i--, t--) {
            drawable = mDrawables.getDrawable(i);
            rbitmap = Bitmap.createScaledBitmap(((BitmapDrawable) drawable).getBitmap(), this.getWidth(), this.getHeight(), true);
            try {
                int pixel = rbitmap.getPixel((int) x, (int) y);
                if (pixel == Color.TRANSPARENT) {
                    continue;
                } else {
                    hit = 1;
                    break;
                }
            } catch (Exception e) {
                continue;
            }
        }
        if (hit == 1) {
            if (temp == i)
                return true;
            drawable.setColorFilter(tempColor, PorterDuff.Mode.SRC_IN);
            String HexColor = colorToHexString(tempColor);
            LC[i] = HexColor;
            rbitmap = null;

            temp = i;
            return true;
        }
        i = NUM - 1;
        for (; i >= 0; i--) {
            drawable = mDrawables.getDrawable(i);
            rbitmap = Bitmap.createScaledBitmap(((BitmapDrawable) drawable).getBitmap(), this.getWidth(), this.getHeight(), true);

            try {
                int pixel = rbitmap.getPixel((int) x, (int) y);
                if (pixel == Color.TRANSPARENT) {
                    continue;
                } else {
                    break;
                }
            } catch (Exception e) {
                continue;
            }
        }
        if (i >= 0) {
            temp = i;
            drawable.setColorFilter(tempColor, PorterDuff.Mode.SRC_IN);
            String HexColor = colorToHexString(tempColor);
            LC[i] = HexColor;

        }
        rbitmap = null;
        return true;

    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    public String[] getLC() {
        return LC;
    }

    public void setLC(String[] LC) {
        this.LC = LC;
    }

    public int getTempColor() {
        return tempColor;
    }

    public void setTempColor(int tempColor) {
        Log.i("Color Begin", "开始调用组件的设置颜色！");
        this.tempColor = tempColor;
        Log.i("Color Set Success", "设置颜色成功！");
    }

    private static String colorToHexString(int color) {
        return String.format("#%06X", 0xFFFFFFFF & color);
    }
}
