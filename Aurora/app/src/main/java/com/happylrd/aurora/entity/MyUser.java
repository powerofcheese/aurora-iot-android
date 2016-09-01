package com.happylrd.aurora.entity;

import cn.bmob.v3.BmobUser;

public class MyUser extends BmobUser {

    private String nickName;
    private Boolean sex;
    private Integer age;

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Boolean getSex() {
        return sex;
    }

    public void setSex(Boolean sex) {
        this.sex = sex;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
