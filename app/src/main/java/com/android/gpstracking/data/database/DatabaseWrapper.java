package com.android.gpstracking.data.database;

import java.util.Locale;
import java.util.Stack;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteFullException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.database.sqlite.SQLiteStatement;

import com.android.gpstracking.utils.Logger;


/**
 * @author LongKen
 */
public class DatabaseWrapper {
    private final SQLiteDatabase mDatabase;
    private final Context mContext;

    static class TransactionData {
        long time;
        boolean transactionSuccessful;
    }

    // track transaction on a per thread basis
    private static ThreadLocal<Stack<TransactionData>> sTransactionDepth =
            new ThreadLocal<Stack<TransactionData>>() {
        @Override
        public Stack<TransactionData> initialValue() {
            return new Stack<TransactionData>();
        }
    };

    public DatabaseWrapper(final Context context) {
        mDatabase = DatabaseHelper.getInstance(context).getWritableDatabase();
        mContext = context;
    }
    
    DatabaseWrapper(final Context context, final SQLiteDatabase db) {
        mDatabase = db;
        mContext = context;
    }

    public Context getContext() {
        return mContext;
    }

    /**
     * Begin transaction.
     */
    public void beginTransaction() {
        Logger.enter();
        // push the current time onto the transaction stack
        final TransactionData f = new TransactionData();
        sTransactionDepth.get().push(f);
        mDatabase.beginTransaction();
        Logger.exit();
    }

    /**
     * Set Transaction Successful
     */
    public void setTransactionSuccessful() {
        Logger.enter();
        final TransactionData f = sTransactionDepth.get().peek();
        f.transactionSuccessful = true;
        mDatabase.setTransactionSuccessful();
        Logger.exit();
    }

    /**
     * End Transaction.
     */
    public void endTransaction() {
        Logger.enter();
        final TransactionData f = sTransactionDepth.get().pop();
        if (f.transactionSuccessful == false) {
            Logger.w("endTransaction without setting successful");
            for (final StackTraceElement st : (new Exception()).getStackTrace()) {
                Logger.w("    " + st.toString());
            }
        }
        try {
            mDatabase.endTransaction();
        } catch (SQLiteFullException ex) {
            Logger.e("Database full, unable to endTransaction", ex);
        }
        Logger.exit();
    }

    public void insertWithOnConflict(final String searchTable, final String nullColumnHack,
            final ContentValues initialValues, final int conflictAlgorithm) {
        try {
            mDatabase.insertWithOnConflict(searchTable, nullColumnHack, initialValues,
                    conflictAlgorithm);
        } catch (SQLiteFullException ex) {
            Logger.e("Database full, unable to insertWithOnConflict", ex);
        }
    }

    /**
     * Query data.
     * @param searchTable Table name.
     * @param projection projection.
     * @param selection Column query.
     * @param selectionArgs param query.
     * @param groupBy groupBy String.
     * @param having Having String.
     * @param orderBy orderBy String.
     * @param limit limit Objects.
     * @return Cursor object.
     */
    public Cursor query(final String searchTable, final String[] projection,
            final String selection, final String[] selectionArgs, final String groupBy,
            final String having, final String orderBy, final String limit) {
        final Cursor cursor = mDatabase.query(searchTable, projection, selection, selectionArgs,
                groupBy, having, orderBy, limit);
        return cursor;
    }

    public Cursor query(final String searchTable, final String[] columns,
            final String selection, final String[] selectionArgs, final String groupBy,
            final String having, final String orderBy) {
        return query(
                searchTable, columns, selection, selectionArgs,
                groupBy, having, orderBy, null);
    }

    public Cursor query(final SQLiteQueryBuilder qb,
            final String[] projection, final String selection, final String[] queryArgs,
            final String groupBy, final String having, final String sortOrder, final String limit) {
        final Cursor cursor = qb.query(mDatabase, projection, selection, queryArgs, groupBy,
                having, sortOrder, limit);
        return cursor;
    }

    public long queryNumEntries(final String table, final String selection,
            final String[] selectionArgs) {
        final long retval =
                DatabaseUtils.queryNumEntries(mDatabase, table, selection, selectionArgs);
        return retval;
    }

    public Cursor rawQuery(final String sql, final String[] args) {
        final Cursor cursor = mDatabase.rawQuery(sql, args);
        return cursor;
    }

    /**
     * Update data to table
     * @param table Table name.
     * @param values ContentValues data update.
     * @param selection Column update.
     * @param selectionArgs param update.
     * @return Count object updated.
     */
    public int update(final String table, final ContentValues values,
            final String selection, final String[] selectionArgs) {
        int count = 0;
        try {
            count = mDatabase.update(table, values, selection, selectionArgs);
        } catch (SQLiteFullException ex) {
            Logger.e("Database full, unable to update", ex);
        }
        return count;
    }

    /**
     * Delete query in table
     * @param table Table name.
     * @param whereClause Where clause.
     * @param whereArgs Where param.
     * @return Count object deleted.
     */
    public int delete(final String table, final String whereClause, final String[] whereArgs) {
        int count = 0;
        try {
            count = mDatabase.delete(table, whereClause, whereArgs);
        } catch (SQLiteFullException ex) {
            Logger.e("Database full, unable to delete", ex);
        }
        return count;
    }

    /**
     * Insert to table.
     * @param table Table name.
     * @param nullColumnHack Null column to hack.
     * @param values ContentValues data insert.
     * @return id of row.
     */
    public long insert(final String table, final String nullColumnHack,
            final ContentValues values) {
        long rowId = -1;
        try {
            rowId = mDatabase.insert(table, nullColumnHack, values);
        } catch (SQLiteFullException ex) {
            Logger.e("Database full, unable to insert", ex);
        }
        return rowId;
    }

    public long replace(final String table, final String nullColumnHack,
            final ContentValues values) {
        long rowId = -1;
        try {
            rowId = mDatabase.replace(table, nullColumnHack, values);
        } catch (SQLiteFullException ex) {
            Logger.e("Database full, unable to replace", ex);
        }
        return rowId;
    }

    public void setLocale(final Locale locale) {
        mDatabase.setLocale(locale);
    }

    /**
     * Exec sql.
     * @param sql String sql.
     * @param bindArgs param.
     */
    public void execSQL(final String sql, final String[] bindArgs) {
        try {
            mDatabase.execSQL(sql, bindArgs);
        } catch (SQLiteFullException ex) {
            Logger.e("Database full, unable to execSQL", ex);
        }

    }

    /**
     * Exec sql.
     * @param sql String sql.
     */
    public void execSQL(final String sql) {
        try {
            mDatabase.execSQL(sql);
        } catch (SQLiteFullException ex) {
            Logger.e("Database full, unable to execSQL", ex);
        }
    }

    /**
     * @param sql String sql.
     * @return int Rows updated.
     */
    public int execSQLUpdateDelete(final String sql) {
        final SQLiteStatement statement = mDatabase.compileStatement(sql);
        int rowsUpdated = 0;
        try {
            rowsUpdated = statement.executeUpdateDelete();
        } catch (SQLiteFullException ex) {
            Logger.e("Database full, unable to execSQLUpdateDelete", ex);
        }
        return rowsUpdated;
    }

    /**
     * Get database.
     * @return SQLiteDatabase instance.
     */
    public SQLiteDatabase getDatabase() {
        return mDatabase;
    }
}
