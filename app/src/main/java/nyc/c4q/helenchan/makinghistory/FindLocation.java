package nyc.c4q.helenchan.makinghistory;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import nyc.c4q.helenchan.makinghistory.models.MapPoint;

/**
 * Created by leighdouglas on 3/7/17.
 */

public class FindLocation implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleApiClient mGoogleApiClient;
    private Context context;
    private NearLocationListener listener;

    public FindLocation(Context context, NearLocationListener listener) {
        this.context = context;
        this.listener = listener;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d("success", "successful connection");
        Location currentUserLocation = grabUserLocation();
        checkUserLocation(currentUserLocation);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d("failed", "failed connection");

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("fail", "failed connection");

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    public void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    public void connectApiClient() {
        mGoogleApiClient.connect();
    }

    private Location grabUserLocation() {
        return LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
    }

    private void checkUserLocation(final Location userLocation) {
        DatabaseReference locationDatabse = FirebaseDatabase.getInstance().getReference().child("MapPoint");
        locationDatabse.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean temp = false;
                Location mapLocation = new Location("");
                String locationKey = "";
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    MapPoint mp = ds.getValue(MapPoint.class);
                    mapLocation.setLatitude(mp.getLatitude());
                    mapLocation.setLongitude(mp.getLongitude());
                    if(userLocation.distanceTo(mapLocation)<2600.0){
                        locationKey = String.valueOf(ds.getKey());
                        temp = true;
                        break;
                    }
                    Log.d("distance to:", String.valueOf(userLocation.distanceTo(mapLocation)));
                }

                if(temp){
                    listener.foundLocation(locationKey, temp);
                } else {
                    listener.foundLocation(locationKey, temp);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mGoogleApiClient.disconnect();
    }

    public interface NearLocationListener {
        void foundLocation(String userLocation, boolean foundLocation);
    }


}
