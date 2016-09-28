package com.happylrd.aurora;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.happylrd.aurora.entity.MyUser;
import com.happylrd.aurora.entity.WriteSth;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_WRITESTH_ID =
            "com.happylrd.aurora.writesth_id";

    public String WRITESTH_ID;

    private CollapsingToolbarLayout collapsingToolbarLayout;

    private RecyclerView recyclerView;
    private PicAdapter mPicAdapter;

    private TextView tv_nick_name;
    private TextView tv_text_content;
    private TextView tv_date;


    public static Intent newIntent(Context context, String writeSthId){
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
//        collapsingToolbarLayout.setTitle(getString(R.string.item_title));

        recyclerView = (RecyclerView) findViewById(R.id.rv_pics);
        recyclerView.setLayoutManager(new LinearLayoutManager(DetailActivity.this));

        tv_nick_name = (TextView) findViewById(R.id.tv_nick_name);
        tv_text_content = (TextView) findViewById(R.id.tv_text_content);
        tv_date = (TextView) findViewById(R.id.tv_date);

        BmobQuery<WriteSth> query = new BmobQuery<>();
        query.getObject(WRITESTH_ID, new QueryListener<WriteSth>() {
            @Override
            public void done(WriteSth writeSth, BmobException e) {
                if (e == null) {
                    Log.d("TV C is equalsZero", (writeSth.getTextContent().equals("")) + "");
                    tv_date.setText(writeSth.getCreatedAt());
                    if (!writeSth.getTextContent().equals("")) {
                       tv_text_content.setText(writeSth.getTextContent());
                    }

                    mPicAdapter.addAll(writeSth.getPicsPathList());

                    BmobQuery<MyUser> query = new BmobQuery<>();
                    query.getObject(writeSth.getAuthor().getObjectId(),
                            new QueryListener<MyUser>() {
                                @Override
                                public void done(MyUser myUser, BmobException e) {
                                    if (e == null) {
                                        tv_nick_name.setText(myUser.getNickName());
                                        collapsingToolbarLayout.setTitle(tv_nick_name.getText());
                                    } else {
                                        showFindFailedToast();
                                    }
                                }
                            });
                } else {
                    showFindFailedToast();
                }
            }
        });

        mPicAdapter = new PicAdapter();
        recyclerView.setAdapter(mPicAdapter);

    }

    private class PicHolder extends RecyclerView.ViewHolder{

        private String mPicUrl;

        public ImageView iv_pic;

        public PicHolder(View itemView){
            super(itemView);

            iv_pic = (ImageView) itemView.findViewById(R.id.iv_pic);
        }

        public void bindPicUrl(String picUrl){
            mPicUrl = picUrl;

            Log.d("Pic Url ", picUrl);

            Glide.with(DetailActivity.this)
                    .load(picUrl)
                    .into(iv_pic);
        }
    }

    private class PicAdapter extends RecyclerView.Adapter<PicHolder>{

        private List<String> mPicUrlList;

        public PicAdapter(){
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
}
