package fiestate.sliding;

import fiestate.actos.FragmentListado;
import android.os.Bundle;
import android.support.v4.app.Fragment;

public class SamplePagerItem {
	
	private final CharSequence mTitle;
    private final int mIndicatorColor;
    private final int mDividerColor;
    private final int position;
    
    private final Bundle miBundle;
        
    private Fragment[] listFragments;
    public SamplePagerItem(Bundle bundle, int position, CharSequence title, int indicatorColor, int dividerColor) {
        this.mTitle = title;
        this.position = position;
        this.mIndicatorColor = indicatorColor;
        this.mDividerColor = dividerColor;
        this.miBundle = bundle;
        
        listFragments = new Fragment[] {FragmentListado.newInstance()};        
    }

    public Fragment createFragment() {
		return listFragments[position];
    }
    
    public Bundle getBundle()
    {
    	return miBundle;
    }

    public CharSequence getTitle() {
        return mTitle;
    }

    public int getIndicatorColor() {
        return mIndicatorColor;
    }

    public int getDividerColor() {
        return mDividerColor;
    }
}
