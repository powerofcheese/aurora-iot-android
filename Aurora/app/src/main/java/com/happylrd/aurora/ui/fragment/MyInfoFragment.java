package com.happylrd.aurora.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.happylrd.aurora.ui.activity.ColorSchemeActivity;
import com.happylrd.aurora.ui.activity.PersonalInfoActivity;
import com.happylrd.aurora.R;
import com.happylrd.aurora.model.MyUser;

import cn.bmob.v3.BmobUser;

public class MyInfoFragment extends Fragment {

    private RelativeLayout rl_profile;
    private ImageView iv_head_portrait;
    private TextView tv_nick_name;

    private RelativeLayout rl_color_scheme;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_info, container, false);
        initView(view);
        initListener();
        initData();

        return view;
    }

    private void initView(View view) {
        rl_profile = (RelativeLayout) view.findViewById(R.id.rl_profile);
        iv_head_portrait = (ImageView) view.findViewById(R.id.iv_head_portrait);
        tv_nick_name = (TextView) view.findViewById(R.id.tv_nick_name);
        rl_color_scheme = (RelativeLayout) view.findViewById(R.id.rl_color_scheme);
    }

    private void initListener() {
        rl_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = PersonalInfoActivity.newIntent(getActivity());
                startActivity(intent);
            }
        });

        rl_color_scheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ColorSchemeActivity.newIntent(getActivity());
                startActivity(intent);
            }
        });
    }

    private void initData() {
        MyUser myUser = BmobUser.getCurrentUser(MyUser.class);
        tv_nick_name.setText(myUser.getNickName());
        if (myUser.getHeadPortraitPath() != null) {
            Glide.with(getActivity())
                    .load(myUser.getHeadPortraitPath())
                    .into(iv_head_portrait);
        }
    }
}
