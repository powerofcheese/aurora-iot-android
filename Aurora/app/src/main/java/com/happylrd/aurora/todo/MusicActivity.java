package com.happylrd.aurora.todo;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.happylrd.aurora.R;
import com.sa90.materialarcmenu.ArcMenu;
import com.sa90.materialarcmenu.StateChangeListener;

import java.util.ArrayList;
import java.util.List;

public class MusicActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<String> paths;
    private List<String> names;
    private static MediaPlayer player;

    private FrameLayout frameLayout;
    private ArcMenu fabMenu;

    private ProgressDialog progressDialog;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 11) {
                progressDialog.dismiss();
                //绑定数据到View中
                dataToView();
            }
        }
    };

    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, MusicActivity.class);
        return intent;
    }

    private void dataToView() {
        MusicAdapter musicAdapter = new MusicAdapter(this, names);
        musicAdapter.setOnItemClickListener(new MusicAdapter.onItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {


                play(paths.get(position));
            }
        });
        recyclerView.setAdapter(musicAdapter);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        recyclerView = (RecyclerView) findViewById(R.id.id_music_recyclerView);
        initDatas();
        frameLayout = (FrameLayout) findViewById(R.id.id_music_frame_layout);
        frameLayout.getBackground().setAlpha(0);
        fabMenu = (ArcMenu) findViewById(R.id.id_music_fab_menu);

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
        findViewById(R.id.id_music_fab_pause).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pause();
            }
        });
        findViewById(R.id.id_music_fab_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MusicActivity.this, TryActivity.class);
                startActivity(i);
            }
        });
        findViewById(R.id.id_music_fab_contunite_play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                continuePlay();
            }
        });
        findViewById(R.id.id_music_fab_return).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fabMenu.toggleMenu();
                finish();
            }
        });
    }

    private void initDatas() {
        names = new ArrayList<String>();
        paths = new ArrayList<String>();
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(this, "当前存储卡不可用！", Toast.LENGTH_LONG).show();
            return;
        }
        if (player == null)
            player = new MediaPlayer();
        progressDialog = ProgressDialog.show(this, null, "正在加载...");
        new Thread() {
            @Override
            public void run() {
                Uri mMusicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                ContentResolver cr = getContentResolver();
                Cursor cursor = cr.query(mMusicUri, null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);

                while (cursor.moveToNext()) {
                    names.add(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)));
                    paths.add(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA)));
                }
                cursor.close();
                //通知Handler扫描完成
                mHandler.sendEmptyMessage(11);
            }
        }.start();
    }

    //服务的播放方法
    public void play(final String addr) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                //1： 先重置
                player.reset();
                try {
                    //2:设置数据
                    player.setDataSource(addr);
                    //3.准备
                    player.prepare();
                    //4.开始播放
                    player.start();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }.start();

    }

    //服务的暂停服务
    public void pause() {
        player.pause();
    }

    //服务的继续播放
    public void continuePlay() {
        player.start();
    }

    public static MediaPlayer getPlayer() {
        if (player == null)
            player = new MediaPlayer();
        return player;
    }
}
