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
import android.widget.Button;
import android.widget.TextView;

import com.artitech.tsalano.tukisha.errorhandling.BackendDownException;
import com.artitech.tsalano.tukisha.errorhandling.InvalidMeterNumberException;
import com.artitech.tsalano.tukisha.model.ErrorMessageModel;
import com.artitech.tsalano.tukisha.model.VoucherModel;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.io.IOException;
import java.net.SocketTimeoutException;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Tsheko on 13-May-18.
 */

@SuppressLint("ValidFragment")
public class Re_PrintFragment extends Fragment {

    private TextView mToolbarTitleTextView;
    private TextView itemMeterNumber, errorMessage;

    private TukishaApplication tukishaApplication;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reprint, container, false);

        Button buyReprintButton = (Button) view.findViewById(R.id.buyReprintButton);
        itemMeterNumber = (TextView) view.findViewById(R.id.item_meternumber);

        errorMessage = (TextView) view.findViewById(R.id.errormessage);

        tukishaApplication = (TukishaApplication) getActivity().getApplication();

        buyReprintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!isMeterNumberValid(itemMeterNumber.getText().toString())) {
                    itemMeterNumber.setError(getActivity().getString(R.string.error_invalid_meternumberlength));
                    itemMeterNumber.requestFocus();
                    return;
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder
                        .setMessage("Are you sure want to re-print the voucher for meter number " + itemMeterNumber.getText() + "?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                // Yes-code
                                confirmDialog();
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

    private void confirmDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder
                .setMessage("Are you sure you want to re-print?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // Yes-code

                        final ProgressDialog progressDialog = new ProgressDialog(getActivity()); // this = YourActivity
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progressDialog.setMessage("Printing voucher, please wait...");
                        progressDialog.setIndeterminate(true);
                        progressDialog.setCanceledOnTouchOutside(false);
                        progressDialog.show();

                        AsyncHttpClient client = new AsyncHttpClient();
                        client.get("http://munipoiapp.herokuapp.com/api/app/reprint?meternumber=" + itemMeterNumber.getText().toString() + "&agentid=" + tukishaApplication.getAgentID(), new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                try {

                                    String response = responseBody == null ? null : new String(
                                            responseBody, getCharset());

                                    progressDialog.dismiss();

                                    if (response.contains("ERROR")) {
                                        Gson gson = new Gson();
                                        ErrorMessageModel error = gson.fromJson(response, ErrorMessageModel.class);
                                        throw new InvalidMeterNumberException(error.getCustMessage());

                                    } else {
                                        Gson gson = new Gson();

                                        VoucherModel voucher = gson.fromJson(response, VoucherModel.class);
                                        Log.d("Response: ", response);

                                        Intent i = new Intent(getActivity(), ThankYouActivity.class);
                                        i.putExtra("vouchernumber", voucher.getTokenNumber());
                                        i.putExtra("distributor", voucher.getDistributer());
                                        i.putExtra("date", voucher.getDate());
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
                                        i.putExtra("balance", tukishaApplication.getBalance());
                                        i.putExtra("header", "TAX INVOICE (copy)");
                                        startActivity(i);
                                    }

                                } catch (final IOException e) {

                                    errorMessage.setText("Technical error occurred " + e.getMessage());
                                    progressDialog.dismiss();
                                } catch (final InvalidMeterNumberException e) {

                                    errorMessage.setText(e.getMessage());
                                    progressDialog.dismiss();

                                } catch (Exception e) {
                                    errorMessage.setText("Technical error occurred " + e.getMessage());
                                    progressDialog.dismiss();
                                }
                            }

                            @Override
                            public void onFailure(int status, Header[] headers, byte[] responseBody, Throwable error) {
                                if (status == 404) {
                                    try {
                                        throw new BackendDownException("System is down, Please try again later or Call this number 0861 852 853");
                                    } catch (BackendDownException e) {
                                        errorMessage.setText("Technical error occurred, either your connection is down or you ran out of data. " + e.getMessage());
                                    }
                                } else if (error instanceof SocketTimeoutException) {
                                    errorMessage.setText("Connection timeout !");
                                    progressDialog.dismiss();
                                } else
                                    errorMessage.setText("Technical error occurred, either your connection is down or you ran out of data. ");

                            }
                        });

                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        }).show();

    }

}





