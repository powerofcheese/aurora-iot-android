package com.happylrd.aurora.model;

import cn.bmob.v3.BmobObject;

public class Comment extends BmobObject {

    private String textContent;
    private MyUser user;
    private WriteSth writeSth;

    public String getTextContent() {
        return textContent;
    }

    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }

    public MyUser getUser() {
        return user;
    }

    public void setUser(MyUser user) {
        this.user = user;
    }

    public WriteSth getWriteSth() {
        return writeSth;
    }

    public void setWriteSth(WriteSth writeSth) {
        this.writeSth = writeSth;
    }
}
