package com.artitech.tsalano.tukisha;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

/**
 * Created by Tsheko on 13-Apr-17.
 */

public class ElectricityMunicipalityActivity extends AppCompatActivity {

    static CharSequence[] electricity = new String[]{"Purchase Token", "Redeem FBE", "Re-Print Receipt"};
    AppSectionsPagerAdapter mAppSectionsPagerAdapter;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TextView mToolbarTitleTextView;

    private int prevend,tokennumber;
    private String municipalityname, endpoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabs_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        configureToolbar();

        viewPager = (ViewPager) findViewById(R.id.viewpager);

        mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mAppSectionsPagerAdapter);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            prevend = bundle.getInt("prevend");
            tokennumber = bundle.getInt("tokennumber");
            municipalityname = bundle.getString("name");
            endpoint = bundle.getString("endpoint");
        }

        if (mToolbarTitleTextView != null) {
            //Setting name for toolbar
            mToolbarTitleTextView.setText(municipalityname);
        }

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

    public class AppSectionsPagerAdapter extends FragmentPagerAdapter {

        public AppSectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {

            switch (i) {
                case 0:
                    return new PurchaseCreditNoteMunicipalityFragment(prevend,tokennumber,endpoint);
                case 1:
                    return new IssueFBEMunicipalityFragment(prevend,tokennumber);

                case 2:
                    return new Re_PrintMunicipalityFragment(prevend,tokennumber);


                default:

                    return null;
            }

        }

        @Override
        public int getCount() {
            return 3;
        }


        public CharSequence electricityTabs(int position){
            return electricity[position];

        }

        @Override
        public CharSequence getPageTitle(int position) {

            return electricityTabs(position);

        }


    }

}

