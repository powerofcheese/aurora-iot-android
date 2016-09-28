package com.happylrd.aurora;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.github.danielnilsson9.colorpickerview.dialog.ColorPickerDialogFragment;
import com.sa90.materialarcmenu.ArcMenu;
import com.sa90.materialarcmenu.StateChangeListener;

import java.util.ArrayList;
import java.util.List;

public class ShoesActivity extends AppCompatActivity implements ColorPickerDialogFragment.ColorPickerDialogListener {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FloatingActionButton fab_color_picker;
    private ArcMenu scenes_menu;
    private CoordinatorLayout des;


    private static final int DIALOG_ID = 0;
    Fragment_shoe_left Left_shoe;

    public ShoesActivity() {
    }

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, ShoesActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoes);
        Left_shoe = new Fragment_shoe_left();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("CUSTOM");
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
                ColorPickerDialogFragment f = ColorPickerDialogFragment
                        .newInstance(DIALOG_ID, null, null, Color.BLACK, true);
                f.setStyle(DialogFragment.STYLE_NORMAL, R.style.LightPickerDialogTheme);
                f.show(getFragmentManager(), "d");
            }
        });

        scenes_menu = (ArcMenu) findViewById(R.id.scenes_menu);
        des = (CoordinatorLayout)findViewById(R.id.main_content);
        scenes_menu.setStateChangeListener(new StateChangeListener() {
            @Override
            public void onMenuOpened() {
                des.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        scenes_menu.toggleMenu();
                    }

                });
            }

            @Override
            public void onMenuClosed() {
                    des.setOnClickListener(null);
            }
        });
        findViewById(R.id.scenes_loop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String send = "cycle";
                String[] temp = Left_shoe.getLC();
                for(int i = 0;i < Left_shoe.getLC().length;i++){
                    send = send +" " + temp[i];
                }
                MainActivity.service.write(send.getBytes());
            }
        });
        findViewById(R.id.scenes_breath).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String send = "breath";
                MainActivity.service.write(send.getBytes());
            }
        });
        findViewById(R.id.scenes_shining).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String send = "shining";
                MainActivity.service.write(send.getBytes());
            }
        });

    }




    public void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(Left_shoe, "左鞋");
        adapter.addFragment(new Fragment_shoe_right(), "右鞋");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onColorSelected(int dialogId, int color) {
        if(Left_shoe != null)
            Left_shoe.setColor(color);
        else
            Log.i("aaa", "碎片 = null！");
    }

    @Override
    public void onDialogDismissed(int dialogId) {

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

    private static String colorToHexString(int color) {
        return String.format("#%06X", 0xFFFFFFFF & color);
    }

}
