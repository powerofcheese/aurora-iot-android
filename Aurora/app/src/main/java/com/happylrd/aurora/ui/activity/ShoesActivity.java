package com.happylrd.aurora.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import com.google.gson.Gson;
import com.happylrd.aurora.R;
import com.happylrd.aurora.model.Motion;
import com.happylrd.aurora.todo.colorMode.ColorController;
import com.happylrd.aurora.todo.colorMode.ColorFolder;
import com.happylrd.aurora.ui.dialog.ActionTabDialog;
import com.happylrd.aurora.ui.dialog.AnimTabDialog;
import com.happylrd.aurora.ui.dialog.ColorPickerDialog;
import com.happylrd.aurora.ui.dialog.PatternTabDialog;
import com.happylrd.aurora.ui.dialog.RotationTabDialog;
import com.happylrd.aurora.ui.fragment.LeftShoeFragment;
import com.happylrd.aurora.ui.fragment.RightShoeFragment;
import com.happylrd.aurora.util.ZhToEnMapUtil;

import java.util.ArrayList;
import java.util.List;

public class ShoesActivity extends AppCompatActivity {

    private static final String TAG = "ShoesActivity";

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

    // need to be normalized later
    private ColorController mColorController;
    private ColorFolder mColorFolder = new ColorFolder();

    private ZhToEnMapUtil mZhToEnMapUtil;

    private List<Integer> tempCustomColorList;

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
        initData();
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
        fabMenu.setOnFloatingActionsMenuUpdateListener(
                new FloatingActionsMenu.OnFloatingActionsMenuUpdateListener() {
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

    private void initData() {
        mZhToEnMapUtil = new ZhToEnMapUtil();
        mZhToEnMapUtil.initMap(ShoesActivity.this);

        tempCustomColorList = new ArrayList<>();
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

        if (id == R.id.menu_item_preview) {
            Log.d(TAG, getJsonByMotion());

            // can pass json data to edison board

            doPreview();

            Log.d(TAG, getJsonByMotion());
        }

        if (id == R.id.menu_item_done) {
            Intent intent = StateActivity.newIntent(ShoesActivity.this, mMotion);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * a method for preview the magic shoes
     */
    private void doPreview() {
        mColorController = new ColorController(mLeftShoeFragment.getLeftShoe());

        if (mMotion.getIntColorList() != null) {
            mColorFolder.setColorList(mMotion.getIntColorList());
        } else {
            // just as the default value temporarily
            List<Integer> defaultColors = new ArrayList<>();
            defaultColors.add(Color.GRAY);
            mColorFolder.setColorList(defaultColors);
        }

        if (mMotion.getPatternName() != null) {
            mColorFolder.setPattern(
                    mZhToEnMapUtil.getEnValueByZhKey(mMotion.getPatternName())
            );
        } else {
            mColorFolder.setPattern(
                    getString(R.string.pattern_custom_en)
            );

            mMotion.setPatternName(
                    getString(R.string.pattern_custom)
            );
        }

        if (mMotion.getAnimationName() != null) {
            mColorFolder.setAnimation(
                    mZhToEnMapUtil.getEnValueByZhKey(mMotion.getAnimationName())
            );
        } else {
            mColorFolder.setAnimation(
                    getString(R.string.anim_nothing_en)
            );

            mMotion.setAnimationName(
                    getString(R.string.anim_nothing)
            );
        }

        if (mMotion.getRotationName() != null) {
            mColorFolder.setRotation(
                    mZhToEnMapUtil.getEnValueByZhKey(mMotion.getRotationName())
            );
        } else {
            mColorFolder.setRotation(
                    getString(R.string.rotation_nothing_en)
            );

            mMotion.setRotationName(
                    getString(R.string.rotation_nothing)
            );
        }

        mColorController.Control(mColorFolder);
    }

    private String getJsonByMotion() {
        Gson gson = new Gson();
        String json = gson.toJson(mMotion);
        return json;
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

    public void setPatternColorListFromDialog(List<Integer> intColorList) {
//        clearTempCustomColorList();

        mMotion.setIntColorList(intColorList);
        for (Integer item : mMotion.getIntColorList()) {
            Log.d("ReceivePatternColor:", item + "");
        }
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

//    public void setTempCustomColorList(){
//        String[] strColors = mLeftShoeFragment.getLeftShoe().getLC();
//
//        for(int i =0; i < strColors.length; i++){
//            tempCustomColorList.add(
//                    Integer.parseInt(strColors[i])
//            );
//        }
//    }
//
//    public void clearTempCustomColorList(){
//        Log.d("SizeBeforeClear ", tempCustomColorList.size() + "");
//        tempCustomColorList.clear();
//        Log.d("SizeAfterClear ", tempCustomColorList.size() + "");
//    }
}
