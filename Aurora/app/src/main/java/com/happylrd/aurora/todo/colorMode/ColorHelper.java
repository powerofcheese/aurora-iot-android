package com.happylrd.aurora.todo.colorMode;

import android.graphics.Color;

import com.happylrd.aurora.ui.view.ShoeView;

import java.util.List;

public class ColorHelper {

    int delay = 4;
    int sleep = 4;
    static int[] colorArray_int = new int[ShoeView.NUM];
    ShoeView shoe;

    public ColorHelper() {
        super();
        shoe = null;
        for (int i = 0; i < ShoeView.NUM; i++) {
            colorArray_int[i] = Color.CYAN;
        }
    }

    public ColorHelper(ShoeView shoe) {
        super();
        this.shoe = shoe;
    }

    public void setShoe(ShoeView shoe) {
        this.shoe = shoe;
    }

    public static void setColorArray_int(int[]colorForCoustom){
        colorArray_int = colorForCoustom;
    }

    //生成一个随机的颜色值
    public static int randomColor() {
        return Color.argb((int) (Math.random() * 256), (int) (Math.random() * 256),
                (int) (Math.random() * 256), (int) (Math.random() * 256));
    }

    //只选一个颜色
    public static int[] Single(List<Integer> colors) {
        int color = colors.get(colors.size() - 1);
        for (int i = 0; i < ShoeView.NUM; i++) {
            colorArray_int[i] = color;
        }
        return colorArray_int;
    }

    /**
     * 效果：所选颜色的渐变
     * 参数：最多6个颜色,其中每两个颜色(颜色用int表示)之间以空格隔开的字符串
     */
    public static int[] gradation(List<Integer> colors) {
        int interval = (ShoeView.NUM - colors.size()) / colors.size();
        int l = ShoeView.NUM - 1;
        int[][] temp = new int[colors.size()][4];

        for (int i = colors.size() - 1; i > 0; i--) {
            int r = (Color.red(colors.get(i)) - Color.red(colors.get(i - 1))) / interval;
            int g = (Color.green(colors.get(i)) - Color.green(colors.get(i - 1))) / interval;;
            int b = (Color.blue(colors.get(i)) - Color.blue(colors.get(i - 1))) / interval;
            temp[i][1] = Color.red(colors.get(i));
            temp[i][2] = Color.green(colors.get(i));
            temp[i][3] = Color.blue(colors.get(i));

            colorArray_int[l] = colors.get(i);
            for (int k = 0; k < interval; k++) {
                l--;
                temp[i][1] -= r;
                temp[i][2] -= g;
                temp[i][3] -= b;
                colorArray_int[l] = Color.argb(255, temp[i][1], temp[i][2], temp[i][3]);
            }
            l--;
        }

        int r = (Color.red(colors.get(0)) - Color.red(colors.get(colors.size() - 1))) / l;
        int g = (Color.green(colors.get(0)) - Color.green(colors.get(colors.size() - 1))) / l;
        int b = (Color.blue(colors.get(0)) - Color.blue(colors.get(colors.size() - 1))) / l;

        temp[0][1] = Color.red(colors.get(0));
        temp[0][2] = Color.green(colors.get(0));
        temp[0][3] = Color.blue(colors.get(0));

        colorArray_int[l] = Color.argb(255, temp[0][1], temp[0][2], temp[0][3]);

        for (;l > 0;) {
            l--;
            temp[0][1] -= r;
            temp[0][2] -= g;
            temp[0][3] -= b;

            colorArray_int[l] = Color.argb(255, temp[0][1], temp[0][2], temp[0][3]);
        }

        return colorArray_int;
    }
    /**
     * 效果：细条纹
     * 参数：最多6个颜色,其中每两个颜色(颜色用int表示)之间以空格隔开的字符串
     */
    public static int[] thinStripe(List<Integer> tem) {


        int j = 0;
        for (int i = 0; i < ShoeView.NUM; i++) {
            colorArray_int[i] = tem.get(j);
            j++;
            if (j >= tem.size())
                j = 0;
        }
        return colorArray_int;
    }

