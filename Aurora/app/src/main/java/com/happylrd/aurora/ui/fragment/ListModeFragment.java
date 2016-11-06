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

import com.google.gson.Gson;
import com.happylrd.aurora.R;
import com.happylrd.aurora.model.Mode;
import com.happylrd.aurora.model.Motion;
import com.happylrd.aurora.model.MyUser;
import com.happylrd.aurora.model.NormalState;
import com.happylrd.aurora.todo.ModeView;
import com.happylrd.aurora.util.ToastUtil;
import com.happylrd.aurora.util.ZhToEnMapUtil;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycler_view, container, false);

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

        private Motion mPassMotion;

        private String mPassModeName;

        public ModeHolder(View itemView) {
            super(itemView);

            mPassMotion = new Motion();

            mModeView = (ModeView) itemView.findViewById(R.id.item_mode_view);
            mModeView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // can pass motion json to edison
                    Log.d(TAG, getJsonByMotion());

                    // can pass mode name to edison
                    Log.d(TAG, mPassModeName);
                }
            });
        }

        public void bindMode(final Mode mode) {

            mPassModeName = mode.getModeName();

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
        }

        private String getJsonByMotion() {
            Gson gson = new Gson();
            String json = gson.toJson(mPassMotion);
            return json;
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
