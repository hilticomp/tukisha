package com.artitech.tsalano.tukisha;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.artitech.tsalano.tukisha.errorhandling.BackendDownException;
import com.artitech.tsalano.tukisha.errorhandling.InvalidMeterNumberException;
import com.artitech.tsalano.tukisha.errorhandling.MeterNumberNotRegisteredException;
import com.artitech.tsalano.tukisha.model.ErrorMessageModel;
import com.google.gson.Gson;
import com.artitech.tsalano.tukisha.model.VoucherModel;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Tsheko on 13-May-17.
 */

@SuppressLint("ValidFragment")
public class PurchaseCreditNoteFragment extends Fragment {

    EditText itemAmount, itemMeterNumber;
    TextView errorMessage;
    TukishaApplication tukishaApplication;
    Button buyElectricity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_purchasecreditnote, container, false);

        buyElectricity = (Button) view.findViewById(R.id.buyElectricityButton);


        itemMeterNumber = (EditText) view.findViewById(R.id.item_meternumber);
        itemAmount = (EditText) view.findViewById(R.id.item_amount);

        errorMessage = (TextView) view.findViewById(R.id.errormessage);

        tukishaApplication = (TukishaApplication) getActivity().getApplication();

        buyElectricity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!isMeterNumberValid(itemMeterNumber.getText().toString())) {
                    itemMeterNumber.setError(getActivity().getString(R.string.error_invalid_meternumberlength));
                    itemMeterNumber.requestFocus();
                    return;
                }

                if (!isAmountValid(itemAmount.getText().toString())) {
                    itemAmount.setError(getActivity().getString(R.string.error_invalid_amountvalue));
                    itemAmount.requestFocus();
                    return;
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder
                        .setMessage("Are you sure want to buy electricity for meter " + itemMeterNumber.getText() + "?")
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

    private boolean isAmountValid(String amount) {

        if (amount.length() > 0)
            return Integer.parseInt(amount) >= 10;
        else
            return false;

    }

    private void confirmDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder
                .setMessage("Are you sure you want to buy for R" + itemAmount.getText() + "?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // Yes-code

                        final ProgressDialog progressDialog = new ProgressDialog(getActivity()); // this = YourActivity
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progressDialog.setMessage("Printing voucher, please wait...");
                        progressDialog.setCancelable(false);
                        progressDialog.setIndeterminate(true);
                        progressDialog.setCanceledOnTouchOutside(false);
                        progressDialog.show();

                        AsyncHttpClient client = new AsyncHttpClient();
                        client.get("http://munipoiapp.herokuapp.com/api/app/electricity?meternumber=" + itemMeterNumber.getText().toString() + "&amount=" + itemAmount.getText().toString() + "&agentid=" + tukishaApplication.getAgentID(), new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(int status, Header[] headers, byte[] responseBody) {
                                try {

                                    String response = responseBody == null ? null : new String(
                                            responseBody, getCharset());

                                    progressDialog.dismiss();

                                    if (status == 200) {

                                        if (response.contains("ERROR")) {
                                            Gson gson = new Gson();
                                            ErrorMessageModel error = gson.fromJson(response, ErrorMessageModel.class);

                                            if (error.getCustMessage().contains("not registered"))
                                                throw new MeterNumberNotRegisteredException("Meter Number not registered. Please enter additional information");
                                            else
                                                throw new InvalidMeterNumberException(error.getCustMessage());

                                        } else {

                                            Gson gson = new Gson();
                                            VoucherModel voucher = gson.fromJson(response, VoucherModel.class);

                                            //Update Balance
                                            tukishaApplication.setBalance(voucher.getBalance());

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
                                            i.putExtra("balance", voucher.getBalance());
                                            i.putExtra("header", "CREDIT VEND - TAX INVOICE");
                                            startActivity(i);
                                        }

                                    }

                                } catch (final IOException e) {

                                    errorMessage.setText("Technical error occurred, either your connection is down or you ran out of data. " + e.getMessage());
                                    progressDialog.dismiss();
                                } catch (final MeterNumberNotRegisteredException e) {

                                    errorMessage.setText(e.getMessage());
                                    progressDialog.dismiss();

                                } catch (final InvalidMeterNumberException e) {

                                    errorMessage.setText(e.getMessage());
                                    progressDialog.dismiss();

                                } catch (Exception e) {
                                    errorMessage.setText("Technical error occurred, either your connection is down or you ran out of data. " + e.getMessage());
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
                                        errorMessage.setText("Technical error occurred " + e.getMessage());
                                    }
                                } else if (error instanceof SocketTimeoutException) {
                                    errorMessage.setText("Connection timeout !");
                                } else {
                                    errorMessage.setText("Technical error occurred");
                                }


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





