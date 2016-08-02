package fiestate.fragments;
import fiestate.navigationviewpagergen.R;
import fiestate.utils.Menus;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.TextView;

public class MyAppsFragment extends Fragment {
	
	private TextView txtFragmentDownload;
	private boolean searchCheck;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub		
		View rootView = inflater.inflate(R.layout.myapps_fragment, container, false);
		
		txtFragmentDownload = (TextView) rootView.findViewById(R.id.txtFragmentDownload);
		txtFragmentDownload.setText(R.string.fragment_my_apps);
		
		rootView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT ));		
		return rootView;		
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		setHasOptionsMenu(true);
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu, inflater);		
		inflater.inflate(R.menu.menu, menu);
		 	    
	
  	    
		searchCheck = false;	
	}	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		

		return true;
	}	

	private OnQueryTextListener OnQuerySearchView = new OnQueryTextListener() {
		
		@Override
		public boolean onQueryTextSubmit(String arg0) {
			// TODO Auto-generated method stub
			return false;
		}
		
		@Override
		public boolean onQueryTextChange(String arg0) {
			// TODO Auto-generated method stub
			if (searchCheck){
				// implement your search here
			}
			return false;
		}
	};
}
