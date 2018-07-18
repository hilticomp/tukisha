package com.artitech.tsalano.tukisha;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.artitech.tsalano.tukisha.errorhandling.BackendDownException;
import com.artitech.tsalano.tukisha.errorhandling.InsufficientFundsException;
import com.artitech.tsalano.tukisha.model.DSTVCustomerModel;
import com.artitech.tsalano.tukisha.model.DSTVResponseModel;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.io.IOException;
import java.net.SocketTimeoutException;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Tsheko on 13-May-17.
 */

@SuppressLint("ValidFragment")
public class PayDSTVActivity extends AppCompatActivity {

    EditText itemAmount;
    TextView errorMessage,mToolbarTitleTextView,item_accountnumber,item_initials,item_Surname,item_customernumber,item_amountdue,item_cellNumber,item_dateofpayment ;
    TukishaApplication tukishaApplication;
    Button payDSTVButton;
    private Context context;
    private Toolbar toolbar;
    EditText editAmount;
    DSTVCustomerModel DSTVCustomerInfo;
    private String customerid, accountnumber,customerinitials,customersurname,amount,amountdue,cellNumber,dateofpayment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_paydstv);

        context = this;

        tukishaApplication = (TukishaApplication) getApplicationContext();
        Button btnGetMoreResults = (Button)findViewById(R.id.btn);
        btnGetMoreResults.setText("Trading Balance :  " + tukishaApplication.getBalance());

        final View gohome = findViewById(R.id.gohome);
        gohome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent int9 = new Intent(context,MainActivity.class);
                startActivity(int9);
            }
        });

        Bundle bundle = getIntent().getExtras();
        customerid = bundle.getString("customernumber");
        customerinitials = bundle.getString("customerinitials");
        customersurname = bundle.getString("customersurname");
        accountnumber = bundle.getString("accountnumber");
        amountdue = bundle.getString("amountdue");
        amount = bundle.getString("amount");
        cellNumber = bundle.getString("cellNumber");

        payDSTVButton = (Button) findViewById(R.id.payDSTVButton);
        itemAmount = (EditText) findViewById(R.id.item_amount);
        item_accountnumber = (TextView) findViewById(R.id.item_accountnumber);
        item_initials = (TextView) findViewById(R.id.item_initials);
        item_customernumber = (TextView) findViewById(R.id.item_customernumber);
        item_Surname = (TextView) findViewById(R.id.item_Surname);
        item_amountdue = (TextView) findViewById(R.id.item_amountdue);
        item_cellNumber = (TextView) findViewById(R.id.item_cellnumber);

        item_customernumber.setText(customerid);
        item_amountdue.setText(amountdue);
        item_accountnumber.setText(accountnumber);
        item_initials.setText(customerinitials);
        item_Surname.setText(customersurname);
        itemAmount.setText(amount);
        item_cellNumber.setText(cellNumber);

        errorMessage = (TextView) findViewById(R.id.errormessage);

        payDSTVButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!isAmountValid(itemAmount.getText().toString())) {
                    itemAmount.setError(context.getString(R.string.error_invalid_amountvalue));
                    itemAmount.requestFocus();
                    return;
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder
                        .setMessage("Are you sure want to pay DSTV account "+ accountnumber +" for R" + itemAmount.getText() + "?")
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


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        configureToolbar();

        if (mToolbarTitleTextView != null) {
            //Setting name for toolbar
            mToolbarTitleTextView.setText("Pay DSTV");
        }



    }


    private boolean isAmountValid(String amount) {

        if (amount.length() > 0)
            return Integer.parseInt(amount) >= 20;
        else
            return false;

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

        final ProgressDialog progressDialog = new ProgressDialog(context); // this = YourActivity
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Processing payment, please wait...");
        progressDialog.setIndeterminate(true);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

                        AsyncHttpClient client = new AsyncHttpClient();
                        client.get("https://munipoiapp.herokuapp.com/api/app/dstvtpayment?agentid=" + tukishaApplication.getAgentID() + "&customerid=" + customerid + "&amount=" + itemAmount.getText().toString() + "&cellphone=" + item_cellNumber.getText().toString()+ "&accountnumber=" + item_accountnumber.getText().toString(), new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(int status, Header[] headers, byte[] responseBody) {
                                try {

                                    progressDialog.dismiss();

                                    final String response = responseBody == null ? null : new String(
                                            responseBody, getCharset());

                                    if (status == 200) {

                                        if (response.contains("insufficient funds")) {

                                            throw new InsufficientFundsException("Insufficient funds. Please load cash.");

                                        } else if (response.contains("doesnt exist")) {

                                            AlertDialog.Builder builder = new AlertDialog.Builder(context);

                                            builder
                                                    .setTitle("Customer Doesnt Exists")
                                                    .setMessage("Customer doesnt exist")
                                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int id) {

                                                        }
                                                    }).show();

                                        }  else {

                                            final Gson gson = new Gson();
                                            final DSTVResponseModel dstvResponse = gson.fromJson(response, DSTVResponseModel.class);

                                            AlertDialog.Builder builder = new AlertDialog.Builder(context);

                                            AlertDialog show = builder.setTitle("Payment Processed")
                                                    .setMessage("Payment processed for account number " + accountnumber + " for R" + itemAmount.getText())
                                                    .setPositiveButton("Successful Print Slip", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int id) {

                                                            tukishaApplication.setBalance(dstvResponse.getBalance());

                                                            Intent CustomInfo = new Intent(PayDSTVActivity.this,PrintDSTV.class);

                                                            CustomInfo.putExtra("amount",dstvResponse.getAmountPaid());
                                                            CustomInfo.putExtra("transactionNumber", dstvResponse.getTransactionNumber());
                                                            CustomInfo.putExtra("receiptNumber", dstvResponse.getReceiptNumber());
                                                            CustomInfo.putExtra("paymentDate", dstvResponse.getPaymentDate());
                                                            CustomInfo.putExtra("customernumber",customerid);
                                                            CustomInfo.putExtra("customerinitials",customerinitials);
                                                            CustomInfo.putExtra("customersurname",customersurname);
                                                            CustomInfo.putExtra("accountnumber",accountnumber);
                                                            CustomInfo.putExtra("amountdue",amountdue);
                                                            CustomInfo.putExtra("cellNumber",cellNumber);




                                                            startActivity(CustomInfo);


                                                        }
                                                    }).show();
                                        }

                                    }

                                } catch (final IOException e) {

                                    errorMessage.setText("Technical error occurred, either your connection is down or you ran out of data. ");
                                    progressDialog.dismiss();

                                }  catch (final InsufficientFundsException e) {

                                    errorMessage.setText(e.getMessage());
                                    progressDialog.dismiss();

                                }  catch (Exception e) {
                                    errorMessage.setText("Technical error occurred, either your connection is down or you ran out of data. ");
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

}





