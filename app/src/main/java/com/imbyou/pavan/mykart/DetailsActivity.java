package com.imbyou.pavan.mykart;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.imbyou.pavan.mykart.model.CategoryTypesModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Lenovo on 18-Apr-17.
 */

public class DetailsActivity extends AppCompatActivity {
    /**
     * Called when the activity is first created.
     */

    private TextView mToolbarTitleTextView;
    private FirebaseDatabase database;
    private  int initialValue = 0;

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
        TextView removeItem = (TextView)findViewById(R.id.remove_item);
        TextView addItem = (TextView)findViewById(R.id.add_item);
        TextView itemAmount = (TextView)findViewById(R.id.item_amount);

        configureToolbar();

        if (mToolbarTitleTextView != null) {
            //Setting name for toolbar
            mToolbarTitleTextView.setText(sample.getName());
        }
        productDescription.setText(sample.getDescription());
        productName.setText(sample.getName());
        productRupees.setText(sample.getRs());
        itemAmount.setText(String.valueOf(initialValue));
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
}






