package com.android.gpstracking.data.model;

import android.content.ContentValues;
import android.database.Cursor;

import com.android.gpstracking.data.database.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Place model.
 */
public class PlaceModel extends BaseModel {
    /** Router Id. **/
    private long mRouterId;
    /** Note information about place. **/
    private String mNote;
    /** Latitude of place. **/
    private long mLatitude;
    /** Longitude of place. **/
    private long mLongitude;
    /** List url image taken. **/
    private List<String> mListUrlImg;

    public PlaceModel(long id, String title, long routerId, String note, long lat,
                      long longitude, ArrayList<String> listUrlImg) {
        super(id, title);
        setRouterId(routerId);
        setNote(note);
        setLatitude(lat);
        setLongitude(longitude);
        setListUrlImg(listUrlImg);
    }

    @Override
    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.PlaceColumns.ROUTER_ID, getRouterId());
        values.put(DatabaseHelper.PlaceColumns.NAME, getTitle());
        values.put(DatabaseHelper.PlaceColumns.TIME, getTime());
        values.put(DatabaseHelper.PlaceColumns.LATITUDE, getLatitude());
        values.put(DatabaseHelper.PlaceColumns.LONGITUDE, getLongitude());
        if (mListUrlImg != null) {
            // FIXME: 9/22/2016 Dummy code.
            values.put(DatabaseHelper.PlaceColumns.IMG1, "");
            values.put(DatabaseHelper.PlaceColumns.IMG2, "");
            values.put(DatabaseHelper.PlaceColumns.IMG3, "");
        }
        return values;
    }

    @Override
    public void toObject(Cursor c) {

    }

    public String getNote() {
        return mNote;
    }

    public void setNote(String note) {
        mNote = note;
    }

    public long getLatitude() {
        return mLatitude;
    }

    public void setLatitude(long latitude) {
        mLatitude = latitude;
    }

    public long getLongitude() {
        return mLongitude;
    }

    public void setLongitude(long longitude) {
        mLongitude = longitude;
    }

    public List<String> getListUrlImg() {
        return mListUrlImg;
    }

    public void setListUrlImg(List<String> listUrlImg) {
        mListUrlImg = listUrlImg;
    }

    public long getRouterId() {
        return mRouterId;
    }

    public void setRouterId(long routerId) {
        mRouterId = routerId;
    }

}
