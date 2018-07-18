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

/**
 * Created by Tsheko on 13-Apr-17.
 */

public class ElectricityActivity extends AppCompatActivity {

    static CharSequence[] electricity = new String[]{"Purchase Token", "Redeem FBE", "Blind Vend", "Issue Advise", "Re-Print Receipt","Key Change"};
    private static String message;
    AppSectionsPagerAdapter mAppSectionsPagerAdapter;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabs_main);

        message ="from_main_activity";

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        //setupViewPager(viewPager);
        mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mAppSectionsPagerAdapter);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

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

    public static class AppSectionsPagerAdapter extends FragmentPagerAdapter {

        public AppSectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {

            switch (i) {
                case 0:
                    return new PurchaseCreditNoteFragment();
                case 1:
                    return new IssueFBEFragment();

                case 2:
                    return new BlindVendFragment();

                case 3:
                    return new IssueAdviceFragment();

                case 4:
                    return new Re_PrintFragment();

                case 5:
                    return new MeterChanger();

                default:

                    return null;
            }

        }

        @Override
        public int getCount() {
            return 6;
        }

        /*public CharSequence getSubCategories(int position){


        }*/


        public CharSequence shopDataTabs(String name, int position){
            return electricity[position];


        }

        @Override
        public CharSequence getPageTitle(int position) {
            //return "Section " + (position + 1);

            return shopDataTabs(message, position);


        }


    }

}

