package com.happylrd.aurora.util;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {

    public static void showSignUpSuccessToast(Context context) {
        Toast.makeText(context, "注册成功", Toast.LENGTH_SHORT)
                .show();
    }

    public static void showSignUpFailToast(Context context) {
        Toast.makeText(context, "注册失败", Toast.LENGTH_SHORT)
                .show();
    }

    public static void showLoginSuccessToast(Context context) {
        Toast.makeText(context, "登录成功", Toast.LENGTH_SHORT)
                .show();
    }

    public static void showLoginFailToast(Context context) {
        Toast.makeText(context, "登录失败", Toast.LENGTH_SHORT)
                .show();
    }

    public static void showInputNotNullToast(Context context) {
        Toast.makeText(context, "输入不能为空", Toast.LENGTH_SHORT)
                .show();
    }

    public static void showUpdateFailToast(Context context) {
        Toast.makeText(context, "更新失败", Toast.LENGTH_SHORT)
                .show();
    }

    public static void showFindFailedToast(Context context) {
        Toast.makeText(context, "查询失败", Toast.LENGTH_SHORT)
                .show();
    }

    public static void showSaveSuccessToast(Context context) {
        Toast.makeText(context, "保存成功", Toast.LENGTH_SHORT)
                .show();
    }

    public static void showSaveFailToast(Context context) {
        Toast.makeText(context, "保存失败", Toast.LENGTH_SHORT)
                .show();
    }
}
