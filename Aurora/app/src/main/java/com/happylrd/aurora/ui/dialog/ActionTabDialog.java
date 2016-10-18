package com.happylrd.aurora.ui.dialog;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.happylrd.aurora.R;
import com.happylrd.aurora.adapter.TabAdapter;
import com.happylrd.aurora.ui.fragment.CircleUnitFragment;

import me.relex.circleindicator.CircleIndicator;

public class ActionTabDialog extends DialogFragment {

    private ViewPager viewPager;
    private TabAdapter adapter;
    private CircleIndicator circleIndicator;

    private RecyclerView mRecyclerView;
    private ColorAdapter mColorAdapter;

    private Button btn_ok;

    public static ActionTabDialog newInstance() {
        ActionTabDialog actionTabDialog = new ActionTabDialog();
        return actionTabDialog;
    }

    public ActionTabDialog() {
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

        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 8));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mColorAdapter = new ColorAdapter();
        mRecyclerView.setAdapter(mColorAdapter);

        return view;
    }

    private void initView(View view) {
        viewPager = (ViewPager) view.findViewById(R.id.view_pager);
        circleIndicator = (CircleIndicator) view.findViewById(R.id.circle_indicator);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
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
        adapter = new TabAdapter(getChildFragmentManager());

        adapter.addFragment(CircleUnitFragment
                        .newInstance(Color.YELLOW, getString(R.string.action_quick_in_out)),
                getString(R.string.number_one));
        adapter.addFragment(CircleUnitFragment
                        .newInstance(Color.YELLOW, getString(R.string.action_fade_in_out)),
                getString(R.string.number_two));
        adapter.addFragment(CircleUnitFragment
                        .newInstance(Color.YELLOW, getString(R.string.action_power_slow)),
                getString(R.string.number_three));
        adapter.addFragment(CircleUnitFragment
                        .newInstance(Color.YELLOW, getString(R.string.action_power_quick)),
                getString(R.string.number_four));

        viewPager.setAdapter(adapter);
        circleIndicator.setViewPager(viewPager);
    }

    private class ColorHolder extends RecyclerView.ViewHolder {

        public ColorHolder(View itemView) {
            super(itemView);

        }

        public void bindColor() {
        }
    }

    private class ColorAdapter extends RecyclerView.Adapter<ColorHolder> {

        private static final int LENGTH = 3;

        @Override
        public ColorHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater
                    .inflate(R.layout.item_color_circle, parent, false);
            return new ColorHolder(view);
        }

        @Override
        public void onBindViewHolder(ColorHolder holder, int position) {
        }

        @Override
        public int getItemCount() {
            return LENGTH;
        }
    }
}
