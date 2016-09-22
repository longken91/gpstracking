package com.android.gpstracking.utils;


import com.android.gpstracking.Factory;

/**
 * Thin wrapper to get/set shared prefs values.
 */
public abstract class TrackingPrefs {
    /**
     * Shared preferences name for preferences applicable to the entire app.
     */
    public static final String SHARED_PREFERENCES_NAME = "tracking";

    /**
     * A placeholder base version for Bugle builds where no shared pref version was defined.
     */
    public static final int NO_SHARED_PREFERENCES_VERSION = -1;

    /**
     * Returns the shared preferences file name to use.
     * Subclasses should override and return the shared preferences file.
     */
    public abstract String getSharedPreferencesName();


    /**
     * Handles pref version upgrade.
     */
    public abstract void onUpgrade(final int oldVersion, final int newVersion);

    /**
     * Gets the SharedPreferences accessor to the application-wide preferences.
     */
    public static TrackingPrefs getApplicationPrefs() {
        return Factory.get().getApplicationPrefs();
    }

    /**
     * @param key The key to look up in shared prefs
     * @param defaultValue The default value if value in shared prefs is null or if
     * NumberFormatException is caught.
     * @return The corresponding value, or the default value.
     */
    public abstract int getInt(final String key, final int defaultValue);

    /**
     * @param key The key to look up in shared prefs
     * @param defaultValue The default value if value in shared prefs is null or if
     * NumberFormatException is caught.
     * @return The corresponding value, or the default value.
     */
    public abstract long getLong(final String key, final long defaultValue);

    /**
     * @param key The key to look up in shared prefs
     * @param defaultValue The default value if value in shared prefs is null.
     * @return The corresponding value, or the default value.
     */
    public abstract boolean getBoolean(final String key, final boolean defaultValue);

    /**
     * @param key The key to look up in shared prefs
     * @param defaultValue The default value if value in shared prefs is null.
     * @return The corresponding value, or the default value.
     */
    public abstract String getString(final String key, final String defaultValue);

    /**
     * @param key The key to look up in shared prefs
     * @return The corresponding value, or null if not found.
     */
    public abstract byte[] getBytes(final String key);

    /**
     * @param key The key to set in shared prefs
     * @param value The value to assign to the key
     */
    public abstract void putInt(final String key, final int value);

    /**
     * @param key The key to set in shared prefs
     * @param value The value to assign to the key
     */
    public abstract void putLong(final String key, final long value);

    /**
     * @param key The key to set in shared prefs
     * @param value The value to assign to the key
     */
    public abstract void putBoolean(final String key, final boolean value);

    /**
     * @param key The key to set in shared prefs
     * @param value The value to assign to the key
     */
    public abstract void putString(final String key, final String value);

    /**
     * @param key The key to set in shared prefs
     * @param value The value to assign to the key
     */
    public abstract void putBytes(final String key, final byte[] value);

    /**
     * @param key The key to remove from shared prefs
     */
    public abstract void remove(String key);
}
