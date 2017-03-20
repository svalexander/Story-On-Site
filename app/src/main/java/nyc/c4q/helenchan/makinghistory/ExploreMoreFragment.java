package nyc.c4q.helenchan.makinghistory;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.konifar.fab_transformation.FabTransformation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import nyc.c4q.helenchan.makinghistory.models.Content;
import nyc.c4q.helenchan.makinghistory.models.nypl.Feature;
import nyc.c4q.helenchan.makinghistory.models.nypl.FeatureResponse;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by shannonalexander-navarro on 3/7/17.
 */

public class ExploreMoreFragment extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnMarkerClickListener, LocationListener, MapListener, View.OnClickListener {

    private DatabaseReference mFirebaseDatabase;
    private DatabaseReference mFirebaseDatabase2;

    private FloatingActionButton searchFabBtn;
    private android.support.v7.widget.Toolbar fabToolBar;

    private EditText locationAddressSearch;
    private TextView toolBarSearch;
    private TextView toolBarCancel;

    private MapListener mapListener;
    private static final String TAG = "Main Activity";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private GoogleApiClient mLocationClient;
    GoogleMap mMap;
    private float zoomLevel = 15;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.activity_map, container, false);
        mapListener = this;

        if (checkPlayServices()) {

            SupportMapFragment mapFragment =
                    (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);

            mLocationClient = new GoogleApiClient.Builder(getActivity())
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();

        }
        mFirebaseDatabase2 = FirebaseDatabase.getInstance().getReference();

        toolBarSearch = (TextView) root.findViewById(R.id.search_location_map);
        toolBarSearch.setOnClickListener(this);
        toolBarCancel = (TextView) root.findViewById(R.id.cancel_search_map);
        toolBarCancel.setOnClickListener(this);
        locationAddressSearch = (EditText) root.findViewById(R.id.location_search_input);
        searchFabBtn = (FloatingActionButton) root.findViewById(R.id.search_fab);
        searchFabBtn.setOnClickListener(this);
        fabToolBar = (android.support.v7.widget.Toolbar) root.findViewById(R.id.toolbar_fab);


        setActionBarTitle(root);

        setHasOptionsMenu(false);

        return root;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.search_fab:
                FabTransformation.with(searchFabBtn)
                        .transformTo(fabToolBar);
                break;
            case R.id.cancel_search_map:
                FabTransformation.with(searchFabBtn)
                        .transformFrom(fabToolBar);
                break;
            case R.id.search_location_map:
                try {
                    locateFromAddress(v);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

        }
    }

    private void setActionBarTitle(View v) {
        ((BaseActivity) v.getContext()).getSupportActionBar().setTitle(R.string.app_name);
        ((BaseActivity) v.getContext()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(getActivity());
        if (result != ConnectionResult.SUCCESS) {
            if (googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(getActivity(), result,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            }
            return false;
        }
        return true;
    }

    private void gotoLocation(double lat, double lng, float zoom) {
        LatLng latlng = new LatLng(lat, lng);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latlng, zoom);
        mMap.moveCamera(update);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            getApplicationContext(), R.raw.map_style));

            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }

        if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    Constants.REQUEST_CODE_LOCATION);
        } else {
            mMap.setMyLocationEnabled(true);
            mLocationClient.connect();
        }

        parseJSON(getActivity());
//        mLocationClient.connect();
        mMap.setOnMarkerClickListener(this);
    }

    private void hideSoftKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    public void locateFromAddress(View v) throws IOException {
        hideSoftKeyboard(v);

        String searchString = locationAddressSearch.getText().toString();

        Geocoder gc = new Geocoder(getActivity());
        List<Address> list = gc.getFromLocationName(searchString, 1);

        if (list.size() > 0) {
            Address add = list.get(0);
            String locality = add.getLocality();

            double lat = add.getLatitude();
            double lng = add.getLongitude();
            gotoLocation(lat, lng, zoomLevel);
        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mapListener.zoomToUserLocation(mMap);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Constants.REQUEST_CODE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mMap.setMyLocationEnabled(true);
                    mLocationClient.connect();
                    break;
                }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(getActivity(), "Map creation interrupted", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(getActivity(), "Map not connected!!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Feature feature = (Feature) marker.getTag();
        feature.getGeometry().getCoordinates();
        double lat;
        double lng;
        double[] coordinates = feature.getGeometry().getCoordinates();
        lng = coordinates[0];
        lat = coordinates[1];
        Intent intent = new Intent(getApplicationContext(), ViewContentActivity.class);
        intent.putExtra("Latitude", lat);
        intent.putExtra("Longitude", lng);
        startActivity(intent);
        return true;
    }


    public void parseJSON(Context context) {

        try {
            AssetFileDescriptor fileDescriptor = context.getAssets().openFd("map.json");
            final BufferedReader reader = new BufferedReader(new InputStreamReader(fileDescriptor.createInputStream()));
            Gson gson = new Gson();
            FeatureResponse featureResponse = gson.fromJson(reader, FeatureResponse.class);
            Log.d(TAG, "parsing" + " " + featureResponse.features.size());
            mapListener.updateMarkers(mMap, featureResponse.features);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateMarkers(GoogleMap map, List<Feature> featuresList) {
        mFirebaseDatabase = FirebaseDatabase.getInstance().getReference().child("MapPoint");
        double latitude;
        double longitude;
        for (Feature feature : featuresList) {
            double[] coordinates = feature.getGeometry().getCoordinates();
            longitude = coordinates[0];
            latitude = coordinates[1];
            LatLng currLatLng = new LatLng(latitude, longitude);
            map.addMarker(new MarkerOptions()
                    .position(currLatLng)
                    .title(feature.getProperties().getName()))
                    .setTag(feature);
        }
    }

    @Override
    public void zoomToUserLocation(GoogleMap map) {
        Location myLocation = LocationServices.FusedLocationApi.getLastLocation(mLocationClient);
        CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(myLocation.getLatitude(), myLocation.getLongitude())).zoom(12).build();
        if (mMap != null) {
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    }

    @Override
    public void onLocationChanged(Location location) {

    }


}

