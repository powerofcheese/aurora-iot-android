package com.happylrd.aurora.todo.colorMode;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;

public class ColorFolder {
    private List<Integer> colorarray;
    private String pattern;
    private String animation;
    private String rotation;
    private String reaction;

    public ColorFolder() {
        colorarray = new ArrayList<>();
        colorarray.add(Color.BLACK);
        rotation = "Nothing";
        animation = "Nothing";
    }

    public List<Integer> getColorarray() {
        return colorarray;
    }

    public String getAnimation() {
        return animation;
    }

    public String getPattern() {
        return pattern;
    }

    public String getReaction() {
        return reaction;
    }

    public String getRotation() {
        return rotation;
    }

    public void setAnimation(String animation) {
        this.animation = animation;
    }

    public void setColorarray(List<Integer> colorarray) {
        this.colorarray = colorarray;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public void setReaction(String reaction) {
        this.reaction = reaction;
    }

    public void setRotation(String rotation) {
        this.rotation = rotation;
    }
}
