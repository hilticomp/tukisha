package com.artitech.tsalano.tukisha;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.artitech.tsalano.tukisha.errorhandling.BackendDownException;
import com.artitech.tsalano.tukisha.model.AirtimeVoucher;
import com.artitech.tsalano.tukisha.model.CategoryTypesModel;
import com.artitech.tsalano.tukisha.viewholder.CategoryTypesViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.twotoasters.jazzylistview.recyclerview.JazzyRecyclerViewScrollListener;

import java.net.SocketTimeoutException;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Tsheko on 01-June-17.
 */

@SuppressLint("ValidFragment")
public class AirtimeFragment extends Fragment {

    String imbyou;
    String category;
    private RecyclerView mRecycler;

    //private Preferences mPreferences;
    private LinearLayoutManager mManager;
    private TukishaApplication tukishaApplication;
    private FirebaseDatabase database;
    private FirebaseRecyclerAdapter<CategoryTypesModel, CategoryTypesViewHolder> mAdapter;


    @SuppressLint("ValidFragment")
    public AirtimeFragment(String category, String data) {
        // Required empty public constructor
        this.category = category;
        this.imbyou = data;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_airtime, container, false);

        mRecycler = (RecyclerView) view.findViewById(R.id.invite_frag_recyclerView);
        database = FirebaseDatabase.getInstance();
        //mPreferences = new Preferences();

        mRecycler.setHasFixedSize(true);
        mManager = new LinearLayoutManager(getActivity());
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecycler.setLayoutManager(mManager);

        tukishaApplication = (TukishaApplication) getActivity().getApplication();


        /* PAWAN: If you want to add animation to the recycler view just add these below three lines
        in whichever activity you want which has a recycler view, after importing the library.*/

        JazzyRecyclerViewScrollListener jazzyScrollListener = new JazzyRecyclerViewScrollListener();
        mRecycler.setOnScrollListener(jazzyScrollListener);

        //PAWAN: Channge this numeric "2" to anything to change the animation for recyler view

        jazzyScrollListener.setTransitionEffect(1);

        // Inflate the layout for this fragment
        return view;


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.d("onActivityCreated: ", "onActivityCreated: " + imbyou);
        getMyCategories(imbyou);
    }

    private void getMyCategories(final String s) {

        // myKey = new Preferences().getUserKey(MyConnectionsActivity.this);
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("category_types");
        Query postsQuery = myRef.child(category).child(s);
        //Query postsQuery = myRef.Child(imbyou);
        mAdapter = new FirebaseRecyclerAdapter<CategoryTypesModel, CategoryTypesViewHolder>
                (CategoryTypesModel.class,
                        R.layout.item_product,
                        CategoryTypesViewHolder.class, postsQuery) {
            @Override
            protected void populateViewHolder(CategoryTypesViewHolder viewHolder,
                                              final CategoryTypesModel model, int position) {
                //final DatabaseReference postRef = getRef(position);
                //inal String friend_key = postRef.getKey();

                //viewHolder.bindToResponse(MyConnectionsActivity.this, model);

                if (model != null) {
                    viewHolder.bindtoCategoriesTypes(getActivity(), model,
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

                                    builder
                                            .setMessage("Are you sure want to buy " + category + " for " + model.getName() + "?")
                                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int id) {
                                                    // Yes-code

                                                    if (s.equals("Data") || s.equals("SMS")) {

                                                        Intent i = new Intent(getActivity(), TopUpDataActivity.class);
                                                        i.putExtra("productcode", model.getRs().toString());
                                                        i.putExtra("agentid", tukishaApplication.getAgentID());
                                                        i.putExtra("producttype", s);
                                                        startActivity(i);

                                                    } else {

                                                        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
                                                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                                        progressDialog.setMessage("Printing voucher, please wait...");
                                                        progressDialog.setIndeterminate(true);
                                                        progressDialog.setCanceledOnTouchOutside(false);
                                                        progressDialog.show();

                                                        AsyncHttpClient client = new AsyncHttpClient();
                                                        client.setTimeout(20000);
                                                        client.get("http://munipoiapp.herokuapp.com/api/app/airtime?productcode=" + model.getRs().toString() + "&agentid=" + tukishaApplication.getAgentID(), new AsyncHttpResponseHandler() {
                                                            @Override
                                                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                                                try {

                                                                    String response = responseBody == null ? null : new String(
                                                                            responseBody, getCharset());

                                                                    Gson gson = new Gson();
                                                                    AirtimeVoucher voucher = gson.fromJson(response, AirtimeVoucher.class);
                                                                    Log.d("airtime: ", voucher.getVoucher());

                                                                    progressDialog.dismiss();

                                                                    if (voucher.getBalance() != null) {
                                                                        if (!voucher.getBalance().isEmpty())
                                                                            tukishaApplication.setBalance(voucher.getBalance());


                                                                        Intent i = new Intent(getActivity(), AirtimeThankYouActivity.class);
                                                                        i.putExtra("vouchernumber", voucher.getVoucher());
                                                                        i.putExtra("operator", voucher.getOperator());
                                                                        i.putExtra("date", voucher.getDate());
                                                                        i.putExtra("amount", voucher.getAmount());
                                                                        i.putExtra("instructions", voucher.getInstructions());
                                                                        i.putExtra("balance", voucher.getBalance());
                                                                        startActivity(i);
                                                                    } else {

                                                                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                                                                        builder
                                                                                .setMessage("Something went wrong, please check if voucher was issued for " + category + " for " + model.getName() + "?")
                                                                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                                                    @Override
                                                                                    public void onClick(DialogInterface dialog, int id) {

                                                                                        Intent i = new Intent(getActivity(), TransactionHistoryActivity.class);
                                                                                        startActivity(i);
                                                                                    }
                                                                                }).show();
                                                                    }

                                                                } catch (Exception e) {
                                                                    Log.d("airtime: ", e.getMessage());
                                                                }
                                                            }

                                                            @Override
                                                            public void onFailure(int status, Header[] headers, byte[] responseBody, Throwable error) {

                                                                if (status == 404) {
                                                                    try {
                                                                        throw new BackendDownException("System is down, Please try again later or Call this number 078 377 6207");
                                                                    } catch (BackendDownException e) {


                                                                    }
                                                                } else if (error instanceof SocketTimeoutException) {

                                                                    progressDialog.dismiss();

                                                                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                                                                    builder
                                                                            .setMessage("System is taking too long to process your request, please check if voucher was issued for " + category + " for " + model.getName() + "?")
                                                                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                                                @Override
                                                                                public void onClick(DialogInterface dialog, int id) {

                                                                                    Intent i = new Intent(getActivity(), TransactionHistoryActivity.class);
                                                                                    startActivity(i);
                                                                                }
                                                                            }).show();

                                                                } else {

                                                                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                                                                    builder
                                                                            .setMessage("System is taking too long to process your request, please check if voucher was issued " + category + " for " + model.getName() + "?")
                                                                            .setPositiveButton("Technical error message", new DialogInterface.OnClickListener() {
                                                                                @Override
                                                                                public void onClick(DialogInterface dialog, int id) {
                                                                                    Intent i = new Intent(getActivity(), TransactionHistoryActivity.class);
                                                                                    startActivity(i);
                                                                                }
                                                                            });

                                                                }
                                                            }


                                                        });

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
}





