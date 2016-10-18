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

public class RotationTabDialog extends DialogFragment {

    private ViewPager viewPager;
    private TabAdapter adapter;
    private CircleIndicator circleIndicator;

    private RecyclerView mRecyclerView;
    private ColorAdapter mColorAdapter;

    private Button btn_ok;

    public static RotationTabDialog newInstance() {
        RotationTabDialog rotationTabDialog = new RotationTabDialog();
        return rotationTabDialog;
    }

    public RotationTabDialog() {
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
                        .newInstance(Color.YELLOW, getString(R.string.rotation_float_left)),
                getString(R.string.number_one));
        adapter.addFragment(CircleUnitFragment
                        .newInstance(Color.YELLOW, getString(R.string.rotation_float_right)),
                getString(R.string.number_two));
        adapter.addFragment(CircleUnitFragment
                        .newInstance(Color.YELLOW, getString(R.string.rotation_rotate_left)),
                getString(R.string.number_three));
        adapter.addFragment(CircleUnitFragment
                        .newInstance(Color.YELLOW, getString(R.string.rotation_rotate_right)),
                getString(R.string.number_four));
        adapter.addFragment(CircleUnitFragment
                        .newInstance(Color.YELLOW, getString(R.string.rotation_spin_left)),
                getString(R.string.number_five));
        adapter.addFragment(CircleUnitFragment
                        .newInstance(Color.YELLOW, getString(R.string.rotation_spin_right)),
                getString(R.string.number_six));
        adapter.addFragment(CircleUnitFragment
                        .newInstance(Color.YELLOW, getString(R.string.rotation_swing)),
                getString(R.string.number_seven));
        adapter.addFragment(CircleUnitFragment
                        .newInstance(Color.YELLOW, getString(R.string.rotation_switch)),
                getString(R.string.number_eight));
        adapter.addFragment(CircleUnitFragment
                        .newInstance(Color.YELLOW, getString(R.string.rotation_snake)),
                getString(R.string.number_nine));
        adapter.addFragment(CircleUnitFragment
                        .newInstance(Color.YELLOW, getString(R.string.rotation_random_snake)),
                getString(R.string.number_ten));
        adapter.addFragment(CircleUnitFragment
                        .newInstance(Color.YELLOW, getString(R.string.rotation_move_to_rotation)),
                getString(R.string.number_eleven));
        adapter.addFragment(CircleUnitFragment
                        .newInstance(Color.YELLOW, getString(R.string.rotation_nothing)),
                getString(R.string.number_twelve));

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
