package com.happylrd.aurora;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ImageAdapter extends BaseAdapter {

        private static Set<String> mSeletedImg = new HashSet<String>();
        
        private String mDirPath;
        private List<String> mImagPaths;
        private LayoutInflater mInflater;

        public ImageAdapter(Context context,List<String> mDatas,String dirPath){
            this.mDirPath = dirPath;
            this.mImagPaths = mDatas;
            mInflater = LayoutInflater.from(context);
        }
        @Override
        public int getCount() {
            return mImagPaths.size();
        }

        @Override
        public Object getItem(int position) {
            return mImagPaths.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder viewHolder;
            if(convertView == null){
                convertView = mInflater.inflate(R.layout.gallery_item,parent,false);
                viewHolder = new ViewHolder();
                viewHolder.mImg = (ImageView) convertView.findViewById(R.id.id_item_image);
                viewHolder.mSelect = (ImageButton) convertView.findViewById(R.id.id_item_select);
                convertView.setTag(viewHolder);
            }
            else{
                viewHolder = (ViewHolder) convertView.getTag();
            }
            //重置状态
            viewHolder.mImg.setImageResource(R.color.grey);
            viewHolder.mSelect.setImageResource(R.drawable.ic_add_black_24dp);
            viewHolder.mImg.setColorFilter(null);

            ImageLoader.getmInstance(3, ImageLoader.Type.LIFO).loadImage(mDirPath + "/" + mImagPaths.get(position), viewHolder.mImg);
            final String filePath = mDirPath+"/"+mImagPaths.get(position);
            viewHolder.mImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //已被选择
                    if(mSeletedImg.contains(filePath)){
                        mSeletedImg.remove(filePath);
                        viewHolder.mImg.setColorFilter(null);
                        viewHolder.mSelect.setImageResource(R.drawable.ic_add_black_24dp);
                    }//未被选择
                    else {
                        mSeletedImg.add(filePath);
                        //设置为被选择的状态
                        viewHolder.mImg.setColorFilter(Color.parseColor("#77000000"));
                        viewHolder.mSelect.setImageResource(R.drawable.picture_selected);
                    }

                }
            });
            if(mSeletedImg.contains(filePath)){
                viewHolder.mImg.setColorFilter(Color.parseColor("#77000000"));
                viewHolder.mSelect.setImageResource(R.color.white);
            }
            return convertView;
        }

        private class ViewHolder{
            ImageView mImg;
            ImageButton mSelect;
        }
    public static Set<String>  getSeletedImg(){
        return mSeletedImg;
    }
    }