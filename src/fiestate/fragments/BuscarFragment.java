package fiestate.fragments;

import java.util.ArrayList;
import java.util.TooManyListenersException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import fiestate.actos.FragmentListado;
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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebView.FindListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

public class BuscarFragment extends Fragment {

	Button btnBuscar;
	Button btnBuscarListado;

	int botonClick;

	// ----------------------------------

	String[] listaDias = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
			"11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21",
			"22", "23", "24", "25", "26", "27", "28", "29", "30", "31" };
	String[] listaMeses = { "Enero", "Febrero", "Marzo", "Abril", "Mayo",
			"Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre",
			"Diciembre" };
	String[] listaAnos = { "2014", "2015", "2016" };

	ToggleButton toggleFecha;

	Spinner spnDia;
	Spinner spnMes;
	Spinner spnAno;

	Boolean buscarConFecha = false;

	// ----------------------------------

	public Spinner spnProvincias;

	public Spinner spnLocalidades;

	private String[] tiposActo = { "Todos los tipos de acto", "Infantiles",
			"Taurinos", "Religiosos", "Musicales", "Otros" };
	public Spinner spnTiposActo;

	// --------------------------------

	// URL PROVINCIAS
	private static String urlprovincias = "http://fiestate.dinamice.com/ApiV2/todas_provincias.php";

	// Nombres de JSON Provincias

	private static final String TAG_PROVINCIAS = "provincias";
	private static final String TAG_IDPROV = "idprovincia";
	private static final String TAG_NOMBREPROV = "nombre";

	// Provincias JSONArray
	JSONArray provincias = null;

	ArrayList<String> listaNombreProvincias = new ArrayList<String>();
	public ArrayList<String> listaIdProvincias = new ArrayList<String>();

	// --------------------------------

	String provinciaSelec;
	String localidadSelec;
	String tipoSelec;

	// URL LOCALIDADES
	private String urllocalidades = "http://fiestate.dinamice.com/ApiV2/localidades_provincia.php?idprovincia=" + 38;

	// Nombres de JSON Localidades

	private static final String TAG_LOCALIDADES = "localidades";
	private static final String TAG_IDLOC = "idlocalidad";
	private static final String TAG_NOMBRELOC = "nombre";

	// Provincias JSONArray
	JSONArray localidades = null;

	ArrayList<String> listaNombreLocalidades = new ArrayList<String>();
	public ArrayList<String> listaIdLocalidades = new ArrayList<String>();

	// --------------------------------

	// URL FECHAS
	private String urlFechas = "http://fiestate.dinamice.com/ApiV2/buscar_fechas.php?idprovincia="
			+ provinciaSelec
			+ "&idlocalidad="
			+ localidadSelec
			+ "&idtipoacto=" + tipoSelec;

	// Nombres de JSON
	private static final String TAG_FECHAS = "actos";
	private static final String TAG_FECHA = "Fecha";

	// Actos JSONArray
	JSONArray fechas = null;

	// ListView Hashmap
	ArrayList<String> listaFechas = new ArrayList<String>();

	String[] arrayFechas;

	// --------------------------

	public static BuscarFragment newInstance() {
		Bundle bundle = new Bundle();

		BuscarFragment fragment = new BuscarFragment();
		fragment.setArguments(bundle);

		return fragment;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		toggleFecha.setChecked(false);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_busqueda, container,
				false);

		rootView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));

		// SPINNERS
		spnProvincias = (Spinner) rootView.findViewById(R.id.SpnProvincia);
		spnLocalidades = (Spinner) rootView.findViewById(R.id.SpnLocalidad);
		spnTiposActo = (Spinner) rootView.findViewById(R.id.SpnTipoActo);

		spnDia = (Spinner) rootView.findViewById(R.id.spnDia);
		spnMes = (Spinner) rootView.findViewById(R.id.spnMes);
		spnAno = (Spinner) rootView.findViewById(R.id.spnAno);

		// Obtenemos las PROVINCIAS del API

		new GetProvincias().execute();

		spnProvincias.setOnItemSelectedListener(new ProvinciasOnItemSelected());

		ArrayAdapter<String> adapTipos = new ArrayAdapter<String>(
				getActivity(), android.R.layout.simple_spinner_item, tiposActo);
		adapTipos
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnTiposActo.setAdapter(adapTipos);

		btnBuscar = (Button) rootView.findViewById(R.id.btnBuscar);
		btnBuscarListado = (Button) rootView.findViewById(R.id.btnListado);
		toggleFecha = (ToggleButton) rootView.findViewById(R.id.toogleFecha);

		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		setHasOptionsMenu(true);

		btnBuscar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				int sTipoSelec = (int) spnTiposActo.getSelectedItemId();
				tipoSelec = null;

				switch (sTipoSelec) {
				case 0:
					tipoSelec = null;
					break;
				case 1:
					tipoSelec = "1";
					break;
				case 2:
					tipoSelec = "3";
					break;
				case 3:
					tipoSelec = "2";
					break;
				case 4:
					tipoSelec = "4";
					break;
				case 5:
					tipoSelec = "5";
					break;
				default:
					tipoSelec = null;
					break;
				}

				String[] arrayIdProvincias = new String[listaIdProvincias
						.size()];
				listaIdProvincias.toArray(arrayIdProvincias);
				provinciaSelec = arrayIdProvincias[(int) spnProvincias
						.getSelectedItemId()];

				if (listaIdLocalidades.size() != 0) {
					botonClick = 1;

					String[] arrayIdLocalidades = new String[listaIdLocalidades
							.size()];
					listaIdLocalidades.toArray(arrayIdLocalidades);
					localidadSelec = arrayIdLocalidades[(int) spnLocalidades
							.getSelectedItemId()];

					if (tipoSelec == null) {
						urlFechas = "http://fiestate.dinamice.com/ApiV2/buscar_fechas.php?idprovincia="
								+ provinciaSelec
								+ "&idlocalidad="
								+ localidadSelec;
					}

					Log.i("BUSCARFRAGMENT", "  \nProvincia: " + provinciaSelec
							+ "\nLocalidad: " + localidadSelec + "\nTipo: "
							+ tipoSelec);

					new GetFechas().execute();

				} else {
					Log.i("BUSCARFRAGMENT", "No se puede buscar");
					Toast toast = Toast.makeText(getActivity(),
							"Seleccione otra localidad", Toast.LENGTH_SHORT);
					toast.show();

				}

			}
		});

		btnBuscarListado.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				int sTipoSelec = (int) spnTiposActo.getSelectedItemId();
				tipoSelec = null;

				switch (sTipoSelec) {
				case 0:
					tipoSelec = null;
					break;
				case 1:
					tipoSelec = "1";
					break;
				case 2:
					tipoSelec = "3";
					break;
				case 3:
					tipoSelec = "2";
					break;
				case 4:
					tipoSelec = "4";
					break;
				case 5:
					tipoSelec = "5";
					break;
				default:
					tipoSelec = null;
					break;
				}

				String[] arrayIdProvincias = new String[listaIdProvincias
						.size()];
				listaIdProvincias.toArray(arrayIdProvincias);
				provinciaSelec = arrayIdProvincias[(int) spnProvincias
						.getSelectedItemId()];

				if (listaIdLocalidades.size() != 0) {
					botonClick = 2;

					String[] arrayIdLocalidades = new String[listaIdLocalidades
							.size()];
					listaIdLocalidades.toArray(arrayIdLocalidades);
					localidadSelec = arrayIdLocalidades[(int) spnLocalidades
							.getSelectedItemId()];

					if (tipoSelec == null) {
						urlFechas = "http://fiestate.dinamice.com/ApiV2/buscar_fechas.php?idprovincia="
								+ provinciaSelec
								+ "&idlocalidad="
								+ localidadSelec;
					}

					Log.i("BUSCARFRAGMENT", "  \nProvincia: " + provinciaSelec
							+ "\nLocalidad: " + localidadSelec + "\nTipo: "
							+ tipoSelec);

					new GetFechas().execute();

				} else {
					Log.i("BUSCARFRAGMENT", "No se puede buscar");
					Toast toast = Toast.makeText(getActivity(),
							"Seleccione otra localidad", Toast.LENGTH_SHORT);
					toast.show();

				}

			}
		});

		toggleFecha.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (toggleFecha.isChecked()) {
					buscarConFecha = true;
					activarFecha();
				} else {
					buscarConFecha = false;
					activarFecha();
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

	public void activarFecha() {
		if (buscarConFecha) {

			// Dias
			ArrayAdapter<String> adapDias = new ArrayAdapter<String>(
					getActivity(), android.R.layout.simple_spinner_item,
					listaDias);
			adapDias.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spnDia.setAdapter(adapDias);

			// Meses
			ArrayAdapter<String> adapMeses = new ArrayAdapter<String>(
					getActivity(), android.R.layout.simple_spinner_item,
					listaMeses);
			adapMeses
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spnMes.setAdapter(adapMeses);

			// Anos
			ArrayAdapter<String> adapAnos = new ArrayAdapter<String>(
					getActivity(), android.R.layout.simple_spinner_item,
					listaAnos);
			adapAnos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spnAno.setAdapter(adapAnos);

		} else {
			spnDia.setAdapter(null);
			spnMes.setAdapter(null);
			spnAno.setAdapter(null);
		}
	}

	public class ProvinciasOnItemSelected implements OnItemSelectedListener {
		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			// TODO Auto-generated method stub

			String[] arrayIdProvincias = new String[listaIdProvincias.size()];
			listaIdProvincias.toArray(arrayIdProvincias);

			String idprov = arrayIdProvincias[(int) parent
					.getItemIdAtPosition(position)];

			urllocalidades = "http://fiestate.dinamice.com/ApiV2/localidades_provincia.php?idprovincia="
					+ idprov;

			// Mostramos las localidades de la provincia seleccionada
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
	private class GetProvincias extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			try {

				String[] arrayIdProvincias = new String[listaIdProvincias
						.size()];
				String[] arrayNombresProvincias = new String[listaNombreProvincias
						.size()];

				listaIdProvincias.toArray(arrayIdProvincias);
				listaNombreProvincias.toArray(arrayNombresProvincias);

				ArrayAdapter<String> adapProvincias = new ArrayAdapter<String>(
						getActivity(), android.R.layout.simple_spinner_item,
						arrayNombresProvincias);
				adapProvincias
						.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				spnProvincias.setAdapter(adapProvincias);

			} catch (NullPointerException e) {
				e.printStackTrace();
				Log.w("BUSCARFRAGMENT",
						"ERROR NULL POINTER EXCEPTION, Al establecer adaptador a la lista");
			}

		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub

			// Creamos handler del servicio
			ServiceHandler sh = new ServiceHandler();

			// Petición a lal URL para obtener respuesta
			String jsonStr = sh.makeServiceCall(urlprovincias,
					ServiceHandler.GET);

			Log.i("BUSCARFRAGMENT", "JSON Obtenido -> " + jsonStr);

			if (jsonStr != null) {
				try {

					JSONObject jsonobj = new JSONObject(jsonStr);

					// Obtenemos el Array JSON
					provincias = jsonobj.getJSONArray(TAG_PROVINCIAS);

					// Bucle de provincias
					for (int i = 0; i < provincias.length(); i++) {
						JSONObject p = provincias.getJSONObject(i);

						String IdProvincia = p.getString(TAG_IDPROV);
						String Nombre = p.getString(TAG_NOMBREPROV);

						// Anadimos los valores a las dos listas
						listaIdProvincias.add(IdProvincia);
						listaNombreProvincias.add(Nombre);

					}

				} catch (JSONException e) {
					e.printStackTrace();
					Log.w("BUSCARFRAGMENT", "ERROR Capturar y mostrar Json");
				}

			} else {
				Log.i("BUSCARFRAGMENT",
						"No se ha podido obtener ningún dato de la URL");
			}

			return null;
		}

	}

	/**
	 * Clase async task para obtener el JSON haciendo una llamada HTTP
	 */
	private class GetLocalidades extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

			listaIdLocalidades.clear();
			listaNombreLocalidades.clear();

		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			try {
				if (listaNombreLocalidades.isEmpty()) {
					listaNombreLocalidades
							.add("No existe ninguna localidad con actos");
				}

				String[] arrayIdLocalidades = new String[listaIdLocalidades
						.size()];
				String[] arrayNombresLocalidades = new String[listaNombreLocalidades
						.size()];

				listaIdLocalidades.toArray(arrayIdLocalidades);
				listaNombreLocalidades.toArray(arrayNombresLocalidades);

				ArrayAdapter<String> adapLocalidades = new ArrayAdapter<String>(
						getActivity(), android.R.layout.simple_spinner_item,
						arrayNombresLocalidades);
				adapLocalidades
						.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				spnLocalidades.setAdapter(adapLocalidades);

			} catch (NullPointerException e) {
				e.printStackTrace();
				Log.w("BUSCARFRAGMENT",
						"ERROR NULL POINTER EXCEPTION, Al establecer adaptador a la lista");
			}

		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub

			// Creamos handler del servicio
			ServiceHandler sh = new ServiceHandler();

			// Petición a lal URL para obtener respuesta
			String jsonStr = sh.makeServiceCall(urllocalidades,
					ServiceHandler.GET);

			Log.i("BUSCARFRAGMENT", "URL -> " + urllocalidades);
			Log.i("BUSCARFRAGMENT", "JSON Obtenido -> " + jsonStr);

			if (jsonStr != null) {

				try {

					JSONObject jsonobj = new JSONObject(jsonStr);

					// Obtenemos el Array JSON
					localidades = jsonobj.getJSONArray(TAG_LOCALIDADES);

					// Bucle de localidades
					for (int i = 0; i < localidades.length(); i++) {
						JSONObject l = localidades.getJSONObject(i);

						String IdLocalidad = l.getString(TAG_IDLOC);
						String Nombre = l.getString(TAG_NOMBRELOC);

						// Anadimos los valores a las dos listas
						listaIdLocalidades.add(IdLocalidad);
						listaNombreLocalidades.add(Nombre);

					}

				} catch (JSONException e) {
					e.printStackTrace();
					Log.w("BUSCARFRAGMENT", "ERROR Capturar y mostrar Json");

				}
			} else {
				Log.i("BUSCARFRAGMENT",
						"No se ha podido obtener ningún dato de la URL");
			}

			return null;
		}
	}

	// -----------------------

	/**
	 * Clase async task para obtener el JSON haciendo una llamada HTTP
	 */
	private class GetFechas extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			arrayFechas = new String[listaFechas.size()];

			listaFechas.toArray(arrayFechas);

			for (int i = 0; i < arrayFechas.length; i++) {
				Log.i("BUSCARFRAGMENT", "Fecha: " + arrayFechas[i]);
			}

			if (botonClick == 1) {
				// Cambio a fragment de resultados
				Fragment viewpagerFrag = new ViewPagerFragment();

				Bundle bundle = new Bundle();
				bundle.putStringArray("fechas", arrayFechas);
				bundle.putString("provincia", provinciaSelec);
				bundle.putString("localidad", localidadSelec);
				bundle.putString("tipo", tipoSelec);

				viewpagerFrag.setArguments(bundle);

				android.support.v4.app.FragmentTransaction ft = getFragmentManager()
						.beginTransaction();
				ft.replace(((ViewGroup) getView().getParent()).getId(),
						viewpagerFrag);
				ft.addToBackStack(null);
				ft.commit();
			} else {
				// Cambio a fragment de resultados
				Fragment fragmentListado = new FragmentListado();

				Bundle bundle = new Bundle();
				bundle.putStringArray("fechas", arrayFechas);
				bundle.putString("provincia", provinciaSelec);
				bundle.putString("localidad", localidadSelec);
				bundle.putString("tipo", tipoSelec);
				String fech = null;
				if (buscarConFecha) {
					int mes = spnMes.getSelectedItemPosition() + 1;
					fech = spnAno.getSelectedItem().toString() + "-"
							+ Integer.toString(mes) + "-"
							+ spnDia.getSelectedItem().toString();
				}
				bundle.putString("fecha", fech);

				fragmentListado.setArguments(bundle);

				android.support.v4.app.FragmentTransaction ft = getFragmentManager()
						.beginTransaction();
				ft.replace(((ViewGroup) getView().getParent()).getId(),
						fragmentListado);
				ft.addToBackStack(null);
				ft.commit();
			}

			listaIdProvincias.clear();
			listaNombreProvincias.clear();
			listaFechas.clear();

		}

		@Override
		protected Void doInBackground(Void... params) {

			// Creamos handler del servicio
			ServiceHandler sh = new ServiceHandler();

			// Petición a lal URL para obtener respuesta
			String jsonStr = sh.makeServiceCall(urlFechas, ServiceHandler.GET);

			Log.i("BUSCARFRAGMENT", "URL -> " + urlFechas);

			Log.i("BUSCARFRAGMENT", "JSON Obtenido -> " + jsonStr);

			if (jsonStr != null) {
				try {

					JSONObject jsonobj = new JSONObject(jsonStr);

					// Obtenemos el Array JSON
					fechas = jsonobj.getJSONArray(TAG_FECHAS);

					// Bucle de fechas
					for (int i = 0; i < fechas.length(); i++) {
						JSONObject f = fechas.getJSONObject(i);

						String Fecha = f.getString(TAG_FECHA);

						// Anadimos los valores a las dos listas
						listaFechas.add(Fecha);

					}

				} catch (JSONException e) {
					e.printStackTrace();
					Log.w("BUSCARFRAGMENT", "ERROR Capturar y mostrar Json");
				}

			} else {
				Log.i("BUSCARFRAGMENT",
						"No se ha podido obtener ningún dato de la URL");
			}

			return null;
		}

	}

}
