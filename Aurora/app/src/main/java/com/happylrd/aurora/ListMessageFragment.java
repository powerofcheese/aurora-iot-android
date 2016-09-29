package com.happylrd.aurora;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;

public class ListMessageFragment extends Fragment {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private MessageAdapter mMessageAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.recycler_view, container, false);

        initView(view);
        initListener();

        return view;
    }

    private void initView(View view){
        recyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        mMessageAdapter = new MessageAdapter();
        AlphaInAnimationAdapter alphaInAnimationAdapter =
                new AlphaInAnimationAdapter(mMessageAdapter);
        ScaleInAnimationAdapter scaleInAnimationAdapter =
                new ScaleInAnimationAdapter(alphaInAnimationAdapter);
        recyclerView.setAdapter(scaleInAnimationAdapter);

        swipeRefreshLayout.setDistanceToTriggerSync(400);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        swipeRefreshLayout.setSize(SwipeRefreshLayout.LARGE);
    }

    private void initListener(){
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
    }

    private class MessageHolder extends RecyclerView.ViewHolder{
        private CircleImageView civ_head_portrait;
        private TextView tv_nick_name;
        private TextView tv_message;

        public MessageHolder(View itemView){
            super(itemView);

            civ_head_portrait = (CircleImageView) itemView.findViewById(R.id.civ_head_portrait);
            tv_nick_name = (TextView) itemView.findViewById(R.id.tv_nick_name);
            tv_message = (TextView) itemView.findViewById(R.id.tv_message);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

        public void bindMessage(){

        }
    }

    private class MessageAdapter extends RecyclerView.Adapter<MessageHolder>{

        private static final int LENGTH = 100;

        public MessageAdapter(){

        }

        @Override
        public MessageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater
                    .inflate(R.layout.item_list, parent, false);
            return new MessageHolder(view);
        }

        @Override
        public void onBindViewHolder(MessageHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return LENGTH;
        }
    }
}
