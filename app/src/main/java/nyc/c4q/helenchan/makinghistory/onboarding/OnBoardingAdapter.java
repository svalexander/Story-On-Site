package nyc.c4q.helenchan.makinghistory.onboarding;

import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import nyc.c4q.helenchan.makinghistory.R;

/**
 * Created by leighdouglas on 3/12/17.
 */

public class OnBoardingAdapter extends PagerAdapter{
    private List<OnboardingObject> pages = new ArrayList<>();


    @Override
    public int getCount() {
        return pages.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        ViewGroup layout = (ViewGroup) LayoutInflater.from(container.getContext()).inflate(R.layout.onboard_fragment, container, false);
        container.addView(layout);



        switch(position){
            case 0:
                //onboarding object contains layout attributes that are set here
                break;

            case 1:
                break;
            case 2:
                break;
        }
        return layout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }
}
