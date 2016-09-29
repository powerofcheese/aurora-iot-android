package com.happylrd.aurora;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hookedonplay.decoviewlib.DecoView;
import com.hookedonplay.decoviewlib.charts.SeriesItem;
import com.hookedonplay.decoviewlib.events.DecoEvent;

public class StepCounterFragment extends Fragment {

    private DecoView mDecoView;

    private TextView tv_step_goal;
    private TextView tv_percentage;
    private Button btn_set_step_goal;
    private Button btn_start_counter;
    private ImageView iv_running;
    private TextView tv_running;

    private String[] step_goal_array = new String[]{"10", "50", "100", "500", "1000"};
    private int step_goal_array_selected_index = 0;

    private final String percentage_text_format = "%.0f%%";
    private final String running_text_format = "%.0f Km";

    private static final String DEFAULT_GOAL_TEXT = "200";

    private int mBackIndex;
    private int mDataIndex_1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_step_counter, container, false);

        mDecoView = (DecoView) view.findViewById(R.id.dynamicArcView);

        tv_percentage = (TextView) view.findViewById(R.id.tv_percentage);
        tv_running = (TextView) view.findViewById(R.id.tv_running);

        tv_step_goal = (TextView) view.findViewById(R.id.tv_step_goal);
        tv_step_goal.setText(DEFAULT_GOAL_TEXT);

        btn_start_counter = (Button) view.findViewById(R.id.btn_start_counter);
        btn_start_counter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDataSeries1();
                createEvents();
            }
        });

        btn_set_step_goal = (Button) view.findViewById(R.id.btn_set_step_goal);
        btn_set_step_goal.setOnClickListener(new View.OnClickListener() {
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
                                        tv_step_goal.setText(step_goal_array[step_goal_array_selected_index]);
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

        iv_running = (ImageView) view.findViewById(R.id.iv_running);

        createBackSeries();

        return view;
    }

    private void createBackSeries() {

        SeriesItem seriesItem = new SeriesItem.Builder(Color.argb(255, 218, 218, 218))
                .setRange(0, 100, 100)
                .setInitialVisibility(true)
                .build();

        mBackIndex = mDecoView.addSeries(seriesItem);
    }

    private void createDataSeries1() {
        final SeriesItem seriesItem = new SeriesItem.Builder(Color.argb(255, 221, 19, 29))
                .setRange(0, 100, 0)
                .build();

        seriesItem.addArcSeriesItemListener(new SeriesItem.SeriesItemListener() {
            @Override
            public void onSeriesItemAnimationProgress(float percentComplete, float currentPosition) {
                float percentFilled = (
                        (currentPosition - seriesItem.getMinValue()) /
                                (seriesItem.getMaxValue() - seriesItem.getMinValue()));
                tv_percentage.setText(
                        String.format(percentage_text_format, percentFilled * 100f));
            }

            @Override
            public void onSeriesItemDisplayProgress(float v) {

            }
        });

        seriesItem.addArcSeriesItemListener(new SeriesItem.SeriesItemListener() {
            @Override
            public void onSeriesItemAnimationProgress(float percentComplete, float currentPosition) {
                float percentFilled = (
                        (currentPosition - seriesItem.getMinValue()) /
                                (seriesItem.getMaxValue() - seriesItem.getMinValue()));
                tv_running.setText(
                        String.format(running_text_format,
                                percentFilled *
                                        Float.parseFloat(tv_step_goal.getText().toString())));
            }

            @Override
            public void onSeriesItemDisplayProgress(float v) {

            }
        });

        mDataIndex_1 = mDecoView.addSeries(seriesItem);
    }

    private void createEvents() {
        mDecoView.addEvent(new DecoEvent.Builder(50)
                .setIndex(mDataIndex_1)
                .setDelay(0)
                .setDuration(2000)
                .build());

        mDecoView.addEvent(new DecoEvent.Builder(100)
                .setIndex(mDataIndex_1)
                .setDelay(4000)
                .setDuration(2000)
                .build());

        mDecoView.addEvent(new DecoEvent.Builder(DecoEvent.EventType.EVENT_HIDE, false)
                .setDelay(8000)
                .setDuration(2000)
                .setListener(new DecoEvent.ExecuteEventListener() {
                    @Override
                    public void onEventStart(DecoEvent decoEvent) {
                        Log.d("Amimator state", "Hide of DecoView Starting");
                    }

                    @Override
                    public void onEventEnd(DecoEvent decoEvent) {
                        Log.d("Animator state", "Hide of DecoView Complete");
                    }
                })
                .build());
    }

    private void resetText() {
        // need to add
    }
}
