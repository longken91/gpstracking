package com.android.gpstracking.data.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.android.gpstracking.R;


/**
 * @author LongKen
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "tracking_db";

    private static int getDatabaseVersion(final Context context) {
        return Integer.parseInt(context.getResources().getString(R.string.database_version));
    }

    // Table names
    static final String ROUTER_TABLE = "router";
    static final String PLACE_TABLE = "place";

    interface TrackColumns {
        String NAME = "name";

        String TIME = "time";
    }

    // Songs table schema
    public static class RouterColumns implements BaseColumns, TrackColumns {
        // Start place id.
        public static final String START = "start";
        // End place id.
        public static final String END = "end";

        public static final String STATUS = "status";
    }
    
    // Songs table schema
    public static class PlaceColumns implements BaseColumns, TrackColumns {

        public static final String ROUTER_ID = "router_id";

        public static final String NOTE = "note";

        public static final String LATITUDE = "latitude";

        public static final String LONGITUDE = "longitude";

        public static final String IMG1 = "img1";

        public static final String IMG2 = "img2";

        public static final String IMG3 = "img3";
    }

    // Completed table SQL
    private static final String CREATE_ROUTER_TABLE_SQL =
            "CREATE TABLE " + ROUTER_TABLE + "("
                    + RouterColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + RouterColumns.NAME + " TEXT, "
                    + RouterColumns.TIME + " LONG, "
                    + RouterColumns.STATUS + " INT, "
                    + RouterColumns.START + " INT, "
                    + RouterColumns.END + " INT "
                    + ");";
    
    // Processing table SQL
    private static final String CREATE_PLACE_TABLE_SQL =
            "CREATE TABLE " + PLACE_TABLE + "("
                    + PlaceColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + PlaceColumns.ROUTER_ID + " INT, "
                    + PlaceColumns.NAME + " TEXT, "
                    + PlaceColumns.NOTE + " TEXT, "
                    + PlaceColumns.TIME + " LONG, "
                    + PlaceColumns.LATITUDE + " INT, "
                    + PlaceColumns.LONGITUDE + " DOUBLE, "
                    + PlaceColumns.IMG1 + " TEXT, "
                    + PlaceColumns.IMG2 + " TEXT, "
                    + PlaceColumns.IMG3 + " TEXT "
                    + ");";
    
    
    // List of all our SQL tables
    private static final String[] CREATE_TABLE_SQLS = new String[] {
            CREATE_PLACE_TABLE_SQL,
            CREATE_ROUTER_TABLE_SQL
    };

    private static final Object sLock = new Object();
    private final Context mApplicationContext;
    // Protected by sLock.
    private static DatabaseHelper sHelperInstance;

    private final Object mDatabaseWrapperLock = new Object();
    // Protected by mDatabaseWrapperLock.
    private DatabaseWrapper mDatabaseWrapper;

    /**
     * Get a (singleton) instance of {@link DatabaseHelper}, creating one if there isn't one yet.
     * This is the only public method for getting a new instance of the class.
     * @param context Should be the application context (or something that will live for the
     * lifetime of the application).
     * @return The current (or a new) DatabaseHelper instance.
     */
    public static DatabaseHelper getInstance(final Context context) {
        synchronized (sLock) {
            if (sHelperInstance == null) {
                sHelperInstance = new DatabaseHelper(context);
            }
            return sHelperInstance;
        }
    }

    /**
     * Private constructor.
     * @param context Should be the application context (or something that will live for the
     * lifetime of the application).
     */
    private DatabaseHelper(final Context context) {
        super(context, DATABASE_NAME, null, getDatabaseVersion(context), null);
        mApplicationContext = context;
    }

    /**
     * Get DatabaseWrapper.
     * @return DatabaseWrapper instance.
     */
    DatabaseWrapper getDatabase() {
        // We prevent the main UI thread from accessing the database here since we have to allow
        // public access to this class to enable sub-packages to access data.
        synchronized (mDatabaseWrapperLock) {
            if (mDatabaseWrapper == null) {
                mDatabaseWrapper = new DatabaseWrapper(mApplicationContext, getWritableDatabase());
            }
            return mDatabaseWrapper;
        }
    }

    /**
     * Create database.
     * @param db SQLiteDatabase instance.
     */
    private static void createDatabase(final SQLiteDatabase db) {
        for (final String sql : CREATE_TABLE_SQLS) {
            db.execSQL(sql);
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createDatabase(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
        db.execSQL("DROP TABLE IF EXISTS " + PLACE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + ROUTER_TABLE);
        onCreate(db);
    }
}
