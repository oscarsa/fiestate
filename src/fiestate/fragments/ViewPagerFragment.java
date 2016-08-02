package fiestate.fragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import fiestate.actos.FragmentListado;
import fiestate.adapter.ViewPagerAdapter;
import fiestate.data.ServiceHandler;
import fiestate.fragments.BuscarFragment.ProvinciasOnItemSelected;
import fiestate.navigationviewpagergen.R;
import fiestate.sliding.SamplePagerItem;
import fiestate.sliding.SlidingTabLayout;
import fiestate.utils.Utils;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class ViewPagerFragment extends Fragment{
	
	
	private List<SamplePagerItem> mTabs = new ArrayList<SamplePagerItem>();
	

	
	public String[] fechas;
	public String provincia;
	public String localidad;
	public String tipo;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Recibimos Bundle
        Bundle bundle = this.getArguments();
        fechas = bundle.getStringArray("fechas");
        provincia = bundle.getString("provincia");
        localidad = bundle.getString("localidad");
        tipo = bundle.getString("tipo");
        
  
	  
        for(int i = 0; i<fechas.length; i++)
        {
       
			
			Bundle bundleEnviar = new Bundle();
			bundleEnviar.putStringArray("fechas", fechas);
			bundleEnviar.putString("provincia", provincia);
			bundleEnviar.putString("localidad", localidad);
			bundleEnviar.putString("tipo", tipo);
			


        	mTabs.add(new SamplePagerItem(bundleEnviar, 0, fechas[i], getResources().getColor(Utils.colors[3]), Color.GRAY));
        }
        

        
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	
    	
    	
    	
    	
    	
    	
        return inflater.inflate(R.layout.viewpager_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
    	ViewPager mViewPager = (ViewPager) view.findViewById(R.id.mPager);
    	
    	mViewPager.setOffscreenPageLimit(3); 
        mViewPager.setAdapter(new ViewPagerAdapter(getChildFragmentManager(), mTabs));

        SlidingTabLayout mSlidingTabLayout = (SlidingTabLayout) view.findViewById(R.id.mTabs);
        mSlidingTabLayout.setBackgroundResource(R.color.gray_navegation);
        mSlidingTabLayout.setViewPager(mViewPager);

        mSlidingTabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {

            @Override
            public int getIndicatorColor(int position) {
                return mTabs.get(position).getIndicatorColor();
            }

            @Override
            public int getDividerColor(int position) {
                return mTabs.get(position).getDividerColor();
            }
        });
    }

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		
		
		super.onResume();

		
	}
	
	
	

    
}












