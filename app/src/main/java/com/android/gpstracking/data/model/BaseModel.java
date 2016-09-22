package com.android.gpstracking.data.model;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * Base model.
 */

public abstract class BaseModel {
    public static final long DEFAULT_INIT_ID = -1;
    /** ID of model. **/
    private long mId;
    /** Title of model. **/
    private String mTitle;
    /** Time init model. **/
    private long mTime;

    public BaseModel(long id, String title) {
        setId(id);
        setTitle(title);
        setTime(System.currentTimeMillis());
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public long getTime() {
        return mTime;
    }

    public void setTime(long time) {
        mTime = time;
    }

    /**
     * To content values.
     */
    public abstract ContentValues toContentValues();

    /**
     * To object.
     * @param c Cursor object from database.
     */
    public abstract void toObject(Cursor c);
}
