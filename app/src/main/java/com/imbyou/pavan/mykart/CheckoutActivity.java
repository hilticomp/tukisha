package com.imbyou.pavan.mykart;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Lenovo on 21-Apr-17.
 */

public class CheckoutActivity extends AppCompatActivity {

    private TextView mToolbarTitleTextView;
    private Toolbar toolbar;

    /*Here i am having all the items "Total" of our order summary activity in "pawan" and i am setting
    it to the text view of "final total" text view in this activity*/

    private static ArrayList<String> pawan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            pawan = bundle.getStringArrayList("from_order_summary_activity");
        }

        TextView finalItemsTotal = (TextView)findViewById(R.id.finaltotal);


        int total = 0;

        for(int i =0; i< pawan.size(); i++)
        {

            total = total+Integer.parseInt(pawan.get(i));
        }
                //TODO
        finalItemsTotal.setText("Grand Total :" + " " + String.valueOf(total));


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        configureToolbar();

        if (mToolbarTitleTextView != null) {
            //Setting name for toolbar
            mToolbarTitleTextView.setText("Totol Amount");
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_logout, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection

        switch (item.getItemId()) {

            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.menu_signout:


                Intent i = new Intent(CheckoutActivity.this, ThankYouActivity.class);
                startActivity(i);
               // Toast.makeText(CheckoutActivity.this, "Logged Out", Toast.LENGTH_LONG).show();
                return true;


            default:
                return super.onOptionsItemSelected(item);

        }


    }


}

