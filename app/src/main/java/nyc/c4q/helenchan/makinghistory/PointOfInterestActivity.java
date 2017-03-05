package nyc.c4q.helenchan.makinghistory;

import android.os.Bundle;
import android.widget.FrameLayout;

public class PointOfInterestActivity extends BaseActivity {

    private FrameLayout baseLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        baseLayout = (FrameLayout) findViewById(R.id.base_frame_Layout);
        getLayoutInflater().inflate(R.layout.activity_point_of_interest, baseLayout);
    }
}
