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
import com.artitech.tsalano.tukisha.model.MonthlyCommisionModel;
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

public class MonthlyCommissionActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

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

    private TextView  cashElectricityTotalTextView,titleTextView, cashTelcoTotalTextView, cashTotalTextView, DSTVTotal,DSTVTotal1, municipalityaTotal,Commission;

    private Button printButton;

    private MonthlyCommisionModel monthlyCommisionModel;


    public static String getDateTimeString() {

        Date today = Calendar.getInstance().getTime();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        return formatter.format(today);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_monthly_commission);

        tukishaApplication = (TukishaApplication) getApplication();
        goHome = (Button) findViewById(R.id.gohome);
        goHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MonthlyCommissionActivity.this, MainActivity.class);
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


        loadMonthlyCommission(getDateTimeString());



        printButton = (Button) findViewById(R.id.printButton);
        printButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tukishaApplication.SendDataByte(PrinterCommand.POS_Print_Text("Monthly Commission\n\n", CHINESE, 0, 1, 1, 0));

                tukishaApplication.SendDataByte(PrinterCommand.POS_Set_Cut(1));

                tukishaApplication.SendDataByte(PrinterCommand.POS_Set_PrtInit());

                tukishaApplication.SendDataString(String.format("Total Sales for Electricity is : %s \nTotal Commission is : %s\n\n", monthlyCommisionModel.getTotalEskom(),monthlyCommisionModel.getEskomCommission()));
                tukishaApplication.SendDataString(String.format("Total Sales for Telco is : %s \nTotal Commission  is : %s\n\n", monthlyCommisionModel.getTelcoSales(), monthlyCommisionModel.getTelcoCommission()));
                tukishaApplication.SendDataString(String.format("Total Sales for DSTV Above R70 is : %s \nTotal Commission  is : %s\n\n", monthlyCommisionModel.getDstvaboveAmount(), monthlyCommisionModel.getDstvAboveCommission()));
                tukishaApplication.SendDataString(String.format("Total Sales for DSTV Below R70 is : %s \nTotal Commission  is : %s\n\n", monthlyCommisionModel.getDstvBelowSales(), monthlyCommisionModel.getDstvBelowCommission()));
                tukishaApplication.SendDataString(String.format("Total Sales for Municipality is : %s \nTotal Commission  is : %s\n\n", monthlyCommisionModel.getMunicipalitySales(), monthlyCommisionModel.getMunicipalityCommission()));
                tukishaApplication.SendDataString(String.format("Total Sales for %s is :  %s\n\n", getDateTimeString(),monthlyCommisionModel.getTotalAmount()));
                tukishaApplication.SendDataString(String.format("Total Commission   for %s is :  %s\n\n", getDateTimeString(),monthlyCommisionModel.getTelcoCommission()));

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

        new DatePickerDialog(MonthlyCommissionActivity.this, this, myCalendar
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
        client.get("https://munipoiapp.herokuapp.com/api/app/agentmonthlycommission?agentid=" + tukishaApplication.getAgentID()  + "&dateprocessed=" + startdate, new AsyncHttpResponseHandler() {
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
                        monthlyCommisionModel = gson.fromJson(response, MonthlyCommisionModel.class);
                        cashElectricityTotalTextView.setText("Eskom Total: " + monthlyCommisionModel.getTotalEskom()+ "  | Percent   : " + monthlyCommisionModel.getEskomPecent()+ "  | Commission : " + monthlyCommisionModel.getEskomCommission());
                        cashTelcoTotalTextView.setText("Telco Total: " + monthlyCommisionModel.getTelcoSales()+ "  | Percent  : " + monthlyCommisionModel.getTelcoPercentage()+ "  | Commission : " + monthlyCommisionModel.getTelcoCommission());
                        DSTVTotal.setText("DSTV Above R69: " + monthlyCommisionModel.getDstvaboveAmount()+ "  | Amount  : " + monthlyCommisionModel.getDstvabovePercentage()+ "  | Commission : " + monthlyCommisionModel.getDstvAboveCommission());
                        DSTVTotal1.setText("DSTV Below R70: " + monthlyCommisionModel.getDstvBelowSales()+ "  | Percent  : " + monthlyCommisionModel.getDstvBelowPercentage()+ "  | Commission : " + monthlyCommisionModel.getDstvBelowCommission());
                        municipalityaTotal.setText("Municipality: " + monthlyCommisionModel.getMunicipalitySales()+ "  | Percent   : " + monthlyCommisionModel.getMunicipalityPercentage()+ "  | Commission : " + monthlyCommisionModel.getMunicipalityCommission());
                        cashTotalTextView.setText("Total Sales : " + monthlyCommisionModel.getTotalAmount());
                        Commission.setText("Total Commission : " + monthlyCommisionModel.getTotalCommission());

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
