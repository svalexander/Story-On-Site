package nyc.c4q.helenchan.makinghistory;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

import nyc.c4q.helenchan.makinghistory.models.nypl.Feature;
import nyc.c4q.helenchan.makinghistory.models.nypl.FeatureResponse;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.content.Context.INPUT_METHOD_SERVICE;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by shannonalexander-navarro on 3/7/17.
 */

public class ExploreMoreFragment extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnMarkerClickListener, MapListener {

    private DatabaseReference mFirebaseDatabase;
    private DatabaseReference mFirebaseDatabase2;
    private static final String ANONYMOUS = "ANONYMOUS";
    private static final int RC_SIGN_IN = 1;
    private FirebaseDatabase loginFirebaseDatabase;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private String mUsername;
    private TextView signInTV;
    private TextView welcomeTV;

    private MapListener mapListener;
    private static final String TAG = "Main Activity";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private GoogleApiClient mLocationClient;
    GoogleMap mMap;
    private float zoomLevel = 15;
    private Button searchAddressBtn;
    private FrameLayout baseLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.activity_map, container, false);
        mapListener = this;

        loginFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mUsername = ANONYMOUS;
        setAuthenticationListener();

        signInTV = (TextView) root.findViewById(R.id.sign_in_tv);
        signInTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLoginScreen();
            }
        });
        welcomeTV = (TextView) root.findViewById(R.id.welcome_tv);

        if (checkPlayServices()) {

            SupportMapFragment mapFragment =
                    (SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);

            mLocationClient = new GoogleApiClient.Builder(getActivity())
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();

            mLocationClient.connect();
        }
        mFirebaseDatabase2 = FirebaseDatabase.getInstance().getReference();

        searchAddressBtn = (Button) root.findViewById(R.id.location_search_btn);
        searchAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    locateFromAddress(view);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        return root;
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

//        if (checkPermissions()) {
//            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                // TODO: Consider calling
//                //    ActivityCompat#requestPermissions
//                // here to request the missing permissions, and then overriding
//                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                //                                          int[] grantResults)
//                // to handle the case where the user grants the permission. See the documentation
//                // for ActivityCompat#requestPermissions for more details.
//                return;
//            }
//            mMap.setMyLocationEnabled(true);
//        } else if (requestPermissions()) {
//            mMap.setMyLocationEnabled(true);
//        } else {
//            Toast.makeText(getApplicationContext(), "To view your location, please visit settings and change location permissions", Toast.LENGTH_LONG).show();
//        }

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

        // TODO: update later
        // centers map on current user location, stack overflow solution. will update later
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
        if (location != null) {
            CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(location.getLatitude(), location.getLongitude())).zoom(12).build();
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }

        parseJSON(getActivity());
        mMap.setOnMarkerClickListener(this);
    }

    private void hideSoftKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    public void locateFromAddress(View v) throws IOException {
        hideSoftKeyboard(v);

        EditText locationInput = (EditText) v.findViewById(R.id.location_search_input);
        String searchString = locationInput.getText().toString();

        Geocoder gc = new Geocoder(getActivity());
        List<Address> list = gc.getFromLocationName(searchString, 1);

        if (list.size() > 0) {
            Address add = list.get(0);
            String locality = add.getLocality();
            Toast.makeText(getActivity(), "Found: " + locality, Toast.LENGTH_SHORT).show();

            double lat = add.getLatitude();
            double lng = add.getLongitude();
            gotoLocation(lat, lng, zoomLevel);
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Toast.makeText(getActivity(), "Map working!!", Toast.LENGTH_SHORT).show();
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


    private void setAuthenticationListener() {
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    mUsername = user.getDisplayName();
                    welcomeTV.setVisibility(View.VISIBLE);
                    welcomeTV.setText("Hello " + mUsername);
                    signInTV.setVisibility(View.GONE);

                } else {
                    welcomeTV.setVisibility(View.GONE);
                    signInTV.setVisibility(View.VISIBLE);
                }
            }
        };
    }

    private void getLoginScreen() {
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setIsSmartLockEnabled(false)
                        .setProviders(Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()))
                        .build(),
                RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(getActivity(), "Welcome", Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getActivity(), "Canceled", Toast.LENGTH_SHORT).show();
                getActivity().finish();
            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out_menu:
                AuthUI.getInstance().signOut(getActivity());
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mAuthStateListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }
}
