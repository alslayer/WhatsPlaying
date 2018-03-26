package com.defianttechnology.al.whatsplaying;

import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import java.util.Date;

/**
 * Created by Al on 3/25/2018.
 */

public class Tab2 extends android.support.v4.app.Fragment {

    private ListView mainListView ;
    private ArrayAdapter<String> listAdapter ;
    private ArrayList<Movie> inTheatresList;
    private RESTinterface grabJSON;
    private RESTinterface grabInTheatreJSON;;
    private URL inTheatresConnectionString = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab2, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        // Pass in the listview
        mainListView = getView().findViewById(R.id.movieList);

        //Grab the data
        new Tab2.MyTask(mainListView).execute();
    }

    public void fillList(ArrayList<Movie> comingSoonList, Context context, ListView mainListView) {

        ArrayList<String> movieList = new ArrayList<>();
        for(Movie movie: comingSoonList) {
            movieList.add(movie.getName() + "\n" + "Popularity: " + movie.getPopularity());
        }

        // Create ArrayAdapter using the planet list.
        listAdapter = new ArrayAdapter<String>(context, R.layout.simplerow, movieList);

        // Set the ArrayAdapter as the ListView's adapter.
        mainListView.setAdapter( listAdapter );
    }

    //Run search in background
    class MyTask extends AsyncTask {

        private ListView mainListView ;

        // Constructor
        public MyTask(ListView mainListView) {
            this.mainListView = mainListView;
        }

        @Override
        protected Object doInBackground(Object[] objects) {

            //Get Dates for connection strings
            final int IN_THEATRES_DATE = -30;  //Include movies from last month
            ChangeDate newDate = new ChangeDate();
            String inTheatres = newDate.changeDate(IN_THEATRES_DATE);

            // Build connection strings
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            DateFormat yearFormat = new SimpleDateFormat("yyyy");
            Date date = new Date();
            String todaysDate = dateFormat.format(date);
            String thisYear = yearFormat.format(date);
            try {
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
            inTheatresList = new ArrayList<>();
            try {
                inTheatresList = grabInTheatreJSON.connect(inTheatresConnectionString);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            // Fill up the list
            Tab1 tab1 = new Tab1();
            tab1.fillList(inTheatresList, getContext(), mainListView);
        }
    }

}






























