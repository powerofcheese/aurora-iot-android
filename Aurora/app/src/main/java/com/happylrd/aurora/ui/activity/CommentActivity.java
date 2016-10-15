package com.happylrd.aurora.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.happylrd.aurora.R;
import com.happylrd.aurora.model.Comment;
import com.happylrd.aurora.model.MyUser;
import com.happylrd.aurora.model.WriteSth;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import de.hdodenhof.circleimageview.CircleImageView;

public class CommentActivity extends AppCompatActivity {

    public static final String EXTRA_WRITESTH_ID =
            "com.happylrd.aurora.writesth_id";
    public String WRITESTH_ID;

    private RecyclerView recyclerView;
    private CommentAdapter mCommentAdapter;

    private Toolbar toolbar;
    private EditText et_write_comment;
    private ImageButton ibtn_send_comment;


    public static Intent newIntent(Context packageContext, String writeSthId) {
        Intent intent = new Intent(packageContext, CommentActivity.class);
        intent.putExtra(EXTRA_WRITESTH_ID, writeSthId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        WRITESTH_ID = (String) getIntent().getSerializableExtra(EXTRA_WRITESTH_ID);

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        mCommentAdapter = new CommentAdapter();
        recyclerView.setAdapter(mCommentAdapter);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("评论");
        setSupportActionBar(toolbar);

        et_write_comment = (EditText) findViewById(R.id.et_write_comment);

        ibtn_send_comment = (ImageButton) findViewById(R.id.ibtn_send_comment);
        ibtn_send_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!et_write_comment.getText().toString().equals("")) {
                    uploadComment(et_write_comment.getText().toString());
                } else {
                    Toast.makeText(CommentActivity.this, "请至少说点什么", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });

        initData();
    }

    private class CommentHolder extends RecyclerView.ViewHolder {
        private Comment mComment;

        public TextView tv_nick_name;
        public CircleImageView civ_head_portrait;
        public TextView tv_text_content;
        public TextView tv_comment_date;

        public CommentHolder(View itemView) {
            super(itemView);

            tv_nick_name = (TextView) itemView.findViewById(R.id.tv_nick_name);
            civ_head_portrait = (CircleImageView) itemView.findViewById(R.id.civ_head_portrait);
            tv_text_content = (TextView) itemView.findViewById(R.id.tv_text_content);
            tv_comment_date = (TextView) itemView.findViewById(R.id.tv_comment_date);
        }

        public void bindComment(Comment comment) {
            mComment = comment;
            tv_comment_date.setText(mComment.getCreatedAt());

            BmobQuery<MyUser> query = new BmobQuery<>();
            query.getObject(mComment.getUser().getObjectId(),
                    new QueryListener<MyUser>() {
                        @Override
                        public void done(MyUser myUser, BmobException e) {
                            if (e == null) {
                                tv_nick_name.setText(myUser.getNickName());
                                if (myUser.getHeadPortraitPath() != null) {
                                    Glide.with(CommentActivity.this)
                                            .load(myUser.getHeadPortraitPath())
                                            .into(civ_head_portrait);
                                }
                            } else {
                                Log.d("Find State1 failed", e.getMessage());
                            }
                        }
                    });

            if (mComment.getTextContent() != null) {
                tv_text_content.setText(mComment.getTextContent());
            }
        }
    }

    private class CommentAdapter extends RecyclerView.Adapter<CommentHolder> {

        private List<Comment> mCommentList;

        public CommentAdapter() {
            mCommentList = new ArrayList<>();
        }

        @Override
        public CommentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(CommentActivity.this);
            View view =
                    inflater.inflate(R.layout.item_comment, parent, false);
            return new CommentHolder(view);
        }

        @Override
        public void onBindViewHolder(CommentHolder holder, int position) {
            Comment comment = mCommentList.get(position);
            holder.bindComment(comment);
        }

        @Override
        public int getItemCount() {
            return mCommentList.size();
        }

        public void add(Comment comment) {
            mCommentList.add(comment);
            notifyDataSetChanged();
        }

        public void addAll(List<Comment> comments) {
            mCommentList.addAll(comments);
            notifyDataSetChanged();
        }

        public void addAllInFront(List<Comment> comments) {
            mCommentList.addAll(0, comments);
            notifyDataSetChanged();
        }
    }

    private void initData() {
        BmobQuery<Comment> query = new BmobQuery<>();
        query.order("-createdAt");
        query.findObjects(new FindListener<Comment>() {
            @Override
            public void done(List<Comment> list, BmobException e) {
                if (e == null) {
                    Log.d("Find is null?", (list == null) + "");
                    Log.d("Find state", "find success" + list.size());

                    mCommentAdapter.addAll(list);
                } else {
                    Log.d("Find State2 failed ", e.getMessage());
                }
            }
        });
    }

    private void uploadComment(final String textContent) {
        BmobQuery<WriteSth> query = new BmobQuery<>();
        query.getObject(WRITESTH_ID, new QueryListener<WriteSth>() {
            @Override
            public void done(WriteSth writeSth, BmobException e) {
                if (e == null) {
                    Comment comment = new Comment();
                    comment.setTextContent(textContent);
                    comment.setUser(MyUser.getCurrentUser(MyUser.class));
                    comment.setWriteSth(writeSth);

                    comment.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if (e == null) {
                                Toast.makeText(CommentActivity.this, "Comment succeed", Toast.LENGTH_SHORT)
                                        .show();
                            } else {
                                Log.d("Comment Error", e.getMessage());
                            }
                        }
                    });
                }
            }
        });
    }
}
