package com.happylrd.aurora;

/**
 * Created by lenovo on 2016/9/2.
 */
public class FolderBean  {

    //当前文件夹的路径
    private String dir;
    //当前文件夹第一张图片的路径
    private String firstImgPath;
    private String name;
    private int count;

    public int getCount() {
        return count;
    }

    public String getDir() {
        return dir;
    }

    public String getFirstImgPath() {
        return firstImgPath;
    }

    public String getName() {
        return name;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setDir(String dir) {
        this.dir = dir;

        int lastIndexOf = this.dir.lastIndexOf("/");
        this.name = this.dir.substring(lastIndexOf + 1);
    }

    public void setFirstImgPath(String firstImgPath) {
        this.firstImgPath = firstImgPath;
    }

}
