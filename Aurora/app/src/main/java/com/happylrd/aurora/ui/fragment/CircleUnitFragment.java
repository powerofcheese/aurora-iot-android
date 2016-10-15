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

    private CircleView cv_item_circle;

    public static CircleUnitFragment newInstanceAlpha() {
        CircleUnitFragment circleUnitFragment = new CircleUnitFragment();
        return circleUnitFragment;
    }

    public static CircleUnitFragment newInstance(int colorId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_COLOR_ID, colorId);

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

        View view = inflater.inflate(R.layout.fragment_circle_unit, container, false);

        initView(view);
        initData(colorId);
        initListener();

        return view;
    }

    private void initView(View view) {
        cv_item_circle = (CircleView) view.findViewById(R.id.cv_item_circle);
    }

    private void initData(int colorId) {
        cv_item_circle.setStrokeColor(colorId);
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
}
