package com.happylrd.aurora;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by lenovo on 2016/9/2.
 */
public class photos_Activity extends AppCompatActivity {

    private ListImageDirPopupWindow mDirPopupWindow;

    private GridView mGridView;
    private List<String> mImgs;
    private ImageAdapter mImageAdapter;

    private RelativeLayout mBottomly;
    private TextView mDirname;
    private TextView mDirCount;

    private File mCurrentDir;
    private int mMaxCount;

    private List<FolderBean> mFolderBeans = new ArrayList<FolderBean>();

    private ProgressDialog progressDialog;
    private Button mReturn;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 11){
                progressDialog.dismiss();
                //绑定数据到View中
                data2View();
                initDirPopupWindow();
            }
        }
    };

    protected void initDirPopupWindow() {
        mDirPopupWindow = new ListImageDirPopupWindow(this,mFolderBeans)  ;
        mDirPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                lightOn();
            }
        });
        mDirPopupWindow.setOnDirSelectedListener(new ListImageDirPopupWindow.OnDirSelectedListener() {
            @Override
            public void OnSelected(FolderBean folderBean) {
                mCurrentDir = new File(folderBean.getDir());
                mImgs = Arrays.asList( mCurrentDir.list(new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String filename) {
                        if(filename.endsWith(".jpg")||filename.endsWith(".jepg")||filename.endsWith(".png"))
                            return true;
                        return false;
                    }
                }));
                mImageAdapter = new ImageAdapter(photos_Activity.this,mImgs,mCurrentDir.getAbsolutePath());
                mGridView.setAdapter(mImageAdapter);
                mDirCount.setText(" " + mImgs.size());
                mDirname.setText(folderBean.getName());
                mDirPopupWindow.dismiss();
            }

        });
    }

    //内容区域变亮
    protected void lightOn(){
        WindowManager.LayoutParams LP = getWindow().getAttributes();
        LP.alpha = 1.0f;
        getWindow().setAttributes(LP);
    }

    //内容区域变暗
    protected void lightOff() {
        WindowManager.LayoutParams LP = getWindow().getAttributes();
        LP.alpha = 0.3f;
        getWindow().setAttributes(LP);
    }

    protected void data2View() {
        if(mCurrentDir == null){
            Toast.makeText(this,"未扫描到任何图片",Toast.LENGTH_SHORT).show();
            return;
        }
        mImgs = Arrays.asList(mCurrentDir.list());
        mImageAdapter = new ImageAdapter(this,mImgs,mCurrentDir.getAbsolutePath());
        mGridView.setAdapter(mImageAdapter);
        mDirCount.setText(mMaxCount + " ");
        mDirname.setText(mCurrentDir.getName());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photos_activity);
        //初始化控件
        initView();
        //获取想要显示的图片
        initDatas();
        //初始化s事件
        initEvent();
    }

    private void initEvent() {
        mBottomly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mDirPopupWindow.setAnimationStyle(animationstyle);
                mDirPopupWindow.showAsDropDown(mBottomly, 0, 0);
                lightOff();
            }
        });
        mReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                Bundle bundle = new Bundle();
                bundle.putStringArray("paths", (String[]) mImageAdapter.getSeletedImg().toArray(new String[0]));
                i.putExtra("PATH", bundle);
                setResult(Activity.RESULT_OK, i);
                finish();
            }
        });
    }



    private void initView() {
        mGridView = (GridView) findViewById(R.id.id_gridView);
        mBottomly = (RelativeLayout) findViewById(R.id.id_bottom_ly);
        mDirCount = (TextView) findViewById(R.id.id_dir_count);
        mDirname = (TextView) findViewById(R.id.id_dir_name);
        mReturn = (Button) findViewById(R.id.id_galleryPhotos_seleted);
    }

    //利用ContentProvider扫描手机中的所有图片
    private void initDatas(){
        if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            Toast.makeText(this, "当前存储卡不可用！", Toast.LENGTH_LONG).show();
            return;
        }
        progressDialog = ProgressDialog.show(this, null, "正在加载...");
        new Thread(){
            @Override
            public void run() {
                Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver cr = getContentResolver();
                Cursor cursor = cr.query(mImageUri, null,// MediaStore.Images.Media.MIME_TYPE + "=? or " + MediaStore.Images.Media.MIME_TYPE + "=?", new String[]{"image/jpeg", "image/png"},
                       null,null, MediaStore.Images.Media.DATE_TAKEN + " desc");

                Set<String> mDirPaths = new HashSet<String>();

                while (cursor.moveToNext()){
                    String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    File parentFile = new File(path).getParentFile();
                    if(parentFile == null)
                        continue;
                    String dirPath = parentFile.getAbsolutePath();
                    FolderBean folderBean = null;

                    if(mDirPaths.contains(dirPath))
                        continue;
                    else {
                        mDirPaths.add(dirPath);
                        folderBean = new FolderBean();
                        folderBean.setDir(dirPath);
                        folderBean.setFirstImgPath(path);
                    }
                    if(parentFile.list() == null)
                        continue;
                    int  picSize = parentFile.list(new FilenameFilter() {
                        @Override
                        public boolean accept(File dir, String filename) {
                            if(filename.endsWith(".jpg")||filename.endsWith(".jepg")||filename.endsWith(".png"))
                                return true;
                            return false;
                        };
                    }).length;
                    folderBean.setCount(picSize);
                    mFolderBeans.add(folderBean);

                    if(picSize > mMaxCount){
                        mMaxCount = picSize;
                        mCurrentDir = parentFile;
                    }
                }
                cursor.close();
                //通知Handler扫描图片完成
                mHandler.sendEmptyMessage(11);
            }
        }.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mImageAdapter.getSeletedImg().clear();
    }
}
