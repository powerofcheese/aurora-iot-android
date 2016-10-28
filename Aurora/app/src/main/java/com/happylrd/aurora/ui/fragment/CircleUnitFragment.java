package com.happylrd.aurora.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.pavlospt.CircleView;
import com.happylrd.aurora.R;

public class CircleUnitFragment extends Fragment {

    private static final String ARG_COLOR_ID = "color_id";
    private static final String ARG_MODE_NAME = "mode_name";

    private CircleView cv_item_circle;

    public static CircleUnitFragment newInstance(int colorId, String modeName) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_COLOR_ID, colorId);
        args.putSerializable(ARG_MODE_NAME, modeName);

        CircleUnitFragment circleUnitFragment = new CircleUnitFragment();
        circleUnitFragment.setArguments(args);
        return circleUnitFragment;
    }

    public CircleUnitFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        int colorId = (int) getArguments().getSerializable(ARG_COLOR_ID);
        String modeName = (String) getArguments().getSerializable(ARG_MODE_NAME);

        View view = inflater.inflate(R.layout.fragment_circle_unit, container, false);

        initView(view);
        initData(colorId, modeName);
        initListener();

        return view;
    }

    private void initView(View view) {
        cv_item_circle = (CircleView) view.findViewById(R.id.cv_item_circle);
    }

    private void initData(int colorId, String modeName) {
        cv_item_circle.setStrokeColor(colorId);
        cv_item_circle.setTitleText(modeName);
    }

    private void initListener() {
        cv_item_circle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "已选中当前模式", Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }

    public String getMotionName(){
        return cv_item_circle.getTitleText();
    }
}
