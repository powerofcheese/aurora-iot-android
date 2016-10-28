package com.happylrd.aurora.model;

import cn.bmob.v3.BmobObject;

public class Mode extends BmobObject {

    private String modeName;
    private MyUser author;  // author and mode are one to many

    public String getModeName() {
        return modeName;
    }

    public void setModeName(String modeName) {
        this.modeName = modeName;
    }

    public MyUser getAuthor() {
        return author;
    }

    public void setAuthor(MyUser author) {
        this.author = author;
    }
}
