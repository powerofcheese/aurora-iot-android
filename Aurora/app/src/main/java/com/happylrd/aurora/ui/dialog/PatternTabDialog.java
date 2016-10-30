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
import android.widget.ImageButton;

import com.github.pavlospt.CircleView;
import com.happylrd.aurora.R;
import com.happylrd.aurora.adapter.TabAdapter;
import com.happylrd.aurora.ui.activity.ShoesActivity;
import com.happylrd.aurora.ui.fragment.CircleUnitFragment;
import com.happylrd.aurora.util.DialogUtil;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;

public class PatternTabDialog extends DialogFragment {
    private ViewPager viewPager;
    private TabAdapter adapter;
    private CircleIndicator circleIndicator;

    private RecyclerView mRecyclerView;
    private ColorAdapter mColorAdapter;

    public ColorAdapter getColorAdapter() {
        return mColorAdapter;
    }

    private ImageButton ibtn_add_color;
    private Button btn_ok;

    public static PatternTabDialog newInstance() {
        PatternTabDialog patternTabDialog = new PatternTabDialog();
        return patternTabDialog;
    }

    public PatternTabDialog() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_pattern_tab, container);

        initView(view);
        initListener();
        initData();

        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 6));
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

        ibtn_add_color = (ImageButton) view.findViewById(R.id.ibtn_add_color);
        btn_ok = (Button) view.findViewById(R.id.btn_ok);
    }

    private void initListener() {

        ibtn_add_color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorMotionDialog colorMotionDialog = ColorMotionDialog.newInstance();
                colorMotionDialog.setTargetFragment(PatternTabDialog.this, 0);
                colorMotionDialog.show(getFragmentManager(), "colorMotionDialog");
            }
        });

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String patternMotionName = ((CircleUnitFragment)
                        (adapter.getItem(viewPager.getCurrentItem())))
                        .getMotionName();

                ((ShoesActivity) getActivity())
                        .setPatternMotionNameFromDialog(patternMotionName);

                ((ShoesActivity) getActivity())
                        .setPatternColorListFromDialog(mColorAdapter.getIntColorList());

                dismiss();
            }
        });
    }

    private void initData() {
        adapter = new TabAdapter(getChildFragmentManager());

        adapter.addFragment(CircleUnitFragment
                        .newInstance(Color.YELLOW, getString(R.string.pattern_single_color)),
                getString(R.string.number_one));
        adapter.addFragment(CircleUnitFragment
                        .newInstance(Color.YELLOW, getString(R.string.pattern_gradation)),
                getString(R.string.number_two));
        adapter.addFragment(CircleUnitFragment
                        .newInstance(Color.YELLOW, getString(R.string.pattern_thin_stripe)),
                getString(R.string.number_three));
        adapter.addFragment(CircleUnitFragment
                        .newInstance(Color.YELLOW, getString(R.string.pattern_thick_stripe)),
                getString(R.string.number_four));
        adapter.addFragment(CircleUnitFragment
                        .newInstance(Color.YELLOW, getString(R.string.pattern_half_stripe)),
                getString(R.string.number_five));
        adapter.addFragment(CircleUnitFragment
                        .newInstance(Color.YELLOW, getString(R.string.pattern_skip_stripe)),
                getString(R.string.number_six));
        adapter.addFragment(CircleUnitFragment
                        .newInstance(Color.YELLOW, getString(R.string.pattern_gradation_skipping)),
                getString(R.string.number_seven));
        adapter.addFragment(CircleUnitFragment
                        .newInstance(Color.YELLOW, getString(R.string.pattern_random)),
                getString(R.string.number_eight));
        adapter.addFragment(CircleUnitFragment
                        .newInstance(Color.YELLOW, getString(R.string.pattern_random_skipping)),
                getString(R.string.number_nine));
        adapter.addFragment(CircleUnitFragment
                        .newInstance(Color.YELLOW, getString(R.string.pattern_similar_color)),
                getString(R.string.number_ten));

        viewPager.setAdapter(adapter);
        circleIndicator.setViewPager(viewPager);
    }

    public class ColorHolder extends RecyclerView.ViewHolder {

        private Integer mIntColor;

        private CircleView mCircleView;

        public ColorHolder(View itemView) {
            super(itemView);

            mCircleView = (CircleView) itemView.findViewById(R.id.cv_item_color);
            mCircleView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // just as a placeholder
                    DialogUtil.showColorPickerDialog(getFragmentManager());

                }
            });
        }

        public void bindColor(Integer intColor) {
            mIntColor = intColor;
            mCircleView.setFillColor(mIntColor);
        }
    }

    public class ColorAdapter extends RecyclerView.Adapter<ColorHolder> {

        private List<Integer> mIntColorList;

        public List<Integer> getIntColorList() {
            return mIntColorList;
        }

        public ColorAdapter() {
            mIntColorList = new ArrayList<>();
        }

        @Override
        public ColorHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater
                    .inflate(R.layout.item_color_circle, parent, false);
            return new ColorHolder(view);
        }

        @Override
        public void onBindViewHolder(ColorHolder holder, int position) {
            Integer intColor = mIntColorList.get(position);
            holder.bindColor(intColor);
        }

        @Override
        public int getItemCount() {
            return mIntColorList.size();
        }

        public void add(Integer intColor) {
            mIntColorList.add(intColor);
            notifyDataSetChanged();
        }

        public void addAll(List<Integer> intColors) {
            mIntColorList.addAll(intColors);
            notifyDataSetChanged();
        }
    }
}
