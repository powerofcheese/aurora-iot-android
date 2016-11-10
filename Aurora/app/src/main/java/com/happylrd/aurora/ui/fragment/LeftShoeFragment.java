package com.happylrd.aurora.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.happylrd.aurora.R;
import com.happylrd.aurora.constant.Constants;
import com.happylrd.aurora.todo.BlueToothCommunication;
import com.happylrd.aurora.ui.view.ShoeView;

public class LeftShoeFragment extends Fragment {

    private ShoeView mLeftShoe;

    private BlueToothCommunication blueToothCommunication;
    private String send;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case Constants.MESSAGE_TOAST:
                    Toast.makeText(getContext(), msg.getData().getString("TOAST"),
                            Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    public ShoeView getLeftShoe() {
        return mLeftShoe;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater
                .inflate(R.layout.fragment_left_shoe, container, false);

        mLeftShoe = (ShoeView) view.findViewById(R.id.left_shoe);

        blueToothCommunication = new BlueToothCommunication();
        blueToothCommunication.setHandler(handler);
        send = "";

        mLeftShoe = (ShoeView) view.findViewById(R.id.left_shoe);
        mLeftShoe.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_MOVE) {
                    int i = mLeftShoe.onTouch(event);
                    send = "custom " + mLeftShoe.colorToHexString(mLeftShoe.getTempColor()) + " " + i;
                    if (blueToothCommunication.mService != null) {
                        blueToothCommunication.write(send);
                    }
                }
                return true;
            }
        });

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
