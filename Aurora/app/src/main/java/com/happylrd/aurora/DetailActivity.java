package com.happylrd.aurora;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.happylrd.aurora.entity.MyUser;
import com.happylrd.aurora.entity.WriteSth;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import de.hdodenhof.circleimageview.CircleImageView;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_WRITESTH_ID =
            "com.happylrd.aurora.writesth_id";

    public String WRITESTH_ID;

    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Toolbar toolbar;

    private RecyclerView recyclerView;
    private PicAdapter mPicAdapter;

    private CircleImageView civ_head_portrait_bg;
    private CircleImageView civ_head_portrait;
    private TextView tv_nick_name;
    private TextView tv_text_content;

    public static Intent newIntent(Context context, String writeSthId) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(EXTRA_WRITESTH_ID, writeSthId);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        WRITESTH_ID = (String) getIntent().getSerializableExtra(EXTRA_WRITESTH_ID);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        collapsingToolbarLayout =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        collapsingToolbarLayout.setTitle(getString(R.string.item_title));

        recyclerView = (RecyclerView) findViewById(R.id.rv_pics);
        recyclerView.setLayoutManager(new LinearLayoutManager(DetailActivity.this));

        tv_nick_name = (TextView) findViewById(R.id.tv_nick_name);
        tv_text_content = (TextView) findViewById(R.id.tv_text_content);

        civ_head_portrait_bg = (CircleImageView) findViewById(R.id.civ_head_portrait_bg);
        civ_head_portrait = (CircleImageView) findViewById(R.id.civ_head_portrait);

        BmobQuery<WriteSth> query = new BmobQuery<>();
        query.getObject(WRITESTH_ID, new QueryListener<WriteSth>() {
            @Override
            public void done(WriteSth writeSth, BmobException e) {
                if (e == null) {
                    Log.d("TV C is equalsZero", (writeSth.getTextContent().equals("")) + "");
                    if (!writeSth.getTextContent().equals("")) {
                        tv_text_content.setText(writeSth.getTextContent());
                    }

                    Log.d("Pic is null?", (writeSth.getPicsPathList() == null) + "");
                    if (writeSth.getPicsPathList() != null) {
                        mPicAdapter.addAll(writeSth.getPicsPathList());
                    }

                    BmobQuery<MyUser> query = new BmobQuery<>();
                    query.getObject(writeSth.getAuthor().getObjectId(),
                            new QueryListener<MyUser>() {
                                @Override
                                public void done(MyUser myUser, BmobException e) {
                                    if (e == null) {
                                        tv_nick_name.setText(myUser.getNickName());
                                        collapsingToolbarLayout.setTitle(tv_nick_name.getText());
                                        if (myUser.getHeadPortraitPath() != null) {
                                            Glide.with(DetailActivity.this)
                                                    .load(myUser.getHeadPortraitPath())
                                                    .into(civ_head_portrait);
                                            Glide.with(DetailActivity.this)
                                                    .load(myUser.getHeadPortraitPath())
                                                    .into(civ_head_portrait_bg);
                                            setBgColorByHeadPortrait(myUser.getHeadPortraitPath());
                                        } else {
                                            setBgColorByDefaultHeadPortrait();
                                        }
                                    } else {
//                                        showFindFailedToast();
                                        Log.d("Find fail 1 ", e.getMessage());
                                    }
                                }
                            });
                } else {
//                    showFindFailedToast();
                    Log.d("Find fail 2 ", e.getMessage());
                }
            }
        });

        mPicAdapter = new PicAdapter();
        recyclerView.setAdapter(mPicAdapter);

    }

    private class PicHolder extends RecyclerView.ViewHolder {

        private String mPicUrl;

        public ImageView iv_pic;

        public PicHolder(View itemView) {
            super(itemView);

            iv_pic = (ImageView) itemView.findViewById(R.id.iv_pic);
        }

        public void bindPicUrl(String picUrl) {
            mPicUrl = picUrl;

            Log.d("Pic Url ", picUrl);

            Glide.with(DetailActivity.this)
                    .load(picUrl)
                    .into(iv_pic);
        }
    }

    private class PicAdapter extends RecyclerView.Adapter<PicHolder> {

        private List<String> mPicUrlList;

        public PicAdapter() {
            mPicUrlList = new ArrayList<>();
        }

        @Override
        public PicHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(DetailActivity.this);
            View view = inflater
                    .inflate(R.layout.item_pic, parent, false);
            return new PicHolder(view);
        }

        @Override
        public void onBindViewHolder(PicHolder holder, int position) {
            String picUrl = mPicUrlList.get(position);
            holder.bindPicUrl(picUrl);
        }

        @Override
        public int getItemCount() {
            return mPicUrlList.size();
        }

        public void addAll(List<String> picUrlList) {
            mPicUrlList.addAll(picUrlList);
            Log.d("writeSthList size: ", mPicUrlList.size() + "");
            notifyDataSetChanged();
        }
    }

    public void showFindFailedToast() {
        Toast.makeText(DetailActivity.this, "查询失败", Toast.LENGTH_SHORT)
                .show();
    }

    private void setBgColorByDefaultHeadPortrait() {
        BitmapDrawable bitmapDrawable =
                (BitmapDrawable) getResources().getDrawable(R.drawable.profile);
        Bitmap bitmap = bitmapDrawable.getBitmap();
        usePaletteByBitmap(bitmap);
    }

    private void setBgColorByHeadPortrait(String headPortraitPath) {
        Glide.with(DetailActivity.this)
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
                Palette.Swatch vibrant = palette.getVibrantSwatch();
                collapsingToolbarLayout.setBackgroundColor(vibrant.getRgb());
                toolbar.setBackgroundColor(vibrant.getRgb());
                if (Build.VERSION.SDK_INT >= 21) {
                    Window window = getWindow();
                    window.setStatusBarColor(vibrant.getRgb());
                }
            }
        });
    }
}
