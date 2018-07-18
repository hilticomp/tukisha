package com.artitech.tsalano.tukisha;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.artitech.tsalano.tukisha.errorhandling.BackendDownException;
import com.artitech.tsalano.tukisha.model.AirtimeVoucher;
import com.artitech.tsalano.tukisha.model.TransactionHistoryModel;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

/**
 * Created by solly on 2017/06/29.
 */

public class TelcoTransactionHistoryActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    protected static ArrayList<TransactionHistoryModel> listItems;
    Calendar myCalendar = Calendar.getInstance();
    TukishaApplication tukishaApplication;
    private Toolbar toolbar;
    private TextView mToolbarTitleTextView, dateView;
    private ListView transactionListView;
    private RelativeLayout layoutEmpty;
    private EditText dateEditText;

    public static String getDateTimeString() {

        Date today = Calendar.getInstance().getTime();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        return formatter.format(today);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_transaction_history);

        tukishaApplication = (TukishaApplication) getApplication();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        transactionListView = (ListView) findViewById(R.id.listv);
        layoutEmpty = (RelativeLayout) findViewById(R.id.layout_empty);

        dateEditText = (EditText) findViewById(R.id.dateEditText);
        dateEditText.setOnClickListener(this);

        configureToolbar();

        if (mToolbarTitleTextView != null) {
            //Setting name for toolbar
            mToolbarTitleTextView.setText(R.string.transaction_history_title);
        }

        loadTransactionHistory(getDateTimeString());

    }

    @Override
    public void onClick(View v) {

        new DatePickerDialog(TelcoTransactionHistoryActivity.this, this, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear,
                          int dayOfMonth) {
        myCalendar.set(Calendar.YEAR, year);
        myCalendar.set(Calendar.MONTH, monthOfYear);
        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        updateLabel();
    }

    private void updateLabel() {

        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        dateEditText.setText(sdf.format(myCalendar.getTime()));

        loadTransactionHistory(sdf.format(myCalendar.getTime()));
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

    private void loadTransactionHistory(String date) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        AsyncHttpClient client = new AsyncHttpClient();
        client.get("https://munipoiapp.herokuapp.com/api/app/telcotransactions?customerId=" + tukishaApplication.getAgentID() + "&searchCriteria=" + date, new AsyncHttpResponseHandler() {
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
                        TransactionHistoryModel[] transactionHistoryModel = gson.fromJson(response, TransactionHistoryModel[].class);

                        if (transactionHistoryModel.length > 0) {

                            listItems = new ArrayList<TransactionHistoryModel>();

                            for (int i = 0; i < transactionHistoryModel.length; i++) {

                                listItems.add(transactionHistoryModel[i]);

                            }

                            transactionListView = (ListView) findViewById(R.id.listv);

                            ListAdapter adapter = new ListAdapter(getApplicationContext(), R.layout.list_transaction_layout, R.id.listv, listItems);

                            transactionListView.setAdapter(adapter);

                            // Click event for single list row
                            transactionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                @Override
                                public void onItemClick(AdapterView parent, View view,
                                                        int position, long id) {

                                    TransactionHistoryModel transHistory = (TransactionHistoryModel) transactionListView.getItemAtPosition(position);

                                    final ProgressDialog progressDialog = new ProgressDialog(TelcoTransactionHistoryActivity.this);
                                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                    progressDialog.setMessage("Re-Printing voucher, please wait...");
                                    progressDialog.setIndeterminate(true);
                                    progressDialog.setCanceledOnTouchOutside(false);
                                    progressDialog.show();

                                    AsyncHttpClient client = new AsyncHttpClient();
                                    client.get("https://munipoiapp.herokuapp.com/api/app/telcoreprint?messageID=" + transHistory.getMessageID(), new AsyncHttpResponseHandler() {
                                        @Override
                                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                            try {

                                                String response = responseBody == null ? null : new String(
                                                        responseBody, getCharset());

                                                Gson gson = new Gson();
                                                AirtimeVoucher voucher = gson.fromJson(response, AirtimeVoucher.class);
                                                Log.d("airtime: ", voucher.getVoucher());

                                                progressDialog.dismiss();

                                                Intent i = new Intent(getBaseContext(), AirtimeThankYouActivity.class);
                                                i.putExtra("vouchernumber", voucher.getVoucher());
                                                i.putExtra("operator", voucher.getOperator());
                                                i.putExtra("date", voucher.getDate());
                                                i.putExtra("amount", voucher.getAmount());
                                                i.putExtra("instructions", voucher.getInstructions());
                                                i.putExtra("balance", voucher.getBalance());
                                                startActivity(i);

                                            } catch (Exception e) {
                                                Log.d("airtime: ", e.getMessage());
                                            }
                                        }

                                        @Override
                                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                                            Log.d("airtime: ", error.getMessage());
                                        }


                                    });

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
