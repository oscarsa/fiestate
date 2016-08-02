package fiestate.fragments;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import fiestate.actos.Acto;
import fiestate.data.ServiceHandler;

import fiestate.navigationviewpagergen.R;
import fiestate.utils.Menus;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class AddActoFragment extends Fragment{

	private Spinner spnAddProvincia;
	private Spinner spnAddLocalidad;
	private Spinner spnAddTipo;
	
	private EditText addTitulo;
	private EditText addDescripcion;
	private EditText addFecha;
	private EditText addHora;
	
	private Button btnAdd;

	private String[] tiposActo = {"Acto infantil","Acto taurino","Acto religioso","Acto musical","Otro tipo de acto"};
	
	
	// --------------------------------

    //URL PROVINCIAS
    private static String urlprovincias = "http://fiestate.dinamice.com/ApiV2/todas_provincias.php";
    
    //Nombres de JSON Provincias
    
    private static final String TAG_PROVINCIAS = "provincias";
    private static final String TAG_IDPROV = "idprovincia";
    private static final String TAG_NOMBREPROV = "nombre";
    
    //Provincias JSONArray
    JSONArray provincias = null;
    
    ArrayList<String> listaNombreProvincias = new ArrayList<String>();
    public ArrayList<String> listaIdProvincias = new ArrayList<String>();


   
 // --------------------------------	

    String provinciaSelec;
    String localidadSelec;
    String tipoSelec;
    
    String nombre;
    String descripcion;
    String hora;
    String fecha;
    String lugar = "";
    
    
    //URL LOCALIDADES
    private  String urllocalidades = "http://fiestate.dinamice.com/ApiV2/localidades_provincia.php?idprovincia="+provinciaSelec;
    
    //Nombres de JSON Localidades
    
    private static final String TAG_LOCALIDADES = "localidades";
    private static final String TAG_IDLOC = "idlocalidad";
    private static final String TAG_NOMBRELOC = "nombre";
    
    //Provincias JSONArray
    JSONArray localidades = null;
    
    ArrayList<String> listaNombreLocalidades = new ArrayList<String>();
    public  ArrayList<String> listaIdLocalidades = new ArrayList<String>();

   
 // --------------------------------	
	//URL AÑADIR ACTO
    private String urlacto = "http://fiestate.dinamice.com/ApiV2/crear_acto.php?nombre="+nombre+"&descripcion="+descripcion+"&horainicio="+hora+"&idtipoacto="+tipoSelec+"&idlocalidad="+localidadSelec+"&fecha='"+fecha+"'&lugar="+lugar;
    
    
    public static AddActoFragment newInstance() {
        AddActoFragment fragment = new AddActoFragment();
        return fragment;
    }	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
			
		View rootView = inflater.inflate(R.layout.fragment_add_acto, container, false);
		
		spnAddProvincia = (Spinner) rootView.findViewById(R.id.spnAddProvincia);
		spnAddLocalidad = (Spinner) rootView.findViewById(R.id.spnAddLocalidad);
		spnAddTipo = (Spinner) rootView.findViewById(R.id.spnAddTipo);
		
		addTitulo = (EditText) rootView.findViewById(R.id.addTitulo);
		addDescripcion = (EditText) rootView.findViewById(R.id.addDescripcion);
		addFecha = (EditText) rootView.findViewById(R.id.addFecha);
		addHora = (EditText) rootView.findViewById(R.id.addHora);
		
		btnAdd = (Button) rootView.findViewById(R.id.btnAdd);
		
		//Obtenemos las PROVINCIAS del API
		
		new GetProvincias().execute();
		
		spnAddProvincia.setOnItemSelectedListener(new ProvinciasOnItemSelected());
	
		ArrayAdapter<String> adapTipos = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, tiposActo);
		adapTipos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnAddTipo.setAdapter(adapTipos);
		
		
		
		rootView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT ));	
		
		return rootView;
	}
			
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		setHasOptionsMenu(true);
		
		 btnAdd.setOnClickListener(new  OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					
					int sTipoSelec = (int) spnAddTipo.getSelectedItemId();
					tipoSelec = null;
					

					switch (sTipoSelec) {
					case 0:
						tipoSelec = "1";
						break;
					case 1:
						tipoSelec = "3";
						break;
					case 2:
						tipoSelec = "2";
						break;
					case 3:
						tipoSelec = "4";
						break;
					case 4:
						tipoSelec = "5";
						break;
					default:
						tipoSelec = null;
						break;
					}
					
					
					String[] arrayIdProvincias = new String[listaIdProvincias.size()];	
					listaIdProvincias.toArray(arrayIdProvincias);
					provinciaSelec = arrayIdProvincias[(int) spnAddProvincia.getSelectedItemId()];
					
					if(listaIdLocalidades.size()!=0)
					{
					
						
						String[] arrayIdLocalidades = new String[listaIdLocalidades.size()];	
						listaIdLocalidades.toArray(arrayIdLocalidades);
						localidadSelec = arrayIdLocalidades[(int) spnAddLocalidad.getSelectedItemId()];
						
						cogerDatos();
						
			
						
					}
					else
					{
						Log.i("BUSCARFRAGMENT", "No se puede buscar");	
						Toast toast = Toast.makeText(getActivity(), "Seleccione otra localidad", Toast.LENGTH_SHORT);
						toast.show();

					}
				}
					
	});
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu, inflater);		
		inflater.inflate(R.menu.menu, menu);
					    	   	    

	
	}	
	
	public void cogerDatos()
	{
		nombre = addTitulo.getText().toString();
		descripcion = addDescripcion.getText().toString();
		hora = addHora.getText().toString();
		fecha = addFecha.getText().toString();
		
		Log.i("ADDACTOFRAGMENT", "Datos: \nProvincia: "+provinciaSelec+"\nLocalidad: "+localidadSelec+"\nTipo: "+tipoSelec+"\nTitulo: "+nombre+"\nDescripcion: "+descripcion+"\nFecha: "+fecha+"\nHora: "+hora);
	
		
		
		if(!nombre.trim().isEmpty() && !descripcion.trim().isEmpty() && !hora.trim().isEmpty() && !fecha.trim().isEmpty())
		{
			new enviarActo().execute(); 
		}
		else
		{
			Toast.makeText(getActivity(), "Rellene todos datos correctamente", Toast.LENGTH_SHORT).show();
		}
		
	}


	
	public class ProvinciasOnItemSelected implements OnItemSelectedListener {
		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			// TODO Auto-generated method stub
			
			String[] arrayIdProvincias = new String[listaIdProvincias.size()];	
			listaIdProvincias.toArray(arrayIdProvincias);

			String idprov = arrayIdProvincias[(int) parent.getItemIdAtPosition(position)];
			
			urllocalidades = "http://fiestate.dinamice.com/ApiV2/localidades_provincia.php?idprovincia="+idprov;
			

			//Mostramos las localidades de la provincia seleccionada
			new GetLocalidades().execute();
			
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// TODO Auto-generated method stub
			
		}
		
		
}



    /**
     * Clase async task para obtener el JSON haciendo una llamada HTTP
     */
    private class GetProvincias extends AsyncTask<Void, Void, Void>
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
			
				try
				{
					
					String[] arrayIdProvincias = new String[listaIdProvincias.size()];
					String[] arrayNombresProvincias = new String[listaNombreProvincias.size()];
					
					listaIdProvincias.toArray(arrayIdProvincias);
					listaNombreProvincias.toArray(arrayNombresProvincias);
					
					ArrayAdapter<String> adapProvincias = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, arrayNombresProvincias);
					adapProvincias.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					spnAddProvincia.setAdapter(adapProvincias);

				
				}
				catch (NullPointerException e)
				{
					e.printStackTrace();
					Log.w("ADDACTOFRAGMENT", "ERROR NULL POINTER EXCEPTION, Al establecer adaptador a la lista");
				}
				
	
			
		}

		@Override
		protected Void doInBackground(Void... params) 
		{
			// TODO Auto-generated method stub
		
	//Creamos handler del servicio
			ServiceHandler sh = new ServiceHandler();
			
			//Petición a lal URL para obtener respuesta
			String jsonStr = sh.makeServiceCall(urlprovincias, ServiceHandler.GET);
			
			
			Log.i("ADDACTOFRAGMENT", "JSON Obtenido -> "+jsonStr);

			if(jsonStr != null)
			{		
				try
				{
							
					JSONObject jsonobj = new JSONObject(jsonStr);
					
					//Obtenemos el Array JSON
					provincias = jsonobj.getJSONArray(TAG_PROVINCIAS);
							
					//Bucle de provincias
					for (int i = 0; i < provincias.length(); i++)
					{
						JSONObject p = provincias.getJSONObject(i);
			
						String IdProvincia = p.getString(TAG_IDPROV);
						String Nombre = p.getString(TAG_NOMBREPROV);
						
						//Añadimos los valores a las dos listas
						listaIdProvincias.add(IdProvincia);
						listaNombreProvincias.add(Nombre);	
						
					}
					
				}
				catch (JSONException e)
				{
					e.printStackTrace();
					Log.w("ADDACTOFRAGMENT", "ERROR Capturar y mostrar Json");
				}
				
			}
			else
			{
				Log.i("ADDACTOFRAGMENT", "No se ha podido obtener ningún dato de la URL");
			}

			return null;
		}
    	
    	
    }
    
    /**
     * Clase async task para obtener el JSON haciendo una llamada HTTP
     */
    private class GetLocalidades extends AsyncTask<Void, Void, Void>

    
    {

		@Override
		protected void onPreExecute() 
		{
			// TODO Auto-generated method stub
			super.onPreExecute();
			
			listaIdLocalidades.clear();
			listaNombreLocalidades.clear();
			
		}

		@Override
		protected void onPostExecute(Void result) 
		{
			// TODO Auto-generated method stub
			super.onPostExecute(result);			
			
				try
				{
					if(listaNombreLocalidades.isEmpty())
					{
						listaNombreLocalidades.add("No existe ninguna localidad con actos");
					}
					
					String[] arrayIdLocalidades = new String[listaIdLocalidades.size()];
					String[] arrayNombresLocalidades = new String[listaNombreLocalidades.size()];
					
					listaIdLocalidades.toArray(arrayIdLocalidades);
					listaNombreLocalidades.toArray(arrayNombresLocalidades);
					
					
					
					ArrayAdapter<String> adapLocalidades = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, arrayNombresLocalidades);
					adapLocalidades.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					spnAddLocalidad.setAdapter(adapLocalidades);

				
				}
				catch (NullPointerException e)
				{
					e.printStackTrace();
					Log.w("ADDACTOFRAGMENT", "ERROR NULL POINTER EXCEPTION, Al establecer adaptador a la lista");
				}
				
	
			
		}

		@Override
		protected Void doInBackground(Void... params) 
		{
			// TODO Auto-generated method stub
		
	//Creamos handler del servicio
			ServiceHandler sh = new ServiceHandler();
			
			//Petición a lal URL para obtener respuesta
			String jsonStr = sh.makeServiceCall(urllocalidades, ServiceHandler.GET);
			
			Log.i("ADDACTOFRAGMENT", "URL -> "+urllocalidades);
			Log.i("ADDACTOFRAGMENT", "JSON Obtenido -> "+jsonStr);
			
			if(jsonStr != null)
			{
				
				try
				{
					
					
					JSONObject jsonobj = new JSONObject(jsonStr);
					

					
					//Obtenemos el Array JSON
					localidades = jsonobj.getJSONArray(TAG_LOCALIDADES);

							
					//Bucle de localidades
					for (int i = 0; i < localidades.length(); i++)
					{
						JSONObject l = localidades.getJSONObject(i);
			
						String IdLocalidad = l.getString(TAG_IDLOC);
						String Nombre = l.getString(TAG_NOMBRELOC);
						
						//Añadimos los valores a las dos listas
						listaIdLocalidades.add(IdLocalidad);
						listaNombreLocalidades.add(Nombre);

					}
					
				}
				catch (JSONException e)
				{
					e.printStackTrace();
					Log.w("ADDACTOFRAGMENT", "ERROR Capturar y mostrar Json");
					
					

				}				
			}
			else
			{
				Log.i("ADDACTOFRAGMENT", "No se ha podido obtener ningún dato de la URL");
			}

			return null;
		}
    }


    /**
     * Clase async task para obtener el JSON haciendo una llamada HTTP
     */
    private class enviarActo extends AsyncTask<Void, Void, Void>
    {

    	String success;
    	
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
			
			if (success.equalsIgnoreCase("1"))
			{
				Toast.makeText(getActivity(), "Acto añadido correctamente", Toast.LENGTH_SHORT).show();
			}
			else
			{
				Toast.makeText(getActivity(), "Imposible añadir acto, comprueba los datos y la conexión", Toast.LENGTH_SHORT).show();	
			}
	
			
		}

		@Override
		protected Void doInBackground(Void... params) 
		{
			// TODO Auto-generated method stub
		
			
			List<NameValuePair> parametros = new ArrayList<NameValuePair>();
			parametros.add(new BasicNameValuePair("nombre", nombre));
			parametros.add(new BasicNameValuePair("descripcion", descripcion));
			parametros.add(new BasicNameValuePair("horainicio", hora));
			parametros.add(new BasicNameValuePair("idtipoacto", tipoSelec));
			parametros.add(new BasicNameValuePair("idlocalidad", localidadSelec));
			parametros.add(new BasicNameValuePair("fecha", fecha));
			parametros.add(new BasicNameValuePair("lugar", lugar));
		
			//Creamos handler del servicio
			ServiceHandler sh = new ServiceHandler();
			
			//Petición a lal URL para obtener respuesta
			String jsonStr = sh.makeServiceCall(urlacto, ServiceHandler.POST, parametros);
	
			Log.i("ADDACTOFRAGMENT", "JSON Obtenido -> "+jsonStr);

			if(jsonStr != null)
			{		
				try
				{
					
					JSONObject jsonobj = new JSONObject(jsonStr);
					
					

					success = jsonobj.getString("success");
		
		
					Log.i("ADDACTOFRAGMENT", "EXITO->"+success);
					
						
					
				}
				catch (JSONException e)
				{
					e.printStackTrace();
					Log.w("ADDACTOFRAGMENT", "ERROR Capturar y mostrar Json");
				}
				
			}
			else
			{
				Log.i("ADDACTOFRAGMENT", "No se ha podido obtener ningún dato de la URL");
			}

			return null;
		}
    	
    	
    }


}
    
