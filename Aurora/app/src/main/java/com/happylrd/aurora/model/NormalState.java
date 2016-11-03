package com.happylrd.aurora.model;

import cn.bmob.v3.BmobObject;

public class NormalState extends BmobObject {

    private Mode mode;  // mode and gestureState is one to many

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }
}
