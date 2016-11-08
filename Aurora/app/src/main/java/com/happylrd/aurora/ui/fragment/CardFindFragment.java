package com.happylrd.aurora.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.happylrd.aurora.R;
import com.happylrd.aurora.model.MyUser;
import com.happylrd.aurora.model.WriteSth;
import com.happylrd.aurora.ui.activity.CommentActivity;
import com.happylrd.aurora.ui.activity.DetailActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;
import de.hdodenhof.circleimageview.CircleImageView;

public class CardFindFragment extends Fragment {
    public static final String PRAISE_COLUMN_NAME = "praise";

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private WriteSthAdapter mWriteSthAdapter;

    public String prevEndTime;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.recycler_view, container, false);

        initView(view);
        initListener();

        mWriteSthAdapter = new WriteSthAdapter();
        recyclerView.setAdapter(mWriteSthAdapter);
        initData();

        prevEndTime = new String();
        return view;
    }

    private void initView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        swipeRefreshLayout.setDistanceToTriggerSync(400);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        swipeRefreshLayout.setSize(SwipeRefreshLayout.LARGE);
    }

    private void initListener() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                refreshData();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private class WriteSthHolder extends RecyclerView.ViewHolder {
        private WriteSth mWriteSth;

        private TextView tv_nick_name;
        private CircleImageView civ_head_portrait;
        private TextView tv_text_content;
        private ImageButton ibtn_praise;
        private ImageButton ibtn_comment;

        private RecyclerView rv_pics;
        private PicAdapter mPicAdapter;

//        private ShineButton sbtn_praise;

        public WriteSthHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("WriteSth Id ", mWriteSth.getObjectId() + "");
                    Intent intent = DetailActivity.newIntent(getActivity(), mWriteSth.getObjectId());
                    startActivity(intent);
                }
            });

            tv_nick_name = (TextView) itemView.findViewById(R.id.tv_nick_name);
            civ_head_portrait = (CircleImageView) itemView.findViewById(R.id.civ_head_portrait);
            tv_text_content = (TextView) itemView.findViewById(R.id.tv_text_content);

            rv_pics = (RecyclerView) itemView.findViewById(R.id.rv_pics);
            rv_pics.setLayoutManager(new GridLayoutManager(getActivity(), 3));
            mPicAdapter = new PicAdapter();
            rv_pics.setAdapter(mPicAdapter);

            ibtn_praise = (ImageButton) itemView.findViewById(R.id.ibtn_praise);
            ibtn_comment = (ImageButton) itemView.findViewById(R.id.ibtn_comment);

