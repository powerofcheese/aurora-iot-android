package com.happylrd.aurora.todo.colorMode;

import android.graphics.Color;
import android.os.Handler;
import android.os.Message;

import com.happylrd.aurora.constant.Constants;
import com.happylrd.aurora.ui.view.ShoeView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ColorController {

    private ColorFolder data;
    private ColorHelper colorHelper;
    private TimerTask task_Rotation;
    private TimerTask task_Animation;
    private Timer timer_Rotation;
    private Timer timer_Animation;
    private int[] initColor = new int[ShoeView.NUM];
    private Handler handler_parent2child;
    private Handler handler_child2parent = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.MESSAGE_UPDATE_SHOE:
                    colorHelper.shoe.updateColor(colorHelper.colorArray_int);
                    break;
                case Constants.MESSAGE_INIT_SHOE:
                    colorHelper.shoe.updateColor(initColor);
                break;
                default:
                    break;
            }
        }
    };


    public void setShoeForColorHelper(ShoeView shoeView) {
        colorHelper.setShoe(shoeView);
    }

    public ColorController() {
        data = new ColorFolder();
        timer_Animation = null;
        timer_Rotation = null;
        colorHelper = new ColorHelper();
        for (int i = 0; i < ShoeView.NUM; i++) {
            initColor[i] = Color.GRAY;
        }
    }

    public ColorController(ShoeView shoe) {
        data = new ColorFolder();
        colorHelper = new ColorHelper(shoe);
        timer_Animation = null;
        timer_Rotation = null;
        for (int i = 0; i < ShoeView.NUM; i++) {
            initColor[i] = Color.GRAY;
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
            case "Custom":
                colorHelper.setColorArray_int(data_j.getColorList());
                break;
            default:
                List<Integer> init = new ArrayList<>();
                init.add(Color.CYAN);
                colorHelper.Single(init);
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
        if(task_Animation != null){
            task_Animation.cancel();
            task_Animation = null;
        }
        if(task_Rotation != null){
            task_Rotation.cancel();
            task_Rotation = null;
        }
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
        handler_child2parent.sendEmptyMessage(Constants.MESSAGE_INIT_SHOE);
    }


    public void handleRotation(ColorFolder data_j) {
        switch (data_j.getRotation()) {
            case "FloatRight":
                task_Rotation = new TimerTask() {
                    @Override
                    public void run() {
                        colorHelper.Right(colorHelper.colorArray_int);
                        handler_child2parent.sendEmptyMessage(Constants.MESSAGE_UPDATE_SHOE);
                    }
                };
                startTimer(timer_Rotation,task_Rotation , 100);
                break;
            case "FloatLeft":
                task_Rotation = new TimerTask() {
                    @Override
                    public void run() {
                        colorHelper.Left(colorHelper.colorArray_int);
                        handler_child2parent.sendEmptyMessage(Constants.MESSAGE_UPDATE_SHOE);
                    }
                };
                startTimer(timer_Rotation, task_Rotation, 100);
                break;
            case "RotateRight":
                task_Rotation = new TimerTask() {
                    @Override
                    public void run() {
                        colorHelper.Right(colorHelper.colorArray_int);
                        handler_child2parent.sendEmptyMessage(Constants.MESSAGE_UPDATE_SHOE);
                    }
                };
                startTimer(timer_Rotation,task_Rotation , 50);
                break;
            case "RotateLeft":
                task_Rotation = new TimerTask() {
                    @Override
                    public void run() {
                        colorHelper.Left(colorHelper.colorArray_int);
                        handler_child2parent.sendEmptyMessage(Constants.MESSAGE_UPDATE_SHOE);
                    }
                };
                startTimer(timer_Rotation, task_Rotation, 50);
                break;
            case "SpinRight":
                task_Rotation = new TimerTask() {
                    @Override
                    public void run() {
                        colorHelper.Right(colorHelper.colorArray_int);
                        handler_child2parent.sendEmptyMessage(Constants.MESSAGE_UPDATE_SHOE);
                    }
                };
                startTimer(timer_Rotation,task_Rotation , 20);
                break;
            case "SpinLeft":
                task_Rotation = new TimerTask() {
                    @Override
                    public void run() {
                        colorHelper.Left(colorHelper.colorArray_int);
                        handler_child2parent.sendEmptyMessage(Constants.MESSAGE_UPDATE_SHOE);
                    }
                };
                startTimer(timer_Rotation,task_Rotation , 20);
                break;
            case "Swing":
                task_Rotation = new TimerTask() {
                    @Override
                    public void run() {
                        colorHelper.Swing(colorHelper.colorArray_int);
                        handler_child2parent.sendEmptyMessage(Constants.MESSAGE_UPDATE_SHOE);
                    }
                };
                startTimer(timer_Rotation,task_Rotation , 50);
                break;
            case "Switch":
                task_Rotation = new TimerTask() {
                    @Override
                    public void run() {
                        colorHelper.Switch();
                        handler_child2parent.sendEmptyMessage(Constants.MESSAGE_UPDATE_SHOE);
                    }
                };
                startTimer(timer_Rotation,task_Rotation , 100);
                break;
            case "Snake":
                task_Rotation =  new TimerTask() {
                    @Override
                    public void run() {
                        colorHelper.Swing(colorHelper.colorArray_int);
                        handler_child2parent.sendEmptyMessage(Constants.MESSAGE_UPDATE_SHOE);
                    }
                };
                startTimer(timer_Rotation,task_Rotation, 20);
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
                task_Animation =  new TimerTask() {
                    @Override
                    public void run() {
                        colorHelper.breath();
                        handler_child2parent.sendEmptyMessage(Constants.MESSAGE_UPDATE_SHOE);
                    }
                };
                startTimer(timer_Animation,task_Animation, 100);
                break;
            case "Wave":
                task_Animation =  new TimerTask() {
                    @Override
                    public void run() {
                        colorHelper.breath();
                        handler_child2parent.sendEmptyMessage(Constants.MESSAGE_UPDATE_SHOE);
                    }
                };
                startTimer(timer_Animation,task_Animation, 80);
                break;
            case "Flash":
                task_Animation = new TimerTask() {
                    @Override
                    public void run() {
                        colorHelper.breath();
                        handler_child2parent.sendEmptyMessage(Constants.MESSAGE_UPDATE_SHOE);
                    }
                };
                startTimer(timer_Animation,task_Animation , 50);
                break;
            case "Strobe":
                task_Animation = new TimerTask() {
                    @Override
                    public void run() {
                        colorHelper.shining();
                        handler_child2parent.sendEmptyMessage(Constants.MESSAGE_UPDATE_SHOE);
                    }
                };
                startTimer(timer_Animation,task_Animation , 50);
                break;
            case "StrobeInOut":
                task_Animation = new TimerTask() {
                    @Override
                    public void run() {
                        colorHelper.StrobeINOut();
                        handler_child2parent.sendEmptyMessage(Constants.MESSAGE_UPDATE_SHOE);
                    }
                };
                startTimer(timer_Animation,task_Animation , 20);
                break;
            case "SlowHue":
                task_Animation =  new TimerTask() {
                    @Override
                    public void run() {
                        colorHelper.hue();
                        handler_child2parent.sendEmptyMessage(Constants.MESSAGE_UPDATE_SHOE);
                    }
                };
                startTimer(timer_Animation,task_Animation, 100);
                break;
            case "HueCycle":
                task_Animation = new TimerTask() {
                    @Override
                    public void run() {
                        colorHelper.hue();
                        handler_child2parent.sendEmptyMessage(Constants.MESSAGE_UPDATE_SHOE);
                    }
                };
                startTimer(timer_Animation,task_Animation , 50);
                break;
            case "HueStrobe":
                task_Animation = new TimerTask() {
                    @Override
                    public void run() {
                        colorHelper.hue();
                        handler_child2parent.sendEmptyMessage(Constants.MESSAGE_UPDATE_SHOE);
                    }
                };
                startTimer(timer_Animation,task_Animation , 20);
                break;
            default:
                break;
        }
    }
}