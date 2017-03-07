package nyc.c4q.helenchan.makinghistory;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import me.anwarshahriar.calligrapher.Calligrapher;
import nyc.c4q.helenchan.makinghistory.leigh.AddConentActivity;

public class LandingPageActivity extends BaseActivity implements View.OnClickListener {
    
    private FrameLayout pntOfInterestLayout;
    private FrameLayout exploreLayout;
    private FrameLayout createLayout;
    private FrameLayout loginLayout;
    private FrameLayout baseLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseLayout = (FrameLayout) findViewById(R.id.base_frame_Layout);
        getLayoutInflater().inflate(R.layout.activity_landing_page, baseLayout);
        initViews();
        setListeners();
        setFontType();


    }


    private void setFontType() {
        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "ArimaMadurai-Bold.ttf", true);
        calligrapher.setFont(findViewById(R.id.loginFrameLayout), "Raleway-Regular.ttf");
    }

    private void initViews() {
        pntOfInterestLayout = (FrameLayout) findViewById(R.id.point_interest_layout);
        exploreLayout = (FrameLayout) findViewById(R.id.explore_layout);
        createLayout = (FrameLayout) findViewById(R.id.create_layout);
        loginLayout = (FrameLayout) findViewById(R.id.loginFrameLayout);

    }

    private void setListeners() {
        exploreLayout.setOnClickListener(this);
        createLayout.setOnClickListener(this);
        pntOfInterestLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.explore_layout:
                Intent exploreIntent = new Intent(LandingPageActivity.this, ExploreMoreActivity.class);
                startActivity(exploreIntent);
                break;
            case R.id.create_layout:
                Intent createIntent = new Intent(LandingPageActivity.this, AddConentActivity.class);
                startActivity(createIntent);
                break;
            case R.id.point_interest_layout:
                Intent pntInterestIntent = new Intent(LandingPageActivity.this, PointOfInterestActivity.class);
                startActivity(pntInterestIntent);
                break;

        }
    }

}
