package com.artitech.tsalano.tukisha;

import android.content.Intent;
import android.net.Uri;
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
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tsheko on 01-June-17.
 */

public class MainTabProductsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    private static final String CHINESE = "GBK";
    static CharSequence[] vodacom = new String[]{"Airtime", "Data", "SMS"};
    static CharSequence[] mtn = new String[]{"Airtime", "Data", "SMS"};
    static CharSequence[] cellc = new String[]{"Airtime", "Data", "SMS"};
    static CharSequence[] telkom = new String[]{"Airtime", "Data", "SMS"};
    /* PAWAN IMPORTANT: Whatever the name you gave it in firebase the names here
                 should be the same in this below cases which are in green color.*/
    static CharSequence[] virginmobile = new String[]{"Airtime", "Data", "SMS"};
    static CharSequence[] neotel = new String[]{"Airtime", "Data", "SMS"};
    private static String message;
    AppSectionsPagerAdapter mAppSectionsPagerAdapter;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabs_main);

        Bundle bundle = getIntent().getExtras();
        message = bundle.getString("from_main_activity");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        //setupViewPager(viewPager);
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
        View header = navigationView.getHeaderView(0);

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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_scan) {

            Intent serverIntent = new Intent(MainTabProductsActivity.this, DeviceListActivity.class);
            startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);

        } else if (id == R.id.nav_callcentre) {

            String phone = "+27861852853";
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
            startActivity(intent);

        } else if (id == R.id.nav_cash_mini_statement) {

            Intent i = new Intent(MainTabProductsActivity.this, CashMiniStatementActivity.class);
            startActivity(i);

        } else if (id == R.id.nav_transactionHistory) {

            Intent i = new Intent(MainTabProductsActivity.this, TransactionHistoryActivity.class);
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

    public static class AppSectionsPagerAdapter extends FragmentPagerAdapter {

        public AppSectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(final int i) {

            return new AirtimeFragment(message, shopDataTabs(message, i).toString());

        }

        @Override
        public int getCount() {
            return 3;
        }


        public CharSequence shopDataTabs(String name, int position) {
            switch (name) {

               /* PAWAN IMPORTANT: Whatever the name you gave it in firebase the names here
                 should be the same in this below cases which are in green color.*/

                case "Buy":

                    //BuyActivity buy = new BuyActivity();
                    //return buy;

                case "Pay Account":

                    //PayActivity pay = new PayActivity();
                    //return pay;

                case "Products":

                    //RewardsActivity rewards = new RewardsActivity();
                    //return rewards;

                case "Rewards":

                    //ProductsActivity product = new ProductsActivity();
                    //return product;


                default:
                    return null;


            }


        }

        @Override
        public CharSequence getPageTitle(int position) {

            return shopDataTabs(message, position);


        }


    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {


            return mFragmentList.get(position);

        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}

