package nyc.c4q.helenchan.makinghistory;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

import nyc.c4q.helenchan.makinghistory.model.FeatureResponse;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "jjjj";
    private TextView skipBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        setListeners();
        parseJSON();
        
        

    }
    public static void parseJSON(){

        Gson gson = new Gson();
        FeatureResponse featureResponse = gson.fromJson(String.valueOf(R.raw.map), FeatureResponse.class );

        Log.d(TAG, "parsing" + featureResponse.features.get(0).getType());

//        InputStream raw = context.getResources().openRawResource(R.raw.map);
//        Reader rd = new BufferedReader(new InputStreamReader(raw));
//        Gson gson = new GsonBuilder().create();
//        Feature obj = gson.fromJson(rd, Feature.class);
//        FeatureResponse featureParse = gson.fromJson(rd, FeatureResponse.class);
//        return featureParse;
    }
    

    private void initViews() {
        skipBtn = (Button) findViewById(R.id.skipBtn);
    }

    private void setListeners() {
        skipBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.skipBtn:
                Intent skipIntent = new Intent(MainActivity.this, PointOfInterestActivity.class);
                startActivity(skipIntent);
        }
    }

}
