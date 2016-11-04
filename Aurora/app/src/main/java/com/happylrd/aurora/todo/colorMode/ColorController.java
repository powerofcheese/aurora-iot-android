package com.happylrd.aurora.todo.colorMode;

import android.graphics.Color;
import android.os.Handler;
import android.os.Message;

import com.happylrd.aurora.constant.Constants;
import com.happylrd.aurora.ui.view.ShoeView;

import java.util.Timer;
import java.util.TimerTask;

public class ColorController {

    private ColorFolder data;
    private ColorHelper colorHelper;
    private Timer timer_Rotation;
    private Timer timer_Animation;
    private Handler handler_parent2child;
    private Handler handler_child2parent = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            colorHelper.shoe.updateColor(colorHelper.colorArray_int);
        }
    };
    private int[] initColor = new int[ShoeView.NUM];

    public void setShoeForColorHelper(ShoeView shoeView) {
        colorHelper.setShoe(shoeView);
    }

    public ColorController() {
        data = new ColorFolder();
        timer_Animation = null;
        timer_Rotation = null;
        colorHelper = new ColorHelper();
        for (int i = 0; i < ShoeView.NUM; i++) {
            initColor[i] = Color.CYAN;
        }
    }

    public ColorController(ShoeView shoe) {
        data = new ColorFolder();
        colorHelper = new ColorHelper(shoe);
        timer_Animation = null;
        timer_Rotation = null;
        for (int i = 0; i < ShoeView.NUM; i++) {
            initColor[i] = Color.CYAN;
        }
    }

    /**
     * 控制流程相关
     */
    public void Control(ColorFolder data_j) {
        stopTimer();
        switch (data_j.getPattern()) {
            case "Single":
                colorHelper.Single(data_j.getColorList());
                break;
            case "Gradation":
                colorHelper.gradation(data_j.getColorList());
                break;
            case "ThinStripe":
                colorHelper.thinStripe(data_j.getColorList());
                break;
            case "ThickStripe":
                colorHelper.thickStripe(data_j.getColorList());
                break;
            case "HalfStripe":
                colorHelper.halfStripe(data_j.getColorList());
                break;
            case "SkipStripe":
                colorHelper.gradationskip(data_j.getColorList());
                break;
            case "GradationSkipping":
                colorHelper.gradationskip(data_j.getColorList());
                break;
            case "Random":
                colorHelper.random();
                break;
            case "RandomSkipping":
                colorHelper.randomskip();
                break;
            case "Similar":
                colorHelper.similar(data_j.getColorList());
                break;
            default:
                colorHelper.Single(data_j.getColorList());
                break;
        }
        handler_child2parent.sendEmptyMessage(Constants.MESSAGE_UPDATE_SHOE);

        if (data_j.getAnimation() == "Nothing") {
            handleRotation(data_j);
        } else {
            handleRotation(data_j);
            handle_Animation(data_j);
        }
    }

    public static void startTimer(Timer timer, TimerTask task, long time) {
        if (timer == null)
            timer = new Timer();
        timer.schedule(task, 0, time);
    }

    public void stopTimer() {
        if (timer_Rotation != null) {
            timer_Rotation.cancel();
            timer_Rotation = null;
        }
        if (timer_Animation != null) {
            timer_Animation.cancel();
            timer_Animation = null;
        }
    }

    public void recover2init() {
        stopTimer();
        colorHelper.shoe.updateColor(initColor);
    }


    public void handleRotation(ColorFolder data_j) {
        switch (data_j.getRotation()) {
            case "FloatRight":
                startTimer(timer_Rotation, new TimerTask() {
                    @Override
                    public void run() {
                        colorHelper.Right(colorHelper.colorArray_int);
                        handler_child2parent.sendEmptyMessage(Constants.MESSAGE_UPDATE_SHOE);
                    }
                }, 100);
                break;
            case "FloatLeft":
                startTimer(timer_Rotation, new TimerTask() {
                    @Override
                    public void run() {
                        colorHelper.Left(colorHelper.colorArray_int);
                        handler_child2parent.sendEmptyMessage(Constants.MESSAGE_UPDATE_SHOE);
                    }
                }, 100);
                break;
            case "RotateRight":
                startTimer(timer_Rotation, new TimerTask() {
                    @Override
                    public void run() {
                        colorHelper.Right(colorHelper.colorArray_int);
                        handler_child2parent.sendEmptyMessage(Constants.MESSAGE_UPDATE_SHOE);
                    }
                }, 50);
                break;
            case "RotateLeft":
                startTimer(timer_Rotation, new TimerTask() {
                    @Override
                    public void run() {
                        colorHelper.Left(colorHelper.colorArray_int);
                        handler_child2parent.sendEmptyMessage(Constants.MESSAGE_UPDATE_SHOE);
                    }
                }, 50);
                break;
            case "SpinRight":
                startTimer(timer_Rotation, new TimerTask() {
                    @Override
                    public void run() {
                        colorHelper.Right(colorHelper.colorArray_int);
                        handler_child2parent.sendEmptyMessage(Constants.MESSAGE_UPDATE_SHOE);
                    }
                }, 20);
                break;
            case "SpinLeft":
                startTimer(timer_Rotation, new TimerTask() {
                    @Override
                    public void run() {
                        colorHelper.Left(colorHelper.colorArray_int);
                        handler_child2parent.sendEmptyMessage(Constants.MESSAGE_UPDATE_SHOE);
                    }
                }, 20);
                break;
            case "Swing":
                startTimer(timer_Rotation, new TimerTask() {
                    @Override
                    public void run() {
                        colorHelper.Swing(colorHelper.colorArray_int);
                        handler_child2parent.sendEmptyMessage(Constants.MESSAGE_UPDATE_SHOE);
                    }
                }, 50);
                break;
            case "Switch":
                startTimer(timer_Rotation, new TimerTask() {
                    @Override
                    public void run() {
                        colorHelper.Switch();
                        handler_child2parent.sendEmptyMessage(Constants.MESSAGE_UPDATE_SHOE);
                    }
                }, 100);
                break;
            case "Snake":
                startTimer(timer_Rotation, new TimerTask() {
                    @Override
                    public void run() {
                        colorHelper.Swing(colorHelper.colorArray_int);
                        handler_child2parent.sendEmptyMessage(Constants.MESSAGE_UPDATE_SHOE);
                    }
                }, 20);
                break;
            case "MoveToRotation":
                break;
            default:
                handler_child2parent.sendEmptyMessage(Constants.MESSAGE_UPDATE_SHOE);
                break;
        }
    }

    public void handle_Animation(ColorFolder data_j) {
        switch (data_j.getAnimation()) {
            case "Ramp":
                startTimer(timer_Animation, new TimerTask() {
                    @Override
                    public void run() {
                        colorHelper.breath();
                        handler_child2parent.sendEmptyMessage(Constants.MESSAGE_UPDATE_SHOE);
                    }
                }, 100);
                break;
            case "Wave":
                startTimer(timer_Animation, new TimerTask() {
                    @Override
                    public void run() {
                        colorHelper.breath();
                        handler_child2parent.sendEmptyMessage(Constants.MESSAGE_UPDATE_SHOE);
                    }
                }, 80);
                break;
            case "Flash":
                startTimer(timer_Animation, new TimerTask() {
                    @Override
                    public void run() {
                        colorHelper.breath();
                        handler_child2parent.sendEmptyMessage(Constants.MESSAGE_UPDATE_SHOE);
                    }
                }, 50);
                break;
            case "Strobe":
                startTimer(timer_Animation, new TimerTask() {
                    @Override
                    public void run() {
                        colorHelper.shining();
                        handler_child2parent.sendEmptyMessage(Constants.MESSAGE_UPDATE_SHOE);
                    }
                }, 50);
                break;
            case "StrobeInOut":
                startTimer(timer_Animation, new TimerTask() {
                    @Override
                    public void run() {
                        colorHelper.StrobeINOut();
                        handler_child2parent.sendEmptyMessage(Constants.MESSAGE_UPDATE_SHOE);
                    }
                }, 20);
                break;
            case "SlowHue":
                startTimer(timer_Animation, new TimerTask() {
                    @Override
                    public void run() {
                        colorHelper.hue();
                        handler_child2parent.sendEmptyMessage(Constants.MESSAGE_UPDATE_SHOE);
                    }
                }, 100);
                break;
            case "HueCycle":
                startTimer(timer_Animation, new TimerTask() {
                    @Override
                    public void run() {
                        colorHelper.hue();
                        handler_child2parent.sendEmptyMessage(Constants.MESSAGE_UPDATE_SHOE);
                    }
                }, 50);
                break;
            case "HueStrobe":
                startTimer(timer_Animation, new TimerTask() {
                    @Override
                    public void run() {
                        colorHelper.hue();
                        handler_child2parent.sendEmptyMessage(Constants.MESSAGE_UPDATE_SHOE);
                    }
                }, 20);
                break;
            default:
                break;
        }
    }
}
