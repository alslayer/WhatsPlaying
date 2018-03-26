package com.defianttechnology.al.whatsplaying;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by Al on 3/25/2018.
 */

public class Tab1 extends android.support.v4.app.Fragment {

    private ListView mainListView ;
    private ArrayAdapter<String> listAdapter ;

    ArrayList<Movie> comingSoonList;
    ArrayList<Movie> inTheatresList;
    RESTinterface grabJSON;
    RESTinterface grabInTheatreJSON;;
    String comingSoon;
    URL comingSoonConnectionString = null;
    URL inTheatresConnectionString = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab1, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        mainListView = getView().findViewById(R.id.movieList);

        //Grab the data
        new MyTask(mainListView).execute();
    }

    @Override
    public void onResume() {
        super.onResume();

//        mainListView = getView().findViewById(R.id.movieList);

    }

    public void fillList(ArrayList<Movie> comingSoonList, Context context, ListView mainListView) {
        // Find the ListView resource.
//        context.
//        mainListView = (ListView) getView().findViewById( R.id.movieList );
//        mainListView = (ListView)

//        ImageView imageView = (ImageView) getView().findViewById(R.id.foo);
        // or  (ImageView) view.findViewById(R.id.foo);

        // Create and populate a List of planet names.
        String[] planets = new String[] { "Mercury", "Venus", "Earth", "Mars",
                "Jupiter", "Saturn", "Uranus", "Neptune"};

//        ArrayList<String> planetList = new ArrayList<String>();
//        planetList.addAll( Arrays.asList(planets) );

        ArrayList<String> movieList = new ArrayList<>();
        for(Movie movie: comingSoonList) {
            movieList.add(movie.getName() + "\n" + "Popularity: " + movie.getPopularity());
        }

        ArrayList<String> planetList = new ArrayList<String>();
        planetList.addAll( Arrays.asList(planets) );

        // Create ArrayAdapter using the planet list.
        listAdapter = new ArrayAdapter<String>(context, R.layout.simplerow, movieList);

        // Add more planets. If you passed a String[] instead of a List<String>
        // into the ArrayAdapter constructor, you must not add more items.
        // Otherwise an exception will occur.
//        listAdapter.add( "Ceres" );
//        listAdapter.add( "Pluto" );
//        listAdapter.add( "Haumea" );
//        listAdapter.add( "Makemake" );
//        listAdapter.add( "Eris" );

        // Set the ArrayAdapter as the ListView's adapter.
        mainListView.setAdapter( listAdapter );
    }

    //Run search in background
    class MyTask extends AsyncTask {

        private ListView mainListView ;

        public MyTask(ListView mainListView) {
            this.mainListView = mainListView;
        }

        @Override
        protected Object doInBackground(Object[] objects) {

            //Get Dates for connection strings
            final int COMING_SOON_DATE = 14;   //Movies coming in the next two weeks
            final int IN_THEATRES_DATE = -30;  //Include movies from last month
            ChangeDate newDate = new ChangeDate();
            comingSoon = newDate.changeDate(COMING_SOON_DATE);
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
            grabJSON = new RESTinterface(getContext());
            grabInTheatreJSON = new RESTinterface(getContext());
            comingSoonList = new ArrayList<>();
            inTheatresList = new ArrayList<>();

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

            Tab1 tab1 = new Tab1();

            tab1.fillList(comingSoonList, getContext(), mainListView);

            Log.d("AlTag", comingSoonList.toString());
            Log.d("AlTag", inTheatresList.toString());
        }
    }

}


























