package com.happylrd.aurora;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

/**
 * Created by lenovo on 2016/8/25.
 */
public class BTS {

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

    public BTS(Handler handler){
        state = STATE_NONE;
        mHandler = handler;
        mdevice = bluetoothAdapter.getRemoteDevice(ADDRESS);
        connectThread = new ConnectThread(mdevice);
        connectThread.start();
    }

    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;


        public ConnectThread(BluetoothDevice device) {
            // Use a temporary object that is later assigned to mmSocket,
            // because mmSocket is final
            BluetoothSocket tmp = null;
            mmDevice = device;

            // Get a BluetoothSocket to connect with the given BluetoothDevice
            try {
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
                Message msg = mHandler.obtainMessage(MainActivity.MESSAGE_TOAST);
                Bundle bundle = new Bundle();
                bundle.putString("TOAST","createRfcommSocketToServiceRecord(MY_UUID)发生异常！");
                msg.setData(bundle);
                mHandler.sendMessage(msg);
            }
            mmSocket = tmp;
        }

        public void run() {
            // Cancel discovery because it will slow down the connection
            //mBluetoothAdapter.cancelDiscovery();

            try {
                // Connect the device through the socket. This will block
                // until it succeeds or throws an exception
                if(bluetoothAdapter.isDiscovering()){
                    bluetoothAdapter.cancelDiscovery();
                }
                mmSocket.connect();
                Message msg = mHandler.obtainMessage(MainActivity.MESSAGE_TOAST);
                Bundle bundle = new Bundle();
                bundle.putString("TOAST", "蓝牙连接成功！");
                msg.setData(bundle);
                mHandler.sendMessage(msg);
            } catch (IOException connectException) {
                Message msg = mHandler.obtainMessage(MainActivity.MESSAGE_TOAST);
                Bundle bundle = new Bundle();
                bundle.putString("TOAST"," 执行mmSocket.connect();时发生异常！" + connectException);
                msg.setData(bundle);
                mHandler.sendMessage(msg);
                try {
                    mmSocket.close();
                    connectThread = null;
                }
                catch (IOException closeException) {
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

    private void manageConnectedSocket(BluetoothSocket socket){
        Communication = new ConnectedThread(socket);
        Communication.start();
    }

    /**
     * This thread runs during a connection with a remote device.
     * It handles all incoming and outgoing transmissions.
     */
    private class ConnectedThread extends Thread {
        private  BluetoothSocket mmSocket;
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
                    mHandler.obtainMessage(MainActivity.MESSAGE_READ, bytes, -1, buffer)
                            .sendToTarget();
                } catch (IOException e) {
                    // connectionLost();
                    Message msg = mHandler.obtainMessage(MainActivity.MESSAGE_TOAST);
                    Bundle bundle = new Bundle();
                    bundle.putString("TOAST", "蓝牙连接异常！");
                    msg.setData(bundle);
                    mHandler.sendMessage(msg);
                    break;
                }
            }
        }

        public void write(byte[] bytes) {
            try {
                mmOutStream.write(bytes);
            } catch (IOException e) {
                Message msg = mHandler.obtainMessage(MainActivity.MESSAGE_TOAST);
                Bundle bundle = new Bundle();
                bundle.putString("TOAST", "The Bluethooth isn't connected！");
                msg.setData(bundle);
                mHandler.sendMessage(msg);
                state = STATE_NONE;
            }
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }

    }

    public void write(byte[] out) {
        // Create temporary object
        ConnectedThread r = Communication;
        r.write(out);
    }


    public synchronized void setState(int s){
        state = s;
    }

    public int getState(){
        return state;
    }
}

