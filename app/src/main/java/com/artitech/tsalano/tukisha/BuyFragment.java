package com.artitech.tsalano.tukisha;

/**
 * Created by rammamn on 2017/08/07.
 */

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class BuyFragment extends Fragment {


    /******************************************************************************************************/
    // Debugging
    private static final String TAG = "MainActivity";
    private static final boolean DEBUG = true;
    /*******************************************************************************************************/
    // Key names received from the BluetoothService Handler

    private static final int REQUEST_CONNECT_DEVICE = 1;
    /******************************************************************************************************/
    private static final int REQUEST_ENABLE_BT = 2;
    private static final String CHINESE = "GBK";
    private static String balance, agentid;
    TukishaApplication tukishaApplication;
    private Button btnGetMoreResults;

    @Override
    public void onStart() {
        super.onStart();

        // If Bluetooth is not on, request that it be enabled.
        if (tukishaApplication.mBluetoothAdapter == null) {
            Intent enableIntent = new Intent(
                    BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        } else if (!tukishaApplication.mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(
                    BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            // Otherwise, setup the session
        } else {
            if (tukishaApplication.mService == null)
                tukishaApplication.setupBluetoothSession();
        }
    }

    @Override
    public synchronized void onResume() {
        super.onResume();

        if (tukishaApplication.mService != null) {

            if (tukishaApplication.mService.getState() == BluetoothService.STATE_NONE) {
                // Start the Bluetooth services
                tukishaApplication.mService.start();
            }
        }
    }

    @Override
    public synchronized void onPause() {
        super.onPause();
        if (DEBUG)
            Log.e(TAG, "- ON PAUSE -");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Stop the Bluetooth services
        if (tukishaApplication.mService != null)
            tukishaApplication.mService.stop();
        if (DEBUG)
            Log.e(TAG, "--- ON DESTROY ---");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        tukishaApplication = (TukishaApplication) getActivity().getApplicationContext();

        View view = inflater.inflate(R.layout.fragment_buy, container, false);


        btnGetMoreResults = (Button)view.findViewById(R.id.btnGetMoreResults);
        btnGetMoreResults.setText("Trading Balance : " + tukishaApplication.getBalance());




        // Inflate the layout for this fragment
        return view;

    }
}

