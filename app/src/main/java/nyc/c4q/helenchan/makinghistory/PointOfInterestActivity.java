package nyc.c4q.helenchan.makinghistory;

import android.*;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import nyc.c4q.helenchan.makinghistory.leigh.AddConentActivity;

public class PointOfInterestActivity extends AppCompatActivity implements View.OnClickListener {

    private Button exploreBtn;
    private Button createBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point_of_interest);
        initViews();
        setListeners();

    }


    private void initViews() {
        exploreBtn = (Button) findViewById(R.id.exploreBtn);
        createBtn = (Button) findViewById(R.id.createBtn);
    }

    private void setListeners(){
        exploreBtn.setOnClickListener(this);
        createBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.exploreBtn:
                Intent exploreIntent = new Intent(PointOfInterestActivity.this, ExploreMoreActivity.class);
                startActivity(exploreIntent);
                break;
            case R.id.createBtn:
                Intent createIntent = new Intent(PointOfInterestActivity.this, AddConentActivity.class);
                startActivity(createIntent);
                break;
        }
    }
}
