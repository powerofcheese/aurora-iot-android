package com.happylrd.aurora;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hookedonplay.decoviewlib.DecoView;
import com.hookedonplay.decoviewlib.charts.SeriesItem;
import com.hookedonplay.decoviewlib.events.DecoEvent;

public class StepCounterFragment extends Fragment {

    private DecoView decoView;

    private TextView tv_percentage;
    private ImageView iv_step_goal;
    private TextView tv_step_goal;

    private String[] step_goal_array = new String[]{"10", "50", "100", "500", "1000"};
    private int step_goal_array_selected_index = 0;

    private final String format = "%.0f%%";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_step_counter, container, false);

        decoView = (DecoView) view.findViewById(R.id.dynamicArcView);

        decoView.addSeries(new SeriesItem.Builder(Color.argb(255, 218, 218, 218))
                .setRange(0, 100, 100)
                .setInitialVisibility(true)
                .build());

        final SeriesItem seriesItem = new SeriesItem.Builder(Color.argb(255, 221, 19, 29))
                .setRange(0, 100, 0)
                .build();

        int series1Index = decoView.addSeries(seriesItem);

        decoView.addEvent(new DecoEvent.Builder(50)
                .setIndex(series1Index)
                .setDelay(2000)
                .build());

        decoView.addEvent(new DecoEvent.Builder(100)
                .setIndex(series1Index)
                .setDelay(5000)
                .build());

        tv_percentage = (TextView) view.findViewById(R.id.textPercentage);

        seriesItem.addArcSeriesItemListener(new SeriesItem.SeriesItemListener() {
            @Override
            public void onSeriesItemAnimationProgress(float percentComplete, float currentPosition) {
                if (format.contains("%%")) {
                    float percentFilled = ((currentPosition - seriesItem.getMinValue()) / (seriesItem.getMaxValue() - seriesItem.getMinValue()));
                    tv_percentage.setText(String.format(format, percentFilled * 100f));
                } else {
                    tv_percentage.setText(String.format(format, currentPosition));
                }
            }

            @Override
            public void onSeriesItemDisplayProgress(float v) {

            }
        });

        tv_step_goal = (TextView) view.findViewById(R.id.tv_step_goal);

        iv_step_goal = (ImageView) view.findViewById(R.id.iv_step_goal);
        iv_step_goal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog =
                        new AlertDialog.Builder(getActivity());
                dialog.setTitle("目标(单位:km)")
                        .setSingleChoiceItems(step_goal_array, step_goal_array_selected_index,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        step_goal_array_selected_index = which;
                                    }
                                })
                        .setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        tv_step_goal.setText(step_goal_array[step_goal_array_selected_index] + "km");
                                    }
                                })
                        .setNegativeButton("取消",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })
                        .create();
                dialog.show();
            }
        });

        return view;
    }
}
