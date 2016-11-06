package com.happylrd.aurora.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.happylrd.aurora.R;
import com.happylrd.aurora.constant.Constants;
import com.happylrd.aurora.model.MyUser;
import com.happylrd.aurora.util.ToastUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import de.hdodenhof.circleimageview.CircleImageView;
import me.nereo.multi_image_selector.MultiImageSelector;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

public class PersonalInfoActivity extends BasePermissionActivity {

    private static final String TAG = "PersonalInfoActivity";

    private final int REQUEST_CAMERA = 0;
    private final int REQUEST_GALLERY = 1;

    private String[] sex_array = new String[]{"男", "女", "未知"};
    private int sex_array_selected_index = 2;

    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Toolbar toolbar;

    private RelativeLayout rl_head_portrait;
    private ImageView iv_head_portrait;
    private File pic_file;
    private CircleImageView civ_head_portrait;

    private RelativeLayout rl_nick_name;
    private TextView tv_nick_name;

    private RelativeLayout rl_sex;
    private TextView tv_sex;

    private RelativeLayout rl_age;
    private TextView tv_age;

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, PersonalInfoActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);

        initView();
        initListener();
        initData();
    }

    private void initView() {
        collapsingToolbarLayout =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tv_nick_name = (TextView) findViewById(R.id.tv_nick_name);
        rl_nick_name = (RelativeLayout) findViewById(R.id.rl_nick_name);
        tv_sex = (TextView) findViewById(R.id.tv_sex);
        rl_sex = (RelativeLayout) findViewById(R.id.rl_sex);
        tv_age = (TextView) findViewById(R.id.tv_age);
        rl_age = (RelativeLayout) findViewById(R.id.rl_age);
        civ_head_portrait = (CircleImageView) findViewById(R.id.civ_head_portrait);
        iv_head_portrait = (ImageView) findViewById(R.id.iv_head_portrait);
        rl_head_portrait = (RelativeLayout) findViewById(R.id.rl_head_portrait);
    }

    private void initListener() {
        rl_nick_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog =
                        new AlertDialog.Builder(PersonalInfoActivity.this);
                LayoutInflater inflater = LayoutInflater.from(PersonalInfoActivity.this);
                View dialogView = inflater.inflate(R.layout.dialog_et_text, null);

                final EditText editText = (EditText) dialogView.findViewById(R.id.edit_text);
                editText.setText(tv_nick_name.getText());
                editText.setSelection(editText.getText().length());

                dialog.setTitle("修改用户名")
                        .setView(dialogView)
                        .setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (editText.getText().toString().equals("")) {
                                            ToastUtil.showInputNotNullToast(PersonalInfoActivity.this);
                                        } else {
                                            String nickName = editText.getText().toString();
                                            updateNickName(nickName);
                                        }
                                    }
                                })
                        .setNegativeButton("取消",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                dialog.create()
                        .show();
            }
        });

        rl_sex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog =
                        new AlertDialog.Builder(PersonalInfoActivity.this);
                dialog.setTitle("性别")
                        .setSingleChoiceItems(sex_array, sex_array_selected_index,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        sex_array_selected_index = which;
                                    }
                                })
                        .setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String str_sex = sex_array[sex_array_selected_index];
                                        updateSex(str_sex);
                                    }
                                })
                        .setNegativeButton("取消",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                dialog.create()
                        .show();
            }
        });

        rl_age.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog =
                        new AlertDialog.Builder(PersonalInfoActivity.this);
                LayoutInflater inflater = LayoutInflater.from(PersonalInfoActivity.this);
                View dialogView = inflater.inflate(R.layout.dialog_et_number, null);

                final EditText editText = (EditText) dialogView.findViewById(R.id.edit_text);
                editText.setText(tv_age.getText());
                editText.setSelection(editText.getText().length());

                dialog.setTitle("修改年龄")
                        .setView(dialogView)
                        .setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (editText.getText().toString().equals("")) {
                                            ToastUtil.showInputNotNullToast(PersonalInfoActivity.this);
                                        } else {
                                            Integer integer_age = Integer.parseInt(
                                                    editText.getText().toString());
                                            updateAge(integer_age);
                                        }
                                    }
                                })
                        .setNegativeButton("取消",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                dialog.create()
                        .show();
            }
        });

        rl_head_portrait.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
    }

    private void initData() {
        MyUser myUser = BmobUser.getCurrentUser(MyUser.class);
        tv_nick_name.setText(myUser.getNickName());
        collapsingToolbarLayout.setTitle(myUser.getNickName());
        tv_sex.setText(myUser.getSex());
        if (myUser.getAge() != null) {
            tv_age.setText(myUser.getAge() + "");
        }

        if (myUser.getHeadPortraitPath() != null) {
            loadHeadPortraitByPath(myUser.getHeadPortraitPath());
            setBgColorByHeadPortrait(myUser.getHeadPortraitPath());
        } else {
            setBgColorByDefaultHeadPortrait();
        }
    }

    private void loadHeadPortraitByPath(String headPortraitPath) {
        Glide.with(PersonalInfoActivity.this)
                .load(headPortraitPath)
                .into(iv_head_portrait);
        Glide.with(PersonalInfoActivity.this)
                .load(headPortraitPath)
                .into(civ_head_portrait);
    }

    private void updateNickName(String nickName) {
        MyUser newUser = new MyUser();
        newUser.setNickName(nickName);
        MyUser myUserNickName = BmobUser.getCurrentUser(MyUser.class);
        newUser.update(myUserNickName.getObjectId(),
                new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            logUpdateSuccess();
                        } else {
                            ToastUtil.showUpdateFailToast(PersonalInfoActivity.this);
                        }
                    }
                });
        tv_nick_name.setText(nickName);
    }

    private void updateSex(String str_sex) {
        MyUser newUser = new MyUser();
        newUser.setSex(str_sex);
        MyUser myUserSex = BmobUser.getCurrentUser(MyUser.class);
        newUser.update(myUserSex.getObjectId(),
                new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            logUpdateSuccess();
                        } else {
                            ToastUtil.showUpdateFailToast(PersonalInfoActivity.this);
                        }
                    }
                });
        tv_sex.setText(str_sex);
    }

    private void updateAge(Integer integer_age) {
        MyUser newUser = new MyUser();
        newUser.setAge(integer_age);
        MyUser myUserAge = BmobUser.getCurrentUser(MyUser.class);
        newUser.update(myUserAge.getObjectId(),
                new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            logUpdateSuccess();
                        } else {
                            ToastUtil.showUpdateFailToast(PersonalInfoActivity.this);
                        }
                    }
                });

        tv_age.setText(integer_age + "");
    }

    private void uploadAndUpdateHeadPortrait(String picPath) {
        if (picPath == null) {
            return;
        }

        final BmobFile bmobFile = new BmobFile(new File(picPath));
        bmobFile.uploadblock(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    updateHeadPortraitPath(bmobFile.getFileUrl());
                } else {
                    Toast.makeText(PersonalInfoActivity.this, "上传头像失败", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });
    }

    private void updateHeadPortraitPath(String headPortraitPath) {
        MyUser newUser = new MyUser();
        newUser.setHeadPortraitPath(headPortraitPath);
        MyUser myUser = BmobUser.getCurrentUser(MyUser.class);
        newUser.update(myUser.getObjectId(),
                new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            logUpdateSuccess();
                        } else {
                            ToastUtil.showUpdateFailToast(PersonalInfoActivity.this);
                        }
                    }
                });

        loadHeadPortraitByPath(headPortraitPath);
        setBgColorByHeadPortrait(headPortraitPath);
    }

    private void setBgColorByDefaultHeadPortrait() {
        BitmapDrawable bitmapDrawable =
                (BitmapDrawable) getResources().getDrawable(R.drawable.default_head_portrait);
        Bitmap bitmap = bitmapDrawable.getBitmap();
        usePaletteByBitmap(bitmap);
    }

    private void setBgColorByHeadPortrait(String headPortraitPath) {
        Glide.with(PersonalInfoActivity.this)
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
                    collapsingToolbarLayout.setBackgroundColor(vibrantSwatch.getRgb());
                    toolbar.setBackgroundColor(vibrantSwatch.getRgb());

                    if (Build.VERSION.SDK_INT >= 21) {
                        Window window = getWindow();
                        window.setStatusBarColor(vibrantSwatch.getRgb());
                    }
                } else {
                    Log.d(TAG, "getVibrantSwatch() is null");
                    // need to deal with
                }
            }
        });
    }

    private void logUpdateSuccess() {
        Log.d("PersonalInfoActivity", "update success");
    }

    public String getUniquePictureFilename() {
        SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date();
        String picName = "IMG_" + simpleDateFormat.format(date) + ".jpg";
        return picName;
    }

    public File getPhotoFile() {
        File externalFilesDir = getApplicationContext()
                .getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        if (externalFilesDir == null) {
            return null;
        }
        return new File(externalFilesDir, getUniquePictureFilename());
    }

    private void selectImage() {
        final CharSequence[] items = {"拍照", "选择照片"};

        AlertDialog.Builder dialog = new AlertDialog.Builder(PersonalInfoActivity.this);
        dialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("拍照")) {
                    cameraIntent();
                } else if (items[item].equals("选择照片")) {
                    sdCardPermission();
                }
            }
        });
        dialog.show();
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        pic_file = getPhotoFile();
        Log.d("Pic Path:", pic_file.getPath());

        Uri uri = Uri.fromFile(pic_file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    /**
     * the start of choosing picture in gallery
     */
    private void sdCardPermission() {
        if (hasPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            doSDCardPermission();
        } else {
            requestPermission(Constants.WRITE_EXTERNAL_CODE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
    }

    /**
     * the business logic of choosing picture in gallery
     */
    @Override
    public void doSDCardPermission() {
        galleryIntent();
    }

    private void galleryIntent() {
        MultiImageSelector.create()
                .showCamera(false)
                .single()
                .start(PersonalInfoActivity.this, REQUEST_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            pic_file = null;
            return;
        }

        if (requestCode == REQUEST_CAMERA) {
            uploadAndUpdateHeadPortrait(pic_file.getPath());
        } else if (requestCode == REQUEST_GALLERY) {
            List<String> picPath = data
                    .getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);

            if (picPath.size() == 1) {
                loadHeadPortraitByPath(picPath.get(0));
            }
            uploadAndUpdateHeadPortrait(picPath.get(0));
        }
    }
}
