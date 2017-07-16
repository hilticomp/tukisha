package com.artitech.tsalano.tukisha;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.artitech.tsalano.tukisha.errorhandling.BackendDownException;
import com.artitech.tsalano.tukisha.model.CashMiniStatementModel;
import com.artitech.tsalano.tukisha.model.TransactionHistoryModel;
import com.artitech.tsalano.tukisha.printer.PrinterCommand;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import cz.msebera.android.httpclient.Header;

/**
 * Created by solly on 2017/06/29.
 */

public class CashMiniStatementActivity extends AppCompatActivity {

    private static final String CHINESE = "GBK";
    protected static ArrayList<TransactionHistoryModel> listItems;
    private TukishaApplication tukishaApplication;
    private Toolbar toolbar;

    private TextView mToolbarTitleTextView, cashElectricityTotalTextView, cashTelcoTotalTextView, cashTotalTextView;

    private Button printButton;

    private CashMiniStatementModel cashMiniStatementModel;

    public static String getDateTimeString() {

        Date today = Calendar.getInstance().getTime();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        return formatter.format(today);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_cash_mini_statement);

        tukishaApplication = (TukishaApplication) getApplication();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        configureToolbar();

        if (mToolbarTitleTextView != null) {
            //Setting name for toolbar
            mToolbarTitleTextView.setText(R.string.cash_mini_statement_title);
        }

        cashElectricityTotalTextView = (TextView) findViewById(R.id.cashElectricityTotal);

        cashTelcoTotalTextView = (TextView) findViewById(R.id.cashTelcoTotal);

        cashTotalTextView = (TextView) findViewById(R.id.cashTotal);

        loadCashMiniStatementHistory();

        printButton = (Button) findViewById(R.id.printButton);
        printButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tukishaApplication.SendDataByte(PrinterCommand.POS_Print_Text("Cash Up Mini Statement\n\n", CHINESE, 0, 1, 1, 0));

                tukishaApplication.SendDataByte(PrinterCommand.POS_Set_Cut(1));
                tukishaApplication.SendDataByte(PrinterCommand.POS_Set_PrtInit());

                tukishaApplication.SendDataString(String.format("Total Sales for Electricity is : R %s\n\n\n", cashMiniStatementModel.getElectricityTotal()));

                tukishaApplication.SendDataString(String.format("Total Sales for Telco is : R %s\n\n\n", cashMiniStatementModel.getTelcoTotal()));

                tukishaApplication.SendDataString(String.format("Total Sales for the day is : R %s\n\n\n", cashMiniStatementModel.getTotal()));

                tukishaApplication.SendDataString("Date\n");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String currentDateandTime = sdf.format(new Date());
                tukishaApplication.SendDataString(currentDateandTime + "\n\n\n");

                tukishaApplication.SendDataString(String.format("Agent ID:%s\n\n\n", tukishaApplication.getAgentID()));


            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
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

    private void loadCashMiniStatementHistory() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        AsyncHttpClient client = new AsyncHttpClient();
        client.get("https://munipoiapp.herokuapp.com/api/app/cashbackministatement?customerId=" + tukishaApplication.getAgentID() + "&date=" + getDateTimeString(), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int status, Header[] headers, byte[] responseBody) {
                try {

                    String response = responseBody == null ? null : new String(
                            responseBody, getCharset());

                    progressDialog.dismiss();

                    if (status == 200) {

                        if (!response.contains("total")) {
                            return;
                        }

                        Gson gson = new Gson();
                        cashMiniStatementModel = gson.fromJson(response, CashMiniStatementModel.class);
                        cashElectricityTotalTextView.setText("Electricity Total : " + cashMiniStatementModel.getElectricityTotal());
                        cashTelcoTotalTextView.setText("Telco Total : " + cashMiniStatementModel.getTelcoTotal());
                        cashTotalTextView.setText("Total : " + cashMiniStatementModel.getTotal());

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
