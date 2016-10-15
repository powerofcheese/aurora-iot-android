package com.happylrd.aurora.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.happylrd.aurora.R;
import com.hookedonplay.decoviewlib.DecoView;
import com.hookedonplay.decoviewlib.charts.SeriesItem;
import com.hookedonplay.decoviewlib.events.DecoEvent;

public class StepCounterFragment extends Fragment {
    private static DecoView mDecoView;

    private EditText et_step_goal;
    private static TextView tv_step_num;
    private ImageView iv_running;

    private int mBackIndex;
    private static int mDataIndex_1;

    private static int step_goal = 10000;
    // need to be fetched by bluetooth
    public static int step_num = 5500;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_step_counter, container, false);

        mDecoView = (DecoView) view.findViewById(R.id.dynamicArcView);

        tv_step_num = (TextView) view.findViewById(R.id.tv_step_num);

        et_step_goal = (EditText) view.findViewById(R.id.et_step_goal);
        et_step_goal.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                step_goal = Integer.parseInt(et_step_goal.getText().toString());
                setUI();
                return false;
            }
        });

        iv_running = (ImageView) view.findViewById(R.id.iv_running);

        createBackSeries();

        setUI();

        return view;
    }

    private void createBackSeries() {

        SeriesItem seriesItem = new SeriesItem.Builder(Color.argb(255, 218, 218, 218))
                .setRange(0, 100, 100)
                .setInitialVisibility(true)
                .build();

        mBackIndex = mDecoView.addSeries(seriesItem);
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

    public static void setUI() {
        float precent = 1.0f * step_num / step_goal;
        if (precent > 1) {
            precent = 1;
        }
        final SeriesItem seriesItem = new SeriesItem.Builder(Color.argb(255, 255, 193, 0))
                .setRange(0, 100, precent * 100)
                .build();
        seriesItem.addArcSeriesItemListener(new SeriesItem.SeriesItemListener() {
            @Override
            public void onSeriesItemAnimationProgress(float percentComplete, float currentPosition) {
                float percentFilled = (
                        (currentPosition - seriesItem.getMinValue()) /
                                (seriesItem.getMaxValue()));
                tv_step_num.setText("" + (int) (step_goal * percentFilled));
            }

            @Override
            public void onSeriesItemDisplayProgress(float v) {

            }
        });
        mDataIndex_1 = mDecoView.addSeries(seriesItem);
    }
}
