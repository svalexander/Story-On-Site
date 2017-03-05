package nyc.c4q.helenchan.makinghistory.backend;

import java.util.List;

import nyc.c4q.helenchan.makinghistory.model.Feature;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by akashaarcher on 3/5/17.
 */

public interface NYPLApi {

    @GET(" ")
    Call<List<Feature>> getFeatures();
}
