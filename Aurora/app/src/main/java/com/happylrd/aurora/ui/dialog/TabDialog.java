package com.happylrd.aurora.ui.dialog;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.happylrd.aurora.R;
import com.happylrd.aurora.adapter.TabAdapter;

import me.relex.circleindicator.CircleIndicator;

public class TabDialog extends DialogFragment {

    private ViewPager mViewPager;
    private TabAdapter mTabAdapter;
    private CircleIndicator mCircleIndicator;

    private Button btn_ok;

    public static TabDialog newInstance() {
        TabDialog tabDialog = new TabDialog();
        return tabDialog;
    }

    public TabDialog() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_tab, container);

        initView(view);
        initListener();
        initData();

        return view;
    }

    private void initView(View view) {
        mViewPager = (ViewPager) view.findViewById(R.id.view_pager);
        mCircleIndicator = (CircleIndicator) view.findViewById(R.id.circle_indicator);
        btn_ok = (Button) view.findViewById(R.id.btn_ok);
    }

    private void initListener() {
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private void initData() {
        mTabAdapter = new TabAdapter(getChildFragmentManager());

        initDataByFragment();

        mViewPager.setAdapter(mTabAdapter);
        mCircleIndicator.setViewPager(mViewPager);
    }

    /**
     * a method for sub-class to process the logic of initializing data
     */
    public void initDataByFragment() {

    }
}
