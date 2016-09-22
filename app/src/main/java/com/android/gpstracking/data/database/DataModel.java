package com.android.gpstracking.data.database;

import com.android.gpstracking.Factory;
import com.android.gpstracking.data.model.PlaceModel;
import com.android.gpstracking.data.model.RouterModel;

import java.util.List;

/**
 * @author LongKen
 *
 */

public abstract class DataModel {
    /**
     * Get instance DataModel.
     * @return DataModel object.
     */
    public static DataModel get() {
        return Factory.get().getDataModel();
    }
    /**
     * Create new router.
     * @param routerModel input.
     * @return RouterModel object.
     */
    public abstract RouterModel newRouter(RouterModel routerModel);
    /**
     * Get list all router.
     * @return List RouterModels.
     */
    public abstract List<RouterModel> getListRouter();
    /**
     * Get list place of router.
     * @param routerId Router id.
     * @return List PlaceModels.
     */
    public abstract List<PlaceModel> getListRouterPlace(int routerId);
    /**
     * Delete router.
     * @param routerId Router id.
     */
    public abstract void deleteRouter(int routerId);
    /**
     * Create new place.
     * @return PlaceModel object.
     */
    public abstract PlaceModel newPlace(PlaceModel model);
    /**
     * Update place.
     * @return PlaceModel object.
     */
    public abstract PlaceModel updatePlace(PlaceModel model);
    /**
     * Delete Place.
     * @param placeId Place id.
     */
    public abstract void deletePlace(long placeId);
}
