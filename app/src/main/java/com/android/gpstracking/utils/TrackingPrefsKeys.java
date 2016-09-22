package com.android.gpstracking.utils;

/**
 * List of shared preferences keys and default values. These are all internal
 * (not user-visible) preferences. Preferences that are exposed via the Settings
 * activity should be defined in the constants.xml resource file instead.
 */
public final class TrackingPrefsKeys {
    private TrackingPrefsKeys() {}   // do not instantiate

    /**
     * Bugle's shared preferences version
     */
    public static final String SHARED_PREFERENCES_VERSION =
            "shared_preferences_version";
    public static final int SHARED_PREFERENCES_VERSION_DEFAULT =
            TrackingPrefs.NO_SHARED_PREFERENCES_VERSION;

    /**
     * Last time that we ran a a sync (in millis)
     */
    public static final String LAST_SYNC_TIME
            = "last_sync_time_millis";
    public static final long LAST_SYNC_TIME_DEFAULT
            = -1;

    /**
     * Last time that we ran a full sync (in millis)
     */
    public static final String LAST_FULL_SYNC_TIME
            = "last_full_sync_time_millis";
    public static final long LAST_FULL_SYNC_TIME_DEFAULT
            = -1;

    /**
     * Timestamp of the message for which we last did a message notification.
     */
    public static final String LATEST_NOTIFICATION_MESSAGE_TIMESTAMP
        = "latest_notification_message_timestamp";

    /**
     * The last selected chooser index in the media picker.
     */
    public static final String SELECTED_MEDIA_PICKER_CHOOSER_INDEX
            = "selected_media_picker_chooser_index";
    public static final int SELECTED_MEDIA_PICKER_CHOOSER_INDEX_DEFAULT
            = -1;

    /**
     * The attempt number when retrying ProcessPendingMessagesAction
     */
    public static final String PROCESS_PENDING_MESSAGES_RETRY_COUNT
            = "process_pending_retry";

}
