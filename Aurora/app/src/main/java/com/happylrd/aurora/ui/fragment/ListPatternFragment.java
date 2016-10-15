package com.happylrd.aurora.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.happylrd.aurora.R;

import java.util.Arrays;
import java.util.List;

public class ListPatternFragment extends Fragment {

    private RecyclerView recyclerView;
    private PatternAdapter mPatternAdapter;
    private List<Integer> pics;
    private String[] names = {"wave", "ball", "star", "go", "fire", "eye", "charse"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycler_view, container, false);

        initView(view);
        initListener();

        return view;
    }

    private void initView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        pics = Arrays.asList(R.drawable.main_1, R.drawable.main_2, R.drawable.main_3,
                R.drawable.main_4, R.drawable.main_5, R.drawable.main_6, R.drawable.main_7);

        mPatternAdapter = new PatternAdapter(pics);
        recyclerView.setAdapter(mPatternAdapter);
    }

    private void initListener() {
    }

    private class PatternHolder extends RecyclerView.ViewHolder {

        private ImageView itemAvatar;

        public PatternHolder(View itemView) {
            super(itemView);

            itemAvatar = (ImageView) itemView.findViewById(R.id.item_newlist_image);
            itemAvatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }

    private class PatternAdapter extends RecyclerView.Adapter<PatternHolder> {
        private List<Integer> mContents;

        public PatternAdapter(List<Integer> contents) {
            mContents = contents;
        }

        @Override
        public PatternHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater
                    .inflate(R.layout.item_pattern, parent, false);

            return new PatternHolder(view);
        }

        @Override
        public void onBindViewHolder(PatternHolder holder, int position) {
            holder.itemAvatar.setBackgroundResource(mContents.get(position));
        }

        @Override
        public int getItemCount() {
            return mContents.size();
        }
    }
}
