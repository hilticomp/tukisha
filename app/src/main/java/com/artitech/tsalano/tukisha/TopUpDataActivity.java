package com.artitech.tsalano.tukisha;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.artitech.tsalano.tukisha.model.AirtimeVoucher;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.util.regex.Pattern;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Lenovo on 10-May-17.
 */

public class TopUpDataActivity extends AppCompatActivity {

    private TextView mToolbarTitleTextView, itemCellNumber, itemTitle;
    private Toolbar toolbar;
    private Button goHome, sendSMSButton;

    private String agentid, productcode, producttype;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_topupdata);

        context = this;

        Bundle bundle = getIntent().getExtras();
        productcode = bundle.getString("productcode");
        agentid = bundle.getString("agentid");
        producttype = bundle.getString("producttype");

        goHome = (Button) findViewById(R.id.thankyouHomeButton);
        sendSMSButton = (Button) findViewById(R.id.sendVoucherSMSButton);

        itemCellNumber = (EditText) findViewById(R.id.item_cellnumber);
        itemTitle = (TextView) findViewById(R.id.title_name);
        itemTitle.setText("Top Up " + producttype);

        sendSMSButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isValidMobile(itemCellNumber.getText().toString()))
                    confirmDialog();
            }
        });

        goHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(TopUpDataActivity.this, MainActivity.class);
                startActivity(i);
            }
        });


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        configureToolbar();

        if (mToolbarTitleTextView != null) {
            //Setting name for toolbar
            mToolbarTitleTextView.setText("Thank You");
        }

    }

    private void configureToolbar() {
        Toolbar mainToolbar = (Toolbar) findViewById(R.id.toolbar);

        if (mainToolbar != null) {
            mToolbarTitleTextView = (TextView) mainToolbar.findViewById(R.id.toolbar_title_textView);

            setSupportActionBar(mainToolbar);

            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setHomeButtonEnabled(true);
                getSupportActionBar().setDisplayShowTitleEnabled(false);
            }
        }

    }

    private void confirmDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder
                .setMessage("Are you sure you want to top up " + itemCellNumber.getText() + " with data?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // Yes-code

                        final ProgressDialog progressDialog = new ProgressDialog(context);
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progressDialog.setMessage("Sending voucher, please wait...");
                        progressDialog.setIndeterminate(true);
                        progressDialog.setCanceledOnTouchOutside(false);
                        progressDialog.show();


                        AsyncHttpClient client = new AsyncHttpClient();
                        client.get("http://munipoiapp.herokuapp.com/api/app/data?productcode=" + productcode + "&agentid=" + agentid + "&mobileNumber=" + itemCellNumber.getText().toString(), new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                try {

                                    String response = responseBody == null ? null : new String(
                                            responseBody, getCharset());

                                    Gson gson = new Gson();
                                    AirtimeVoucher voucher = gson.fromJson(response, AirtimeVoucher.class);
                                    Log.d("airtime: ", voucher.getVoucher());

                                    progressDialog.dismiss();

                                    Intent i = new Intent(TopUpDataActivity.this, AirtimeThankYouActivity.class);
                                    i.putExtra("vouchernumber", voucher.getVoucher());
                                    i.putExtra("operator", voucher.getOperator());
                                    i.putExtra("date", voucher.getDate());
                                    i.putExtra("amount", voucher.getAmount());
                                    i.putExtra("instructions", voucher.getInstructions());
                                    i.putExtra("balance", voucher.getBalance());
                                    startActivity(i);

                                } catch (Exception e) {
                                    Log.d("airtime error: ", e.toString());
                                    progressDialog.dismiss();
                                }
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                                Log.d("airtime: ", error.toString());
                                progressDialog.dismiss();
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

    private boolean isValidMobile(String phone) {
        boolean check = false;
        if (!Pattern.matches("[a-zA-Z]+", phone)) {
            if (phone.length() < 10 || phone.length() > 13) {
                // if(phone.length() != 10) {
                check = false;
                itemCellNumber.setError("Not Valid Number");
                itemCellNumber.requestFocus();
            } else {
                check = true;
            }
        } else {
            check = false;
        }
        return check;
    }

}
