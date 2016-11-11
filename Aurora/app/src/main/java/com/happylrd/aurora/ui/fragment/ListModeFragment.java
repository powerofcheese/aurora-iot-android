package com.happylrd.aurora.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.happylrd.aurora.R;
import com.happylrd.aurora.model.GestureState;
import com.happylrd.aurora.model.Mode;
import com.happylrd.aurora.model.Motion;
import com.happylrd.aurora.model.MyUser;
import com.happylrd.aurora.model.NormalState;
import com.happylrd.aurora.todo.BlueToothCommunication;
import com.happylrd.aurora.todo.ModeView;
import com.happylrd.aurora.util.ToastUtil;
import com.happylrd.aurora.util.ZhToEnMapUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class ListModeFragment extends Fragment {
    private static final String TAG = "ListModeFragment";

    private ZhToEnMapUtil mZhToEnMapUtil;

    private RecyclerView recyclerView;
    private ModeAdapter mModeAdapter;

    private BlueToothCommunication blueToothCommunication;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mode_recycler_view, container, false);

        initView(view);
        initListener();
        initData();

        return view;
    }

    private void initView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private void initListener() {
    }

    private void initData() {
        mModeAdapter = new ModeAdapter();
        recyclerView.setAdapter(mModeAdapter);

        initModeData();

        mZhToEnMapUtil = new ZhToEnMapUtil();
        mZhToEnMapUtil.initMap(getActivity());

        blueToothCommunication = new BlueToothCommunication();
        if (blueToothCommunication.mService != null) {
            blueToothCommunication.write("step");
        }
    }

    private void initModeData() {
        BmobQuery<Mode> query = new BmobQuery<>();
        query.order("createdAt");
        MyUser currentUser = MyUser.getCurrentUser(MyUser.class);
        query.addWhereEqualTo("author", new BmobPointer(currentUser));

        query.findObjects(new FindListener<Mode>() {
            @Override
            public void done(List<Mode> list, BmobException e) {
                if (e == null) {
                    Log.d("Find is null?", (list == null) + "");
                    Log.d("Find size ", list.size() + "");

                    mModeAdapter.addAll(list);
                } else {
                    ToastUtil.showFindFailedToast(getActivity());
                }
            }
        });
    }

    private class ModeHolder extends RecyclerView.ViewHolder {
        // need to be tested and normalized
        private ModeView mModeView;

        private Mode mPassMode;
        private Motion mPassMotion;
        private List<GestureState> mGestureStateList;

        // just a toy
        private Motion mGestureMotion;

        public ModeHolder(View itemView) {
            super(itemView);

            mPassMotion = new Motion();

            mModeView = (ModeView) itemView.findViewById(R.id.item_mode_view);
            mModeView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (blueToothCommunication.mService != null) {

                        if (isPreDefineForUser(mPassMode.getModeName())) {
                            blueToothCommunication.write(getPassJson());
                        } else {
                            blueToothCommunication.write(getPassJson());
                        }
                    }

                    // can pass mode name to edison
                    Log.d(TAG, mPassMode.getModeName());

                    // can pass data to edison
                    Log.d(TAG, getPassJson());
                }
            });
        }

        public void bindMode(final Mode mode) {

            mPassMode = mode;

            BmobQuery<NormalState> query = new BmobQuery<>();
            query.addWhereEqualTo("mode", mode);

            query.findObjects(new FindListener<NormalState>() {
                @Override
                public void done(List<NormalState> list, BmobException e) {
                    if (e == null) {
                        BmobQuery<Motion> queryMotion = new BmobQuery<Motion>();
                        queryMotion.addWhereEqualTo("normalState", list.get(0));

                        queryMotion.findObjects(new FindListener<Motion>() {
                            @Override
                            public void done(List<Motion> list, BmobException e) {
                                if (e == null) {
                                    Log.d("FindMotion ", "success");

                                    Motion tempMotion = list.get(0);

                                    mModeView.setColorAndName(
                                            tempMotion.getIntColorList(),
                                            mode.getModeName(),
                                            mZhToEnMapUtil.getEnValueByZhKey(tempMotion.getPatternName())
                                    );

                                    mPassMotion = list.get(0);
                                } else {
                                    Log.d("FindMotionStateFailed ", e.getMessage());
                                }
                            }
                        });
                    } else {
                        Log.d("FindNormalStateFailed ", e.getMessage());
                    }
                }
            });

            findGestureStateByMode(mode);
        }

        /**
         * a method that query gestureState and set mGestureStateList
         *
         * @param mode
         */
        private void findGestureStateByMode(Mode mode) {
            BmobQuery<GestureState> query = new BmobQuery<>();
            query.addWhereEqualTo("mode", new BmobPointer(mode));
            query.findObjects(new FindListener<GestureState>() {
                @Override
                public void done(List<GestureState> list, BmobException e) {
                    if (e == null) {
                        mGestureStateList = list;

                        findGestureMotionByState(list.get(0));
                    } else {
                        Log.d(TAG, "find gestureState failed");
                    }
                }
            });
        }

        private void findGestureMotionByState(GestureState gestureState){
            BmobQuery<Motion> query = new BmobQuery<>();
            query.addWhereEqualTo("gestureState", gestureState);
            query.findObjects(new FindListener<Motion>() {
                @Override
                public void done(List<Motion> list, BmobException e) {
                    if(e == null){
                        mGestureMotion = list.get(0);
                    }else{
                        Log.d(TAG, "find GestureState failed");
                    }
                }
            });

        }

        /**
         * can pass the full json data about mode
         *
         * @return
         */
        private String getPassJson() {
            String jsonStr = "";
            JSONObject jsonModeObj = new JSONObject();

            try {
                jsonModeObj.put("modeName", mPassMode.getModeName());

                JSONObject jsonStateObj = new JSONObject();

                JSONObject jsonNormalStateObj = new JSONObject();
                JSONObject jsonMotionObj = getJsonObjByMotion();
                jsonNormalStateObj.put("motion", jsonMotionObj);

                Log.d("GestureStateList ", mGestureStateList.size() + "");

                JSONArray jsonGestureStateList = new JSONArray();

                for (int i = 0; i < mGestureStateList.size(); i++) {
                    JSONObject jsonGestureStateObj = new JSONObject();

                    jsonGestureStateObj.put("isToe", mGestureStateList.get(0).getToe());
                    jsonGestureStateObj.put("isHeel", mGestureStateList.get(0).getHeel());
                    jsonGestureStateObj.put("isStomp", mGestureStateList.get(0).getStomp());
                    jsonGestureStateObj.put("isKickLow", mGestureStateList.get(0).getKickLow());
                    jsonGestureStateObj.put("isKickMid", mGestureStateList.get(0).getKickMid());
                    jsonGestureStateObj.put("isKickHigh", mGestureStateList.get(0).getKickHigh());

                    JSONObject jsonGestureMotionObj = getJsonObjByGestureMotion();
                    jsonGestureStateObj.put("motion", jsonGestureMotionObj);

                    jsonGestureStateList.put(jsonGestureStateObj);
                }

                jsonStateObj.put("normalState", jsonNormalStateObj);
                jsonStateObj.put("gestureStateList", jsonGestureStateList);

                jsonModeObj.put("state", jsonStateObj);

                jsonStr = jsonModeObj.toString();

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return jsonStr;
        }

        private JSONObject getJsonObjByMotion() {
            JSONObject jsonMotionObj = new JSONObject();
            try {
                jsonMotionObj.put("patternName", mZhToEnMapUtil.getEnValueByZhKey(
                        mPassMotion.getPatternName())
                );
                jsonMotionObj.put("animationName", mZhToEnMapUtil.getEnValueByZhKey(
                        mPassMotion.getAnimationName())
                );
                jsonMotionObj.put("rotationName", mZhToEnMapUtil.getEnValueByZhKey(
                        mPassMotion.getRotationName())
                );
                jsonMotionObj.put("actionName", mZhToEnMapUtil.getEnValueByZhKey(
                        mPassMotion.getActionName())
                );

                String colorListStr = getDirtyColorListStr();
                jsonMotionObj.put("intColorList", colorListStr);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return jsonMotionObj;
        }


        private JSONObject getJsonObjByGestureMotion() {
            JSONObject jsonMotionObj = new JSONObject();
            try {
                jsonMotionObj.put("patternName", mZhToEnMapUtil.getEnValueByZhKey(
                        mGestureMotion.getPatternName())
                );
                jsonMotionObj.put("animationName", mZhToEnMapUtil.getEnValueByZhKey(
                        mGestureMotion.getAnimationName())
                );
                jsonMotionObj.put("rotationName", mZhToEnMapUtil.getEnValueByZhKey(
                        mGestureMotion.getRotationName())
                );
                jsonMotionObj.put("actionName", mZhToEnMapUtil.getEnValueByZhKey(
                        mGestureMotion.getActionName())
                );

                String colorListStr = getDirtyColorStrForGesture();
                jsonMotionObj.put("intColorList", colorListStr);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return jsonMotionObj;
        }

        /**
         * a dirty method due to some compromises
         *
         * @return
         */
        private String getDirtyColorListStr() {
            List<String> strColors = new ArrayList<>();

            List<Integer> intColors = mPassMotion.getIntColorList();
            for (int i = 0; i < intColors.size(); i++) {
                String argbHexStr = String.format("#%08X", intColors.get(i));
                strColors.add(argbHexStr);
            }

            StringBuffer stringBuffer = new StringBuffer();
            for (int i = 0; i < strColors.size(); i++) {
                stringBuffer.append(strColors.get(i));
                if (i != strColors.size() - 1) {
                    stringBuffer.append(" ");
                }
            }

            return stringBuffer.toString();
        }

        private String getDirtyColorStrForGesture() {
            List<String> strColors = new ArrayList<>();

            List<Integer> intColors = mGestureMotion.getIntColorList();
            for (int i = 0; i < intColors.size(); i++) {
                String argbHexStr = String.format("#%08X", intColors.get(i));
                strColors.add(argbHexStr);
            }

            StringBuffer stringBuffer = new StringBuffer();
            for (int i = 0; i < strColors.size(); i++) {
                stringBuffer.append(strColors.get(i));
                if (i != strColors.size() - 1) {
                    stringBuffer.append(" ");
                }
            }

            return stringBuffer.toString();
        }

        /**
         * need to be modified
         * @param s
         * @return
         */
        private boolean isPreDefineForUser(String s) {
            boolean result = false;
            if (s.equals(getString(R.string.default_mode_1)) ||
                    s.equals(getString(R.string.default_mode_2)) ||
                    s.equals(getString(R.string.default_mode_3)) ||
                    s.equals(getString(R.string.default_mode_4)) ||
                    s.equals(getString(R.string.default_mode_5)) ||
                    s.equals(getString(R.string.default_mode_6)) ||
                    s.equals(getString(R.string.default_mode_7))) {
                result = true;
            }

            Log.d("isPredefineMode ", result + "");
            return result;
        }
    }

    private class ModeAdapter extends RecyclerView.Adapter<ModeHolder> {

        private List<Mode> mModeList;

        public ModeAdapter() {
            mModeList = new ArrayList<>();
        }

        public ModeAdapter(List<Mode> modeList) {
            mModeList = modeList;
        }

        @Override
        public ModeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater
                    .inflate(R.layout.item_mode_2, parent, false);

            return new ModeHolder(view);
        }

        @Override
        public void onBindViewHolder(ModeHolder holder, int position) {
            Mode mode = mModeList.get(position);
            holder.bindMode(mode);
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
            Log.d("modeList size ", mModeList.size() + "");
            notifyDataSetChanged();
        }
    }
}
