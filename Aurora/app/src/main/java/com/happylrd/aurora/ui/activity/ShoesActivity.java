package com.happylrd.aurora.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.happylrd.aurora.R;
import com.happylrd.aurora.model.Motion;
import com.happylrd.aurora.ui.dialog.ActionTabDialog;
import com.happylrd.aurora.ui.dialog.AnimTabDialog;
import com.happylrd.aurora.ui.dialog.ColorPickerDialog;
import com.happylrd.aurora.ui.dialog.PatternTabDialog;
import com.happylrd.aurora.ui.dialog.RotationTabDialog;
import com.happylrd.aurora.ui.fragment.LeftShoeFragment;
import com.happylrd.aurora.ui.fragment.RightShoeFragment;

import java.util.ArrayList;
import java.util.List;

public class ShoesActivity extends AppCompatActivity {

    private Motion mMotion = new Motion();

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private FrameLayout frameLayout;
    private FloatingActionsMenu fabMenu;
    private FloatingActionButton fab_color_picker;
    private FloatingActionButton fab_pattern;
    private FloatingActionButton fab_anim;
    private FloatingActionButton fab_rotation;
    private FloatingActionButton fab_action;

    private LeftShoeFragment mLeftShoeFragment;
    private RightShoeFragment mRightShoeFragment;

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, ShoesActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoes);

        initView();
        initListener();
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("我的鞋子");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        frameLayout = (FrameLayout) findViewById(R.id.frame_layout);
        frameLayout.getBackground().setAlpha(0);

        fabMenu = (FloatingActionsMenu) findViewById(R.id.fab_menu);
        fab_color_picker = (FloatingActionButton) findViewById(R.id.fab_color_picker);
        fab_pattern = (FloatingActionButton) findViewById(R.id.fab_pattern);
        fab_anim = (FloatingActionButton) findViewById(R.id.fab_anim);
        fab_rotation = (FloatingActionButton) findViewById(R.id.fab_rotation);
        fab_action = (FloatingActionButton) findViewById(R.id.fab_action);
    }

    private void initListener() {

        fabMenu.setOnFloatingActionsMenuUpdateListener(new FloatingActionsMenu.OnFloatingActionsMenuUpdateListener() {
            @Override
            public void onMenuExpanded() {
                frameLayout.getBackground().setAlpha(240);
                frameLayout.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        fabMenu.collapse();
                        return true;
                    }
                });
            }

            @Override
            public void onMenuCollapsed() {
                frameLayout.getBackground().setAlpha(0);
                frameLayout.setOnTouchListener(null);
            }
        });

        fab_color_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showColorPickerDialog();
                frameLayout.getBackground().setAlpha(0);
                fabMenu.collapse();
            }
        });

        fab_pattern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPatternTabDialog();
                frameLayout.getBackground().setAlpha(0);
                fabMenu.collapse();
            }
        });

        fab_anim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAnimTabDialog();
                frameLayout.getBackground().setAlpha(0);
                fabMenu.collapse();
            }
        });

        fab_rotation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRotationTabDialog();
                frameLayout.getBackground().setAlpha(0);
                fabMenu.collapse();
            }
        });

        fab_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showActionTabDialog();
                frameLayout.getBackground().setAlpha(0);
                fabMenu.collapse();
            }
        });
    }

    public void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());

        mLeftShoeFragment = new LeftShoeFragment();
        mRightShoeFragment = new RightShoeFragment();

        adapter.addFragment(mLeftShoeFragment, "左鞋");
        adapter.addFragment(mRightShoeFragment, "右鞋");
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_shoes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_item_done) {
            Intent intent = StateActivity.newIntent(ShoesActivity.this, mMotion);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
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

    private void showPatternTabDialog() {
        PatternTabDialog patternTabDialog = PatternTabDialog.newInstance();
        patternTabDialog.show(getSupportFragmentManager(), "patternTabDialog");
    }

    private void showAnimTabDialog() {
        AnimTabDialog animTabDialog = AnimTabDialog.newInstance();
        animTabDialog.show(getSupportFragmentManager(), "animTabDialog");
    }

    private void showRotationTabDialog() {
        RotationTabDialog rotationTabDialog = RotationTabDialog.newInstance();
        rotationTabDialog.show(getSupportFragmentManager(), "rotationTabDialog");
    }

    private void showActionTabDialog() {
        ActionTabDialog actionTabDialog = ActionTabDialog.newInstance();
        actionTabDialog.show(getSupportFragmentManager(), "actionTabDialog");
    }

    public void setPatternMotionNameFromDialog(String patternMotionName) {
        mMotion.setPatternName(patternMotionName);
        Log.d("ReceivePatternData", mMotion.getPatternName());
    }

    public void setAnimationMotionNameFromDialog(String animationMotionName) {
        mMotion.setAnimationName(animationMotionName);
        Log.d("ReceiveAnimData", mMotion.getAnimationName());
    }

    public void setRotationMotionNameFromDialog(String rotationMotionName) {
        mMotion.setRotationName(rotationMotionName);
        Log.d("ReceiveRotationData", mMotion.getRotationName());
    }

    public void setActionMotionNameFromDialog(String actionMotionName) {
        mMotion.setActionName(actionMotionName);
        Log.d("ReceiveActionData", mMotion.getActionName());
    }

    private void showColorPickerDialog() {
        ColorPickerDialog colorPickerDialog = ColorPickerDialog.newInstance();
        colorPickerDialog.show(getSupportFragmentManager(), "colorPickerDialog");
    }

    public void setShoesColor(int color) {
        mLeftShoeFragment.setColor(color);
        mRightShoeFragment.setColor(color);
    }
}
