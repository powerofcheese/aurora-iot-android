package com.happylrd.aurora.entity;

import cn.bmob.v3.BmobObject;

public class WriteSthContent extends BmobObject {

    private String textContent;
    private String picPath;

    public String getTextContent() {
        return textContent;
    }

    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }
}
