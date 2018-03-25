package com.defianttechnology.al.whatsplaying;

import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.defianttechnology.al.whatsplaying.ChangeDate;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Movie> comingSoonList;
    private ArrayList<Movie> inTheatresList;
    RESTinterface grabJSON;
    RESTinterface grabInTheatreJSON;;
    String comingSoon;
    URL comingSoonConnectionString = null;
    URL inTheatresConnectionString = null;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        //Get Dates for connection strings
        final int COMING_SOON_DATE = 14;   //Movies coming in the next two weeks
        final int IN_THEATRES_DATE = -30;  //Include movies from last month
        ChangeDate newDate = new ChangeDate();
        String comingSoon = newDate.changeDate(COMING_SOON_DATE);
        String inTheatres = newDate.changeDate(IN_THEATRES_DATE);

        // Build connection strings

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat yearFormat = new SimpleDateFormat("yyyy");
        Date date = new Date();
        String todaysDate = dateFormat.format(date);
        String thisYear = yearFormat.format(date);
        try {
            comingSoonConnectionString = new URL("https://api.themoviedb.org/3/discover/movie?api_key=9c56db095c05fca088971352f6b6c1e5&language=en-US&region=us" +
                    "&sort_by=popularity.desc&include_adult=false&include_video=false&page=1&with_original_language=en" +
                    "&primary_release_year=" + thisYear +
                    "&release_date.gte=" + todaysDate +
                    "&release_date.lte=" + comingSoon);
            inTheatresConnectionString = new URL ("https://api.themoviedb.org/3/discover/movie?api_key=9c56db095c05fca088971352f6b6c1e5&language=en-US&region=us" +
                    "&sort_by=popularity.desc&include_adult=false&include_video=false&page=1&with_original_language=en" +
                    "&primary_release_year=" + thisYear +
                    "&release_date.gte=" + inTheatres +
                    "&release_date.lte=" + todaysDate);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        // Send connection strings to RESTinterface and get back movies
        grabJSON = new RESTinterface(this);
        grabInTheatreJSON = new RESTinterface(this);
        comingSoonList = new ArrayList<>();
        inTheatresList = new ArrayList<>();

        //Grab the data
        new MyTask().execute();
    }

    //Run search in background
    class MyTask extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                comingSoonList = grabJSON.connect(comingSoonConnectionString);
                inTheatresList = grabInTheatreJSON.connect(inTheatresConnectionString);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            Log.d("AlTag", comingSoonList.toString());
            Log.d("AlTag", inTheatresList.toString());
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static MainActivity.PlaceholderFragment newInstance(int sectionNumber) {
            MainActivity.PlaceholderFragment fragment = new MainActivity.PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.tab1, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    Tab1 tab1 = new Tab1();
                    return tab1;
                case 1:
                    Tab2 tab2 = new Tab2();
                    return tab2;
            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }
    }

}


































