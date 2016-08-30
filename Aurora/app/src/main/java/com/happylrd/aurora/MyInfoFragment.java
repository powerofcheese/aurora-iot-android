package com.happylrd.aurora;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public class MyInfoFragment extends Fragment {

    private RelativeLayout rl_profile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_info, container, false);

        rl_profile = (RelativeLayout) view.findViewById(R.id.rl_profile);
        rl_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = PersonalInfoActivity.newIntent(getActivity());
                startActivity(intent);
            }
        });

        return view;
    }
}
