package com.happylrd.aurora.entity;

import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by lenovo on 2016/8/26.
 */
public class MyUser extends BmobUser {

    private String signature;
    private String sex;
    private BmobFile my_picture;
    private List<MyUser> friend;

    public BmobFile getMy_picture() {
        return my_picture;
    }

    public void setMy_picture(BmobFile my_picture) {
        this.my_picture = my_picture;
    }

    public List<MyUser> getFriend() {
        return friend;
    }

    public void setFriend(List<MyUser> friend) {
        this.friend = friend;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
