package nyc.c4q.helenchan.makinghistory;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by akashaarcher on 3/13/17.
 */

public class DescriptionDetailActivity extends AppCompatActivity {

    DescriptionDetailFragment descriptionFragment = new DescriptionDetailFragment();

    String TAG = "Description Detail Activity: ";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description_detail);

        FragmentTransaction detailFragTransaction = getSupportFragmentManager().beginTransaction();
        detailFragTransaction.replace(R.id.detail_container, descriptionFragment);
        detailFragTransaction.commit();

        Log.i(TAG, "Made the fragment");
    }

}
