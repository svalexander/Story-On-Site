package nyc.c4q.helenchan.makinghistory;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
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

public class ExploreMoreFragment extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnMarkerClickListener, LocationListener, MapListener {

    private DatabaseReference mFirebaseDatabase;
    private DatabaseReference mFirebaseDatabase2;

    private MapListener mapListener;
    private static final String TAG = "Main Activity";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private GoogleApiClient mLocationClient;
    GoogleMap mMap;
    private float zoomLevel = 15;
    private Button searchAddressBtn;

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

            mLocationClient.connect();
        }
        mFirebaseDatabase2 = FirebaseDatabase.getInstance().getReference();

//        searchAddressBtn = (Button) root.findViewById(R.id.location_search_btn);
//        searchAddressBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                try {
//                    locateFromAddress(view);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        });

        setActionBarTitle(root);


        return root;
    }

    private void setActionBarTitle(View v) {
        ((BaseActivity) v.getContext()).getSupportActionBar().setTitle(R.string.explore_btn_text);
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

        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);

        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                mMap.setMyLocationEnabled(true);
            }
        }
        // centers map on current user location, stack overflow solution. will update later

        parseJSON(getActivity());
        mMap.setOnMarkerClickListener(this);
    }

    private void hideSoftKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

//    public void locateFromAddress(View v) throws IOException {
//        hideSoftKeyboard(v);
//
//        EditText locationInput = (EditText) v.findViewById(R.id.location_search_input);
//        String searchString = locationInput.getText().toString();
//
//        Geocoder gc = new Geocoder(getActivity());
//        List<Address> list = gc.getFromLocationName(searchString, 1);
//
//        if (list.size() > 0) {
//            Address add = list.get(0);
//            String locality = add.getLocality();
//            Toast.makeText(getActivity(), "Found: " + locality, Toast.LENGTH_SHORT).show();
//
//            double lat = add.getLatitude();
//            double lng = add.getLongitude();
//            gotoLocation(lat, lng, zoomLevel);
//        }
//    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Toast.makeText(getActivity(), "Map working!!", Toast.LENGTH_SHORT).show();
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
        } else {
            Location myLocation =
                    LocationServices.FusedLocationApi.getLastLocation(mLocationClient);
            CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(myLocation.getLatitude(), myLocation.getLongitude())).zoom(12).build();
            if (mMap != null) {
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
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

    private boolean checkPermissions() {
        return (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED);
    }

    private boolean requestPermissions() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,}, 1);
        return checkPermissions();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Feature feature = (Feature) marker.getTag();
        feature.getGeometry().getCoordinates();
        double lat = 0;
        double lng = 0;
        double[] coordinates = feature.getGeometry().getCoordinates();
        for (int i = 0; i < coordinates.length; i++) {
            if (i == 0) {
                lng = coordinates[i];
            }
            if (i == 1) {
                lat = coordinates[i];
            }

        }
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


        //        mFirebaseDatabase.child("MapPoint").child("Location3").child("ContentList").push().setValue(new Content("Highline", "Historical", "This was the highline a long time ago", "HighLine", "http://oldnyc-assets.nypl.org/600px/712105f-a.jpg", "1920"));
    }

    @Override
    public void updateMarkers(GoogleMap map, List<Feature> featuresList) {
        mFirebaseDatabase = FirebaseDatabase.getInstance().getReference().child("MapPoint");
        double latitude = 0;
        double longitude = 0;
        for (Feature feature : featuresList) {
            double[] coordinates = feature.getGeometry().getCoordinates();
            for (int i = 0; i < coordinates.length; i++) {
                if (i == 0) {
                    longitude = coordinates[i];
                }
                if (i == 1) {
                    latitude = coordinates[i];
                }
            }

            LatLng currLatLng = new LatLng(latitude, longitude);
            map.addMarker(new MarkerOptions().position(currLatLng).title(feature.getProperties().getName())).setTag(feature);

        }

    }


    @Override
    public void onLocationChanged(Location location) {

    }
}

