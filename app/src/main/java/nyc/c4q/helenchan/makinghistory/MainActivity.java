package nyc.c4q.helenchan.makinghistory;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import nyc.c4q.helenchan.makinghistory.model.FeatureResponse;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "Main Activity";
    private TextView skipBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        setListeners();

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
                Intent skipIntent = new Intent(MainActivity.this, LandingPageActivity.class);
                startActivity(skipIntent);
        }
    }

}
