package nyc.c4q.helenchan.makinghistory.models;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by leighdouglas on 3/4/17.
 */

public class Coordinate {

    private double latitude;
    private double longitude;

    public Coordinate() {
    }

    public Coordinate(double latitude, double longitude){
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public LatLng toLatLng() {
        return new LatLng(latitude, longitude);
    }
}
