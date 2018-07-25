package com.artitech.tsalano.tukisha;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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

public class CashMiniStatementActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private static final String CHINESE = "GBK";

    protected static ArrayList<TransactionHistoryModel> listItems;
    private TukishaApplication tukishaApplication;
    private Toolbar toolbar;
    private EditText startDateEditText,endDateEditText;

    private TextView mToolbarTitleTextView, cashElectricityTotalTextView, cashTelcoTotalTextView, cashTotalTextView, DSTVTotal, municipalityaTotal;

    private Button printButton;

    private CashMiniStatementModel cashMiniStatementModel;



    private TextView cashMunicipalityTotalTextView,DSTVTextWiew, titleTextView;

    private Button  goHome;


    private Calendar myCalendar = Calendar.getInstance();

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


        startDateEditText = (EditText) findViewById(R.id.startDateEditText);
        startDateEditText.setTag("startdate");
        startDateEditText.setOnClickListener(this);

        configureToolbar();

        if (mToolbarTitleTextView != null) {
            //Setting name for toolbar
            mToolbarTitleTextView.setText(R.string.cash_mini_statement_title);
        }

        cashElectricityTotalTextView = (TextView) findViewById(R.id.cashElectricityTotal);
        DSTVTotal = (TextView) findViewById(R.id.dstvtotal);
        municipalityaTotal = (TextView) findViewById(R.id.municipality);
        cashTelcoTotalTextView = (TextView) findViewById(R.id.cashTelcoTotal);
        cashTotalTextView = (TextView) findViewById(R.id.cashTotal);

        titleTextView = (TextView) findViewById(R.id.titleTextView);

        loadCashMiniStatementHistory(getDateTimeString());


        printButton = (Button) findViewById(R.id.printButton);
        printButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tukishaApplication.SendDataByte(PrinterCommand.POS_Print_Text("Cash Up Mini Statement\n\n", CHINESE, 0, 1, 1, 0));

                tukishaApplication.SendDataByte(PrinterCommand.POS_Set_Cut(1));
                tukishaApplication.SendDataByte(PrinterCommand.POS_Set_PrtInit());

                tukishaApplication.SendDataString(String.format("Total Sales for Electricity is :  %s\n\n\n", cashMiniStatementModel.getElectricityTotal()));

                tukishaApplication.SendDataString(String.format("Total Sales for Telco is :  %s\n\n\n", cashMiniStatementModel.getTelcoTotal()));

                tukishaApplication.SendDataString(String.format("Total Sales for DSTV is :  %s\n\n\n", cashMiniStatementModel.getDstvtotal()));

                tukishaApplication.SendDataString(String.format("Total Sales for Municipality is :  %s\n\n\n", cashMiniStatementModel.getMunicipalityTotal()));

                tukishaApplication.SendDataString(String.format("Total Sales for the day is :  %s\n\n\n", cashMiniStatementModel.getTotal()));

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

        new DatePickerDialog(CashMiniStatementActivity.this, this, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();

    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear,
                          int dayOfMonth) {
        myCalendar.set(Calendar.YEAR, year);
        myCalendar.set(Calendar.MONTH, monthOfYear);
        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        startDateEditText.setText(sdf.format(myCalendar.getTime()));
       /*titleTextView.setText("Total Sales for " + sdf.format(myCalendar.getTime()));*/

        try {

            Date startdate = sdf.parse(startDateEditText.getText().toString());
            //String currentDateandTime = sdf.format(new Date());

            loadCashMiniStatementHistory(sdf.format(startdate.getTime()));

        } catch (ParseException e) {

        }

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

    private void loadCashMiniStatementHistory(String currentDate) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();


        AsyncHttpClient client = new AsyncHttpClient();
        client.get("https://munipoiapp.herokuapp.com/api/app/cashbackministatement?customerId=" + tukishaApplication.getAgentID() + "&date=" + currentDate, new AsyncHttpResponseHandler() {
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
                        DSTVTotal.setText("DSTV Total  : " + cashMiniStatementModel.getDstvtotal());
                        municipalityaTotal.setText("Municipality Total  : " + cashMiniStatementModel.getMunicipalityTotal());
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
