package com.happylrd.aurora.entity;

import java.util.List;

import cn.bmob.v3.BmobObject;

public class WriteSthContent extends BmobObject {

    private String textContent;

    private String picturePath;

    private List<String> picsPathList;
    private MyUser author;

    public String getTextContent() {
        return textContent;
    }

    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }

    public List<String> getPicsPathList() {
        return picsPathList;
    }

    public void setPicsPathList(List<String> picsPathList) {
        this.picsPathList = picsPathList;
    }

    public MyUser getAuthor() {
        return author;
    }

    public void setAuthor(MyUser author) {
        this.author = author;
    }

    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }
}
