package com.android.gpstracking;

import android.app.Application;

public class GPSTrackingApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        FactoryImpl.register(getApplicationContext(), this);
    }
}
