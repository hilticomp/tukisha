package com.artitech.tsalano.tukisha;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.artitech.tsalano.tukisha.model.CategoryTypesModel;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

/**
 * Created by Tsheko on 18-Apr-17.
 */

public class DetailsActivity extends AppCompatActivity{
    /**
     * Called when the activity is first created.
     */

    private TextView mToolbarTitleTextView;
    private FirebaseDatabase database;
    private  int initialValue = 0;
    TextView itemAmount;
    String key;

    CategoryTypesModel sample;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        database = FirebaseDatabase.getInstance();

        Intent i = getIntent();
        sample = (CategoryTypesModel) i.getSerializableExtra("sampleObject");

       /* Bundle bundle = getIntent().getExtras();

        final String message = bundle.getString("from_workouts_types");
        workoutMessage = bundle.getString("from_workouts_types_description");
        String detailsPageName = bundle.getString("from_workouts_types_name");*/



        //Intent i = getIntent();
        //sample = (WorkoutTypesModel) i.getSerializableExtra("sampleObject");

        ImageView productImage = (ImageView)findViewById(R.id.product_image);
        TextView productName = (TextView)findViewById(R.id.product_name);
        TextView productDescription = (TextView) findViewById(R.id.product_description);
        TextView productRupees = (TextView)findViewById(R.id.product_rupees);
        itemAmount = (TextView)findViewById(R.id.item_amount);


        configureToolbar();

        if (mToolbarTitleTextView != null) {
            //Setting name for toolbar
            mToolbarTitleTextView.setText(sample.getName());
        }
        productDescription.setText(sample.getDescription());
        productName.setText(sample.getName());
        productRupees.setText(sample.getRs());
        itemAmount.setText(String.valueOf(initialValue));

        //Picasso.with(DetailsActivity.this).setLoggingEnabled(true);

        Picasso.with(DetailsActivity.this).load(sample.getImgurl()).fit().centerCrop().into(productImage);


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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cart, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection

        switch (item.getItemId()) {

            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.menu_add:

             /*PAWAN: Whenever you want to use the dialog box with checked list items Create a separate
                class as we did in this application like "WeekdayDialogFragment" and the use the
                    below two line to call that class in which ever class you want as we did in this class when the
                    menu item "+" was clicked in our project we needed that dialog so we are calling it on clicking it
                    by using the below two lines 162 and 163 */

                Intent i = new Intent(this, OrderSummaryCartActivity.class);
                //i.putExtra("from_details_activity", sample.getName());
                startActivity(i);


                return true;



            default:
                return super.onOptionsItemSelected(item);

        }


    }
    /*@Override
    public void onYesPressed() {
        //Updates deviceId if some other phone its logged in
        HashMap<String, Object> result = new HashMap<>();
        result.put("quantity", quantity);
        database.getReference().child("cart").child(key).updateChildren(result);
        //getUserDetails(userKey);

    }*/

}





