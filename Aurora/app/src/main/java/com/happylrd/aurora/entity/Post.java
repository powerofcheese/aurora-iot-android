package com.happylrd.aurora.entity;

import java.lang.reflect.Array;
import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * Created by lenovo on 2016/8/26.
 */
public class Post extends BmobObject {

    private Array shoe_color;
    private List<String> post_pics;
    private BmobRelation likes;
    private String content;
    private MyUser author;

    public List<String> getPost_pics() {
        return post_pics;
    }

    public void setPost_pics(List<String> post_pics) {
        this.post_pics = post_pics;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Array getShoe_color() {
        return shoe_color;
    }

    public void setShoe_color(Array shoe_color) {
        this.shoe_color = shoe_color;
    }

    public MyUser getAuthor() {
        return author;
    }

    public void setAuthor(MyUser author) {
        this.author = author;
    }

    public BmobRelation getLikes() {
        return likes;
    }

    public void setLikes(BmobRelation likes) {
        this.likes = likes;
    }
}
