package com.artitech.tsalano.tukisha;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.artitech.tsalano.tukisha.errorhandling.BackendDownException;
import com.artitech.tsalano.tukisha.model.DSTVTransactionHistoryModel;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

/**
 * Created by solly on 2017/06/29.
 */

public class DstvTransactionHistory extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    protected static ArrayList<DSTVTransactionHistoryModel> listItems;
    Calendar myCalendar = Calendar.getInstance();
    TukishaApplication tukishaApplication;
    private Toolbar toolbar;
    private TextView mToolbarTitleTextView, dateView;
    private ListView transactionListView;
    private RelativeLayout layoutEmpty;
    private EditText startDateEditText,endDateEditText;
    private Boolean isStartDate;
    private Date startDate,endDate;
    private Context context;
    private Button goHome;

    public static String getDateTimeString() {

        Date today = Calendar.getInstance().getTime();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        return formatter.format(today);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_dstv_transaction_history);

        tukishaApplication = (TukishaApplication) getApplication();

        context = this;

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        transactionListView = (ListView) findViewById(R.id.listv);
        layoutEmpty = (RelativeLayout) findViewById(R.id.layout_empty);

        startDateEditText = (EditText) findViewById(R.id.startDateEditText);
        startDateEditText.setTag("startDate");
        startDateEditText.setOnClickListener(this);

        endDateEditText = (EditText) findViewById(R.id.endDateEditText);
        endDateEditText.setTag("endDate");
        endDateEditText.setOnClickListener(this);

        goHome = (Button) findViewById(R.id.gohome);
        goHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DstvTransactionHistory.this, MainActivity.class);
                startActivity(i);
            }
        });

        configureToolbar();

        if (mToolbarTitleTextView != null) {
            //Setting name for toolbar
            mToolbarTitleTextView.setText(R.string.transaction_history_title);
        }

        loadTransactionHistory(getDateTimeString(),getDateTimeString());

    }

    @Override
    public void onClick(View v) {

        new DatePickerDialog(DstvTransactionHistory.this, this, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();

        if(v.getTag().equals("startDate"))
            isStartDate = true;
        else
            isStartDate = false;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear,
                          int dayOfMonth) {
        myCalendar.set(Calendar.YEAR, year);
        myCalendar.set(Calendar.MONTH, monthOfYear);
        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        if(isStartDate)
            startDateEditText.setText(sdf.format(myCalendar.getTime()));
        else
            endDateEditText.setText(sdf.format(myCalendar.getTime()));

        try {
            Date startDate = sdf.parse(startDateEditText.getText().toString());
            Date endDate = sdf.parse(endDateEditText.getText().toString());

            loadTransactionHistory(sdf.format(startDate.getTime()),sdf.format(endDate.getTime()));

        } catch (ParseException e) {

        }


    }

    private void updateStartLabel() {

        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        startDateEditText.setText(sdf.format(myCalendar.getTime()));


    }

    private void updateEndLabel() {

        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        endDateEditText.setText(sdf.format(myCalendar.getTime()));
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

    private void loadTransactionHistory(String startDate, String endDate) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        AsyncHttpClient client = new AsyncHttpClient();
        client.get("https://munipoiapp.herokuapp.com/api/app/dstvtransactionhistory?agentid=" + tukishaApplication.getAgentID() + "&startdate=" + startDate  + "&enddate=" + endDate, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int status, Header[] headers, byte[] responseBody) {
                try {

                    String response = responseBody == null ? null : new String(
                            responseBody, getCharset());

                    progressDialog.dismiss();

                    status = 200;

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
                        DSTVTransactionHistoryModel[] dstvTransactionHistoryModels = gson.fromJson(response, DSTVTransactionHistoryModel[].class);


                        if (dstvTransactionHistoryModels.length > 0) {

                            listItems = new ArrayList<DSTVTransactionHistoryModel>();

                            for (int i = 0; i < dstvTransactionHistoryModels.length; i++) {

                                listItems.add(dstvTransactionHistoryModels[i]);

                            }

                            transactionListView = (ListView) findViewById(R.id.listv);

                            DSTVListAdapter adapter = new DSTVListAdapter(getApplicationContext(), R.layout.list_dstvtransaction_layout, R.id.item_ProductType, listItems);

                            transactionListView.setAdapter(adapter);

                            // Click event for single list row
                            transactionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                @Override
                                public void onItemClick(AdapterView parent, View view,
                                                        int position, long id) {

                                    DSTVTransactionHistoryModel voucher = (DSTVTransactionHistoryModel) transactionListView.getItemAtPosition(position);

                                    Intent i = new Intent(context, ThankYouActivityDstv.class);
                                    i.putExtra("distributor", voucher.getSurname());
                                    i.putExtra("date", voucher.getDatetime());
                                    i.putExtra("dateOfPurchase", voucher.getDatetime());
                                    i.putExtra("energyKWh", voucher.getReceiptNumber());
                                    i.putExtra("amount", voucher.getAmount());
                                    i.putExtra("vatNumber", voucher.getAmount());
                                    i.putExtra("meterNumber", voucher.getAmountDue());
                                    i.putExtra("tokTech", voucher.getAmount());
                                    i.putExtra("alg", voucher.getAmountDue());
                                    i.putExtra("krn", voucher.getAmountDue());
                                    i.putExtra("address", voucher.getFirstName());
                                    i.putExtra("receipt", voucher.getReceiptNumber());
                                    i.putExtra("description",voucher.getTransactionNumber());
                                    i.putExtra("header", "TUKISHA PREPAID (PTY) LTD");

                                    i.putExtra("flag", "Vendor");
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
