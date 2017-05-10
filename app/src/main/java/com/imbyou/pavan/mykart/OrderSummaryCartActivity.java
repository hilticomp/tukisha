package com.imbyou.pavan.mykart;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.imbyou.pavan.mykart.model.OrderSummaryModel;
import com.imbyou.pavan.mykart.viewholder.OrderSummaryViewHolder;
import com.twotoasters.jazzylistview.recyclerview.JazzyRecyclerViewScrollListener;

import java.util.ArrayList;

/**
 * Created by Lenovo on 19-Apr-17.
 */

public class OrderSummaryCartActivity extends AppCompatActivity {

    private TextView mToolbarTitleTextView;
    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;
    private RelativeLayout mRelativeLayout;

    private static String message;

    //private Switch mySwitch;

    private FirebaseDatabase database;


    private FirebaseRecyclerAdapter<OrderSummaryModel, OrderSummaryViewHolder> mAdapter;
    ArrayList<String> listTotal = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_summary);

       /* Bundle bundle = getIntent().getExtras();
        message = bundle.getString("from_main_activity");*/

        database = FirebaseDatabase.getInstance();

        configureToolbar();

        mRecycler = (RecyclerView) findViewById(R.id.cart_recyclerView);

       mRecycler.setHasFixedSize(true);
        mManager = new LinearLayoutManager(this);
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecycler.setLayoutManager(mManager);

        JazzyRecyclerViewScrollListener jazzyScrollListener = new JazzyRecyclerViewScrollListener();
        mRecycler.setOnScrollListener(jazzyScrollListener);
        jazzyScrollListener.setTransitionEffect(1);

        //PAWAN: Channge this numeric "2" to anything to change the animation for recyler view

        //jazzyScrollListener.setTransitionEffect(1);
        //jazzyScrollListener.setTransitionEffect(2);

        //jazzyScrollListener.setTransitionEffect(4);
        //jazzyScrollListener.setTransitionEffect(5);
        //jazzyScrollListener.setTransitionEffect(6);
        //jazzyScrollListener.setTransitionEffect(7);
        //jazzyScrollListener.setTransitionEffect(8);
        //jazzyScrollListener.setTransitionEffect(9);
        //jazzyScrollListener.setTransitionEffect(10);
        //jazzyScrollListener.setTransitionEffect(11);
        //jazzyScrollListener.setTransitionEffect(12);
        //jazzyScrollListener.setTransitionEffect(13);
        //jazzyScrollListener.setTransitionEffect(14);
        //jazzyScrollListener.setTransitionEffect(15);

        if (mToolbarTitleTextView != null) {
            //Setting name for toolbar
            mToolbarTitleTextView.setText("Order Summary");
        }


        /*Bundle bundle = getIntent().getExtras();
        String message = bundle.getString("from_workouts");*/


  /* PAWAN: Checking network connections while searching text, here we are calling "getMyConnections" method
        so check the internet connection here, this method is defines somewhere below in this same activity*/

        //getMyConnections(message);
        getMyCartList();

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_checkout, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection

        switch (item.getItemId()) {

            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.menu_checkout:

             /*PAWAN: Whenever you want to use the dialog box with checked list items Create a separate
                class as we did in this application like "WeekdayDialogFragment" and the use the
                    below two line to call that class in which ever class you want as we did in this class when the
                    menu item "+" was clicked in our project we needed that dialog so we are calling it on clicking it
                    by using the below two lines 162 and 163 */


                Intent in = new Intent(OrderSummaryCartActivity.this, CheckoutActivity.class);
                in.putStringArrayListExtra("from_order_summary_activity", listTotal);
                startActivity(in);
                /*Intent i = new Intent(OrderSummaryCartActivity.this, CheckoutActivity.class);
                startActivity(i);*/

            return  true;
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


    // String myKey;
    /*PAWAN: only the first three lines i.e,. the queries changes in getMyConnections() and searchConnection()
    methods, remaining everthing remains same, have a  close look */
    ArrayList<OrderSummaryModel> arrayModel = new ArrayList();

    private void getMyCartList() {

        // myKey = new Preferences().getUserKey(MyConnectionsActivity.this);
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("cart");
        Query postsQuery = myRef;
        mAdapter = new FirebaseRecyclerAdapter<OrderSummaryModel, OrderSummaryViewHolder>
                (OrderSummaryModel.class,
                        R.layout.item_order_summary,
                        OrderSummaryViewHolder.class, postsQuery) {
            @Override
            protected void populateViewHolder(OrderSummaryViewHolder viewHolder,
                                              final OrderSummaryModel model, int position) {
                //final DatabaseReference postRef = getRef(position);
                //inal String friend_key = postRef.getKey();

                //viewHolder.bindToResponse(MyConnectionsActivity.this, model);
        //arrayModel.add(model);
                if (model != null) {

                    String rupees = model.getRs().replace("Rs: ","");
                    int total = Integer.parseInt(model.getQuantity()) * Integer.parseInt(rupees);
                    listTotal.add(String.valueOf (total));

                    viewHolder.bindtoCartTypes(OrderSummaryCartActivity.this, model,
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    Toast.makeText(OrderSummaryCartActivity.this, "I am from order summary activity ",
                                            Toast.LENGTH_SHORT).show();

                                   /* Intent i = new Intent(WorkoutTypesActivity.this, DetailsActivity.class);
                                    i.putExtra("from_workouts_types", model.getVidurl());
                                    i.putExtra("from_workouts_types_description", model.getDescription());
                                    i.putExtra("from_workouts_types_name", model.getName());
                                    startActivity(i);*/



                                  /*  Intent i2 = new Intent(WorkoutTypesActivity.this, DetailsActivity.class);
                                    i2.putExtra("sampleObject", model);
                                    startActivity(i2);
*/
                                    //Creating the instance of PopupMenu
                                    //PopupMenu popup = new PopupMenu(WorkoutsActivity.this, view);
                                    //Inflating the Popup using xml file
                                 /*   popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

                                    //registering popup with OnMenuItemClickListener
                                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                        public boolean onMenuItemClick(MenuItem item) {

                                    *//*DatabaseReference my_NotfiRef = database.getReference("my_connections");
                                    my_NotfiRef.child(myKey).child(friend_key).removeValue();*//*


                                        }
                                    });
*/


                                    //return true;
                                    //popup.show();//showing popup menu

                                }
                            });
                }
            }
        };

        mRecycler.setAdapter(mAdapter);


        for(int i =0; i< arrayModel.size(); i++)
        {
            String rupees = arrayModel.get(i).getRs().replace("Rs: ","");
            int total = Integer.parseInt(arrayModel.get(i).getQuantity()) * Integer.parseInt(rupees);
            listTotal.add(String.valueOf (total));


        }
    }



}


