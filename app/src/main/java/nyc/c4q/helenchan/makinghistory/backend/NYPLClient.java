package nyc.c4q.helenchan.makinghistory.backend;

import java.util.List;

import nyc.c4q.helenchan.makinghistory.model.Feature;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by akashaarcher on 3/5/17.
 */

public class NYPLClient {


    private final String API_URL = " ";

    private static NYPLClient instance;
    private final NYPLApi api;

    public static NYPLClient getInstance() {
        if (instance == null) {
            instance = new NYPLClient();
        }
        return instance;
    }

    private NYPLClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(NYPLApi.class);
    }

    public Call<List<Feature>> getFeature() {
        return api.getFeatures();
    }




}
