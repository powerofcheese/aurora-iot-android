package com.happylrd.aurora.ui.dialog;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.happylrd.aurora.R;
import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.OpacityBar;

public class ColorMotionDialog extends DialogFragment {

    private static final String TAG = "ColorMotionDialog";

    private ColorPicker colorPicker;
    private OpacityBar opacityBar;

    private Button btn_determine_color;

    public static ColorMotionDialog newInstance() {
        ColorMotionDialog colorMotionDialog = new ColorMotionDialog();
        return colorMotionDialog;
    }

    public ColorMotionDialog() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_color_motion, container);

        colorPicker = (ColorPicker) view.findViewById(R.id.color_picker);
        opacityBar = (OpacityBar) view.findViewById(R.id.opacity_bar);

        colorPicker.addOpacityBar(opacityBar);

        btn_determine_color = (Button) view.findViewById(R.id.btn_determine_color);
        btn_determine_color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int intColor = colorPicker.getColor();
                colorPicker.setOldCenterColor(intColor);

                String rgbHexStr = String.format("#%06X", (0xFFFFFF & intColor));
                Log.d(TAG, rgbHexStr);
                String argbHexStr = String.format("#%08X", intColor);
                Log.d(TAG, argbHexStr);

                Log.d("Targer is null?", (getTargetFragment() == null) + "");
                Log.d("Target:", getTargetFragment() + "");


                // Not elegant here,but I haven't found a better solution yet.
                ((PatternTabDialog) getTargetFragment()).getColorAdapter().add(intColor);

                dismiss();
            }
        });

        colorPicker.setOnColorChangedListener(new ColorPicker.OnColorChangedListener() {
            @Override
            public void onColorChanged(int color) {

            }
        });

        opacityBar.setOnOpacityChangedListener(new OpacityBar.OnOpacityChangedListener() {
            @Override
            public void onOpacityChanged(int opacity) {

            }
        });

        return view;
    }
}
