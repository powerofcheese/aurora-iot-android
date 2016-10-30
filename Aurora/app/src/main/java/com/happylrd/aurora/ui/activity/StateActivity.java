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

import com.github.pavlospt.CircleView;
import com.google.gson.Gson;
import com.happylrd.aurora.R;
import com.happylrd.aurora.model.Mode;
import com.happylrd.aurora.model.Motion;
import com.happylrd.aurora.model.MyUser;
import com.happylrd.aurora.model.GestureState;
import com.happylrd.aurora.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;

public class StateActivity extends AppCompatActivity {

    private static final String TAG = "StateActivity";
    public static final String EXTRA_MOTION = "com.happylrd.aurora.motion";
    public Motion mMotion;

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

    public static Intent newIntent(Context packageContext, Motion motion) {
        Intent intent = new Intent(packageContext, StateActivity.class);
        intent.putExtra(EXTRA_MOTION, motion);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_state);

        mMotion = (Motion) getIntent().getSerializableExtra(EXTRA_MOTION);

        if (mMotion == null) {
            initMotion();
        }

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

    /**
     * temporarily place here
     */
    private void initData() {
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

    private void initMotion() {
        mMotion = new Motion();
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
                                    saveMode(modeName);
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


    /**
     * just test for Gson
     */
    private void gestureStateToJson(GestureState gestureState){
        Gson gson = new Gson();
        String json = gson.toJson(gestureState);
        Log.d(TAG, json);
    }


    /**
     * save Mode by modeName
     *
     * @param modeName
     */
    private void saveMode(String modeName) {
        Mode mode = new Mode();
        mode.setModeName(modeName);
        mode.setAuthor(MyUser.getCurrentUser(MyUser.class));

        mode.save(new SaveListener<String>() {
            @Override
            public void done(String objectId, BmobException e) {
                if (e == null) {
                    findModeById(objectId);
                    ToastUtil.showSaveSuccessToast(StateActivity.this);
                } else {
                    ToastUtil.showSaveFailToast(StateActivity.this);
                }
            }
        });
    }

    /**
     * find Mode by objectId and prepare for saving GestureState
     *
     * @param objectId
     */
    private void findModeById(String objectId) {
        BmobQuery<Mode> query = new BmobQuery<Mode>();
        query.getObject(objectId, new QueryListener<Mode>() {
            @Override
            public void done(Mode mode, BmobException e) {
                if (e == null) {
                    saveGestureState(mode);
                } else {
                    Log.d(TAG, "find mode failed");
                }
            }
        });
    }

    /**
     * save GestureState by Mode
     *
     * @param mode
     */
    private void saveGestureState(Mode mode) {

        // temporarily place here
        GestureState gestureState = new GestureState();
        gestureState.setToe(false);
        gestureState.setHeel(false);
        gestureState.setStomp(false);
        gestureState.setKickLow(false);
        gestureState.setKickMid(false);
        gestureState.setKickHigh(true);

        gestureState.setMode(mode);

//        gestureStateToJson(gestureState);

        gestureState.save(new SaveListener<String>() {
            @Override
            public void done(String objectId, BmobException e) {
                if (e == null) {
                    findGestureStateById(objectId);
                } else {
                    Log.d(TAG, "save gestureState failed");
                }
            }
        });
    }

    /**
     * find GestureState by objectId and prepare for saving Motion
     *
     * @param objectId
     */
    private void findGestureStateById(String objectId) {
        BmobQuery<GestureState> queryGestureState = new BmobQuery<GestureState>();
        queryGestureState.getObject(objectId, new QueryListener<GestureState>() {
            @Override
            public void done(GestureState gestureState, BmobException e) {
                if (e == null) {
                    saveMotionByGestureState(gestureState);
                } else {
                    Log.d(TAG, "find gestureState failed");
                }
            }
        });
    }

    /**
     * save Motion by GestureState
     *
     * @param gestureState
     */
    private void saveMotionByGestureState(GestureState gestureState) {
        mMotion.setGestureState(gestureState);
        mMotion.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    // save motion success
                } else {
                    Log.d(TAG, "save motion failed");
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
