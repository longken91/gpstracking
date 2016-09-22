package com.android.gpstracking;

import android.content.Context;

import com.android.gpstracking.data.database.DataModel;
import com.android.gpstracking.data.database.DatabaseWrapper;
import com.android.gpstracking.utils.TrackingPrefs;

/**
 *
 */
public abstract class Factory {
    // Making this volatile because on the unit tests, setInstance is called from a unit test
    // thread, and then it's read on the UI thread.
    private static volatile Factory sInstance;
    /** Flag check already registered **/
    protected static boolean sRegistered;

    /**
     * Singleton instance.
     * @return Factory instance.
     */
    public static Factory get() {
        return sInstance;
    }

    /**
     * Setter.
     * @param factory Factory instance.
     */
    protected static void setInstance(final Factory factory) {
        // Not allowed to call this after real application initialization is complete
        sInstance = factory;
    }

    /**
     * Get application context.
     * @return Context
     */
    public abstract Context getApplicationContext();

    /**
     * Data model manager.
     * @return DataModel object.
     */
    public abstract DataModel getDataModel();

    /**
     * Get application prefs
     * @return TrackingPrefs object.
     */
    public abstract TrackingPrefs getApplicationPrefs();

    /**
     * Get database wrapper.
     * @return DatabaseWrapper object.
     */
    public abstract DatabaseWrapper getDatabaseWrapper();
}