//            sbtn_praise = (ShineButton) itemView.findViewById(R.id.sbtn_praise);
//            sbtn_praise.init(getActivity());

            ibtn_praise.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickPraiseLogic();
                }
            });

            ibtn_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("IdForComment ", mWriteSth.getObjectId() + "");
                    Intent intent = CommentActivity.newIntent(getActivity(), mWriteSth.getObjectId());
                    startActivity(intent);
                }
            });
        }

        public void bindWriteSth(WriteSth writeSth) {
            mWriteSth = writeSth;

            BmobQuery<MyUser> query = new BmobQuery<>();
            query.getObject(mWriteSth.getAuthor().getObjectId(),
                    new QueryListener<MyUser>() {
                        @Override
                        public void done(MyUser myUser, BmobException e) {
                            if (e == null) {
                                tv_nick_name.setText(myUser.getNickName());
                                if (myUser.getHeadPortraitPath() != null) {
                                    Glide.with(getActivity())
                                            .load(myUser.getHeadPortraitPath())
                                            .into(civ_head_portrait);
                                }
                            } else {
                                showFindFailedToast();
                            }
                        }
                    });

            if (mWriteSth.getTextContent() != null) {
                tv_text_content.setText(mWriteSth.getTextContent());
            }

            Log.d("Pic is null?", (writeSth.getPicsPathList() == null) + "");
            if (mWriteSth.getPicsPathList() != null) {
                mPicAdapter.addAll(writeSth.getPicsPathList());
            }

            // need to set praise view
        }

        public void clickPraiseLogic() {
            BmobQuery<MyUser> query = new BmobQuery<>();
            query.addWhereRelatedTo(PRAISE_COLUMN_NAME, new BmobPointer(mWriteSth));
            Log.d("CurrentId: ", BmobUser.getCurrentUser(MyUser.class).getObjectId());

            query.findObjects(new FindListener<MyUser>() {
                @Override
                public void done(List<MyUser> list, BmobException e) {
                    if (e == null) {
                        Log.d("Query List size", list.size() + "");

                        Boolean isPraise = false;
                        for (int i = 0; i < list.size(); i++) {
                            Log.d("List str", list.get(i).getObjectId());

                            if (list.get(i).getObjectId().equals(BmobUser.getCurrentUser(MyUser.class).getObjectId())) {
                                isPraise = true;
                                break;
                            }
                        }
                        if (!isPraise) {
                            clickPraise();
                        } else {
                            cancelPraise();
                        }
                    } else {
                        showFindFailedToast();
                    }
                }
            });
        }

        public void clickPraise() {
            MyUser myUser = BmobUser.getCurrentUser(MyUser.class);
            BmobRelation relation = new BmobRelation();
            relation.add(myUser);
            mWriteSth.setPraise(relation);
            mWriteSth.update(new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        ibtn_praise.setColorFilter(getResources().getColor(R.color.colorAccent));
                        Log.d("Click Praise", "点赞成功");
                    } else {
                        Toast.makeText(getActivity(), "点赞失败", Toast.LENGTH_SHORT)
                                .show();
                    }
                }
            });
        }

        public void cancelPraise() {
            MyUser myUser = BmobUser.getCurrentUser(MyUser.class);
            BmobRelation relation = new BmobRelation();
            relation.remove(myUser);
            mWriteSth.setPraise(relation);
            mWriteSth.update(new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        ibtn_praise.setColorFilter(getResources().getColor(R.color.grey_image_button));
                        Log.d("Cancel Praise", "取消赞成功");
                    } else {
                        Toast.makeText(getActivity(), "取消赞失败", Toast.LENGTH_SHORT)
                                .show();
                    }
                }
            });
        }

        // it works randomly, but i don't know why.
        public void queryCurrentUserPraise() {
            BmobQuery<MyUser> query = new BmobQuery<>();
            query.addWhereRelatedTo(PRAISE_COLUMN_NAME, new BmobPointer(mWriteSth));
            Log.d("CurrentId: ", BmobUser.getCurrentUser(MyUser.class).getObjectId());

            query.findObjects(new FindListener<MyUser>() {
                @Override
                public void done(List<MyUser> list, BmobException e) {
                    if (e == null) {
                        Log.d("Query List size", list.size() + "");
                        for (int i = 0; i < list.size(); i++) {
                            Log.d("List str", list.get(i).getObjectId());
                            if (list.get(i).getObjectId().equals(BmobUser.getCurrentUser(MyUser.class).getObjectId())) {
                                ibtn_praise.setColorFilter(getResources().getColor(R.color.colorAccent));
                                break;
                            }
                        }
                    } else {
                        showFindFailedToast();
                    }
                }
            });
        }
    }

    private class WriteSthAdapter extends RecyclerView.Adapter<WriteSthHolder> {

        private List<WriteSth> mWriteSthList;

        public WriteSthAdapter() {
            mWriteSthList = new ArrayList<>();
        }

        @Override
        public WriteSthHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater
                    .inflate(R.layout.item_card, parent, false);
            return new WriteSthHolder(view);
        }

        @Override
        public void onBindViewHolder(WriteSthHolder holder, int position) {
            WriteSth writeSth = mWriteSthList.get(position);
            holder.bindWriteSth(writeSth);
        }

        @Override
        public int getItemCount() {
            return mWriteSthList.size();
        }

        public void add(WriteSth writeSth) {
            mWriteSthList.add(writeSth);
            notifyDataSetChanged();
        }

        public void addAll(List<WriteSth> writeSths) {
            mWriteSthList.addAll(writeSths);
            Log.d("writeSthList size: ", mWriteSthList.size() + "");
            notifyDataSetChanged();
        }

        public void addAllInFront(List<WriteSth> writeSths) {
            mWriteSthList.addAll(0, writeSths);
            notifyDataSetChanged();
        }
    }

    private class PicHolder extends RecyclerView.ViewHolder {
        private String mPicUrl;
        private ImageView iv_pic;

        public PicHolder(View itemView) {
            super(itemView);

            iv_pic = (ImageView) itemView.findViewById(R.id.iv_pic);
        }

        public void bindPicUrl(String picUrl) {
            mPicUrl = picUrl;
            Log.d("Pic Url ", picUrl);

            Glide.with(getActivity())
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
            LayoutInflater inflater = LayoutInflater.from(getActivity());
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

    private void initData() {
        BmobQuery<WriteSth> query = new BmobQuery<>();
        query.order("-createdAt");
        query.findObjects(new FindListener<WriteSth>() {
            @Override
            public void done(List<WriteSth> list, BmobException e) {
                if (e == null) {
                    Log.d("Find is null?", (list == null) + "");
                    Log.d("Find state", "find success" + list.size());

                    prevEndTime = list.get(0).getCreatedAt();
                    Log.d("prevEndTime: ", list.get(0).getCreatedAt());

                    mWriteSthAdapter.addAll(list);
                } else {
                    showFindFailedToast();
                }
            }
        });
    }

    private void refreshData() {
        BmobQuery<WriteSth> query = new BmobQuery<>();
        String start = prevEndTime;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = sdf.parse(start);
            date.setTime(date.getTime() + 1000);
        } catch (ParseException pe) {
            pe.printStackTrace();
        }
        query.addWhereGreaterThan("createdAt", new BmobDate(date));
        query.order("-createdAt");
        query.findObjects(new FindListener<WriteSth>() {
            @Override
            public void done(List<WriteSth> list, BmobException e) {
                if (e == null) {
                    Log.d("refreshCreated", list.get(0).getCreatedAt());
                    if (list.size() > 0) {
                        prevEndTime = list.get(0).getCreatedAt();
                        mWriteSthAdapter.addAllInFront(list);
                    }
                } else {
                    Toast.makeText(getActivity(), "已是最新内容", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });
    }

    public void showFindFailedToast() {
        Toast.makeText(getActivity(), "查询失败", Toast.LENGTH_SHORT)
                .show();
    }

    // it's useless temporarily
    private void initRefreshProgress() {
        if (!swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    swipeRefreshLayout.setRefreshing(true);
                    initData();
                    if (swipeRefreshLayout.isRefreshing()) {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }
            });
        } else {
            initData();
        }
    }
}
