package com.example.android.sunshine.app;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
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

import com.example.android.sunshine.app.data.WeatherContract;


/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    //Tell the fragment that there is a menu
    public DetailActivityFragment() {
        setHasOptionsMenu(true);
    }

    private final String LOG_TAG = DetailActivityFragment.class.getSimpleName();

    private final String SHARE_HASH_TAG = "App created by @johnkagga";

    private static final int DETAIL_LOADER = 0;

    ShareActionProvider mshareActionProvider;

    private String mForecastStr;

    private static final String[] FORECAST_COLUMNS = {
            WeatherContract.WeatherEntry.TABLE_NAME + "." + WeatherContract.WeatherEntry._ID,
            WeatherContract.WeatherEntry.COLUMN_DATE,
            WeatherContract.WeatherEntry.COLUMN_SHORT_DESC,
            WeatherContract.WeatherEntry.COLUMN_MAX_TEMP,
            WeatherContract.WeatherEntry.COLUMN_MIN_TEMP,
    };

    // these constants correspond to the projection defined above, and must change if the
    // projection changes
    private static final int COL_WEATHER_ID = 0;
    private static final int COL_WEATHER_DATE = 1;
    private static final int COL_WEATHER_DESC = 2;
    private static final int COL_WEATHER_MAX_TEMP = 3;
    private static final int COL_WEATHER_MIN_TEMP = 4;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Getting the intent
        Intent forecastIntent =getActivity().getIntent();

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        //Checking for the intent and intent extra
        if (forecastIntent != null )
        {
            mForecastStr = forecastIntent.getDataString();
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
        mshareActionProvider =
                (ShareActionProvider) MenuItemCompat.getActionProvider(item);

        if (mshareActionProvider != null)
        {
            // If onLoadFinished happens before this, we can go ahead and set the share intent now.
            mshareActionProvider.setShareIntent(createForecastShareIntent());
        }
        else
        {
            Log.d(LOG_TAG,"Share provider is null");
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    //Share intent
    private Intent createForecastShareIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, mForecastStr + " " + SHARE_HASH_TAG);
        return shareIntent;
    }


    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        Log.v(LOG_TAG,"in oncreate loader");

        Intent detailIntent = getActivity().getIntent();
        if (detailIntent == null)
        {
            return null;
        }
        Uri detailUri = detailIntent.getData();

        return new CursorLoader(
                getActivity(),
                detailUri,
                FORECAST_COLUMNS,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.v(LOG_TAG, "In onLoadFinished");
        if (!data.moveToFirst()) { return; }

        String dateString = Utility.formatDate(
                data.getLong(COL_WEATHER_DATE));

        String weatherDescription =
                data.getString(COL_WEATHER_DESC);

        boolean isMetric = Utility.isMetric(getActivity());

        String high = Utility.formatTemperature(getActivity(),
                data.getDouble(COL_WEATHER_MAX_TEMP), isMetric);

        String low = Utility.formatTemperature(getActivity(),
                data.getDouble(COL_WEATHER_MIN_TEMP), isMetric);

        mForecastStr = String.format("%s - %s - %s/%s", dateString, weatherDescription, high, low);

        TextView detailTextView = (TextView)getView().findViewById(R.id.detail_textview);
        detailTextView.setText(mForecastStr);

        // If onCreateOptionsMenu has already happened, we need to update the share intent now.
        if (mshareActionProvider != null) {
            mshareActionProvider.setShareIntent(createForecastShareIntent());
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }


}
