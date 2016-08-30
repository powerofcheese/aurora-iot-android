package com.happylrd.aurora;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.happylrd.aurora.entity.MyUser;
import com.happylrd.aurora.entity.Post;
import com.happylrd.aurora.entity.WriteSthContent;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadBatchListener;

public class WriteSthActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText et_write_sth;
    private ImageView image_submit_sth;
    private Button button_takePhoto;

    private final int TAKE_PHOTO = 0;
    private final int CHOOSE_PHOTO = 1;
    private ArrayList<String> paths;

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, WriteSthActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_sth);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("写说说");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        et_write_sth = (EditText) findViewById(R.id.et_write_sth);

        image_submit_sth = (ImageView) findViewById(R.id.image_submit_sth);
        image_submit_sth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.putExtra("crop", true);
                intent.putExtra("scale", true);
                intent.setType("image/*");
                startActivityForResult(intent, CHOOSE_PHOTO);
            }
        });
        button_takePhoto = (Button) findViewById(R.id.button_takePhoto);
        button_takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, TAKE_PHOTO);
            }
        });
        paths = new ArrayList<String>();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_write_sth, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_item_publish) {
            String textContent = et_write_sth.getText().toString();

            sendPost(textContent);
        }

        return super.onOptionsItemSelected(item);
    }

    private void uploadResToCloud(String textContent) {
        WriteSthContent writeSthContent = new WriteSthContent();
        writeSthContent.setTextContent(textContent);
        writeSthContent.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    Toast.makeText(WriteSthActivity.this, "发表成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(WriteSthActivity.this, "发表失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void uploadPicToCloud(String picPath) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PHOTO:
                String sdStatus = Environment.getExternalStorageState();
                if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
                    Log.v("TestFile", "SD card is not avaiable/writeable right now.");
                    return;
                }
                Bundle bundle = data.getExtras();
                Bitmap bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式
                Log.i("bmob", "get Bitmap take photo-----");
                image_submit_sth.setImageBitmap(bitmap);
                FileOutputStream b = null;
                File file = new File("/sdcard/myImage/");
                file.mkdirs();// 创建文件夹，名称为pk4fun // 照片的命名，目标文件夹下，以当前时间数字串为名称，即可确保每张照片名称不相同。网上流传的其他Demo这里的照片名称都写死了，则会发生无论拍照多少张，后一张总会把前一张照片覆盖。细心的同学还可以设置这个字符串，比如加上“ＩＭＧ”字样等；然后就会发现sd卡中myimage这个文件夹下，会保存刚刚调用相机拍出来的照片，照片名称不会重复。
                String str = null;
                Date date = null;
                SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");// 获取当前时间，进一步转化为字符串
                date = new Date();
                str = format.format(date);
                String fileName = "/sdcard/myImage/" + str + ".jpg";
                paths.add(fileName);
                Log.i("bmob", "拍摄中" + fileName);
                try {
                    b = new FileOutputStream(fileName);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        b.flush();
                        b.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case CHOOSE_PHOTO:
                if (resultCode == Activity.RESULT_OK) {
                    Log.i("bmob", "Enter-----");
                    Uri uri = data.getData(); //返回的结果
                    Log.i("bmob", "get URI-----");
                    ContentResolver cr = this.getContentResolver();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
                    if (cursor == null) {
                        return;
                    }
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    paths.add(picturePath);
                    Toast.makeText(getApplicationContext(), picturePath,
                            Toast.LENGTH_SHORT).show();
                    Log.i("bmob", "相册中" + picturePath);
                    if (!picturePath.toLowerCase().endsWith(".jpg") && !picturePath.toLowerCase().endsWith(".jpeg")) {
                        Toast.makeText(getApplicationContext(), "请选择jpg格式的图片",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                    try {
                        Bitmap bitmap1 = BitmapFactory.decodeStream(cr.openInputStream(uri));
                        image_submit_sth.setImageBitmap(bitmap1);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            default:
                break;
        }

    }

    private void sendPost(String content){
        final String c = content;
        Log.i("bmob", "pics----onload  start!");
        if(paths == null || paths.size() == 0){
            final Post post = new Post();
            post.setContent(content);
            post.setAuthor(BmobUser.getCurrentUser(MyUser.class));
            post.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    if (e == null) {
                        Log.i("bmob", "发表说说成功：" + s);
                        Toast.makeText(WriteSthActivity.this, "发表说说成功!" , Toast.LENGTH_SHORT).show();
                    } else {
                        Log.i("bmob", "发表说说失败：" + e.getMessage() + "," + e.getErrorCode());
                        Toast.makeText(WriteSthActivity.this,"发表说说失败!" + e.getMessage()  , Toast.LENGTH_SHORT).show();
                    }
                }
            });
            return;
        }
        String [] strings = new String[paths.size()];
        for (int i = 0; i <paths.size() ; i++) {
            strings[i] = paths.get(i);
        }
        BmobFile.uploadBatch(strings, new UploadBatchListener() {
            @Override
            public void onSuccess(List<BmobFile> list, List<String> list1) {
                if (list1.size() == paths.size()) {
                    Log.i("bmob", "pics----onload completely!");
                    final Post post = new Post();
                    post.setContent(c);
                    post.setAuthor(BmobUser.getCurrentUser(MyUser.class));
                    post.setPost_pics(list1);
                    post.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if (e == null) {
                                Log.i("bmob", "发表说说成功：" + s);
                                Toast.makeText(WriteSthActivity.this, "发表说说成功!" , Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(WriteSthActivity.this,"发表说说失败!" + e.getMessage()  , Toast.LENGTH_SHORT).show();
                                Log.i("bmob", "发表说说失败：" + e.getMessage() + "," + e.getErrorCode());
                            }
                        }
                    });
                }
            }

            @Override
            public void onProgress(int i, int i1, int i2, int i3) {

            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(WriteSthActivity.this, "pics----onload Failure!" + s, Toast.LENGTH_SHORT).show();
                Log.i("bmob", "pics----onload Failure!" + s);
            }
        });
    }
}