    /**
     * 效果：粗条纹
     * 参数：最多6个颜色,其中每两个颜色(颜色用int表示)之间以空格隔开的字符串
     */
    public static int[] thickStripe(List<Integer> tem) {

        int l = 0;
        int k = 0;
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 5; j++) {
                colorArray_int[l++] = tem.get(k);
            }
            k++;
            if (k >= tem.size())
                k = 0;
        }

        //为最后的2个灯赋值
        colorArray_int[l++] = tem.get(k);
        colorArray_int[l++] = tem.get(k);
        return colorArray_int;
    }

    /**
     * 效果：前2个颜色各占一半
     * 参数：最多6个颜色,其中每两个颜色(颜色用int表示)之间以空格隔开的字符串
     */
    public static int[] halfStripe(List<Integer> tem) {


        for (int i = 0; i < ShoeView.NUM / 2; i++) {
            colorArray_int[i] = tem.get(0);
        }
        for (int i = ShoeView.NUM / 2; i < ShoeView.NUM; i++) {
            colorArray_int[i] = tem.get(1);
        }
        return colorArray_int;
    }

    /**
     * 效果：有间隔的渐变
     * 参数：最多6个颜色,其中每两个颜色(颜色用int表示)之间以空格隔开的字符串
     */
    public static int[] gradationskip(List<Integer> colors) {

        int interval = (ShoeView.NUM - colors.size()) / colors.size();
        int l = ShoeView.NUM - 1;
        int[][] temp = new int[colors.size()][4];

        for (int i = colors.size() - 1; i > 0; i--) {
            int r = Color.red(colors.get(i)) - Color.red(colors.get(i - 1)) / interval;
            int g = Color.green(colors.get(i)) - Color.green(colors.get(i - 1)) / interval;
            ;
            int b = Color.blue(colors.get(i)) - Color.blue(colors.get(i - 1)) / interval;
            int a = Color.alpha(colors.get(i)) - Color.alpha(colors.get(i - 1)) / interval;
            temp[i][0] = Color.alpha(colors.get(i));
            temp[i][1] = Color.red(colors.get(i));
            temp[i][2] = Color.green(colors.get(i));
            temp[i][3] = Color.blue(colors.get(i));

            colorArray_int[l] = Color.argb(temp[i][0], temp[i][1], temp[i][2], temp[i][3]);
            for (int k = 0; k < interval; k++) {
                l--;
                temp[i][0] -= a;
                temp[i][1] -= r;
                temp[i][2] -= g;
                temp[i][3] -= b;
                colorArray_int[l] = Color.argb(temp[i][0], temp[i][1], temp[i][2], temp[i][3]);
            }
            l--;
        }

        int r = Color.red(colors.get(0)) - Color.red(colors.get(colors.size() - 1)) / l;
        int g = Color.green(colors.get(0)) - Color.green(colors.get(colors.size() - 1)) / l;
        ;
        int b = Color.blue(colors.get(0)) - Color.blue(colors.get(colors.size() - 1)) / l;
        int a = Color.alpha(colors.get(0)) - Color.alpha(colors.get(colors.size() - 1)) / l;
        temp[0][0] = Color.alpha(colors.get(0));
        temp[0][1] = Color.red(colors.get(0));
        temp[0][2] = Color.green(colors.get(0));
        temp[0][3] = Color.blue(colors.get(0));

        colorArray_int[l] = Color.argb(temp[0][0], temp[0][1], temp[0][2], temp[0][3]);
        for (; l > 0;) {
            l--;
            temp[0][0] -= a;
            temp[0][1] -= r;
            temp[0][2] -= g;
            temp[0][3] -= b;
            colorArray_int[l] = Color.argb(temp[0][0], temp[0][1], temp[0][2], temp[0][3]);
        }
        for (int i = 0; i < ShoeView.NUM; i += 2) {
            colorArray_int[i] = Color.BLACK;
        }
        return colorArray_int;
    }

    /**
     * 效果：每一个灯的颜色都是随机的
     */
    public static int[] random() {
        for (int i = 0; i < ShoeView.NUM; i++) {
            colorArray_int[i] = randomColor();
        }
        return colorArray_int;
    }

    /**
     * 效果：每一个灯的颜色都是随机的,中间是有间隔的
     */
    public static int[] randomskip() {
        for (int i = 0; i < ShoeView.NUM; i += 2) {
            colorArray_int[i] = Color.BLACK;
            colorArray_int[i + 1] = randomColor();
        }
        return colorArray_int;
    }

    /**
     * 获得min与max之间的3个任意整数,分别代表RGB
     */
    public static int[] getRGB(int min, int max) {
        int[] temp = new int[3];
        temp[0] = min + (int) (Math.round(Math.random() * 1000) % (max - min));
        if (temp[0] > 255)
            temp[0] = 255;
        temp[1] = min + (int) (Math.round(Math.random() * 1000) % (max - min));
        if (temp[1] > 255)
            temp[1] = 255;
        temp[2] = min + (int) (Math.round(Math.random() * 1000) % (max - min));
        if (temp[2] > 255)
            temp[2] = 255;
        return temp;
    }


    /**
     * 效果：相似的色系
     * 参数： 一个整形的颜色
     */
    public static int[] similar(List<Integer> colors) {

        int color = colors.get(0);
        int color_value = Color.red(color) + Color.blue(color) + Color.green(color);
        int min = color_value - 200;
        int max = color_value + 200;
        int l = 0;
        int[] data = new int[3];

        for (int i = 0; i < 10; i++) {
            while (true) {
                data = getRGB(min / 3, max / 3);
                if ((data[0] + data[1] + data[2]) >= min && (data[0] + data[1] + data[2]) <= max) {
                    break;
                }
            }
            for (int j = 0; j < 3; j++) {
                colorArray_int[l] = Color.argb(255, data[0], data[1], data[2]);
                l++;
            }
        }

        while (true) {
            data = getRGB(min / 3, max / 3);
            if ((data[0] + data[1] + data[2]) >= min && (data[0] + data[1] + data[2]) <= max) {
                break;
            }
            for (int i = 0; i < 2; i++) {
                colorArray_int[l] = Color.argb(255, data[0], data[1], data[2]);
                l++;
            }
        }
        return colorArray_int;
    }

    //交换左右两边的颜色
    public static void Switch() {

        int[] tem = new int[ShoeView.NUM / 2];
        for (int i = 0; i < ShoeView.NUM / 2; i++) {
            tem[i] = colorArray_int[i + ShoeView.NUM / 2];
            colorArray_int[i + ShoeView.NUM / 2] = colorArray_int[i];
            colorArray_int[i] = tem[i];
        }
    }


    /**
     * 以下是颜色的旋转
     */

    //右旋(顺时针)
    public static void Right(int[] color_arr) {
        int temp = color_arr[0];
        for (int i = 1; i < ShoeView.NUM; i++) {
            color_arr[i - 1] = color_arr[i];
        }
        color_arr[ShoeView.NUM - 1] = temp;
    }


    //左旋（逆时针）
    public static void Left(int[] color_arr) {
        int temp = color_arr[ShoeView.NUM - 1];
        for (int i = ShoeView.NUM - 1; i > 0; i--) {
            color_arr[i] = color_arr[i - 1];
        }
        color_arr[0] = temp;
    }

    /**
     * 明暗的变化
     */

    //明暗的渐变
    public void breath() {
        int choice = 0;
        for (int i = 0; i < ShoeView.NUM; i++) {
            int a = Color.alpha(colorArray_int[i]);
            switch (choice) {
                case 0:
                   a += 8.5;
                    if (a >= 255) {
                        a = 255;
                        choice = 1;
                    }
                    break;
                case 1:
                    a -= 8.5;
                    if (a <= 0) {
                        a = 0;
                        choice = 0;
                    }
                    break;
            }
            int tempRGB = colorArray_int[i];
            colorArray_int[i] = Color.argb(a, Color.red(tempRGB), Color.green(tempRGB), Color.blue(tempRGB));
        }
    }

    //明暗的突变
    public void shining() {
        int choice = 0;

        for (int i = 0; i < ShoeView.NUM; i++) {
            int a = Color.alpha(colorArray_int[i]);
            switch (choice) {
                case 0:
                    a = 255;
                    choice = 1;
                    break;
                case 1:
                    a = 0;
                    choice = 0;
                    break;
            }
            int tempRGB = colorArray_int[i];
            colorArray_int[i] = Color.argb(a, Color.red(tempRGB), Color.green(tempRGB), Color.blue(tempRGB));
        }
    }

    //
    public void StrobeINOut() {
        if (delay != 0) {
            shining();
            delay--;
            sleep = 4;
        } else {
            if (sleep != 0) {

                for (int i = 0; i < ShoeView.NUM; i++) {
                    int a = Color.alpha(colorArray_int[i]);
                    a = 0;
                    int tempRGB = colorArray_int[i];
                    colorArray_int[i] = Color.argb(a, Color.red(tempRGB), Color.green(tempRGB), Color.blue(tempRGB));
                }
                sleep--;
            } else
                delay = 4;
        }
    }

    /**
     * HSV相关
     */


    int or = 0;

    void Swing(int[] color_arr) {
        if (or < 64) {
            Right(color_arr);
            or++;
        } else if (or < 128) {
            Left(color_arr);
            or++;
        } else {
            or = 0;
        }
    }

    /**
     * 改变色阶
     */
    public void hue() {
        float[] hsv3 = new float[3];
        for (int i = 0; i < ShoeView.NUM; i++) {
            Color.colorToHSV(colorArray_int[i], hsv3);
            hsv3[0] = hsv3[0] + 1 > 359 ? 0 : hsv3[0] + 1;
            colorArray_int[i] = Color.HSVToColor(hsv3);
        }
    }

    public void updateShoe() {
        shoe.updateColor(colorArray_int);
    }
}
