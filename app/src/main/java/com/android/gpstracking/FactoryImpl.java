package com.android.gpstracking;

import android.content.Context;

import com.android.gpstracking.data.database.DataModel;
import com.android.gpstracking.data.database.DataModelImpl;
import com.android.gpstracking.data.database.DatabaseWrapper;
import com.android.gpstracking.utils.TrackingPrefs;

class FactoryImpl extends Factory {
    private GPSTrackingApplication mApplication;
    private DataModel mDataModel;
    private Context mApplicationContext;
    private DatabaseWrapper mDatabaseWrapper;

    private FactoryImpl() {
    }

    /**
     * Register factory.
     * @param applicationContext context instance.
     * @param application GPSTrackingApplication instance.
     * @return Factory object.
     */
    public static Factory register(final Context applicationContext,
            final GPSTrackingApplication application) {
        final FactoryImpl factory = new FactoryImpl();
        Factory.setInstance(factory);
        sRegistered = true;
        factory.mApplication = application;
        factory.mApplicationContext = applicationContext;
        factory.mDataModel = new DataModelImpl(applicationContext);
        factory.mDatabaseWrapper = new DatabaseWrapper(applicationContext);

        return factory;
    }

    @Override
    public Context getApplicationContext() {
        return mApplicationContext;
    }

    @Override
    public DataModel getDataModel() {
        return mDataModel;
    }

    @Override
    public TrackingPrefs getApplicationPrefs() {
        return null;
    }

    @Override
    public DatabaseWrapper getDatabaseWrapper() {
        return mDatabaseWrapper;
    }
}
