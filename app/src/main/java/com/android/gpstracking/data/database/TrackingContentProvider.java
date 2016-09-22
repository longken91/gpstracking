package com.android.gpstracking.data.database;

import java.io.FileNotFoundException;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.support.annotation.NonNull;

import com.android.gpstracking.Factory;


/**
 * @author LongKen
 */
public class TrackingContentProvider extends ContentProvider {
    public static final String AUTHORITY = "com.android.gpstracking.data.database.TrackingContentProvider";
    private static final String CONTENT_AUTHORITY = "content://" + AUTHORITY
            + '/';

    // Router query
    private static final String ROUTER_QUERY = "router";
    public static final Uri ROUTER_URI = Uri.parse(CONTENT_AUTHORITY
            + ROUTER_QUERY);

    // Place by router id query
    private static final String PLACE_BY_ROUTER_QUERY = "place_by_router";
    public static final Uri PLACE_BY_ROUTER_URI = Uri.parse(CONTENT_AUTHORITY
            + PLACE_BY_ROUTER_QUERY);

    // Place query
    private static final String PLACE_QUERY = "place";
    public static final Uri PLACE_URI = Uri.parse(CONTENT_AUTHORITY
            + PLACE_QUERY);

    // Internal
    private static final int ROUTER_QUERY_CODE = 10;

    private static final int ROUTER_ID_QUERY_CODE = 20;
    
    private static final int PLACE_BY_ROUTER_QUERY_CODE  = 30;

    private static final int PLACE_QUERY_CODE  = 40;

    private static final int PLACE_ID_QUERY_CODE  = 50;

    private static final UriMatcher sURIMatcher = new UriMatcher(
            UriMatcher.NO_MATCH);

    static {
        sURIMatcher.addURI(AUTHORITY, ROUTER_QUERY, ROUTER_QUERY_CODE);
        sURIMatcher.addURI(AUTHORITY, ROUTER_QUERY + "/*", ROUTER_ID_QUERY_CODE);
        sURIMatcher.addURI(AUTHORITY, PLACE_BY_ROUTER_QUERY + "/*",
                PLACE_BY_ROUTER_QUERY_CODE);
        sURIMatcher.addURI(AUTHORITY, PLACE_QUERY, PLACE_QUERY_CODE);
        sURIMatcher.addURI(AUTHORITY, PLACE_QUERY + "/*", PLACE_ID_QUERY_CODE);
    }

    /**
     * Notify Everything Changed.
     */
    public static void notifyEverythingChanged() {
        final Uri uri = Uri.parse(CONTENT_AUTHORITY);
        final Context context = Factory.get().getApplicationContext();
        final ContentResolver cr = context.getContentResolver();
        cr.notifyChange(uri, null);
    }

    /**
     * Notify Place Changed
     */
    public void notifyPlaceChanged() {
        final Context context = Factory.get().getApplicationContext();
        final ContentResolver cr = context.getContentResolver();
        cr.notifyChange(PLACE_BY_ROUTER_URI, null);
    }

    /**
     * Notify List Router Changed.
     */
    public void notifyListRouterChanged() {
        final Context context = Factory.get().getApplicationContext();
        final ContentResolver cr = context.getContentResolver();
        cr.notifyChange(ROUTER_URI, null);
    }
    
    private DatabaseWrapper mDatabaseWrapper;

    public TrackingContentProvider() {
        super();
    }

    /**
     * Get Database wrapper.
     * @return DatabaseWrapper instance.
     */
    private DatabaseWrapper getDatabaseWrapper() {
        if (mDatabaseWrapper == null) {
            mDatabaseWrapper = Factory.get().getDatabaseWrapper();
        }
        return mDatabaseWrapper;
    }

    @Override
    public Cursor query(@NonNull final Uri uri, final String[] projection,
                        String selection, final String[] selectionArgs, String sortOrder) {
        final SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        final int match = sURIMatcher.match(uri);
        String groupBy = null;
        String limit = null;
        switch (match) {
            case ROUTER_QUERY_CODE:
                queryBuilder.setTables(DatabaseHelper.ROUTER_TABLE);
                break;
            case ROUTER_ID_QUERY_CODE:
                queryBuilder.setTables(DatabaseHelper.ROUTER_TABLE);
                queryBuilder.appendWhere(DatabaseHelper.RouterColumns._ID + "="
                        + uri.getLastPathSegment());
                break;
            case PLACE_BY_ROUTER_QUERY_CODE:
                queryBuilder.setTables(DatabaseHelper.PLACE_TABLE);
                queryBuilder.appendWhere(DatabaseHelper.PlaceColumns.ROUTER_ID + "="
                        + uri.getLastPathSegment());
                break;
            case PLACE_ID_QUERY_CODE:
                queryBuilder.setTables(DatabaseHelper.PLACE_TABLE);
                queryBuilder.appendWhere(DatabaseHelper.PlaceColumns._ID + "="
                        + uri.getLastPathSegment());
                break;
            default: {
                throw new IllegalArgumentException("Unknown URI " + uri);
            }
        }

        Cursor cursor = queryBuilder.query(getDatabaseWrapper().getDatabase(), projection, selection,
                selectionArgs, groupBy, limit, sortOrder);
        if ((getContext() != null) && (getContext().getContentResolver() != null)) {
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        }
        return cursor;
    }

