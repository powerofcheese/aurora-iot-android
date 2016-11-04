package com.happylrd.aurora.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.happylrd.aurora.R;
import com.happylrd.aurora.ui.view.ShoeView;

public class LeftShoeFragment extends Fragment {

    private ShoeView mLeftShoe;

    public ShoeView getLeftShoe() {
        return mLeftShoe;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater
                .inflate(R.layout.fragment_left_shoe, container, false);

        mLeftShoe = (ShoeView) view.findViewById(R.id.left_shoe);

        Log.d("ShoeView is null?", (mLeftShoe == null)+"");

        return view;
    }

    public int getColor() {
        return mLeftShoe.getTempColor();
    }

    public void setColor(int color) {
        mLeftShoe.setTempColor(color);
    }

    public String[] getLC() {
        return mLeftShoe.getLC();
    }
}
