package com.artitech.tsalano.tukisha;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.artitech.tsalano.tukisha.errorhandling.BackendDownException;
import com.loopj.android.http.HttpGet;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.util.EntityUtils;

/**
 * Created by Tsheko on 01-June-17.
 */

public class MainTabProductActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private static UserCashBackTask mUserCashBackTask;
    AppSectionsPagerAdapter mAppSectionsPagerAdapter;
    private TextView mToolbarTitleTextView;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private NavigationView navigationView;
    private Button menuButton;
    private TukishaApplication tukishaApplication;


    /******************************************************************************************************/
    // Debugging
    private static final String TAG = "MainActivity";
    private static final boolean DEBUG = true;
    /******************************************************************************************************/

    /*******************************************************************************************************/
    // Key names received from the BluetoothService Handler

    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    private static final String CHINESE = "GBK";

    @Override
    public synchronized void onResume() {
        super.onResume();

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

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabs_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tukishaApplication = (TukishaApplication) getApplication();

        configureToolbar();

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mAppSectionsPagerAdapter);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);




        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        //NavigationView
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getHeaderView(0);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {

            Intent i = new Intent(MainTabProductActivity.this, LoginActivity.class);
            startActivity(i);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        menu.findItem(R.id.action_balance).setTitle(tukishaApplication.getBalance());

        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_scan) {

            Intent serverIntent = new Intent(MainTabProductActivity.this, DeviceListActivity.class);
            startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);

        } else if (id == R.id.nav_callcentre) {

            String phone = "+27861852853";
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
            startActivity(intent);

        } else if (id == R.id.nav_cash_mini_statement) {

            Intent i = new Intent(MainTabProductActivity.this, CashMiniStatementActivity.class);
            startActivity(i);

        } else if (id == R.id.nav_transactionHistory) {

            Intent i = new Intent(MainTabProductActivity.this, TelcoTransactionHistoryActivity.class);
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


    public static String getStartDateTimeString() {

        Date today = Calendar.getInstance().getTime();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        return formatter.format(today);

    }

    public static String getEndDateTimeString() {

        Calendar c = Calendar.getInstance();

        c.add(Calendar.DATE, 1);

        Date today = c.getInstance().getTime();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        return formatter.format(today);

    }

    public static class AppSectionsPagerAdapter extends FragmentPagerAdapter {

        public AppSectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(final int position) {

            switch (position) {
                case 0:
                    BuyActivity buy = new BuyActivity();
                    return buy;
               case 1:
                    PayFragment pay = new PayFragment();
                    return pay;
                /* case 2:
                    ProductsFragment product = new ProductsFragment();
                    return product;
                case 3:
                    RewardsFragment rewards = new RewardsFragment();
                    return rewards;
                case 4:
                    FragmentBets bets = new FragmentBets();
                    return bets;*/


                default:
                    return null;


            }
        }

        @Override
        public int getCount() {
            return 2;
        }


        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Buy";
                case 1:
                    return "Pay Account";
                /*case 2:
                    return "Products";
                case 3:
                    return "Rewards";
                case 4:
                    return "Bets";*/
            }
            return null;
        }

    }


    /**
     * Represents an asynchronous cash back task used
     * the user.
     */
    private class UserCashBackTask extends AsyncTask<Object, Object, Void> {

        private final String mAgentId;

        UserCashBackTask(String agentid) {
            mAgentId = agentid;
        }

        @Override
        protected Void doInBackground(Object... params) {


            HttpGet httpget = new HttpGet("https://munipoiapp.herokuapp.com/api/selfservice/cashback?customerid=" + mAgentId + "&startdate=" + getStartDateTimeString() + "&enddate="+ getEndDateTimeString());
            HttpClient httpclient = new DefaultHttpClient();

            HttpResponse response;
            try {

                response = httpclient.execute(httpget);

                int status = response.getStatusLine().getStatusCode();

                if (status == 200) {

                    HttpEntity entity = response.getEntity();

                    String responsedata = EntityUtils.toString(entity);

                    JSONObject responseAgent = new JSONObject(responsedata);

                    if (responseAgent.getString("statusCode").contains("failure"))
                        throw new Exception("Unable to refresh the balance.");


                } else {
                    if (status == 404) {
                        throw new BackendDownException("System is down, Please try again later or Call this number 078899999");
                    }
                }

                Log.d("Error", response.getStatusLine().toString());


            } catch (final Exception e) {

                return null;

            }
            return null;
        }

        @Override
        protected void onPostExecute(final Void success) {

        }

        @Override
        protected void onCancelled() {

        }

    }


}