    @Override
    public String getType(@NonNull final Uri uri) {
        return null;
    }

    protected DatabaseHelper getDatabase() {
        return DatabaseHelper.getInstance(getContext());
    }

    @Override
    public ParcelFileDescriptor openFile(@NonNull final Uri uri, final String fileMode)
            throws FileNotFoundException {
        throw new IllegalArgumentException("openFile not supported: " + uri);
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        int uriType = sURIMatcher.match(uri);
        long id = 0;
        String uriInserted = "";
        switch (uriType) {
            case ROUTER_QUERY_CODE:
                getDatabaseWrapper().beginTransaction();
                try {
                    id = getDatabaseWrapper().insert(DatabaseHelper.ROUTER_TABLE, null, values);
                    getDatabaseWrapper().setTransactionSuccessful();
                    uriInserted = ROUTER_QUERY + "/" + id;
                } finally {
                    getDatabaseWrapper().endTransaction();
                }
                break;
            case PLACE_QUERY_CODE:
                try {
                    id = getDatabaseWrapper().insert(DatabaseHelper.PLACE_TABLE, null, values);
                    getDatabaseWrapper().setTransactionSuccessful();
                    uriInserted = PLACE_QUERY + "/" + id;
                } finally {
                    getDatabaseWrapper().endTransaction();
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        if ((getContext() != null) && (getContext().getContentResolver() != null)) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return Uri.parse(uriInserted);
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        int rowsDeleted = 0;
        switch (uriType) {
            case ROUTER_QUERY_CODE: {
                getDatabaseWrapper().beginTransaction();
                try {
                    rowsDeleted = getDatabaseWrapper().delete(DatabaseHelper.ROUTER_TABLE,
                            selection, selectionArgs);
                    getDatabaseWrapper().setTransactionSuccessful();
                } finally {
                    getDatabaseWrapper().endTransaction();
                }
                break;
            }
            case ROUTER_ID_QUERY_CODE: {
                String id = uri.getLastPathSegment();
                getDatabaseWrapper().beginTransaction();
                try {
                    rowsDeleted = getDatabaseWrapper().delete(DatabaseHelper.PLACE_TABLE,
                            DatabaseHelper.PlaceColumns.ROUTER_ID + "=" + id, null);
                    getDatabaseWrapper().setTransactionSuccessful();
                } finally {
                    getDatabaseWrapper().endTransaction();
                }
                notifyListRouterChanged();
                break;
            }
            case PLACE_ID_QUERY_CODE: {
                String id = uri.getLastPathSegment();
                getDatabaseWrapper().beginTransaction();
                try {
                    rowsDeleted = getDatabaseWrapper().delete(DatabaseHelper.PLACE_TABLE,
                            DatabaseHelper.PlaceColumns._ID + "=" + id, null);
                    getDatabaseWrapper().setTransactionSuccessful();
                } finally {
                    getDatabaseWrapper().endTransaction();
                }
                notifyListRouterChanged();
                break;
            }
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        if ((getContext() != null) && (getContext().getContentResolver() != null)) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection,
            String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        int rowsUpdated = 0;
        switch (uriType) {
            case PLACE_BY_ROUTER_QUERY_CODE: {
                String id = uri.getLastPathSegment();
                getDatabaseWrapper().beginTransaction();
                try {
                    rowsUpdated = getDatabaseWrapper().update(DatabaseHelper.PLACE_TABLE, values,
                            DatabaseHelper.PlaceColumns._ID + "=" + id, selectionArgs);
                    getDatabaseWrapper().setTransactionSuccessful();
                } finally {
                    getDatabaseWrapper().endTransaction();
                }
                notifyPlaceChanged();
                break;
            }
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        if ((getContext() != null) && (getContext().getContentResolver() != null)) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public boolean onCreate() {
        getDatabase();
        return true;
    }
}
