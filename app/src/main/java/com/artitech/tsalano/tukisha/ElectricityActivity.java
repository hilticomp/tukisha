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
 * Created by Tsheko on 13-Apr-17.
 */

public class ElectricityActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    AppSectionsPagerAdapter mAppSectionsPagerAdapter;

    static CharSequence[] electricity = new String[]{"Purchase Token","Redeem FBE","Blind Vend","Re-Print Receipt"};

    private static String message;

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
                    return new Re_PrintFragment();

                default:

                    return null;
            }

        }

        @Override
        public int getCount() {
            return 4;
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

    /*private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new SundayFragment(), "Sunday");
        adapter.addFrag(new MondayFragment(), "Monday");
        adapter.addFrag(new TuesdayFragment(), "Tuesday");
        adapter.addFrag(new WednesdayFragment(), "Wednesday");
        adapter.addFrag(new ThursdayFragment(), "Thursday");
        adapter.addFrag(new FridayFragment(), "Friday");
        adapter.addFrag(new SatuardayFragment(), "Satuarday");



        viewPager.setAdapter(adapter);
    }*/

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

