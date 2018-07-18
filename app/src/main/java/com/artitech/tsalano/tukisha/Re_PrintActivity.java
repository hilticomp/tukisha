package com.artitech.tsalano.tukisha;

/**
 * Created by solly on 2017/11/29.
 */

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
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
public class Re_PrintActivity extends AppCompatActivity {


    protected static ArrayList<VoucherModel> listItems;
    private ListView transactionListView;
    private RelativeLayout layoutEmpty;
    private TextView errorMessage;
    private EditText meterNumberEditText;
    private static String meternumber;
    private Context context;


    private TukishaApplication tukishaApplication;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_electrcity_reprint);

        tukishaApplication = (TukishaApplication) getApplicationContext();

        context = this;

        Bundle bundle = getIntent().getExtras();
        meternumber = bundle.getString("meternumber");

        Button btnGetMoreResults = (Button)findViewById(R.id.btn);
        btnGetMoreResults.setText("Trading Balance :  " + tukishaApplication.getBalance());

        final View gohome = findViewById(R.id.gohome);
        gohome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent int1 = new Intent(v.getContext(),MainActivity.class);
                startActivity(int1);
            }
        });

        transactionListView = (ListView) findViewById(R.id.listv);
        layoutEmpty = (RelativeLayout) findViewById(R.id.layout_empty);

        meterNumberEditText = (EditText) findViewById(R.id.item_meternumber);
        meterNumberEditText.setText(meternumber);

        errorMessage = (TextView) findViewById(R.id.errormessage);

        Button buyReprintButton = (Button) findViewById(R.id.buyReprintButton);
        buyReprintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!isMeterNumberValid(meterNumberEditText.getText().toString())) {
                    meterNumberEditText.setError(getString(R.string.error_invalid_meternumberlength));
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


    }

    private boolean isMeterNumberValid(String meterNumber) {
        return meterNumber.length() >= 4 && meterNumber.length() <= 13;
    }

    private void loadTransactionHistory() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
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
                       VoucherModel[]voucherModels = gson.fromJson(response, VoucherModel[].class);

                        if (voucherModels.length > 0) {

                            listItems = new ArrayList<VoucherModel>();

                            for (int i = 0; i < voucherModels.length; i++) {

                                listItems.add(voucherModels[i]);

                            }

                            ListAdapterR adapter = new ListAdapterR(context, R.layout.list_transaction_layout, R.id.item_ProductType, listItems);

                            transactionListView.setAdapter(adapter);

                            // Click event for single list row
                            transactionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                @Override
                                public void onItemClick(AdapterView parent, View view,
                                                        int position, long id) {

                                   VoucherModel voucher = (VoucherModel) transactionListView.getItemAtPosition(position);
                                    Log.d("Response: ", voucher.getTokenNumber());

                                    Intent i = new Intent(context, ThankYouActivity.class);
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






