package com.example.android.sunshine.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final String DETAIL_FRAGMENT_TAG = "DFAG";

    private String mLocation;
    private Boolean mPaneUI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Current known location in the location setting
        mLocation = Utility.getPreferredLocation(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (findViewById(R.id.weather_detail_container) != null)
        {
            mPaneUI = true;
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.weather_detail_container, new DetailActivityFragment(),
                                DETAIL_FRAGMENT_TAG)
                        .commit();
            }
        }
        else {

            mPaneUI = false;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this,SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        else if (id == R.id.location_on_map)
        {
            showLocationOnMap();
        }

        return super.onOptionsItemSelected(item);
    }

    //This occurs when we get back to the main activity after changing the  location setting
    @Override
    protected void onResume() {
        super.onResume();
        Log.i(LOG_TAG,"OnResume now");

        String currentLocation = Utility.getPreferredLocation(this);
        if (currentLocation != null && !currentLocation.equals(mLocation))
        {
            ForecastFragment forecastFragment = (ForecastFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.fragment_forecast);
            if (null != forecastFragment)
            {
                //Get new data for the new location
                forecastFragment.onLocationChanged();
            }
            mLocation = currentLocation;
        }
    }

    private void showLocationOnMap() {
        SharedPreferences locationPreference = PreferenceManager
                .getDefaultSharedPreferences(this);
        String location = locationPreference.getString(getString(R.string.pref_location_key),
                getString(R.string.pref_location_default_value));

        Uri geoLocation = Uri.parse("geo:0,0?").buildUpon()
                .appendQueryParameter("q",location)
                .build();
        Intent mapIntent = new Intent(Intent.ACTION_VIEW,geoLocation);

        if (mapIntent.resolveActivity(getPackageManager()) != null)
        {
            startActivity(mapIntent);
        }
        else
        {
            Log.d(LOG_TAG, "Could not call " + location);
        }
    }
}
