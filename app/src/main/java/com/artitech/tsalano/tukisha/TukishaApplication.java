package com.artitech.tsalano.tukisha;

import android.annotation.SuppressLint;
import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;

/**
 * Created by solly on 2017/06/04.
 */

public class TukishaApplication extends Application {
    public static final String MyAgentPref = "AgentPrefs";
    public static final String curentAgentID = "agentID";
    public static final String currentBalance = "balance";
    public static final String TOAST = "toast";
    public static final String DEVICE_NAME = "device_name";
    // Message types sent from the BluetoothService Handler
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;
    public static final int MESSAGE_CONNECTION_LOST = 6;
    public static final int MESSAGE_UNABLE_CONNECT = 7;
    private static final boolean DEBUG = true;
    private static final String TAG = "TukishaApplication";
    private static Context context;
    // Name of the connected device
    public String mConnectedDeviceName = null;
    /****************************************************************************************************/
    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_STATE_CHANGE:
                    if (DEBUG)
                        Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
                    switch (msg.arg1) {
                        case BluetoothService.STATE_CONNECTED:
                            Toast.makeText(getApplicationContext(), "Device Connected",
                                    Toast.LENGTH_SHORT).show();
                            break;
                        case BluetoothService.STATE_CONNECTING:
                            Toast.makeText(getApplicationContext(), "Device Connecting ...",
                                    Toast.LENGTH_SHORT).show();
                            break;
                        case BluetoothService.STATE_LISTEN:
                        case BluetoothService.STATE_NONE:
                            Toast.makeText(getApplicationContext(), "Device Not Connected",
                                    Toast.LENGTH_SHORT).show();
                            break;
                    }
                    break;
                case MESSAGE_WRITE:

                    break;
                case MESSAGE_READ:

                    break;
                case MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
                    Toast.makeText(getApplicationContext(),
                            "Connected to " + mConnectedDeviceName,
                            Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Connected to " + mConnectedDeviceName);
                    break;
                case MESSAGE_TOAST:
                    Toast.makeText(getApplicationContext(),
                            msg.getData().getString(TOAST), Toast.LENGTH_SHORT)
                            .show();
                    break;
                case MESSAGE_CONNECTION_LOST:    //蓝牙已断开连接
                    Toast.makeText(getApplicationContext(), "Device connection was lost",
                            Toast.LENGTH_SHORT).show();
                    break;
                case MESSAGE_UNABLE_CONNECT:     //无法连接设备
                    Toast.makeText(getApplicationContext(), "Unable to connect device",
                            Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    // Local Bluetooth adapter
    public BluetoothAdapter mBluetoothAdapter = null;
    // Member object for the services
    public BluetoothService mService = null;
    private SharedPreferences mPreferences;
    private String balance, agentID;

    public static Context getAppContext() {
        return TukishaApplication.context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        TukishaApplication.context = getApplicationContext();

        mPreferences = getSharedPreferences(MyAgentPref, Context.MODE_PRIVATE);

    }

    public void setupBluetoothSession() {
        mService = new BluetoothService(this, mHandler);
    }

    public String getAgentID() {

        mPreferences = TukishaApplication.context.getSharedPreferences(MyAgentPref, Context.MODE_PRIVATE);
        agentID = mPreferences.getString(curentAgentID, null);

        return agentID;
    }

    public void setAgentID(String agentID) {

        mPreferences = getSharedPreferences(MyAgentPref, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(curentAgentID, agentID);
        editor.commit();
    }

    public String getBalance() {

        mPreferences = TukishaApplication.context.getSharedPreferences(MyAgentPref, Context.MODE_PRIVATE);
        balance = mPreferences.getString(currentBalance, null);

        return balance;
    }

    public void setBalance(String balance) {

        mPreferences = getSharedPreferences(MyAgentPref, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(currentBalance, balance);
        editor.commit();
    }

    /*
    * SendDataString
    */
    public void SendDataString(String data) {

        if (mService.getState() != BluetoothService.STATE_CONNECTED) {
            Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        if (data.length() > 0) {
            try {
                mService.write(data.getBytes("GBK"));
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /*
 *SendDataByte
 */
    public void SendDataByte(byte[] data) {

        if (mService.getState() != BluetoothService.STATE_CONNECTED) {
            Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        mService.write(data);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
