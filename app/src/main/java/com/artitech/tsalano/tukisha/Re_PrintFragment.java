package com.artitech.tsalano.tukisha;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.artitech.tsalano.tukisha.errorhandling.BackendDownException;
import com.artitech.tsalano.tukisha.model.VoucherModel;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Tsheko on 13-May-17.
 */

@SuppressLint("ValidFragment")
public class Re_PrintFragment extends Fragment {

    protected static ArrayList<VoucherModel> listItems;
    private ListView transactionListView;
    private RelativeLayout layoutEmpty;
    private TextView errorMessage;
    private EditText meterNumberEditText;

    private TukishaApplication tukishaApplication;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reprint, container, false);

        transactionListView = (ListView) view.findViewById(R.id.listv);
        layoutEmpty = (RelativeLayout) view.findViewById(R.id.layout_empty);

        meterNumberEditText = (EditText) view.findViewById(R.id.item_meternumber);

        errorMessage = (TextView) view.findViewById(R.id.errormessage);

        tukishaApplication = (TukishaApplication) getActivity().getApplication();

        Button buyReprintButton = (Button) view.findViewById(R.id.buyReprintButton);
        buyReprintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!isMeterNumberValid(meterNumberEditText.getText().toString())) {
                    meterNumberEditText.setError(getActivity().getString(R.string.error_invalid_meternumberlength));
                    meterNumberEditText.requestFocus();
                    return;
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder
                        .setMessage("Are you sure want to re-print the voucher for meter number " + meterNumberEditText.getText() + "?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                // Yes-code
                                loadTransactionHistory();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        })
                        .show();
            }
        });

        // Inflate the layout for this fragment
        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    private boolean isMeterNumberValid(String meterNumber) {
        return meterNumber.length() >= 4 && meterNumber.length() <= 13;
    }


    private void loadTransactionHistory() {

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        AsyncHttpClient client = new AsyncHttpClient();
        client.get("https://munipoiapp.herokuapp.com/api/app/electricitytransactions?agentid=" + tukishaApplication.getAgentID() + "&meternumber=" + meterNumberEditText.getText().toString(), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int status, Header[] headers, byte[] responseBody) {
                try {

                    String response = responseBody == null ? null : new String(
                            responseBody, getCharset());

                    progressDialog.dismiss();

                    if (status == 200) {

                        if (response.contains("no results")) {
                            layoutEmpty.setVisibility(View.VISIBLE);
                            transactionListView.setVisibility(View.GONE);
                            return;
                        } else {
                            layoutEmpty.setVisibility(View.GONE);
                            transactionListView.setVisibility(View.VISIBLE);
                        }

                        Gson gson = new Gson();
                       VoucherModel[] electricityVoucherModels = gson.fromJson(response,VoucherModel[].class);

                        if (electricityVoucherModels.length > 0) {

                            listItems = new ArrayList<VoucherModel>();

                            for (int i = 0; i < electricityVoucherModels.length; i++) {

                                listItems.add(electricityVoucherModels[i]);

                            }

                            ListAdapterR adapter = new ListAdapterR(getActivity(), R.layout.list_transaction_layout, R.id.item_ProductType, listItems);

                            transactionListView.setAdapter(adapter);

                            // Click event for single list row
                            transactionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                @Override
                                public void onItemClick(AdapterView parent, View view,
                                                        int position, long id) {

                                    VoucherModel voucher = (VoucherModel) transactionListView.getItemAtPosition(position);
                                    Log.d("Response: ", voucher.getTokenNumber());

                                    Intent i = new Intent(getActivity(), ThankYouActivity.class);
                                    i.putExtra("vouchernumber", voucher.getTokenNumber());
                                    i.putExtra("distributor", voucher.getDistributer());
                                    i.putExtra("date", voucher.getDate());
                                    i.putExtra("dateOfPurchase", voucher.getDatePurchased());
                                    i.putExtra("energyKWh", voucher.getEnergyKWh());
                                    i.putExtra("amount", voucher.getAmount());
                                    i.putExtra("vatNumber", voucher.getVATNumber());
                                    i.putExtra("meterNumber", voucher.getMeterNumber());
                                    i.putExtra("tokTech", voucher.getTokenTech());
                                    i.putExtra("alg", voucher.getAlg());
                                    i.putExtra("sgc", voucher.getSGC());
                                    i.putExtra("krn", voucher.getKrn());
                                    i.putExtra("ti", voucher.getTI());
                                    i.putExtra("terminal", voucher.getTerminalID());
                                    i.putExtra("client", voucher.getClientID());
                                    i.putExtra("description", voucher.getDescription());
                                    i.putExtra("address", voucher.getAddress());
                                    i.putExtra("receipt", voucher.getReceiptNumber());

                                    i.putExtra("fbetoken", voucher.getFBEtoken());
                                    i.putExtra("fbekwh", voucher.getFBEKwh());
                                    i.putExtra("fbeamount", voucher.getBalance());

                                    i.putExtra("balance", tukishaApplication.getBalance());
                                    i.putExtra("header", "TAX INVOICE (COPY) WARNING, THIS IS A REPRINT");
                                    startActivity(i);

                                }
                            });


                        }

                    }

                } catch (final IOException e) {
                    //errorMessage.setText("Technical error occurred " + e.getMessage());
                    progressDialog.dismiss();
                } catch (Exception e) {
                    //errorMessage.setText("Technical error occurred " + e.getMessage());
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(int status, Header[] headers, byte[] responseBody, Throwable error) {

                progressDialog.dismiss();

                if (status == 404) {
                    try {
                        throw new BackendDownException("System is down, Please try again later or Call this number 078 377 6207");
                    } catch (BackendDownException e) {
                        Log.d("TAG", "Technical error occurred " + e.getMessage());
                    }
                } else if (error instanceof SocketTimeoutException) {
                    Log.d("TAG", "Connection timeout !");
                } else
                    Log.d("TAG", "Technical error occurred");

            }

        });
    }


}





