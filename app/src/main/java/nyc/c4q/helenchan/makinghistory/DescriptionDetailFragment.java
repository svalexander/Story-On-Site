package nyc.c4q.helenchan.makinghistory;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by akashaarcher on 3/13/17.
 */

public class DescriptionDetailFragment extends Fragment {

    private String TAG = "DetailFragment";


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_description_detail, container, false);

        Log.i(TAG, "Here I am (detail fragment)!!");
        return view;
    }



}
