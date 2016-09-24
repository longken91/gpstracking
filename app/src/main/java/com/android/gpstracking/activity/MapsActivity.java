package com.android.gpstracking.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import com.android.gpstracking.R;
import com.android.gpstracking.directions.route.AbstractRouting;
import com.android.gpstracking.directions.route.Route;
import com.android.gpstracking.directions.route.RouteException;
import com.android.gpstracking.directions.route.Routing;
import com.android.gpstracking.directions.route.RoutingListener;
import com.android.gpstracking.service.TrackingService;
import com.android.gpstracking.utils.AfterPermissionGranted;
import com.android.gpstracking.utils.EasyPermissions;
import com.android.gpstracking.utils.Logger;
import com.android.gpstracking.utils.NetWorkUtils;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends AppCompatActivity
        implements OnMapReadyCallback,
        RoutingListener, EasyPermissions.PermissionCallbacks {

    /** Request Location Perm. */
    private static final int RQ_LOCATION_PERM = 1000;
    /** Google Map. */
    private GoogleMap mMap;
    /** Polylines. */
    private List<Polyline> polylines;
    /** Start Position. */
    private static LatLng start = new LatLng(35.4956941, 139.654035);
     /** End Position. */
    private static LatLng end = new LatLng(35.4922093,139.643945);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.enter();
        setContentView(R.layout.activity_maps);
        /** Request Permission. */
        requestPermission();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Logger.exit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Logger.enter();
        stopService(new Intent(this, TrackingService.class));
        Logger.exit();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Logger.enter();
        mMap = googleMap;
        // Start marker
        MarkerOptions options = new MarkerOptions();
        options.position(start);
        options.icon(BitmapDescriptorFactory.fromResource(R.drawable.start_blue));
        mMap.addMarker(options);
        // End marker
        options = new MarkerOptions();
        options.position(end);
        options.icon(BitmapDescriptorFactory.fromResource(R.drawable.end_green));
        mMap.addMarker(options);
        // Route
        route(start, end);
        Logger.exit();
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        Logger.enter();
        requestCurrentLocation();
        Logger.exit();
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Logger.enter();

        Logger.exit();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Logger.enter();
        // EasyPermissions handles the request result.
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
        Logger.exit();
    }

    /**
     * Request Location Permission.
     */
    @AfterPermissionGranted(RQ_LOCATION_PERM)
    private void requestPermission() {
        Logger.enter();
        String perms[] = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};
        if (EasyPermissions.hasPermissions(this, perms)) {
            Logger.d("Have permissions, do the thing !");
            requestCurrentLocation();
        } else {
            Logger.d("Ask for both permissions !");
            EasyPermissions.requestPermissions(this, getString(R.string.rationale_location),
                    RQ_LOCATION_PERM, perms);
        }
        Logger.exit();
    }

    @Override
    public void onRoutingFailure(RouteException e) {

    }

    @Override
    public void onRoutingStart() {

    }

    @Override
    public void onRoutingSuccess(List<Route> route, int shortestRouteIndex) {

        CameraUpdate center = CameraUpdateFactory.newLatLng(start);
        CameraUpdateFactory.zoomTo(20);
        mMap.moveCamera(center);

        if(polylines != null && polylines.size()>0) {
            for (Polyline poly : polylines) {
                poly.remove();
            }
        }
        polylines = new ArrayList<>();
        //add route(s) to the map.
        for (int i = 0; i < route.size(); i++) {
            PolylineOptions polyOptions = new PolylineOptions();
            polyOptions.color(getResources().getColor(R.color.colorPrimary));
            polyOptions.width(10);
            polyOptions.addAll(route.get(i).getPoints());
            if (mMap != null) {
                Polyline polyline = mMap.addPolyline(polyOptions);
                polylines.add(polyline);
            }
        }
    }

    @Override
    public void onRoutingCancelled() {

    }

    /**
     * Route function.
     * @param start
     * @param end
     */
    public void route(LatLng start, LatLng end) {
        if (NetWorkUtils.isOnline(this)) {
            Routing routing = new Routing.Builder()
                    .travelMode(AbstractRouting.TravelMode.DRIVING)
                    .withListener(this)
                    .alternativeRoutes(true)
                    .waypoints(start, end)
                    .build();
            routing.execute();
        }
    }

    /**
     * Request get current location.
     */
    private void requestCurrentLocation() {
        Logger.enter();
        startService(new Intent(this, TrackingService.class));
        Logger.exit();
    }
}
