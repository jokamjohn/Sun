package com.example.android.sunshine.app;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    public MainActivityFragment() {
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
}
