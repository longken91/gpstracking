package com.android.gpstracking.data.model;

import android.content.ContentValues;
import android.database.Cursor;

import com.android.gpstracking.data.database.DatabaseHelper;

import java.util.List;

/**
 * Router model.
 */
public class RouterModel extends BaseModel {
    /** Status is tracking. **/
    public static final int STATUS_TRACKING = 0;
    /** Status pause tracking. **/
    public static final int STATUS_PAUSE = 1;
    /** Status stop. **/
    public static final int STATUS_STOP = 2;
    /** Start place of router. **/
    private PlaceModel mStartPlace;
    /** End place of router. **/
    private PlaceModel mEndPlace;
    /** List place of router. **/
    private List<PlaceModel> mListPlace;
    /** Status track. **/
    private int mStatus = STATUS_TRACKING;

    public RouterModel(long id, String title, PlaceModel startPlace, PlaceModel endPlace, int status) {
        super(id, title);
        setStartPlace(startPlace);
        setEndPlace(endPlace);
        setStatus(status);
    }


    @Override
    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.RouterColumns.NAME, getTitle());
        values.put(DatabaseHelper.RouterColumns.TIME, getTime());
        if (getStartPlace() != null) {
            values.put(DatabaseHelper.RouterColumns.START, getStartPlace().getId());
        }
        if (getEndPlace() != null) {
            values.put(DatabaseHelper.RouterColumns.END, getEndPlace().getId());
        }
        values.put(DatabaseHelper.RouterColumns.STATUS, getStatus());
        return values;
    }

    @Override
    public void toObject(Cursor c) {

    }

    public PlaceModel getStartPlace() {
        return mStartPlace;
    }

    public void setStartPlace(PlaceModel startPlace) {
        mStartPlace = startPlace;
    }

    public PlaceModel getEndPlace() {
        return mEndPlace;
    }

    public void setEndPlace(PlaceModel endPlace) {
        mEndPlace = endPlace;
    }

    public List<PlaceModel> getListPlace() {
        return mListPlace;
    }

    public void setListPlace(List<PlaceModel> listPlace) {
        mListPlace = listPlace;
    }

    public int getStatus() {
        return mStatus;
    }

    public void setStatus(int status) {
        mStatus = status;
    }
}
