package com.artitech.tsalano.tukisha;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.artitech.tsalano.tukisha.errorhandling.BackendDownException;
import com.artitech.tsalano.tukisha.errorhandling.InvalidMeterNumberException;
import com.artitech.tsalano.tukisha.errorhandling.MeterNumberNotRegisteredException;
import com.artitech.tsalano.tukisha.model.ErrorMessageModel;
import com.artitech.tsalano.tukisha.model.MuniEkurhuleniVoucherModel;
import com.artitech.tsalano.tukisha.model.UnipinModel;
import com.artitech.tsalano.tukisha.viewholder.CategoryTypesViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.twotoasters.jazzylistview.recyclerview.JazzyRecyclerViewScrollListener;

import java.io.IOException;
import java.net.SocketTimeoutException;

import cz.msebera.android.httpclient.Header;

/**
 * Created by solly on 2017/09/30.
 */

public class UnipinDenominationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    /******************************************************************************************************/
    // Debugging
    private static final String TAG = "UnipinDenominationActivity";
    /*******************************************************************************************************/
    // Key names received from the BluetoothService Handler

    private TukishaApplication tukishaApplication;
    private RecyclerView mRecycler;
    private TextView mToolbarTitleTextView;
    /******************************************************************************************************/
    private LinearLayoutManager mManager;
    private ProgressDialog progressDialog;
    private FirebaseDatabase database;
    private FirebaseRecyclerAdapter<UnipinModel, CategoryTypesViewHolder> mAdapter;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_municipality);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tukishaApplication = (TukishaApplication) getApplication();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Get local Bluetooth adapter
        tukishaApplication.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        database = FirebaseDatabase.getInstance();

        context = getApplicationContext();

        configureToolbar();

        mRecycler = (RecyclerView) findViewById(R.id.invite_recyclerView);
        mRecycler.setHasFixedSize(false);
        mManager = new LinearLayoutManager(this);
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecycler.setLayoutManager(mManager);


        /* PAWAN: If you want to add animation to the recycler view just add these below three lines
        in whichever activity you want which has a recycler view, after importing the library.*/

        JazzyRecyclerViewScrollListener jazzyScrollListener = new JazzyRecyclerViewScrollListener();
        mRecycler.setOnScrollListener(jazzyScrollListener);

        //PAWAN: Channge this numeric "2" to anything to change the animation for recyler view

        jazzyScrollListener.setTransitionEffect(1);


        //Progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.please_wait));

        if (mToolbarTitleTextView != null) {
            //Setting name for toolbar
            mToolbarTitleTextView.setText("Municipality");
        }

        getUnipinDenomations();

    }


    private void configureToolbar() {
        Toolbar mainToolbar = (Toolbar) findViewById(R.id.toolbar);

        if (mainToolbar != null) {
            mToolbarTitleTextView = (TextView) mainToolbar.findViewById(R.id.toolbar_title_textView);
            mToolbarTitleTextView.setText(getString(R.string.app_name));
            setSupportActionBar(mainToolbar);

            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setHomeButtonEnabled(true);
                getSupportActionBar().setDisplayShowTitleEnabled(false);
            }
        }

    }

    private void getUnipinDenomations() {

        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Unipin_Denomination");
        Query postsQuery = myRef;
        mAdapter = new FirebaseRecyclerAdapter<UnipinModel, CategoryTypesViewHolder>
                (UnipinModel.class,
                        R.layout.item_product,
                        CategoryTypesViewHolder.class, postsQuery) {
            @Override
            protected void populateViewHolder(CategoryTypesViewHolder viewHolder,
                                              final UnipinModel model, int position) {

                if (model != null) {
                    viewHolder.bindtoSelectedDenomination(context, model,
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {


                                    AlertDialog.Builder builder = new AlertDialog.Builder(UnipinDenominationActivity.this);

                                    builder
                                            .setMessage("Are you sure you want to buy for " + model.getName() + "?")
                                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int id) {
                                                    // Yes-code

                                                    final ProgressDialog progressDialog = new ProgressDialog(UnipinDenominationActivity.this); // this = YourActivity
                                                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                                    progressDialog.setMessage("Printing voucher, please wait...");
                                                    progressDialog.setCancelable(false);
                                                    progressDialog.setIndeterminate(true);
                                                    progressDialog.setCanceledOnTouchOutside(false);
                                                    progressDialog.show();

                                                    String url_string = model.getEndPoint() + model.getPreVend()  + "&agentid=" + tukishaApplication.getAgentID() + "&amount=" + model.getAmount();

                                                    AsyncHttpClient client = new AsyncHttpClient();
                                                    client.setTimeout(20000);
                                                    client.get(url_string, new AsyncHttpResponseHandler() {
                                                        @Override
                                                        public void onSuccess(int status, Header[] headers, byte[] responseBody) {
                                                            try {

                                                                String response = responseBody == null ? null : new String(
                                                                        responseBody, getCharset());

                                                                progressDialog.dismiss();

                                                                if (status == 200) {

                                                                    if (response.contains("ERROR")) {
                                                                        Gson gson = new Gson();
                                                                        ErrorMessageModel error = gson.fromJson(response, ErrorMessageModel.class);

                                                                        if (error.getCustMessage().contains("not registered"))
                                                                            throw new MeterNumberNotRegisteredException("Meter Number not registered. Please enter additional information");
                                                                        else
                                                                            throw new InvalidMeterNumberException(error.getCustMessage());

                                                                    } else {

                                                                        Gson gson = new Gson();
                                                                        MuniEkurhuleniVoucherModel voucher = gson.fromJson(response, MuniEkurhuleniVoucherModel.class);

                                                                        //Update Balance
                                                                        if (voucher.getBalance() != null) {
                                                                            if (!voucher.getBalance().isEmpty())
                                                                                tukishaApplication.setBalance(voucher.getBalance());

                                                                            Intent i = new Intent(UnipinDenominationActivity.this, ThankYouMuniEkurhuleniActivity.class);
                                                                            i.putExtra("vouchernumber", voucher.getTokenNumber());
                                                                            i.putExtra("distributor", voucher.getDistributer());
                                                                            i.putExtra("date", voucher.getDate());
                                                                            i.putExtra("energyKWh", voucher.getEnergyKWh());
                                                                            i.putExtra("amount", voucher.getAmount());
                                                                            i.putExtra("meterNumber", voucher.getMeterNumber());
                                                                            i.putExtra("sgc", voucher.getSGC());
                                                                            i.putExtra("krn", voucher.getKrn());
                                                                            i.putExtra("ti", voucher.getTI());
                                                                            i.putExtra("receiptNumber", voucher.getReceiptNumber());
                                                                            i.putExtra("description", voucher.getDescription());
                                                                            i.putExtra("balance", voucher.getBalance());
                                                                            i.putExtra("excise", voucher.getExcise());
                                                                            i.putExtra("actualReceipt", voucher.getActualReceipt());
                                                                            i.putExtra("header", "CREDIT VEND - TAX INVOICE");
                                                                            startActivity(i);
                                                                        } else {

                                                                            AlertDialog.Builder builder = new AlertDialog.Builder(UnipinDenominationActivity.this);

                                                                            builder
                                                                                    .setMessage("Something went wrong, please check if voucher was issued for " + model.getName() + "?")
                                                                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                                                        @Override
                                                                                        public void onClick(DialogInterface dialog, int id) {

                                                                                            Intent i = new Intent(UnipinDenominationActivity.this, TelcoTransactionHistoryActivity.class);
                                                                                            startActivity(i);
                                                                                        }
                                                                                    }).show();
                                                                        }
                                                                    }

                                                                }

                                                            } catch (final IOException e) {

                                                                //errorMessage.setText("Technical error occurred, either your connection is down or you ran out of data. " + e.getMessage());
                                                                progressDialog.dismiss();
                                                            } catch (final MeterNumberNotRegisteredException e) {

                                                                //errorMessage.setText(e.getMessage());
                                                                progressDialog.dismiss();

                                                            } catch (final InvalidMeterNumberException e) {

                                                                //errorMessage.setText(e.getMessage());
                                                                progressDialog.dismiss();

                                                            } catch (Exception e) {
                                                                //errorMessage.setText("Technical error occurred, either your connection is down or you ran out of data. " + e.getMessage());
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
                                                                    //errorMessage.setText("Technical error occurred " + e.getMessage());
                                                                }
                                                            } else if (error instanceof SocketTimeoutException) {
                                                                //errorMessage.setText("Connection timeout !");
                                                            } else {
                                                                //errorMessage.setText("Technical error occurred - " + error.getMessage());
                                                            }


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
                            });
                }
            }
        };

        mRecycler.setAdapter(mAdapter);
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


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return false;
    }

}
