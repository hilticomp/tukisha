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
import com.artitech.tsalano.tukisha.model.ElectricityVoucherModel;
import com.artitech.tsalano.tukisha.model.EskomHistoryModel;
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

public class TransactionHistoryActivityEskom extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    protected static ArrayList<ElectricityVoucherModel> listItems;
    Calendar myCalendar = Calendar.getInstance();
    TukishaApplication tukishaApplication;
    private Toolbar toolbar;
    private TextView mToolbarTitleTextView, dateView;
    private ListView transactionListView;
    private RelativeLayout layoutEmpty;
    private EditText startDateEditText,endDateEditText;
    private Boolean isStartDate;
    private Date startDate,endDate;

    public static String getDateTimeString() {

        Date today = Calendar.getInstance().getTime();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        return formatter.format(today);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_transaction_history_eskom);

        tukishaApplication = (TukishaApplication) getApplication();


        final View gohome = findViewById(R.id.gohome);
        gohome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent int1 = new Intent(TransactionHistoryActivityEskom.this,MainActivity.class);
                startActivity(int1);
            }
        });

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        transactionListView = (ListView) findViewById(R.id.listv);
        layoutEmpty = (RelativeLayout) findViewById(R.id.layout_empty);

        startDateEditText = (EditText) findViewById(R.id.startDateEditText);
        startDateEditText.setTag("startdate");
        startDateEditText.setOnClickListener(this);

        endDateEditText = (EditText) findViewById(R.id.endDateEditText);
        endDateEditText.setTag("enddate");
        endDateEditText.setOnClickListener(this);

        configureToolbar();

        if (mToolbarTitleTextView != null) {
            //Setting name for toolbar
            mToolbarTitleTextView.setText(R.string.transaction_history_title);
        }

        loadTransactionHistory(getDateTimeString(),getDateTimeString());

    }

    @Override
    public void onClick(View v) {

        new DatePickerDialog(TransactionHistoryActivityEskom.this, this, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();

        if(v.getTag().equals("startdate"))
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
            Date startdate = sdf.parse(startDateEditText.getText().toString());
            Date enddate = sdf.parse(endDateEditText.getText().toString());

            loadTransactionHistory(sdf.format(startdate.getTime()),sdf.format(enddate.getTime()));

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

    private void loadTransactionHistory(String startdate, String enddate) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        AsyncHttpClient client = new AsyncHttpClient();
        client.get("https://munipoiapp.herokuapp.com/api/app/eskomtransactionhistory?agentid=" + tukishaApplication.getAgentID() + "&startdate=" + startdate  + "&enddate=" + enddate, new AsyncHttpResponseHandler() {
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
                        EskomHistoryModel transactionHistoryModel = gson.fromJson(response, EskomHistoryModel.class);

                       /* TransactionHistoryModel[] transactionHistoryModel = new TransactionHistoryModel[]{new TransactionHistoryModel("Relay", "1234567", "21 Aug 2017", "R200", "Clothing Account", "Foschini"),
                                new TransactionHistoryModel("G Star Raw", "1234567", "21 Aug 2017", "R800", "Clothing Account", "G Star Raw")};

                            */
                        if (transactionHistoryModel.getResults().size() > 0) {

                            listItems = new ArrayList<ElectricityVoucherModel>();

                            for (int i = 0; i < transactionHistoryModel.getResults().size(); i++) {

                                listItems.add(transactionHistoryModel.getResults().get(i));

                            }

                            transactionListView = (ListView) findViewById(R.id.listv);

                            ElectricityListAdapter adapter = new ElectricityListAdapter(getApplicationContext(), R.layout.list_transaction_layout, R.id.item_ProductType, listItems);

                            transactionListView.setAdapter(adapter);

                            // Click event for single list row
                            transactionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                @Override
                                public void onItemClick(AdapterView parent, View view,
                                                        int position, long id) {

                                    ElectricityVoucherModel transHistory = (ElectricityVoucherModel) transactionListView.getItemAtPosition(position);

                                    Intent i = new Intent(getBaseContext(), Re_PrintActivity.class);
                                    i.putExtra("meternumber", transHistory.getMeterNumber());
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
