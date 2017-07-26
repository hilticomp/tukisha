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

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Lenovo on 10-May-17.
 */

public class SendSMSActivity extends AppCompatActivity {

    private TextView mToolbarTitleTextView, itemCellNumber;
    private Toolbar toolbar;
    private Button goHome, sendSMSButton;

    private String vouchernumber, amount, energyKWh, message, flag, fbetoken;
    private Context context;

    public static String getDateTimeString() {

        Date today = Calendar.getInstance().getTime();

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        return formatter.format(today);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sendsms);

        context = this;

        Bundle bundle = getIntent().getExtras();
        flag = bundle.getString("flag");

        goHome = (Button) findViewById(R.id.thankyouHomeButton);
        sendSMSButton = (Button) findViewById(R.id.sendVoucherSMSButton);

        if (flag.equals("Electricity")) {

            vouchernumber = bundle.getString("vouchernumber");
            amount = bundle.getString("amount");
            energyKWh = bundle.getString("energyKWh");

            message = "You have top up with " + amount + ", energy KWh:" + energyKWh + " and your voucher number is " + vouchernumber;

            if (bundle.containsKey("fbetoken")) {
                fbetoken = bundle.getString("fbetoken");
                message = message + " and FBE voucher number is " + fbetoken;
            }

        } else if (flag.equals("Airtime")) {

            vouchernumber = bundle.getString("vouchernumber");
            amount = bundle.getString("amount");

            message = "Airtime recharged with " + amount + " and your voucher number is " + vouchernumber;
        }

        itemCellNumber = (EditText) findViewById(R.id.item_cellnumber);

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
                Intent i = new Intent(SendSMSActivity.this, MainActivity.class);
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
                .setMessage("Are you sure you want to send an SMS to " + itemCellNumber.getText() + "?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // Yes-code

                        final ProgressDialog progressDialog = new ProgressDialog(context); // this = YourActivity
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progressDialog.setMessage("Sending voucher, please wait...");
                        progressDialog.setIndeterminate(true);
                        progressDialog.setCanceledOnTouchOutside(false);
                        progressDialog.show();

                        Log.d("URL", "https://www.budgetmessaging.com/sendsms.ashx?user=jimmyjames&password=012345&cell=" + itemCellNumber.getText().toString() + "&msg=" + message + "&ref=" + System.currentTimeMillis() + "&senddate=" + getDateTimeString());

                        AsyncHttpClient client = new AsyncHttpClient();
                        client.get("https://www.budgetmessaging.com/sendsms.ashx?user=jimmyjames&password=012345&cell=" + itemCellNumber.getText().toString() + "&msg=" + message + "&ref=" + System.currentTimeMillis() + "&senddate=" + getDateTimeString(), new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                try {

                                    progressDialog.dismiss();

                                    AlertDialog.Builder confirmationDialog = new AlertDialog.Builder(context);
                                    confirmationDialog
                                            .setTitle("SMS Sent")
                                            .setMessage("SMS was sent successfully")
                                            .setCancelable(false)
                                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                                                public void onClick(DialogInterface dialog, int which) {

                                                    Intent i = new Intent(SendSMSActivity.this, MainActivity.class);
                                                    startActivity(i);

                                                }
                                            }).show();


                                } catch (Exception e) {
                                    Log.d("Response: ", e.toString());
                                    progressDialog.dismiss();
                                }
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                                Log.d("Response: ", error.getMessage().toString());
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
