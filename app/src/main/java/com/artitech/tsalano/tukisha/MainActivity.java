package com.artitech.tsalano.tukisha;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.artitech.tsalano.tukisha.model.CategoriesModel;
import com.artitech.tsalano.tukisha.printer.PrinterCommand;
import com.artitech.tsalano.tukisha.viewholder.CategoriesViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.twotoasters.jazzylistview.recyclerview.JazzyRecyclerViewScrollListener;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String MyAgentPref = "AgentPrefs";
    public static final String agentID = "agentID";
    public static final String currentBalance = "balance";
    /******************************************************************************************************/
    // Debugging
    private static final String TAG = "MainActivity";
    private static final boolean DEBUG = true;
    /*******************************************************************************************************/
    // Key names received from the BluetoothService Handler

    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    private static final String CHINESE = "GBK";
    private static String balance, agentid;
    TukishaApplication tukishaApplication;
    private TextView mToolbarTitleTextView;
    private RecyclerView mRecycler;
    /******************************************************************************************************/
    private GridLayoutManager mManager;
    private SharedPreferences mPreferences;
    //private CircleImageView mProfileImage;
    private ProgressDialog progressDialog;
    private NavigationView navigationView;
    private FirebaseDatabase database;
    private FirebaseRecyclerAdapter<CategoriesModel, CategoriesViewHolder> mAdapter;

    @Override
    protected void onStop() {
        super.onStop();

        if (DEBUG)
            Log.e(TAG, "-- ON STOP --");
    }

    @Override
    protected void onStart() {
        super.onStart();

        // If Bluetooth is not on, request that it be enabled.
        if(tukishaApplication.mBluetoothAdapter == null)
        {
            Intent enableIntent = new Intent(
                    BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }
        else if (!tukishaApplication.mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(
                    BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            // Otherwise, setup the session
        } else {
            if (tukishaApplication.mService == null)
                tukishaApplication.setupBluetoothSession();
        }
    }

    @Override
    public synchronized void onResume() {
        super.onResume();

        if (tukishaApplication.mService != null) {

            if (tukishaApplication.mService.getState() == BluetoothService.STATE_NONE) {
                // Start the Bluetooth services
                tukishaApplication.mService.start();
            }
        }
    }

    @Override
    public synchronized void onPause() {
        super.onPause();
        if (DEBUG)
            Log.e(TAG, "- ON PAUSE -");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Stop the Bluetooth services
        if (tukishaApplication.mService != null)
            tukishaApplication.mService.stop();
        if (DEBUG)
            Log.e(TAG, "--- ON DESTROY ---");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tukishaApplication = (TukishaApplication) getApplication();

        // Get local Bluetooth adapter
        tukishaApplication.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // If the adapter is null, then Bluetooth is not supported
        if (tukishaApplication.mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available",
                    Toast.LENGTH_LONG).show();
            //finish();
        }


        database = FirebaseDatabase.getInstance();

        configureToolbar();


        Bundle bundle = getIntent().getExtras();
        if(bundle!=null) {
            balance = bundle.getString("balance");
            agentid = bundle.getString("agentid");

            tukishaApplication.setAgentID(agentid);
            tukishaApplication.setBalance(balance);
        }

        mRecycler = (RecyclerView) findViewById(R.id.invite_recyclerView);
        //NavigationView
        navigationView = (NavigationView) findViewById(R.id.nav_view);


        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);


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


        //Progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.please_wait));

        /*//Creating Preferences object
        mPreferences = new Preferences();
        showUserDetails();*/

        if (mToolbarTitleTextView != null) {
            //Setting name for toolbar
            mToolbarTitleTextView.setText("Buy");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.getMenu().findItem(R.id.nav_balance).setTitle("Current Balance is "+ balance);

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


                                    if (model.getName().equals("Eskom"))
                                    {
                                        Intent i = new Intent(MainActivity.this, ElectricityActivity.class);
                                        startActivity(i);
                                    }
                                    else {

                                        Intent i = new Intent(MainActivity.this, AirtimeActivity.class);
                                        i.putExtra("from_main_activity", model.getName());
                                        startActivity(i);
                                    }


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

        Intent i = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        menu.findItem(R.id.action_balance).setTitle(tukishaApplication.getBalance());

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {

            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(i);

            return true;
        }else if (id == R.id.action_printertest) {

           BluetoothPrintTest();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_scan) {

            Intent serverIntent = new Intent(MainActivity.this, DeviceListActivity.class);
            startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);

        } else if (id == R.id.nav_callcentre) {

            String phone = "+27861852853";
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
            startActivity(intent);

        } else if (id == R.id.nav_cash_mini_statement) {

            Intent i = new Intent(MainActivity.this, CashMiniStatementActivity.class);
            startActivity(i);

        } else if (id == R.id.nav_transactionHistory) {

            Intent i = new Intent(MainActivity.this, TransactionHistoryActivity.class);
            startActivity(i);

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (DEBUG)
            Log.d(TAG, "onActivityResult " + resultCode);
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE:{
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    // Get the device MAC address
                    String address = data.getExtras().getString(
                            DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                    // Get the BLuetoothDevice object
                    if (BluetoothAdapter.checkBluetoothAddress(address)) {
                        BluetoothDevice device = tukishaApplication.mBluetoothAdapter
                                .getRemoteDevice(address);
                        // Attempt to connect to the device
                        tukishaApplication.mService.connect(device);
                    }
                }
                break;
            }
            case REQUEST_ENABLE_BT:{
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    // Bluetooth is now enabled, so set up a session
                    tukishaApplication.setupBluetoothSession();
                } else {
                    // User did not enable Bluetooth or an error occured
                    Log.d(TAG, "BT not enabled");
                    Toast.makeText(this, R.string.bt_not_enabled_leaving,
                            Toast.LENGTH_SHORT).show();
                    //finish();
                }
                break;
            }

        }
    }

    private void BluetoothPrintTest(){

        tukishaApplication.SendDataByte(PrinterCommand.POS_Print_Text("Congratulations!\n\n", CHINESE, 0, 1, 1, 0));

        tukishaApplication.SendDataByte(PrinterCommand.POS_Print_Text("\n\n\nYou have sucessfully created communications between your device and our bluetooth printer.\n\n\n\n\n\n", CHINESE, 0, 0, 0, 0));
    }

}
