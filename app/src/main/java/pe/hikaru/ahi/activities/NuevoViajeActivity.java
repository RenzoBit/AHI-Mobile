package pe.hikaru.ahi.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;

import pe.hikaru.ahi.bean.Vehiculo;
import pe.hikaru.ahi.bean.Viaje;
import pe.hikaru.ahi.util.JSONHttpClient;
import pe.hikaru.ahi.util.ServiceUrl;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class NuevoViajeActivity extends ListActivity {

	ArrayList<HashMap<String, String>> listaVehiculo;
	int idvehiculo = 0;
	EditText txtDescripcion;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nuevo_viaje);
		inicializa();
		listaVehiculo = new ArrayList<HashMap<String, String>>();
		new listarVehiculo().execute();
	}

	private void inicializa() {
		txtDescripcion = new EditText(NuevoViajeActivity.this);
		txtDescripcion.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
		txtDescripcion.setHeight(70);
		ListView lstVehiculo = getListView();
		lstVehiculo.setOnItemClickListener(lstVehiculoClick);
	}

	class listarVehiculo extends AsyncTask<String, String, String> {

		private ProgressDialog progressDialog;

		@Override
		protected String doInBackground(String... params) {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			JSONHttpClient jsonHttpClient = new JSONHttpClient();
			Vehiculo[] l = jsonHttpClient.Get(ServiceUrl.VEHICULO, nameValuePairs, Vehiculo[].class);
			if (l.length > 0)
				for (Vehiculo o : l) {
					HashMap<String, String> mapVehiculo = new HashMap<String, String>();
					mapVehiculo.put(Vehiculo.VEHICULO_ID, String.valueOf(o.getIdvehiculo()));
					mapVehiculo.put(Vehiculo.VEHICULO_NAME, o.getPlaca());
					listaVehiculo.add(mapVehiculo);
				}
			else {
				Intent intent = new Intent(getApplicationContext(), NuevoVehiculoActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
			return null;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = new ProgressDialog(NuevoViajeActivity.this);
			progressDialog.setMessage("Cargando vehículos. Espere un momento...");
			progressDialog.show();
		}

		@Override
		protected void onPostExecute(String s) {
			progressDialog.dismiss();
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					ListAdapter adapter = new SimpleAdapter(NuevoViajeActivity.this, listaVehiculo, R.layout.list_item, new String[] { Vehiculo.VEHICULO_ID, Vehiculo.VEHICULO_NAME }, new int[] { R.id.textViewId, R.id.textViewName });
					setListAdapter(adapter);
				}
			});
		}
	}

	private AdapterView.OnItemClickListener lstVehiculoClick = new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			idvehiculo = Integer.parseInt(((TextView) view.findViewById(R.id.textViewId)).getText().toString());
			AlertDialog.Builder alert = new AlertDialog.Builder(NuevoViajeActivity.this);
			alert.setTitle("Viaje - " + ((TextView) view.findViewById(R.id.textViewName)).getText().toString());
			alert.setMessage("Ingrese una descripción");
			alert.setView(txtDescripcion);
			alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					String des = txtDescripcion.getText().toString().equals("") ? "SIN DESCRIPCIÓN" : txtDescripcion.getText().toString();
					new guardarViaje().execute(des);
				}
			});
			alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
				}
			});
			alert.show();
		}
	};
	
	class guardarViaje extends AsyncTask<String, String, String> {
		@Override
		protected void onPostExecute(String s) {
			if (s.equals("0"))
				Toast.makeText(getApplicationContext(), "Error de conexión", Toast.LENGTH_LONG).show();
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... params) {
			String mac = ((WifiManager) getSystemService(Context.WIFI_SERVICE)).getConnectionInfo().getMacAddress();
			if (mac == null)
				mac = "00:00:00:00:00:00";
			Viaje o = new Viaje();
			o.setIdvehiculo(idvehiculo);
			String des = params[0];
			o.setDescripcion(des);
			o.setMac(mac);
			JSONHttpClient jsonHttpClient = new JSONHttpClient();
			o = (Viaje) jsonHttpClient.PostObject(ServiceUrl.VIAJE, o, Viaje.class);
			if (o == null)
				return "0";
			else {
				Intent intent = new Intent(NuevoViajeActivity.this, EnvioUbicacionActivity.class);
	            intent.putExtra(Viaje.VIAJE_ID, o.getIdviaje());
	            if (des.length() > 4 && des.substring(4).matches("\\d+"))
            		intent.putExtra("seg", Integer.parseInt(des.substring(4)));
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
			}
			return o.getIdviaje() + "";
		}
	}

}
