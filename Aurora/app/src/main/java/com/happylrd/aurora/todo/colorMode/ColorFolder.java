package com.happylrd.aurora.todo.colorMode;

import java.util.List;

public class ColorFolder {
    private String pattern;
    private String animation;
    private String rotation;
    private String reaction;

    private List<Integer> colorList;

    public ColorFolder() {
        rotation = "Nothing";
        animation = "Nothing";
    }

    public List<Integer> getColorList() {
        return colorList;
    }

    public void setColorList(List<Integer> colorList) {
        this.colorList = colorList;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getAnimation() {
        return animation;
    }

    public void setAnimation(String animation) {
        this.animation = animation;
    }

    public String getRotation() {
        return rotation;
    }

    public void setRotation(String rotation) {
        this.rotation = rotation;
    }

    public String getReaction() {
        return reaction;
    }

    public void setReaction(String reaction) {
        this.reaction = reaction;
    }
}
