package com.happylrd.aurora.ui.dialog;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.happylrd.aurora.R;
import com.happylrd.aurora.ui.activity.ShoesActivity;
import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.OpacityBar;
import com.larswerkman.holocolorpicker.SaturationBar;
import com.larswerkman.holocolorpicker.ValueBar;

public class ColorPickerDialog extends DialogFragment {

    private ColorPicker colorPicker;
    private OpacityBar opacityBar;
    private SaturationBar saturationBar;
    private ValueBar valueBar;

    private Button btn_determine_color;

    public static ColorPickerDialog newInstance() {
        ColorPickerDialog colorPickerDialog = new ColorPickerDialog();
        return colorPickerDialog;
    }

    public ColorPickerDialog() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_color_picker, container);

        colorPicker = (ColorPicker) view.findViewById(R.id.color_picker);
        opacityBar = (OpacityBar) view.findViewById(R.id.opacity_bar);
        saturationBar = (SaturationBar) view.findViewById(R.id.saturation_bar);
        valueBar = (ValueBar) view.findViewById(R.id.value_bar);

        colorPicker.addOpacityBar(opacityBar);
        colorPicker.addSaturationBar(saturationBar);
        colorPicker.addValueBar(valueBar);

        btn_determine_color = (Button) view.findViewById(R.id.btn_determine_color);
        btn_determine_color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorPicker.setOldCenterColor(colorPicker.getColor());

                // Not elegant here,but I haven't found a better solution yet.
                ((ShoesActivity) getActivity()).setShoesColor(colorPicker.getColor());

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

        saturationBar.setOnSaturationChangedListener(new SaturationBar.OnSaturationChangedListener() {
            @Override
            public void onSaturationChanged(int saturation) {

            }
        });

        valueBar.setOnValueChangedListener(new ValueBar.OnValueChangedListener() {
            @Override
            public void onValueChanged(int value) {

            }
        });

        return view;
    }
}
