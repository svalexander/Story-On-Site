package nyc.c4q.helenchan.makinghistory;

import com.google.android.gms.maps.GoogleMap;

import java.util.List;

import nyc.c4q.helenchan.makinghistory.models.nypl.Feature;

/**
 * Created by leighdouglas on 3/6/17.
 */

public interface MapListener {

    void updateMarkers(GoogleMap map, List<Feature> featuresList);
}
