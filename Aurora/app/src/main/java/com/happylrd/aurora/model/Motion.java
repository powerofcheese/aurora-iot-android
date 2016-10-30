package com.happylrd.aurora.model;

import java.util.List;

import cn.bmob.v3.BmobObject;

public class Motion extends BmobObject {

    private String patternName;
    private String animationName;
    private String rotationName;
    private String actionName;

    private List<Integer> intColorList;

    private GestureState gestureState;  // gestureState and motion is one to one

    public String getPatternName() {
        return patternName;
    }

    public void setPatternName(String patternName) {
        this.patternName = patternName;
    }

    public String getAnimationName() {
        return animationName;
    }

    public void setAnimationName(String animationName) {
        this.animationName = animationName;
    }

    public String getRotationName() {
        return rotationName;
    }

    public void setRotationName(String rotationName) {
        this.rotationName = rotationName;
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public GestureState getGestureState() {
        return gestureState;
    }

    public void setGestureState(GestureState gestureState) {
        this.gestureState = gestureState;
    }

    public List<Integer> getIntColorList() {
        return intColorList;
    }

    public void setIntColorList(List<Integer> intColorList) {
        this.intColorList = intColorList;
    }
}
