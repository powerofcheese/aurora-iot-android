package com.happylrd.aurora;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.sa90.materialarcmenu.ArcMenu;
import com.sa90.materialarcmenu.StateChangeListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private NavigationView navigationView;

    private FrameLayout frameLayout;
    private ArcMenu fabMenu;

    public static BTS service;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    StepCounterFragment.step_num = Integer.parseInt(readMessage);
                    StepCounterFragment.setUI();
                    break;
                case MESSAGE_TOAST:
                    Toast.makeText(getApplicationContext(), msg.getData().getString("TOAST"),
                            Toast.LENGTH_SHORT).show();
                    break;
                case STATE_CHANGE:
                    switch (msg.arg1){
                        case BTS.STATE_NONE:
                            Toast.makeText(getApplicationContext(),"Welcome to the APP-----当前状态为未连接",Toast.LENGTH_SHORT).show();
                            break;
                        case BTS.STATE_CONNECTED:
                            Toast.makeText(getApplicationContext(),"Welcome to the APP-----已连接",Toast.LENGTH_SHORT).show();
                            break;
                    }
                    break;
                case LIGHT:
                    if(service == null){
                        Toast.makeText(getApplicationContext(),"请先连接蓝牙设备！", Toast.LENGTH_LONG).show();
                        return;
                    }
                    String send = msg.arg1 + " " + msg.obj.toString();
                    service.write(send.getBytes());
                    break;

            }
        }
    };
    private static final int REQUEST_OPEN_BT = 1;
    private static final int SEARCH_NEW_DEVICE = 2;
    private static final int SEARCH_PAIRED_DEVICE = 3;
    private static final int DISPLAY_DRAW = 8;

    public static final int LIGHT = 9;
    public static final int MESSAGE_READ = 4;
    public static final int MESSAGE_DEVICE_NAME = 5;
    public static final int MESSAGE_TOAST = 6;
    public static final int STATE_CHANGE = 7;

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);


        // add menu icon to Toolbar
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }

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

        frameLayout = (FrameLayout) findViewById(R.id.frame_layout);
        frameLayout.getBackground().setAlpha(0);
        fabMenu = (ArcMenu) findViewById(R.id.fab_menu);

        fabMenu.setStateChangeListener(new StateChangeListener() {
            @Override
            public void onMenuOpened() {
                frameLayout.getBackground().setAlpha(240);
                frameLayout.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        fabMenu.toggleMenu();
                        return true;
                    }
                });
            }

            @Override
            public void onMenuClosed() {
                frameLayout.getBackground().setAlpha(0);
                frameLayout.setOnTouchListener(null);
            }
        });
        findViewById(R.id.fab_write_sth).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fabMenu.toggleMenu();
                Intent intent = WriteSthActivity.newIntent(MainActivity.this);
                startActivity(intent);
            }
        });
        findViewById(R.id.fab_wrap_shoes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fabMenu.toggleMenu();
                if(service == null){
                    Toast.makeText(getApplicationContext(),"请先连接蓝牙设备！",Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent = ShoesActivity.newIntent(MainActivity.this);
                    startActivity(intent);
                }
            }
        });
       findViewById(R.id.fab_color_picker).setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               fabMenu.toggleMenu();
               // If BT is not on, request that it be enabled.
               BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
               if (!bluetoothAdapter.isEnabled()) {
                   Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                   startActivityForResult(enableIntent, REQUEST_OPEN_BT);
               }
               else
                   service = new BTS(handler);
           }
       });
        findViewById(R.id.fab_others).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fabMenu.toggleMenu();
                Intent intent = new Intent(MainActivity.this,activity_music.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_OPEN_BT){
            if(resultCode == Activity.RESULT_OK){
                Toast.makeText(this,"蓝牙打开成功！", Toast.LENGTH_SHORT).show();
                service = new BTS(handler);
            }
            else {
                Toast.makeText(this,"请打开蓝牙,部分功能需要蓝牙！", Toast.LENGTH_LONG).show();
            }
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

    public void setupViewPager(ViewPager viewPager){
        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(new ListMessageFragment(), "SCENES");
        adapter.addFragment(new StepCounterFragment(), "STEP");
        adapter.addFragment(new CardFindFragment(), "FIND");
        adapter.addFragment(new MyInfoFragment(), "MINE");
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
}
