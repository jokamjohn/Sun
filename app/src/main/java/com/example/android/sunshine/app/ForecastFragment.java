package com.example.android.sunshine.app;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A Forecast fragment containing the weather data.
 */
public class ForecastFragment extends Fragment {

    public ForecastFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //This line enables the fragment to handle menu events
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        //creating an array of fake data
        String [] forecastArray = {
                "Today - sunny - 88/63",
                "Tomorrow - Foggy - 70/40",
                "Weds - cloudy - 72/63",
                "Thur - Asteroids - 75/65",
                "Sat - HELPTAPPED IN WEATHERSTATION -60/51",
                "Sun - Sunny - 80/68"
        };

        //Converting the array to an ArrayList
        List<String> weekForecast = new ArrayList<>(
                Arrays.asList(forecastArray)
        );

        //Array adapter to populate the list data
        ArrayAdapter<String> mForecastAdapter = new ArrayAdapter<>(
                //The current context
                getActivity(),
                //ID of list item layout
                R.layout.list_item_forecast,
                //ID of list textview to populate
                R.id.list_item_forecast_textview,
                //List of data
                weekForecast);

        //List view to populate the data
        ListView forecastListView = (ListView) rootView.findViewById(R.id.listview_forecast);
        //Binding the adapter to the view
        forecastListView.setAdapter(mForecastAdapter);

        return rootView;
    }

    //Inflating the forecast fragment menu to the fragment
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.forecastfragment,menu);

    }

    //Adding the menu items
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_refresh)
        {
            FetchWeatherTask weatherTask = new FetchWeatherTask();
            weatherTask.execute("kampala");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //Async task class
    public class FetchWeatherTask extends AsyncTask<String,Void,Void>
    {

        private final String LOG_TAG = FetchWeatherTask.class.getSimpleName();

        @Override
        protected Void doInBackground(String... params) {
            //network call

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;

            //values to the url params
            String format = "json";
            String units = "metric";
            int days = 7;
            String api_key = "c23bdd3c920067eecb8bd0fa3ab4b6dc";

            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are available at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast

                //Constants for the uri
                final String FORECAST_BASE_URL = "http://api.openweathermap.org/data/2.5/forecast/daily?";
                final String QUERY_PARAM = "q";
                final String MODE_PARAM = "mode";
                final String UNITS_PARAM = "units";
                final String DAYS_PARAM = "cnt";
                final String API_KEY_PARAM = "APPID";

                //Building the weather api uri
                Uri builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                        .appendQueryParameter(QUERY_PARAM,params[0])
                        .appendQueryParameter(MODE_PARAM,format)
                        .appendQueryParameter(UNITS_PARAM,units)
                        .appendQueryParameter(DAYS_PARAM,Integer.toString(days))
                        .appendQueryParameter(API_KEY_PARAM,api_key)
                        .build();

                URL url = new URL(builtUri.toString());

                Log.v(LOG_TAG,"The weather uri: " + builtUri.toString());

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                forecastJsonStr = buffer.toString();

                Log.v(LOG_TAG,"Forecast Json string: " + forecastJsonStr);
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attempting
                // to parse it.
                return null;
            } finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
            return null;
        }
    }
}
