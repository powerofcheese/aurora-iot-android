package com.happylrd.aurora;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by lenovo on 2016/9/5.
 */
public class Fragment_shoe_right extends Fragment {
    private shoeView right_shoe;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_right_shoe, container, false);
        return view;
    }

}
