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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tsheko on 01-June-17.
 */

public class AirtimeActivity extends AppCompatActivity {

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

                case "Vodacom":

                    return vodacom[position];

                case "MTN":

                    return mtn[position];

                case "Cell C":

                    return cellc[position];

                case "Virgin Mobile":

                    return virginmobile[position];

                case "Telkom Mobile":

                    return telkom[position];

                case "Neotel":

                    return neotel[position];

                case "Other":

                    return neotel[position];


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

