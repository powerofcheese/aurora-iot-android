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
import com.happylrd.aurora.R;
import com.happylrd.aurora.model.Mode;
import com.happylrd.aurora.model.Motion;
import com.happylrd.aurora.model.MyUser;
import com.happylrd.aurora.model.GestureState;
import com.happylrd.aurora.model.NormalState;
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
    public NormalState mNormalState;

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
        List<GestureState> tempGestureStateList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            GestureState gestureState = new GestureState();
            gestureState.setToe(false);
            gestureState.setHeel(false);
            gestureState.setStomp(false);
            gestureState.setKickLow(false);
            gestureState.setKickMid(false);
            gestureState.setKickHigh(false);

            tempGestureStateList.add(gestureState);
        }
        mCircleAdapter.addAll(tempGestureStateList);
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
     * find Mode by objectId and prepare for saving NormalState
     * and many GestureState
     *
     * @param objectId
     */
    private void findModeById(String objectId) {
        BmobQuery<Mode> query = new BmobQuery<Mode>();
        query.getObject(objectId, new QueryListener<Mode>() {
            @Override
            public void done(Mode mode, BmobException e) {
                if (e == null) {
                    saveNormalState(mode);

                    saveManyGestureState(mode);
                } else {
                    Log.d(TAG, "find mode failed");
                }
            }
        });
    }

    /**
     * save NormalState by Mode
     *
     * @param mode
     */
    private void saveNormalState(Mode mode) {
        mNormalState = new NormalState();
        mNormalState.setMode(mode);
        mNormalState.save(new SaveListener<String>() {
            @Override
            public void done(String objectId, BmobException e) {
                if (e == null) {
                    findNormalStateById(objectId);
                } else {
                    Log.d(TAG, "save normalState failed");
                }
            }
        });
    }

    /**
     * save many GestureState by calling saveGestureState method for each item
     *
     * @param mode
     */
    private void saveManyGestureState(Mode mode) {
        List<GestureState> gestureStateList = mCircleAdapter.getGestureStateList();
        for (int i = 0; i < gestureStateList.size(); i++) {
            saveGestureState(mode, gestureStateList.get(i));
        }
    }

    /**
     * actually save GestureState item
     *
     * @param mode
     * @param gestureState
     */
    private void saveGestureState(Mode mode, GestureState gestureState) {
        gestureState.setMode(mode);

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
     * find NormalState by objectId and prepare for saving Motion
     *
     * @param objectId
     */
    private void findNormalStateById(String objectId) {
        BmobQuery<NormalState> queryNormalState = new BmobQuery<NormalState>();
        queryNormalState.getObject(objectId, new QueryListener<NormalState>() {
            @Override
            public void done(NormalState normalState, BmobException e) {
                if (e == null) {
                    saveMotionByNormalState(normalState);
                } else {
                    Log.d(TAG, "find normalState failed");
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
     * save Motion by NormalState
     *
     * @param normalState
     */
    private void saveMotionByNormalState(NormalState normalState) {
        mMotion.setNormalState(normalState);

        mMotion.setGestureState(null);

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

    /**
     * save Motion by GestureState
     *
     * @param gestureState
     */
    private void saveMotionByGestureState(GestureState gestureState) {
        mMotion.setGestureState(gestureState);

        mMotion.setNormalState(null);

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

        private GestureState mGestureState;

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
                    Log.d("Toe is null?", (mGestureState.getToe() == null) + "");

                    if (!mGestureState.getToe()) {
                        btn_toe.getBackground().setColorFilter(
                                getResources().getColor(R.color.colorAccent),
                                PorterDuff.Mode.MULTIPLY);
                        mGestureState.setToe(true);
                    } else {
                        btn_toe.getBackground().setColorFilter(
                                getResources().getColor(R.color.white),
                                PorterDuff.Mode.MULTIPLY);
                        mGestureState.setToe(false);
                    }
                }
            });

            btn_heel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!mGestureState.getHeel()) {
                        btn_heel.getBackground().setColorFilter(
                                getResources().getColor(R.color.colorAccent),
                                PorterDuff.Mode.MULTIPLY);
                        mGestureState.setHeel(true);
                    } else {
                        btn_heel.getBackground().setColorFilter(
                                getResources().getColor(R.color.white),
                                PorterDuff.Mode.MULTIPLY);
                        mGestureState.setHeel(false);
                    }
                }
            });

            btn_stomp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!mGestureState.getStomp()) {
                        btn_stomp.getBackground().setColorFilter(
                                getResources().getColor(R.color.colorAccent),
                                PorterDuff.Mode.MULTIPLY);
                        mGestureState.setStomp(true);
                    } else {
                        btn_stomp.getBackground().setColorFilter(
                                getResources().getColor(R.color.white),
                                PorterDuff.Mode.MULTIPLY);
                        mGestureState.setStomp(false);
                    }
                }
            });

            btn_kick_low.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!mGestureState.getKickLow()) {
                        btn_kick_low.getBackground().setColorFilter(
                                getResources().getColor(R.color.colorAccent),
                                PorterDuff.Mode.MULTIPLY);
                        mGestureState.setKickLow(true);
                    } else {
                        btn_kick_low.getBackground().setColorFilter(
                                getResources().getColor(R.color.white),
                                PorterDuff.Mode.MULTIPLY);
                        mGestureState.setKickLow(false);
                    }
                }
            });

            btn_kick_mid.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!mGestureState.getKickMid()) {
                        btn_kick_mid.getBackground().setColorFilter(
                                getResources().getColor(R.color.colorAccent),
                                PorterDuff.Mode.MULTIPLY);
                        mGestureState.setKickMid(true);
                    } else {
                        btn_kick_mid.getBackground().setColorFilter(
                                getResources().getColor(R.color.white),
                                PorterDuff.Mode.MULTIPLY);
                        mGestureState.setKickMid(false);
                    }
                }
            });

            btn_kick_high.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!mGestureState.getKickHigh()) {
                        btn_kick_high.getBackground().setColorFilter(
                                getResources().getColor(R.color.colorAccent),
                                PorterDuff.Mode.MULTIPLY);
                        mGestureState.setKickHigh(true);
                    } else {
                        btn_kick_high.getBackground().setColorFilter(
                                getResources().getColor(R.color.white),
                                PorterDuff.Mode.MULTIPLY);
                        mGestureState.setKickHigh(false);
                    }
                }
            });
        }

        public void bindCircle(GestureState gestureState) {
            mGestureState = gestureState;
        }
    }

    private class CircleAdapter extends RecyclerView.Adapter<CircleHolder> {

        private List<GestureState> mGestureStateList;

        public List<GestureState> getGestureStateList() {
            return mGestureStateList;
        }

        public CircleAdapter() {
            mGestureStateList = new ArrayList<>();
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
            GestureState gestureState = mGestureStateList.get(position);
            holder.bindCircle(gestureState);
        }

        @Override
        public int getItemCount() {
            return mGestureStateList.size();
        }

        public void add(GestureState gestureState) {
            mGestureStateList.add(gestureState);
            notifyDataSetChanged();
        }

        public void addAll(List<GestureState> gestureStates) {
            mGestureStateList.addAll(gestureStates);
            Log.d("gestureStateList: ", mGestureStateList.size() + "");
            notifyDataSetChanged();
        }
    }
}
