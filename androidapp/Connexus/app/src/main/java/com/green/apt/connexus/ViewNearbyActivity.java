package com.green.apt.connexus;

import android.content.Intent;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.green.apt.connexus.R;
import com.green.apt.connexus.controllers.ViewNearbyController;
import com.green.apt.connexus.controllers.ViewSearchController;

public class ViewNearbyActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private ViewNearbyController controller;
    private GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Double lat;
    Double lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_nearby);

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            Log.d("ViewNearbyActivity", "Google API client created: " + mGoogleApiClient.toString());
        }else{
            Log.d("ViewNearbyActivity", "Google API client exists: " + mGoogleApiClient.toString());
        }
        try {

            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
            if (mLastLocation != null) {
                lon = mLastLocation.getLongitude();
                lat =  mLastLocation.getLatitude();
            }else{
                Log.d("ViewNearbyActivity", "mLastLocation is null");
            }
        } catch (SecurityException e) {
            Log.e("ViewNearbyActivity", e.getMessage());
        }

        controller = new ViewNearbyController(this, lat, lon);
        controller.runSearch();
    }

    public void viewNext(View view){

        controller.increaseOffset(8);
        controller.runSearch();
    }

    public void goToStreams(View view) {
        Intent intent = new Intent(this, ViewAllActivity.class);
        startActivity(intent);
    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(Bundle bundle) {
        try {

            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
            if (mLastLocation != null) {
                lon = mLastLocation.getLongitude();
                lat =  mLastLocation.getLatitude();

            }
        } catch (SecurityException e) {

        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}



