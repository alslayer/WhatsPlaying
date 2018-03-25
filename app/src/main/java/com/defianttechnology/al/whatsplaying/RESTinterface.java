package com.defianttechnology.al.whatsplaying;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.Log;
import android.widget.GridView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Al on 3/14/2018.
 */

public class RESTinterface {

    Context mContext;
    ArrayList results;

    public RESTinterface(Context mContext) {
        this.mContext = mContext;
        results = new ArrayList<Movie>();
    }

    public ArrayList<Movie> connect(URL connectionString) throws IOException {
        // Create connection
        HttpsURLConnection myConnection = (HttpsURLConnection) connectionString.openConnection();
        if (myConnection.getResponseCode() == 200) {

            // Success
            InputStream responseBody = myConnection.getInputStream();
            // Further processing here
            InputStreamReader responseBodyReader = new InputStreamReader(responseBody, "UTF-8");
            JsonReader jsonReader = new JsonReader(responseBodyReader);
            jsonReader.beginObject(); // Start processing the JSON object

            // Grab JSON data and put in ArrayList
            while (jsonReader.hasNext()) {
                String name = jsonReader.nextName();
                // first photos branch
                if (name.equals("results")) {
                    while (jsonReader.hasNext()) {
                        int nextItem = 0;
                        jsonReader.beginArray();

                        while (nextItem < 19) {
                            jsonReader.beginObject();
                            Movie movie = new Movie();

                            while (jsonReader.hasNext()) {
                                String title = jsonReader.nextName();
                                if (title.equals("original_title")) {
                                    movie.setName(jsonReader.nextString());
                                } else if (title.equals("popularity")) {
                                    movie.setPopularity(jsonReader.nextString());
                                } else if (title.equals("poster_path")) {
                                    String posterLink = "https://image.tmdb.org/t/p/w600_and_h900_bestv2" +
                                            jsonReader.nextString();
                                    movie.setPosterURL(posterLink);
                                } else {
                                    jsonReader.skipValue();
                                }
                            }
                            jsonReader.endObject();
                            nextItem++;
                            results.add(movie);
                        }
                        jsonReader.close();
                        myConnection.disconnect();
                        return results;
                    }
                    break; // Break out of the loop
                } else {
                    jsonReader.skipValue();
                }
            }
            jsonReader.close();
        }
        myConnection.disconnect();
        return results;
    }
}
