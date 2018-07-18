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

import com.artitech.tsalano.tukisha.model.MunicipalityModel;
import com.artitech.tsalano.tukisha.viewholder.CategoryTypesViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.twotoasters.jazzylistview.recyclerview.JazzyRecyclerViewScrollListener;

/**
 * Created by solly on 2017/09/30.
 */

public class MunicipalityActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    /******************************************************************************************************/
    // Debugging
    private static final String TAG = "MunicipalityActivity";
    /*******************************************************************************************************/
    // Key names received from the BluetoothService Handler

    private TukishaApplication tukishaApplication;
    private RecyclerView mRecycler;
    private TextView mToolbarTitleTextView;
    /******************************************************************************************************/
    private LinearLayoutManager mManager;
    private ProgressDialog progressDialog;
    private NavigationView navigationView;
    private FirebaseDatabase database;
    private FirebaseRecyclerAdapter<MunicipalityModel, CategoryTypesViewHolder> mAdapter;
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

        getMunicipalities();

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

    private void getMunicipalities() {

        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Selected_Municipality");
        Query postsQuery = myRef;
        mAdapter = new FirebaseRecyclerAdapter<MunicipalityModel, CategoryTypesViewHolder>
                (MunicipalityModel.class,
                        R.layout.item_product,
                        CategoryTypesViewHolder.class, postsQuery) {
            @Override
            protected void populateViewHolder(CategoryTypesViewHolder viewHolder,
                                              final MunicipalityModel model, int position) {

                if (model != null) {
                    viewHolder.bindtoSelectedMunicipalities(context, model,
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {


                                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

                                    builder
                                            .setMessage("Are you sure want to buy for " + model.getName() + "?")
                                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int id) {
                                                    // Yes-code

                                                    if(model.getName().contains("Unipin"))
                                                    {
                                                        Intent i = new Intent(MunicipalityActivity.this, UnipinDenominationActivity.class);
                                                        i.putExtra("prevend", model.getPreVend());
                                                        i.putExtra("tokennumber", model.getTokenMeter());
                                                        i.putExtra("name", model.getName());
                                                        i.putExtra("endpoint", model.getEndPoint());
                                                        startActivity(i);

                                                    } else {

                                                        Intent i = new Intent(MunicipalityActivity.this, ElectricityMunicipalityActivity.class);
                                                        i.putExtra("prevend", model.getPreVend());
                                                        i.putExtra("tokennumber", model.getTokenMeter());
                                                        i.putExtra("name", model.getName());
                                                        i.putExtra("endpoint", model.getEndPoint());
                                                        startActivity(i);
                                                    }


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
