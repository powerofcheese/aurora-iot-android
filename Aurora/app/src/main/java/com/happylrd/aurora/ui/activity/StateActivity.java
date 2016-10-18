package com.happylrd.aurora.ui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.pavlospt.CircleView;
import com.happylrd.aurora.R;
import com.happylrd.aurora.model.Mode;
import com.happylrd.aurora.model.MyUser;
import com.happylrd.aurora.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class StateActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    private RecyclerView mRecyclerView;
    private CircleAdapter mCircleAdapter;

    private CircleView cv_current_state;
    private CircleView cv_pre_state;

    private FloatingActionButton fab_add_mode;

    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, StateActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_state);

        initView();
        initListener();
        initData();
    }

    private void initView() {
        cv_current_state = (CircleView) findViewById(R.id.cv_current_state);
        cv_pre_state = (CircleView) findViewById(R.id.cv_pre_state);

        fab_add_mode = (FloatingActionButton) findViewById(R.id.fab_add_mode);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("状态选择");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(StateActivity.this));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mCircleAdapter = new CircleAdapter();
        mRecyclerView.setAdapter(mCircleAdapter);
    }

    private void initData() {
        // temporarily placeholder
        List<Mode> tempModeList = new ArrayList<>();

        for (int i = 0; i < 18; i++) {
            Mode mode = new Mode();
            tempModeList.add(mode);
        }

        mCircleAdapter.addAll(tempModeList);
    }

    private void initListener() {
        cv_current_state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ShoesActivity.newIntent(StateActivity.this);
                startActivity(intent);
            }
        });

        cv_pre_state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ShoesActivity.newIntent(StateActivity.this);
                startActivity(intent);
            }
        });

        fab_add_mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addModeLogic();
            }
        });
    }

    private void addModeLogic() {
        showAddModeDialog();
    }

    private void showAddModeDialog() {
        AlertDialog.Builder dialog =
                new AlertDialog.Builder(StateActivity.this);
        LayoutInflater inflater = LayoutInflater.from(StateActivity.this);
        View dialogView = inflater.inflate(R.layout.dialog_et_text, null);

        final EditText editText = (EditText) dialogView.findViewById(R.id.edit_text);
        editText.setSelection(editText.getText().length());

        dialog.setTitle("设置模式名")
                .setView(dialogView)
                .setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (editText.getText().toString().equals("")) {
                                    ToastUtil.showInputNotNullToast(StateActivity.this);
                                } else {
                                    String modeName = editText.getText().toString();
                                    uploadModeName(modeName);
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

    private void uploadModeName(String modeName) {
        Mode mode = new Mode();
        mode.setModeName(modeName);
        mode.setAuthor(MyUser.getCurrentUser(MyUser.class));

        mode.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    Toast.makeText(StateActivity.this, "添加模式成功", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    Toast.makeText(StateActivity.this, "添加模式失败", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });
    }

    private class CircleHolder extends RecyclerView.ViewHolder {

        private Mode mMode;

        private CircleView cv_item_light;
        private CircleView cv_item_sound;

        private Button btn_toe;
        private Button btn_heel;
        private Button btn_stomp;
        private Button btn_kick_low;
        private Button btn_kick_mid;
        private Button btn_kick_high;

        public CircleHolder(View itemView) {
            super(itemView);

            cv_item_light = (CircleView) itemView.findViewById(R.id.cv_item_light);
            cv_item_sound = (CircleView) itemView.findViewById(R.id.cv_item_sound);

            btn_toe = (Button) itemView.findViewById(R.id.btn_toe);
            btn_heel = (Button) itemView.findViewById(R.id.btn_heel);
            btn_stomp = (Button) itemView.findViewById(R.id.btn_stomp);
            btn_kick_low = (Button) itemView.findViewById(R.id.btn_kick_low);
            btn_kick_mid = (Button) itemView.findViewById(R.id.btn_kick_mid);
            btn_kick_high = (Button) itemView.findViewById(R.id.btn_kick_high);

            cv_item_light.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = ShoesActivity.newIntent(StateActivity.this);
                    startActivity(intent);
                }
            });

            cv_item_sound.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = ShoesActivity.newIntent(StateActivity.this);
                    startActivity(intent);
                }
            });

            btn_toe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btn_toe.getBackground().setColorFilter(
                            getResources().getColor(R.color.colorAccent),
                            PorterDuff.Mode.MULTIPLY);
                }
            });

            btn_heel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btn_heel.getBackground().setColorFilter(
                            getResources().getColor(R.color.colorAccent),
                            PorterDuff.Mode.MULTIPLY);
                }
            });

            btn_stomp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btn_stomp.getBackground().setColorFilter(
                            getResources().getColor(R.color.colorAccent),
                            PorterDuff.Mode.MULTIPLY);
                }
            });

            btn_kick_low.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btn_kick_low.getBackground().setColorFilter(
                            getResources().getColor(R.color.colorAccent),
                            PorterDuff.Mode.MULTIPLY);
                }
            });

            btn_kick_mid.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btn_kick_mid.getBackground().setColorFilter(
                            getResources().getColor(R.color.colorAccent),
                            PorterDuff.Mode.MULTIPLY);
                }
            });

            btn_kick_high.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btn_kick_high.getBackground().setColorFilter(
                            getResources().getColor(R.color.colorAccent),
                            PorterDuff.Mode.MULTIPLY);
                }
            });
        }

        public void bindCircle(Mode mode) {
            mMode = mode;
        }
    }

    private class CircleAdapter extends RecyclerView.Adapter<CircleHolder> {

        private List<Mode> mModeList;

        public CircleAdapter() {
            mModeList = new ArrayList<>();
        }

        @Override
        public CircleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(StateActivity.this);
            View view = inflater
                    .inflate(R.layout.item_state_circle, parent, false);
            return new CircleHolder(view);
        }

        @Override
        public void onBindViewHolder(CircleHolder holder, int position) {
            Mode mode = mModeList.get(position);
            holder.bindCircle(mode);
        }

        @Override
        public int getItemCount() {
            return mModeList.size();
        }

        public void add(Mode mode) {
            mModeList.add(mode);
            notifyDataSetChanged();
        }

        public void addAll(List<Mode> modes) {
            mModeList.addAll(modes);
            Log.d("modeList size: ", mModeList.size() + "");
            notifyDataSetChanged();
        }
    }
}
