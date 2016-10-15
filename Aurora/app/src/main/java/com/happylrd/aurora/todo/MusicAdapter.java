package com.happylrd.aurora.todo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.happylrd.aurora.R;

import java.util.List;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.ViewHolder> {
    //为了初始化(引入)item的布局
    private LayoutInflater mInflater;
    private Context mContext;
    protected List<String> mDatas;

    private onItemClickListener mOnItemClickListener;

    public interface onItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(MusicAdapter.onItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public MusicAdapter(Context context, List<String> datas) {
        mContext = context;
        mDatas = datas;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder = new ViewHolder(mInflater.inflate(R.layout.item_single_textview, parent, false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.tv.setText(mDatas.get(position));
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(holder.itemView, position);
                }
            });
        }

        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        lp.height = (int) (150 + Math.random() * 500);
        holder.itemView.setLayoutParams(lp);
        holder.tv.setText(mDatas.get(position));

    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv;

        public ViewHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.id_music_name);
        }
    }
}
