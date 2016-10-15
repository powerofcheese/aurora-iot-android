package com.happylrd.aurora.todo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import com.happylrd.aurora.todo.AudioCapture;

import java.util.Arrays;

public class MusicEnergy extends SurfaceView implements SurfaceHolder.Callback {

    private final Handler mHandler = new Handler();
    AudioCapture mAudioCapture = null;
    private int[] mVizData = new int[1024];
    private final Paint mPaint = new Paint();

    private static final int frequency = 1000 / 25;

    int mWidth = 0;
    int mCenterY = 0;

    private final Runnable mDrawCube = new Runnable() {
        public void run() {
            drawFrame();
        }
    };

    void drawFrame() {
        final SurfaceHolder holder = getHolder();
        final Rect frame = holder.getSurfaceFrame();
        final int width = frame.width();
        final int height = frame.height();

        Canvas c = null;
        try {
            c = holder.lockCanvas();
            if (c != null) {
                // draw something
                drawCube(c);
            }
        } finally {
            if (c != null) holder.unlockCanvasAndPost(c);
        }

        mHandler.removeCallbacks(mDrawCube);
        mHandler.postDelayed(mDrawCube, frequency);
    }

    public MusicEnergy(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        mAudioCapture = new AudioCapture(AudioCapture.TYPE_PCM, 1024);
        mAudioCapture.start();
        mPaint.setColor(0xffffffff);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(2);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStyle(Paint.Style.STROKE);
        getHolder().addCallback(this);
    }

    void drawCube(Canvas c) {
        c.save();
        c.drawColor(0xff000000);

        if (mAudioCapture != null) {
            mVizData = mAudioCapture.getFormattedData(1, 1);
        } else {
            Arrays.fill(mVizData, (int) 0);
        }

        for (int i = 0; i < mVizData.length; i++) {
            c.drawPoint(i, mCenterY + mVizData[i], mPaint);
            c.drawLine(i, mCenterY, i, mCenterY + mVizData[i], mPaint);
//            if(i == 0 && MainActivity.service != null){
//                String send = "" + mVizData[0];
//                MainActivity.service.write(send.getBytes());
//            }
        }
        c.restore();
    }

    @Override
    protected void finalize() throws Throwable {
        // TODO Auto-generated method stub
        super.finalize();
        if (mAudioCapture != null) {
            mAudioCapture.stop();
            mAudioCapture.release();
            mAudioCapture = null;
        }
    }

    public void onClose() {
        if (mAudioCapture != null) {
            mAudioCapture.stop();
            mAudioCapture.release();
            mAudioCapture = null;
        }
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        // TODO Auto-generated method stub
        mCenterY = height / 2;
        mWidth = width;
    }

    public void surfaceCreated(SurfaceHolder holder) {
        // TODO Auto-generated method stub
        drawFrame();
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // TODO Auto-generated method stub

    }

    private int getScreenWidth() {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;
    }
}
