package nyc.c4q.helenchan.makinghistory.models;

import java.util.List;

/**
 * Created by leighdouglas on 3/2/17.
 */

public class UserPhoto {

    int lat;
    int lng;
    String url;
    List<UserPhoto> userPhotoList;

    public UserPhoto() {

    }

    public UserPhoto(int lat, int lng, String url) {
        this.lat = lat;
        this.lng = lng;
        this.url = url;
    }

    public int getLat() {
        return lat;
    }

    public void setLat(int lat) {
        this.lat = lat;
    }

    public int getLng() {
        return lng;
    }

    public void setLng(int lng) {
        this.lng = lng;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

