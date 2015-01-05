package ua.vsevolodkaganovych.testtaskmovies;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.Locale;


public class PagerAdapter extends FragmentPagerAdapter {

    private Context mContext;

    public PagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: return new FragmentTop();
            case 1: return new FragmentPopular();
            case 2: return new FragmentUpcoming();
        }
        return new FragmentTop();
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Locale l = Locale.getDefault();
        switch (position) {
            case 0: return mContext.getString(R.string.top_fragment_name).toUpperCase(l);
            case 1: return mContext.getString(R.string.popular_fragment_name).toUpperCase(l);
            case 2: return mContext.getString(R.string.upcoming_fragment_name).toUpperCase(l);
        }
        return null;
    }
}
