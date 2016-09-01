package com.happylrd.aurora;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.OpacityBar;
import com.larswerkman.holocolorpicker.SaturationBar;
import com.larswerkman.holocolorpicker.ValueBar;

import java.util.ArrayList;
import java.util.List;

public class ShoesActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FloatingActionButton fab_color_picker;

    private ColorPicker colorPicker;
    private OpacityBar opacityBar;
    private SaturationBar saturationBar;
    private ValueBar valueBar;
    private Button btn_determine_color;

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, ShoesActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoes);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("我的鞋子");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        fab_color_picker = (FloatingActionButton) findViewById(R.id.fab_color_picker);
        fab_color_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showColorPickerDialog();
            }
        });
    }

    public void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(new CardFindFragment(), "左鞋");
        adapter.addFragment(new ListMessageFragment(), "右鞋");
        viewPager.setAdapter(adapter);
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public Adapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }
    }

    private void showColorPickerDialog() {
        AlertDialog.Builder colorDialogBuilder =
                new AlertDialog.Builder(ShoesActivity.this);
        LayoutInflater inflater = LayoutInflater.from(ShoesActivity.this);
        View dialogView = inflater.inflate(R.layout.dialog_color_picker, null);

        colorPicker = (ColorPicker) dialogView.findViewById(R.id.color_picker);
        opacityBar = (OpacityBar) dialogView.findViewById(R.id.opacity_bar);
        saturationBar = (SaturationBar) dialogView.findViewById(R.id.saturation_bar);
        valueBar = (ValueBar) dialogView.findViewById(R.id.value_bar);

        colorPicker.addOpacityBar(opacityBar);
        colorPicker.addSaturationBar(saturationBar);
        colorPicker.addValueBar(valueBar);

        btn_determine_color = (Button) dialogView.findViewById(R.id.btn_determine_color);
        btn_determine_color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorPicker.setOldCenterColor(colorPicker.getColor());
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

        colorDialogBuilder.setTitle("选择颜色")
                .setCancelable(false)
                .setView(dialogView)
                .setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // set shoes color, just a demo below
                                // shoesColor = colorPicker.getColor();
                            }
                        })
                .setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

        colorDialogBuilder.create().show();
    }
}
