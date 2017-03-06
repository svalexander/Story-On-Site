package nyc.c4q.helenchan.makinghistory.models;

import java.util.HashMap;

/**
 * Created by leighdouglas on 3/4/17.
 */

public class MapPoint {
    private double latitude;
    private double longitude;
    private HashMap<String, Content> ContentList;

    public MapPoint() {

    }

    public MapPoint(double latitude, double longitude, HashMap<String, Content> ContentList) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.ContentList = ContentList;
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

    public HashMap<String, Content> getContentList() {
        return ContentList;
    }

    public void setContentList(HashMap<String, Content> ContentList) {
        this.ContentList = ContentList;
    }
}
