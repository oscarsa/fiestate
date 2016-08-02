package fiestate.actos;

import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import fiestate.data.ServiceHandler;
import fiestate.fragments.ViewPagerFragment;
import fiestate.navigationviewpagergen.R;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;
 
public class FragmentListado extends Fragment 
{

 
    private ListView lstListado;
    
    private ActosListener listener;
    

    //JSON URL
    ViewPagerFragment vpg = new ViewPagerFragment();
    public String urlBusqueda = "http://fiestate.dinamice.com/ApiV2/buscar_actos.php?idprovincia=1&idlocalidad=1&idtipoacto=1";
    
    //Nombres de JSON
    private static final String TAG_IDACT = "IdActo";
    private static final String TAG_NOMBRE = "Nombre";
    private static final String TAG_DESCRIPCION = "Descripcion";
    private static final String TAG_HORA = "HoraInicio";
    private static final String TAG_IDTIPO = "IdTipoActo";
    private static final String TAG_GUSTA = "NumGusta";
    private static final String TAG_NOGUSTA = "NumNoGusta";
    private static final String TAG_IDLOC = "IdLocalidad";
    private static final String TAG_FECHA = "Fecha";
    private static final String TAG_LUGAR = "Lugar";

    //Actos JSONArray
    JSONArray actos = null;
    
    //ListView Hashmap
    ArrayList<HashMap<String, String>> listaActos;
    
    
    //-------------------------------------------------------
    boolean conBundle = false;
    
    String[] fechas;
    String provincia;
    String localidad;
    String tipo;
    String fecha;
    
    
    
    public static FragmentListado newInstance() 
    {
        FragmentListado fragment = new FragmentListado();
        return fragment;
    }
 
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
 

    	try
    	{
    		//Recibimos Bundle
            Bundle bundle = this.getArguments();
            fechas = bundle.getStringArray("fechas");
            provincia = bundle.getString("provincia");
            localidad = bundle.getString("localidad");
            tipo = bundle.getString("tipo");
            fecha = bundle.getString("fecha");
            
            conBundle = true;
            
    	}
        catch(NullPointerException e)
        {
        	Log.w("FRAGMENTLISTADO", "ERROR NULL POINTER EXCEPTION, SIN BUNDLE");
        }
    	
    	if(conBundle)
    	{
    		if(tipo==null && fecha==null)
    		{
    			urlBusqueda = "http://fiestate.dinamice.com/ApiV2/buscar_actos.php?idprovincia="+provincia+"&idlocalidad="+localidad;
    		}
    		else if(tipo!=null && fecha==null)
    		{
    			urlBusqueda = "http://fiestate.dinamice.com/ApiV2/buscar_actos.php?idprovincia="+provincia+"&idlocalidad="+localidad+"&idtipoacto="+tipo;
    		}
    		else if(tipo == null && fecha!=null)
    		{
    			urlBusqueda = "http://fiestate.dinamice.com/ApiV2/buscar_actos.php?idprovincia="+provincia+"&idlocalidad="+localidad+"&fecha='"+fecha+"'";

    		}
    		else if(tipo!=null && fecha!=null)
    		{
    			urlBusqueda = "http://fiestate.dinamice.com/ApiV2/buscar_actos.php?idprovincia="+provincia+"&idlocalidad="+localidad+"&idtipoacto="+tipo+"&fecha='"+fecha+"'";

    		}
    		
    	}
    	
        return inflater.inflate(R.layout.fragment_listado, container, false);
    }
 
    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);
 
        //--------------------------
        
        listaActos = new ArrayList<HashMap<String, String>>();
 
        new GetActos().execute();
        
        //---------------------------
        
        lstListado = (ListView)getView().findViewById(R.id.LstListado);
        
    	lstListado.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if (listener!=null) {
                    listener.onActoSeleccionado(
                        (Acto)lstListado.getAdapter().getItem(position));

                }
				
			}
		});
    	
    
    
    }
    
    
    

    public interface ActosListener {
        void onActoSeleccionado(Acto a);
    }
 
    public void setActosListener(ActosListener listener) {
        this.listener=listener;
    }
    
    
    
    
    /**
     * Clase async task para obtener el JSON haciendo una llamada HTTP
     */
    private class GetActos extends AsyncTask<Void, Void, Void>
    {

		@Override
		protected void onPreExecute() 
		{
			// TODO Auto-generated method stub
			super.onPreExecute();
			

			
		}

		@Override
		protected void onPostExecute(Void result) 
		{
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			
			if(lstListado.getCount()<1)
			{
			
				try
				{
					//Actualizar los datos dentro del ListView
					ListAdapter adapter = new SimpleAdapter(getActivity().getApplicationContext(), listaActos, R.layout.listitem_acto, 
							new String[] {TAG_NOMBRE,  TAG_DESCRIPCION}, new int[] {R.id.LblTitulo, R.id.LblHora});

					
					lstListado.setAdapter(adapter);
					
				
					
					
					
				}
				catch (NullPointerException e)
				{
					e.printStackTrace();
					Log.w("FRAGMENTLISTADO", "ERROR NULL POINTER EXCEPTION, Al establecer adaptador a la lista");
				}
				
			
			}
			
		}

		@Override
		protected Void doInBackground(Void... params) 
		{
			// TODO Auto-generated method stub
		
		
			
			
			//Creamos handler del servicio
			ServiceHandler sh = new ServiceHandler();
			
			//Petición a lal URL para obtener respuesta
			String jsonStr = sh.makeServiceCall(urlBusqueda, ServiceHandler.GET);
			
			Log.i("FRAGMENTLISTADO", " URL -> "+urlBusqueda);
			Log.i("FRAGMENTLISTADO", "JSON Obtenido -> "+jsonStr);
			
			if(lstListado.getCount()<1)
			{
			
			
				if(jsonStr != null)
				{

					
					try
					{
						
						JSONObject jsonobj = new JSONObject(jsonStr);
						
						//Obtenemos el Array JSON
						actos = jsonobj.getJSONArray("actos");
						
	
						//Bucle de actos
						for (int i = 0; i < actos.length(); i++)
						{
							
							
							
							JSONObject a = actos.getJSONObject(i);
							
							String IdActo = a.getString(TAG_IDACT);
							String Nombre = a.getString(TAG_NOMBRE);
							String Descripcion = a.getString(TAG_DESCRIPCION);
							String HoraInicio = a.getString(TAG_HORA);
							String IdTipoActo = a.getString(TAG_IDTIPO);
							String NumGusta = a.getString(TAG_GUSTA);
							String NumNoGusta = a.getString(TAG_NOGUSTA);
							String IdLocalidad = a.getString(TAG_IDLOC);
							String Fecha = a.getString(TAG_FECHA);
							String Lugar = a.getString(TAG_LUGAR);

							
							//Hashmap para cada acto
							HashMap<String, String> acto = new HashMap<String, String>();
							
							//Añadimos cada acto con clave - valor
							acto.put(TAG_NOMBRE, Nombre);
							acto.put(TAG_DESCRIPCION, Descripcion);
							
							//Añadir actos a la lista
							listaActos.add(acto);
							
							
							
						}
						
					}
					catch (JSONException e)
					{
						e.printStackTrace();
						Log.w("FRAGMENTLISTADO", "ERROR Capturar y mostrar Json");
						
						
					}
					
				}
				else
				{
					Log.i("FRAGMENTLISTADO", "No se ha podido obtener ningún dato de la URL");
	
				}
			}
			else
			{
				Log.i("FRAGMENTLISTADO", "Lista llena");
			}
			
			
			
			
			return null;
		}
    	
    	
    }
    

    
}

























