package com.android.gpstracking.data.database;

import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;

import com.android.gpstracking.data.model.PlaceModel;
import com.android.gpstracking.data.model.RouterModel;
import com.android.gpstracking.utils.Logger;

/**
 * @author LongKen
 *
 */

public class DataModelImpl extends DataModel{
    private final Context mContext;
    public DataModelImpl(final Context context) {
        mContext = context;
    }

    @Override
    public RouterModel newRouter(RouterModel routerModel) {
        ContentValues values = routerModel.toContentValues();
        Uri uri = mContext.getContentResolver().insert(TrackingContentProvider.ROUTER_URI, values);
        if (uri == null) {
            Logger.e("Error insert Router");
            return null;
        }

        String id = uri.getLastPathSegment();
        routerModel.setId(Long.valueOf(id));
        return routerModel;
    }

    @Override
    public List<RouterModel> getListRouter() {
        return null;
    }

    @Override
    public List<PlaceModel> getListRouterPlace(int routerId) {
        return null;
    }

    @Override
    public void deleteRouter(int routerId) {

    }

    @Override
    public PlaceModel newPlace(PlaceModel model) {
        ContentValues values = model.toContentValues();
        Uri uri = mContext.getContentResolver().insert(TrackingContentProvider.PLACE_URI, values);
        if (uri == null) {
            Logger.e("Error insert new Place");
            return null;
        }

        String id = uri.getLastPathSegment();
        model.setId(Long.valueOf(id));
        return model;
    }

    @Override
    public PlaceModel updatePlace(PlaceModel model) {
        return null;
    }

    @Override
    public void deletePlace(long placeId) {

    }
}
