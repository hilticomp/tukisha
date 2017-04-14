package com.imbyou.pavan.mykart;

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
 * Created by Lenovo on 13-Apr-17.
 */

public class FurnitureActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    AppSectionsPagerAdapter mAppSectionsPagerAdapter;
    static CharSequence[] furniture = new String[]{"Sofas","Beds","Dining", "Wardrobes", "Bookshelves"};
    static CharSequence[] electronics = new String[]{"Laptops","Mobiles","AC", "Refrigerator", "Printers"};
    static CharSequence[] grocey_staples = new String[]{"Pulses","Rice","Dry Fruits and Nuts", "Salt and Sugar", "Spices"};
    static CharSequence[] fruits_vegetables = new String[]{"All vegetables","All fruits","Exotics", "Imported Fruits", "Sprouts"};
    static CharSequence[] beauty_health = new String[]{"Fragrance","Health care devices","Mens grooming", "Diet and Nutrition", "Shaving and Hair Removal"};
    static CharSequence[] clothing_accessories = new String[]{"Womens Clothing","Mens Clothing","Kids clothing", "Footwear", "Watches"};
    static CharSequence[] toys_babyproducts = new String[]{"Soft Toys","Puzzles","Die Cast and Toy Vehicles", "Diapers", "Strollers and Prams"};
    static CharSequence[] books = new String[]{"Telugu","English","Hindi", "Marati", "Bengali"};


    private static String message;



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
        public Fragment getItem(int i) {
            switch (i) {
                case 0:
                    // The first section of the app is the most interesting -- it offers
                    // a launchpad into the other demonstrations in this example application.
                    return new FurnitureFragment("Sofas");
                case 1:
                    // The first section of the app is the most interesting -- it offers
                    // a launchpad into the other demonstrations in this example application.
                    return new FurnitureFragment("Beds");

                case 2:
                    // The first section of the app is the most interesting -- it offers
                    // a launchpad into the other demonstrations in this example application.
                    return new FurnitureFragment("Dining");

                case 3:
                    // The first section of the app is the most interesting -- it offers
                    // a launchpad into the other demonstrations in this example application.
                    return new FurnitureFragment("Wardrobes");

                case 4:
                    // The first section of the app is the most interesting -- it offers
                    // a launchpad into the other demonstrations in this example application.
                    return new FurnitureFragment("Bookshelves");




                default:

                    return null;
            }

        }



        @Override
        public int getCount() {
            return 5;
        }


        public CharSequence shopDataTabs(String name, int position){
            switch (name){

                //PAWAN IMPORTANT: Whatever the name you gave it in firebase the names here should be the same in this below cases.

                case "Furniture":

                    return furniture[position];


                case "Electronics":

                    return electronics[position];

                case "Grocery and Staples":

                    return grocey_staples[position];

                case "Fruits and Vegetables":

                    return fruits_vegetables[position];

                case "Beauty and Health":

                    return beauty_health[position];

                case "Clothing and Accessories":

                    return clothing_accessories[position];

                case "Toys and baby products":

                    return toys_babyproducts[position];

                case "Books":
                    return books[position];


                default:
                    return null;


            }


        }

        @Override
        public CharSequence getPageTitle(int position) {
            //return "Section " + (position + 1);

            return shopDataTabs(message, position);




           /* String title = null;
            if (position == 0)
            {
                title = "Sunday";
            }
            else if (position == 1)
            {
                title = "Monday";
            }
            else if (position == 2)
            {
                title = "Tuesday";
            }
            else if (position == 3)
            {
                title = "Wednesday";
            }
            else if (position == 4)
            {
                title = "Thursday";
            }
            else if (position == 5)
            {
                title = "Friday";
            }
            else if (position == 6)
            {
                title = "Satuarday";
            }
            return title;*/

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

