package fiestate.utils;

import fiestate.navigationviewpagergen.R;
import android.content.Context;

public class Utils {

	//Set all the navigation icons and always to set "zero 0" for the item is a category
	public static int[] iconNavigation = new int[] { 
		0, 0, 0, 0, 0, 0};	
	
	//get title of the item navigation
	public static String getTitleItem(Context context, int posicion){		
		String[] titulos = context.getResources().getStringArray(R.array.nav_menu_items);  
		return titulos[posicion];
	} 
	
	public static int[] colors = new int[] { 
		R.color.blue_dark, R.color.blue_dark, R.color.red_dark, R.color.red_light,
		R.color.green_dark, R.color.green_light, R.color.orange_dark, R.color.orange_light,
		R.color.purple_dark, R.color.purple_light};	
}
