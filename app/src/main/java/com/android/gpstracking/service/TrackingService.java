package com.android.gpstracking.service;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import com.android.gpstracking.R;
import com.android.gpstracking.utils.Logger;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

/**
 * Created by natuan on 22/09/2016.
 */

public class TrackingService extends Service
        implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    /** Provides the entry point to Google Play services. */
    protected GoogleApiClient mGoogleApiClient;
    /** Represents a geographical location. */
    protected Location mLastLocation;

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.enter();
        buildGoogleApiClient();
        Logger.exit();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Logger.enter();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        Logger.exit();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Logger.enter();
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            Toast.makeText(this, "Latitude: "+mLastLocation.getLatitude()
                    + "Longitude: "+mLastLocation.getLongitude(), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getString(R.string.no_location_detected), Toast.LENGTH_SHORT).show();
        }
        Logger.exit();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Logger.enter();
        mGoogleApiClient.connect();
        Logger.exit();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Logger.enter();
        Logger.exit();
    }

    /**
     * Builds a GoogleApiClient.
     * Uses the addApi() method to request the LocationServices API.
     */
    protected synchronized void buildGoogleApiClient() {
        Logger.enter();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
        Logger.exit();
    }
}
