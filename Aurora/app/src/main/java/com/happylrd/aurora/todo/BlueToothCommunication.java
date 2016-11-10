package com.happylrd.aurora.todo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.happylrd.aurora.constant.Constants;
import com.happylrd.aurora.ui.fragment.StepCounterFragment;

public class BlueToothCommunication {

    static public BluetoothService mService;

    static private Handler out_handler;
    private Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Constants.MESSAGE_TOAST:
                    Message msg1 = out_handler.obtainMessage(Constants.MESSAGE_TOAST);
                    Bundle bundle = new Bundle();
                    bundle.putString("TOAST", msg.getData().getString("TOAST"));
                    msg1.setData(bundle);
                    out_handler.sendMessage(msg1);
                    break;
                case Constants.MESSAGE_READ:
                    byte[] buffer = new byte[msg.arg1];
                    String readByBluetooth = buffer.toString();
                    if (isNumeric(readByBluetooth)) {
                        StepCounterFragment.step_num = Integer.parseInt(readByBluetooth);
                        // StepCounterFragment.setUI();
                        Log.d("MESSAGE_READ", readByBluetooth);
                    } else {
                        //
                    }
                    break;
            }
        }
    };

    public void connect(Handler handler) {
        out_handler = handler;
        if (mService != null) {
            return;
        } else {
            mService = new BluetoothService(mhandler);
        }
    }

    public void setHandler(Handler handler) {
        out_handler = handler;
    }

    static public void write(String s) {
        mService.write(s.getBytes());
    }


    private boolean isNumeric(String str) {
        for (int i = str.length(); --i >= 0; ) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
