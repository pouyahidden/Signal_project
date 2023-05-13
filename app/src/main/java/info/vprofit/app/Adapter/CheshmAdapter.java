package info.vprofit.app.Adapter;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import info.vprofit.app.Fragment.Massage;
import info.vprofit.app.Fragment.Unread;


public class CheshmAdapter extends FragmentPagerAdapter {    private final Context myContext;
    int totalTabs;    public CheshmAdapter(Context context, FragmentManager fm, int totalTabs) {
        super(fm);
        myContext = context;
        this.totalTabs = totalTabs;
    }    // this is for fragment tabs
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                Massage allFragment = new Massage();
                return allFragment;
            case 1:
                Unread unreadFragment = new Unread();
                return unreadFragment;

            default:
                return null;
        }
    }
    // this counts total number of tabs
    @Override
    public int getCount() {
        return totalTabs;
    }
}


