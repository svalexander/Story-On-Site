package nyc.c4q.helenchan.makinghistory.onboarding;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.relex.circleindicator.CircleIndicator;
import nyc.c4q.helenchan.makinghistory.BaseActivity;
import nyc.c4q.helenchan.makinghistory.R;

/**
 * Created by leighdouglas on 3/12/17.
 */

public class OnboardingFragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.onboard_fragment, container, false); //switch out layouts
        ViewPager vp = (ViewPager) root.findViewById(R.id.onboarding_viewpager); // switch out viewpager id
        vp.setAdapter(new OnBoardingAdapter());
        CircleIndicator indicator = (CircleIndicator) root.findViewById(R.id.indicator);
        indicator.setViewPager(vp);
        setActionBarTitle(root);
        return root;
    }

    private void setActionBarTitle(View v) {
        ((BaseActivity) v.getContext()).getSupportActionBar().setTitle(R.string.suggested);
    }
}
