package com.happylrd.aurora.ui.fragment;

import android.graphics.Color;
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
import com.happylrd.aurora.model.Mode;
import com.happylrd.aurora.model.MyUser;
import com.happylrd.aurora.todo.ModeView;
import com.happylrd.aurora.util.ToastUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class ListModeFragment extends Fragment {

    private RecyclerView recyclerView;
    private ModeAdapter mModeAdapter;

    private List<Integer> modePicResIdList;

    private String[] mPlaceHoldModeNames =
            {"wave", "ball", "star", "go", "fire", "eye", "charse"};

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
        modePicResIdList = Arrays.asList(R.drawable.main_1, R.drawable.main_2, R.drawable.main_3,
                R.drawable.main_4, R.drawable.main_5, R.drawable.main_6, R.drawable.main_7);

        mModeAdapter = new ModeAdapter();
        recyclerView.setAdapter(mModeAdapter);

        initModeData();
    }

    private void initModeData() {
        BmobQuery<Mode> query = new BmobQuery<>();
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

        public ModeHolder(View itemView) {
            super(itemView);

            mModeView = (ModeView) itemView.findViewById(R.id.item_mode_view);
            mModeView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

        public void bindMode(Mode mode) {
            int[] colors = new int[32];
            //just for test
            int[] colorArray = new int[]{Color.GREEN, Color.BLUE, Color.RED};
            for (int i = 0; i < 32; i++) {
                colors[i] = colorArray[i % colorArray.length];
            }

            // fetch color from NormalState
            mModeView.update(
                    colors, mode.getModeName()
            );
        }
    }

    private class ModeAdapter extends RecyclerView.Adapter<ModeHolder> {

        private List<Mode> mModeList;

        public ModeAdapter() {
            mModeList = new ArrayList<>();
            for (int i = 0; i < mPlaceHoldModeNames.length; i++) {
                Mode mode = new Mode();
                mode.setModeName(mPlaceHoldModeNames[i]);
                mModeList.add(mode);
            }
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
