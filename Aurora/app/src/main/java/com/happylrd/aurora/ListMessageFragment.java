package com.happylrd.aurora;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

public class ListMessageFragment extends Fragment {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private List<Integer> pics;
    private String [] names = {"wave","ball","star","go","fire","eye","charse"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.recycler_view, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);

        pics = Arrays.asList(R.drawable.main_1,R.drawable.main_2,R.drawable.main_3,
                R.drawable.main_4,R.drawable.main_5,R.drawable.main_6,R.drawable.main_7);

        ContentAdapter contentAdapter = new ContentAdapter(pics);
        recyclerView.setAdapter(contentAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        swipeRefreshLayout.setDistanceToTriggerSync(400);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        swipeRefreshLayout.setSize(SwipeRefreshLayout.LARGE);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 2000);
            }
        });

        if(MainActivity.service != null) {
            MainActivity.service.write("step".getBytes());
            Log.d("aaa", "记步界面发送step");
        }

        return swipeRefreshLayout;  // recyclerview
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView itemAvatar;

        public ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.item_list, parent, false));
            itemAvatar = (ImageView) itemView.findViewById(R.id.item_newlist_image);
        }
    }

    public class ContentAdapter extends RecyclerView.Adapter<ViewHolder> {

        private List<Integer> contents;

        public ContentAdapter(List<Integer> contents) {
            this.contents = contents;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder,final int position) {
            holder.itemAvatar.setBackgroundResource(contents.get(position));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(MainActivity.service!= null){
                        String send = "" + names[position];
                        MainActivity.service.write(send.getBytes());
                    }
                    else
                    {
                        Toast.makeText(getActivity(),"请先连接蓝牙！",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return contents.size();
        }
    }
}
