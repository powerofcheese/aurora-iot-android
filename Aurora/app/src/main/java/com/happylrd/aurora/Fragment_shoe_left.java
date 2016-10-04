package com.happylrd.aurora;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by lenovo on 2016/9/5.
 */
public class Fragment_shoe_left extends Fragment {

    private shoeView left_shoe;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_left_shoe, container, false);

        left_shoe = (shoeView) view.findViewById(R.id.Left);
        return view;
    }

    public void setColor(int color){
        left_shoe.setTemp_color(color);
    }

    public String[] getLC(){
        return left_shoe.getLC();
    }
}
