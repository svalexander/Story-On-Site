package nyc.c4q.helenchan.makinghistory.userprofileviewpager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

/**
 * Created by leighdouglas on 3/18/17.
 */

public class UserViewPagerAdapter extends FragmentPagerAdapter {

    static final int STORIES_INDEX = 0;
    static final int FAVES_INDEX = 1;
    private String tabTitles[] = new String[] { "My Stories", "Favorites" };


    public UserViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case STORIES_INDEX:
                return new UserPicsFragment();
            case FAVES_INDEX:
                return new UserFavoritesFragment();
            default:
                throw new IllegalStateException("no more than 2 fragments");
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

}

