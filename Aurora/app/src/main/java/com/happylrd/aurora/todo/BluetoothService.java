package com.happylrd.aurora.todo;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.happylrd.aurora.constant.Constants;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class BluetoothService {

    // Unique UUID for this application
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private static final String ADDRESS = "98:4F:EE:03:59:FC";
    public static final int STATE_NONE = 100;       // we're doing nothing(未连接的状况)
    public static final int STATE_CONNECTED = 102;  // now connected to a remote device

    private BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private int state;
    private Handler mHandler;
    private ConnectedThread Communication;
    private ConnectThread connectThread;
    private BluetoothDevice mdevice;

    public BluetoothService(Handler handler) {
        state = STATE_NONE;
        mHandler = handler;
        mdevice = bluetoothAdapter.getRemoteDevice(ADDRESS);
        connectThread = new ConnectThread(mdevice);
        connectThread.start();
    }

    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;

        public ConnectThread(BluetoothDevice device) {
            // Use a temporary object that is later assigned to mmSocket,
            // because mmSocket is final
            BluetoothSocket tmp = null;

            // Get a BluetoothSocket to connect with the given BluetoothDevice
            try {
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
                Message msg = mHandler.obtainMessage(Constants.MESSAGE_TOAST);
                Bundle bundle = new Bundle();
                bundle.putString("TOAST", "连接蓝牙失败！");
                msg.setData(bundle);
                mHandler.sendMessage(msg);
            }
            mmSocket = tmp;
        }

        public void run() {
            try {
                // Connect the device through the socket. This will block
                // until it succeeds or throws an exception
                if (bluetoothAdapter.isDiscovering()) {
                    bluetoothAdapter.cancelDiscovery();
                }
                mmSocket.connect();
                Message msg = mHandler.obtainMessage(Constants.MESSAGE_TOAST);
                Bundle bundle = new Bundle();
                bundle.putString("TOAST", "蓝牙连接成功！");
                msg.setData(bundle);
                mHandler.sendMessage(msg);
            } catch (IOException connectException) {
                Message msg = mHandler.obtainMessage(Constants.MESSAGE_TOAST);
                Bundle bundle = new Bundle();
                bundle.putString("TOAST", " 连接蓝牙失败！");
                msg.setData(bundle);
                mHandler.sendMessage(msg);
                try {
                    mmSocket.close();
                    connectThread = null;
                } catch (IOException closeException) {
                }
                return;
            }
            // Do work to manage the connection (in a separate thread)
            manageConnectedSocket(mmSocket);
        }

        /**
         * Will cancel an in-progress connection, and close the socket
         */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
            }
        }
    }

    private void manageConnectedSocket(BluetoothSocket socket) {
        Communication = new ConnectedThread(socket);
        Communication.start();
    }

    /**
     * This thread runs during a connection with a remote device.
     * It handles all incoming and outgoing transmissions.
     */
    private class ConnectedThread extends Thread {
        private BluetoothSocket mmSocket;
        private InputStream mmInStream;
        private OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the BluetoothSocket input and output streams
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
                state = STATE_CONNECTED;
            } catch (IOException e) {
                setState(STATE_NONE);
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        @Override
        public void run() {
            byte[] buffer = new byte[1024];
            int bytes;

            // Keep listening to the InputStream while connected
            while (true) {
                try {
                    // Read from the InputStream
                    bytes = mmInStream.read(buffer);

                    // Send the obtained bytes to the UI Activity
                    mHandler.obtainMessage(Constants.MESSAGE_READ, bytes, -1, buffer)
                            .sendToTarget();
                } catch (IOException e) {
                    // connectionLost();
                    Message msg = mHandler.obtainMessage(Constants.MESSAGE_TOAST);
                    Bundle bundle = new Bundle();
                    bundle.putString("TOAST", "蓝牙连接异常！" + e);
                    msg.setData(bundle);
                    mHandler.sendMessage(msg);
//                    cancel();
                    break;
                }
            }
        }

        public void write(byte[] bytes) {
            try {
                mmOutStream.write(bytes);
            } catch (IOException e) {
                Message msg = mHandler.obtainMessage(Constants.MESSAGE_TOAST);
                Bundle bundle = new Bundle();
                bundle.putString("TOAST", "还未连接蓝牙！");
                msg.setData(bundle);
                mHandler.sendMessage(msg);
                state = STATE_NONE;
            }
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
            }
        }

    }

    public void write(byte[] out) {
        // Create temporary object
        ConnectedThread r = Communication;
        r.write(out);
    }


    public synchronized void setState(int s) {
        state = s;
    }

    public int getState() {
        return state;
    }

    public void setHandler(Handler mHandler) {
        this.mHandler = mHandler;
    }
}
