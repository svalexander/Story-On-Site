package nyc.c4q.helenchan.makinghistory;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.relex.circleindicator.CircleIndicator;
import nyc.c4q.helenchan.makinghistory.suggestedviewpager.SuggestedAdapter;

/**
 * Created by shannonalexander-navarro on 3/6/17.
 */

public class SuggestedFragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.suggested_fragment, container, false);
        ViewPager vp = (ViewPager) root.findViewById(R.id.suggested_viewpager);
        vp.setAdapter(new SuggestedAdapter());
        CircleIndicator indicator = (CircleIndicator) root.findViewById(R.id.indicator);
        indicator.setViewPager(vp);
        setActionBarTitle(root);
        return root;
    }

    private void setActionBarTitle(View v) {
        ((BaseActivity) v.getContext()).getSupportActionBar().setTitle(R.string.suggested);
    }
}
