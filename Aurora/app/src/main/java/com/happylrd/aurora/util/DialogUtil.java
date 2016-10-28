package com.happylrd.aurora.util;

import android.support.v4.app.FragmentManager;

import com.happylrd.aurora.ui.dialog.ColorPickerDialog;

public class DialogUtil {

    public static void showColorPickerDialog(FragmentManager manager) {
        ColorPickerDialog colorPickerDialog = ColorPickerDialog.newInstance();
        colorPickerDialog.show(manager, "colorPickerDialog");
    }
}
