package com.happylrd.aurora.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.happylrd.aurora.todo.MusicActivity;
import com.happylrd.aurora.R;
import com.happylrd.aurora.model.MyUser;
import com.happylrd.aurora.ui.fragment.CardFindFragment;
import com.happylrd.aurora.ui.fragment.ListModeFragment;
import com.happylrd.aurora.ui.fragment.MyInfoFragment;
import com.happylrd.aurora.ui.fragment.StepCounterFragment;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private DrawerLayout mDrawerLayout;
    private NavigationView navigationView;
    private View mHeadView;
    private CircleImageView mNavHeadPortrait;
    private TextView mNavNickName;

    private Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    private FrameLayout frameLayout;
    private FloatingActionsMenu fabMenu;
    private FloatingActionButton fab_write_sth;
    private FloatingActionButton fab_wrap_shoes;
    private FloatingActionButton fab_open_music;

    /**
     * just for bluetooth, but need structured
     */
    public static final int MESSAGE_READ = 4;
    public static final int MESSAGE_TOAST = 6;

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initListener();
        initData();
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("极光");
        setSupportActionBar(toolbar);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        mHeadView = navigationView.getHeaderView(0);
        mNavHeadPortrait = (CircleImageView) mHeadView.findViewById(R.id.civ_head_portrait);
        mNavNickName = (TextView) mHeadView.findViewById(R.id.tv_nick_name);

        // add menu icon to Toolbar
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }

        frameLayout = (FrameLayout) findViewById(R.id.frame_layout);
        frameLayout.getBackground().setAlpha(0);

        fabMenu = (FloatingActionsMenu) findViewById(R.id.fab_menu);
        fab_write_sth = (FloatingActionButton) findViewById(R.id.fab_write_sth);
        fab_wrap_shoes = (FloatingActionButton) findViewById(R.id.fab_wrap_shoes);
        fab_open_music = (FloatingActionButton) findViewById(R.id.fab_open_music);
    }

    private void initListener() {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {

                        menuItem.setChecked(true);
                        // TODO: handle navigation

                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });

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

        fab_write_sth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = WriteSthActivity.newIntent(MainActivity.this);
                startActivity(intent);

                frameLayout.getBackground().setAlpha(0);
                fabMenu.collapse();
            }
        });

        fab_wrap_shoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = StateActivity.newIntent(MainActivity.this);
                startActivity(intent);

                frameLayout.getBackground().setAlpha(0);
                fabMenu.collapse();
            }
        });

        fab_open_music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = MusicActivity.newIntent(MainActivity.this);
                startActivity(intent);

                frameLayout.getBackground().setAlpha(0);
                fabMenu.collapse();
            }
        });
    }

    private void initData() {
        MyUser currentUser = BmobUser.getCurrentUser(MyUser.class);
        mNavNickName.setText(currentUser.getNickName());
        if (currentUser.getHeadPortraitPath() != null) {
            Glide.with(MainActivity.this)
                    .load(currentUser.getHeadPortraitPath())
                    .into(mNavHeadPortrait);
            setBgColorByHeadPortrait(currentUser.getHeadPortraitPath());
        } else {
            setBgColorByDefaultHeadPortrait();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_more) {
            return true;
        } else if (id == android.R.id.home) {
            mDrawerLayout.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }

    public void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(new ListModeFragment(), "模式");
        adapter.addFragment(new StepCounterFragment(), "计步");
        adapter.addFragment(new CardFindFragment(), "发现");
        adapter.addFragment(new MyInfoFragment(), "我的");
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

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    private void setBgColorByDefaultHeadPortrait() {
        BitmapDrawable bitmapDrawable =
                (BitmapDrawable) getResources().getDrawable(R.drawable.default_head_portrait);
        Bitmap bitmap = bitmapDrawable.getBitmap();
        usePaletteByBitmap(bitmap);
    }

    private void setBgColorByHeadPortrait(String headPortraitPath) {
        Glide.with(MainActivity.this)
                .load(headPortraitPath)
                .asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        usePaletteByBitmap(resource);
                    }
                });
    }

    private void usePaletteByBitmap(Bitmap bitmap) {
        Palette.Builder builder = Palette.from(bitmap);
        builder.generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                Palette.Swatch vibrantSwatch = palette.getVibrantSwatch();

                if (vibrantSwatch != null) {
                    mHeadView.setBackgroundColor(vibrantSwatch.getRgb());
                } else {
                    Log.d(TAG, "getVibrantSwatch() is null");
                    // need to deal with
                }
            }
        });
    }
}
