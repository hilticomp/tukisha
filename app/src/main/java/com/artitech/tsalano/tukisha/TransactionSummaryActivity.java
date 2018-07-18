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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.artitech.tsalano.tukisha.errorhandling.BackendDownException;
import com.artitech.tsalano.tukisha.model.TransactionSummaryModel;
import com.artitech.tsalano.tukisha.printer.PrinterCommand;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

/**
 * Created by solly on 2017/06/29.
 */

public class TransactionSummaryActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private static final String CHINESE = "GBK";
    Calendar myCalendar = Calendar.getInstance();
    TukishaApplication tukishaApplication;
    private Toolbar toolbar;
    private TextView mToolbarTitleTextView, dateView;
    private ListView transactionListView;
    private RelativeLayout layoutEmpty;
    private EditText startDateEditText,endDateEditText;
    private Boolean isStartDate;
    private Date startDate,endDate;
    private  TextView goHome;
    ;

    private TextView  cashElectricityTotalTextView,totalTransactions, cashTelcoTotalTextView, cashTotalTextView, DSTVTotal,DSTVTotal1, municipalityaTotal,Commission;

    private Button printButton;

    private TransactionSummaryModel transactionSummaryModel;


    public static String getDateTimeString() {

        Date today = Calendar.getInstance().getTime();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        return formatter.format(today);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_transaction_summary);

        tukishaApplication = (TukishaApplication) getApplication();
        goHome = (Button) findViewById(R.id.gohome);
        goHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(TransactionSummaryActivity.this, MainActivity.class);
                startActivity(i);
            }
        });


        startDateEditText = (EditText) findViewById(R.id.startDateEditText);
        startDateEditText.setTag("startdate");
        startDateEditText.setOnClickListener(this);

        /*endDateEditText = (EditText) findViewById(R.id.endDateEditText);
        endDateEditText.setTag("enddate");
        endDateEditText.setOnClickListener(this);*/



        configureToolbar();
        if (mToolbarTitleTextView != null) {
            //Setting name for toolbar
            mToolbarTitleTextView.setText(R.string.cash_mini_statement_title);
        }

        cashElectricityTotalTextView = (TextView) findViewById(R.id.cashElectricityTotal);

        DSTVTotal = (TextView) findViewById(R.id.DSTV);
        DSTVTotal1 = (TextView) findViewById(R.id.DSTV1);

        municipalityaTotal = (TextView) findViewById(R.id.cashMuniTotal);

        cashTelcoTotalTextView = (TextView) findViewById(R.id.cashTelcoTotal);

        cashTotalTextView = (TextView) findViewById(R.id.cashTotal);

        Commission = (TextView) findViewById(R.id.cashTotalCommision);

        totalTransactions = (TextView) findViewById(R.id. TotalTransactions);


        loadMonthlyCommission(getDateTimeString());



        printButton = (Button) findViewById(R.id.printButton);
        printButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tukishaApplication.SendDataByte(PrinterCommand.POS_Print_Text("Transaction Summary\n\n", CHINESE, 0, 1, 1, 0));

                tukishaApplication.SendDataByte(PrinterCommand.POS_Set_Cut(1));

                tukishaApplication.SendDataByte(PrinterCommand.POS_Set_PrtInit());


                tukishaApplication.SendDataString(String.format("Total Sales for Electricity is : %s \nTotal Transactions is : %s\n\n", transactionSummaryModel.getTotalEskom(),transactionSummaryModel.getEskomTransactions()));
                tukishaApplication.SendDataString(String.format("Total Sales for Telco is : %s \nTotal Transactions is : %s\n\n", transactionSummaryModel.getTelcoSales(), transactionSummaryModel.getTelcoTransactions()));
                tukishaApplication.SendDataString(String.format("Total Sales for DSTV Above R70 is : %s \nTotal Transactions is : %s\n\n", transactionSummaryModel.getDstvSales(), transactionSummaryModel.getAbove70Transactions()));
                tukishaApplication.SendDataString(String.format("Total Sales for DSTV Below R70 is : %s \nTotal Transactions is : %s\n\n", transactionSummaryModel.getDstvSales(), transactionSummaryModel.getBelow70Transactions()));
                tukishaApplication.SendDataString(String.format("Total Sales for Municipality is : %s \nTotal Transactions is : %s\n\n", transactionSummaryModel.getMunicipalitySales(), transactionSummaryModel.getMunicipalityTransactions()));
                tukishaApplication.SendDataString(String.format("Total Sales for %s is :  %s\n\n", getDateTimeString(),transactionSummaryModel.getTotalAmount()));
                tukishaApplication.SendDataString(String.format("Total Transaction  for %s is :  %s\n\n", getDateTimeString(),transactionSummaryModel.getTelcoTransactions()));

                tukishaApplication.SendDataString("Date\n");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String currentDateandTime = sdf.format(new Date());
                tukishaApplication.SendDataString(currentDateandTime + "\n\n\n");

                tukishaApplication.SendDataString(String.format("Agent ID:%s\n\n\n", tukishaApplication.getAgentID()));


            }
        });

    }
    @Override
    public void onClick(View v) {

        new DatePickerDialog(TransactionSummaryActivity.this, this, myCalendar
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
            /* Date enddate = sdf.parse(endDateEditText.getText().toString());*/

            loadMonthlyCommission(sdf.format(startdate.getTime()));

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

    private void loadMonthlyCommission(String startdate) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        AsyncHttpClient client = new AsyncHttpClient();
        client.get("https://munipoiapp.herokuapp.com/api/app/tabletsummarystatement?agentid=" + tukishaApplication.getAgentID()  + "&dateprocessed=" + startdate, new AsyncHttpResponseHandler() {
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
                        transactionSummaryModel = gson.fromJson(response, TransactionSummaryModel.class);
                        cashElectricityTotalTextView.setText("Eskom Total: " + transactionSummaryModel.getTotalEskom()+ "  | Transaction  : " + transactionSummaryModel.getEskomTransactions());
                        cashTelcoTotalTextView.setText("Telco Total: " + transactionSummaryModel.getTelcoSales()+ "  | Transaction : " + transactionSummaryModel.getTelcoTransactions());
                        DSTVTotal.setText("DSTV Total Sales: " + transactionSummaryModel.getDstvSales()+ "  | Transaction  : " + transactionSummaryModel.getAbove70Transactions());
                        DSTVTotal1.setText(" Below 70  Transaction  : " + transactionSummaryModel.getBelow70Transactions() );
                        municipalityaTotal.setText("Municipality: " + transactionSummaryModel.getMunicipalitySales()+ "  | Transaction   : " + transactionSummaryModel.getMunicipalityTransactions());
                        totalTransactions.setText("Total Transactions : " + transactionSummaryModel.getTotalTransactions());
                        cashTotalTextView.setText("Total Sales : " + transactionSummaryModel.getTotalAmount());
                        Commission.setText("Total Commission : " + transactionSummaryModel.getTotalCommission());

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
