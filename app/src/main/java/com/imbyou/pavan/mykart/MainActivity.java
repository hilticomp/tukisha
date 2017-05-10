package com.imbyou.pavan.mykart;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.imbyou.pavan.mykart.model.CategoriesModel;
import com.imbyou.pavan.mykart.viewholder.CategoriesViewHolder;
import com.twotoasters.jazzylistview.recyclerview.JazzyRecyclerViewScrollListener;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TextView mToolbarTitleTextView;
    private RecyclerView mRecycler;
    private GridLayoutManager mManager;
    //private TextView mNavUserNameTextView, mNavUserEmailTextView;
    private RelativeLayout mRelativeLayout;
   // private Preferences mPreferences;
    //private CircleImageView mProfileImage;
    private ProgressDialog progressDialog;
    private NavigationView navigationView;
    //private Switch mySwitch;

    private FirebaseDatabase database;
    private FirebaseRecyclerAdapter<CategoriesModel, CategoriesViewHolder> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);




        database = FirebaseDatabase.getInstance();

        configureToolbar();

        mRecycler = (RecyclerView) findViewById(R.id.invite_recyclerView);
        //NavigationView
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        //mNavUserNameTextView = (TextView) header.findViewById(R.id.user_name_textView);
        //mNavUserEmailTextView = (TextView) header.findViewById(R.id.user_email_textView);
        //mProfileImage = (CircleImageView) header.findViewById(R.id.profile_image);

        mRecycler.setHasFixedSize(true);
        mManager = new GridLayoutManager(this, 2);
        mManager.setReverseLayout(true);
        //mManager.setStackFromEnd(true);
        mRecycler.setLayoutManager(mManager);

       /* PAWAN: If you want to add animation to the recycler view just add these below three lines
        in which ever activity you want which has a recycler view after importing the library.*/

        JazzyRecyclerViewScrollListener jazzyScrollListener = new JazzyRecyclerViewScrollListener();
        mRecycler.setOnScrollListener(jazzyScrollListener);
        jazzyScrollListener.setTransitionEffect(11);
        //PAWAN: Channge this numeric "2" to anything to change the animation for recyler view
        //jazzyScrollListener.setTransitionEffect(2);
        //jazzyScrollListener.setTransitionEffect(1);
        //jazzyScrollListener.setTransitionEffect(3);
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

        //Progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.please_wait));

        /*//Creating Preferences object
        mPreferences = new Preferences();
        showUserDetails();*/

        if (mToolbarTitleTextView != null) {
            //Setting name for toolbar
            mToolbarTitleTextView.setText("Categories");
        }

  /* PAWAN: Checking network connections while searching text, here we are calling "getMyConnections" method
        so check the internet connection here, this method is defines somewhere below in this same activity*/



       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
*/
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        getMyCategories();




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

    private void getMyCategories() {

        // myKey = new Preferences().getUserKey(MyConnectionsActivity.this);
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("categories");
        Query postsQuery = myRef;
        mAdapter = new FirebaseRecyclerAdapter<CategoriesModel, CategoriesViewHolder>
                (CategoriesModel.class,
                        R.layout.item_grid_categories,
                        CategoriesViewHolder.class, postsQuery) {
            @Override
            protected void populateViewHolder(CategoriesViewHolder viewHolder,
                                              final CategoriesModel model, int position) {
                //final DatabaseReference postRef = getRef(position);
                //inal String friend_key = postRef.getKey();

                //viewHolder.bindToResponse(MyConnectionsActivity.this, model);

                if (model != null) {
                    viewHolder.bindToWorkouts(MainActivity.this, model,
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                     /*Toast.makeText(WorkoutsActivity.this, "You Clicked : ",
                                            Toast.LENGTH_SHORT).show();*/

                                  /*  PAWAN: If you click abs to need to get abs exercise, similiarly if you
                                    click tricep to need to get tricep and so on, in order to achieve this
                                            you need to send the name like "model.getName()" from this activity
                                            and receive it in "WorkoutTypesActivity" and pass that received intent
                                            in query of firebase line number 96*/

                                    /*Intent i = new Intent(MainActivity.this, CategoriesTypesActivity.class);
                                    i.putExtra("from_workouts", model.getName());

                                    startActivity(i);*/

                                    Intent i = new Intent(MainActivity.this, FurnitureActivity.class);
                                    i.putExtra("from_main_activity", model.getName());
                                    startActivity(i);




                                }
                            });
                }
            }
        };

        mRecycler.setAdapter(mAdapter);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBody = "Here is the share content body";
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Share via"));

        } else if (id == R.id.nav_send) {

            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
            sendIntent.setType("text/plain");
            startActivity(sendIntent);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
