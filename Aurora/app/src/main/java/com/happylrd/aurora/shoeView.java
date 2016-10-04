package com.happylrd.aurora;

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

/**
 * Created by lenovo on 2016/8/16.
 */
public class shoeView extends View {

    private LayerDrawable mDrawables;

    public final static int num = 32;

    private String[] LC = new String[num];
    private int temp_color = Color.WHITE;
    private int temp = 0;
    private String send ;

    public shoeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mDrawables = (LayerDrawable) getBackground();
        for(int i = 0;i < num;i++){
            LC[i] = colorToHexString(Color.BLACK);
        }
    }


    @Override
  public boolean onTouchEvent(MotionEvent event) {

        Drawable drawable = null;
        Bitmap bitmap = null;
        Bitmap rbitmap = null;
        int hit = 0;
       // if(event.getAction() == MotionEvent.ACTION_MOVE){
            final float x = event.getX();
            final float y = event.getY();
            int i = temp>= num-1? num-1:temp +1;
            if(i < 2)
                i = 2;
            for (int t = 3; t > 0;i--,t--)
            {
                drawable = mDrawables.getDrawable(i);
                rbitmap = Bitmap.createScaledBitmap(((BitmapDrawable) drawable).getBitmap(),this.getWidth(),this.getHeight(),true);
                try
                {
                    int pixel = rbitmap.getPixel((int) x, (int) y);
                    if (pixel == Color.TRANSPARENT)
                    {
                        continue;
                    }
                    else{
                        hit = 1;
                        break;
                    }
                }
                catch (Exception e) {
                    continue;
                }
            }
            if (hit == 1) {
                if(temp == i)
                    return true;
                drawable.setColorFilter(temp_color, PorterDuff.Mode.SRC_IN);
                String HexColor = colorToHexString(temp_color);
                LC[i] = HexColor;
                rbitmap = null;
                send ="custom "+ HexColor + " " + i;
                MainActivity.service.write(send.getBytes());
                temp = i;
                return true;
            }
            i = num - 1;
            for (; i >= 0;i--)
            {
                drawable = mDrawables.getDrawable(i);
               rbitmap = Bitmap.createScaledBitmap(((BitmapDrawable) drawable).getBitmap(), this.getWidth(), this.getHeight(),true);

                try
                {
                    int pixel = rbitmap.getPixel((int) x, (int) y);
                    if (pixel == Color.TRANSPARENT)
                    {
                        continue;
                    }
                    else{
                        break;
                    }
                }
                catch (Exception e) {
                    continue;
                }
            }
            if (i >= 0) {
                temp = i;
                drawable.setColorFilter(temp_color, PorterDuff.Mode.SRC_IN);
                String HexColor = colorToHexString(temp_color);
                LC[i] = HexColor;
                send ="custom "+ HexColor + " " + i;
               MainActivity.service.write(send.getBytes());
            }
       // }
        rbitmap = null;
        return true;
    }


    public String[] getLC() {
        return LC;
    }

    public void setLC(String[] LC) {
        this.LC = LC;
    }

    public int getTemp_color() {
        return temp_color;
    }

    public void setTemp_color(int temp_color) {
        Log.i("aaa", "开始调用组件的设置颜色！");
        this.temp_color = temp_color;
        Log.i("aaa","设置颜色成功！");
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }
    private static String colorToHexString(int color) {
        return String.format("#%06X", 0xFFFFFFFF & color);
    }
}
