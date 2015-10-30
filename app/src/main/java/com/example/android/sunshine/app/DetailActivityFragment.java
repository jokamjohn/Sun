package com.example.android.sunshine.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {
    //Tell the fragment that there is a menu
    public DetailActivityFragment() {
        setHasOptionsMenu(true);
    }

    private final String LOG_TAG = DetailActivityFragment.class.getSimpleName();

    private final String SHARE_HASH_TAG = "App created by John Kagga";

    private String mForecastStr;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Getting the intent
        Intent forecastIntent =getActivity().getIntent();

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        //Checking for the intent and intent extra
        if (forecastIntent != null && forecastIntent.hasExtra(Intent.EXTRA_TEXT))
        {
            mForecastStr = forecastIntent.getStringExtra(Intent.EXTRA_TEXT);
            //Adding forecast to the textview
            TextView detail = (TextView) rootView.findViewById(R.id.detail_textview);
            detail.setText(mForecastStr);
        }

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //Inflate the menu
        inflater.inflate(R.menu.detail_fragment,menu);

        //Locate the menu item
        MenuItem item = menu.findItem(R.id.detail_menu_share);

        //Fetch and store the shareActionProvider
        ShareActionProvider mshareActionProvider =
                (ShareActionProvider) MenuItemCompat.getActionProvider(item);

        if (mshareActionProvider != null)
        {
            mshareActionProvider.setShareIntent(createForecastShareIntent());
        }
        else
        {
            Log.d(LOG_TAG,"Share provider is null");
        }
    }

   //Share intent
    private Intent createForecastShareIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT,mForecastStr + " " + SHARE_HASH_TAG);
        return shareIntent;
    }

}
