package fiestate.adapter;

import java.util.List;

import fiestate.sliding.SamplePagerItem;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ViewPagerAdapter extends FragmentPagerAdapter {

	
	private List<SamplePagerItem> mTabs;
	public ViewPagerAdapter(FragmentManager fragmentManager, List<SamplePagerItem> mTabs) {
		super(fragmentManager);
		this.mTabs = mTabs;
	}

    @Override
    public Fragment getItem(int i) {
    	
        return mTabs.get(i).createFragment();
    }
    
    public Bundle getBundle(int i)
    {
    	return mTabs.get(i).getBundle();
    }

    @Override
    public int getCount() {
        return mTabs.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTabs.get(position).getTitle();
    }
}