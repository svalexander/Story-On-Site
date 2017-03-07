package nyc.c4q.helenchan.makinghistory;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import nyc.c4q.helenchan.makinghistory.models.nypl.FeatureResponse;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AnimationListener {

    private static final String TAG = "Main Activity";
    private TextView skipBtn;
    private Animation fadeOutAnimation;
    private LinearLayout mainLayout;
    private Animation fadeInAnimation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        setListeners();
        setAnimations();
        parseJSON(this);
    }


    public void parseJSON(Context context) {

        try {
            AssetFileDescriptor fileDescriptor = context.getAssets().openFd("map.json");
            final BufferedReader reader = new BufferedReader(new InputStreamReader(fileDescriptor.createInputStream()));
            Gson gson = new Gson();
            FeatureResponse featureResponse = gson.fromJson(reader, FeatureResponse.class);
            Log.d(TAG, "parsing" + " " + featureResponse.features.size());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    private void initViews() {
        skipBtn = (Button) findViewById(R.id.skipBtn);
    }

    private void setListeners() {
        skipBtn.setOnClickListener(this);
    }

    private void setAnimations() {
        fadeOutAnimation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.fade_out);
        fadeInAnimation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.fade_in);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.skipBtn:
                Intent skipIntent = new Intent(MainActivity.this, ExploreMoreActivity.class);
                startActivity(skipIntent);
                //    overridePendingTransition(fadeOutAnimation,fadeInAnimation );
                //fadeOutAnimation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.fade_out);
        }
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {


    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
