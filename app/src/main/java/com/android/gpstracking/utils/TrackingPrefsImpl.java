package com.android.gpstracking.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

/**
 * Thin wrapper to get/set shared prefs values.
 */
public class TrackingPrefsImpl extends TrackingPrefs {

    private final Context mContext;

    public TrackingPrefsImpl(final Context context) {
        mContext = context;
    }

    /**
     * Validate the prefs key passed in. Subclasses should override this as needed to perform
     * runtime checks (such as making sure per-subscription settings don't sneak into application-
     * wide settings).
     */
    protected void validateKey(String key) {
    }

    @Override
    public String getSharedPreferencesName() {
        return TrackingPrefs.SHARED_PREFERENCES_NAME;
    }

    @Override
    public void onUpgrade(int oldVersion, int newVersion) {

    }

    @Override
    public int getInt(final String key, final int defaultValue) {
        validateKey(key);
        final SharedPreferences prefs = mContext.getSharedPreferences(
                getSharedPreferencesName(), Context.MODE_PRIVATE);
        return prefs.getInt(key, defaultValue);
    }

    @Override
    public long getLong(final String key, final long defaultValue) {
        validateKey(key);
        final SharedPreferences prefs = mContext.getSharedPreferences(
                getSharedPreferencesName(), Context.MODE_PRIVATE);
        return prefs.getLong(key, defaultValue);
    }

    @Override
    public boolean getBoolean(final String key, final boolean defaultValue) {
        validateKey(key);
        final SharedPreferences prefs = mContext.getSharedPreferences(
                getSharedPreferencesName(), Context.MODE_PRIVATE);
        return prefs.getBoolean(key, defaultValue);
    }

    @Override
    public String getString(final String key, final String defaultValue) {
        validateKey(key);
        final SharedPreferences prefs = mContext.getSharedPreferences(
                getSharedPreferencesName(), Context.MODE_PRIVATE);
        return prefs.getString(key, defaultValue);
    }

    @Override
    public byte[] getBytes(String key) {
        final String byteValue = getString(key, null);
        return byteValue == null ? null : Base64.decode(byteValue, Base64.DEFAULT);
    }

    @Override
    public void putInt(final String key, final int value) {
        validateKey(key);
        final SharedPreferences prefs = mContext.getSharedPreferences(
                getSharedPreferencesName(), Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    @Override
    public void putLong(final String key, final long value) {
        validateKey(key);
        final SharedPreferences prefs = mContext.getSharedPreferences(
                getSharedPreferencesName(), Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = prefs.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    @Override
    public void putBoolean(final String key, final boolean value) {
        validateKey(key);
        final SharedPreferences prefs = mContext.getSharedPreferences(
                getSharedPreferencesName(), Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    @Override
    public void putString(final String key, final String value) {
        validateKey(key);
        final SharedPreferences prefs = mContext.getSharedPreferences(
                getSharedPreferencesName(), Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.apply();
    }

    @Override
    public void putBytes(String key, byte[] value) {
        final String encodedBytes = Base64.encodeToString(value, Base64.DEFAULT);
        putString(key, encodedBytes);
    }

    @Override
    public void remove(final String key) {
        validateKey(key);
        final SharedPreferences prefs = mContext.getSharedPreferences(
                getSharedPreferencesName(), Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = prefs.edit();
        editor.remove(key);
        editor.apply();
    }
}
