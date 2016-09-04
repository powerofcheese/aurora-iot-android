package com.happylrd.aurora;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.happylrd.aurora.entity.MyUser;
import com.happylrd.aurora.entity.WriteSth;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadBatchListener;

public class WriteSthActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private EditText et_write_sth;

    private ImageView iv_add_image;

    private final int REQUEST_CAMERA = 0;
    private final int REQUEST_GALLERY = 1;

    private List<File> pics_file_list;
    private List<String> pics_path_list;

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, WriteSthActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_sth);

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        TileAdapter tileAdapter = new TileAdapter();
        recyclerView.setAdapter(tileAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(WriteSthActivity.this, 3));

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("写说说");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        et_write_sth = (EditText) findViewById(R.id.et_write_sth);

        pics_file_list = new ArrayList<>();
        pics_path_list = new ArrayList<>();
    }


    public class TileHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;

        public TileHolder(View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.iv_add_image);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    iv_add_image = imageView;
                    selectImage();
                }
            });
        }
    }

    public class TileAdapter extends RecyclerView.Adapter<TileHolder> {

        @Override
        public TileHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(WriteSthActivity.this);
            View view = layoutInflater
                    .inflate(R.layout.item_tile, parent, false);
            return new TileHolder(view);
        }

        @Override
        public void onBindViewHolder(TileHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return pics_file_list.size() + 1;
        }
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
            cameraLogic(textContent);
        }

        return super.onOptionsItemSelected(item);
    }

    private void cameraLogic(String textContent) {

        if (textContent.equals("") &&
                (pics_file_list.size() == 0 ||
                        pics_file_list.get(pics_file_list.size() - 1) == null)) {
            // set menu no clickable
        }

        if (pics_file_list.size() == 0 ||
                pics_file_list.get(pics_file_list.size() - 1) == null) {
            uploadText(textContent);
        } else if (textContent.equals("")) {
            uploadPics(getPicsPathByFileList(pics_file_list));
        } else {
            uploadTextAndPics(textContent, getPicsPathByFileList(pics_file_list));
        }
    }


    private void galleryLogic(String textContent, List<File> picsFileList) {

        if (textContent.equals("") &&
                (picsFileList == null || picsFileList.size() == 0)) {
            // set menu no clickable
        }

        if (picsFileList == null || picsFileList.size() == 0) {
            uploadText(textContent);
        } else if (textContent.equals("")) {
            uploadPics(getPicsPathByFileList(picsFileList));
        } else {
            uploadTextAndPics(textContent, getPicsPathByFileList(picsFileList));
        }
    }

    private List<String> getPicsPathByFileList(List<File> picsFileList) {
        for (int i = 0; i < picsFileList.size(); i++) {
            pics_path_list.add(picsFileList.get(i).getPath());
        }
        return pics_path_list;
    }

    private void uploadText(String textContent) {
        WriteSth writeSth = new WriteSth();
        writeSth.setTextContent(textContent);
        writeSth.setAuthor(MyUser.getCurrentUser(MyUser.class));

        writeSth.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    Toast.makeText(WriteSthActivity.this, "发表文字成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(WriteSthActivity.this, "发表文字失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void uploadPics(final List<String> picsPathList) {

        BmobFile.uploadBatch(
                picsPathList.toArray(new String[picsPathList.size()]),
                new UploadBatchListener() {
                    @Override
                    public void onSuccess(List<BmobFile> list, List<String> list1) {
                        if (list1.size() == picsPathList.size()) {
                            WriteSth writeSth = new WriteSth();
                            writeSth.setAuthor(MyUser.getCurrentUser(MyUser.class));
                            writeSth.setPicsPathList(list1);

                            writeSth.save(new SaveListener<String>() {
                                @Override
                                public void done(String s, BmobException e) {
                                    if (e == null) {
                                        Toast.makeText(WriteSthActivity.this, "发表图片成功!",
                                                Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(WriteSthActivity.this, "发表图片失败!",
                                                Toast.LENGTH_SHORT).show();
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

                    }
                });
    }

    private void uploadTextAndPics(final String textContent,
                                   final List<String> picsPathList) {
        BmobFile.uploadBatch(
                picsPathList.toArray(new String[picsPathList.size()]),
                new UploadBatchListener() {
                    @Override
                    public void onSuccess(List<BmobFile> list, List<String> list1) {
                        if (list1.size() == picsPathList.size()) {
                            WriteSth writeSth = new WriteSth();
                            writeSth.setTextContent(textContent);
                            writeSth.setAuthor(MyUser.getCurrentUser(MyUser.class));
                            writeSth.setPicsPathList(list1);

                            writeSth.save(new SaveListener<String>() {
                                @Override
                                public void done(String s, BmobException e) {
                                    if (e == null) {

                                        Toast.makeText(WriteSthActivity.this, "发表图片与文字成功!",
                                                Toast.LENGTH_SHORT).show();

                                    } else {
                                        Toast.makeText(WriteSthActivity.this, "发表图片与文字失败!",
                                                Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(WriteSthActivity.this, "pics----onload Failure!" +
                                s, Toast.LENGTH_SHORT).show();
                    }
                });
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
        final CharSequence[] items = {"拍照", "从手机中选择"};

        AlertDialog.Builder dialog = new AlertDialog.Builder(WriteSthActivity.this);
        dialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("拍照")) {
                    cameraIntent();
                } else if (items[item].equals("选择照片")) {
                    galleryIntent();
                }
            }
        });
        dialog.show();
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        pics_file_list.add(getPhotoFile());

        Log.d("Pics Size:", pics_file_list.size() + "");
        Log.d("Pics Path:", pics_file_list
                .get(pics_file_list.size() - 1).getPath());

        Uri uri = Uri.fromFile(pics_file_list
                .get(pics_file_list.size() - 1)
        );

        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void galleryIntent() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(
                Intent.createChooser(intent, "Select picture"), REQUEST_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            pics_file_list.remove(pics_file_list.size() - 1);
//            Log.d("After remove Pics size", pics_file_list.size()+" size");
            return;
        }

        if (requestCode == REQUEST_CAMERA) {
            updateImageItem(pics_file_list
                    .get(pics_file_list.size() - 1)
            );

        } else if (requestCode == REQUEST_GALLERY) {

            // just for demo, need to modify
            Bitmap bm = null;
            if (data != null) {
                try {
                    bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
            iv_add_image.setImageBitmap(bm);
        }
    }

    private void updateImageItem(File pictureFile) {

        if (pictureFile == null || !pictureFile.exists()) {
            Log.d("PictureFile is null?",
                    (pictureFile == null || !pictureFile.exists()) + "");
        } else {
            Log.d("updateImage path", pictureFile.getPath());

            Glide.with(WriteSthActivity.this)
                    .load(pictureFile.getPath())
                    .into(iv_add_image);
        }
    }
}
