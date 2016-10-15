package com.happylrd.aurora.model;

import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobRelation;

public class WriteSth extends BmobObject {

    private String textContent;
    private List<String> picsPathList;

    private MyUser author;  // author and writesth are one to many
    private BmobRelation praise;  // user and writesth are many to many

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

    public BmobRelation getPraise() {
        return praise;
    }

    public void setPraise(BmobRelation praise) {
        this.praise = praise;
    }
}
