package com.example.android.sunshine.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Getting the intent
        Intent forecastIntent =getActivity().getIntent();

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        if (forecastIntent != null && forecastIntent.hasExtra(Intent.EXTRA_TEXT))
        {
            String forecast = forecastIntent.getStringExtra(Intent.EXTRA_TEXT);
            TextView detail = (TextView) rootView.findViewById(R.id.detail_textview);
            detail.setText(forecast);
        }

        return rootView;
    }
}
