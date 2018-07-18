package com.artitech.tsalano.tukisha;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by rammamn on 2017/08/07.
 */

public class PayFragment extends Fragment {


    private static final String CHINESE = "GBK";
    private static String balance, agentid;
    TukishaApplication tukishaApplication;
    private Button goHome;
    private Button btnGetMoreResults;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pay, container, false);

        tukishaApplication = (TukishaApplication) getActivity().getApplicationContext();

        btnGetMoreResults = (Button)view.findViewById(R.id.BalanceResults);
        btnGetMoreResults.setText("Trading Balance : " + tukishaApplication.getBalance());



        final View dstv = view.findViewById(R.id.ivDSTV);
        dstv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent int1 = new Intent(getActivity(),DSTVActivity.class);
                startActivity(int1);
            }
        });

        final View home = view.findViewById(R.id.gohome);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent int1 = new Intent(getActivity(),MainTabProductActivity.class);
                startActivity(int1);
            }
        });

        return view;

    }


}

