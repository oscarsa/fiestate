package fiestate.navigationviewpager;

import fiestate.adapter.NavigationAdapter;
import fiestate.adapter.NavigationItemAdapter;
import fiestate.navigationviewpagergen.R;
import fiestate.utils.Utils;
import android.content.Context;

public class NavigationList {
	
	public static NavigationAdapter getNavigationAdapter(Context context){
		
		NavigationAdapter navigationAdapter = new NavigationAdapter(context);		
		String[] menuItems = context.getResources().getStringArray(R.array.nav_menu_items);
		
		for (int i = 0; i < menuItems.length; i++) {
			
			String title = menuItems[i];				
			NavigationItemAdapter itemNavigation;				
			itemNavigation = new NavigationItemAdapter(title, Utils.iconNavigation[i]);									
			navigationAdapter.addItem(itemNavigation);
		}		
		return navigationAdapter;			
	}	
}
