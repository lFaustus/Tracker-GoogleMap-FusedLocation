package com.fluxinated.googlemappractice.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.fluxinated.googlemappractice.R;
import com.fluxinated.googlemappractice.adapters.PlaceAutocompleteAdapter;
import com.fluxinated.googlemappractice.common.logger.Log;
import com.fluxinated.googlemappractice.enums.TAGS;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.lang.reflect.Field;

public class MainActivity extends BaseActivity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks
{

    protected GoogleApiClient mGoogleApiClient;

    private PlaceAutocompleteAdapter mAdapter;

    private AutoCompleteTextView mAutocompleteViewDestination, mAutocompleteViewOrigin;

    private static final LatLngBounds BOUNDS_GREATER_SYDNEY = new LatLngBounds(
            new LatLng(-34.041458, 150.790100), new LatLng(-33.682247, 151.383362));

    private Intent mShowMapIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);


        mShowMapIntent = new Intent(this,MapActivity.class);
        // Construct a GoogleApiClient for the {@link Places#GEO_DATA_API} using AutoManage
        // functionality, which automatically sets up the API client to handle Activity lifecycle
        // events. If your activity does not extend FragmentActivity, make sure to call connect()
        // and disconnect() explicitly. enableAutomanage only works if activity is extended with fragmentactivity
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                //.enableAutoManage(this, 0 /* clientId */, this)
                .addApi(Places.GEO_DATA_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        setContentView(R.layout.activity_main);
        mAdapter = new PlaceAutocompleteAdapter(this, mGoogleApiClient, BOUNDS_GREATER_SYDNEY,
                null);
        mAutocompleteViewOrigin = (AutoCompleteTextView) findViewById(R.id.autocomplete_places_origin);
        mAutocompleteViewOrigin.setAdapter(mAdapter);
        mAutocompleteViewOrigin.setOnItemClickListener(new myAutoCompleteItemClickListener(TAGS.ORIGIN));

        mAutocompleteViewDestination = (AutoCompleteTextView) findViewById(R.id.autocomplete_places_destination);
        mAutocompleteViewDestination.setAdapter(mAdapter);
        mAutocompleteViewDestination.setOnItemClickListener(new myAutoCompleteItemClickListener(TAGS.DESTINATION));
        ((Button) findViewById(R.id.button_map)).setOnClickListener(this);
       // initializeViews();
        // initializeViews((ViewGroup) this.findViewById(R.id.places_layout));
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        mGoogleApiClient.connect();

    }

    @Override
    protected void onPause()
    {
        super.onPause();
        mGoogleApiClient.disconnect();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void initializeViews()
    {
        for (Field field : R.class.getFields())
        {
            if (field.getName().contains("autocomplete_places"))
            {
                try
                {
                    AutoCompleteTextView autoCompleteTextView = ((AutoCompleteTextView) findViewById(field.getInt(field.getName())));
                    ;
                    switch (field.getInt(field.getName()))
                    {
                        case R.id.autocomplete_places_origin:
                            autoCompleteTextView.setOnItemClickListener(new myAutoCompleteItemClickListener(TAGS.ORIGIN));
                            break;

                        case R.id.autocomplete_places_destination:
                            autoCompleteTextView.setOnItemClickListener(new myAutoCompleteItemClickListener(TAGS.DESTINATION));
                            break;
                    }

                    autoCompleteTextView.setAdapter(mAdapter);

                }
                catch (IllegalAccessException e)
                {
                    e.printStackTrace();
                }
            }
            else if(field.getName().contains("button_"))
            {
                try
                {
                    Button mButton = (Button)findViewById(field.getInt(field.getName()));
                    mButton.setOnClickListener(this);
                }
                catch (IllegalAccessException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onConnected(Bundle bundle)
    {
    }

    @Override
    public void onConnectionSuspended(int i)
    {

    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult)
    {
        Log.e(TAG, "onConnectionFailed: ConnectionResult.getErrorCode() = "
                + connectionResult.getErrorCode());

        // TODO(Developer): Check error code and notify the user of error state and resolution.
        Toast.makeText(this,
                "Could not connect to Google API Client: Error " + connectionResult.getErrorCode(),
                Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.button_map:
                 startActivity(mShowMapIntent);
                //Toast.makeText(this, "asdsa", Toast.LENGTH_SHORT).show();
                break;
        }
    }


    private class myAutoCompleteItemClickListener implements AdapterView.OnItemClickListener
    {

        private TAGS tag;

        public myAutoCompleteItemClickListener(TAGS tag)
        {
            this.tag = tag;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {
            final AutocompletePrediction item = mAdapter.getItem(position);
            final String placeId = item.getPlaceId();
            final CharSequence primaryText = item.getPrimaryText(null);

            Log.i(TAG, "Autocomplete item selected: " + primaryText);


            /*
             Issue a request to the Places Geo Data API to retrieve a Place object with additional
             details about the place.
              */
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(new ResultCallback<PlaceBuffer>()
            {
                @Override
                public void onResult(PlaceBuffer places)
                {
                    if (!places.getStatus().isSuccess())
                    {
                        Log.e(TAG, "Place query did not complete. Error: " + places.getStatus().toString());
                        places.release();
                        return;
                    }
                    // Get the Place object from the buffer.
                    final Place place = places.get(0);
                    /*final CharSequence thirdPartyAttribution = places.getAttributions();
                    if (thirdPartyAttribution == null) {
                        mPlaceDetailsAttribution.setVisibility(View.GONE);
                    } else {
                        mPlaceDetailsAttribution.setVisibility(View.VISIBLE);
                        mPlaceDetailsAttribution.setText(Html.fromHtml(thirdPartyAttribution.toString()));
                    }*/
                    Log.i(TAG, "Place details received: " + place.getName());
                    Bundle mBundle = new Bundle();
                    mBundle.putDouble("Lat", place.getLatLng().latitude);
                    mBundle.putDouble("Lng", place.getLatLng().longitude);
                    mBundle.putString("Name", place.getName().toString());
                    mBundle.putString("Address", place.getAddress().toString());
                    mShowMapIntent.putExtra(tag.name(), mBundle);
                    places.release();
                }
            });

            Toast.makeText(getApplicationContext(), "Clicked: " + primaryText,
                    Toast.LENGTH_SHORT).show();
            Log.i(TAG, "Called getPlaceById to get Place details for " + placeId);
        }
    }
}
